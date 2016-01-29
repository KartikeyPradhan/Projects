__author__ = 'Kartikey and Jay'

import re
from HTMLParser import HTMLParser
from nltk.corpus import stopwords
from nltk.tokenize.punkt import PunktSentenceTokenizer
from nltk.stem import PorterStemmer

##Stopwords
stopWords = set(stopwords.words('english'))
stopWords.remove("no")
stopWords.remove("nor")
stopWords.remove("not")
#stopWords.remove("against")

##Stemming
stemmer = PorterStemmer()

class remHtmlTag(HTMLParser):
    def __init__(self):
        self.reset()
        self.fed = []

    def get_data(self):
        return ''.join(self.fed)

    def handle_data(self, d):
        self.fed.append(d)

class Tweet:
	def __init__(self):
		self.tweet = []

class preprocessing:
    featurelist = []
    # start process_tweet

    # function to preprocess
    def preprocessin(self, cell_value):

        # to tokenize the tweet into sentences
        tweet = PunktSentenceTokenizer().tokenize(cell_value)
        # to remove 'u'
        tweet = '\n'.join(tweet)
        # to remove html tags
        tweet = self.remTags(tweet)
        # to lower aplphabets
        tweet = tweet.lower()

        ##Removing all junk
        tweet = re.sub(u'(RT |\\\\|\u201c)"?@.*?[: ]', ' ', tweet)
        tweet = re.sub('@', ' ', tweet)
        tweet = re.sub(r'[^\x00-\x7F]', ' ', tweet)
        tweet = re.sub('[\s]+', ' ', tweet)
        tweet = re.sub('_', ' ', tweet)
        tweet = re.sub('((www\.[\s]+)|(https?://[^\s]+))', '', tweet)
        tweet = re.sub(r'\\([^\s]+)', ' ', tweet)
        tweet = re.sub(u'[\u2018\u2019]', '\'', tweet)
        tweet = re.sub('(^|)?http?s?:?/?/?.*?( |$)', ' ', tweet)
        tweet = re.sub(u'\u2026', ' ', tweet)
        tweet = re.sub('---', ' ', tweet)
        tweet = re.sub(u'[\u201c\u201d]', '"', tweet)
        tweet = re.sub('\.?@.*?( |:|$)', ' ', tweet)
        tweet = re.sub(r"\.\.+", ' ', tweet)
        tweet = re.sub('&amp', ' ', tweet)
        tweet = re.sub('\.\.\.', ' ', tweet)
        tweet = tweet.strip('\'"')
        tweet = re.sub('(, |\.( |$))', ' ', tweet)
        tweet = re.sub('[][!"$*,/;<=>?@\\\\^_`{|}~]', ' ', tweet)
        tweet = re.sub('( - )', ' ', tweet)

        return tweet

    def remTags(self, html):
        feeds = remHtmlTag()
        feeds.feed(html)
        return feeds.get_data()

    def tokenizzer(self, tweet):

        bagofwords = []
        letters = tweet.split()

        for j in letters:

            if j not in stopWords:

                f = stemmer.stem(j)
                f = re.sub(r'(.)\1+', r'\1\1', f)
                bagofwords.append(f)

        return bagofwords

    def dataExtraction(self, start, end, excelSheet, label_file, bagOfWordFile):

        bow = open(bagOfWordFile, "w")
        labelCollector = open(label_file, "w")

        num_rows = end - 1

        while start <= num_rows:

            try:
                sentiment = excelSheet.cell_value(start, 4)
                cell_value = excelSheet.cell_value(start, 3)
                start += 1

            except:
                continue

            if sentiment == 1 or sentiment == 0 or sentiment == -1:

                rows = self.tokenizzer(self.preprocessin(cell_value))

                for i in rows:
                    bow.write(i + ",")

                labelCollector.write(str(sentiment))
                labelCollector.write("\n")
                bow.write("\n")