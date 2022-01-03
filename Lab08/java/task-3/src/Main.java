import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	static class Task {
		public static final String INPUT_FILE = "in";
		public static final String OUTPUT_FILE = "out";
		public static final int NMAX = 100005; // 10^5

		int n;
		int m;
		
		int[] idx; // idx[i] = timpul de descoperire pt nodul i
		int[] low;
		
		int time = 0;
		
		List<Edge> sol = new ArrayList<>();

		@SuppressWarnings("unchecked")
		List<Integer> adj[] = new ArrayList[NMAX];

		class Edge {
			int x;
			int y;
			
			Edge(int x, int y) {
				this.x = x;
				this.y = y;
			}
		}

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();

				for (int i = 1; i <= n; i++)
					adj[i] = new ArrayList<>();
				for (int i = 1; i <= m; i++) {
					int x, y;
					x = sc.nextInt();
					y = sc.nextInt();
					adj[x].add(y);
					adj[y].add(x);
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(List<Edge> result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				pw.printf("%d\n", result.size());
				for (Edge e : result) {
					pw.printf("%d %d\n", e.x, e.y);
				}
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		private void computeBridges(int vertex, int parent) {
			idx[vertex] = time;
			low[vertex] = time;
			
			// timpul de descoperire
			time++;
			
			for (int adjVertex : adj[vertex]) {
				if (adjVertex != parent) {
					// vertex NU este un copil de-al lui adjVertex
					
					if (idx[adjVertex] == -1) {
						// nu e definit, deci adjVertex nu a fost procesat inca
						
						// parcurgem in adancime, continuand cu adjVertex
						computeBridges(adjVertex, vertex);
						
						// actualizam pasii minimi
						low[vertex] = Math.min(low[vertex], low[adjVertex]);
						
						if (low[adjVertex] > idx[vertex]) {
							/*
							 *  nu se poate ajunge din adjVertex la un alt nivel
							 *  mai mic sau egal cu nivelul lui vertex
							 *  
							 *  deci, muchia (vertex, adjVertex) este critica
							 */
							
							sol.add(new Edge(vertex, adjVertex));
						}
					} else {
						// actualizam pasii minimi
						
						low[vertex] = Math.min(low[vertex], idx[adjVertex]);
					}
				}
			}
		}

		private List<Edge> getResult() {
			// TODO: Gasiti muchiile critice ale grafului neorientat stocat cu liste
			// de adiacenta in adj.
			
			idx = new int[n + 1];
			low = new int[n + 1];
			
			for (int i = 0; i <= n; i++) {
				// initializare
				idx[i] = -1;
				low[i] = -1;
			}
			
			for (int currVertex = 1; currVertex <= n; currVertex++) {
				if (idx[currVertex] == -1) {
					// nu e definit
					
					// testam acest nod (currVertex va fi radacina arborelui de adancime)
					computeBridges(currVertex, -1);
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
