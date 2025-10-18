#!/usr/bin/env bash

err_report() {
  echo
  echo "***** errexit on line $(caller)" >&2
}

trap err_report ERR

set -e

echo Creating multiple inventories
SERVER=${SERVER:-localhost}
SERVER_URL=${SERVER_URL:-http://${SERVER}:8080}/oms-0.0.1-SNAPSHOT/service

curl --location --request POST ${SERVER_URL}'/inventory/multi-create' \
--header 'Content-Type: application/json' \
--data-raw '[
    {
        "skuId":"SM-S20-BLK",
        "storeId": "11",
        "quantity": 20
    },
    {
        "skuId":"SM-S20-WHT",
        "storeId": "11",
        "quantity": 25
    },
    {
        "skuId": "IPHN-12-64-MINI-BLK",
        "storeId": "11",
        "quantity": 13
    },
    {
        "skuId": "IPHN-12-64-MINI-WHT",
        "storeId": "11",
        "quantity": 23
    },
    {
        "skuId": "IPHN-12-64-MINI-GRN",
        "storeId": "11",
        "quantity": 12
    }
]'