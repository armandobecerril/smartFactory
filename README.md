Smart Factory & Operations 
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
    docker build -t smart-factory-kafka-spark-flink .
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
    docker logs smart-factory-kafka-spark-flink_kafka-producer-1 -f
    ```
    
    Output should be similar to:
    ```
    [main] INFO  KafkaProducerExample - Sent (74b23319-084c-4309-80a7-c0d6f107a092, eight) to topic example @ 1525127107909.
    ```
1. Check consumer with Spark logs
    ```
    docker logs smart-factory-kafka-spark-flink_kafka-consumer-spark-1 -f
    ```
    
    Output should be similar to:
    ```
    (two,3)
    (one,3)
    (nine,5)
    (six,8)
    (three,2)
    (five,2)
    (four,9)
    (seven,3)
    (eight,6)
    (ten,6)
    ```
1. Check consumer with Flink logs
    ```
    docker logs smart-factory-kafka-spark-flink_kafka-consumer-flink-1 -f
    ```
    
    Output should be similar to:
    ```
    1> (ten,85)
    4> (nine,104)
    1> (ten,86)
    4> (five,91)
    4> (one,94)
    4> (six,90)
    1> (three,89)
    4> (six,91)
    4> (five,92)
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

Smart Factory Druid
---
1. Update environment file from distribution/docker path
     ``` 
     druid_extensions_loadList=["druid-histogram", "druid-datasketches", "druid-lookups-cached-global", "postgresql-metadata-storage", "druid-hdfs-storage", "druid-kafka-extraction-namespace", "druid-kafka-indexing-service","druid-azure-extensions","druid-kinesis-indexing-service"]
     ```
2. Build
     From the root of the repo, run 
     ``` 
     docker build -t apache/druid:0.18.1 -f distribution/docker/Dockerfile .
     ``` 
3. Run
    Edit environment to suite. Run
    ``` 
    docker-compose -f distribution/docker/docker-compose.yml up
    ``` 
