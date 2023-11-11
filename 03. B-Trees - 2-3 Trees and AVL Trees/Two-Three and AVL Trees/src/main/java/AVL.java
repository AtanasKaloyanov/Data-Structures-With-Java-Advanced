import java.util.function.Consumer;

public class AVL<T extends Comparable<T>> {
    private Node<T> root;

    public Node<T> getRoot() {
        return this.root;
    }

    public void insert(T value) {
        this.root = this.insert(this.root, value);
    }

    public Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            return new Node<>(value);
        }

        int compareNumber = value.compareTo(node.value);
        if (compareNumber < 0) {
            node.left = insert(node.left, value);
        } else if (compareNumber > 0) {
            node.right = insert(node.right, value);
        }

        updateHeight(node);
        node = balance(node);
        return node;
    }

    private void updateHeight(Node<T> node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private int height(Node<T> node) {
        if (node == null) {
            return 0;
        }

        return node.height;
    }

    public boolean contains(T key) {
        Node<T> node = this.contains(this.root, key);
        return node != null;
    }

    public Node<T> contains(Node<T> root, T key) {
        if (root == null) {
            return null;
        }

        int compareNumber = key.compareTo(root.value);
        if (compareNumber < 0) {
            return contains(root.left, key);
        } else if (compareNumber > 0) {
            return contains(root.right, key);
        }

        return root;
    }

    public void eachInOrder(Consumer<T> consumer) {
        this.eachInOrder(this.root, consumer);
    }

    private void eachInOrder(Node<T> node, Consumer<T> action) {
        if (node == null) {
            return;
        }

        this.eachInOrder(node.left, action);
        action.accept(node.value);
        this.eachInOrder(node.right, action);
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> right = node.right;
        node.right = node.right.left;
        right.left = node;

        this.updateHeight(node);
        this.updateHeight(right);
        return right;
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> left = node.left;
        node.left = node.left.right;
        left.right = node;

        this.updateHeight(node);
        this.updateHeight(left);
        return left;
    }

    private Node<T> balance(Node<T> node) {
        int balance = this.height(node.left) - this.height(node.right);

        if (balance > 1) {
            int childBalance = this.height(node.left.left) - this.height(node.left.right);
            if (childBalance < 0) {
                node.left = this.rotateLeft(node.left);
            }
            return this.rotateRight(node);

        } else if (balance < -1) {
            int childBalance = this.height(node.right.left) - this.height(node.right.right);
            if (childBalance > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

}

