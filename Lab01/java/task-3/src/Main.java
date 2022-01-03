import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	static class Task {
		public final static String INPUT_FILE = "in";
		public final static String OUTPUT_FILE = "out";

		int n, x, y;

		private void readInput() {
			try {
				Scanner sc = new Scanner(new File(INPUT_FILE));
				n = sc.nextInt();
				x = sc.nextInt();
				y = sc.nextInt();
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int answer) {
			try {
				PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
				pw.printf("%d\n", answer);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int getAnswer(int n, int x, int y) {
			// TODO: Calculati valoarea de pe pozitia (x, y) din matricea de dimensiune
			// 2^N * 2^N.

			if (n == 0) {
				return 1;
			}

			int aux = (int) Math.pow(2, n - 1);
			int addition;

			if (x <= aux && y <= aux) {
				// primul cadran
				addition = 0;
			} else if (x <= aux && y > aux) {
				// al doilea cadran
				y -= aux;
				addition = aux*aux;
			} else if (x > aux && y <= aux) {
				// al treilea cadran
				x -= aux;
				addition = 2*aux*aux;
			} else {
				// al patrulea cadran
				x -= aux;
				y -= aux;
				addition = 3*aux*aux;
			}

			return addition + getAnswer(n - 1, x, y);
		}

		public void solve() {
			readInput();
			writeOutput(getAnswer(n, x, y));
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
