import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int testCases = input.nextInt();

        while (testCases-- > 0) {
            int numberOfVehicles = input.nextInt();
            int numberOfEvents = input.nextInt();

            Map<String, VehicleStats> vehicles = new HashMap<>();

            while (numberOfVehicles-- > 0) {
                String vehicleType = input.next();
                int catalogPrice = input.nextInt();
                int pickupPrice = input.nextInt();
                int costPerKilometer = input.nextInt();
                VehicleStats vehicleStats = new VehicleStats(catalogPrice, pickupPrice, costPerKilometer);
                vehicles.put(vehicleType, vehicleStats);
            }

            List<Spy> spies = new ArrayList<>();

            while (numberOfEvents-- > 0) {
                int timeOfEvent = input.nextInt();
                String name = input.next();
                char typeOfEvent = input.next().charAt(0);
                Spy spy;
                if (typeOfEvent == 'p') {
                    String vehicleType = input.next();
                    spy = new Spy(name, timeOfEvent, typeOfEvent, vehicleType);
                } else {
                    int value = input.nextInt();
                    spy = new Spy(name, timeOfEvent, typeOfEvent, value);
                }
                spies.add(spy);
            }

            Collections.sort(spies);

            Map<String, String> rented = new HashMap<>();
            TreeMap<String, Integer> payment = new TreeMap<>();

            for (Spy spy : spies) {
                if ((spy.typeOfEvent == 'a' && rented.get(spy.name) == null) || (spy.typeOfEvent == 'r' && rented.get(spy.name) == null)) {
                    payment.put(spy.name, Integer.MIN_VALUE);
                    continue;
                }
                if (spy.typeOfEvent == 'p') {
                    rented.put(spy.name, spy.vehicleType);
                    payment.put(spy.name, vehicles.get(spy.vehicleType).pickupPrice);
                }
                if (spy.typeOfEvent == 'a') {
                    int bill = (int) (payment.get(spy.name) + vehicles.get(rented.get(spy.name)).catalogPrice * spy.value /100.00);
                    payment.put(spy.name, bill);
                }
                if (spy.typeOfEvent == 'r') {
                    int bill = payment.get(spy.name) + vehicles.get(rented.get(spy.name)).costPerKilometer * spy.value;
                    payment.put(spy.name, bill);
                    rented.put(spy.name, null);
                }
            }
            payment.forEach((spyName, bill) -> {
                if (rented.get(spyName) != null || bill == Integer.MIN_VALUE) {
                    System.out.println(spyName + " INCONSISTENT");
                } else {
                    System.out.println(spyName + " " + bill);
                }
            });
        }
        input.close();
    }

    static class VehicleStats {
        int catalogPrice;
        int pickupPrice;
        int costPerKilometer;

        public VehicleStats(int catalogPrice, int pickupPrice, int costPerKilometer) {
            this.catalogPrice = catalogPrice;
            this.pickupPrice = pickupPrice;
            this.costPerKilometer = costPerKilometer;
        }

        public String toString() {
            return catalogPrice + " " + pickupPrice + " " + costPerKilometer;
        }
    }

    static class Spy implements Comparable<Spy> {
        String name;
        int timeOfEvent;
        char typeOfEvent;
        String vehicleType;
        int value;

        public Spy(String name, int timeOfEvent, char typeOfEvent, String vehicleType) {
            this.name = name;
            this.timeOfEvent = timeOfEvent;
            this.typeOfEvent = typeOfEvent;
            this.vehicleType = vehicleType;
        }

        public Spy(String name, int timeOfEvent, char typeOfEvent, int value) {
            this.name = name;
            this.timeOfEvent = timeOfEvent;
            this.typeOfEvent = typeOfEvent;
            this.value = value;
        }

        public String toString() {
            return name + " " + timeOfEvent + " " + typeOfEvent + " " + value;
        }

        @Override
        public int compareTo(Spy spy) {
            return this.timeOfEvent - spy.timeOfEvent;
        }
    }
}