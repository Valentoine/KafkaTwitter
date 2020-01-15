package Broker;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Collector {

    private static final Logger logger = LoggerFactory.getLogger(Collector.class);
    private final KafkaPublisher publisher;

    Collector(KafkaPublisher publisher) {
        this.publisher = publisher;
    }

    void collect() throws TwitterException, IOException {

        //Configuration of twitter4j
        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("6ioQfLZqEjX6t52rJ5ZWNKog5")
                    .setOAuthConsumerSecret("H1zw77qcgFK0HXR8a0jIWfd02jeCgJeN3pBl9uG8KwsFgeuS1A")
                    .setOAuthAccessToken("1084581651987030017-QNq0JI9z4NZpZWlkTPDLbEMIFzQP7F")
                    .setOAuthAccessTokenSecret("vEGb3k0TgWE1ojqrz6asxXDp4UsdZ7KXZYxqFP1GkKcRZ");

            //stream api

            StatusListener listener = new StatusListener() {

                public void onStatus(Status status) {
                    formatAndPublish(status);
                }

                public void onScrubGeo(long userId, long upToStatusId) {
                }

                public void onStallWarning(StallWarning warning) {
                }

                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                }

                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                }

                public void onException(Exception ex) {
                    ex.printStackTrace();
                }
            };

            //Filter of the api

            FilterQuery query = new FilterQuery();
            String trackParam = "Macron";
            query.track(trackParam.split(","));

            TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

            twitterStream.addListener(listener);
            twitterStream.filter(query);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    void formatAndPublish(Status status){
        String user = status.getUser().getName().replaceAll("[^a-zA-Z0-9]", "");
        String text = new String(status.getText().replaceAll("\n", "").replaceAll("\"", ""));
        String dateTweet = String.valueOf(status.getCreatedAt());
        String isRetweet = Boolean.toString(status.isRetweet());
        String keyTweet = Long.toString(status.getId());

        publisher.publish(keyTweet,"{\"User\":\""+user+"\", \"Text\":\""+text+"\", \"Is Retweet\":\""+isRetweet+"\", \"Date\":\""+dateTweet+"\"}");
    }
}
