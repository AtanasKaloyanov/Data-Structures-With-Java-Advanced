import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PersonCollectionSlowImpl implements PersonCollection {
    List<Person> people = new ArrayList<>();

    @Override
    public boolean add(String email, String name, int age, String town) {
        for (Person person : this.people) {
            if (person.getEmail().equals(email)) {
                return false;
            }
        }
        Person person = new Person(email, name, age, town);
        this.people.add(person);
        return true;
    }

    @Override
    public int getCount() {
        return this.people.size();
    }

    @Override
    public boolean delete(String email) {
        return this.people.removeIf((person) -> person.getEmail().equals(email));
    }

    @Override
    public Person find(String email) {
        for (Person person : this.people) {
            if (person.getEmail().equals(email)) {
                return person;
            }
        }
        return null;
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        return this.people.stream()
                .filter((person) -> person.getEmail().endsWith("@" + emailDomain))
                .sorted(Comparator.comparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        return this.people.stream()
                .filter((person) -> person.getName().equals(name)
                        && person.getTown().equals(town))
                .sorted(Comparator.comparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        return this.people.stream()
                .filter((person) -> person.getAge() >= startAge
                        && person.getAge() <= endAge)
                .sorted(Comparator.comparing(Person::getAge)
                        .thenComparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        return this.people.stream()
                .filter((person) -> person.getAge() >= startAge
                        && person.getAge() <= endAge
                        && person.getTown().equals(town))
                .sorted(Comparator.comparing(Person::getAge)
                        .thenComparing(Person::getEmail))
                .collect(Collectors.toList());
    }
}
