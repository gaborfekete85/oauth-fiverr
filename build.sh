#!/bin/bash

echo "Building the gateway-service"
cd api-gateway
#./mvnw clean install
mvn clean install
docker image build -t fiverr/okta-gateway-service .
cd ..

echo "Building eureka-service"
cd eureka
#./mvnw clean install
mvn clean install
docker image build -t fiverr/okta-eureka-server .
cd ..

echo "Building Stock Service"
cd stock-service
#./mvnw clean install
mvn clean install
docker image build -t fiverr/okta-stock-service .
cd ..

echo "Building Exchange Service"
cd exchange-service
#./mvnw clean install
mvn clean install
docker image build -t fiverr/okta-exchange-service .
cd ..

echo "Building hystrix-dashboard"
cd hystrix-dashboard
#./mvnw clean install
mvn clean install
docker image build -t fiverr/okta-hystrix-dashboard .
cd ..