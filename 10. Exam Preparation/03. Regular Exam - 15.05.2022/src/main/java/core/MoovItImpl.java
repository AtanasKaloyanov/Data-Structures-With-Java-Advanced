package core;

import models.Route;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MoovItImpl implements MoovIt {
    private Map<String, Route> routesByIds = new LinkedHashMap<>();
    private Map<String, Route> routesByStartEndDistance = new LinkedHashMap<>();

    @Override
    public void addRoute(Route route) {
        if (this.routesByIds.containsKey(route.getId())) {
            throw new IllegalArgumentException();
        }

        String key = route.getDistance() + "-" + route.getLocationPoints().get(0) + "-" + route.getLocationPoints().get(route.getLocationPoints().size() - 1);
        if (this.routesByStartEndDistance.containsKey(key)) {
            throw new IllegalArgumentException();
        }

        this.routesByIds.put(route.getId(), route);
        this.routesByStartEndDistance.put(key, route);
    }

    @Override
    public void removeRoute(String routeId) {
        Route route = this.routesByIds.remove(routeId);
        if (route == null) {
            throw new IllegalArgumentException();
        }
        this.routesByStartEndDistance.remove(route.getDistance() + "-" + route.getLocationPoints().get(0) + "-" + route.getLocationPoints().get(route.getLocationPoints().size() - 1));
    }

    @Override
    public boolean contains(Route route) {
        return (this.routesByIds.containsKey(route.getId())) ||
                this.routesByStartEndDistance.containsKey(route.getDistance() + "-" + route.getLocationPoints().get(0) + "-" + route.getLocationPoints().get(route.getLocationPoints().size() - 1));
    }

    @Override
    public int size() {
        return this.routesByIds.size();
    }

    @Override
    public Route getRoute(String routeId) {
        Route route = this.routesByIds.get(routeId);
        if (route == null) {
            throw new IllegalArgumentException();
        }
        return route;
    }

    @Override
    public void chooseRoute(String routeId) {
        Route route = this.routesByIds.get(routeId);
        if (!this.routesByIds.containsKey(routeId)) {
            throw new IllegalArgumentException();
        }
        route.setPopularity(route.getPopularity() + 1);
    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        return this.routesByIds.values()
                .stream()
                .filter((route) -> route.getLocationPoints().contains(startPoint) && route.getLocationPoints().contains(endPoint))
                .sorted((first, second) -> {
                    boolean firsResult = first.getIsFavorite();
                    boolean secondResult = second.getIsFavorite();
                    int result = Boolean.compare(secondResult, firsResult);

                    if (result == 0) {
                        List<String> firstPoints = first.getLocationPoints();
                        List<String> secondPoints = second.getLocationPoints();
                        int firstDist = firstPoints.indexOf(endPoint) - firstPoints.indexOf(startPoint);
                        int secondDist = secondPoints.indexOf(endPoint) - secondPoints.indexOf(startPoint);
                        result = Integer.compare(firstDist, secondDist);
                    }
                    if (result == 0) {
                        result = Integer.compare(second.getPopularity(), first.getPopularity());
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        return this.routesByIds.values()
                .stream()
                .filter((route) -> !route.getLocationPoints().get(0).equals(destinationPoint)
                        && route.getIsFavorite())
                .sorted(Comparator.comparing(Route::getDistance)
                        .thenComparing(Route::getPopularity).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getTop5RoutesByPopularityThenByDistanceThenByCountOfLocationPoints() {
        return this.routesByIds.values()
                .stream()
                .sorted(Comparator.comparing(Route::getPopularity).reversed()
                        .thenComparing(Route::getDistance)
                        .thenComparing(Route::getLocationPointsNumber))
                .limit(5)
                .collect(Collectors.toList());

    }
}
