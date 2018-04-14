package com.msemu.commons.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Weber on 2018/3/14.
 */
public class Rand {

    public Rand() {
    }

    public static double get() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static int get(int n) {
        return ThreadLocalRandom.current().nextInt(Math.abs(n));
    }

    public static long get(long n) {
        return (long) (ThreadLocalRandom.current().nextDouble() * (double) n);
    }

    public static double get(double n) {
        return ThreadLocalRandom.current().nextDouble() * n;
    }

    public static int get(int min, int max) {
        return min + get(max - min + 1);
    }

    public static long get(long min, long max) {
        return min + get(max - min + 1L);
    }

    public static double get(double min, double max) {
        return min + get(max - min + 1.0D);
    }

    public static int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double nextGaussian() {
        return ThreadLocalRandom.current().nextGaussian();
    }

    public static boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static byte[] nextBytes(byte[] bytes) {
        ThreadLocalRandom.current().nextBytes(bytes);
        return bytes;
    }

    public static boolean getChance(int chance) {
        return getChance(chance, 100);
    }

    public static boolean getChance(double chance) {
        return getChance(chance, 100);
    }

    public static boolean getChance(double chance, double max) {
        return ThreadLocalRandom.current().nextDouble() * max <= chance;
    }

    public static <E> E get(E[] list) {
        return list[get(list.length)];
    }

    public static int get(int[] list) {
        return list[get(list.length)];
    }

    public static <E> E get(List<E> list) {
        return list.get(get(list.size()));
    }
}
