#!/usr/bin/env bash

err_report() {
  echo
  echo "***** errexit on line $(caller)" >&2
}

trap err_report ERR

set -e

SERVER=${SERVER:-localhost}
SERVER_URL=${SERVER_URL:-http://${SERVER}:8080}/order-1.0-SNAPSHOT/service
CURL=${CURL:-curl -sf}

ORDER_ID=${ORDER_ID:-`od -A n -l -N 7 /dev/urandom | tr -d " "`}
#due to a bug this has to be the same as order ID
ORDER_LINE_ID=${ORDER_ID}
SKU=${SKU:-`od -A n -l -N 2 /dev/urandom | tr -d " "`}

#create an order
cat order.json | jq ".customerOrderId = \"${ORDER_ID}\"" | jq ".orderLines[].customerSKU=\"${SKU}\"" | $CURL -X POST -H "Content-Type: application/json" -d @- $SERVER_URL/order

#get the order
$CURL $SERVER_URL/order/${ORDER_ID}

echo
echo
printf "\033[0;32mOK\033[0m\n"
