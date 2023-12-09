package core;

import models.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

public class VehicleRepositoryImpl implements VehicleRepository {
    private Map<String, Vehicle> vehiclesById = new LinkedHashMap<>();
    private Map<String, LinkedHashSet<Vehicle>> vehiclesBySellerName = new TreeMap<>();
    private Map<String, String> sellerByVehicleId = new LinkedHashMap<>();

    @Override
    public void addVehicleForSale(Vehicle vehicle, String sellerName) {
        if (this.vehiclesById.containsKey(vehicle.getId())) {
            return;
        }

        this.vehiclesBySellerName.putIfAbsent(sellerName, new LinkedHashSet<>());
        this.vehiclesBySellerName.get(sellerName).add(vehicle);
        this.vehiclesById.put(vehicle.getId(), vehicle);
        this.sellerByVehicleId.put(vehicle.getId(), sellerName);
    }

    @Override
    public void removeVehicle(String vehicleId) {
        Vehicle vehicle = this.vehiclesById.remove(vehicleId);
        if (vehicle == null) {
            throw new IllegalArgumentException();
        }
        String seller = this.sellerByVehicleId.get(vehicleId);
        this.vehiclesBySellerName.get(seller).remove(vehicle);
    }

    @Override
    public int size() {
        return this.vehiclesById.size();
    }

    @Override
    public boolean contains(Vehicle vehicle) {
        return this.vehiclesById.containsKey(vehicle.getId());
    }

    @Override
    public Iterable<Vehicle> getVehicles(List<String> keywords) {
        return this.vehiclesById.values()
                .stream()
                .filter((vehicle) -> keywords.contains(vehicle.getBrand()) ||
                        keywords.contains(vehicle.getLocation())
                        || keywords.contains(vehicle.getColor())
                        || keywords.contains(vehicle.getModel()))
                .sorted(Comparator.comparing(Vehicle::getIsVIP, Comparator.reverseOrder())
                        .thenComparing(Vehicle::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Vehicle> getVehiclesBySeller(String sellerName) {
        LinkedHashSet<Vehicle> vehicles = this.vehiclesBySellerName.get(sellerName);
        if (vehicles == null) {
            throw new IllegalArgumentException();
        }

        return vehicles;
    }

    @Override
    public Iterable<Vehicle> getVehiclesInPriceRange(double lowerBound, double upperBound) {
        return this.vehiclesById.values()
                .stream()
                .filter( (vehicle) -> vehicle.getPrice() >= lowerBound && vehicle.getPrice() <= upperBound)
                .sorted(Comparator.comparing(Vehicle::getHorsepower, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Vehicle>> getAllVehiclesGroupedByBrand() {
        return null;
    }

    @Override
    public Iterable<Vehicle> getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName() {
        return this.vehiclesBySellerName.values()
                .stream()
                .flatMap(LinkedHashSet::stream)
                .sorted(Comparator.comparing(Vehicle::getHorsepower, Comparator.reverseOrder())
                                .thenComparing(Vehicle::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public Vehicle buyCheapestFromSeller(String sellerName) {
       return null;
    }
}
