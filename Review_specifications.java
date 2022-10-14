import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// In this file we calculate the meanWordLength and meanReviewLength of real and fake reviews.

public class Review_specifications{

    public static void main(String[] args) throws FileNotFoundException {
        List<List<String>> realReviews = new ArrayList<>();
        List<List<String>> fakeReviews = new ArrayList<>();

        Scanner scanner = new Scanner(new File("Reviews.txt"));
        int realWordLength = 0;
        int fakeWordLength = 0;
        int realwords = 0;
        int fakewords = 0;
        int realSentenceLength = 0;
        int fakeSentenceLength = 0;


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
                realSentenceLength += words.size();

                for (String word : words) {
                    realWordLength += word.length();
                    realwords += 1;
                }

            } else {
                fakeReviews.add(words);
                fakeSentenceLength += words.size();
                for (String word : words) {
                    fakeWordLength += word.length();
                    fakewords += 1;
                }
            }
        }
        float meanRealWordLength = (float)realWordLength / (float)realwords;
        System.out.println("meanRealWordLength = " + meanRealWordLength);
        float meanFakeWordLength = (float)fakeWordLength / (float)fakewords;
        System.out.println("meanFakeWordLength = " + meanFakeWordLength);
        float meanRealSentenceLength = (float) realSentenceLength / (float) realReviews.size();
        System.out.println("meanRealSentenceLength = " + meanRealSentenceLength);
        float meanFakeSentenceLength = (float) fakeSentenceLength / (float) fakeReviews.size();
        System.out.println("meanFakeSentenceLength = " + meanFakeSentenceLength);;
    }
}

