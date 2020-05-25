Kafka streaming with Spark and Flink example
===
Example project on how to use [Apache Kafka](https://kafka.apache.org) and streaming consumers, namely:
- Producer sending random number words to Kafka
- Consumer using Kafka to output received messages
- Streaming Consumer using [Apache Spark](https://spark.apache.org) to count words occurrences
- Streaming Consumer using [Apache Flink](https://flink.apache.org) to count words occurrences 

Requirements
---
- Docker
- Docker Compose
- Java 8
- Maven

Build
---
1. Build Java project
    ```
    mvn clean package
    ```
1. Build Docker image 
    ```
    docker build -t kafka-spark-flink-example .
    ```

Run
---
1. Start docker containers
    ```
    docker-compose up -d
    ```

Check
---
1. Check producer logs
    ```
    docker logs kafka-spark-flink-example_kafka-producer_1 -f
    ```
    
    Output should be similar to:
    ```
    wait-for-it.sh: zookeeper:32181 is available after 6 seconds
    wait-for-it.sh: waiting 30 seconds for kafka:9092
    wait-for-it.sh: kafka:9092 is available after 3 seconds
    16:57:58.820 [main] INFO  com.smartfactory.kafka.cli.Main - Kafka Topic: example
    16:57:58.827 [main] INFO  com.smartfactory.kafka.cli.Main - Kafka Server: kafka:9092
    16:57:58.827 [main] INFO  com.smartfactory.kafka.cli.Main - Zookeeper Server: zookeeper:32181
    16:57:58.837 [main] INFO  com.smartfactory.kafka.cli.Main - GOAL: producer
    CsvData: -> db24a1d0-23da-439c-b3a3-0840eed31fa1 |      ,machineID,datetime,volt,rotate,pressure,vibration,model,age,error1,error2,error3,error4,comp1,comp2,comp3,comp4,fail_comp1,  fail_comp2,fail_comp3,fail_comp4,last_maint_dt
   16:58:00.392 [kafka-producer-network-thread | KafkaProducerExample] INFO  com.smartfactory.kafka.producer.KafkaProducerExample - Sent (,machineID,datetime,volt,rotate,pressure,vibration,model,age,error1,error2,error3,error4,comp1,comp2,comp3,comp4,fail_comp1,fail_comp2,fail_comp3,fail_comp4,last_maint_dt) to topic example @ 1590425880083.
   CsvData: -> 7a5384f9-e98f-4524-8776-c46442cecaed | 0,1,2015-01-01 06:00:00,176.217853015625,418.504078221616,113.07793546208299,45.0876857639276,model3,18,0,0,0,0,0,0,0,0,0,0,0,0,
   16:58:05.119 [kafka-producer-network-thread | KafkaProducerExample] INFO     com.smartfactory.kafka.producer.KafkaProducerExample - Sent (0,1,2015-01-01   06:00:00,176.217853015625,418.504078221616,113.07793546208299,45.0876857639276,model3,18,0,0,0,0,0,0,0,0,0,0,0,0,) to topic example @ 1590425885095.

    ```
1. Check consumer with Spark logs
    ```
    docker logs kafka-spark-flink-example_kafka-consumer-spark_1 -f
    ```
    
    Output should be similar to:
    ```
    Time: 1590434385000 ms
    -------------------------------------------
    (1699,1,2015-03-12,1)
    (22:00:00,152.665627430332,464.385575189557,100.27031798843599,44.7902437715049,model3,18,0,0,0,0,0,0,0,0,0,0,0,0,,1)
    ```
1. Check consumer with Flink logs
    ```
    docker logs kafka-spark-flink-example_kafka-consumer-flink_1 -f
    ```
    
    Output should be similar to:
    ```
   5> (13,124)
   5> (720066317184,1)
   6> (0,20436)
   6> (0,20437)
   6> (0,20438)
   6> (0,20439)
   6> (0,20440)
   6> (0,20441)
   6> (0,20442)
   6> (0,20443)
    ```
   
Kafka Web UI
---
Kafka Manager Web UI available at [http://localhost:9000]().

Spark Web UI
---
Spark Web UI available at [http://localhost:4040]().

Stop
---
1. Stop docker containers
    ```
    docker-compose down
    ```
