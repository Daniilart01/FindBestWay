import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class Main {


    public static void main(String[] args) {

        List<Train> trains = new ArrayList<>();
        //scan data from file
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/test_task_data.csv"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] params = line.split("[;}]");
                trains.add(new Train(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]), Double.parseDouble(params[3]), Train.calculateTravelTime(LocalTime.parse(params[4]), LocalTime.parse(params[5]))));
            }
        } catch (IOException e) {
            System.err.println("Error reading data");
            return;
        }
        //list with results
        List<List<Train>> cheapestWays = new ArrayList<>();
        List<List<Train>> fastestWays = new ArrayList<>();

        //run algo
        Algorithm.minPriceAndTimeAlgo(trains,cheapestWays,fastestWays);

        //print results
        Algorithm.printCheapestWays(cheapestWays);
        Algorithm.printFastestWays(fastestWays);
    }
}
