import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class RoyaleArena implements IArena {
    private Map<Integer, Battlecard> cardsById;
    private Map<CardType, Set<Battlecard>> cardsByTypes;


    public RoyaleArena() {
        this.cardsById = new LinkedHashMap<>();
        this.cardsByTypes = new LinkedHashMap<>();
    }

    @Override
    public void add(Battlecard card) {
        this.cardsById.putIfAbsent(card.getId(), card);
        this.cardsByTypes.putIfAbsent(card.getType(), new TreeSet<>(Battlecard::compareTo));
        this.cardsByTypes.get(card.getType()).add(card);
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
        // TODO: exclusive first range ?
        return battleCards.stream()
                .filter((element) -> element.getDamage() > lo && element.getDamage() < hi)
                .sorted(Battlecard::compareTo)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        Set<Battlecard> battleCards = getBattleCards(type);
        List<Battlecard> result = battleCards.stream()
                .filter((card) -> card.getDamage() <= damage)
                .sorted(Battlecard::compareTo)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return result;
    }

    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        List<Battlecard> battleCards = getBattlecardsByByPredicate(
                c -> c.getName().equals(name));

        if (battleCards.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        battleCards.sort(Comparator.comparingDouble(Battlecard::getSwag).reversed()
                .thenComparing(Battlecard::getId));
        return battleCards;
    }

    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        List<Battlecard> battleCards = getBattlecardsByByPredicate(
                c -> c.getName().equals(name) && c.getSwag() >= lo && c.getSwag() < hi);

        if (battleCards.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        battleCards.sort(Comparator.comparingDouble(Battlecard::getSwag).reversed()
                .thenComparing(Battlecard::getId));
        return battleCards;
    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {
        Map<String, Battlecard> battlecards = new HashMap<>();
        for (Battlecard battleCard : this.cardsById.values()) {
            if (!battlecards.containsKey(battleCard.getName())) {
                battlecards.put(battleCard.getName(), battleCard);
            } else {
                double oldSwag = battlecards.get(battleCard.getName()).getSwag();
                double newSwag = battleCard.getSwag();
                if (newSwag > oldSwag) {
                    battlecards.put(battleCard.getName(), battleCard);
                }
            }
        }

        if (battlecards.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return battlecards.values();
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if (n > this.count()) {
            throw new UnsupportedOperationException();
        }

        return this.cardsById.values()
                .stream()
                .sorted(Comparator.comparing(Battlecard::getSwag)
                        .thenComparing(Battlecard::getId))
                .limit(n)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {
        return getBattlecardsByByPredicate(c ->
                c.getSwag() >= lo && c.getSwag() <= hi)
                .stream()
                .sorted(Comparator.comparingDouble(Battlecard::getSwag))
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Battlecard> iterator() {
        return this.cardsById.values().iterator();
    }

    private Set<Battlecard> getBattleCards(CardType type) {
        Set<Battlecard> battleCards = this.cardsByTypes.get(type);
        if (battleCards == null || battleCards.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return battleCards;
    }

    private List<Battlecard> getBattlecardsByByPredicate(Predicate<Battlecard> predicate) {
        List<Battlecard> battleCards = new ArrayList<>();
        for (Battlecard battleCard : this.cardsById.values()) {
            if (predicate.test(battleCard)) {
                battleCards.add(battleCard);
            }
        }
        return battleCards;
    }

}
