import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	static class Task {
		public static final String INPUT_FILE = "in";
		public static final String OUTPUT_FILE = "out";
		public static final int NMAX = 50005;
		
		public static final boolean USE_QUEUE = true; // pentru varianta mai performanta

		int n;
		int m;
		int source;

		public class Edge {
			public int node;
			public int cost;

			Edge(int _node, int _cost) {
				node = _node;
				cost = _cost;
			}
			
			public int getCost() {
				return cost;
			}
		}

		@SuppressWarnings("unchecked")
		List<Edge> adj[] = new ArrayList[NMAX];

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();
				source = sc.nextInt();

				for (int i = 1; i <= n; i++)
					adj[i] = new ArrayList<>();
				for (int i = 1; i <= m; i++) {
					int x, y, w;
					x = sc.nextInt();
					y = sc.nextInt();
					w = sc.nextInt();
					adj[x].add(new Edge(y, w));
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(List<Integer> result) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(
								OUTPUT_FILE));
				StringBuilder sb = new StringBuilder();
				if (result.size() == 0) {
					sb.append("Ciclu negativ!\n");
				} else {
					for (int i = 1; i <= n; i++) {
						sb.append(result.get(i)).append(' ');
					}
					sb.append('\n');
				}
				bw.write(sb.toString());
				bw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		private List<Integer> getResultQueue() {
			Integer[] dists = new Integer[n + 1];
			
			Queue<Integer> queue = new LinkedList<>();
			boolean[] inQueue = new boolean[n + 1]; // daca i este in queue, inQueue[i] = true;
			int[] timesInQueue = new int[n + 1]; // de cate ori nodul i a fost adaugat in queue
			
			boolean hasNegCycle = false;
			
			// initializare
			dists[0] = 0;
			inQueue[0] = false;
			timesInQueue[0] = 0;
			
			for (int i = 1; i <= n; i++) {
				dists[i] = -1;
				inQueue[i] = false;
				timesInQueue[i] = 0;
			}
			
			dists[source] = 0;
			
			// adaugam sursa in queue
			queue.add(source);
			inQueue[source] = true;
			timesInQueue[source]++;
			
			while (!queue.isEmpty() && !hasNegCycle) {
				// cat timp mai sunt noduri de prelucrat si nu exista un ciclu negativ
				
				int currNode = queue.poll();
								
				inQueue[currNode] = false; // nu mai este in queue
				
				for (Edge adjEdge : adj[currNode]) {
					int adjNode = adjEdge.node;
					int adjCost = adjEdge.cost;
										
					if (dists[adjNode] == -1 || dists[adjNode] > dists[currNode] + adjCost) {
						// daca nu exista drum, nu ne intereseaza acest nod
						// totodata, costul trebuie sa poata fi relaxat
						
						// relaxam costul
						dists[adjNode] = dists[currNode] + adjCost;
						
						if (!inQueue[adjNode]) {
							// daca adjNode NU este in coada, dorim sa-l adaugam
							
							if (timesInQueue[adjNode] > n) {
								// acest nod se tot readauga in queue datorita unui ciclu negativ
								hasNegCycle = true;
							} else {
								// inca nu exista un ciclu negativ
								queue.add(adjNode);
								inQueue[adjNode] = true; // acum este in queue
								timesInQueue[adjNode]++; // inca o data
							}
						}

					}
				}
			}
			
			if (hasNegCycle) {
				// cel putin un ciclu negativ exista
				return new ArrayList<>();
			}
			
			// daca ajuns aici, nu exista un ciclu negativ
			return Arrays.asList(dists);
		}

		private List<Integer> getResult() {
			// TODO: Gasiti distantele minime de la nodul source la celelalte noduri
			// folosind BellmanFord pe graful orientat cu n noduri, m arce stocat in
			// adj.
			//	d[node] = costul minim / lungimea minima a unui drum de la source la
			//	nodul node;
			//	d[source] = 0;
			//	d[node] = -1, daca nu se poate ajunge de la source la node.

			// Atentie:
			// O muchie este tinuta ca o pereche (nod adiacent, cost muchie):
			//	adj[x].get(i).node = nodul adiacent lui x,
			//	adj[x].get(i).cost = costul.

			// In cazul in care exista ciclu de cost negativ, returnati un vector gol:
			//	return new ArrayList<Integer>();
			
			Integer[] dists = new Integer[n + 1];
			
			// initializare
			dists[0] = 0;
			
			for (int i = 1; i <= n; i++) {
				dists[i] = -1;
			}
			
			dists[source] = 0;
			
			
			// prima relaxare (initializare drumurilor din sursa)
			for (Edge adjEdge : adj[source]) {
				// pentru fiecare muchie a sursei
				int adjNode = adjEdge.node;
				int adjCost = adjEdge.cost;
				
				dists[adjNode] = adjCost;
			}
			
			// restul relaxarilor
			for (int i = 1; i <= n - 2; i++) {
				// n - 2 relaxari
				
				for (int currNode = 1; currNode <= n; currNode++) {
					// pentru fiecare nod
					
					for (Edge adjEdge : adj[currNode]) {
						// pentru fiecare muchie a acestui nod
						int adjNode = adjEdge.node;
						int adjCost = adjEdge.cost;
						
						if (dists[adjNode] == -1 || dists[adjNode] > dists[currNode] + adjCost) {
							// daca nu s-a initializat distanta prin adjNode sau drumul prin el este mai scurt
							
							dists[adjNode] = dists[currNode] + adjCost;
						}
					}
				}
			}
			
			for (int currNode = 1; currNode <= n; currNode++) {
				// pentru fiecare nod
				
				for (Edge adjEdge : adj[currNode]) {
					// pentru fiecare muchie a acestui nod
					int adjNode = adjEdge.node;
					int adjCost = adjEdge.cost;
					
					if (dists[adjNode] == -1 || dists[adjNode] > dists[currNode] + adjCost) {
						// se mai pot relaxa muchii, deci exista cel putin un ciclu negativ
						
						return new ArrayList<>();
					}
				}
			}
				
			// daca a ajuns aici, nu exista cicluri negative
			return Arrays.asList(dists);
		}

		public void solve() {
			readInput();
			writeOutput(USE_QUEUE ? getResultQueue() : getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
