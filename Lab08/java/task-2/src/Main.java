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
		
		List<Integer> sol = new ArrayList<>();

		@SuppressWarnings("unchecked")
		List<Integer> adj[] = new ArrayList[NMAX];

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

		private void writeOutput(List<Integer> result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				for (int node : result) {
					pw.printf("%d ", node);
				}
				pw.printf("\n");
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void computeCutVertices(int vertex, int parent) {
			idx[vertex] = time;
			low[vertex] = time;
			
			// timpul de descoperire
			time++;
			
			List<Integer> children = new ArrayList<>();
			
			for (int adjVertex : adj[vertex]) {
				if (adjVertex != parent) {
					// vertex NU este un copil de-al lui adjVertex
					
					if (idx[adjVertex] == -1) {
						// nu e definit, deci adjVertex nu a fost procesat inca
						
						// adjVertex este un copil de-al lui vertex
						children.add(adjVertex);
						
						// parcurgem in adancime, continuand cu adjVertex
						computeCutVertices(adjVertex, vertex);
						
						// actualizam pasii minimi
						low[vertex] = Math.min(low[vertex], low[adjVertex]);
					} else {
						// adjVertex este in curs de procesare
						
						low[vertex] = Math.min(low[vertex], idx[adjVertex]);
					}
				}
			}
			
			if (parent == -1) {
				/*
				 *  if-uri separate intrucat ramura else de mai jos trebuie sa
				 *  corespunda DOAR cu acesta
				 */
				
				if (children.size() >= 2) {
					/*
					 *  vertex este radacina acestui arbore de adancime si
					 *  are cel putin 2 copii
					 *  
					 *  deci, este punct de articulatie
					 */
					sol.add(vertex);
				}
				
			} else {
				for (int child : children) {
					if (low[child] >= idx[vertex]) {
						/*
						 *  exista (cel putin) un copil child cu proprietatea ca
						 *  niciun copil de-al lui nu poate ajunge la vertex prin
						 *  alta cale
						 *  
						 *  deci, vertex este punct de articulatie
						 */
						
						sol.add(vertex);
						break;
					}
				}
			}
		}

		private List<Integer> getResult() {
			// TODO: Gasiti nodurile critice ale grafului neorientat stocat cu liste
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
					computeCutVertices(currVertex, -1);
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
