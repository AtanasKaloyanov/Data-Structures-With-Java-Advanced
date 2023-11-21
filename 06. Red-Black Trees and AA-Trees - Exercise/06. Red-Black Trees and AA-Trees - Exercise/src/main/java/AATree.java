import jdk.jshell.EvalException;

import java.util.function.Consumer;

class AATree<T extends Comparable<T>> {
    private Node<T> root;

    public static class Node<T extends Comparable<T>> {
        private T value;
        private Node<T> left;
        private Node<T> right;
        private int size;
        private int level;

        public Node(T value) {
            this.value = value;
            this.level = 1;
        }
    }

    public AATree() {

    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void clear() {
        this.root = null;
    }

    public void insert(T element) {
        this.root = this.insert(this.root, element);

    }

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            return new Node<>(value);
        }
        int compareNumber = value.compareTo(node.value);

        if (compareNumber < 0) {
            node.left = insert(node.left, value);
        } else if (compareNumber > 0) {
            node.right = insert(node.right, value);
        }

        node = skew(node);
        node = split(node);
        node.size = size(node.left) + size(node.right) + 1;

        return node;
    }

    private int size(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    private Node<T> skew(Node<T> node) {
        if (node.left == null) {
            return node;
        }

        if (node.level == node.left.level) {
            Node<T> temp = node.left;
            node.left = temp.right;
            temp.right = node;
            node = temp;
        }

        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    private Node<T> split(Node<T> node) {
        if (node.right == null || node.right.right == null) {
            return node;
        }

        if (node.level == node.right.right.level) {
            Node<T> temp = node.right;
            node.right = temp.left;
            temp.left = node;
            temp.level++;
            node = temp;
        }

        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    public int countNodes() {
        if (isEmpty()) {
            return 0;
        }
        return this.root.size;
    }

    public boolean search(T element) {
        Node<T> current = this.root;

        while (current != null) {
            int compareNumber = element.compareTo(current.value);

            if (compareNumber < 0) {
                current = current.left;
            } else if (compareNumber > 0) {
                current = current.right;
            } else {
                return true;
            }
        }

        return false;
    }

    public void inOrder(Consumer<T> consumer) {
        inOrderRecc(this.root, consumer);
    }

    private void inOrderRecc(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        inOrderRecc(node.left, consumer);
        consumer.accept(node.value);
        inOrderRecc(node.right, consumer);
    }

    public void preOrder(Consumer<T> consumer) {
        preOrderRecc(this.root, consumer);
    }

    private void preOrderRecc(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        consumer.accept(node.value);
        preOrderRecc(node.left, consumer);
        preOrderRecc(node.right, consumer);
    }

    public void postOrder(Consumer<T> consumer) {
        postOrderRecc(this.root, consumer);
    }

    private void postOrderRecc(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        postOrderRecc(node.left, consumer);
        postOrderRecc(node.right, consumer);
        consumer.accept(node.value);
    }
}

//         10