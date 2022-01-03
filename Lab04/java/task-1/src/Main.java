import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    static class Task {
        public final static String INPUT_FILE = "in";
        public final static String OUTPUT_FILE = "out";

        int n;
        int[] v;

        private final static int MOD = 1000000007;

        private void readInput() {
            try {
                Scanner sc = new Scanner(new File(INPUT_FILE));
                n = sc.nextInt();
                v = new int[n + 1];
                for (int i = 1; i <= n; i++) {
                    v[i] = sc.nextInt();
                }
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeOutput(int result) {
            try {
                PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
                pw.printf("%d\n", result);
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private long modpow(long base, long exp, long mod) {
            long res = 1;

            base %= mod;

            while (exp > 0) {
                if (exp % 2 == 1) {
                    res = (res * base) % mod;
                }

                exp /= 2;
                base = (base*base) % mod;
            }

            return res;
        }

        private int getResult() {
            // TODO: Aflati numarul de subsiruri (ale sirului stocat in v,
            // indexat de la 1 la n), nevide cu suma numerelor para.
            // Rezultatul se va intoarce modulo MOD (1000000007).

            long dpPar = 0;
            long dpImpar = 0;
            long elemPare = 0;

            for (int i = 0; i < n; i++) {
                if (v[i] % 2 == 1) {
                    long dpImparTemp = dpImpar;
                    dpImpar += dpPar + 1;
                    dpPar += dpImparTemp;
                } else {
                    elemPare++;
                    dpPar += dpPar + 1;
                    dpImpar += dpImpar;
                }

                dpImpar %= MOD;
                dpPar %= MOD;
            }

            long formula = modpow(2, n - (elemPare < n ? 1 : 0), MOD) - 1;

            return (int)(dpPar == formula ? dpPar : formula);
        }

        public void solve() {
            readInput();
            writeOutput(getResult());
        }
    }

    public static void main(String[] args) {
        new Task().solve();
    }
}
