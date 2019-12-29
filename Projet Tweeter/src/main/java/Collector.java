import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;

public class Collector {

    public static void main(String[] args) throws InterruptedException, TwitterException, IOException {

        collect();
    }

    static void collect() throws TwitterException, IOException {

        //Configuration of twitter4j

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("6ioQfLZqEjX6t52rJ5ZWNKog5")
                .setOAuthConsumerSecret("H1zw77qcgFK0HXR8a0jIWfd02jeCgJeN3pBl9uG8KwsFgeuS1A")
                .setOAuthAccessToken("1084581651987030017-QNq0JI9z4NZpZWlkTPDLbEMIFzQP7F")
                .setOAuthAccessTokenSecret("vEGb3k0TgWE1ojqrz6asxXDp4UsdZ7KXZYxqFP1GkKcRZ");

        //stream api

        StatusListener listener = new StatusListener(){
            public void onStatus(Status status) {
                System.out.println(status.getUser().getName() + " : " + status.getText());
            }
            public void onScrubGeo(long userId, long upToStatusId) {}
            public void onStallWarning(StallWarning warning) {}
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        //Filter of the api

        FilterQuery query = new FilterQuery();
        String trackParam = "Trump, Obama";
        query.track(trackParam.split(","));

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        twitterStream.addListener(listener);
        twitterStream.filter(query);
        // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
    }
}
