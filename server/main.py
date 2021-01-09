from flask import Flask, request, Response, jsonify

app = Flask("poBop")

@app.route('/')
def home():
    """ Basically just here to check if server is running """
    return 'poBop server is running'

if __name__ == "__main__":
    app.run(port=33507)
