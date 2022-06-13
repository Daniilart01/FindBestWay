import java.util.*;

public class Algorithm {
    public static void minPriceAndTimeAlgo(List<Train> trains, List<List<Train>> cheapestWays,List<List<Train>> fastestWays){
        //all stations without repetition
        List<Integer> stations = trains.stream().map(Train::getDepartureStation).distinct().sorted().toList();

        // three-dimensional list of the cheapest trains from each station to each(2-nd dimension - 6 lists with trains
        // from x-station to others, 3-nd dimension - list with all trains from x-station to y-station )
        List<List<List<Train>>> trainForEachTrip = new ArrayList<>();

        //splitting 250 trains, completing the list trainForEachTrip
        groupTrains(trainForEachTrip, stations,trains);

        //the cheapest and fastest ways for every station`s permutation
        List<List<Train>> theCheapestTrips = new ArrayList<>();
        List<List<Train>> theFastestTrips = new ArrayList<>();

        //process of finding and filling the cheapest and fastest trips
        for (int a = 0; a < 6; a++) {
            for (int b = 0; b < 6; b++) {
                if(b==a) continue;
                List<Train> cheapestTrains_1 = Train.findMinTrainsPrice(trainForEachTrip.get(a).get(b));
                List<Train> fastestTrains_1 =Train.findMinTrainsTime(trainForEachTrip.get(a).get(b));
                if(cheapestTrains_1 == null) continue;
                for (int c = 0; c < 6; c++) {
                    if(c==b||c==a) continue;
                    List<Train> cheapestTrains_2 = Train.findMinTrainsPrice(trainForEachTrip.get(b).get(c));
                    List<Train> fastestTrains_2 =Train.findMinTrainsTime(trainForEachTrip.get(b).get(c));
                    if(cheapestTrains_2 == null) continue;
                    for (int d = 0; d < 6; d++) {
                        if(d==c||d==b||d==a) continue;
                        List<Train> cheapestTrains_3 = Train.findMinTrainsPrice(trainForEachTrip.get(c).get(d));
                        List<Train> fastestTrains_3 =Train.findMinTrainsTime(trainForEachTrip.get(c).get(d));
                        if(cheapestTrains_3 == null) continue;
                        for (int e = 0; e < 6; e++) {
                            if(e==d||e==c||e==b||e==a)continue;
                            List<Train> cheapestTrains_4 = Train.findMinTrainsPrice(trainForEachTrip.get(d).get(e));
                            List<Train> fastestTrains_4 =Train.findMinTrainsTime(trainForEachTrip.get(d).get(e));
                            if(cheapestTrains_4 == null) continue;
                            for (int f = 0; f < 6; f++) {
                                if(f==e||f==d||f==c||f==b||f==a) continue;
                                List<Train> cheapestTrains_5 = Train.findMinTrainsPrice(trainForEachTrip.get(e).get(f));
                                List<Train> fastestTrains_5 =Train.findMinTrainsTime(trainForEachTrip.get(e).get(f));
                                if(cheapestTrains_5 == null) continue;

                                for (Train train1 : cheapestTrains_1) {
                                    for (Train train2 : cheapestTrains_2) {
                                        for (Train train3 : cheapestTrains_3) {
                                            for (Train train4 : cheapestTrains_4) {
                                                for (Train train5 : cheapestTrains_5) {
                                                    theCheapestTrips.add(new ArrayList<>());
                                                    theCheapestTrips.get(theCheapestTrips.size() - 1).addAll(List.of(train1, train2, train3, train4, train5));
                                                }
                                            }
                                        }
                                    }
                                }

                                for (Train train1 : Objects.requireNonNull(fastestTrains_1)) {
                                    for (Train train2 : Objects.requireNonNull(fastestTrains_2)) {
                                        for (Train train3 : Objects.requireNonNull(fastestTrains_3)) {
                                            for (Train train4 : Objects.requireNonNull(fastestTrains_4)) {
                                                for (Train train5 : Objects.requireNonNull(fastestTrains_5)) {
                                                    theFastestTrips.add(new ArrayList<>());
                                                    theFastestTrips.get(theFastestTrips.size() - 1).addAll(List.of(train1, train2, train3, train4, train5));
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        //mapping index of trip in theCheapestTrips and theFastestList lists to it`s price and durability
        Map<Integer, Double> mapWithPrices = new HashMap<>();
        Map<Integer, Integer> mapWithTime = new HashMap<>();
        theCheapestTrips.forEach(list -> {
            double i = list.stream().mapToDouble(Train::getPrice).sum();
            mapWithPrices.put(theCheapestTrips.indexOf(list), i);
        });
        theFastestTrips.forEach(list -> {
            int i = list.stream().mapToInt(Train::getTravelTime).sum();
            mapWithTime.put(theFastestTrips.indexOf(list), i);
        });

        // finding min value in map and saving indexes of the cheapest and fastest trips
        double minPrice = mapWithPrices.values().stream().min(Double::compareTo).get();
        double minTime = mapWithTime.values().stream().min(Integer::compareTo).get();
        List<Integer> resultPriceIndexes = new ArrayList<>();
        List<Integer> resultTimeIndexes = new ArrayList<>();
        for (Integer integer : mapWithPrices.keySet()) {
            if(mapWithPrices.get(integer)==minPrice)
                resultPriceIndexes.add(integer);
        }
        for (Integer integer : mapWithTime.keySet()) {
            if(mapWithTime.get(integer)==minTime)
                resultTimeIndexes.add(integer);
        }


        for (int i = 0; i < resultPriceIndexes.size(); i++) {
            cheapestWays.add(theCheapestTrips.get(resultPriceIndexes.get(0)));
        }
        for (int i = 0; i < resultTimeIndexes.size(); i++) {
            fastestWays.add(theFastestTrips.get(resultTimeIndexes.get(0)));
        }
    }

    private static void groupTrains(List<List<List<Train>>> trainForEachTrip, List<Integer> stations, List<Train> trains){
        for (int i = 0; i < 6; i++) {
            trainForEachTrip.add(new ArrayList<>());
            for (int j = 0; j < 6; j++) {
                if (i==j){
                    trainForEachTrip.get(trainForEachTrip.size()-1).add(new ArrayList<>());
                    continue;
                }
                int stationDeparture = stations.get(i);
                int stationArrival = stations.get(j);
                trainForEachTrip.get(trainForEachTrip.size()-1).add(trains.stream().filter(train -> train.getDepartureStation() == stationDeparture&&train.getArrivalStation()==stationArrival).toList());
            }
        }
    }
    public static void printCheapestWays(List<List<Train>> ways){
        for (int i = 0; i < ways.size(); i++) {
            System.out.println("Way "+(i+1)+":");
            double sum = 0;
            for (Train train : ways.get(i)) {
                sum+=train.getPrice();
                System.out.print("Train "+train.getNumber()+" from station "+train.getDepartureStation()+" to station "+train.getArrivalStation()+", price: "+train.getPrice()+"\n");
            }
            sum = Math.round(sum*100.0)/100.0;
            System.out.print("Total cost: "+sum+"\n");
        }
    }
    public static void printFastestWays(List<List<Train>> ways){
        System.out.println("The fastest ways:");
        for (int i = 0; i < ways.size(); i++) {
            System.out.println("Way "+(i+1)+":");
            int sum = 0;
            for (Train train : ways.get(0)) {
                sum+=train.getTravelTime();
                System.out.print("Train "+train.getNumber()+" from station "+train.getDepartureStation()+" to station "+train.getArrivalStation()+", durability: "+train.getTravelTime()+" minutes\n");
            }

            System.out.print("Total durability: "+sum/60+"h " +sum%60+"min"+"\n");
        }
    }
}
