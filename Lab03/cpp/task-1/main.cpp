#include <bits/stdc++.h>
using namespace std;

#define MAX_VALUE 100

class Task {
 public:
    void solve() {
        read_input();
        print_output(get_result());
    }

 private:
    int n, S;
    vector<int> v;

    void read_input() {
        ifstream fin("in");
        fin >> n >> S;
        for (int i = 1, e; i <= n; i++) {
            fin >> e;
            v.push_back(e);
        }
        fin.close();
    }

    int get_result() {
        /*
        TODO: Aflati numarul minim de monede ce poate fi folosit pentru a obtine suma S. Tipurile monedelor sunt stocate in vectorul v, de dimensiune n.
        */

        int max = S + 1;
        int dp[MAX_VALUE];

        // popularea array-ului 'dp' cu valoarea 'S + 1'
        for (int i = 0; i < max; i++) {
            dp[i] = max;
        }

        dp[0] = 0;

        for (int curr_amount = 1; curr_amount <= S; curr_amount++) {
            // pentru fiecare suma pana in S
            for (int coin : v) {
                // pentru fiecare moneda
                if (coin <= curr_amount) {
                    // se poate folosi pentru plata
                    int required_coins = dp[curr_amount - coin] + 1;
                    dp[curr_amount] = min(dp[curr_amount], required_coins);
                }
            }
        }

        // daca aceasta valoare a ramas S + 1, inseamna ca nu a fost
        // posibil sa fie platita, return -1
        return dp[S] > S ? -1 : dp[S];
    }

    void print_output(int result) {
        ofstream fout("out");
        fout << result;
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
