#!/bin/bash

docker-compose -p cluster up -d

sleep 10

docker exec cluster_rmq02_1 rabbitmqctl stop_app
docker exec cluster_rmq02_1 rabbitmqctl reset
docker exec cluster_rmq02_1 rabbitmqctl join_cluster rabbit@rmq01
docker exec cluster_rmq02_1 rabbitmqctl start_app

docker exec cluster_rmq03_1 rabbitmqctl stop_app
docker exec cluster_rmq03_1 rabbitmqctl reset
docker exec cluster_rmq03_1 rabbitmqctl join_cluster rabbit@rmq01
docker exec cluster_rmq03_1 rabbitmqctl start_app

docker exec cluster_rmq01_1 rabbitmqctl set_policy haq-policy \
    "^haq*" '{"ha-mode": "exactly", "ha-params": 3, "ha-sync-batch-size": 3, "ha-sync-mode": "automatic"}' \
    --priority 1 \
    --apply-to all

docker exec cluster_rmq01_1 rabbitmqadmin declare queue name=haq1 durable=true -u admin -p admin
docker exec cluster_rmq01_1 rabbitmqadmin declare exchange name=hae1 type=direct -u admin -p admin
docker exec cluster_rmq01_1 rabbitmqadmin declare binding source=hae1 destination=haq1 -u admin -p admin