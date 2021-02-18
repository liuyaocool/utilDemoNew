package com.liuyao.algorithm.util;

import com.liuyao.algorithm.sort.SortUtils;

import java.lang.reflect.Array;
import java.util.Random;

/**
 * 对数器
 */
public class DataChecker {

    static final Random random = new Random();

    public static <T> T[] copyArray(T[] arr){
        T[] ret = (T[]) Array.newInstance(arr[0].getClass(), arr.length);
        for (int i = 0; i < ret.length; i++) {
            ret[i] = arr[i];
        }
        return ret;
    }

    public static int[] randomArray(int len){
        int[] ret = new int[len];
//        len *= 5;
        for (int i = 0; i < ret.length; i++) {
            ret[i] = random.nextInt(len) + 1;
        }
        return ret;
    }

    private static void correctSort(int[] correct) {
    }

    public static int[] copyArray(int[] arr){
        int[] ret = new int[arr.length];
        System.arraycopy(arr, 0, ret, 0, ret.length);
        return ret;
    }

    public static boolean check(int[] arr1, int[] arr2){
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                System.out.println("数组不同。");
                return false;
            }
        }
        System.out.println("数组相同。");
        return true;
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void print(String name, int[] arr){
        System.out.print(name);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(" " + arr[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] a = randomArray(10);
        int[] copy = copyArray(a);
        int[] correct = copyArray(a);
        correctSort(correct);
        SortUtils.quickSort(a, 0, a.length-1);

        print("原来数组：", copy);
        print("排序之后：", a);
        print("正确排序：", correct);
        check(correct, a);
    }

}
