#!/usr/bin/env bash

err_report() {
  echo
  echo "***** errexit on line $(caller)" >&2
}

trap err_report ERR

set -e

echo Creating multiple orders
SERVER=${SERVER:-localhost}
SERVER_URL=${SERVER_URL:-http://${SERVER}:8080}/oms-0.0.1-SNAPSHOT/service

curl --location --request POST ${SERVER_URL}'/order/multi' \
--header 'Content-Type: application/json' \
--data-raw '[
    {
        "customerOrderId": "1",
        "primaryPhone": "9525944805",
        "customerEmailId": "as@yahoo.com",
        "orderStatus": "SUBMITTED",
        "firstName": "Anand",
        "orderDate": null,
        "profileId": null,
        "lastName": null,
        "entryType": null,
        "billToAddress": {
            "billToAddressId": "101",
            "firstName": "Anand",
            "lastName": "sudhakar",
            "address1": null,
            "address2": null,
            "city": "BLOOMINGTON",
            "state": "MN",
            "zipCode": "55344"
        },
        "shipToAddress": {
            "shipToAddressId": "102",
            "firstName": "Anand",
            "lastName": "sudhakar",
            "address1": null,
            "address2": null,
            "city": "BLOOMINGTON",
            "state": "MN",
            "zipCode": "55344"
        },
        "orderLines": [
            {
                "lineItemId": "100",
                "customerOrderId": "1",
                "shipNode": null,
                "shipNodeDescription": null,
                "levelOfService": null,
                "primeLineNumber": "1",
                "subLineNumber": "1",
                "customerSKU": "SM-S20-BLK",
                "skuDescription": null,
                "estimatedArrivalDate": null,
                "status": null,
                "reshippedBefore": null,
                "shipToAddress": {
                    "shipToAddressId": "102",
                    "firstName": "Anand",
                    "lastName": "sudhakar",
                    "address1": null,
                    "address2": null,
                    "city": "BLOOMINGTON",
                    "state": "MN",
                    "zipCode": "55344"
                },
                "charges": {
                    "lineChargeId": "601",
                    "totalCharges": 10.0
                }
            }
        ],
        "paymentInfo": {
            "paymentId": "234",
            "paymentStatus": "SUCCESS",
            "cardType": "VISA",
            "authorizedAmount": 43.0,
            "collectedAmount": 5.0
        },
        "charges": {
            "chargesId": "601",
            "lineSubTotal": null,
            "totalCharges": 10.0,
            "salesTax": null,
            "grandTotal": null
        }
    },
    {
        "customerOrderId": "2",
        "primaryPhone": "9525944805",
        "customerEmailId": "as@yahoo.com",
        "orderStatus": "SUBMITTED",
        "firstName": "Anand",
        "orderDate": null,
        "profileId": null,
        "lastName": null,
        "entryType": null,
        "billToAddress": {
            "billToAddressId": "101",
            "firstName": "Anand",
            "lastName": "sudhakar",
            "address1": null,
            "address2": null,
            "city": "BLOOMINGTON",
            "state": "MN",
            "zipCode": "55344"
        },
        "shipToAddress": {
            "shipToAddressId": "102",
            "firstName": "Anand",
            "lastName": "sudhakar",
            "address1": null,
            "address2": null,
            "city": "BLOOMINGTON",
            "state": "MN",
            "zipCode": "55344"
        },
        "orderLines": [
            {
                "lineItemId": "200",
                "customerOrderId": "2",
                "shipNode": null,
                "shipNodeDescription": null,
                "levelOfService": null,
                "primeLineNumber": "1",
                "subLineNumber": "1",
                "customerSKU": "SM-S20-WHT",
                "skuDescription": null,
                "estimatedArrivalDate": null,
                "status": null,
                "reshippedBefore": null,
                "shipToAddress": {
                    "shipToAddressId": "102",
                    "firstName": "Anand",
                    "lastName": "sudhakar",
                    "address1": null,
                    "address2": null,
                    "city": "BLOOMINGTON",
                    "state": "MN",
                    "zipCode": "55344"
                },
                "charges": {
                    "lineChargeId": "602",
                    "totalCharges": 11.0
                }
            }
        ],
        "paymentInfo": {
            "paymentId": "234",
            "paymentStatus": "SUCCESS",
            "cardType": "VISA",
            "authorizedAmount": 43.0,
            "collectedAmount": 5.0
        },
        "charges": {
            "chargesId": "602",
            "lineSubTotal": null,
            "totalCharges": 11.0,
            "salesTax": null,
            "grandTotal": null
        }
    },
    {
        "customerOrderId": "3",
        "primaryPhone": "9525944805",
        "customerEmailId": "as@yahoo.com",
        "orderStatus": "SUBMITTED",
        "firstName": "Anand",
        "orderDate": null,
        "profileId": null,
        "lastName": null,
        "entryType": null,
        "billToAddress": {
            "billToAddressId": "101",
            "firstName": "Anand",
            "lastName": "sudhakar",
            "address1": null,
            "address2": null,
            "city": "BLOOMINGTON",
            "state": "MN",
            "zipCode": "55344"
        },
        "shipToAddress": {
            "shipToAddressId": "102",
            "firstName": "Anand",
            "lastName": "sudhakar",
            "address1": null,
            "address2": null,
            "city": "BLOOMINGTON",
            "state": "MN",
            "zipCode": "55344"
        },
        "orderLines": [
            {
                "lineItemId": "300",
                "customerOrderId": "3",
                "shipNode": null,
                "shipNodeDescription": null,
                "levelOfService": null,
                "primeLineNumber": "1",
                "subLineNumber": "1",
                "customerSKU": "IPHN-12-64-MINI-BLK",
                "skuDescription": null,
                "estimatedArrivalDate": null,
                "status": null,
                "reshippedBefore": null,
                "shipToAddress": {
                    "shipToAddressId": "102",
                    "firstName": "Anand",
                    "lastName": "sudhakar",
                    "address1": null,
                    "address2": null,
                    "city": "BLOOMINGTON",
                    "state": "MN",
                    "zipCode": "55344"
                },
                "charges": {
                    "lineChargeId": "603",
                    "totalCharges": 12.0
                }
            }
        ],
        "paymentInfo": {
            "paymentId": "234",
            "paymentStatus": "SUCCESS",
            "cardType": "VISA",
            "authorizedAmount": 43.0,
            "collectedAmount": 5.0
        },
        "charges": {
            "chargesId": "603",
            "lineSubTotal": null,
            "totalCharges": 12.0,
            "salesTax": null,
            "grandTotal": null
        }
    }
]
'