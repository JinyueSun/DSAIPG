package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;

public class FibonacciHeap<K> {
    private Node<K> min;
    private int size = 0;
    private final Comparator<K> comparator;

    public FibonacciHeap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    private static class Node<K> {
        K key;
        int degree;
        Node<K> parent, child, next, prev;
        boolean marked;

        Node(K key) {
            this.key = key;
            next = prev = this;
        }
    }

    public void insert(K key) {
        Node<K> node = new Node<>(key);
        if (min == null) {
            min = node;
        } else {
            insertNode(node);
            if (comparator.compare(node.key, min.key) < 0) {
                min = node;
            }
        }
        size++;
    }

    private void insertNode(Node<K> node) {
        node.next = min;
        node.prev = min.prev;
        min.prev.next = node;
        min.prev = node;
    }

    public K extractMin() {
        if (min == null) return null;
        Node<K> oldMin = min;

        if (min.child != null) {
            Node<K> child = min.child;
            do {
                Node<K> next = child.next;
                insertNode(child);
                child.parent = null;
                child = next;
            } while (child != min.child);
        }

        min.prev.next = min.next;
        min.next.prev = min.prev;

        if (min == min.next) {
            min = null;
        } else {
            min = min.next;
            consolidate();
        }

        size--;
        return oldMin.key;
    }

    private void consolidate() {
        Map<Integer, Node<K>> degrees = new HashMap<>();
        List<Node<K>> nodes = new ArrayList<>();

        for (Node<K> x = min; nodes.isEmpty() || nodes.get(0) != x; x = x.next) {
            nodes.add(x);
        }

        for (Node<K> node : nodes) {
            int degree = node.degree;
            while (degrees.containsKey(degree)) {
                Node<K> other = degrees.get(degree);
                if (comparator.compare(node.key, other.key) > 0) {
                    Node<K> temp = node;
                    node = other;
                    other = temp;
                }
                link(other, node);
                degrees.remove(degree);
                degree++;
            }
            degrees.put(degree, node);
        }

        min = null;
        for (Node<K> node : degrees.values()) {
            if (min == null || comparator.compare(node.key, min.key) < 0) {
                min = node;
            }
        }
    }

    private void link(Node<K> child, Node<K> parent) {
        child.prev.next = child.next;
        child.next.prev = child.prev;
        child.parent = parent;
        if (parent.child == null) {
            parent.child = child;
            child.next = child.prev = child;
        } else {
            child.next = parent.child;
            child.prev = parent.child.prev;
            parent.child.prev.next = child;
            parent.child.prev = child;
        }
        parent.degree++;
        child.marked = false;
    }

    public boolean isEmpty() { return min == null; }

    public int size() { return size; }

    public K peekMin() { return min == null ? null : min.key; }
}
