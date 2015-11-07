__author__ = 'Kartikey and Jay'

'''Import following modules that will be used in main.py to implement level 3 MS-GSP algorithm:
(module name - description)
readParaFile -  Parse para.txt/para_training file, extract and store MIS values for each item and create a sorted item list
readDataFile - Parse data.txt/data_training file
init_pass - Generates level 1 candidates
generateF1 - Generates level 1 frequent sets
level2_candidate_gen_SPM - Generates level 2 candidates
generateFk - Generates level k Frequent sets, where k>1
MScandidate_gen_SPM - Generates level k candidates, where k>2
'''
import readParaFile
import readDataFile
import init_pass
import generateF1
import level2_candidate_gen_SPM
import generateFk
import MScandidate_gen_SPM
import sys

# Create a data structure to store the required fields
class Transaction:
	def __init__(self, count=0, minSup=99, minItem=None, itemset=[]):
		self.itemset = itemset
		self.count = count
		self.minSup = minSup
		self.minItem = minItem

#variables that refer to file i.e. training data or test data

LargeDir = "Data\LargeData\\"

SmallDir = "Data\SmallData\\"

OutputFile = "Data\OutputData\\"

##Select which kind of data to be selected:

##Small File selection


##Small Data para1_1: Start
dataFile = SmallDir + "data_1"
paraFile = SmallDir + "para1_1"
outputFileName = "result1_1.txt"
##Small Data para1_1: End


'''
##Small Data para1_2: Start
dataFile = SmallDir + "data_1"
paraFile = SmallDir + "para1_2"
outputFileName = "result1_2.txt"
##Small Data para1_2: End
'''


##Large file selection

'''
##Large Data para2_1: Start
dataFile = LargeDir + "data_2"
paraFile = LargeDir + "para2_1"
outputFileName = "result2_1.txt"
##Large Data para2_1: End
'''

'''
##Large Data para2_2: Start
dataFile = LargeDir + "data_2"
paraFile = LargeDir+ "para2_2"
outputFileName = "result2_2.txt"
##Large Data para2_2: Start
'''


candidates = {}
frequentSet = {}
final_seq1_del = {}
final_seq2_del = {}
minSupp_seq = {}

# read the para file and extract SDC and MIS values. Additionally, create sorted item list
SDC, MIS, sortedItems = readParaFile.readMISValues(paraFile)

# parse the transactions provided in dataFile
SequenceDataset = readDataFile.readSequenceDataset(dataFile)

#print("SDC: " + str(SDC))
#print("MIS: " + str(MIS))
#print("sortedItems: " + str(sortedItems))
#print("SequenceDataset: " + str(SequenceDataset))

# Evaluate level 1 candidates
candidates[0] = init_pass.init_pass(sortedItems, SequenceDataset, MIS)

# Evaluate level 1 frequent sets
frequentSet[0] = generateF1.generateF1(candidates[0], MIS, len(SequenceDataset))
k=2
# For all levels greater than 1 following loops handles the candidate gen and frequent set gen. Works till level 3 only.
while len(frequentSet[k-2])>0:
    # For level 2, candidate generation has a different logic than other levels.
    if k==2:
        candidates[k-1] = level2_candidate_gen_SPM.level2_candidate_gen(candidates[0], sortedItems, SDC, len(SequenceDataset), MIS, SequenceDataset)
        frequentSet[k-1] = generateFk.generateFk(candidates[k-1], len(SequenceDataset), MIS)
    # Handles all the candidate gen and frequent set gen for level k, k>2
    else:
        candidates[k-1], final_seq1_del[k-1], final_seq2_del[k-1], minSupp_seq[k-1] = MScandidate_gen_SPM.MScandidate_gen_SPM(frequentSet[k-2], MIS, SequenceDataset)
        frequentSet[k-1] = generateFk.generateFk(candidates[k-1], len(SequenceDataset), MIS)
    k+=1

# Print the output till level 3
result = sys.stdout
file = open(OutputFile + outputFileName, 'w')
sys.stdout = file
k=1
while len(frequentSet[k-1])>0:
    print("The number of length "+ str(k) +" sequential patterns is " + str(len(frequentSet[k-1])) )
    for i in frequentSet[k-1]:
        print("Pattern: <"+ str(i.itemset).replace('[','{').replace(']','}').replace('{{','{').replace('}}','}')+">  Count: "+str(i.count))
    print()
    k+=1

sys.stdout = result
file.close()
