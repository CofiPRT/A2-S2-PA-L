import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.List;

public class Main {
	static class Task {
		public static final String INPUT_FILE = "in";
		public static final String OUTPUT_FILE = "out";
		public static final int NMAX = 100005; // 10^5

		int n;
		int m;

		@SuppressWarnings("unchecked")
		ArrayList<Integer> adj[] = new ArrayList[NMAX];

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
				}
				sc.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(ArrayList<Integer> result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));

				for (int i = 0; i < result.size(); i++) {
					pw.printf("%d ", result.get(i));
				}
				pw.printf("\n");
				pw.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private ArrayList<Integer> getResult() {
			// TODO: Faceti sortarea topologica a grafului stocat cu liste de
			// adiacenta in adj.
			// ***
			// ATENTIE: nodurile sunt indexate de la 1 la n.
			// ***
            
			ArrayList<Integer> topsort = new ArrayList<>();
            Stack<Integer> stack = new Stack<>();
            
            int[] headEnds = new int[n + 1]; // headEnds[i] = nr de in-muchii pt nodul i
            for (int i = 0; i <= n; i++) {
                headEnds[i] = 0;
            }
            
            for (int currNode = 1; currNode <= n; currNode++) {
                for (Integer adjNode : adj[currNode]) {
                    headEnds[adjNode]++;
                }
            }
            
            for (int currNode = 1; currNode <= n; currNode++) {
                if (headEnds[currNode] == 0) {
                    // nu are in-muchii, initializam stiva
                    stack.push(currNode);
                }
            }
            
            while (!stack.isEmpty()) {
                int currNode = stack.pop();
                topsort.add(currNode);
                
                for (Integer adjNode : adj[currNode]) {
                    if (--headEnds[adjNode] == 0) {
                        // nu mai are in-muchii
                        stack.push(adjNode);
                    }
                }
            }
            
			return topsort;
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