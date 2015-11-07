__author__ = 'Kartikey and Jay'


# Create a data structure to store the required fields
class Transaction:
	def __init__(self,count=0,minSup=100,minItem=None, itemset=[]):
		self.itemset = itemset
		self.count = count
		self.minSup = minSup
		self.minItem = minItem


# generates level 1 frequent sets by checking the support with MIS values
def generateF1(L, MIS, totalSequence):

    sequenceF1 = []
    for i in L:
        if float(L[i])/totalSequence < MIS[i]:
            continue
        else:
            sequenceF1.append(Transaction(L[i], MIS[i], i, [[i]]))

    return sequenceF1
