#!/bin/sh
#set -e

curl -i -H "Content-Type: application/json" http://localhost:8082/discounts -d '{ "name": "PROMO10", "amount": 10, "enterprise": "ZARA", "type": "VALUE"}'
echo ---
curl -i -H "Content-Type: application/json" http://localhost:8082/discounts -d '{ "name": "PROMO12", "amount": 20, "enterprise": "ALBACETEBANK", "type": "VALUE"}'
echo ---
curl -i -H "Content-Type: application/json" http://localhost:8082/discounts -d '{ "name": "PROMO35", "amount": 10, "enterprise": "MERCADONO", "type": "PERCENT" }'
echo ---
echo try insert the same value
curl -i -H "Content-Type: application/json" http://localhost:8082/discounts -d '{ "name": "PROMO35", "amount": 10, "enterprise": "MERCADONO", "type": "PERCENT" }'
echo 
echo ---
echo does not exist
curl -i http://localhost:8082/discounts/consume/PROMO20
echo 
echo ---
curl -i http://localhost:8082/discounts/consume/PROMO12
echo 
echo ---
curl -i http://localhost:8082/discounts/consume/PROMO12
echo 
echo ---
curl -i http://localhost:8082/discounts/consume/PROMO35
echo 
echo ---
curl -i http://localhost:8082/discounts/VALUE
echo 
echo ---
curl -i http://localhost:8082/discounts/PERCENT
echo 
echo ---