# Fake-Review-Detection-GLPA
Fake review detection with an additional graphical linguistic pattern (GLPA) analysis in combination with the RoBERTa algorithm.

This code was made to look if the GPLA can improve fake review detection. 

file 1: Main2.java --> In this file the reviews are loaded in and split into different sections. Also the GLPA is run in this file.

file 2: Review_specification.java --> In this file the mean of the review length and word length of the fake and real revies are calculated.

file 3: Outliers.java --> In this file the mean of the realfake-count is calculated of the wrongly classified reviews and the Standard deviation. This to determine how many outliers there are in the worngly classified reviews.

file 4: functions.py --> Written functions to load in the data. 

file 5: RoBERTa.py --> In This file the RoBERTa algorithm is imported from the Tensorflow hub. In this file the reviews are put in to the algorithm in batches.

file 6: Neural Network.py --> In this file the follow-up neural network is defined. 

file 7: Reviews.txt --> This file contains the reviews used in the research. It contains 21000 reviews from which 10500 reviews are fake.

file 8: OutlierDetection.java --> This file does a analysis on the counts of the outliers. This file aims to find patterns in the outliers for optimizing the GLPA.

This file is made by students of the Erasmus Univeristy Rotterdam. 
