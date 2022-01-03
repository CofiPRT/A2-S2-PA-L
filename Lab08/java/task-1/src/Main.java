import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {
	static class Task {
		public static final String INPUT_FILE = "in";
		public static final String OUTPUT_FILE = "out";
		public static final int NMAX = 100005; // 10^5

		int n;
		int m;
		
		int[] idx;// nivelul / ordinea de vizitare
		int[] lowlink;
		
		int index = 0;
		
		Stack<Integer> stack = new Stack<>();
		
		List<List<Integer>> sol = new ArrayList<>();
		
		

		@SuppressWarnings("unchecked")
		List<Integer> adj[] = new ArrayList[NMAX];
		@SuppressWarnings("unchecked")
		List<Integer> adjt[] = new ArrayList[NMAX];

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();

				for (int i = 1; i <= n; i++) {
					adj[i] = new ArrayList<>();
					adjt[i] = new ArrayList<>();
				}
				for (int i = 1; i <= m; i++) {
					int x, y;
					x = sc.nextInt();
					y = sc.nextInt();
					adj[x].add(y);
					adjt[y].add(x);
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(List<List<Integer>> result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				pw.printf("%d\n", result.size());
				for (List<Integer> ctc : result) {
					for (int nod : ctc) {
						pw.printf("%d ", nod);
					}
					pw.printf("\n");
				}
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		private void tarjan(int vertex) {
			idx[vertex] = index;
			lowlink[vertex] = index;
			
			index++;
			
			// incepem sa verificam acest nod
			stack.push(vertex);
			
			for (int adjVertex : adj[vertex]) {
				if (idx[adjVertex] == -1) {
					// nu e definit
					tarjan(adjVertex);
					
					lowlink[vertex] = Math.min(lowlink[vertex], lowlink[adjVertex]);
				} else if (stack.contains(adjVertex)) {
					lowlink[vertex] = Math.min(lowlink[vertex], idx[adjVertex]);
				}
			}
			
			if (lowlink[vertex] == idx[vertex]) {
				// vertex este radacina unei componente tare conexe
				
				List<Integer> component = new ArrayList<>();
				
				int adjVertex = -1;
				
				while (adjVertex != vertex) {
					adjVertex = stack.pop();
					
					// adaugam in componenta conexa
					component.add(adjVertex);
				}
				
				// adaugam componenta in solutie
				sol.add(component);
			}
		}

		private List<List<Integer>> getResult() {
			// TODO: Gasiti componentele tare conexe ale grafului orientat cu
			// n noduri, stocat in adj. Rezultatul se va returna sub forma
			// unui ArrayList, ale carui elemente sunt componentele tare conexe
			// detectate. Nodurile si componentele tare conexe pot fi puse in orice
			// ordine in arraylist.
			//
			// Atentie: graful transpus este stocat in adjt.
			
			idx = new int[n + 1];
			lowlink = new int[n + 1];
			
			for (int i = 0; i <= n; i++) {
				// initializare
				idx[i] = -1;
				lowlink[i] = -1;
			}
			
			for (int currVertex = 1; currVertex <= n; currVertex++) {
				if (idx[currVertex] == -1) {
					// nu a fost vizitat
					
					tarjan(currVertex);
				}
			}
			
			return sol;
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
