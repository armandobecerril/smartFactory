package com.smartfactory.kafka.cli;


import com.smartfactory.kafka.consumer.KafkaConsumerExample;
import com.smartfactory.kafka.consumer.KafkaFlinkConsumerExample;
import com.smartfactory.kafka.consumer.KafkaSparkConsumerExample;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.smartfactory.kafka.commons.Commons;
import com.smartfactory.kafka.producer.KafkaProducerExample;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(final String... args) {
        String EXAMPLE_GOAL = System.getenv("EXAMPLE_GOAL") != null ?
                System.getenv("EXAMPLE_GOAL") : "producer";

        logger.info("Kafka Topic: {}", Commons.EXAMPLE_KAFKA_TOPIC);
        logger.info("Kafka Server: {}", Commons.EXAMPLE_KAFKA_SERVER);
        logger.info("Zookeeper Server: {}", Commons.EXAMPLE_ZOOKEEPER_SERVER);
        logger.info("GOAL: {}", EXAMPLE_GOAL);

        switch (EXAMPLE_GOAL.toLowerCase()) {
            case "producer":
                KafkaProducerExample.main();
                break;
            case "consumer.kafka":
                KafkaConsumerExample.main();
                break;
            case "consumer.spark":
                KafkaSparkConsumerExample.main();
                break;
            case "consumer.flink":
                KafkaFlinkConsumerExample.main();
                break;
            default:
                logger.error("No valid goal to run.");
                break;
        }
    }
}
