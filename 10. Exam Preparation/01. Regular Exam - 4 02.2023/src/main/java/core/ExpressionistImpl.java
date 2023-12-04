
package core;

import models.Expression;
import models.ExpressionType;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class ExpressionistImpl implements Expressionist {
    private Map<String, Expression> expressionsById = new HashMap<>();
    private Expression root;

    @Override
    public void addExpression(Expression expression) {
        if (!this.expressionsById.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.root = expression;
        this.expressionsById.put(expression.getId(), expression);
    }

    @Override
    public void addExpression(Expression expression, String parentId) {
        Expression parent = this.expressionsById.get(parentId);
        if (parent == null) {
            throw new IllegalArgumentException();
        }

        if (parent.getLeftChild() == null) {
            parent.setLeftChild(expression);
        } else if (parent.getLeftChild() != null && parent.getRightChild() == null) {
            parent.setRightChild(expression);
        } else if (parent.getLeftChild() != null && parent.getRightChild() != null) {
            throw new IllegalArgumentException();
        }

        this.expressionsById.put(expression.getId(), expression);
    }

    @Override
    public boolean contains(Expression expression) {
        return this.expressionsById.containsKey(expression.getId());
    }

    @Override
    public int size() {
        return this.expressionsById.size();
    }

    @Override
    public Expression getExpression(String expressionId) {
        if (!this.expressionsById.containsKey(expressionId)) {
            throw new IllegalArgumentException();
        }
        return this.expressionsById.get(expressionId);
    }

    @Override
    public void removeExpression(String expressionId) {
        if (!this.expressionsById.containsKey(expressionId)) {
            throw new IllegalArgumentException();
        }
        Expression removed = this.expressionsById.remove(expressionId);
        boolean flag = false;

        for (Expression expression : this.expressionsById.values()) {
            if (expression.getLeftChild().equals(removed)) {
                expression.setLeftChild(expression.getRightChild());
                expression.setRightChild(null);
                flag = true;
            } else if (expression.getRightChild().equals(removed)) {
                expression.setRightChild(null);
                flag = true;
            }

            if (flag) {
                break;
            }
        }

        removeChildrenBFS(removed);
    }

    private void removeChildrenBFS(Expression removed) {
        ArrayDeque<Expression> deque = new ArrayDeque<>();
        deque.offer(removed);

        while (!deque.isEmpty()) {
            Expression current = deque.poll();
            this.expressionsById.remove(current.getId());

            if (current.getLeftChild() != null) {
                deque.offer(current.getLeftChild());
            }
            if (current.getRightChild() != null) {
                deque.offer(current.getRightChild());
            }
        }
    }

    @Override
    public String evaluate() {
        StringBuilder sb = new StringBuilder();
        inOrder(this.root, sb);
        return sb.toString();
    }

    private void inOrder(Expression node, StringBuilder sb) {
        if (node == null) {
            return;
        }

        inOrder(node.getLeftChild(), sb);
        if (node.getType().equals(ExpressionType.VALUE)) {
            sb.append(node.getValue());
        } else if (node.getType().equals(ExpressionType.OPERATOR)) {
            String r = "("
                    + node.getLeftChild().getValue()
                    + " "
                    + node.getValue()
                    + " "
                    + node.getRightChild().getValue()
                    + ")";
            sb.append(r);
        }

        inOrder(node.getRightChild(), sb);
    }
}