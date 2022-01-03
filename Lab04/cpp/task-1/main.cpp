#include <bits/stdc++.h>
using namespace std;

const int kMod = 1e9 + 7;

class Task {
 public:
    void solve() {
        read_input();
        print_output(get_result());
    }

 private:
    int n;
    vector<int> v;

    void read_input() {
        ifstream fin("in");
        fin >> n;
        v.push_back(-1); // adaugare element fictiv - indexare de la 1
        for (int i = 1, e; i <= n; i++) {
            fin >> e;
            v.push_back(e);
        }
        fin.close();
    }

    int modpow()

    int get_result() {
        /*
        Calculati numarul de subsiruri ale lui v cu suma numerelor para si
        returnati restul impartirii numarului la 10^9 + 7.
        */
        unsigned long long dpPar = 0;    // subsiruri cu suma para
        unsigned long long dpImpar = 0;  // subsiruru cu suma impara

        unsigned long long nrPar = 0;

        for (u_int i = 1; i <= n; i++) {
            if (v[i] % 2) {
                // numar impar:
                    // suma para + suma impara = suma impara
                unsigned long long dpImparTemp = dpImpar;

                    // +1 pentru subsirul cu suma impara format din acest
                    // element impar
                dpImpar += dpPar + 1;
                    // suma impara + element impar = suma para
                dpPar += dpImparTemp;
            } else {
                nrPar++;
                // numar par:
                    // suma para + suma para = suma para
                    // suma para + acest element par = suma para
                dpPar += dpPar + 1;

                    // suma impara + element par = suma impara
                    // acest element poate fi adaugat la orice "subsir impar"
                    // precedent
                dpImpar += dpImpar;
            }

            // face overflow repede si returneaza 582344007 de fiecare data
            // fa asta...
            dpImpar %= kMod;
            dpPar %= kMod;
        }

        unsigned long long formula = pow(2, n - (nrPar < n ? 1 : 0)) - 1;

        return (dpPar % kMod == formula % kMod) ? (dpPar % kMod) : (formula % kMod);
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
