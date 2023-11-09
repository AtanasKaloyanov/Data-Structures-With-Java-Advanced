import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
     AVL<Integer> avl = new AVL<>();
     avl.insert(10);
     avl.insert(5);
     avl.insert(15);
     avl.insert(8);

        Consumer<Integer> cons = (intt) -> System.out.println(intt);
        avl.eachInOrder(cons);
    }
}
