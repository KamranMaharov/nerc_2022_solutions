import java.io.*;
import java.math.BigInteger;
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
	
	private static boolean within(int a, int l, int r) {
		return l <= a && a <= r;
	}
	
	private static boolean go_match(int b, List<Integer>[] graph,
			int[] matched, boolean[] visited) {
		if (visited[b])
			return false;
		visited[b] = true;
		for (Integer b2 : graph[matched[b]]) {
			if (b2 == b)
				continue;
			if (matched[b2] == 0 || go_match(b2, graph, matched, visited)) {
				matched[b2] = matched[b];
				matched[matched[b]] = b2;
				return true;
			}
		}
		return false;
	}
	
	private static int go_count(int v, int ex, List<Integer>[] graph,
			boolean[] visited, int[] matched) {
		if (visited[v])
			return 0;
		visited[v] = true;
		int count = 1;
		for (Integer w : graph[v]) {
			if (w == ex || matched[v] == w)
				continue;
			count += go_count(matched[w], ex, graph, visited, matched);
		}
		return count;
	}
	
	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		
		String[] line = in.readLine().split(" ");
		int N = Integer.parseInt(line[0]);
		int M = Integer.parseInt(line[1]);
		
		String[] table = new String[N];
		int[][] id = new int[N][M];
		int whites = 0;
		int blacks = 0;
		for (int i=0; i<N; i++) {
			table[i] = in.readLine();
			for (int j=0; j<M; j++) {
				if (table[i].charAt(j) == '#')
					continue;
				if ((i + j) % 2 == 0) {
					whites++;
					id[i][j] = whites;
				} else {
					blacks++;
					id[i][j] = blacks;
				}
			}
		}
		
		// guaranteed that whites == blacks
		long same_color = (1L * whites * (whites - 1)) / 2L +
				(1L * blacks * (blacks - 1)) / 2L;
		if (same_color >= 1000000) {
			out.write("1000000\n");
			in.close();
			out.close();
			return;
		}
		
		List<Integer>[] graph = new ArrayList[whites + blacks + 1];
		for (int i=1; i<=(whites+blacks); i++) {
			graph[i] = new ArrayList<>();
		}
		
		for (int i=0; i<N; i++) {
			for (int j=0; j<M; j++) {
				if (table[i].charAt(j) == '#')
					continue;
				if ((i + j) % 2 == 0) {
					int[] dx = {-1, 0, 1, 0};
					int[] dy = {0, 1, 0, -1};
					for (int k=0; k<4; k++) {
						if (within(i+dx[k], 0, N-1) && within (j+dy[k], 0, M-1) &&
								table[i+dx[k]].charAt(j+dy[k]) != '#') {
							graph[id[i][j]].add(id[i+dx[k]][j+dy[k]]+whites);
							graph[id[i+dx[k]][j+dy[k]]+whites].add(id[i][j]);
						}
					}
				}
			}
		}
		
		// print graph here
		int[] matched = new int[whites + blacks + 1];
		boolean[] visited = new boolean[whites + blacks + 1];
		for (int i=1; i<=whites; i++) {
			for (Integer b : graph[i]) {
				Arrays.fill(visited, false);
				if (matched[b] == 0 || go_match(b, graph, matched, visited)) {
					matched[i] = b;
					matched[b] = i;
					break;
				}
			}
		}
		
		long ans = same_color;
		//out.write(ans + "\n");
		
		for (int i=1; i<=whites; i++) {
			Arrays.fill(visited, false);
			ans += (blacks - go_count(matched[i], i, graph, visited, matched));
		}
		if (ans > 1000000) {
			ans = 1000000;
		}
		out.write(ans + "\n");
		
		in.close();
		out.close();
	}
}
