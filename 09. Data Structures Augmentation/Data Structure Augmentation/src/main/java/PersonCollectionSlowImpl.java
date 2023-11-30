import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PersonCollectionSlowImpl implements PersonCollection {
    private Map<String, Person> personByEmail = new HashMap<>();

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (this.personByEmail.containsKey(email)) {
            return false;
        }
        Person person = new Person(email, name, age, town);
        this.personByEmail.put(email, person);
        return true;
    }

    @Override
    public int getCount() {
        return this.personByEmail.size();
    }

    @Override
    public boolean delete(String email) {
        Person person = this.personByEmail.remove(email);
        return person != null;
    }

    @Override
    public Person find(String email) {
        return this.personByEmail.get(email);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        return this.personByEmail.values()
                .stream()
                .filter( (person) -> person.getEmail().endsWith("@" + emailDomain))
                .sorted(Comparator.comparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
         return this.personByEmail.values()
                 .stream()
                 .filter( (person) -> person.getName().equals(name) && person.getTown().equals(town))
                 .sorted(Comparator.comparing(Person::getEmail))
                 .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        return this.personByEmail
                .values()
                .stream()
                .filter( (person) -> person.getAge() >= startAge && person.getAge() <= endAge)
                .sorted(Comparator.comparing(Person::getAge).thenComparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        return this.personByEmail.values()
                .stream()
                .filter( (person) -> person.getAge() >= startAge && person.getAge() <= endAge && person.getTown().equals(town))
                .sorted(Comparator.comparing(Person::getAge).thenComparing(Person::getEmail))
                .collect(Collectors.toList());
    }
}
