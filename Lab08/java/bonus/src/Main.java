import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

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
		
		Stack<Edge> stack = new Stack<>();
		
		List<List<Edge>> sol = new ArrayList<>(); // solutie de MUCHII
		List<List<Integer>> vSol = new ArrayList<>(); // solutie de VARFURI (va fi printata)
		
		class Edge {
			int x;
			int y;
			
			Edge(int x, int y) {
				this.x = x;
				this.y = y;
			}
		}

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

		private void writeOutput(List<List<Integer>> result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				
				pw.printf(result.size() + "\n");
				
				for (List<Integer> component : result) {
					for (int vertex : component) {
						pw.printf(vertex + " ");
					}
					
					pw.printf("\n");
				}
				
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		private void addVertexSolution(List<Edge> component) {
			// converteste o solutie de muchii in varfuri
			Set<Integer> newSolution = new HashSet<>();
			
			for (Edge edge : component) {
				newSolution.add(edge.x);
				newSolution.add(edge.y);
			}
			
			if (newSolution.size() != 0) {
				// nu exista componente biconexe goale
				
				vSol.add(new ArrayList<>(newSolution));
			}
		}

		private void computeCutVertices(int vertex, int parent) {
			idx[vertex] = time;
			low[vertex] = time;
			
			// timpul de descoperire
			time++;
			
			List<Integer> children = new ArrayList<>();
			
			for (int adjVertex : adj[vertex]) {

					
				if (idx[adjVertex] == -1) {
					// nu e definit, deci adjVertex nu a fost procesat inca
					
					// adjVertex este un copil de-al lui vertex
					children.add(adjVertex);
					
					// salvare in stiva
					stack.push(new Edge(vertex, adjVertex));
					
					// parcurgem in adancime, continuand cu adjVertex
					computeCutVertices(adjVertex, vertex);
					
					// actualizam pasii minimi
					low[vertex] = Math.min(low[vertex], low[adjVertex]);
					
					boolean isRoot = (parent == -1);
					boolean has2Children = (children.size() >= 2);
					boolean hasChildProperty = (low[adjVertex] >= idx[vertex]);
						
					if ((isRoot && has2Children) || (!isRoot && hasChildProperty)) {
						// vertex este punct critic
						
						List<Edge> newComponent = new ArrayList<>();
						
						Edge currEdge = null;
						
						// adaugam toate muchiile pana la (vertex, child) inclusiv
						do {
							currEdge = stack.pop();
							newComponent.add(currEdge);
						} while (currEdge.x != vertex && currEdge.y != adjVertex);
						
						addVertexSolution(newComponent);
					}
					
				} else if (parent != adjVertex && low[vertex] >= idx[adjVertex]) {
					// adjVertex este in curs de procesare, muchie de intoarcere
					
					// salvare in stiva
					stack.push(new Edge(vertex, adjVertex));
					
					low[vertex] = Math.min(low[vertex], idx[adjVertex]);
				}
			}
		}

		private List<List<Integer>> getResult() {
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
				
				// nodul radacina trebuie tratat separat
				List<Edge> component = new ArrayList<>();
				
				while (!stack.isEmpty()) {
					component.add(stack.pop());
				}
				
				addVertexSolution(component);
			}
			
			return vSol;
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
