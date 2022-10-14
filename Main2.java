import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Main2 {
    private static long realWordCount;
    private static long fakeWordCount;
    //splits for the  analysis
    private static final int splitBuildingGraph = 21000 / 2;
    private static final int splitControlGraph = 210;
    private static final int splitNeuralNetworkBuild = 4200;
    private static final int splitNeuralNetworkValidation = 4200;
    private static final int splitNeuralNetworkTest = 1890;

    public static void main(String[] args) throws IOException {
        System.out.println("Starting Program ...");
        Scanner scanner = new Scanner(new File("Reviews.txt"));
        scanner.nextLine(); // skip first line because of labels.
        System.out.println("Loading and pre-processing Data ...");

        List<List<String>> realReviews = new ArrayList<>();
        List<List<String>> fakeReviews = new ArrayList<>();
        processData(scanner, realReviews, fakeReviews); //Fill the list with random reviews


        System.out.println("Creating and filling graphs ...");
        HashMap<String, List<Tuple>> realMap = new HashMap<>();
        HashMap<String, List<Tuple>> fakeMap = new HashMap<>();
        realWordCount = fillMap(realReviews, realMap); // Fill the hashmaps with keys and connection counts
        fakeWordCount = fillMap(fakeReviews, fakeMap);

        FileWriter out = new FileWriter("realMapKeysAndTuples.txt");
        for (String key : realMap.keySet()) {
            out.write(key + " : " + realMap.get(key) + System.lineSeparator());
        }
        out.close();

        FileWriter out2 = new FileWriter("fakeMapKeysAndTuples.txt");
        for (String key : fakeMap.keySet()) {
            out2.write(key + " : " + fakeMap.get(key) + System.lineSeparator());
        }
        out2.close();

        System.out.println("realWordCount = " + realWordCount);
        System.out.println("fakeWordCount = " + fakeWordCount);

        System.out.println("Analyzing data ...");
        analyseData(realReviews, fakeReviews, realMap, fakeMap); // analyzing data and printing accuracy
        System.out.println("Process ended.");
    }

    private static void analyseData(List<List<String>> realReviews,
                                    List<List<String>> fakeReviews, HashMap<String,
            List<Tuple>> realMap,
                                    HashMap<String, List<Tuple>> fakeMap) throws IOException {
        Random random = new Random();

        List<Boolean> realCheck = new ArrayList<>();
        List<List<String>> reviewsToCheck = new ArrayList<>();
        for (int i = 0; i < 10500; i++) {
            if (random.nextInt() % 2 == 0) {
                reviewsToCheck.add(realReviews.get(random.nextInt(realReviews.size())));
                realCheck.add(true);
            } else {
                realCheck.add(false);
                reviewsToCheck.add(fakeReviews.get(random.nextInt(fakeReviews.size())));
            }
        }
        List<Integer> counts = sumCounts(reviewsToCheck, realMap, fakeMap);
        int correctAmount = 0;

        //Writing the files for later use in python file.
        filewriting("NN_trainData.txt", reviewsToCheck, counts, realCheck, splitNeuralNetworkBuild);

        filewriting("NN_testData.txt", reviewsToCheck, counts, realCheck, splitNeuralNetworkTest);

        filewriting("NN_validationData.txt", reviewsToCheck, counts, realCheck, splitNeuralNetworkValidation);

        //File for are the wrongly classified reviews. For follow-up analysis
        FileWriter out = new FileWriter("checkedreviews.txt");
        out.write("ID,Reviewtext,count,realorfake,sentencelength,meanwordlength" + System.lineSeparator());

        for (int i = 0; i < splitControlGraph; i++) {
            int offset = 0;
            if (realCheck.get(i) != counts.get(i) >= offset) {

                float meanWordSum = 0;
                for (String element : reviewsToCheck.get(i)) {
                    meanWordSum += element.length();
                }

                float meanWordLengthSentence = meanWordSum / (float) reviewsToCheck.get(i).toArray().length;
                String checkedReviewsString = String.join(" ", reviewsToCheck.get(i));

                out.write(i + "," + checkedReviewsString + "," + counts.get(i) + "," + (realCheck.get(i) ? 1 : 0) + "," +
                        reviewsToCheck.get(i).toArray().length + "," + meanWordLengthSentence + System.lineSeparator());
            }

            if (counts.get(i) >= offset == realCheck.get(i)) {
                correctAmount++;

            }
            //If you want to print the results of the 210 reviews uncomment this line underneath.
            //System.out.println("#" + (i + 1) + " is " + (realCheck.get(i) ? "real" : "fake") + " and was noted as " + (counts.get(i) >= offset ? "real" : "fake") + " with a count of: " + counts.get(i));

        }
        out.close();
        System.out.println("Accuracy: " + ((float) correctAmount) / ((float) splitControlGraph) * 100f);
    }

    private static void filewriting(String fileName, List<List<String>> reviewsToCheck,
                                    List<Integer> counts, List<Boolean> realCheck, int split) throws IOException {
        FileWriter outToFile = new FileWriter(fileName);
        outToFile.write("ID,Reviewtext,count,realorfake,sentencelength,meanwordlength" + System.lineSeparator());
        Random RNG = new Random();
        for (int i = 0; i < split; i++) {
            float meanWordSum = 0;
            int random = RNG.nextInt(reviewsToCheck.size());
            int count = counts.remove(random);
            boolean realCheck1 = realCheck.remove(random);

            List<String> reviewToCheck = reviewsToCheck.remove(random);
            for (String element : reviewToCheck) {
                meanWordSum += element.length();
            }

            float meanWordLengthSentence = meanWordSum / (float) reviewToCheck.size();
            String checkedReviewsString = String.join(" ", reviewToCheck);

            outToFile.write(random + "," + checkedReviewsString + "," + count + "," + (realCheck1 ? 1 : 0) + "," +
                    reviewToCheck.toArray().length + "," + meanWordLengthSentence + System.lineSeparator());


        }
        outToFile.close();
    }


    //Method to process the data by replacing links, characters and other useless figures using regular expression.
    private static void processData(Scanner scanner, List<List<String>> realReviews, List<List<String>> fakeReviews) throws IOException {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase();
            String[] splitLine = line.split("\t");
            String review = splitLine[8];

            review = review.replaceAll("&#34;", " ")
                    .replaceAll("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)", " ")
                    .replaceAll("(<br />)", " ")
                    .replaceAll("[^a-zA-Z0-9']", " ");
            List<String> words = Arrays.asList(review.split("[ ]+"));

            //Filling the lists with sorted data.
            if (splitLine[1].equals("__label1__")) {
                realReviews.add(words);
            } else {
                fakeReviews.add(words);
            }
        }
    }


    //Method to calculate the fake score of each review that is tested.
    private static List<Integer> sumCounts(List<List<String>> reviews, HashMap<String, List<Tuple>> realMap, HashMap<String, List<Tuple>> fakeMap) {
        List<Integer> sums = new ArrayList<>();
        for (List<String> review : reviews) {
            int sum = 0;
            int realSum = 0;
            int fakeSum = 0;
            for (int j = 0; j < review.size() - 1; j++) {
                Tuple realMapTuple = assignTuple(realMap, review, j);
                Tuple fakeMapTuple = assignTuple(fakeMap, review, j);
                if (realMapTuple == null) {
                    if (fakeMapTuple != null) {
                        sum -= 1;
                    }
                } else {
                    if (fakeMapTuple == null) {
                        sum += 1;
                    } else {
                        if (((long) realMapTuple.getCount()) * fakeWordCount / realWordCount > fakeMapTuple.getCount()) { //Normalizing for the wordcount.
                            sum += 1;
                        } else {
                            sum -= 1;
                        }
                    }
                }
            }
            sums.add(sum);
        }
        return sums;
    }

    //Method to look for tuple in map and return if it exists. Else return null.
    private static Tuple assignTuple(HashMap<String, List<Tuple>> map, List<String> review, int j) {
        if (map.containsKey(review.get(j))) {
            final int finalJ = j;
            List<Tuple> filteredTuples = map.get(review.get(j)).stream()
                    .filter(tuple -> tuple.getString().equals(review.get(finalJ + 1)))
                    .collect(Collectors.toList());
            if (filteredTuples.size() > 0) {
                return filteredTuples.get(0);
            }
        }
        return null;
    }

    //Method to fill the maps with randomly selected review data. Takes a random review checks for every word in the review if it's in the map.
    //If it exists in the map it will only increase the count of the word.
    //If the word doesn't exist in the map it will make a new entry and then increase the count.
    private static long fillMap(List<List<String>> reviews, HashMap<String, List<Tuple>> map) throws IOException {
        Random random = new Random();
        long wordCount = 0;

        for (int i = 0; i < splitBuildingGraph / 2; i++) {
            int index = random.nextInt(reviews.size());
            List<String> review = reviews.get(index);
            wordCount += review.size();

            for (int j = 0; j < review.size() - 1; j++) {

                if (map.containsKey(review.get(j))) {
                    List<Tuple> tuples = map.get(review.get(j));
                    final int finalJ = j; //create final for the stream method.
                    List<Tuple> filteredTuples = tuples.stream()
                            .filter(tuple -> tuple.getString().equals(review.get(finalJ + 1)))
                            .collect(Collectors.toList());

                    if (filteredTuples.size() == 0) {
                        List<Tuple> tuplesCopy = new ArrayList<>(tuples);
                        tuplesCopy.add(new Tuple(review.get(j + 1)));
                        map.replace(review.get(j), tuplesCopy);
                    } else {
                        filteredTuples.get(0).addCount();
                    }
                } else {
                    map.put(review.get(j), List.of(new Tuple(review.get(j + 1))));
                }
            }
            reviews.remove(index);
        }
        return wordCount;
    }

    //Class for a tuple with a string and a count. These will be added in the map.
    public static class Tuple {
        private final String string;
        private int count;

        public Tuple(String string) {
            this.string = string;
            this.count = 1;
        }

        public String getString() {
            return string;
        }

        public int getCount() {
            return count;
        }

        public void addCount() {
            this.count++;
        }

        @Override
        public String toString() {
            return "Tuple{" +
                    "string='" + string + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}

