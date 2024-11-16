package org.dromara.boss.controller;

import java.util.Arrays;

/**
 * @author xiexi
 * @description
 * @date 2024/11/11 20:48
 */
public class MaoPao {

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 7, 9, 5, 3, 6, 4};

        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int tem = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tem;
                }
            }
        }
        System.out.println(Arrays.toString(a));
    }
}
