from dotenv import load_dotenv
load_dotenv()

from flask import Flask, request, Response, jsonify
import db
import bcrypt
import os
from spoon import SpoonAPI
import requests
import json


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

    accounts = db.db["users"].find({"email": request.json["email"]})

    if accounts.count() == 0:
        return Response("Email is not registered", status=400)
    
    for account in accounts: # Should always just be one
        print(account)
        if bcrypt.checkpw(request.json["password"].encode('utf8'), account["password"]):
            return Response(str(account["_id"]), status=200)
        else:
            return Response("Wrong password", status=400)


@app.route('/products/getinfo', methods=['POST'])
def getProductName():
    """ 
    Converts a product's UPC code into the product's name using buycott's API 
    Expecting body { UPC: str }
    Returns body with the full name, an image and TODO ingredient it counts as, if found 
    """
    if "UPC" not in request.json:
        return Response("Expected parameter 'UPC' in body", status=400)

    body = {
        "barcode": request.json["UPC"],
        "access_token": os.environ["BUYCOTT_TOKEN"]
    }
    resp = requests.request(method='get', url='https://www.buycott.com/api/v4/products/lookup', json=body)
    as_dict = json.loads(resp.content) # Product count is theoretically one because UPC unique
    product_name = as_dict["products"][0]["product_name"]
    ret = {
        "name": product_name,
        "product_type": spoon_api.lookup_product(product_name),
        "image_url": as_dict["products"][0]["product_image_url"]
    }
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
