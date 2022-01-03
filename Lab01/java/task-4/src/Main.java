import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    static class Task {
        public final static String INPUT_FILE = "in";
        public final static String OUTPUT_FILE = "out";

        int base;
        int exponent;
        int mod;

        private void readInput() {
            try {
                Scanner sc = new Scanner(new File(INPUT_FILE));
                base = sc.nextInt();
                exponent = sc.nextInt();
                mod = sc.nextInt();
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeOutput(long result) {
            try {
                PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
                pw.printf("%d\n", result);
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private int fastPow(int base, int exponent, int mod) {
            // TODO: Calculati (base^exponent) modulo mod in O(log exponent).
            if (base == 0) {
                return 0;
            }
            if (exponent == 0) {
                return 1;
            }

            long res;
            if (exponent % 2 == 0) {
                res = fastPow(base, exponent/2, mod);
                res = (res*res) % mod;
            } else {
                res = fastPow(base, exponent - 1, mod);
                res = ((base % mod) * (res)) % mod;
            }

            return ((int)(res)) % mod;
        }

        public void solve() {
            readInput();
            writeOutput(fastPow(base, exponent, mod));
        }
    }

    public static void main(String[] args) {
        new Task().solve();
    }
}
