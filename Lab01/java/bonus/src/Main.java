import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
 
public class Main {
	static class Task {
		public final static String INPUT_FILE = "in";
		public final static String OUTPUT_FILE = "out";
 
		int n;
		int[] v;
 
		private void readInput() {
			try {
				Scanner sc = new Scanner(new File(INPUT_FILE));
				n = sc.nextInt();
				v = new int[n];
				for (int i = 0; i < n; i++) {
					v[i] = sc.nextInt();
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
 
		private void writeOutput(int count) {
			try {
				PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
				pw.printf("%d\n", count);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public int countMergeArrays(int[] array, int left, int right) {
			int count = 0;

			int middle = (left + right) / 2 + 1;

			int leftIndex = left, rightIndex = middle, tempIndex = left;

			int[] tempV = new int[n];

			while (leftIndex < middle && rightIndex <= right) {
				if (array[leftIndex] <= array[rightIndex]) {
					tempV[tempIndex++] = array[leftIndex++];
				} else {
					tempV[tempIndex++] = array[rightIndex++];
					count += middle - leftIndex;
				}
			}

			while (leftIndex < middle) {
				tempV[tempIndex++] = array[leftIndex++];
			}

			while (rightIndex <= right) {
				tempV[tempIndex++] = array[rightIndex++];
			}

			for (int i = left; i <= right; i++) {
				array[i] = tempV[i];
			}

			return count;
		}

		public int countMergeSort(int[] array, int left, int right) {
			int count = 0;

			if (left < right) {
				int middle = (left + right) / 2;

				count += countMergeSort(array, left, middle) +
						countMergeSort(array, middle + 1, right) +
						countMergeArrays(array, left, right);

				System.out.println("COUNT: " + count);
			}

			return count;
		}
 	
 		public int countInvs() {
 			int[] tempV = new int[n];

 			for (int i = 0; i < n; i++) {
 				tempV[i] = v[i];
 			}

 			return countMergeSort(tempV, 0, n - 1);
 		}
 
		public void solve() {
			readInput();
			writeOutput(countInvs());
		}
	}
 
	public static void main(String[] args) {
		new Task().solve();
	}
}