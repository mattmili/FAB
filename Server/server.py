#!flask/bin/python

from flask import Flask, jsonify, request
from lxml import html
import requests
import urllib2
import json
import os
import config
import ujson 

app = Flask(__name__)

PORT=int(os.environ.get('PORT', 5000))

# http://localhost:5000/getbook?items=keywords

@app.route('/getbook', methods=['GET'])
def get_reviews():
    # get items as url arguments
    response = request.args.get('items')
    items = str(response).replace(" ", "+")

    url = config.idreambooks_API_endpoint + items + "&key=" + config.idreambooks_key

    json_data = ujson.load(urllib2.urlopen(url))
    return jsonify(json_data)

if __name__ == '__main__':
    app.debug=True
    app.run(host='0.0.0.0', port=PORT)
