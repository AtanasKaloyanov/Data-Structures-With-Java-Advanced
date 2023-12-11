package core;

import models.Doodle;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DoodleSearchImpl implements DoodleSearch {
    private Map<String, Doodle> doodlesByIds = new LinkedHashMap<>();
    private Map<String, Doodle> doodlesByTitle = new LinkedHashMap<>();

    @Override
    public void addDoodle(Doodle doodle) {
        if (doodlesByTitle.containsKey(doodle.getTitle())) {
            return;
        }
        this.doodlesByIds.put(doodle.getId(), doodle);
        this.doodlesByTitle.put(doodle.getTitle(), doodle);
    }

    @Override
    public void removeDoodle(String doodleId) {
        Doodle doodle = this.doodlesByIds.remove(doodleId);
        if (doodle == null) {
            throw new IllegalArgumentException();
        }
        this.doodlesByTitle.remove(doodle.getTitle());
    }

    @Override
    public int size() {
        return this.doodlesByIds.size();
    }

    @Override
    public boolean contains(Doodle doodle) {
        return this.doodlesByIds.containsKey(doodle.getId());
    }

    @Override
    public Doodle getDoodle(String id) {
        Doodle doodle = this.doodlesByIds.get(id);
        if (doodle == null) {
            throw new IllegalArgumentException();
        }
        return doodle;
    }

    @Override
    public double getTotalRevenueFromDoodleAds() {
        double result = 0;
        for (Doodle doodle : this.doodlesByIds.values()) {
            if (doodle.getIsAd()) {
                result += doodle.getRevenue() * doodle.getVisits();
            }
        }
        return result;
    }

    @Override
    public void visitDoodle(String title) {
        Doodle doodle = this.doodlesByTitle.get(title);
        if (doodle == null) {
            throw new IllegalArgumentException();
        }
        doodle.setVisits(doodle.getVisits() + 1);

    }

    @Override
    public Iterable<Doodle> searchDoodles(String searchQuery) {
        return this.doodlesByIds.values()
                .stream()
                .filter((title) -> title.getTitle().contains(searchQuery))
                .sorted(Comparator.comparing(Doodle::getIsAd, Comparator.reverseOrder())
                        .thenComparing(Doodle::getTitle)
                        .thenComparing(Doodle::getVisits, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Doodle> getDoodleAds() {
        return this.doodlesByIds.values()
                .stream()
                .filter(Doodle::getIsAd)
                .sorted(Comparator.comparing(Doodle::getRevenue, Comparator.reverseOrder())
                        .thenComparing(Doodle::getVisits, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Doodle> getTop3DoodlesByRevenueThenByVisits() {
        return this.doodlesByIds.values()
                .stream()
                .sorted(Comparator.comparing(Doodle::getRevenue, Comparator.reverseOrder())
                        .thenComparing(Doodle::getVisits, Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toList());
    }
}
