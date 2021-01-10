from flask import Flask, request, Response, jsonify
import db
import bcrypt

app = Flask("poBop")

@app.route('/')
def home():
    """ Basically just here to check if server is running """
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


if __name__ == "__main__":
    app.run(port=33507)
