from flask import Flask, request, Response, jsonify
from pprint import pprint
import requests
import os
from typing import List


class SpoonAPI:
    def __init__(self, debug=True):
        self.debug = debug
        self.baseUrl = 'https://api.spoonacular.com'
        self.api_key = os.environ['SPOON_API_KEY']
        # self.diets = [
        #     'vegetarian',
        #     'vegan',
        #     'ketogenic',
        #     'paleo',
        #     'gluten free'
        # ]
        # self.intolerences = {
        #     'dairy',
        #     'egg',
        #     'gluten',
        #     'grain',
        #     'peanut',
        #     'seafood',
        #     'shellfish',
        #     'soy',
        #     'sulfite',
        #     'tree nut',
        #     'wheat'
        # }

    def get_random_recipe(self, tags, amount):
        request_url = self.baseUrl + '/recipes/random'
        params = {
            'number': amount,
            'tags': tags,
            'apiKey': self.api_key
        }
        reponse = requests.get(request_url, params=params)
        pprint(reponse.json())

    def get_recipe(self, ingredients: str):

        params = {
            'number': 1,
            'ingredients': ingredients,
            'apiKey': self.api_key,
            'ranking': 2,
            'ignorePantry': False
        }

        request_url = self.baseUrl + '/recipes/findByIngredients'

        ar = requests.get(request_url, params=params).json()[0]
        if self.debug:
            pprint(ar)

        response = {
            'id': ar['id'],
            'image': ar['image'],
            'title': ar['title'],
            'likes': ar['likes'],
            'missingIngredients': ar['missedIngredients'],
            'numMissingIngredients': ar['missedIngredientCount'],
            'summary': self.get_recipe_summary(ar['id'])['summary']

        }
        return Response(str(response), status=200)

    def get_recipe_instructions(self, recipe_id: int):
        request_url = self.baseUrl + f'/recipes/{recipe_id}/analyzedInstructions'
        params = {
            'apiKey': self.api_key,
            'stepBreakdown': True 
        }
        response = requests.get(request_url, params=params)
        if self.debug:
            pprint(response.json())
        return Response(str(response.json()), status=200)

    def get_recipe_summary(self, recipe_id):
        request_url = self.baseUrl + f'/recipes/{recipe_id}/summary'
        params = {
            'apiKey': self.api_key
        }
        response = requests.get(request_url, params=params)
        return response.json()

