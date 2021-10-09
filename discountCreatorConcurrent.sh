#!/bin/sh
#set -e
APP_URL=http://quarkus-datagrid-challenge-user1-challenge6.apps.cluster-pjw6b.pjw6b.sandbox1032.opentlc.com
#APP_URL=http://localhost:8082

curl -i -H "Content-Type: application/json" ${APP_URL}/discounts/concurrent -d '{ "name": "PROMO10", "amount": 10, "enterprise": "ZARA", "type": "VALUE"}'
echo ---
curl -i -H "Content-Type: application/json" ${APP_URL}/discounts/concurrent -d '{ "name": "PROMO12", "amount": 20, "enterprise": "ALBACETEBANK", "type": "VALUE"}'
echo ---
curl -i -H "Content-Type: application/json" ${APP_URL}/discounts/concurrent -d '{ "name": "PROMO35", "amount": 10, "enterprise": "MERCADONO", "type": "PERCENT" }'
echo ---
echo try insert the same value
curl -i -H "Content-Type: application/json" ${APP_URL}/discounts/concurrent -d '{ "name": "PROMO35", "amount": 10, "enterprise": "MERCADONO", "type": "PERCENT" }'
echo 
echo ---
echo does not exist
curl -i ${APP_URL}/discounts/concurrent/consume/PROMO20
echo 
echo ---
curl -i ${APP_URL}/discounts/concurrent/consume/PROMO12
echo 
echo ---
curl -i ${APP_URL}/discounts/concurrent/consume/PROMO12
echo 
echo ---
curl -i ${APP_URL}/discounts/concurrent/consume/PROMO35
echo 
echo ---
curl -i ${APP_URL}/discounts/concurrent/VALUE
echo 
echo ---
curl -i ${APP_URL}/discounts/concurrent/PERCENT
echo 
echo ---
