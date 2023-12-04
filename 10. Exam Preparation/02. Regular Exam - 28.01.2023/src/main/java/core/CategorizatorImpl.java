package core;

import models.Category;

import java.util.*;

public class CategorizatorImpl implements Categorizator {
    private Map<String, Category> categoryByIds = new LinkedHashMap<>();

    @Override
    public void addCategory(Category category) {
        if (this.categoryByIds.containsKey(category.getId())) {
            throw new IllegalArgumentException();
        }
        this.categoryByIds.put(category.getId(), category);
    }

    @Override
    public void assignParent(String childCategoryId, String parentCategoryId) {
        if (!this.categoryByIds.containsKey(childCategoryId)
                || !this.categoryByIds.containsKey(parentCategoryId)) {
            throw new IllegalArgumentException();
        }
        Category child = this.categoryByIds.get(childCategoryId);
        Category parent = this.categoryByIds.get(parentCategoryId);
        if (parent.getChildren().contains(child)) {
            throw new IllegalArgumentException();
        }
        parent.getChildren().add(child);
    }

    @Override
    public void removeCategory(String categoryId) {
        if (!this.categoryByIds.containsKey(categoryId)) {
            throw new IllegalArgumentException();
        }
        Category categoryForRemoving = this.categoryByIds.remove(categoryId);
        for (Category child : categoryForRemoving.getChildren()) {
            this.categoryByIds.remove(child.getId());
        }
    }

    @Override
    public boolean contains(Category category) {
        return this.categoryByIds.containsKey(category.getId());
    }

    @Override
    public int size() {
        return this.categoryByIds.size();
    }

    @Override
    public Iterable<Category> getChildren(String categoryId) {
        if (!this.categoryByIds.containsKey(categoryId)) {
            throw new IllegalArgumentException();
        }
        Category category = this.categoryByIds.get(categoryId);

        ArrayDeque<Category> children = new ArrayDeque<>(category.getChildren());
        List<Category> result = new ArrayList<>();

        while (!children.isEmpty()) {
            Category current = children.poll();
            result.add(current);
            if (current.getChildren() != null) {
                for (Category child : current.getChildren()) {
                    children.offer(child);
                }
            }
        }

        return result;
    }


    @Override
    public Iterable<Category> getHierarchy(String categoryId) {
        if (!this.categoryByIds.containsKey(categoryId)) {
            throw new IllegalArgumentException();
        }

        return new ArrayList<>();
    }

    @Override
    public Iterable<Category> getTop3CategoriesOrderedByDepthOfChildrenThenByName() {
        return new ArrayList<>();
    }
}
