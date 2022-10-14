import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Outliers {

    public static void main(String[] args) throws FileNotFoundException {

        List<Integer> counts = new ArrayList<>();
        int totalCount = 0;
        Scanner scanner = new Scanner(new File("checkedreviews.txt"));
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase();
            String[] splitLine = line.split(",");
            int count = Integer.parseInt(splitLine[2]);
            counts.add(count);
            totalCount += count;

        }
        double standard_deviation = 0.0;
        float mean = (float) totalCount / (float)counts.size();
        System.out.println("mean = " + mean);
        for(double temp: counts) {
            standard_deviation += Math.pow(temp - mean, 2);
        }
        double result = Math.sqrt(standard_deviation/counts.size());
        System.out.println("result = " + result);
    }

}

