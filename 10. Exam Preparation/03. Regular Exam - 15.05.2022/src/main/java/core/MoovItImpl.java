package core;

import models.Route;

import java.util.*;

import java.util.stream.Collectors;

public class MoovItImpl implements MoovIt {
    private Map<String, Route> routesByIds = new LinkedHashMap<>();
    private Set<Route> routes = new LinkedHashSet<>();

    @Override
    public void addRoute(Route route) {
        if (this.routes.contains(route)) {
            throw new IllegalArgumentException();
        }

        this.routesByIds.put(route.getId(), route);
        this.routes.add(route);
    }

    @Override
    public void removeRoute(String routeId) {
        Route route = this.routesByIds.remove(routeId);
        if (route == null) {
            throw new IllegalArgumentException();
        }
        this.routes.remove(route);
    }

    @Override
    public boolean contains(Route route) {
        return this.routes.contains(route);
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
        if (route == null) {
            throw new IllegalArgumentException();
        }
        route.setPopularity(route.getPopularity() + 1);
    }

    @Override
    public Iterable<Route> searchRoutes(String startPoint, String endPoint) {
        return this.routesByIds.values()
                .stream()
                .filter( (Route route) -> {
                        int startIndex = route.getLocationPoints().indexOf(startPoint);
                        int endIndex = route.getLocationPoints().indexOf(endPoint);
                        return startIndex >= 0 && endIndex >= 0 && endIndex > startIndex;
                })
                .sorted(Comparator.comparing(Route::getIsFavorite, Comparator.reverseOrder())
                        .thenComparing((Route r) -> r.getLocationPoints().indexOf(endPoint) - r.getLocationPoints().indexOf(startPoint))
                        .thenComparing(Route::getPopularity, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        return this.routesByIds.values()
                .stream()
                .filter( (route) -> route.getIsFavorite()
                        && !route.getLocationPoints().get(0).equals(destinationPoint)
                        && route.getLocationPoints().contains(destinationPoint))
                .sorted(Comparator.comparing(Route::getDistance)
                        .thenComparing(Route::getPopularity, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getTop5RoutesByPopularityThenByDistanceThenByCountOfLocationPoints() {
        return this.routesByIds.values()
                .stream()
                .sorted(Comparator.comparing(Route::getPopularity, Comparator.reverseOrder())
                        .thenComparing(Route::getDistance)
                        .thenComparing((route) -> route.getLocationPoints().size()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
