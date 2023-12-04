package core;

import models.Expression;
import models.ExpressionType;

import java.util.*;

public class ExpressionistImpl implements Expressionist {
    private final Map<String, Expression> expressionById;

    public ExpressionistImpl() {
        this.expressionById = new TreeMap<>();
    }

    @Override
    public void addExpression(Expression expression) {
        if (!this.expressionById.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.expressionById.put(expression.getId(), expression);
    }

    @Override
    public void addExpression(Expression expression, String parentId) {
        Expression currentParentExpr = this.expressionById.get(parentId);

        if (currentParentExpr == null) {
            throw new IllegalArgumentException();
        }

        if (currentParentExpr.getLeftChild() == null) {
            currentParentExpr.setLeftChild(expression);
            this.expressionById.put(expression.getId(), expression);
        } else if (currentParentExpr.getRightChild() == null){
            currentParentExpr.setRightChild(expression);
            this.expressionById.put(expression.getId(), expression);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean contains(Expression expression) {
        return this.expressionById.containsValue(expression);
    }

    @Override
    public int size() {
        return this.expressionById.size();
    }

    @Override
    public Expression getExpression(String expressionId) {
        Expression result = this.expressionById.get(expressionId);

        if (result == null) {
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Override
    public void removeExpression(String expressionId) {
        Expression result = this.expressionById.get(expressionId);

        if (result == null) {
            throw new IllegalArgumentException();
        }

        for (Expression value : this.expressionById.values()) {
            Expression root = value;

            boolean flag = false;

            if (value.getLeftChild() == result) {
                removeExpr(result);

                root.setLeftChild(root.getRightChild());
                root.setRightChild(null);

                flag = true;
            } else if (value.getRightChild() == result) {
                removeExpr(result);

                root.setRightChild(null);

                flag = true;
            }

            if (flag) {
                return;
            }

        }
    }

    public void removeExpr (Expression expression) {

        if (expression.getLeftChild() != null) {
            this.removeExpr(expression.getLeftChild());
            expression.setLeftChild(null);
        }

        if (expression.getRightChild() != null) {
            this.removeExpr(expression.getRightChild());
            expression.setRightChild(null);
        }

        this.expressionById.remove(expression.getId());
    }

    @Override
    public String evaluate() {
        Expression root = findRoot();

        return goDeap(root);
    }

    private Expression findRoot() {
        Expression root = new Expression();

        for (Expression value : this.expressionById.values()) {
            root = value;
            break;
        }

        return root;
    }

    private String goDeap(Expression root) {
        StringBuilder sb = new StringBuilder();

        Expression leftChild = root.getLeftChild();//10
        Expression rightChild = root.getRightChild();//30

        if (leftChild.getLeftChild() != null) {
            goDeap(leftChild);
        }

        String rootType = root.getType().toString();
        String rootValue = root.getValue();
        String leftChildValue = leftChild.getValue();
        String rightChildValue = rightChild.getValue();

        if (rootType.equals("Value")) {
            sb.append(rootValue);
        } else {
            sb.append(String.format("(%s %s %s)", leftChildValue, rootValue, rightChildValue));
        }


        return sb.toString();
    }
}