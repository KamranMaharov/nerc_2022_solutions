import java.io.*;
import java.util.*;

public class Main {
	static class Scanner {
		Scanner(InputStream in) { this.in = in; } InputStream in;
		byte[] bb = new byte[1 << 15]; int i, n;
		byte getc() {
			if (i == n) {
				i = n = 0;
				try { n = in.read(bb); } catch (IOException e) {}
			}
			return i < n ? bb[i++] : 0;
		}
		int nextInt() {
			byte c = 0; while (c <= ' ') c = getc();
			int a = 0; while (c > ' ') { a = a * 10 + c - '0'; c = getc(); }
			return a;
		}
		
		long nextLong() {
			byte c = 0; while (c <= ' ') c = getc();
			long a = 0; while (c > ' ') { a = a * 10 + c - '0'; c = getc(); }
			return a;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		
		int N = in.nextInt();
		int K = in.nextInt();
		
		int[][] records = new int[K+1][N+1];
		int[] Par = new int[N+1];
		for (int i=1; i<=N; i++)
			Par[i] = -1;
		TreeSet<Integer>[] trees = new TreeSet[N+1];
		
		for (int i=1; i<=K; i++) {
			for (int j=1; j<=N; j++) {
				records[i][j] = in.nextInt();
			}
		}
		
		ArrayList<Integer> queue = new ArrayList<>();
		
		for (int j=1; j<=N; j+=2) {
			trees[records[1][j]] = new TreeSet<>(Arrays.asList(records[1][j]));
			queue.add(records[1][j]);
		}
		
		while (!queue.isEmpty()) {
			Integer currentRoot = queue.remove(queue.size()-1);
			TreeSet<Integer> subtree = trees[currentRoot];
			
			int[] counts = new int[N+1];
			
			for (int i=1; i<=K; i++) {
				int start = 0;
				int end = 0;
				for (int j=1; j<=N; j++) {
					if (subtree.contains(records[i][j])) {
						if (start == 0)
							start = j;
						end = j;
					} else if (start != 0) {
						break;
					}
				}
				if (start > 1) {
					counts[records[i][start-1]]++;
				}
				if (end < N) {
					counts[records[i][end+1]]++;
				}
			}
			int parent = 0;
			int max_count = 0;
			for (int i=1; i<=N; i++) {
				if (counts[i] > max_count) {
					max_count = counts[i];
					parent = i;
				}
			}
			
			Par[currentRoot] = parent > 0 ? parent : -1;
			if (trees[parent] == null) {
				trees[parent] = subtree;
				trees[parent].add(parent);
			} else {
				trees[parent].addAll(subtree);
				queue.add(parent);
			}
		}
		
		for (int i=1; i<N; i++)
			out.write(Par[i] + " ");
		out.write(Par[N] + "\n");
		
		out.close();
	}
}
