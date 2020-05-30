package com.smartfactory.kafka.producer;

import com.smartfactory.kafka.commons.Commons;
import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

public class KafkaProducerExample {

    private static String KafkaBrokerEndpoint = null;
    private static String KafkaTopic = "example";
    private static String CsvFile = null;
    private static final Logger logger = LogManager.getLogger(KafkaProducerExample.class);

    public static void main(final String... args) {
        // Create topic
        //createTopic();

        // String[] words = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        Random ran = new Random(System.currentTimeMillis());

        // final Producer<String, String> producer = createProducer();
        int EXAMPLE_PRODUCER_INTERVAL = System.getenv("EXAMPLE_PRODUCER_INTERVAL") != null ?
                Integer.parseInt(System.getenv("EXAMPLE_PRODUCER_INTERVAL")) : 100000;

        KafkaProducerExample kafkaProducer = new KafkaProducerExample();
        kafkaProducer.PublishMessages();

       /* try {
            while (true) {
                String word = words[ran.nextInt(words.length)];
                String uuid = UUID.randomUUID().toString();

                ProducerRecord<String, String> record = new ProducerRecord<>(Commons.EXAMPLE_KAFKA_TOPIC, uuid, word);
                RecordMetadata metadata = producer.send(record).get();

                logger.info("Sent ({}, {}) to topic {} @ {}.", uuid, word, Commons.EXAMPLE_KAFKA_TOPIC, metadata.timestamp());

                Thread.sleep(EXAMPLE_PRODUCER_INTERVAL);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("An error occurred.", e);
        } finally {
            producer.flush();
            producer.close();
        }*/
    }

    private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Commons.EXAMPLE_KAFKA_SERVER);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerExample");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }
    private Producer<String, String> ProducerProperties(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Commons.EXAMPLE_KAFKA_SERVER);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerExample");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<String, String>(properties);
    }

    private static void createTopic() {
        int sessionTimeoutMs = 10 * 1000;
        int connectionTimeoutMs = 8 * 1000;

        ZkClient zkClient = new ZkClient(
                Commons.EXAMPLE_ZOOKEEPER_SERVER,
                sessionTimeoutMs,
                connectionTimeoutMs,
                ZKStringSerializer$.MODULE$);

        boolean isSecureKafkaCluster = false;
        ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(Commons.EXAMPLE_ZOOKEEPER_SERVER), isSecureKafkaCluster);

        int partitions = 1;
        int replication = 1;

        // Add topic configuration here
        Properties topicConfig = new Properties();
        if (!AdminUtils.topicExists(zkUtils, Commons.EXAMPLE_KAFKA_TOPIC)) {
            AdminUtils.createTopic(zkUtils, Commons.EXAMPLE_KAFKA_TOPIC, partitions, replication, topicConfig, RackAwareMode.Safe$.MODULE$);
            logger.info("Topic {} created.", Commons.EXAMPLE_KAFKA_TOPIC);
        } else {
            logger.info("Topic {} already exists.", Commons.EXAMPLE_KAFKA_TOPIC);
        }

        zkClient.close();
    }
    private void PublishMessages(){

        int EXAMPLE_PRODUCER_INTERVAL = System.getenv("EXAMPLE_PRODUCER_INTERVAL") != null ?
                Integer.parseInt(System.getenv("EXAMPLE_PRODUCER_INTERVAL")) : 100000;
        //Defining a Producer Object with set Properties.

        final Producer<String, String> CsvProducer = ProducerProperties();
        String fileName =  "PDM_DataModel.csv";

        //Putting it in a try catch  block to catch File read exceptions.
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(fileName);
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
            /*for (String line; (line = reader.readLine()) != null;) {
                System.out.println("read file -> "+line);
                Thread.sleep(EXAMPLE_PRODUCER_INTERVAL);
            }*/
        try {
        //Setting up a Stream to our CSV File.
        Stream<String> FileStream = reader.lines();

        //Here we are going to read each line using Foreach loop on the FileStream object
        FileStream.forEach(line -> {
            //The topic the record will be appended to
            //The key that will be included in the record
            //The record contents
            final ProducerRecord<String, String> CsvRecord = new ProducerRecord<String, String>(
                    KafkaTopic, UUID.randomUUID().toString(), line
            );
            // Send a record and set a callback function with metadata passed as argument.
            CsvProducer.send(CsvRecord, (metadata, exception) -> {
                if(metadata != null){
                    //Printing successful writes.
                    System.out.println("CsvData: -> "+ CsvRecord.key()+" | "+ CsvRecord.value());
                    logger.info("Sent ({}) to topic {} @ {}.", CsvRecord.value(), Commons.EXAMPLE_KAFKA_TOPIC, metadata.timestamp());
                }
                else{
                    //Printing unsuccessful writes.
                    System.out.println("Error Sending Csv Record -> "+ CsvRecord.value());
                }
            });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
