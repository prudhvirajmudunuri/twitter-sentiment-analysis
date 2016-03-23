
import twitter4j.*;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Tweets {
		private static final String CONSUMER_KEY		= "0a6OmqKsjWWuoXOKJ30aSrxIB";
		private static final String CONSUMER_SECRET 	= "2tPLbwKJ9I1XzuPEHuADwmt2lU8PvskiEHfUKcNdb1JRJNKqtJ";
		private static final int TWEETS_PER_QUERY		= 100;
		private static final long MAX_QUERIES			= 100000;
		private static final String SEARCH_TERM			= "#black";
		/**
		 *
		 *
		 * @return	The oAuth2 bearer token
		 */
		public static OAuth2Token token_is_working()
		{
			OAuth2Token token = null;
			ConfigurationBuilder configBuider = new ConfigurationBuilder();
			configBuider.setApplicationOnlyAuthEnabled(true);
			configBuider.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET);
		try
			{
				token = new TwitterFactory(configBuider.build()).getInstance().getOAuth2Token();
			}
			catch (Exception e)
			{
				System.out.println("Authentication failed - OAuth2 token is not working..");
				e.printStackTrace();
				System.exit(0);
			}
		return token;
		}

		/**
		 * 
		 * @return	Twitter4J Twitter object that's ready for API calls
		 */
		public static Twitter getTwitterAPI()
		{
			OAuth2Token token = token_is_working();
			ConfigurationBuilder configBuilder = new ConfigurationBuilder();
			configBuilder.setApplicationOnlyAuthEnabled(true);
			configBuilder.setOAuthConsumerKey(CONSUMER_KEY);
			configBuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
			configBuilder.setOAuth2TokenType(token.getTokenType());
			configBuilder.setOAuth2AccessToken(token.getAccessToken());

			//	Create Twitter object.
			return new TwitterFactory(configBuilder.build()).getInstance();
		}

		public static void main(String[] args) throws IOException
		{
			int	number_of_tweets = 0;
			long maxID = -1;
			FileWriter file = new FileWriter("/Users/sudhakareddy/Desktop/Tweets.JSON");
			JSONObject objJSON = new JSONObject();
			Twitter twitterObj = getTwitterAPI();
			try
			{
				Map<String, RateLimitStatus> rls = twitterObj.getRateLimitStatus("search");
				RateLimitStatus searchTweetsRate = rls.get("/search/tweets");
				System.out.println("Starting Tweets collection..");
				for (int qNo=0; qNo < MAX_QUERIES; qNo++)
				{
					if (searchTweetsRate.getRemaining() == 0)
					{
						Thread.sleep((searchTweetsRate.getSecondsUntilReset()+2) * 1000l);
					}

					Query query = new Query(SEARCH_TERM);
					query.setCount(TWEETS_PER_QUERY);
					query.resultType(Query.RECENT);
					query.setLang("en");

					if (maxID != -1)
					{
						query.setMaxId(maxID - 1);
					}

					QueryResult result = twitterObj.search(query);
					if (result.getTweets().size() == 0)
					{
						break;
					}

					for (Status s: result.getTweets())			
					{
						number_of_tweets++;
						if (maxID == -1 || s.getId() < maxID)
						{
							maxID = s.getId();
						}
						objJSON.put("Created at", s.getCreatedAt().toString());
						objJSON.put("User", s.getUser().getScreenName());
						objJSON.put("Tweet", s.getText());
					file.write(objJSON.toString());
					}
					searchTweetsRate = result.getRateLimitStatus();
				}
				System.out.println("Tweets collection process completed successfuly..");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
		        {
		            if (file != null)
		            {
		                file.close();
		            }
		        }
		}
	}
