import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	static class Task {
		public static final String INPUT_FILE = "in";
		public static final String OUTPUT_FILE = "out";
		public static final int NMAX = 1005;

		int n;
		int m;

		@SuppressWarnings("unchecked")
		ArrayList<Integer> adj[] = new ArrayList[NMAX];
		int C[][];

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();

				C = new int[n + 1][n + 1];
				for (int i = 1; i <= n; i++) {
					adj[i] = new ArrayList<>();
				}
				for (int i = 1; i <= m; i++) {
					int x, y, z;
					x = sc.nextInt();
					y = sc.nextInt();
					z = sc.nextInt();
					adj[x].add(y);
					adj[y].add(x);
					C[x][y] += z;
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				pw.printf("%d\n", result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int getResult() {
			// TODO: Calculati fluxul maxim pe graful orientat dat.
			// Sursa este nodul 1.
			// Destinatia este nodul n.
			//
			// In adj este stocat graful neorientat obtinut dupa ce se elimina orientarea
			// arcelor, iar in C sunt stocate capacitatile arcelor.
			// De exemplu, un arc (x, y) de capacitate z va fi tinut astfel:
			// C[x][y] = z, adj[x] contine y, adj[y] contine x.
			int source = 1;
			int dest = n;
			
			int fmax = 0;
			
			int[][] f = new int[n + 1][n + 1];
						
			while (true) {
				// BFS
				boolean visited[] = new boolean[n + 1]; // deja false
				int parents[] = new int[n + 1];
				
				Queue<Integer> queue = new LinkedList<Integer>();
				
				queue.add(source);
				visited[source] = true;
				parents[source] = -1;
				
				while (!queue.isEmpty()) {
					int currNode = queue.poll();
					
					for (int adjNode : adj[currNode]) {
						// pentru fiecare nod adiacent nevizitat
						
						// calculam reziduul
						int cf = C[currNode][adjNode] - f[currNode][adjNode];
						
						if (visited[adjNode] == false && cf > 0) {
							// daca reziduul este 0 sau mai mic, nu exista cale
							visited[adjNode] = true; // acum e vizitat
							
							queue.add(adjNode);
							parents[adjNode] = currNode; // salvam parintele
						}
					}
				}
				
				if (visited[dest] == false) {
					// nu exista cale de la producator la consumator
					break;
				}
				
				// cautam capacitatea reziduala minima, de la dest la source
				int cf = Integer.MAX_VALUE;
				
				int currNode = dest;
								
				while (currNode != source) {
					int parent = parents[currNode];
					
					// calculam capacitatea reziduala
					int currCapacity = C[parent][currNode];
					int currFlow = f[parent][currNode];
					
					int currResidue = currCapacity - currFlow;
					
					// salvam minimul
					cf = Math.min(cf, currResidue);
					
					// avansam la urmatorul parinte
					currNode = parent;
				}
				
				// adaugam la fluxul maxim
				fmax += cf;
				
				// trecem din nou prin calea de parinti pentru actualizare
				currNode = dest;
								
				while (currNode != source) {
					int parent = parents[currNode];
					
					f[parent][currNode] += cf;
					f[currNode][parent] = -f[parent][currNode]; // antisimetrie
					
					currNode = parent;
				}
			}
			
			return fmax;
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
