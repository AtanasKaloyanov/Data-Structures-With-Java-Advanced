import java.util.*;
import java.util.stream.Collectors;


public class RoyaleArena implements IArena {
    private LinkedHashMap<Integer, Battlecard> cardsById;

    private Map<CardType, Set<Battlecard>> cardsByTypes;
    private LinkedHashMap<String, Battlecard> cardsByName;

    public RoyaleArena() {
        this.cardsById = new LinkedHashMap<>();
        this.cardsByTypes = new HashMap<>();
        // cardsById - HashMap
        this.cardsByName = new LinkedHashMap<>();
    }

    @Override
    public void add(Battlecard card) {
        this.cardsById.putIfAbsent(card.getId(), card);
        this.cardsByTypes.putIfAbsent(card.getType(), new TreeSet<>(Battlecard::compareTo));
        this.cardsByTypes.get(card.getType()).add(card);


        if (!this.cardsByName.containsKey(card.getName())) {
            this.cardsByName.put(card.getName(), card);
        }
        Battlecard sameNameCard = this.cardsByName.get(card.getName());
        if (card.getSwag() > sameNameCard.getSwag()) {
            this.cardsByName.put(card.getName(), card);
        }
    }

    @Override
    public boolean contains(Battlecard card) {
        return this.cardsById.containsKey(card.getId());
    }

    @Override
    public int count() {
        return this.cardsById.size();
    }

    @Override
    public void changeCardType(int id, CardType type) {
        Battlecard battlecard = this.cardsById.get(id);
        if (battlecard == null) {
            throw new IllegalArgumentException();
        }
        battlecard.setType(type);
    }

    @Override
    public Battlecard getById(int id) {
        Battlecard battlecard = this.cardsById.get(id);
        if (battlecard == null) {
            throw new UnsupportedOperationException();
        }
        return battlecard;
    }

    @Override
    public void removeById(int id) {
        Battlecard battlecard = this.cardsById.remove(id);
        if (battlecard == null) {
            throw new UnsupportedOperationException();
        }
        this.cardsByTypes.get(battlecard.getType()).remove(battlecard);
    }

    @Override
    public Iterable<Battlecard> getByCardType(CardType type) {
        return getBattleCards(type);
    }
    @Override
    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        Set<Battlecard> battleCards = getBattleCards(type);
        return battleCards.stream()
                .filter((element) -> element.getDamage() >= lo && element.getDamage() < hi)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        Set<Battlecard> battleCards = getBattleCards(type);
        List<Battlecard> result = battleCards.stream()
                .filter((card) -> card.getDamage() <= damage)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return result;
    }
    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        List<Battlecard> battleCards = this.cardsById.values()
                .stream()
                .filter((card) -> card.getName().equals(name))
                .collect(Collectors.toList());

        if (battleCards.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        battleCards.sort(Comparator.comparing(Battlecard::getSwag).reversed()
                .thenComparing(Battlecard::getId));
        return battleCards;
    }

    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        List<Battlecard> result = this.cardsById.values()
                .stream()
                .filter((card) -> card.getName().equals(name) && card.getSwag() >= lo && card.getSwag() < hi)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return result.stream()
                .sorted(Comparator.comparing(Battlecard::getSwag).reversed()
                .thenComparing(Battlecard::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {
        return this.cardsByName.values();
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if (n > this.cardsById.size()) {
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

    private Set<Battlecard> getBattleCards(CardType type) {
        Set<Battlecard> battleCards = this.cardsByTypes.get(type);
        if (battleCards == null) {
            throw new UnsupportedOperationException();
        }
        return battleCards;
    }
}
