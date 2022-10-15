import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class outlierDetection {

    public static void main(String[] args) throws FileNotFoundException {   	
    	String fileCheckedReviews = "checkedreviews.txt";
        String filefakeReviewTuples = "fakeMapKeysAndTuples.txt";
        String filerealReviewTuples = "realMapKeysAndTuples.txt";
        String fileOutliers = "outliers.txt";

        Scanner inputFileFCR = new Scanner(new File(fileCheckedReviews));
        Scanner inputFileFRT = new Scanner(new File(filefakeReviewTuples));
        Scanner inputFileRRT = new Scanner(new File(filerealReviewTuples));
        Scanner outlierFile = new Scanner(new File(fileOutliers));

        Map<String, List<String>> checkedReviews = new HashMap<>();
        Map<String, List<String>> tuplesReal = new HashMap<>();
        Map<String, List<String>> tuplesFake = new HashMap<>();
        Map<String, List<String>> outliers = new HashMap<>();

        ArrayList<String> reviewNumbers = new ArrayList<>();
        ArrayList<String> outlierNumbers = new ArrayList<>();

        readFileCheckedReviews(inputFileFCR, checkedReviews, reviewNumbers);
        readFileReadTuples(inputFileFRT, tuplesFake);
        readFileReadTuples(inputFileRRT,tuplesReal);  
        readFileCheckedReviews(outlierFile,outliers,outlierNumbers); 
        

        for (int i = 0; i < checkedReviews.size(); i++) {
            String reviewNumber = reviewNumbers.get(i);
            String review = checkedReviews.get(reviewNumber).get(0);

            String[] wordsOnly = review.split(" ");
            String[][] data = new String[wordsOnly.length-1][4];
            String[][] adjustedData = new String[wordsOnly.length-1][4];

            for (int j = 0; j < wordsOnly.length-1; j++) {
                data[j][0] = wordsOnly[j];
                data[j][1] = wordsOnly[j+1];

                adjustedData[j][0] = wordsOnly[j];
                adjustedData[j][1] = wordsOnly[j+1];
                
                String toLookFor = wordsOnly[j+1]; ;

                boolean found = false;
                int indexReal = 0;

                List<String> tempReal = new ArrayList<String>();

                if (!tuplesReal.containsKey(wordsOnly[j])) {
                    tempReal.add(0, "0");
                }
                else {
                    tempReal = tuplesReal.get(wordsOnly[j]);
                }


                while (!found) {

                    if (indexReal > tempReal.size()-2) {
                        data[j][2] = "0";
                        adjustedData[j][2] = "0";
                        found = true;
                    }
                    else if (tempReal.get(indexReal).contentEquals(toLookFor)) {
                        data[j][2] = tuplesReal.get(wordsOnly[j]).get(indexReal+1);
                        int intermediate  = 0; 
                        
                        if (!tuplesReal.get(wordsOnly[j]).get(indexReal+1).contentEquals("0")) {
                        	intermediate = Integer.parseInt(tuplesReal.get(wordsOnly[j]).get(indexReal+1)); 
                        }
                        adjustedData[j][2] = String.valueOf(intermediate); 
                        found = true;
                    }

                    indexReal = indexReal + 2;
                }

                boolean found2 = false;
                int indexFake = 0;

                List<String> tempFake = new ArrayList<String>();

                if (!tuplesFake.containsKey(wordsOnly[j])) {
                    tempFake.add(0, "0");
                }
                else {
                    tempFake = tuplesFake.get(wordsOnly[j]);
                }

                String toLookFor2 = wordsOnly[j+1];

                while (!found2) {

                    if (indexFake > tempFake.size()-2) {
                        data[j][3] = "0";
                        adjustedData[j][3] = "0";
                        found2 = true;
                    }
                    else if (tempFake.get(indexFake).contentEquals(toLookFor2)) {
                        data[j][3] = tuplesFake.get(wordsOnly[j]).get(indexFake+1); 
                        int intermediate  = 0; 
                        
                        if (!tuplesFake.get(wordsOnly[j]).get(indexFake+1).contentEquals("0")) {
                        	intermediate = Integer.parseInt(tuplesFake.get(wordsOnly[j]).get(indexFake+1)); 
                        }
                        adjustedData[j][3] = String.valueOf(intermediate);  
                        found2 = true;
                    }

                    indexFake = indexFake + 2;
                }

            }
            //System.out.println("Review " + reviewNumber + ":   " + Arrays.deepToString(adjustedData));
            
        }

        System.out.println(); 
        
        for (int i = 0; i < outliers.size(); i++) {
        	int count = 0; 
        	
        	String reviewNumber = outlierNumbers.get(i);
            String review = outliers.get(reviewNumber).get(0);

            String[] wordsOnly = review.split(" ");
            String[][] data = new String[wordsOnly.length-1][4];
            String[][] adjustedData = new String[wordsOnly.length-1][4];

            for (int j = 0; j < wordsOnly.length-1; j++) {
                data[j][0] = wordsOnly[j];
                data[j][1] = wordsOnly[j+1];

                adjustedData[j][0] = wordsOnly[j];
                adjustedData[j][1] = wordsOnly[j+1];
                
                String toLookFor = wordsOnly[j+1]; ;

                boolean found = false;
                int indexReal = 0;

                List<String> tempReal = new ArrayList<String>();

                if (!tuplesReal.containsKey(wordsOnly[j])) {
                    tempReal.add(0, "0");
                }
                else {
                    tempReal = tuplesReal.get(wordsOnly[j]);
                }


                while (!found) {

                    if (indexReal > tempReal.size()-2) {
                        data[j][2] = "0";
                        adjustedData[j][2] = "0";
                        found = true;
                    }
                    else if (tempReal.get(indexReal).contentEquals(toLookFor)) {
                        data[j][2] = tuplesReal.get(wordsOnly[j]).get(indexReal+1);
                        int intermediate  = 0; 
                        
                        if (!tuplesReal.get(wordsOnly[j]).get(indexReal+1).contentEquals("0")) {
                        	intermediate = Integer.parseInt(tuplesReal.get(wordsOnly[j]).get(indexReal+1)); 
                        }
                        adjustedData[j][2] = String.valueOf(intermediate); 
                        found = true;
                    }

                    indexReal = indexReal + 2;
                }

                boolean found2 = false;
                int indexFake = 0;

                List<String> tempFake = new ArrayList<String>();

                if (!tuplesFake.containsKey(wordsOnly[j])) {
                    tempFake.add(0, "0");
                }
                else {
                    tempFake = tuplesFake.get(wordsOnly[j]);
                }

                String toLookFor2 = wordsOnly[j+1];

                while (!found2) {

                    if (indexFake > tempFake.size()-2) {
                        data[j][3] = "0";
                        adjustedData[j][3] = "0";
                        found2 = true;
                    }
                    else if (tempFake.get(indexFake).contentEquals(toLookFor2)) {
                        data[j][3] = tuplesFake.get(wordsOnly[j]).get(indexFake+1); 
                        int intermediate  = 0; 
                        
                        if (!tuplesFake.get(wordsOnly[j]).get(indexFake+1).contentEquals("0")) {
                        	intermediate = Integer.parseInt(tuplesFake.get(wordsOnly[j]).get(indexFake+1)); 
                        }
                        adjustedData[j][3] = String.valueOf(intermediate);  
                        found2 = true;
                    }

                    indexFake = indexFake + 2;
                }
                
                int difference = Integer.parseInt(adjustedData[j][3]) -  Integer.parseInt(adjustedData[j][2]);
                if (difference <= 0) {
                	count = count + 1; 
                
                }
            

            }
            System.out.println(count); 
            System.out.println("Review " + reviewNumber + ":   " + Arrays.deepToString(adjustedData));
            
            
        }

    }


    //private static void readFileCheckedReviews(Scanner scanner, Map<String, List<String>> fileName, ArrayList<String> numbers, ArrayList<String> categorized) {
    private static void readFileCheckedReviews(Scanner scanner, Map<String, List<String>> fileName, ArrayList<String> numbers) {  
    	while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase();

            String[] splittedLine = line.split(",");

            List<String> temp = new ArrayList<String>();
            for (int i = 0; i < splittedLine.length-1; i++) {
                temp.add(splittedLine[i+1]);
            }
            //categorized.add(splittedLine[3]);
            fileName.put(splittedLine[0],temp);
            numbers.add(splittedLine[0]); 
        }
    }
    
    
    //Method to process the data by replacing links, characters and other useless figures using regular expression.
    private static void readFileReadTuples(Scanner scanner, Map<String, List<String>> fileName) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase();

            String[] splittedLine = line.split(" : ");
            String numberOfReview = splittedLine[0];
            String tupleRelations = splittedLine[1];

            tupleRelations = tupleRelations.replaceAll("\\[", "");
            tupleRelations = tupleRelations.replaceAll("]", "");
            tupleRelations = tupleRelations.replaceAll("\\{", "");
            tupleRelations = tupleRelations.replaceAll("}", "");
            tupleRelations = tupleRelations.replaceAll("Tuple", "");
            tupleRelations = tupleRelations.replaceAll("tuple", "");
            tupleRelations = tupleRelations.replaceAll("string", "");
            tupleRelations = tupleRelations.replaceAll(" ", "");
            tupleRelations = tupleRelations.replaceAll("count", "");
            tupleRelations = tupleRelations.replaceAll("=", "");
            tupleRelations = tupleRelations.replaceAll("'", "");

            String[] relations = tupleRelations.split(",");

            List<String> temp = new ArrayList<String>();
            for (int i = 0; i < relations.length; i++){
                temp.add(relations[i]);
            }
            fileName.put(numberOfReview,temp);
        }
    }
    
    private static ArrayList<String> outlierDiagnostics(String[][] adjustedData, String classification) {
    	ArrayList<String> toReturn = new ArrayList<>(); 
    	//String[][] toReturn = new String[adjustedData.length][2];  
    
    		
    	return toReturn; 
    }

}


    

