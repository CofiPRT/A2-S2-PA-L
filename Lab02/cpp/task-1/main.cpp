#include <bits/stdc++.h>
using namespace std;

struct Object {
	int weight;
	int price;

	Object(int _weight, int _price) : weight(_weight), price(_price) {}

	double ratio() {
		return (double)price / (double)weight;
	}
};

class Task {
 public:
	void solve() {
		read_input();
		print_output(get_result());
	}

 private:
	int n, w;
	vector<Object> objs;

	void read_input() {
		ifstream fin("in");
		fin >> n >> w;
		for (int i = 0, weight, price; i < n; i++) {
			fin >> weight >> price;
			objs.push_back(Object(weight, price));
		}
		fin.close();
	}

	static bool compare_objs(Object o1, Object o2) {
		return o1.ratio() > o2.ratio();
	}

	double get_result() {
		/*
		TODO: Aflati profitul maxim care se poate obtine cu obiectele date.
		*/
		double profit = 0.0;
		int remaining_weight = w;

		// COMPLEXITATE: O(n * log(n))
		stable_sort(objs.begin(), objs.end(), compare_objs);

		// COMPLEXITATE: O(n)
		for (auto obj : objs) {
			if (obj.weight > remaining_weight) {
				// taiere
				profit += obj.ratio() * (double)remaining_weight;
				remaining_weight = 0;
			} else {
				profit += obj.price;
				remaining_weight -= obj.weight;
			}

			if (remaining_weight == 0) {
				// nu mai e spatiu in rucsac
				break;
			}
		}

		// COMPLEXITATE FINALA: O(n * log(n))
		return profit;
	}

	void print_output(double result) {
		ofstream fout("out");
		fout << setprecision(4) << fixed << result;
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
