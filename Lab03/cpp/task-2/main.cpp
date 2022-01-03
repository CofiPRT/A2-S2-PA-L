#include <bits/stdc++.h>
using namespace std;

#define MAX_LENGTH 100

struct Result {
    int len;
    vector<int> subsequence;
};

class Task {
 public:
    void solve() {
        read_input();
        print_output(get_result());
    }

 private:
    int n, m;
    vector<int> v;
    vector<int> w;

    void read_input() {
        ifstream fin("in");
        fin >> n >> m;

        for (int i = 1, e; i <= n; i++) {
            fin >> e;
            v.push_back(e);
        }

        for (int i = 1, e; i <= m; i++) {
            fin >> e;
            w.push_back(e);
        }

        fin.close();
    }

    Result get_result() {
        Result result;
        result.len = 0;

        /*
        TODO: Aflati cel mai lung subsir comun intre v (de lungime n)
        si w (de lungime m).
        Se puncteaza separat urmatoarele 2 cerinte:
        2.1. Lungimea CMLSC. Rezultatul pentru cerinta aceasta se va pune in
        ``result.len``.
        2.2. Reconstructia CMLSC. Se puncteaza orice subsir comun maximal valid.
        Solutia pentru aceasta cerinta se va pune in ``result.subsequence``.
        */

        int dp[MAX_LENGTH][MAX_LENGTH] = {0};

        int i, j; // vor fi folosite ulterior
        for (i = 0; i <= n; i++) {
            for (j = 0; j <= m; j++) {
                if (i == 0 || j == 0) {
                    // initializare prima linie si prima coloana
                    dp[i][j] = 0;
                } else if (v[i - 1] == w[j - 1]) {
                    // elemente egale, lungimea creste
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // se pasteaza lungimea mai mare
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        result.len = dp[n][m];

        // de la rezultat
        i = n;
        j = m;
        while (i && j) {
            if (v[i - 1] == w[j - 1]) {
                // face parte din subsir
                result.subsequence.insert(result.subsequence.begin(), v[i - 1]);
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                // in directia celei mai mari lungimi
                i--;
            } else {
                j--;
            }
        }

        return result;
    }

    void print_output(Result result) {
        ofstream fout("out");
        fout << result.len << '\n';
        for (int x : result.subsequence) {
            fout << x << ' ';
        }
        fout << '\n';
        fout.close();
    }
};

// Please always keep this simple main function!
int main() {
    // Allocate a Task object on heap in order to be able to
    // declare huge static-allocated data structures inside the class.
    Task *task = new Task();
    task->solve();
    delete task;
    return 0;
}
