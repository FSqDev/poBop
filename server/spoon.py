from flask import Flask, request, Response, jsonify
from pprint import pprint
import requests
import os
from typing import List
import pandas as pd


class SpoonAPI:
    def __init__(self, debug=True):
        self.debug = debug
        self.baseUrl = 'https://api.spoonacular.com'
        self.api_key = os.environ['SPOON_API_KEY']
        self.lookup_table = pd.read_csv('top-1k-ingredients.csv')
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
            'number': 5,
            'ingredients': ingredients,
            'apiKey': self.api_key,
            'ranking': 2,
            'ignorePantry': False
        }

        request_url = self.baseUrl + '/recipes/findByIngredients'
        res = requests.get(request_url, params=params).json()
        if self.debug:
            pprint(res)
        responses = []
        for ar in res:
            responses.append({
                'id': ar['id'],
                'image': ar['image'],
                'title': ar['title'],
                'likes': ar['likes'],
                # 'missingIngredients': ar['missedIngredients'],
                'numMissingIngredients': ar['missedIngredientCount'],

            })
        return Response(str({'recipes': responses}), status=200)

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
        return Response(str(response.json()), status=200)

    def lookup_product(self, product_name: str):
        max_match = -100
        best_guess = None
        for row in self.lookup_table.itertuples():
            item = row[1].split(';')[0]
            length = self._lcs(product_name, item)
            if length > max_match:
                max_match = length
                best_guess = item

        return best_guess if best_guess != None else "N/A"

    
    def _lcs(self, s1, s2):
        s1 = s1.lower()
        s2 = s2.lower()
        set1 = set(s1.split())
        set2 = set(s2.split())
        matches = len(set1.intersection(set2))
        if matches == 0:
            return -100
        return  matches - (len(set1 ^ set2))
        

