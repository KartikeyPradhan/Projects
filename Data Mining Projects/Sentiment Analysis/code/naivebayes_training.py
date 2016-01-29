__author__ = 'Kartikey and Jay'

from sklearn import cross_validation
from sklearn.feature_extraction.text import CountVectorizer
from training_testing_data import balance_data
from sklearn.naive_bayes import MultinomialNB
from sklearn.naive_bayes import BernoulliNB
from sklearn import metrics
from sklearn.metrics import precision_recall_fscore_support
from training_testing_data import voting
from sklearn.feature_extraction.text import TfidfTransformer
import sys

'''
##writting into this file
#fm = open("incorrectClassifications.txt", "w")
outputFile = "obama_output.txt"
feature_file = open("obama_words.txt", "r")
precision = [0 for i in range(3)]
features = feature_file.readlines()
fscore = [0 for i in range(3)]
class_file = open("obama_labels.txt", "r")
recall = [0 for i in range(3)]
labels = class_file.readlines()
new_test_words = open("obama_words_test.txt", "r")
new_test_labels = open("obama_labels_test.txt", "r")


'''
outputFile = "romney_output.txt"
feature_file = open("romney_words.txt", "r")
precision = [0 for i in range(3)]
features = feature_file.readlines()
fscore = [0 for i in range(3)]
class_file = open("romney_labels.txt", "r")
recall = [0 for i in range(3)]
labels = class_file.readlines()
new_test_words = open("romney_words_test.txt", "r")
new_test_labels = open("romney_labels_test.txt", "r")


i = 0
#Stripping \n from all labels
for str in labels:
    labels[i] = str.replace("\n", "")
    i += 1

accuracy = 0
# Naive Bayes Multinomial Distribution
algorithm = MultinomialNB()

# Naive Bayes Bernoulli Distribution
#algorithm = BernoulliNB()

#Cross validation-Generating indices
sss = cross_validation.KFold(len(labels), n_folds=10)

#StratifiedShuffleSplit indices
#sss = StratifiedShuffleSplit(labels, 10, test_size=0.1, random_state=0)

# 10 fold cross validation
for trainingIndexes, testingIndexes in sss:

    #Fetching train labels
    trainingLabels = list(labels[i] for i in trainingIndexes)

    count_vect = CountVectorizer()

    #Fetching training features
    trainingData = list(features[i] for i in trainingIndexes)

    [newTrainingData, newTrainingLabel] = balance_data(trainingData, trainingLabels)
    bag_classifier = []

    #Fetching test features
    test_features = list(features[i] for i in testingIndexes)

    #Fetching test labels
    test_labels = list(labels[i] for i in testingIndexes)


    estimated=[]
    for batch, batch_label in zip(newTrainingData, newTrainingLabel):

        #TF IDF object instantiation
        tf_idf_transformer = TfidfTransformer()

        #count word_freq
        trainingCounts = count_vect.fit_transform(batch)

        #TF IDF object fitting to data
        training_tf_idf = tf_idf_transformer.fit_transform(trainingCounts)

        #Train Naive Bayes
        algorithm.fit(trainingCounts, batch_label)
        X_new_tfidf = tf_idf_transformer.transform(count_vect.transform(test_features))
        estimated.append(algorithm.predict(count_vect.transform(test_features)))

    new_predicted = voting(estimated)
    metric = precision_recall_fscore_support(test_labels, new_predicted)
    accuracy += metrics.accuracy_score(test_labels, new_predicted, normalize=True, sample_weight=None)

    precision += metric[0]
    recall += metric[1]
    fscore += metric[2]

print '\t\t\t\t', '-1', '\t\t\t', '0', '\t\t\t', '1'
precision=precision*10
recall=recall*10
fscore=fscore*10
print 'precision:\t', precision, "\n", 'recall:\t\t', recall, "\n", 'F1-score:\t', fscore, "\n"
print 'accuracy: ', accuracy*10

'''
result = sys.stdout
file = open(outputFile, 'w')
sys.stdout = file
print '\t\t\t\t', '-1', '\t\t\t', '0', '\t\t\t', '1'
precision=precision
recall=recall
fscore=fscore
print 'precision:\t', precision, "\n", 'recall:\t\t', recall, "\n", 'F1-score:\t', fscore, "\n"
print 'accuracy: ', accuracy*10
sys.stdout = result
file.close()
'''