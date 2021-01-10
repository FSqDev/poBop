from flask import Flask, request, Response, jsonify
import db
import bcrypt
import os
from spoon import SpoonAPI

app = Flask("app")
spoon_api = SpoonAPI()

@app.route('/')
def home():
    """ Basically just here to check if server is running """
    # spoon_api.get_recipe(['chicken', 'tuna', 'chocolate'])
    return 'poBop server is running'


@app.route('/users/register', methods=['POST'])
def register():
    """ Registers new user account
    Expecting body { email: str, password: str } """
    if db.db["users"].find({"email": request.json["email"]}).count() != 0:
        return Response("Email already registered", status=400)

    salt = bcrypt.gensalt()
    password_hashed = bcrypt.hashpw(request.json["password"].encode('utf8'), salt)
    ret = db.db["users"].insert_one({"email": request.json["email"], "password": password_hashed})
    return Response("Registration success", status=200)


@app.route('/users/login', methods=['POST'])
def login():
    """ Attempts to login
    Expecting body { email: str, password: str } """
    accounts = db.db["users"].find({"email": request.json["email"]})

    if accounts.count() == 0:
        return Response("Email is not registered", status=400)
    
    for account in accounts: # Should always just be one
        print(account)
        if bcrypt.checkpw(request.json["password"].encode('utf8'), account["password"]):
            return Response(str(account["_id"]), status=200)
        else:
            return Response("Wrong password", status=400)


@app.route('/recipes', methods=['GET'])
def get_recipes():
    """
    Returns a recipe that includes as many of the given ingredients as possible.
    Prioritizes recipes that don't contain ingredients that are missing from the given list

    args:
        ingredients: a string of comma seperated items that we want to be included in the recipe
    """
    try:
        return spoon_api.get_recipe(request.args['ingredients'])
    except Exception as e:
        return Response('Error occured while fetching recipes ' + str(e), status=404)


if __name__ == "__main__":
    port = int(os.environ.get('PORT', 5000))
    app.run(port=port)
