#include <bits/stdc++.h>
using namespace std;

struct Object {
	int before;
	int after;

	Object(int _before, int _after) : before(_before), after(_after) {}
};

class Task {
 public:
	void solve() {
		read_input();
		print_output(get_result());
	}

 private:
	int n, k;
	vector<Object> objs;

	void read_input() {
		ifstream fin("in");

		fin >> n >> k;
		for (int i = 0, before, after; i < n; i++) {
			fin >> before;
			objs.push_back(Object(before, 0));
		}

		for (auto &obj : objs) {
			int x;
			fin >> x;
			obj.after = x;
		}

		fin.close();
	}

	static int compare_objs(Object o1, Object o2) {
		// crescator dupa diferenta de pret
		return o1.before - o1.after < o2.before - o2.after;
	}

	int get_result() {
		int money_spent = 0;
		int items_bought = 0;

		stable_sort(objs.begin(), objs.end(), compare_objs);

		for (auto obj : objs) {
			if (obj.before <= obj.after) {
				items_bought++;
				money_spent += obj.before;
			} else if (items_bought < k) {
				items_bought++;
				money_spent += obj.before;
			} else {
				items_bought++;
				money_spent += obj.after;
			}
		}

		return money_spent;
	}

	void print_output(int result) {
		ofstream fout("out");
		fout << setprecision(4) << fixed << result << endl;
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
