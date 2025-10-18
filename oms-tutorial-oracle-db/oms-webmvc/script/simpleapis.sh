#!/usr/bin/env bash
err_report() {
  echo
  echo "***** errexit on line $(caller)" >&2
}

trap err_report ERR

set -e
	
./createProducts.sh
./createInventories.sh
./createOrders.sh
./findapis.sh
