#/bin/bash

ISSUER_URI=https://feketegabor.okta.com/oauth2/default \
INTROSPECT_URL=https://feketegabor.okta.com/oauth2/default/v1/introspect?token_type_hint=access_token&token=%s \
OKTA_CLIENT_ID=0oabsgpx3c1mF4HJx696 \
OKTA_CLIENT_SECRET=2wUqjphORGsm2Ldi63H3dw8NlUL2knAe6upXTcDv \
EXCHANGE_CLIENT_ID=0oabtaqwv7wsP2PdC696 \
EXCHANGE_CLIENT_SECRET=B0IO0ubogBtn84ie-ahlwt_H4s4l05ZCeUluzGTB \
docker-compose up