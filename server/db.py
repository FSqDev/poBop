import os
from flask import Flask
from flask_pymongo import pymongo
from main import app

client = pymongo.MongoClient("mongodb+srv://Admin:lZ3wBWPxo0tKiikW@cluster0.ssnyz.mongodb.net/appdb?retryWrites=true&w=majority")
db = client.get_database('flask_mongodb_atlas')
