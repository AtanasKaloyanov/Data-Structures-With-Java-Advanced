import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        AVL<Integer> avl = new AVL<>();
        //        12  15  5  3  8  1
        avl.insert(12);
        avl.insert(15);
        avl.insert(5);
        avl.insert(3);
        avl.insert(8);
        avl.insert(1);

        Random random = new Random();
        System.out.println(random.nextInt(40));

    }
}
