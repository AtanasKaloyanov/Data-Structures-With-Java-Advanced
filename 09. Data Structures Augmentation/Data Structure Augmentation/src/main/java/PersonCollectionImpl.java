import com.sun.source.tree.Tree;

import java.util.*;
import java.util.stream.Collectors;

public class PersonCollectionImpl implements PersonCollection {
    private Map<String, Person> personByEmail = new HashMap<>();
    private Map<String, TreeMap<String, Person>> peopleByDomainSortedByEmail = new HashMap<>();
    private Map<String, TreeMap<String, Person>> peopleByNamAndTownOrderedByEmail = new HashMap<>();
    private Map<String, TreeSet<Person>> peropleByTownSortedByAgeAndEmail = new HashMap<>();
    private Set<Person> peopleSortedByAgeAndEmail = new TreeSet<>(Comparator.comparing(Person::getAge)
            .thenComparing(Person::getEmail));

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (this.personByEmail.containsKey(email)) {
            return false;
        }

        Person person = new Person(email, name, age, town);
        this.personByEmail.put(email, person);

        // by domain
        int dotIndex = email.indexOf("@");
        String domain = email.substring(dotIndex + 1);
        if (!this.peopleByDomainSortedByEmail.containsKey(domain)) {
            this.peopleByDomainSortedByEmail.put(domain, new TreeMap<>());
        }
        this.peopleByDomainSortedByEmail.get(domain).put(email, person);

        // by name + town
        String namePlTown = name + "-" + town;
        if (!this.peopleByNamAndTownOrderedByEmail.containsKey(namePlTown)) {
            this.peopleByNamAndTownOrderedByEmail.put(namePlTown, new TreeMap<>());
        }
        this.peopleByNamAndTownOrderedByEmail.get(namePlTown).put(email, person);

        //by town, sorted by age and email
        if (!this.peropleByTownSortedByAgeAndEmail.containsKey(town)) {
            this.peropleByTownSortedByAgeAndEmail.put(town, new TreeSet<>(Comparator.comparing(Person::getAge)
                    .thenComparing(Person::getEmail)));
        }
        this.peropleByTownSortedByAgeAndEmail.get(town).add(person);
        this.peopleSortedByAgeAndEmail.add(person);
        return true;

    }

    @Override
    public int getCount() {
        return this.personByEmail.size();
    }

    @Override
    public boolean delete(String email) {
        if (this.find(email) == null) {
            return false;
        }
        this.personByEmail.remove(email);
        this.peopleByDomainSortedByEmail.remove(email.substring(email.indexOf("@") + 1));
        return true;
    }

    @Override
    public Person find(String email) {
        return this.personByEmail.get(email);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        TreeMap<String, Person> map = this.peopleByDomainSortedByEmail.get(emailDomain);
        if (map == null) {
            map = new TreeMap<>();
        }
        return map.values();
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        TreeMap<String, Person> map = this.peopleByNamAndTownOrderedByEmail.get(name + "-" + town);
        if (map == null) {
            map = new TreeMap<>();
        }
        return map.values();
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        if (this.peopleSortedByAgeAndEmail == null) {
            return new TreeSet<>();
        }
       return this.peopleSortedByAgeAndEmail.stream()
               .filter( (person) -> person.getAge() >= startAge && person.getAge() <= endAge)
               .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        TreeSet<Person> set = this.peropleByTownSortedByAgeAndEmail.get(town);
        if (set == null) {
            return new TreeSet<>();
        }
        return set
                .stream()
                .filter((person) -> person.getAge() >= startAge && person.getAge() <= endAge)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}