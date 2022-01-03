import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    static class Task {
        public final static String INPUT_FILE = "in";
        public final static String OUTPUT_FILE = "out";

        private final static int MOD = 1000000007;
        public long dp[][][];

        int n;
        char[] expr;

        private void readInput() {
            try {
                Scanner sc = new Scanner(new File(INPUT_FILE));
                n = sc.nextInt();
                String s = sc.next().trim();
                s = " " + s;
                expr = s.toCharArray();
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

        public long testSubstr(String str, int i, int j, int matrix) {
        // matrix = 1, pentru matricea pentru evaluari la True
        // matrix = 0, pentru matricea pentru evaluari la False
       
        if (i > j) return 0; // nu are sens
       
        if (i == j) {
            // caz de baza
            if (matrix == 1) {
                // se doreste evaluarea la True
                return str.charAt(i) == 'T' ? 1 : 0;
            } else {
                // se doreste evaluarea la False
                return str.charAt(i) == 'F' ? 1 : 0;
            }
        }
       
        if (dp[matrix][i][j] != -1) {
            // deja am calculat aceasta subproblema
            return dp[matrix][i][j];
        }
       
        dp[matrix][i][j] = 0; // se pregateste pentru calcul
       
        for (int index = i + 1; index < j; index += 2) {
            /* se imparte subproblema in stanga si dreapta.
             * mijlocul fiind mereu pe un operator (se itereaza prin toti)
             *
             * leftT -> in cate modalitati se poate evalua partea stanga la True
             */
           
            long leftT = testSubstr(str, i, index - 1, 1);
            long leftF = testSubstr(str, i, index - 1, 0);
            long rightT = testSubstr(str, index + 1, j, 1);
            long rightF = testSubstr(str, index + 1, j, 0);
           
            /* dupa calcularea solutiilor, se genereaza o noua solutie
             * in functie de operator
             *
             * pentru fiecare operator, rezultatul depinde de matricea 'matrix'
             * ce se doreste a fi umpluta
             * ex: pentru matrix = 1 (True), se calculeaza in cate modalitati
             * operatorul OP returneaza True, in functie de evaluarile stanga si dreapta
             */
            switch (str.charAt(index)) {
                case '&':
                    if (matrix == 1) {
                        // cand returneaza & True? doar cand left SI (*) right sunt True
                        dp[matrix][i][j] += (leftT*rightT)%MOD;
                    } else {
                        // returneaza false cand left e false, SAU (+) right e false, SAU ambele
                        dp[matrix][i][j] += (leftF*rightT)%MOD +
                                            (leftT*rightF)%MOD +
                                            (leftF*rightF)%MOD;
                    }
                    break;
                case '|':
                    if (matrix == 1) {
                        dp[matrix][i][j] += (leftF*rightT)%MOD + (leftT*rightF)%MOD + (leftT*rightT)%MOD;
                    } else {
                        dp[matrix][i][j] += (leftF*rightF)%MOD;
                    }
                    break;
                case '^':
                    if (matrix == 1) {
                        dp[matrix][i][j] += (leftF*rightT)%MOD + (leftT*rightF)%MOD;
                    } else {
                        dp[matrix][i][j] += (leftF*rightF)%MOD + (leftT*rightT)%MOD;
                    }
            }
        }
       
        // prevenire overflow
        return (dp[matrix][i][j] % MOD);
    }

        private int getResult() {
            // TODO: Calculati numarul de moduri in care se pot aseza
            // parantezele astfel incat rezultatul expresiei sa fie TRUE.
            // Numarul de moduri se va calcula modulo MOD (1000000007).
            dp = new long[2][n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dp[0][i][j] = -1;
                    dp[1][i][j] = -1;
                }
            }

            return (int)testSubstr(String.valueOf(expr).trim(), 0, n - 1, 1);
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
