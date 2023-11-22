public class Main {

    public static void main(String[] args) {
        HashTable<String, Integer> map = new HashTable<>();
        map.add("one", 1);
        map.add("two", 2);
        map.add("three", 3);
        map.add("four", 4);

        for (KeyValue<String, Integer> pair : map) {
            System.out.println(pair);
        }
    }
}
