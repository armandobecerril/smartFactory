FROM openjdk:8u151-jdk-alpine3.7
MAINTAINER everis Mx (abecerri@everis.com)

# Install Bash
RUN apk add --no-cache bash libc6-compat

# Copy resources
WORKDIR /
COPY wait-for-it.sh wait-for-it.sh
COPY target/smart-factory-kafka-spark-flink-1.0-SNAPSHOT-jar-with-dependencies.jar smart-factory-kafka-spark-flink.jar

# Wait for Zookeeper and Kafka to be available and run application
CMD ./wait-for-it.sh -s -t 30 $EXAMPLE_ZOOKEEPER_SERVER -- ./wait-for-it.sh -s -t 30 $EXAMPLE_KAFKA_SERVER -- java -Xmx512m -jar smart-factory-kafka-spark-flink.jar