#!/usr/bin/env bash

err_report() {
  echo
  echo "***** errexit on line $(caller)" >&2
}

trap err_report ERR

set -e


echo Creating multiple products
SERVER=${SERVER:-localhost}
SERVER_URL=${SERVER_URL:-http://${SERVER}:8080}/oms-0.0.1-SNAPSHOT/service

curl --location --request POST ${SERVER_URL}'/product/register-list' \
--header 'Content-Type: application/json' \
--data-raw '[
    {
        "productId":"SM-S20-BLK",
        "name":"S20 Black",
        "description":"Samsung S20 Black",
        "manuf":"Samsung"
    },
    {
        "productId":"SM-S20-WHT",
        "name":"S20 White",
        "description":"Samsung S20 White",
        "manuf":"Samsung"
    },
    {
        "productId": "IPHN-12-64-MINI-BLK",
        "name" : "IPhone 12 Mini 1",
        "description" : "IPhone 12 Mini 64GB Black",
        "manuf" : "Apple"
    },
    {
        "productId": "IPHN-12-64-MINI-WHT",
        "name" : "IPhone 12 Mini 2",
        "description" : "IPhone 12 Mini 64GB White",
        "manuf" : "Apple"
    },
    {
        "productId": "IPHN-12-64-MINI-GRN",
        "name" : "IPhone 12 Mini 3",
        "description" : "IPhone 12 Mini 64GB Green",
        "manuf" : "Apple"
    }
]
'