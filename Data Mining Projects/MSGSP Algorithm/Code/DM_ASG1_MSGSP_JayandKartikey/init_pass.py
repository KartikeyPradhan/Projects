__author__ = 'Kartikey and Jay'


# Checks if the item is present in sequence, if present returns the count
def checkItemInSequence(item, sequence):
	count = 0
	for i in sequence:
		count += i.count(item)
	return count


# Generates level 1 candidates
def init_pass(sortedItems,SequenceDataset,MIS):
    # variable to store items that had been discovered at earlier stage from data file
    candidate1 = {}
    for i in sortedItems:
        candidate1[i] = 0
    # Check for the item in a sequence. Considers only the first instance.
    for sequence in SequenceDataset:
        for item in sortedItems:
            candidate1[item]=candidate1[item] + int(checkItemInSequence(item,sequence) > 0)

    counter = 0
    flag = False
    # Checks the minimum support requirement
    for counter in range(0, len(sortedItems)):
        # if the support is greater than the MIS value, we consider that item as a candidate else skip that item.
        if float(candidate1[sortedItems[counter]])/len(SequenceDataset) > MIS[sortedItems[counter]]:
            minSup = MIS[sortedItems[counter]]
            flag = True
            break
        else:
            del candidate1[sortedItems[counter]]
            counter += 1

    # Check if all the level 1 candidates have support greater than min support, if not exit the algorithm.
    if flag == True:
        for i in range(counter, len(sortedItems)):
            if float(candidate1[sortedItems[i]])/len(SequenceDataset) > minSup:
                continue
            else:
                del candidate1[sortedItems[i]]
    else:
        print("Level 1 candidate found!")
        return 0

    return candidate1
