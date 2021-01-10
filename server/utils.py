import requests
import os
import json
from spoon import SpoonAPI

def product_info(upc: str, spoon_api: SpoonAPI) -> dict:
    """
    Converts a product's UPC code into the product's name using buycott's API 
    Returns body with the full name, an image and product type it counts as, if found
    """
    body = {
        "barcode": upc,
        "access_token": os.environ["BUYCOTT_TOKEN"]
    }
    resp = requests.request(method='get', url='https://www.buycott.com/api/v4/products/lookup', json=body)
    as_dict = json.loads(resp.content) # Product count is theoretically one because UPC unique
    product_name = as_dict["products"][0]["product_name"]
    return {
        "name": product_name,
        "product_type": spoon_api.lookup_product(product_name),
        "image_url": as_dict["products"][0]["product_image_url"]
    }
