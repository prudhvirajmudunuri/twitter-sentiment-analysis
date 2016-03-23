from __future__ import absolute_import, print_function

from tweepy import OAuthHandler
from tweepy import Stream
from tweepy.streaming import StreamListener


consumer_key="hkce7PkPYfBvHjrLGh8Um4FD1"


consumer_secret="PGix2mcFegGb0pPh2HT7UJNkzmQlfT8MNEcqDKwXP99gPmWVUZ"


access_token="3535791433-5Xm2uZXXODJNkjc2BO3gOGuQDVEuOWccDOfKfAx"


access_token_secret="LharOF5IVt00dkA5DFCq1IME2xvBsyE9gPPuX3MujCXnm"

class StdOutListener(StreamListener):
    
    def on_data(self, data):
        
		print(data)
		saveFile = open('TweetsDV.json','a')
		saveFile.write(data)
		saveFile.write('\n')
		saveFile.close()  
       		return True
	

    def on_error(self, status):
        print(status)

if __name__ == '__main__':
    x = StdOutListener()
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    stream = Stream(auth, x)
    stream.filter(track=['domesticviolence','domestic violence','DomesticViolence','Domestic Violence','#domesticviolence','#DomesticViolence', 'White Ribbon', 'whiteribbon','#whiteribbon','#WhiteRibbonDay','#WhiteRibbonCampaign','@whiteribbon'])
