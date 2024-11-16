package org.dromara.boss.controller;

import java.util.Arrays;

/**
 * @author xiexi
 * @description
 * @date 2024/11/11 20:08
 */
public class Test1 {

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 7, 9};
        int[] b = {2, 4, 6};
        int[] merge = merge(a, b);
        System.out.println(Arrays.toString(merge));
    }

    public static int[] merge(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        int a1 = 0, b1 = 0, c1 = 0;
        while ((a1 < a.length || b1 < b.length) && (c1 < (a.length + b.length))) {
            Integer ac1 = null;
            if (a1 < a.length) {
                ac1 = a[a1];
            }
            Integer bc1 = null;
            if (b1 < b.length) {
                bc1 = b[b1];
            }
            if (null != ac1 && null != bc1) {
                if (ac1 > bc1) {
                    c[c1] = bc1;
                    b1++;
                } else {
                    c[c1] = ac1;
                    a1++;
                }
            } else {
                if (null == ac1) {
                    c[c1] = bc1;
                    b1++;
                }
                if (null == bc1) {
                    c[c1] = ac1;
                    a1++;
                }
            }
            c1++;
        }
        return c;
    }
}
