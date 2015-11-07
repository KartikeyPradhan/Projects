__author__ = 'Kartikey and Jay'

# Create a data structure to store the required fields
class Transaction:
	def __init__(self, itemset=[], count=0,minSup=100,minItem=None):
		self.itemset = itemset
		self.count = count
		self.minSup = minSup
		self.minItem = minItem


# Generate level k frequesnt sets by checking the support with MIS values
def generateFk(Ck, totalSequence, MIS):

    F=[]

    for c in Ck:

        try:
            if float(c.count)/totalSequence >= MIS[c.minItem]:
                F.append(Transaction(c.itemset, c.count, c.minSup, c.minItem))
        except:
            print("Value Error!!")
    return F