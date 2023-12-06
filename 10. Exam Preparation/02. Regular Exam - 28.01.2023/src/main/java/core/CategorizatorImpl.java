package core;

import models.Category;

import java.util.*;
import java.util.stream.Collectors;

public class CategorizatorImpl implements Categorizator {
    LinkedHashMap<String, Category> categoriesById = new LinkedHashMap<>();
    Map<String, Category> parentByChildId = new HashMap<>();
    Map<String, LinkedHashSet<Category>> childrenByCatId = new HashMap<>();

    @Override
    public void addCategory(Category category) {
        if (this.categoriesById.containsKey(category.getId())) {
            throw new IllegalArgumentException();
        }

        categoriesById.put(category.getId(), category);
        childrenByCatId.put(category.getId(), new LinkedHashSet<>());
    }

    @Override
    public void assignParent(String childCategoryId, String parentCategoryId) {
        Category child = this.categoriesById.get(childCategoryId);
        Category parent = this.categoriesById.get(parentCategoryId);

        if (child == null || parent == null) {
            throw new IllegalArgumentException();
        }

        Category previousParent = parentByChildId.put(child.getId(), parent);
        if (previousParent != null) {
            throw new IllegalArgumentException();
        }

        LinkedHashSet<Category> parentCategoryChildren = childrenByCatId.get(parent.getId());
        parentCategoryChildren.add(child);
    }

    @Override
    public void removeCategory(String categoryId) {
        Category categoryToDelete = categoriesById.remove(categoryId);
        if (categoryToDelete == null) {
            throw new IllegalArgumentException();
        }

        LinkedHashSet<Category> childrenToDelete = new LinkedHashSet<>(childrenByCatId.get(categoryToDelete.getId()));
        for (Category category : childrenToDelete) {
            removeCategory(category.getId());
        }

        Category parent = parentByChildId.remove(categoryToDelete.getId());
        if (parent != null) {
            LinkedHashSet<Category> parentCategoryChildren = childrenByCatId.get(parent.getId());
            parentCategoryChildren.remove(categoryToDelete);
        }
    }

    @Override
    public boolean contains(Category category) {
        return this.categoriesById.containsKey(category.getId());
    }

    @Override
    public int size() {
        return categoriesById.size();
    }

    @Override
    public Iterable<Category> getChildren(String categoryId) {
        if (!this.categoriesById.containsKey(categoryId)) {
            throw new IllegalArgumentException();
        }

        List<Category> allChildren = new ArrayList<>();
        fillChildren(categoryId, allChildren);

        return allChildren;
    }

    private void fillChildren(String categoryId, List<Category> allChildren) {
        LinkedHashSet<Category> directChildren = childrenByCatId.get(categoryId);
        for (Category directChild : directChildren) {
            allChildren.add(directChild);
            fillChildren(directChild.getId(), allChildren);
        }
    }
    @Override
    public Iterable<Category> getHierarchy(String categoryId) {
        Category category = this.categoriesById.get(categoryId);
        if (category == null) {
            throw new IllegalArgumentException();
        }

        List<Category> hierarchy = new ArrayList<>();
        while (category != null) {
            hierarchy.add(category);
            category = parentByChildId.get(category.getId());
        }

        Collections.reverse(hierarchy);
        return hierarchy;
    }

    Map<String, Long> depthByCategoryId = new HashMap<>();
    @Override
    public Iterable<Category> getTop3CategoriesOrderedByDepthOfChildrenThenByName() {
        depthByCategoryId = new HashMap<>();

        for (Category category : categoriesById.values()) {
            if (parentByChildId.get(category.getId()) == null) {
                calculateDepth(category);
            }
        }

        return categoriesById.values().stream()
                .sorted(
                        Comparator.comparing((Category c) -> depthByCategoryId.get(c.getId()), Comparator.reverseOrder())
                                .thenComparing((Category c) -> c.getName())
                )
                .limit(3)
                .collect(Collectors.toList());
    }

    private long calculateDepth(Category category) {
        long maxChildDepth = 0;

        for (Category childCategory : childrenByCatId.get(category.getId())) {
            long childDepth = calculateDepth(childCategory);
            if (maxChildDepth < childDepth) {
                maxChildDepth = childDepth;
            }
        }

        long depth = 1 + maxChildDepth;
        depthByCategoryId.put(category.getId(), depth);

        return depth;
    }
}