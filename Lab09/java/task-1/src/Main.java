import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Main {
	static class Task {
		public static final String INPUT_FILE = "in";
		public static final String OUTPUT_FILE = "out";
		public static final int NMAX = 50005;
		
		public static final boolean BONUS = false;
		int[] parents;
		public static final String BONUS_FILE = "bonus";

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
				// pentru sortarea din priority queue
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

				for (int i = 1; i <= n; i++) {
					adj[i] = new ArrayList<>();
				}
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

		@SuppressWarnings("unused")
		private void writeOutput(List<Integer> result) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(
								OUTPUT_FILE));
				StringBuilder sb = new StringBuilder();
				
				// BONUS
				BufferedWriter bonus_bw = null;
				StringBuilder[] bonus_sb; // DP pentru printare
				
				if (BONUS) {
					bonus_bw = new BufferedWriter(new FileWriter(BONUS_FILE));
					bonus_sb = new StringBuilder[n + 1];
					
					for (int i = 0; i <= n; i++) {
						bonus_sb[i] = new StringBuilder();
					}
				}
				
				
				for (int i = 1; i <= n; i++) {					
					sb.append(result.get(i)).append(' ');
					
					if (BONUS && i != source) {
						bonus_bw.write(source + "->" + i + ": ");
						
						Stack<Integer> stack = new Stack<>();
						
						String noPath = "Nu exista drum!\n";
						
						int currNode = i;
						boolean hasPath = false;
						
						while (currNode != -1) {
							if (currNode != i) {
								hasPath = true;
							}
							
							if (bonus_sb[currNode].length() != 0) {
								// deja stim drumul pana in acest nod
								bonus_sb[i].append(bonus_sb[currNode]);
								break;
							}
							
							stack.push(currNode);
							
							currNode = parents[currNode];
						}
						
						if (!hasPath) {
							// nu exista drum catre acest nod
							bonus_sb[i].append(noPath);
						}
						
						if (bonus_sb[i].toString().equals(noPath)) {
							// nu exista drum, fie stabilit mai sus fie in while
							bonus_bw.write(noPath);
							continue;
						}
						
						while (!stack.isEmpty()) {
							int poppedNode = stack.pop();
							
							bonus_sb[i].append(poppedNode + " ");
							
							if (bonus_sb[poppedNode].length() == 0) {
								// teoretic, acest nod deja s-a prelucrat
								bonus_sb[poppedNode].append(bonus_sb[i]);
							}
						}
						
						bonus_bw.write(bonus_sb[i].toString());
						bonus_bw.write("\n");
					}
				}
				sb.append('\n');
				
				bw.write(sb.toString());
				bw.close();
				
				if (BONUS) {
					bonus_bw.close();
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private List<Integer> getResult() {
			// TODO: Gasiti distantele minime de la nodul source la celelalte noduri
			// folosind Dijkstra pe graful orientat cu n noduri, m arce stocat in adj.
			//	d[node] = costul minim / lungimea minima a unui drum de la source la
			//	nodul node;
			//	d[source] = 0;
			//	d[node] = -1, daca nu se poate ajunge de la source la node.
			// Atentie:
			// O muchie este tinuta ca o pereche (nod adiacent, cost muchie):
			//	adj[x].get(i).node = nodul adiacent lui x,
			//	adj[x].get(i).cost = costul.
			
			Integer[] dists = new Integer[n + 1]; // wrapper Integer pentru Arrays.asList de la final
			boolean[] visited = new boolean[n + 1];
			
			if (BONUS) {
				parents = new int[n + 1];
				parents[0] = -1;
			}
			
			// initializare
			dists[0] = 0;
			visited[0] = false;
			
			for (int i = 1; i <= n; i++) {
				dists[i] = -1;
				visited[i] = false;
				
				if (BONUS) {
					parents[i] = -1;
				}
			}
			dists[source] = 0;
			
			Queue<Edge> queue = new PriorityQueue<>(1, Comparator.comparing(Edge::getCost));
			// adaugam sursa
			queue.add(new Edge(source, dists[source]));
				
			while (!queue.isEmpty()) {
				Edge currEdge = queue.poll();
				int currNode = currEdge.node;
				
				if (visited[currNode]) {
					// am vizitat deja acest nod
					continue;
				}
				
				visited[currNode] = true; // prelucram acest nod
				
				for (Edge adjEdge : adj[currNode]) {
					// pentru fiecare nod adiacent
					int adjNode = adjEdge.node;
					int adjCost = adjEdge.cost;
					
					boolean betterPath = (dists[adjNode] == -1 || // nevizitat
										dists[adjNode] > dists[currNode] + adjCost); // drum mai bun
					
					if (visited[adjNode] == false && betterPath) {
						// nu am prelucrat nodul adiacent iar drumul prin el este mai mic
						
						dists[adjNode] = dists[currNode] + adjCost;
						
						if (BONUS) {
							parents[adjNode] = currNode;
						}
						
						queue.add(new Edge(adjNode, dists[adjNode]));
					}
				}
			}

      		return Arrays.asList(dists);
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
