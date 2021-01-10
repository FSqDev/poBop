from dotenv import load_dotenv
load_dotenv()

from flask import Flask, request, Response, jsonify
import db
from bson.objectid import ObjectId
import bcrypt
import os
from spoon import SpoonAPI
import requests
import json
import utils


app = Flask("app")
spoon_api = SpoonAPI()

@app.route('/')
def home():
    """ Basically just here to check if server is running """
    return 'poBop server is running'


@app.route('/users/register', methods=['POST'])
def register():
    """ 
    Registers new user account
    Expecting body { email: str, password: str } 
    Returns 200 if successful, 400 if email already registered 
    """
    if "email" not in request.json:
        return Response("Expected parameter 'email' in body", status=400)
    if "password" not in request.json:
        return Response("Expected parameter 'password' in body", status=400)

    if db.db["users"].find({"email": request.json["email"]}).count() != 0:
        return Response("Email already registered", status=400)

    salt = bcrypt.gensalt()
    password_hashed = bcrypt.hashpw(request.json["password"].encode('utf8'), salt)
    ret = db.db["users"].insert_one({
        "email": request.json["email"], 
        "password": password_hashed,
        "products": []
    })
    return Response("Registration success", status=200) # TODO return id as well


@app.route('/users/login', methods=['POST'])
def login():
    """ 
    Attempts to login
    Expecting body { email: str, password: str } 
    Returns user mongoID if successful, else error 400 
    """
    if "email" not in request.json:
        return Response("Expected parameter 'email' in body", status=400)
    if "password" not in request.json:
        return Response("Expected parameter 'password' in body", status=400)

    accounts = db.db["users"].find({"email": request.json["email"]})

    if accounts.count() == 0:
        return Response("Email is not registered", status=400)
    
    for account in accounts: # Should always just be one
        print(account)
        if bcrypt.checkpw(request.json["password"].encode('utf8'), account["password"]):
            return Response(str(account["_id"]), status=200)
        else:
            return Response("Wrong password", status=400) # TODO wrap in json id


@app.route('/users/products', methods=['GET'])
def getUserproducts():
    """
    Gets the list representing the products a user currently has
    Expecting parameter id (the MongoDB ID)
    Returns json with the products under 'products'
    """
    userID = request.args.get("id")
    if not userID:
        return Response("Expected parameter 'id' in request", status=400)
    
    resp = db.db["users"].find_one({"_id": ObjectId(userID)})
    if not resp:
        return Response("Invalid user ID", status=400)
    return jsonify({
        "products": resp["products"]
    })


@app.route('/users/products', methods=['PUT'])
def addUserProducts():
    """
    Adds a list of new products to the user's db
    Expecting body { id: str, products: list }
    where each product is of form { id: str, barcode: str }
    """
    if "id" not in request.json:
        return Response("Expected parameter 'id' in body", status=400)
    if "products" not in request.json:
        return Response("Expected parameter 'products' in body", status=400)
    
    for product in request.json["products"]:
        info = utils.product_info(product["barcode"], spoon_api)
        db.db["users"].find_one_and_update(
            {'_id': ObjectId(request.json["id"])},
            {'$push': {'products': {
                '_id': ObjectId(product["id"]),
                'name': info["name"],
                'product_type': info["product_type"],
                'image_url': info["image_url"]
            }}}
        )

    return Response("Completed with no errors", status=200)


@app.route('/products/getinfo', methods=['POST'])
def getProductInfo():
    """ 
    Converts a product's UPC code into the product's name using buycott's API 
    Expecting body { UPC: str }
    Returns body with the full name, an image and product type it counts as, if found 
    """
    if "UPC" not in request.json:
        return Response("Expected parameter 'UPC' in body", status=400)

    ret = utils.product_info(request.json["UPC"], spoon_api)
    return jsonify(ret)


@app.route('/recipes', methods=['GET'])
def get_recipes():
    """
    Returns a recipe that includes as many of the given ingredients as possible.
    Prioritizes recipes that don't contain ingredients that are missing from the given list

    args:
        ingredients: a string of comma seperated items that we want to be included in the recipe
    """
    try:
        # eg. spoon_api.get_recipe(['chicken', 'tuna', 'chocolate'])
        return spoon_api.get_recipe(request.args['ingredients'])
    except Exception as e:
        return Response('Error occured while fetching recipes ' + str(e), status=404)


if __name__ == "__main__":
    port = int(os.environ.get('PORT', 5000))
    app.run(port=port)
