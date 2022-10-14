import pandas as pd
import ast

#Function to load in the review which are defined in the java program.
def importingReviews(filename):
    print('Importing reviews form textFile ...')
    csvloader = pd.read_csv(filename)
    print('Done')
    count = csvloader['count'].tolist()
    realorfake = csvloader['realorfake'].tolist()
    sentencelength = csvloader['sentencelength'].tolist()
    meanwordlength = csvloader['meanwordlength'].tolist()
    reviewSpecs = []
    for i in range(len(count)):
        array = [count[i],sentencelength[i],meanwordlength[i]]
        reviewSpecs.append(array)

    return csvloader['Reviewtext'].tolist(), reviewSpecs, realorfake

# Reading in the files with the RoBERTa output for the follow-Up Neural network.
def readingin(filename):
    with open(filename, 'r') as file:
      numberRepresentation = file.readlines()
      for i in range(len(numberRepresentation)):
            numberRepresentation[i] = ast.literal_eval(numberRepresentation[i])
            numberRepresentation[i] = numberRepresentation[i][0:768]

    return numberRepresentation

def readingin2(filename):
    reader = pd.read_csv(filename)
    realorfake = reader['realorfake'].tolist()
    realorfake.reverse()
    return realorfake