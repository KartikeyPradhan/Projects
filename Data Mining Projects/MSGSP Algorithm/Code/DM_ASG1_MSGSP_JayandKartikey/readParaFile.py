__author__ = 'Kartikey and Jay'

import re

'''
Variable detail:
MIS is a tuple: Key= item number, value= MIS value
SDC=support Difference Constraint
'''

MIS = {}

'''
Initialize SDC with a high end value,
thus if value of SDC not found in file
we consider that SDC condition in
algorithm always satisfies
'''
SDC = 100.0
def readMISValues(file):
    # Opens the 'file' in read mode and start reading line by line
    paraFile = open(file, "r")
    line = paraFile.readline()
    while line != '':
        #remove whitespace in the line
        line = line.strip()
        '''
        1. If any data present in the file then apply REGEX and try to extract required values
           Note- REGEX is file format specific
        2. If pattern not found then search if SDC value is mentioned in the line
        3. Once the entire file is parsed, sort the items discovered during parsing
        '''
        if len(line) > 0:
            misExtract = re.search("MIS\(([0-9]+)\) = ([^\n]*)", line)
            if misExtract is None:
                sdcMatch = re.search("SDC = (.*)", line)
                if sdcMatch is not None:
                    SDC = float(sdcMatch.group(1))
            else:
                MIS[int(misExtract.group(1))] = float(misExtract.group(2))

        line = paraFile.readline()
        sortedItems=sorted(MIS,key=lambda x: MIS[x])

    #print("SDC: "+ str(SDC))
    #print("MIS: "+ str(MIS))
    #print("sortedItems: "+ str(sortedItems))

    return SDC, MIS, sortedItems

#readMISValues("para.txt")
