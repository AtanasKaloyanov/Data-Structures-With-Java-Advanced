import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4));
        List<Integer> list2 = list.stream()
                .filter( (number) -> number > 4)
                .collect(Collectors.toList());


    }
}
