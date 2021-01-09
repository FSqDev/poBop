import os
from flask import Flask
from flask_pymongo import pymongo
from main import app

with open("./.mongokey", "r") as ENV:
    for line in ENV:
        os.environ["MONGOKEY"] = line

client = pymongo.MongoClient(os.environ["MONGOKEY"])
db = client.get_database('flask_mongodb_atlas')
user_collection = pymongo.collection.Collection(db, 'user_collection')
