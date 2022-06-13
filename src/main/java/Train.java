import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class Train{
    private int number;
    private int departureStation;
    private int arrivalStation;
    private double price;
    private int travelTime;

    @Override
    public String toString() {
        return "Train{" +
                "number=" + number +
                ", departureStation=" + departureStation +
                ", arrivalStation=" + arrivalStation +
                ", price=" + price +
                ", travelTime=" + travelTime +
                '}';
    }

    public static int calculateTravelTime(LocalTime timeDeparture, LocalTime timeArrival){
        if (timeDeparture.isBefore(timeArrival)){
            return (int) timeDeparture.until(timeArrival, ChronoUnit.MINUTES);
        }
        else{
            int timeToMidnight = (int) timeDeparture.until(LocalTime.parse("23:59:00"), ChronoUnit.MINUTES) + 1;
            return ((int)LocalTime.MIDNIGHT.until(timeArrival, ChronoUnit.MINUTES)) + timeToMidnight;
        }
    }

    public static List<Train> findMinTrainsPrice(List<Train> trains){
        Optional<Train> optionalTrainWithMinPrice = trains.stream().min(Train::compareToPrice);
        if(optionalTrainWithMinPrice.isEmpty()) return null;
        double minPrice = optionalTrainWithMinPrice.get().getPrice();
        List<Train> cheapestTrains = new ArrayList<>();
        for (Train train : trains) {
            if(train.getPrice() == minPrice){
                cheapestTrains.add(train);
            }
        }
        return cheapestTrains;
    }
    public static List<Train> findMinTrainsTime(List<Train> trains){
        Optional<Train> optionalTrainWithMinTime = trains.stream().min(Train::compareToTime);
        if(optionalTrainWithMinTime.isEmpty()) return null;
        double minTime = optionalTrainWithMinTime.get().getTravelTime();
        List<Train> fastestTrains = new ArrayList<>();
        for (Train train : trains) {
            if(train.getTravelTime() == minTime){
                fastestTrains.add(train);
            }
        }
        return fastestTrains;
    }
    public int compareToPrice(Train o) {
        return Double.compare(this.getPrice(), o.getPrice());
    }

    public int compareToTime(Train o) {
        return Integer.compare(this.getTravelTime(), o.getTravelTime());
    }
}
