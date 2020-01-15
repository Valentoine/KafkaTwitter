package Broker;

import twitter4j.TwitterException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class Starter {

    public static void main(String[] args) throws InterruptedException, TwitterException, IOException {
        KafkaHandlerConfig config = buildConfig();
        KafkaPublisher publisher = new KafkaPublisher(config);
        Collector collector = new Collector(publisher);
        collector.collect();

    }

    private static KafkaHandlerConfig buildConfig() {
        KafkaHandlerConfig config = new KafkaHandlerConfig();
        config.setBootstrapServers(Collections.singletonList("localhost:9092"));
        config.setTopicName("Macron");
        return config;
    }
}
