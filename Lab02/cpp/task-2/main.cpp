#include <bits/stdc++.h>
using namespace std;

class Task {
 public:
	void solve() {
		read_input();
		print_output(get_result());
	}

 private:
	int n, m;
	vector<int> dist;

	void read_input() {
		ifstream fin("in");
		fin >> n >> m;
		for (int i = 0, d; i < n; i++) {
			fin >> d;
			dist.push_back(d);
		}
		fin.close();
	}

	int get_result() {
		/*
		TODO: Aflati numarul minim de opriri necesare pentru a ajunge
		la destinatie.
		*/
		int refills = 0;

		// incepe cu rezervorul plin
		int gas_tank = m;

		int prev_dist = 0; // locatia benzinariei precedente

		for (auto d : dist) {
			if ((gas_tank -= (d - prev_dist)) < 0) {
				// a fost nevoie de realimentare la statia precedenta
				gas_tank = m - (d - prev_dist); // realimentare si parcurgere
				refills++; // oprire
			}

			prev_dist = d;
		}

		return refills;
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
