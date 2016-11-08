package com.uberprinny.rc4;

import java.util.Scanner;

/**
 * RC4.java
 * com.uberprinny.rc4
 * 11/8/16, 10:31 AM
 *
 * See: {@linkplain http://link.springer.com/chapter/10.1007/978-3-642-19574-7_5#page-1}
 */
public class RC4 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        RC4 rc4 = new RC4();

        String input = null;
        do {
            if (input != null) {
                short[] K = makeShortArray(input);
                System.out.print("Generated key: ");
                printShortArray(K);

                short[] S = makeKeySchedule(K);
                System.out.println("Key schedule length: " + S.length);
//                System.out.print("Key schedule: ");
//                printShortArray(S);
                rc4.setKeySchedule(S);

                int numRandoms = -1;
                do {
                    if (numRandoms > 0) {
                        for (int i = 0; i < numRandoms; i += 1) {
                            short z = rc4.getNextPseudoRandom();
                            System.out.println("" + (i+1) + " " + z);
                        }
                    }

                    System.out.println();
                    System.out.print("Generate x keystream words: ");
                    try {
                        String s = in.nextLine();
                        numRandoms = Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        numRandoms = -1;
                        System.out.println();
                    }
                } while (numRandoms > 0);
            }

            for (int i = 0; i < 28; i += 1) {
                System.out.print("=");
            }
            System.out.println();
            System.out.print("Enter a secret key: ");
            input = in.nextLine();
        } while (input.length() > 0);

        in.close();
    }

    private static short[] makeShortArray(String s) {
        final int L = s.length();
        short[] a = new short[(L+1)/2];
        for (int i = 0; i < a.length; i += 1) {
            a[i] = 0;
        }
        for (int i = 0; i < L; i += 1) {
            a[i/2] += (short) (s.charAt(i) << (8*(i%2)));
        }
        return a;
    }

    private static void printShortArray(short[] a) {
        for (int i = 0; i < a.length; i += 1) {
            System.out.print(a[i]);
            if (i != a.length - 1) {
                System.out.print(":");
            }
        }
        System.out.println();
    }

    private static short[] makeKeySchedule(short[] K) {
        final int l = K.length;
        final int n = 16; // short has 16 bits
        final int N = power(2, n);
        short[] S = new short[N];
        for (int i = 0; i < N; i += 1) {
            S[i] = (short) i;
        }
        int j = 0;
        for (int i = 0; i < N; i += 1) {
            j = mod(j + S[i] + K[(i % l)], N);
            swap(S, i, j);
        }
        return S;
    }

    private void setKeySchedule(short[] S) {
        _S = S;
        _N = S.length;
    }

    private short[] _S;
    private int _N;
    private int _i = 0, _j = 0;

    private short getNextPseudoRandom() {
        _i = mod(_i + 1, _N);
        _j = mod(_j + _S[_i], _N);
        swap(_S, _i, _j);
        return _S[mod(_S[_i] + _S[_j], _N)];
    }

    private static int power(int base, int exponent) {
        if (exponent < 0) {
            return 0;
        } else if (exponent < 1) {
            return 1;
        } else if (exponent < 2) {
            return base;
        }
        return base * power(base, exponent - 1);
    }

    private static void swap(short[] a, int i, int j) {
        short t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static int mod(int x, int N) {
        int r = x % N;
        if (r < 0) {
            r += N;
        }
        return r;
    }
}
