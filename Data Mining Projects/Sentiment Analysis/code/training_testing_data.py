__author__ = 'Kartikey and Jay'

import operator
import random

def balance_data(train_data, labels):
    traindata = [[] for i in range(0, 5)]

    labelSet={}

    trainLabel = [[] for i in range(0, 5)]
    i = 0
    while(i < len(labels)):
        key = labels[i]
        if key not in labelSet:
            labelSet[key] = [i]
        else:
            labelSet[key].append(i)
        i += 1
    j = 0
    while(j < 5):

        for key in labelSet:

            opinionWords = []

            try:
                opinionWords.append(random.sample(labelSet[key], len(train_data)/5))
            except ValueError:
                opinionWords.append(random.sample(labelSet[key], len(labelSet[key])))

            for d in opinionWords:
                for xx in d:
                    trainLabel[j].append(labels[xx])
                    traindata[j].append(train_data[xx])

        j += 1
    return traindata, trainLabel
            
def voting(predicted):
    prediction=[None for i in range(len(predicted[0]))]
    i = 0
    while( i < len(predicted[0])):
        labelSet={}
        for j in range(len(predicted)):
            try:
                if not labelSet.has_key(predicted[j][i]):
                    labelSet[predicted[j][i]]=0

                else:
                    labelSet[predicted[j][i]]+=1
            except IndexError:
                print "i:", i, "j:", j, "\n"
        prediction[i] = max(labelSet.iteritems(), key=operator.itemgetter(1))[0]
        i += 1
    return prediction

