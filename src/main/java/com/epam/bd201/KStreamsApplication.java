package com.epam.bd201;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static org.apache.kafka.streams.StreamsConfig.*;

public class KStreamsApplication {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(APPLICATION_ID_CONFIG, "expedia-stream");
        props.put(BOOTSTRAP_SERVERS_CONFIG, "10.244.0.7:9092,10.244.0.8:9092,10.244.1.4:9092");
        props.put(BOOTSTRAP_SERVERS_CONFIG, "bdccdev-1f46a8a5.hcp.westeurope.azmk8s.io:9092");
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put("schema.registry.url", "10.244.0.9:8081");

        final String INPUT_TOPIC_NAME = "expedia";
        final String OUTPUT_TOPIC_NAME = "expedia_ext";

        final StreamsBuilder builder = new StreamsBuilder();

        final KStream<String, String> input_records = builder.stream(INPUT_TOPIC_NAME, Consumed.with(Serdes.String(), Serdes.String()));

        input_records.mapValues(KStreamsApplication::toHotelVisit)
                .mapValues(HotelVisit::setStayDurationAndType)
                .to(OUTPUT_TOPIC_NAME);

        final Topology topology = builder.build();
        System.out.println(topology.describe());

        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

    private static HotelVisit toHotelVisit(String value) {
        try {
            return objectMapper.readValue(value, HotelVisit.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Deserialization error");
        }
    }
}
