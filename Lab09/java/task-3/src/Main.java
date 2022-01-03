import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	static class Task {
		public static final String INPUT_FILE = "in";
		public static final String OUTPUT_FILE = "out";
		public static final int NMAX = 105;

		int n;
		int d[][];
		int a[][];

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				d = new int[n + 1][n + 1];
				a = new int[n + 1][n + 1];
				for (int i = 1; i <= n; i++) {
					for (int j = 1; j <= n; j++) {
						a[i][j] = sc.nextInt();
						d[i][j] = a[i][j]; // nu mai trece din nou prin matrice
					}
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput() {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(
								OUTPUT_FILE));
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i <= n; i++) {
					for (int j = 1; j <= n; j++) {
						sb.append(d[i][j]).append(' ');
					}
					sb.append('\n');
				}
				bw.write(sb.toString());
				bw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void compute() {
			// TODO: Gasiti distantele minime intre oricare doua noduri, folosind
			// RoyFloyd pe graful orientat cu n noduri, m arce stocat in matricea
			// ponderilor a (declarata mai sus).

			// Atentie:
			// O muchie (x, y, w) este reprezentata astfel in matricea ponderilor:
			//	a[x][y] = w;
			// Daca nu exista o muchie intre doua noduri x si y, in matricea
			// ponderilor se va afla valoarea 0:
			//	a[x][y] = 0;

			// Trebuie sa populati matricea d[][] (declarata mai sus):
			//	d[x][y] = distanta minima intre nodurile x si y, daca exista drum.
			//	d[x][y] = 0 daca nu exista drum intre x si y.
			//	d[x][x] = 0.
			
			
			
			// initializez d[i][j] cu a[i][j] la readInput() (de ce sa mai trec din nou prin matrice?)
			
			// relaxare de n ori
			for (int k = 1; k <= n; k++) {
				for (int i = 1; i <= n; i++) {
					for (int j = 1; j <= n; j++) {
						if (i != j && d[i][k] != 0 && d[k][j] != 0) {
							int sum = d[i][k] + d[k][j];
							
							if (d[i][j] == 0) {
								d[i][j] = sum;
							} else {
								d[i][j] = Math.min(d[i][j], sum);
							}
						}
					}
				}
			}
		}

		public void solve() {
			readInput();
			compute();
			writeOutput();
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
