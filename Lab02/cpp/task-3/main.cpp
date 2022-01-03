#include <bits/stdc++.h>
#include <iostream>
using namespace std;

struct Homework {
	int deadline;
	int score;

	Homework(int _deadline, int _score) : deadline(_deadline), score(_score) {}
};

class Task {
 public:
	void solve() {
		read_input();
		print_output(get_result());
	}

 private:
	int n;
	vector<Homework> hws;

	void read_input() {
		ifstream fin("in");
		fin >> n;
		for (int i = 0, deadline, score; i < n; i++) {
			fin >> deadline >> score;
			hws.push_back(Homework(deadline, score));
		}
		fin.close();
	}

	static bool compare_hws(Homework h1, Homework h2) {
		// descrescator dupa scor
		return h1.score > h2.score;
	}

	int get_result() {
		/*
		TODO: Aflati punctajul maxim pe care il puteti obtine planificand
		optim temele.
		*/
		int total_score = 0;

		// descrescator dupa scor
		stable_sort(hws.begin(), hws.end(), compare_hws);

		int curr_week = 0;

		// cate teme se pot face (saptamani libere)
		int hw_energy = 0;
		
		for (auto hw : hws) {
			if (hw.deadline > curr_week) {
				// aceasta este o tema importanta, deci trebuie sa o
				// planificam prima

				// saptamani pana la tema (-1 pentru aceasta tema)
				hw_energy += hw.deadline - curr_week - 1;
				total_score += hw.score;

				// presupunem ca am avansat la aceasta saptamana
				curr_week = hw.deadline;

				// urmatoarea tema
				continue;
			}

			if (hw_energy > 0) {
				// daca mai e timp pentru aceasta tema, se face
				hw_energy--;
				total_score += hw.score;
			}

			// daca nu e timp pentru aceasta tema, asta e, restanta
		}

		return total_score;
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
