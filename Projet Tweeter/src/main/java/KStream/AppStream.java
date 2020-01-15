package KStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class AppStream {

    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        //input stream
        KStream<String, String> input = builder.stream("Macron");
        //output stream

        KStream<String, String> output = input.mapValues(new ValueMapper<String, String>() {
                                                           @Override
                                                           public String apply(String s) {
                                                               return checkCountry(s);
                                                           }
                                                       });
        output.to("Macron_Actu", Produced.with(Serdes.String(), Serdes.String()));


        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();


    }

    private static String checkCountry(String jsonRecordString) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(jsonRecordString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode fieldsMode = jsonNode.get("Text");

        if(fieldsMode.asText().contains("retraite") || fieldsMode.asText().contains("retraites") || fieldsMode.asText().contains("Retraite")){
            return "Réforme de la retraite :"+ fieldsMode.asText() ;
        }
        else if(fieldsMode.asText().contains("jaune") || fieldsMode.asText().contains("gilet jaune") || fieldsMode.asText().contains("gilets jaunes") ){

            return " Gilets jaunes :"+ fieldsMode.asText() ;
        }
        else{
            return "Autres infos : " + fieldsMode.asText();
        }
    }


}
