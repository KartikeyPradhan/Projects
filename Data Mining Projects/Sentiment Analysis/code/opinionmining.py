__author__ = 'Kartikey and Jay'

from preprocessing import preprocessing
import xlrd 

def main():

    ##training
    labels_filename=["obama_labels.txt", "romney_labels.txt"]
    book = xlrd.open_workbook("training-Obama-Romney-tweets.xlsx")
    words_filename=["obama_words.txt", "romney_words.txt"]

    for i in range(0, 2):
        sheet = book.sheet_by_index(i)
        
        #extract data and store it in file featurelist.txt
        preprocessing().dataExtraction(2, sheet.nrows, sheet, labels_filename[i], words_filename[i])


    ##testing
    book_test = xlrd.open_workbook("testing-Obama-Romney-tweets-3labels.xlsx")
    labels_filename_test=["obama_labels_test.txt", "romney_labels_test.txt"]
    words_filename_test=["obama_words_test.txt", "romney_words_test.txt"]

    for i in range(0, 2):
        sheet = book_test.sheet_by_index(i)

        #extract data and store it in file featurelist.txt
        preprocessing().dataExtraction(2, sheet.nrows, sheet, labels_filename_test[i], words_filename_test[i])

main()