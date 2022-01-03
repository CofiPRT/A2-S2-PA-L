#include <bits/stdc++.h>
using namespace std;

#define MAX_LENGTH 100

class Task {
 public:
    void solve() {
        read_input();
        print_output(get_result());
    }

 private:
    int n;
    vector<vector<int>> matrix;

    void read_input() {
        ifstream fin("in");
        fin >> n;

        int i, j, e;

        for (i = 0; i < n; i++) {
            vector<int> row;
            for (j = 0; j < n; j++) {
                fin >> e;
                row.push_back(e);
            }

            matrix.push_back(row);
        }

        fin.close();
    }

    vector<int> get_result() {
        vector<int> result(n, 0);
        // result[i] -> numarul de submatrici (i+1)x(i+1)

        vector<vector<int>> dp(n, vector<int>(n, 0));
        // matrice nxn de 0-uri
        int count = 0;

        // marginea din stanga (j = 0)
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix[i][0] == 1) {
                dp[i][0] = 1;
                result[0]++;
            }
        }

        // marginea de sus (i = 0)
        // matrix[0][0] a fost deja parcurs
        for (int j = 1; j < n; j++) {
            if (matrix[0][j] == 1) {
                dp[0][j] = 1;
                result[0]++;
            }
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 1) {
                    // minimul dintre vecinii sus, stanga, sus-stanga
                    dp[i][j] = min(min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]) + 1;

                    // dimensiune dp[i][j] - 1
                    result[dp[i][j] - 1]++;
                }
            }
        }

        /*  daca un element face parte dintr-o submatrice
            de dimensiune d, sigur face parte si din cate o
            submatrice de dimensiune [1..(d-1)]
        */ 
        for (int i = n - 1; i > 0; i--) {
            result[i - 1] += result[i];
        }

        /*  Complexitate temporala:
                Parcurgere matrice: O(n^2)
                Calculare rezultate: O(n)
                    TOTAL: T = O(n^2)
            
            Complexitate spatiala:
                Matrice auxiliara: O(n^2)
                Vector de rezultate: O(n)
                    TOTAL: S = O(n^2)
        */
        return result;
    }

    void print_output(vector<int> result) {
        ofstream fout("out");

        for (int x : result) {
            fout << x << endl;
        }

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
