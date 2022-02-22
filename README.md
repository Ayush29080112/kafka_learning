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

## 5) How to instantiate a Console Producer?
### Without key
      ./kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic
### With Key
      ./kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic --property "key.separator=-" --property "parse.key=true"

## 6)How to instantiate a Console Consumer?
### Without Key
      ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning
### With Key
      ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning -property "key.separator= - " --property "print.key=true"
### With Consumer Group
      ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --group <group-name>
## 7) List the topics in a cluster
      ./kafka-topics.sh --zookeeper localhost:2181 --list
### With Kafka 3.0 onwards
      ./kafka-topics.sh --bootstrap-server localhost:2181 --list
      
## 8) How to view consumer groups
      ./kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
      

