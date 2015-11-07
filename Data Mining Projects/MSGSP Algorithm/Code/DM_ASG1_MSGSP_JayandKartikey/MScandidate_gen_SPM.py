__author__ = 'Kartikey and Jay'

import copy
import line9_16

# Create a data structure to store the required fields
class Transaction:
	def __init__(self, item=[], count=0,minSup=100,minItem=None):
		self.itemset = item
		self.count = count
		self.minSup = minSup
		self.minItem = minItem

# Appends the itemlist with the sequence parsed
def itemInSequence(seq):
	itemList = []
	for s in seq:
		for i in range(0,len(s)):
			itemList.append(s[i])
	return itemList

# removes any empty values that have been parsed
def emptySetRemove(seq):

	for i in seq:
        # checks for the empty values in sequence
		if i == []:
			seq.remove(i)
		else:
			continue
	return seq

# Checks of the two input sequences are equal, returns true if found equal.
def equalityCheck(seq1, seq2):
	if elementLength(seq1) != elementLength(seq2):
		return False
	else:
		for i in range(0,len(seq1)):
			if seq1[i]==seq2[i]:
				continue
			else:
				return False
		return True

# returns length of an itemset
def elementLength(element):
	c = 0
	for item in element:
		c += len(item)
	return c

# checks if the frequent set is available in sequence
def validateFrequent(sequence, F):
	for dataSeq in F:
		Fsequence = dataSeq.itemset
		if Fsequence==sequence:
			return True
		else:
			continue
	return False

# Retuens the values of minimum MIS for a given inout sequence.
def findMinMIS(sequence, MIS):
	minMIS = MIS[sequence[0][0]]
	for element in sequence:
		for item in element:
            # Checks of the MIs of current item is less than the known MIN MIS, if so, change the value of MIS MIS
			if MIS[item] < minMIS:
				minMIS=MIS[item]
			else:
				continue
	return minMIS

#Geenric candidate generation for all levels grater than 2
def MScandidate_gen_SPM(fkminus1, MIS, SequenceDataset):
    Candidate = []
    final_seq1_del= []
    final_seq2_del = []
    # Perform combinations of previously found frequent sets.
    for i in range(0, len(fkminus1)):
        for j in range(0, len(fkminus1)):
            # Extract items of each sequence in consideration into each temporary variables.
            sequence1 = copy.deepcopy(fkminus1[i].itemset)
            sequence2 = copy.deepcopy(fkminus1[j].itemset)
            seq1 = []
            for item in itemInSequence(sequence1):
                seq1.append(MIS[item])
            seq2 = []
            for item in itemInSequence(sequence2):
                seq2.append(MIS[item])


            # Checks of the MIS values of last item in sequence 2 is less than MIS value of every other element in S2
            if seq2[-1] <= min(seq2[0:len(seq2)-1]):

                # create temporary copy to store modified sequences
                s1MinusFirst = copy.deepcopy(sequence1)
                s2_dropseclast = copy.deepcopy(sequence2)

                # Modify the first sequence
                if len(sequence1[0]) > 1:
                    seq1_del = s1MinusFirst[0][0]
                    s1MinusFirst[0].remove(s1MinusFirst[0][0])
                else:
                    seq1_del = sequence1[1:len(sequence1)]
                    s1MinusFirst = sequence1[1:len(sequence1)]

                # Modify second sequence
                if len(sequence2[len(sequence2)-1]) != 1:
                    seq2_del = s2_dropseclast[len(s2_dropseclast)-1][len(s2_dropseclast[len(s2_dropseclast)-1])-2]
                    s2_dropseclast[len(s2_dropseclast)-1].remove(s2_dropseclast[len(s2_dropseclast)-1][len(s2_dropseclast[len(s2_dropseclast)-1])-2])
                else:
                    seq2_del = s2_dropseclast[len(s2_dropseclast)-2][len(s2_dropseclast[len(s2_dropseclast)-2])-1]
                    s2_dropseclast[len(s2_dropseclast)-2].remove(s2_dropseclast[len(s2_dropseclast)-2][len(s2_dropseclast[len(s2_dropseclast)-2])-1])


                if seq1_del == seq2_del:
                    flag = True

                # eliminates any empty sets that are discovered after deletion
                s1MinusFirst=emptySetRemove(s1MinusFirst)
                s2_dropseclast=emptySetRemove(s2_dropseclast)
                seq1_temporary = []
                seq2_temporary = []
                # Checks if the two resulting sequences are equal
                if equalityCheck(s1MinusFirst, s2_dropseclast) and (MIS[sequence2[-1][-1]] <= MIS[sequence1[0][0]]):
                    # Insert the values depending on the length of sequences
                    if len(sequence1[0]) == 1:
                        seq1_temporary.append(sequence1[0])
                        seq2_temporary.append(sequence2)
                        s2_tmp = copy.deepcopy(sequence2)
                        sequence2.insert(0, sequence1[0])
                        Candidate.append(Transaction(sequence2))

                        # if the length of sequence 2 is 2 then append the new values accordingly
                        if (len(s2_tmp) == 2 and elementLength(s2_tmp) == 2) and (float(sequence1[0][0]) < float(s2_tmp[0][0])):
                            s2_tmp[0].insert(0, sequence1[0][0])
                            Candidate.append(Transaction(s2_tmp))
                        # if sequence 2 is of length 1 and sequence 2 is of length 2 then append sequence 2
                    elif (len(sequence2) == 1 and elementLength(sequence2) == 2 and float(sequence1[0][0]) < float(sequence2[0][0])) or elementLength(sequence2) > 2:
                        sequence2[0].insert(0, sequence1[0][0])
                        Candidate.append(Transaction(sequence2))
                continue

            # Case 2: if the MIS values of 1st item in sequence 1 is less than every other item
            if seq1[0] <= min(seq1[1:len(seq1)]):

                # Create temporary place holders for modified sequence
                s1_dropsecond = copy.deepcopy(sequence1)
                s2MinusLast = copy.deepcopy(sequence2)

                # Modify both the sequences
                if len(sequence1[0]) > 1:
                    seq1_del = s1_dropsecond[0][1]
                    s1_dropsecond[0].remove(s1_dropsecond[0][1])
                else:
                    seq1_del = s1_dropsecond[1][0]
                    s1_dropsecond[1].remove(s1_dropsecond[1][0])

                if len(sequence2[len(sequence2)-1]) > 1:
                    seq2_del = s2MinusLast[len(s2MinusLast)-1][len(s2MinusLast[len(s2MinusLast)-1])-1]
                    s2MinusLast[len(s2MinusLast)-1].remove(s2MinusLast[len(s2MinusLast)-1][len(s2MinusLast[len(s2MinusLast)-1])-1])
                else:
                    seq2_del = sequence2[0:len(sequence2)-1]
                    s2MinusLast = sequence2[0:len(sequence2)-1]

                if seq1_del != seq2_del:
                    flag = False
                else:
                    flag = True

                # Eliminate empty itemsets if any after modifying the sequences
                s1_dropsecond=emptySetRemove(s1_dropsecond)
                s2MinusLast=emptySetRemove(s2MinusLast)
                seq1_add = []
                # Checks if the two resulting sequences are equal
                if equalityCheck(s1_dropsecond, s2MinusLast) and (MIS[sequence2[-1][-1]] >= MIS[sequence1[0][0]]):

                    # Append candidates as per the length of existing sequence
                    if len(sequence2[-1]) == 1:
                        seq1_temp = copy.deepcopy(sequence1)

                        # append the values at the end of sequence 1
                        sequence1.append(sequence2[-1])
                        seq1_add.append(sequence2[-1])
                        Candidate.append(Transaction(sequence1))

                        # if both the lengths are same but last item of sequence 2 > last item of sequence 1
                        if (len(seq1_temp) == 2 and elementLength(seq1_temp) == 2) and (float(sequence2[-1][-1]) > float(seq1_temp[-1][-1])):

                            if flag == True:
                                seq1_del = seq1_temp

                            seq1_temp[-1].append(sequence2[-1][-1])
                            seq1_add.append(Transaction(sequence1))
                            Candidate.append(Transaction(seq1_temp))

                    # length of sequence 1 is 1 and length of sequence 2 is 2, and last item condition as above, or length of sequence 1 is greater than 1
                    elif (len(sequence1) == 1 and elementLength(sequence1) == 2 and float(sequence2[-1][-1]) > float(sequence1[-1][-1])) or elementLength(sequence1) > 2:

                        if flag == True:
                                seq1_del = seq1_temp

                        sequence1[-1].append(sequence2[-1][-1])
                        #seq1_add.append(Transaction(seq1_temp))
                        Candidate.append(Transaction(sequence1))
                continue

            # case 3: if none of the above, check for the join step
            else:
                # Create temporary place holders of both the sequences
                s1MinusFirst = copy.deepcopy(sequence1)
                s2MinusLast = copy.deepcopy(sequence2)

                # Modify both the sequences
                if len(sequence1[0]) > 1:
                    seq1_del = s1MinusFirst[0][0]
                    s1MinusFirst[0].remove(s1MinusFirst[0][0])
                else:
                    seq1_del = s1MinusFirst[0][0]
                    s1MinusFirst = sequence1[1:len(sequence1)]

                if len(sequence2[len(sequence2)-1]) != 1:
                    seq2_del = s2MinusLast[len(s2MinusLast)-1][len(s2MinusLast[len(s2MinusLast)-1])-1]
                    s2MinusLast[len(s2MinusLast)-1].remove(s2MinusLast[len(s2MinusLast)-1][len(s2MinusLast[len(s2MinusLast)-1])-1])
                else:
                    seq2_del = sequence2[0:len(sequence2)-1]
                    s2MinusLast = sequence2[0:len(sequence2)-1]

                # Check for empty itemsets in modified sequences
                s1MinusFirst=emptySetRemove(s1MinusFirst)
                s2MinusLast=emptySetRemove(s2MinusLast)

                # Check of the modified sequences are equal
                if equalityCheck(s1MinusFirst, s2MinusLast):
                    if len(sequence2[-1]) == 1:
                        sequence1.append(sequence2[-1])
                        seq1_add.append(Transaction(sequence1))
                        Candidate.append(Transaction(sequence1))
                    else:
                        sequence1[-1].append(sequence2[-1][-1])
                        seq1_add.append(Transaction(sequence1))
                        Candidate.append(Transaction(sequence1))


    final_seq1_del.append(seq1_del)
    final_seq2_del.append(seq2_del)

    # Once the cases have been evaluated and temporary candidate list has been prepared, we proceed to the pruning step
    seq_intermediate = {}
    # Place holder for final candidates
    finalCandidate = []
    minSupp_seq = 100
    #for each discovered candidate check the minimum support
    for seq in range(0, len(Candidate)):
        seq_intermediate[seq] = 1
        minSupp_seq = Candidate[seq].minSup
        subSequence = copy.deepcopy(Candidate[seq].itemset)
        for i in range(0, len(subSequence)):
            for j in range(0, len(subSequence[i])):
                temp_seq = copy.deepcopy(subSequence)
                temp_seq[i].remove(temp_seq[i][j])
                temp_seq = emptySetRemove(temp_seq)
                # check for the availability in sequence and minimum MIS match, if not, skip the candidate
                if (not validateFrequent(temp_seq, fkminus1)) and (findMinMIS(temp_seq, MIS) == findMinMIS(subSequence, MIS)):
                    seq_intermediate[seq] = 0
                    #minSupp_seq = seq_intermediate[seq].minSup
                    break
                # if match found, consider the candidate and move to next sub sequence
                else:
                    continue
            if (not validateFrequent(temp_seq, fkminus1)) and (findMinMIS(temp_seq, MIS) == findMinMIS(subSequence, MIS)):
                break
    # Once the availibility and MIN MIs has been evaluated, store the final candidates
    for i in range(0, len(seq_intermediate)):
        if seq_intermediate[i]:
            finalCandidate.append(Candidate[i])

    # Calculate the rest of the parameters like count, MIN MIS for each candidates
    line9_16.line9_16(SequenceDataset, finalCandidate, MIS)

    return finalCandidate, final_seq1_del, final_seq2_del, minSupp_seq