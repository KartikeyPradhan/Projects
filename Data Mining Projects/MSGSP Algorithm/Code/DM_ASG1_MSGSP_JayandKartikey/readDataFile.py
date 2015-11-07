__author__ = 'Kartikey and Jay'

import re

SequenceDataset = []

def readSequenceDataset(file):
    # Opens the 'file' in read mode and start reading line by line
    dataFile = open(file, "r")
    #define the pattern to be searched for
    pattern = re.compile("({.+?})+?")
    #read data line by line and check if any data is present in the line
    line = dataFile.readline()
    while line != '':
        line = line.strip()
        sequenceSet = []
        if len(line) > 0:
            # Remove angular brackets from the line that is parsed above
            line = line[1:len(line)-1]
            # Apply REGEX and try to extract required values. Note- REGEX is file format specific
            itemsets = pattern.findall(line)

            #Parse item values inside {} for each line and sequentially extract each values into itemset using ',' delimiter
            for itemSetData in itemsets:
                itemSet = []
                set = itemSetData[1:len(itemSetData)-1]
                if set.find(',') > -1:
                    for item in set.split(','):
                        itemSet.append(int(item))
                else:
                    if set != '':
                        itemSet.append(int(set))
                sequenceSet.append(itemSet)
        SequenceDataset.append(sequenceSet)
        line = dataFile.readline()

    return SequenceDataset

#readSequenceDataset("data.txt")
