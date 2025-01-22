/*
 * Copyright (c) 2017-2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.misc;

class NewtonApproximation {
    public static void main(String[] args) {
        // Newton's Approximation to solve sin(x) = x
        double x = 1.0;
        int left = 200;
        for (; left > 0; left--) {
            // calculate f(x)
            final double y = Math.sin(x) - x;
            // check if f(x) < tolerance
            if (Math.abs(y) < 1E-7) {
                System.out.println("the solution to sin(x)=x is: " + x);
                System.exit(0);
            }
            // update x = x - f(x) / f'(x)
            x = x - y / (Math.cos(x) - 1);
        }
    }
}