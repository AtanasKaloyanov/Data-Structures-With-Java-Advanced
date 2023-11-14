package main;

import javax.print.attribute.standard.PresentationDirection;
import java.util.*;
import java.util.stream.Collectors;

public class Hierarchy<T> implements IHierarchy<T> {
    private Map<T, HierarchyNode<T>> hierarchy;
    private HierarchyNode<T> root;

    public Hierarchy(T root) {
        this.hierarchy = new HashMap<>();
        this.root = new HierarchyNode<>(root);
        this.hierarchy.put(root, this.root);
    }

    @Override
    public int getCount() {
        return this.hierarchy.size();
    }

    @Override
    public void add(T element, T child) {
        HierarchyNode<T> parent = ensureExistence(element);

        if (this.hierarchy.containsKey(child)) {
            throw new IllegalArgumentException();
        }

        HierarchyNode<T> childNode = new HierarchyNode<>(child);
        childNode.setParent(parent);
        parent.getChildren().add(childNode);

        this.hierarchy.put(child, childNode);
    }

    @Override
    public void remove(T element) {
        HierarchyNode<T> current = ensureExistence(element);

        if (current.getParent() == null) {
            throw new IllegalStateException();
        }

        HierarchyNode<T> parent = current.getParent();
        parent.getChildren().remove(current);
        List<HierarchyNode<T>> children = current.getChildren();

        for (HierarchyNode<T> child : children) {
            child.setParent(parent);
            parent.getChildren().add(child);
        }

        this.hierarchy.remove(element);
    }

    @Override
    public Iterable<T> getChildren(T element) {
        HierarchyNode<T> current = ensureExistence(element);

        return current.getChildren()
                .stream()
                .map(HierarchyNode::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public T getParent(T element) {
        HierarchyNode<T> node = ensureExistence(element);
        return node.getParent() == null ? null : node.getParent().getValue();
    }

    @Override
    public boolean contains(T element) {
        return this.hierarchy.containsKey(element);
    }

    @Override
    public Iterable<T> getCommonElements(IHierarchy<T> other) {
        List<T> result = new ArrayList<>();
        this.hierarchy.keySet().forEach( (key) -> {
            if (other.contains(key)) {
             result.add(key);
            }
        });
        return result;
    }

    @Override
    public Iterator<T> iterator() {
        Deque<HierarchyNode<T>> allNodesBFS = new ArrayDeque<>(Collections.singleton(root));
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return allNodesBFS.size() > 0;
            }

            @Override
            public T next() {
                HierarchyNode<T> nextElement = allNodesBFS.poll();
                allNodesBFS.addAll(nextElement.getChildren());
                return nextElement.getValue();
            }
        };
    }

    private HierarchyNode<T> ensureExistence(T element) {
        if (!this.hierarchy.containsKey(element)) {
            throw new IllegalArgumentException();
        }

        return this.hierarchy.get(element);
    }
}
