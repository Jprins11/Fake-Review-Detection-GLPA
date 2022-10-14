#imports
import tensorflow as tf
import numpy as np 
import tensorflow_hub as hub
import functions as f

# In this file we define the RoBERTa algorithm.
# We put in all the reviews in batches of 100 to prevent the memory from overflowing


reviews, reviewspecs, realorfake = f.importingReviews('NN_TestData.txt')

# define a text embedding model
text_input = tf.keras.layers.Input(shape=(), dtype=tf.string)
preprocessor = hub.KerasLayer("https://tfhub.dev/jeongukjae/roberta_en_cased_preprocess/1")
encoder_inputs = preprocessor(text_input)

encoder = hub.KerasLayer("https://tfhub.dev/jeongukjae/roberta_en_cased_L-12_H-768_A-12/1")
encoder_outputs = encoder(encoder_inputs)

pooled_output = encoder_outputs["pooled_output"]
print(pooled_output)    # [batch_size, 768].
sequence_output = encoder_outputs["sequence_output"]  # [batch_size, seq_length, 768].

model = tf.keras.Model(text_input, pooled_output)


def modelling(model,input):
      sentences = tf.constant(input)
      numberRepesentation = model(sentences).numpy()
      numberRepesentation = numberRepesentation.tolist()
      return numberRepesentation

index = 0 
numberRepresentation = []
extension =[]
specs = []
numberofbatches = len(reviews) / 100

# Batches from 100 are put in to the model and are written to files for later use.
with open('Numberrepresentation3.txt','w') as file:
      while len(reviews) > 0:
            inputformodel = []
            if len(reviews) < 100:
                  for i in range(len(reviews)):
                        inputformodel.append(reviews.pop())
                        specs.extend(reviewspecs.pop())
                        meanwordlength_z = 1/(1 + np.exp(-specs.pop()))
                        sentencelength_z = 1/(1 + np.exp(-specs.pop()))                  
                        counts_z = 1/(1 + np.exp(-specs.pop()))
                        extension.append([counts_z,sentencelength_z,meanwordlength_z])
            else:
                  for i in range(100):
                        inputformodel.append(reviews.pop())
                        specs.extend(reviewspecs.pop())
                        meanwordlength_z = 1/(1 + np.exp(-specs.pop()))
                        sentencelength_z = 1/(1 + np.exp(-specs.pop()))                  
                        counts_z = 1/(1 + np.exp(-specs.pop()))
                        extension.append([counts_z,sentencelength_z,meanwordlength_z])
            outputfrommodel = modelling(model,inputformodel) 
            for i in range(len(outputfrommodel)):

                  outputfrommodel[-1].extend(extension[-1])
                  file.write(str(outputfrommodel.pop()) + '\n')
            index += 1
            print(f"batch {index} out of {int(numberofbatches)} is done!")
      
file.close()