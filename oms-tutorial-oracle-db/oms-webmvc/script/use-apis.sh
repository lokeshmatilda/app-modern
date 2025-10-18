#!/usr/bin/env bash 
##add -mx to the first line for debugging (see the CURL commands) 

err_report() {
  echo
  echo "***** errexit on line $(caller)" >&2
}

trap err_report ERR

set -e

SERVER=${SERVER:-localhost}
SERVER_URL=${SERVER_URL:-http://${SERVER}:8080}/oms-0.0.1-SNAPSHOT/service
CURL=${CURL:-curl -sf}

ORDER_ID=${ORDER_ID:-`od -A n -l -N 7 /dev/urandom | tr -d " "`}
ORDER_LINE_ID=${ORDER_LINE_ID:-`od -A n -l -N 7 /dev/urandom | tr -d " "`}
SKU=${SKU:-`od -A n -l -N 2 /dev/urandom | tr -d " "`}
CARD_TYPE=${1:-VISA}

#create an order
cat order.json | jq ".customerOrderId = \"${ORDER_ID}\"" | jq ".orderLines[].customerOrderId=\"${ORDER_ID}\"" | jq ".orderLines[].lineItemId=\"${ORDER_LINE_ID}\"" | jq ".orderLines[].customerSKU=\"${SKU}\"" | jq ".paymentInfo.cardType=\"${CARD_TYPE}\"" | $CURL -X POST -H "Content-Type: application/json" -d @- $SERVER_URL/order

#get the order
$CURL $SERVER_URL/order/${ORDER_ID}

#create inventory
cat inv.json | jq ".skuId=\"${SKU}\"" | $CURL -X POST -H "Content-Type: application/json" -d @- $SERVER_URL/inventory

#get inventory
$CURL $SERVER_URL/inventory/${SKU}

#create shipping record
cat shipping.json | jq ".skuId=\"${SKU}\"" | $CURL -X POST -H "Content-Type: application/json" -d @- $SERVER_URL/shipping

#get shipping record
$CURL $SERVER_URL/shipping/${SKU}

#find stores in zip code
$CURL $SERVER_URL/store/07470

#modify fulfillment shipping-to-pickup
cat mforder.json | jq ".customerOrderId = \"${ORDER_ID}\"" | $CURL -X PATCH -H "Content-Type: application/json" -d @- $SERVER_URL/modify/fulfillment/store/items/${ORDER_LINE_ID}

#modify fulfillment pickup-to-shipping
cat mforder.json | jq ".customerOrderId = \"${ORDER_ID}\"" | $CURL -X PATCH -H "Content-Type: application/json" -d @- $SERVER_URL/modify/fulfillment/shipping/items/${ORDER_LINE_ID}

./simpleapis.sh

echo
echo
printf "\033[0;32mOK\033[0m\n"

