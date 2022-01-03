import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    static class Task {
        public static final String INPUT_FILE = "in";
        public static final String OUTPUT_FILE = "out";
        public static final int NMAX = 200005;

        int n;
        int m;

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
        ArrayList<Edge> adj[] = new ArrayList[NMAX];

        private void readInput() {
            try {
                Scanner sc = new Scanner(new BufferedReader(new FileReader(
                                INPUT_FILE)));
                n = sc.nextInt();
                m = sc.nextInt();

                for (int i = 1; i <= n; i++)
                    adj[i] = new ArrayList<>();
                for (int i = 1; i <= m; i++) {
                    int x, y, w;
                    x = sc.nextInt();
                    y = sc.nextInt();
                    w = sc.nextInt();
                    adj[x].add(new Edge(y, w));
                    adj[y].add(new Edge(x, w));
                }
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeOutput(int result) {
            try {
                PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
                pw.printf("%d\n", result);
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private int getResult() {
            /*
            TODO: Calculati costul minim al unui arbore de acoperire
            folosind algoritmul lui Prim.
            */
        	
        	int totalCost = 0;
        	
        	int root = 1; // alegere arbitrara
        	
        	int[] dists = new int[n + 1];
        	int[] parents = new int[n + 1];
        	boolean[] visited = new boolean[n + 1];
        	
        	PriorityQueue<Edge> queue = new PriorityQueue<>(1, Comparator.comparing(Edge::getCost));
        	
        	// initializare
        	for (int i = 0; i <= n; i++) {
        		if (i == root) {
        			dists[i] = 0;
        		} else {
        			dists[i] = Integer.MAX_VALUE;
        		}
        		
        		parents[i] = -1;
        		visited[i] = false;
        		
        		if (i > 0) {
        			queue.add(new Edge(i, dists[i]));
        		}
        	}
        	
        	while (!queue.isEmpty()) {
        		Edge closest = queue.poll();
        		int closestNode = closest.node;
        		
        		if (visited[closestNode]) {
        			continue;
        		}
        		
        		// verificam acest nod
        		visited[closestNode] = true;
        		
        		totalCost += dists[closestNode]; // adaugam la AMA
        		
        		for (Edge adj : adj[closestNode]) {
        			int adjNode = adj.node;
        			int adjCost = adj.cost;
        			
        			if (adjCost < dists[adjNode]) {
        				// trebuie modificat
        				dists[adjNode] = adjCost;
        				parents[adjNode] = closestNode;
        				
        				// adaugam la queue
        				queue.add(new Edge(adjNode, dists[adjNode]));
        			}
        		}
        		
        	}
        	
            return totalCost;
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
