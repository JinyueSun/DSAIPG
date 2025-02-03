/*
 * Copyright (c) 2017-2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.misc.randomwalk;

import org.apache.commons.math3.util.Pair;

import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.util.*;

/**
 * The RandomWalk class simulates a two-dimensional random walk. A "drunkard"
 * moves in a random direction for a specified number of steps, and the distance
 * from the starting point is measured. Additionally, multiple random walk
 * experiments can be performed to compute average distances.
 */
public class RandomWalk {
    // map:{step -> map:{(x, y) -> distance}}
    static HashMap<Integer, HashMap<Pair<Integer, Integer>, Double>> experimentResMap = new HashMap<>();
    /**
     * Method to compute the distance from the origin (the lamp-post where the drunkard starts) to his current position.
     *
     * @return the (Euclidean) distance from the origin to the current position.
     */
    public double distance() {
        // TO BE IMPLEMENTED
         return Math.sqrt((double)this.x * (double)this.x + (double)this.y * (double)this.y);
        // END SOLUTION
    }

    /**
     * Private method to move the current position, that's to say the drunkard moves
     *
     * @param dx the distance he moves in the x direction
     * @param dy the distance he moves in the y direction
     */
    private void move(int dx, int dy) {
        try {
            this.x = this.x + dx;
            this.y = this.y + dy;
        } catch (Exception e) {
            throw new RuntimeException("Not implemented");
        }
        // TO BE IMPLEMENTED  do move
        // END SOLUTION
    }

    /**
     * Perform a random walk of m steps
     *
     * @param m the number of steps the drunkard takes
     */
    private void randomWalk(int m) {
        try {
            while (m-- != 0) {
                randomMove();
            }
        } catch (Exception e) {
            // TO BE IMPLEMENTED
            throw new RuntimeException("implementation missing");
        }
    }

    /**
     * Private method to generate a random move according to the rules of the situation.
     * That's to say, moves can be (+-1, 0) or (0, +-1).
     */
    private void randomMove() {
        boolean ns = random.nextBoolean();
        int step = random.nextBoolean() ? 1 : -1;
        move(ns ? step : 0, ns ? 0 : step);
    }

    private int x = 0;
    private int y = 0;

    private final Random random = new Random();

    /**
     * Perform multiple random walk experiments, returning the mean distance.
     *
     * @param m the number of steps for each experiment
     * @param n the number of experiments to run
     * @return the mean distance
     */
    public static double randomWalkMulti(int m, int n) {
        double totalDistance = 0;
        HashMap<Pair<Integer, Integer>, Double> res = experimentResMap.getOrDefault(m, new HashMap<>());
        for (int i = 0; i < n; i++) {
            RandomWalk walk = new RandomWalk();
            walk.randomWalk(m);
            res.put(new Pair<>(walk.x, walk.y), walk.distance());
            totalDistance = totalDistance + walk.distance();
        }
        experimentResMap.put(m, res);
        return totalDistance / n;
    }

    /**
     * The main method serves as the entry point to the RandomWalk program. It performs
     * either a single random walk experiment or several experiments, based on the
     * provided input arguments, and prints the mean distance.
     *
     * @param args command-line arguments where:
     *             args[0] specifies the number of steps for a random walk (required),
     *             and args[1] optionally specifies the number of experiments (default is 30).
     *             If args is empty, the method throws a RuntimeException indicating invalid syntax.
     */
    public static void main(String[] args) {
//        if (args.length == 0)
//            throw new RuntimeException("Syntax: RandomWalk steps [experiments]");
        List<Integer> steps = new ArrayList<>();
        List<Double> meanDistanceList = new ArrayList<>();
        List<Double> theoreticalDistances = new ArrayList<>();
        int count = 0;
        int step = 1;
        while (++count <= 500) {
//            int m = Integer.parseInt(args[0]);
            steps.add(step);
            theoreticalDistances.add(Math.sqrt(step));
            int m = step;
            int n = 10;
//            if (args.length > 1) n = Integer.parseInt(args[1]);
            double meanDistance = randomWalkMulti(m, n);
            meanDistanceList.add(meanDistance);
            System.out.println(m + " steps: " + meanDistance + " over " + n + " experiments");
            step = step + 10;
        }

        // print each position and distance to original

//        System.out.print("=============================\n");
//
//        for (Integer stepValue : experimentResMap.keySet()) {
//            System.out.printf("%d steps, experiment result:\n", stepValue);
//            for (Map.Entry<Pair<Integer, Integer>, Double> entry : experimentResMap.get(stepValue).entrySet()) {
//                System.out.printf("%d steps : (x, y) => (%d, %d), distance : %.5f)\n",
//                        stepValue, entry.getKey().getKey(), entry.getKey().getValue(), entry.getValue());
//            }
//        }

        // create the graph
        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Random Walk, Relationship between distance & steps")
                .xAxisTitle("step (m)")
                .yAxisTitle("mean distance (d)")
                .build();

        // paint the mock result curve
        XYSeries series1 = chart.addSeries("mock result", steps, meanDistanceList);
        series1.setMarker(SeriesMarkers.CIRCLE);

        // paint the logical result curve √m
        XYSeries series2 = chart.addSeries("Logical result (√m)", steps, theoreticalDistances);
        series2.setMarker(SeriesMarkers.NONE);

        // show picture
        new SwingWrapper<>(chart).displayChart();

    }
}