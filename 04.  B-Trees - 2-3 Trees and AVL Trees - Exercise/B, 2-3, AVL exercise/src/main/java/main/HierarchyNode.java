package main;

import java.util.ArrayList;
import java.util.List;

public class HierarchyNode<T> {
    private T value;
    private HierarchyNode<T> parent;
    private List<HierarchyNode<T>> children;

    public HierarchyNode(T value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public HierarchyNode<T> getParent() {
        return parent;
    }

    public void setParent(HierarchyNode<T> parent) {
        this.parent = parent;
    }

    public List<HierarchyNode<T>> getChildren() {
        return children;
    }

}
