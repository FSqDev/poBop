import os
from flask import Flask
from flask_pymongo import pymongo

client = pymongo.MongoClient(os.environ["MONGO_CLIENT"])
db = client.get_database('flask_mongodb_atlas')
