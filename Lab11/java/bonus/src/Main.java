import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	static class Edge {
		int x;
		int y;
		
		public Edge(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public String toString() {
			return x + " " + y;
		}
	}
	
	static class Task {
		public static final String INPUT_FILE = "in";
		public static final String OUTPUT_FILE = "out";

		int n;
		int C[][]; // mega-storage de capacitati, pentru ambele cai (in and out)
				
		List<Edge> edges = new ArrayList<>();

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
								
				C = new int[2 * (n + 1)][2 * (n + 1)];
				
				for (int currentCity = 1; currentCity <= n; currentCity++) {
					int out_count = sc.nextInt();
					int in_count = sc.nextInt();
					
					// primul rand si ultima coloana vor fi folosite pentru
					// initializare
					C[0][currentCity] = out_count;
					C[n + currentCity][2*n + 1] = in_count;
				}
				
				for (int currentCity = 1; currentCity <= n; currentCity++) {
					for (int otherCity = 1; otherCity <= n; otherCity++) {
						if (currentCity != otherCity) {
							// in dreapta sus avem capacitatile initiale
							// pentru drumurile care ies
							C[currentCity][n + otherCity] = 1;
							
							// in stanga jos avem capacitatile initiale
							// pentru drumurile care intra
							C[n + currentCity][otherCity] = 0;
						}
					}
				}

				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput() {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				
				pw.write(edges.size() + "\n");
				
				for (Edge edge : edges) {
					pw.write(edge.toString() + "\n");
				}
				
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void getResult() {
			int source = 0;
			int dest = 2*n + 1;
			
			while (true) {
				// pt BFS
				Queue<Integer> queue = new LinkedList<>();
				
				int[] parents = new int[dest + 1];
				
				// initializare
				for (int i = 0; i <= dest; i++) {
					parents[i] = -1;
				}
				
				// BFS
				parents[source] = 0;
				queue.add(source);
				
				while (!queue.isEmpty()) {
					int currNode = queue.poll();
					
					if (currNode == dest) {
						// extragem pana cand ramane coada goala
						continue;
					}
					
					for (int adjNode = 1; adjNode <= dest; adjNode++) {
						boolean notVisited = parents[adjNode] == -1;
						boolean hasCapacity = C[currNode][adjNode] != 0;
						
						if (notVisited && hasCapacity) {
							parents[adjNode] = currNode;
							queue.add(adjNode);
						}
					}
				}
				
				if (parents[dest] == -1) {
					// nu exista path
					break;
				}
				
				// pentru fiecare oras
				for (int currNode = n + 1; currNode <= 2*n; currNode++) {
					boolean notVisited = parents[currNode] == -1;
					boolean hasCapacity = C[currNode][dest] != 0;
					
					if (notVisited || !hasCapacity) {
						// nu are rost sa procesam acest nod
						continue;
					}
					
					parents[dest] = currNode;
					
					// cautam capacitatea reziduala minima
					int cf = Integer.MAX_VALUE;
					
					int adjNode = dest;
					while (adjNode != 0) {
						int parent = parents[adjNode];
						
						cf = Math.min(cf, C[parent][adjNode]);
						
						// avansam
						adjNode = parent;
					}
					
					if (cf == 0) {
						// nu are rost sa mai modificam ceva
						continue;
					}
					
					// actualizam fluxul
					adjNode = dest;
					while (adjNode != 0) {
						int parent = parents[adjNode];
						
						// fluxul pastreaza sensul capacitatii
						int sign = C[parent][adjNode] > 0 ? 1 : -1;
						C[adjNode][parent] += sign * cf;
						
						 // antisimetrie
						C[parent][adjNode] -= sign * cf;
						
						adjNode = parent;
					}
				}
			}
			
			// salvam solutiile
			for (int currNode = 1; currNode <= n; currNode++) {
				for (int adjNode = 1; adjNode <= n; adjNode++) {
					boolean isDifferent = currNode != adjNode; // nu are sens
					boolean hasCapacity = C[currNode][n + adjNode] != 0;
					
					if (isDifferent && !hasCapacity) {
						edges.add(new Edge(currNode, adjNode));
					}
				}
			}
		}

		public void solve() {
			readInput();
			getResult();
			writeOutput();
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
