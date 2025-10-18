#!/usr/bin/env bash

err_report() {
  echo
  echo "***** errexit on line $(caller)" >&2
}

trap err_report ERR

set -e


echo Find inventory by product id
SERVER=${SERVER:-localhost}
SERVER_URL=${SERVER_URL:-http://${SERVER}:8080}/oms-0.0.1-SNAPSHOT/service

curl --location --request GET ${SERVER_URL}'/inventory/IPHN-12-64-MINI-BLK' \
--data-raw ''

echo Find order lines by product id
curl --location --request GET ${SERVER_URL}'/product/orderlines/SM-S20-BLK' \
--data-raw ''

echo 
echo Find product by id
curl --location --request GET ${SERVER_URL}'/product/IPHN-12-64-MINI-WHT' \
--data-raw ''

echo 
echo Charges for product 
curl --location --request GET ${SERVER_URL}'/product/charges/SM-S20-BLK' \
--data-raw ''

echo 
echo Find product by description
curl --location --request GET ${SERVER_URL}'/product/desc-includes/green' \
--data-raw ''

echo 
echo Find inventory by product description
curl --location --request GET ${SERVER_URL}'/product/inv-desc/iphone' \
--data-raw ''

echo 
echo Find product by inventory id
curl --location --request GET ${SERVER_URL}'/product/inv/IPHN-12-64-MINI-WHT' \
--data-raw ''

echo 
echo Find product by name
curl --location --request GET ${SERVER_URL}'/product/name/S20%20White' \
--data-raw ''

echo 
echo Find all products
curl --location --request GET ${SERVER_URL}'/product/all' \
--data-raw ''