import java.util.*;
import java.util.stream.Collectors;


public class RoyaleArena implements IArena {
    private int count;
    private LinkedHashMap<Integer, Battlecard> cardsById;
    private LinkedHashMap<String, Battlecard> cardsByName;

    public RoyaleArena() {
        this.cardsById = new LinkedHashMap<>();
        this.cardsByName = new LinkedHashMap<>();
    }

    @Override
    public void add(Battlecard card) {
        if (contains(card)) {
            throw new UnsupportedOperationException();
        }
        this.cardsById.put(card.getId(), card);

        if (!this.cardsByName.containsKey(card.getName())) {
            this.cardsByName.put(card.getName(), card);
        }
        Battlecard sameNameCard = this.cardsByName.get(card.getName());
        if (card.getSwag() > sameNameCard.getSwag()) {
            this.cardsByName.put(card.getName(), card);
        }
        this.count++;
    }

    @Override
    public boolean contains(Battlecard card) {
        return this.cardsById.containsKey(card.getId());
    }

    @Override
    public int count() {
        return this.count;
    }

    @Override
    public void changeCardType(int id, CardType type) {
        if (!this.cardsById.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        this.cardsById.get(id).setType(type);
    }

    @Override
    public Battlecard getById(int id) {
        if (!this.cardsById.containsKey(id)) {
            throw new UnsupportedOperationException();
        }
        return this.cardsById.get(id);
    }

    @Override
    public void removeById(int id) {
        if (!this.cardsById.containsKey(id)) {
            throw new UnsupportedOperationException();
        }
        this.cardsById.remove(id);
        this.count--;
    }

    @Override
    public Iterable<Battlecard> getByCardType(CardType type) {
        List<Battlecard> result = this.cardsById.values()
                .stream()
                .filter((card) -> card.getType().equals(type))
                .sorted(Comparator.comparing(Battlecard::getDamage).reversed()
                        .thenComparing(Battlecard::getId))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        List<Battlecard> result = this.cardsById.values()
                .stream()
                .filter((card) -> card.getType().equals(type) && card.getDamage() > lo && card.getDamage() < hi)
                .sorted(Comparator.comparing(Battlecard::getDamage).reversed())
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        List<Battlecard> result = this.cardsById.values()
                .stream()
                .filter((card) -> card.getType().equals(type) && card.getDamage() <= damage)
                .sorted(Comparator.comparing(Battlecard::getDamage).reversed()
                        .thenComparing(Battlecard::getId))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        List<Battlecard> result = this.cardsById.values()
                .stream()
                .filter((card) -> card.getName().equals(name))
                .sorted(Comparator.comparing(Battlecard::getSwag).reversed()
                        .thenComparing(Battlecard::getId))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        List<Battlecard> result = this.cardsById.values()
                .stream()
                .filter((card) -> card.getName().equals(name) && card.getSwag() >= lo && card.getSwag() < hi)
                .sorted(Comparator.comparing(Battlecard::getSwag).reversed()
                        .thenComparing(Battlecard::getId))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {
        return this.cardsByName.values();
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if (n > this.count) {
            throw new UnsupportedOperationException();
        }

        return this.cardsById.values()
                .stream()
                .sorted(Comparator.comparing(Battlecard::getSwag))
                .limit(n)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {
        return this.cardsById.values()
                .stream()
                .filter((card) -> card.getSwag() >= lo && card.getSwag() <= hi)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Battlecard> iterator() {
        return this.cardsById.values().iterator();
    }
}
