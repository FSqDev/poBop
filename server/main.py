# Environment Variables
from dotenv import load_dotenv
load_dotenv()

# Database access
import db
from bson.objectid import ObjectId

# General
from flask import Flask, request, Response, jsonify
import os
import requests
import json
import bcrypt
import re

# Custom wrappers
from spoon import SpoonAPI
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
    if not re.search('^[a-z0-9]+[\._]?[a-z0-9]+[@]\w+[.]\w{2,3}$', request.json["email"].lower()):
        return Response("Email was not valid", status=400)
    if "password" not in request.json:
        return Response("Expected parameter 'password' in body", status=400)

    if db.db["users"].find({"email": request.json["email"].lower()}).count() != 0:
        return Response("Email already registered", status=400)

    salt = bcrypt.gensalt()
    password_hashed = bcrypt.hashpw(request.json["password"].encode('utf8'), salt)
    ret = db.db["users"].insert_one({
        "email": request.json["email"].lower(), 
        "password": password_hashed,
        "products": []
    })
    return jsonify({
        "id": str(ret.inserted_id)
    })
    return Response("Registration success", status=200)


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

    accounts = db.db["users"].find({"email": request.json["email"].lower()})

    if accounts.count() == 0:
        return Response("Email is not registered", status=400)
    
    for account in accounts: # Should always just be one
        if bcrypt.checkpw(request.json["password"].encode('utf8'), account["password"]):
            return jsonify({
                "id": str(account["_id"])
            })
        else:
            return Response("Wrong password", status=400)


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
    
    ret = []
    for product in resp["products"]:
        new = {
            "_id": str(product["_id"]),
            "name": product["name"],
            "product_type": product["product_type"],
            "image_url": product["image_url"],
            "expiry_date": product["expiry_date"]
        }
        ret.append(new)

    return jsonify({
        "products": ret
    })


@app.route('/users/products', methods=['PUT'])
def addUserProducts():
    """
    Adds a list of new products to the user's db
    Expecting body { id: str, products: list }
    where each product is of form { id: str, barcode: str, expiry_date: str }
    OR of form { id: str, name: str, expiry_date: str }
    """
    if "id" not in request.json:
        return Response("Expected parameter 'id' in body", status=400)
    if "products" not in request.json:
        return Response("Expected parameter 'products' in body", status=400)
    
    for product in request.json["products"]:
        if db.db["users"].products.find(
            {"_id": ObjectId(product["id"])}
        ).count != 1:
            continue

        if "barcode" in product:
            info = utils.product_info(product["barcode"], spoon_api)
            db.db["users"].find_one_and_update(
                {'_id': ObjectId(request.json["id"])},
                {'$push': {'products': {
                    '_id': ObjectId(product["id"]),
                    'name': info["name"],
                    'product_type': info["product_type"],
                    'image_url': info["image_url"],
                    'expiry_date': product["expiry_date"]
                }}}
            )
        else:
            db.db["users"].find_one_and_update(
                {'_id': ObjectId(request.json["id"])},
                {'$push': {'products': {
                    '_id': ObjectId(product["id"]),
                    'name': product["name"],
                    'product_type': spoon_api.lookup_product(product["name"]),
                    'image_url': "",
                    'expiry_date': product["expiry_date"]
                }}}
            )

    return Response("Completed with no errors", status=200)


@app.route('/users/products', methods=['DELETE'])
def deleteUserProduct():
    """
    Deletes a product from a user's db by id
    Expecting body { user_id: str, product_id: str }
    Returns if it doesn't crash : ^ )
    """
    if "user_id" not in request.json:
        return Response("Expected parameter 'user_id' in body", status=400)
    if "product_id" not in request.json:
        return Response("Expected parameter 'product_id' in body", status=400)

    db.db["users"].find_one_and_update(
        {'_id': ObjectId(request.json["user_id"])},
        {'$pull': {'products': {
            '_id': ObjectId(request.json["product_id"]),
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
    if 'ingredients' not in request.args:
        return Response("Expected parameter 'ingredients'", status=400)
    try:
        # eg. spoon_api.get_recipe(['chicken', 'tuna', 'chocolate'])
        return spoon_api.get_recipe(request.args['ingredients'])
    except Exception as e:
        return Response('Error occured while fetching recipes ' + str(e), status=404)


if __name__ == "__main__":
    port = int(os.environ.get('PORT', 5000))
    app.run(port=port)
