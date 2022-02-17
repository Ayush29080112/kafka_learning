# kafka_learning

## 1) Start up the Zookeeper.  
      ./zookeeper-server-start.sh ../config/zookeeper.properties
## 2) Configs to be done for Kafka broker
    Add the below properties in the server.properties
        listeners=PLAINTEXT://localhost:9092      ## used to assign a port to the broker
        auto.create.topics.enable=false           ## used to stop auto creation of topic, in case the topic does not exist
## 3)Start up the Kafka Broker
      ./kafka-server-start.sh ../config/server.properties

## 4) Create a topic
       ./kafka-topics.sh --create --topic test-topic -zookeeper localhost:2181 --replication-factor 1 --partitions 4
       
### With Kafka 3.0 onwards
      ./kafka-topics.sh --create --topic test-topic --replication-factor 1 --partitions 4 --bootstrap-server localhost:9092

