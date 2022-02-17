# kafka_learning

## 1) Start up the Zookeeper.  
      ./zookeeper-server-start.sh ../config/zookeeper.properties
## 2) Configs to be done for Kafka broker
    Add the below properties in the server.properties
        listeners=PLAINTEXT://localhost:9092      ## used to assign a port to the broker
        auto.create.topics.enable=false           ## used to stop auto creation of topic, in case the topic does not exist
## 3)Start up the Kafka Broker
      ./kafka-server-start.sh ../config/server.properties
