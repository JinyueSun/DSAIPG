package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;
import com.phasmidsoftware.dsaipg.adt.pq.PriorityQueue;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class HeapBenchmark {

    private static final Logger logger = Logger.getLogger(HeapBenchmark.class.getName());
    private static final int INSERT_COUNT = 16000;
    private static final int REMOVE_COUNT = 4000;
    private static final int HEAP_CAPACITY = 4095;

    // Stores benchmark results
    private static final Map<String, Double> insertionTimes = new LinkedHashMap<>();
    private static final Map<String, Double> deletionTimes = new LinkedHashMap<>();

    public static void main(String[] args) {
        logger.info("Starting Heap Benchmarks...");

        // Run benchmarks and collect actual timing data
        runBenchmark("Basic Binary Heap", () -> new PriorityQueue<Integer>(HEAP_CAPACITY, true, Comparator.naturalOrder(), false));
        runBenchmark("Binary Heap with Floyd's Trick", () -> new PriorityQueue<Integer>(HEAP_CAPACITY, true, Comparator.naturalOrder(), true));
        runBenchmark("4-ary Heap", () -> new FourAryHeap<Integer>(HEAP_CAPACITY, true, Comparator.naturalOrder(), false));
        runBenchmark("4-ary Heap with Floyd's Trick", () -> new FourAryHeap<Integer>(HEAP_CAPACITY, true, Comparator.naturalOrder(), true));
        runBenchmark("Fibonacci Heap", () -> new FibonacciHeap<Integer>(Comparator.naturalOrder()));

        logger.info("Heap Benchmarks Completed.");

        // Generate log/log plot with actual data
        HeapBenchmarkPlot.generatePlot(insertionTimes, deletionTimes);
    }

    private static <K> void runBenchmark(String description, Supplier<Object> supplier) {
        logger.info("Running benchmark for: " + description);

        Benchmark_Timer<Object> insertBenchmark = new Benchmark_Timer<>(
                description + " (Insertion)",
                heap -> {
                    Random random = new Random();
                    for (int i = 0; i < INSERT_COUNT; i++) {
                        int value = random.nextInt(1000000);
                        if (heap instanceof PriorityQueue) {
                            ((PriorityQueue<Integer>) heap).give(value);
                        } else if (heap instanceof FourAryHeap) {
                            ((FourAryHeap<Integer>) heap).insert(value);
                        } else if (heap instanceof FibonacciHeap) {
                            ((FibonacciHeap<Integer>) heap).insert(value);
                        }
                    }
                });

        Benchmark_Timer<Object> deleteBenchmark = new Benchmark_Timer<Object>(
                description + " (Deletion)",
                heap -> {
                    for (int i = 0; i < REMOVE_COUNT; i++) {
                        Integer removed = null;

                        // Ensure heap is NOT empty before removing elements
                        if (heap instanceof PriorityQueue && !((PriorityQueue<Integer>) heap).isEmpty()) {
                            try {
                                removed = ((PriorityQueue<Integer>) heap).take();
                            } catch (PQException e) {
                                e.printStackTrace();
                            }
                        } else if (heap instanceof FourAryHeap && !((FourAryHeap<Integer>) heap).isEmpty()) {
                            removed = ((FourAryHeap<Integer>) heap).extractRoot();
                        } else if (heap instanceof FibonacciHeap && !((FibonacciHeap<Integer>) heap).isEmpty()) {
                            removed = ((FibonacciHeap<Integer>) heap).extractMin();
                        }

                        if (removed != null) {
                            logger.fine(description + " - Removed element: " + removed);
                        }
                    }
                });

        // Run the insert benchmark first and store the heap instance
        Object heapInstance = supplier.get();
        double insertTime = insertBenchmark.runFromSupplier(() -> heapInstance, 1000);

        // Run the delete benchmark using the same heap instance
        double deleteTime = deleteBenchmark.runFromSupplier(() -> heapInstance, 1000);

        insertionTimes.put(description, insertTime);
        deletionTimes.put(description, deleteTime);

        logger.info(String.format("%s - Insert Avg Time: %.2f ms, Delete Avg Time: %.2f ms",
                description, insertTime, deleteTime));

        // Sleep between benchmarks
        try {
            logger.info("Sleeping for 5 seconds before the next benchmark...");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
