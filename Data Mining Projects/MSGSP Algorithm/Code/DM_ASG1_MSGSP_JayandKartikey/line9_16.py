__author__ = 'Kartikey and Jay'

# Adds rather appends the items in sequence
def itemInSequence(seq):
	itemList = []
	for s in seq:
		for i in range(0,len(s)):
			itemList.append(s[i])
	return itemList

# Checks if an itemset is available in a sequence
def checkItemInSequence(itemset, sequence):
	# temp variables i and j
	i = 0
	j = 0
	# if the length of sequence is less than length of itemset, availability not possible hence return false
	if len(sequence) < len(itemset):
		return False

	# parse each itemset and each sequence and find match of each item in item set in a given sequence.
	while i < len(itemset) and j < len(sequence):
		# if length of item set increases than the length of sequence at any time during parsing, move to next available sequence.
		if len(itemset[i]) > len(sequence[j]):
			j += 1
			continue
		# if item available in sequence, move to next item in itemset to check the availability
		else:
			if (set(itemset[i])<=set(sequence[j])):
				i += 1
				j += 1

			else:
				j += 1
	# if sucessfully found all the elements than return True else return false
	if i < len(itemset):
		return False
	else:
		return True


def line9_16(SequenceDataset, Ck, MIS):

    for s in SequenceDataset:
            for c in Ck:

                for i in itemInSequence(c.itemset):
                    if MIS[i] < c.minSup:
                        c.minSup = MIS[i]
                        c.minItem = i
                if checkItemInSequence(c.itemset, s):
                    c.count += 1
