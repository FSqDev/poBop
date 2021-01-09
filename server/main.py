from flask import Flask, request, Response, jsonify
import db

app = Flask("poBop")

@app.route('/')
def home():
    """ Basically just here to check if server is running """
    return 'poBop server is running'


@app.route('/users/register', methods=['POST'])
def register():
    """ Securely registers a user """


@app.route("/testdb")
def test():
    db.db["collectionname"].insert_one({"param": "value"})
    return "Connected to the data base!"


if __name__ == "__main__":
    app.run(port=33507)
