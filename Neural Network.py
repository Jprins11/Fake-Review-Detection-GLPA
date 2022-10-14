#Imports
from statistics import mean
import tensorflow as tf
import matplotlib.pyplot as plt
import math
import functions as f

#reading the train, validation and test date or the Neural Network.
x_train =f.readingin("Numberrepresentation.txt")
y_train = f.readingin2("NN_TrainData.txt")
x_val = f.readingin("Numberrepresentation2.txt")
y_val = f.readingin2("NN_validationData.txt")
x_test = f.readingin("Numberrepresentation3.txt")
y_test = f.readingin2("NN_testData.txt")

#Defining the Keras Sequential model.
fakeReviewDetectionModel = tf.keras.Sequential()
fakeReviewDetectionModel.add(tf.keras.layers.Input(shape=(768),name='InputLayer'))
fakeReviewDetectionModel.add(tf.keras.layers.Dropout(0.1,name='dropoutlayer'))
fakeReviewDetectionModel.add(tf.keras.layers.Dense(1,activation='sigmoid',name='OutputLayer'))

METRICS = [
      tf.keras.metrics.BinaryAccuracy(name='accuracy'),
      tf.keras.metrics.Precision(name='precision'),
      tf.keras.metrics.Recall(name='recall')
]

loss_fn = tf.keras.losses.binary_crossentropy
opt = tf.keras.optimizers.Adam(learning_rate=0.01)
fakeReviewDetectionModel.compile(optimizer=opt,
              loss=loss_fn,
              metrics=METRICS)

fakeReviewDetectionModel.summary()

#fitting the model
history = fakeReviewDetectionModel.fit(x_train,y_train,validation_data=(x_val,y_val),epochs=10)

#plotting and printing the result.
print(mean(history.history['loss']))
print(mean(history.history['accuracy']))
print(mean(history.history['val_accuracy']))

plt.plot(history.history['loss'], color='grey')
plt.plot(history.history['accuracy'], color='grey')
plt.plot(history.history['val_accuracy'], color='black')

plt.title('model accuracy')
plt.ylabel('accuracy')
plt.xlabel('epoch')
plt.legend(['loss','train', 'validation'], loc='upper left')
plt.show()

#testing the model.
fakeReviewDetectionModel.evaluate(x_test,y_test)