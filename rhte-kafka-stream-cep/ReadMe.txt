0 - Have KAFKA Started

# ZOOKEEPER FIRST

[source, bash]
----
bin/zookeeper-server-start.sh config/zookeeper.properties > zookeeper-server-start.log 2>&1 &
----

# KAFKA NODES AFTER

[source, bash]
----
bin/kafka-server-start.sh config/server.properties  > kafka-server-start.log 2>&1 &
----

# STOP NODES
[source, bash]
----
kafka-server-stop.sh
zookeeper-server-stop.sh
----


1- delete all the old topics

bin/kafka-topics.sh --delete --topic <<topic name>> --zookeeper localhost:2181

   
bin/kafka-topics.sh --delete --topic cc-trans --zookeeper localhost:2181
bin/kafka-topics.sh --delete --topic illegal-trans --zookeeper localhost:2181
bin/kafka-topics.sh --delete --topic by-cc-trans --zookeeper localhost:2181
bin/kafka-topics.sh --delete --topic processed --zookeeper localhost:2181

2- Create the new topics by running 

rhte-kafka-producer/create-topics.sh 

3- Run the producer

rhte-kafka-producer/mvn spring-boot:run

4- Run the consumer
rhte-kafka-consumer/mvn spring-boot:run
