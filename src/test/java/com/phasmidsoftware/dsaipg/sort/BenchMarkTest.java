package com.phasmidsoftware.dsaipg.sort;
import com.phasmidsoftware.dsaipg.sort.elementary.InsertionSortBasic;
import com.phasmidsoftware.dsaipg.sort.elementary.InsertionSortComparator;
import com.phasmidsoftware.dsaipg.util.Timer;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static com.phasmidsoftware.dsaipg.util.Config_Benchmark.setupConfigFixes;

public class BenchMarkTest {

    // initialize the length of array
    private static final int[] SIZES = {1000, 2000, 4000, 8000, 16000}; // Doubling sizes

    // set order types
    private static final String[] ORDER_TYPES = {"Random", "Ordered", "Partially Ordered", "Reverse Ordered"};


    private final Timer timer = new Timer(); // Use the Timer class for benchmarking
    private InsertionSortBasic<Integer> sorter = InsertionSortBasic.create();
    @Test
    public void testInsertionSortPerformance() {
        for (String order : ORDER_TYPES) {
            System.out.println("\nTesting Insertion Sort on " + order + " Arrays:\n");

            for (int n : SIZES) {
                Integer[] array = generateArray(n, order);
                //Integer[] copy = Arrays.copyOf(array, array.length);
                Comparator<Integer> comparator = Integer::compareTo;
                Helper<Integer> helper = HelperFactory.createGeneric("Test", comparator, array.length, 1, setupConfigFixes());
                Sort<Integer> sorter = new InsertionSortComparator<>(helper);
////                // Measure execution time using Timer.repeat()
//                double meanTime = timer.repeat(20, () -> {
//                    sorter.sort(array, true);
//                    return null;
//                });
                long iteration = 50;
                long sumTime = 0;
                for (int i = 0; i < iteration; i++) {
                    Integer[] copy = Arrays.copyOf(array, array.length);
                    long start = System.nanoTime();
//                    sorter.sort(copy, 0, copy.length);
                    sorter.sort(array, true);
                    sumTime = sumTime + (System.nanoTime() - start);
                }
                System.out.printf("Size: %d | Time: %.6f ms%n", n, (double) (sumTime / 100_000_0.0 / iteration));
//                System.out.printf("Size: %d | Time: %.6f ms%n", n, meanTime);

            }
        }
    }

    /**
     * Generates an array of a given size with different orderings.
     *
     * @param n    size of the array
     * @param type ordering type ("Random", "Ordered", "Partially Ordered", "Reverse Ordered")
     * @return generated array
     */
    private static Integer[] generateArray(int n, String type) {
        Integer[] arr = new Integer[n];
        Random rand = new Random();

        switch (type) {
            case "Random":
                for (int i = 0; i < n; i++) arr[i] = rand.nextInt(n);
                break;
            case "Ordered":
                for (int i = 0; i < n; i++) arr[i] = i;
                break;
            case "Partially Ordered":
                for (int i = 0; i < n; i++) arr[i] = (i < n / 2) ? i : rand.nextInt(n);
                break;
            case "Reverse Ordered":
                for (int i = 0; i < n; i++) arr[i] = n - i;
                break;
        }
        return arr;
    }
}
