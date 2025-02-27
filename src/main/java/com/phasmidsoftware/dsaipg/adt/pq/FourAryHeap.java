package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;

public class FourAryHeap<K> {
    private K[] heap;
    private final Comparator<K> comparator;
    private final boolean max;
    private int size;

    @SuppressWarnings("unchecked")
    public FourAryHeap(int capacity, boolean max, Comparator<K> comparator, boolean floyd) {
        this.heap = (K[]) new Object[capacity + 1];  // Capacity includes extra space for root
        this.comparator = comparator;
        this.max = max;
        this.size = 0;
    }

    private int parent(int i) {
        return (i - 1) / 4;  // Parent index in 4-ary heap
    }

    private int firstChild(int i) {
        return 4 * i + 1;  // First child index
    }

    public void insert(K key) {
        if (key == null) throw new IllegalArgumentException("Cannot insert null keys.");

        // Resize if the array is full
        if (size >= heap.length - 1) {
            resizeHeap(heap.length * 2);  // Double the array size
        }

        heap[size++] = key;  // Insert the new key
        swimUp(size - 1);    // Reorder the heap
    }

    public K extractRoot() {
        if (isEmpty()) {
            System.out.println("Heap is empty! Extraction skipped.");
            return null;
        }

        K root = heap[0];
        heap[0] = heap[--size];
        heap[size] = null;  // Avoid memory leak
        sinkDown(0);        // Reorder the heap
        return root;
    }

    private void swimUp(int i) {
        while (i > 0 && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private void sinkDown(int i) {
        while (true) {
            int smallest = i;
            for (int j = 0; j < 4; j++) {
                int child = firstChild(i) + j;
                if (child < size && unordered(smallest, child)) {
                    smallest = child;
                }
            }

            if (smallest != i) {
                swap(i, smallest);
                i = smallest;
            } else {
                break;
            }
        }
    }

    private void resizeHeap(int newCapacity) {
        @SuppressWarnings("unchecked")
        K[] newHeap = (K[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
//        System.out.println("Heap resized to capacity: " + newCapacity);
    }

    private boolean unordered(int i, int j) {
        if (heap[i] == null || heap[j] == null) return false;
        return (comparator.compare(heap[i], heap[j]) > 0) ^ max;
    }

    private void swap(int i, int j) {
        K temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public K peek() {
        if (isEmpty()) throw new IllegalStateException("Heap is empty.");
        return heap[0];
    }

    public void printHeap() {
        System.out.print("Heap elements: ");
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        FourAryHeap<Integer> heap = new FourAryHeap<Integer>(10, true, Comparator.naturalOrder(), false);

        // Test insertions
        System.out.println("Inserting elements into the 4-ary heap:");
        for (int i = 1; i <= 20; i++) {
            heap.insert(i * 10);
        }
        heap.printHeap();

        // Test extractions
        System.out.println("\nExtracting elements from the 4-ary heap:");
        while (!heap.isEmpty()) {
            System.out.println("Extracted: " + heap.extractRoot());
        }
    }
}
