package com.legaoyi.common.util;

import java.util.Arrays;

/**
 * @Description:组合算法 从M个数中取出N个数，无顺序 二维Object数组
 */
public class CombineAlgorithm {

    /* src数组的长度 */
    private int m;

    /* 需要获取N个数 */
    private int n;

    // 临时变量，obj的行数
    private int objLineIndex;

    /* 存放结果的二维数组 */
    public Object[][] result;

    public CombineAlgorithm(Object[] src, int getNum) throws Exception {
        if (src == null){
            throw new Exception("原数组为空.");
        }
        if (src.length < getNum){
            throw new Exception("要取的数据比原数组个数还 大 .");
        }

        m = src.length;
        n = getNum;

        /* 初始化 */
        objLineIndex = 0;
        result = new Object[combination(m, n)][n];

        Object[] tmp = new Object[n];
        combine(src, 0, 0, n, tmp);
    }

    /**
     * <p>
     * 计算 C(m,n)个数 = (m!)/(n!*(m-n)!)
     * </p>
     * 从M个数中选N个数，函数返回有多少种选法 参数 m 必须大于等于 n m = 0; n = 0; retuan 1;
     * 
     * @param m
     * @param n
     * @return
     * @since royoan 2014-6-13 下午8:25:33
     */
    public int combination(int m, int n) {
        if (m < n)
            return 0; // 如果总数小于取出的数，直接返回0

        int k = 1;
        int j = 1;
        // 该种算法约掉了分母的(m-n)!,这样分子相乘的个数就是有n个了
        for (int i = n; i >= 1; i--) {
            k = k * m;
            j = j * n;
            m--;
            n--;
        }
        return k / j;
    }

    /**
     * <p>
     * 递归算法，把结果写到obj二维数组对象
     * </p>
     * 
     * @param src
     * @param srcIndex
     * @param i
     * @param n
     * @param tmp
     */
    private void combine(Object src[], int srcIndex, int i, int n, Object[] tmp) {
        int j;
        for (j = srcIndex; j < src.length - (n - 1); j++) {
            tmp[i] = src[j];
            if (n == 1) {
                System.arraycopy(tmp, 0, result[objLineIndex], 0, tmp.length);
                objLineIndex++;
            } else {
                n--;
                i++;
                combine(src, j + 1, i, n, tmp);
                n++;
                i--;
            }
        }

    }

    public Object[][] getResult() {
        return result;
    }

    /**
     * 用法实例
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Integer[] a = new Integer[] {1, 2, 3, 4, 5, 6};
        CombineAlgorithm ca = new CombineAlgorithm(a, 3);

        Object[][] c = ca.getResult();
        for (int i = 0; i < c.length; i++) {
            System.out.println(Arrays.toString(c[i]));
        }
    }

}
