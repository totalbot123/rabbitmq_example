# RabbitMQ example

Pet project for learning understanding basics of message brokers with RabbitMQ and Java (Spring).

# Usage

* Deploy HA cluster with `./deploy_rabbitmq_cluster.sh`.
* Run Sender project, either directly from IDE or by using Maven (`mvn clean package && java -jar Sender-0.0.1-SNAPSHOT.jar`)
* Run Listener project, either directly from IDE or by using Maven (`mvn clean package && java -jar Listener-0.0.1-SNAPSHOT.jar`)

## Testing HA of the cluster

Stop one or two instances of the cluster (preferably let instance 1 or 2 alive since they have management console). Example:

`docker stop cluster_rmq01_1 && docker stop cluster_rmq03_1`

Observe that messages are still being sent to queue.  
Restart previously stopped RMQ instances:  

`docker start cluster_rmq01_1 && docker start cluster_rmq03_1`

Observe management console to see that these instances are joining the cluster.
# Components

### Java (Spring)

Java code consists on two Spring/Maven projects for Publisher/Listener. These are sharing common classes from Commons project.  
Publisher(Sender) is sending 100k messages with 300ms delay in-between messages. Listener is consuming these from RabbitMQ.

### Docker (docker-compose)

Docker compose file defines three instances of RabbitMQ that create single highly-available cluster. Two of these instances expose management console. Note: RABBITMQ_ERLANG_COOKIE is deprecated, but is used due to ease of use.

### Shell scripts

There are two shell scripts: one for deploying the cluster, creating queue, exchange and binding between the two, as well as policy for creating HA (Mirrored) queue; and other for deleting queue, exchange and policy and stopping the cluster.
## Authors

* **Milan Jecmenica** - *Initial work* - [totalbot123](https://github.com/totalbot123)
