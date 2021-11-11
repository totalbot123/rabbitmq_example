#!/bin/bash

docker exec cluster_rmq01_1 rabbitmqadmin delete queue name=haq1 -u admin -p admin
docker exec cluster_rmq01_1 rabbitmqadmin delete exchange name=hae1 -u admin -p admin
docker exec cluster_rmq01_1 rabbitmqctl clear_policy haq-policy

docker-compose -p cluster stop