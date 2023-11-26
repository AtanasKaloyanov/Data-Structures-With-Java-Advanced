import java.util.*;
import java.util.stream.Collectors;

public class WordCruncher {
    private static Map<String, Integer> wordByCounts = new HashMap<>();
    private static Map<Integer, TreeSet<String>> tree = new HashMap<>();
    private static String target;
    private static List<String> buffer = new ArrayList<>();
    private static List<String> out = new ArrayList<>();

    public static void main(String[] args) {
        // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in);

        List<String> inputStrings = Arrays.stream(scanner.nextLine().split(", "))
                .collect(Collectors.toList());
        target = scanner.nextLine();

        inputStrings = inputStrings.stream()
                .filter((str) -> target.contains(str))
                .collect(Collectors.toList());

        for (String string : inputStrings) {
            wordByCounts.putIfAbsent(string, 0);
            wordByCounts.put(string, wordByCounts.get(string) + 1);

            int index = target.indexOf(string);

            while (index != -1) {
                if (!tree.containsKey(index)) {
                    tree.put(index, new TreeSet<>());
                }
                tree.get(index).add(string);
                index = target.indexOf(string, index + 1);
            }

        }

        dfsTraversal(0);
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < out.size(); i++) {
            builder.append(out.get(i)).append(System.lineSeparator());
        }

        System.out.print(builder.toString());
    }

    private static void dfsTraversal(int index) {
        if (index >= target.length()) {
            printResult();
        } else {
            if (!tree.containsKey(index)) {
                return;
            }
            for (String str : tree.get(index)) {
                if (wordByCounts.get(str) > 0) {
                    buffer.add(str);
                    wordByCounts.put(str, wordByCounts.get(str) - 1);
                    dfsTraversal(index + str.length());
                    wordByCounts.put(str, wordByCounts.get(str) + 1);
                    buffer.remove(buffer.size() - 1);
                }
            }
        }
    }

    private static void printResult() {
        String result = String.join("", buffer);
        if (result.equals(target)) {
            out.add(String.join(" ", buffer));
        }
    }
}
