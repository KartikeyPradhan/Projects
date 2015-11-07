__author__ = 'Kartikey and Jay'

import line9_16

# Create a data structure to store the required fields
class Transaction:
	def __init__(self, itemset=[], count=0,minSup=100,minItem=None):
		self.itemset = itemset
		self.count = count
		self.minSup = minSup
		self.minItem = minItem


# Level 2 candidate generation logic
def level2_candidate_gen(L, sortedItems, SDC, totalSequence, MIS, SequenceDataset):

    Candidate2 = []
    seq=[]
    # Checks the combinations of items that had been discovered and checks the support requirement of the combinations.
    for i in range(0, len(sortedItems)):

        if sortedItems[i] in L.keys():
            for j in range(0, len(sortedItems)):
                if sortedItems[j] in L.keys():
                    # Checks level 2 candidate generation logic, including the SDC check using float data type.
                    if (float(L[sortedItems[i]])/totalSequence >= min(MIS[sortedItems[i]], MIS[sortedItems[j]]) and abs(float(L[sortedItems[i]])/totalSequence-float(L[sortedItems[j]])/totalSequence) <= SDC and float(L[sortedItems[j]])/totalSequence >= min(MIS[sortedItems[i]], MIS[sortedItems[j]]) ):
                        # Logic to form {a,b} item set
                        if sortedItems[i] < sortedItems[j]:
                            seq.append(sortedItems[i])
                            Candidate2.append(Transaction([[sortedItems[i], sortedItems[j]]]))
                        seq.append(sortedItems[j])
                        Candidate2.append(Transaction([[sortedItems[i]], [sortedItems[j]]]))

                else:
                    continue
        else:
            continue


    line9_16.line9_16(SequenceDataset, Candidate2, MIS)

    return Candidate2
