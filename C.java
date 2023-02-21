import java.io.*;
import java.util.*;

public class Main {
	
	
	static class Scanner {
		Scanner(InputStream in) { this.in = in; } InputStream in;
		byte[] bb = new byte[200000 * 19]; int i, n;
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
	
	
	private static void cactus_to_tree(int v, List<Integer>[] cactus, int[] parent,
					int[] depth, List<Integer>[] tree, boolean[] in_cycle, int[] cycles) {
		//System.out.println(v + "\n");
		for (int w : cactus[v]) {
			if (parent[w] == 0) {
				parent[w] = v;
				depth[w] = depth[v] + 1;
				cactus_to_tree(w, cactus, parent, depth, tree, in_cycle, cycles);
			} else if (w != parent[v] && depth[w] < depth[v]) { //back edge found
				int special = tree[0].get(0) + 1;
				tree[0].set(0, special);
				tree[special] = new ArrayList<>();
				
				int k = v;
				while (k != w) {
					cycles[k] += 1;
					tree[special].add(k);
					tree[k].add(special);
					
					in_cycle[k] = true;
					k = parent[k];
				}
				cycles[k] += 1;
				tree[special].add(k);
				tree[k].add(special);
			}
		}
	}
	
	private static boolean cycled(int v, List<Integer>[] cactus, int[] parent,
			int[] depth, int[] cycles, int cc) {
		
		for (int w : cactus[v]) {
			if (parent[w] == v) {
				if (cycled(w, cactus, parent, depth, cycles, cc))
					return true;
			} else if (w != parent[v] && depth[w] < depth[v]) {
				int k = v;
				int sum_cycles = 0;
				int num_vertices = 0;
				while (k != w) {
					sum_cycles += cycles[k];
					num_vertices++;
					k = parent[k];
				}
				sum_cycles += cycles[k];
				num_vertices++;
				
				if (sum_cycles == cc + num_vertices - 1) // TODO: fix this
					return true;
			}
		}
		return false;
	}
	
	private static void dfs(int v, int N, List<Integer>[] tree,
			int[] parent, boolean[] include, int[] included) {
		if (v > N) {
			int k = parent[v];
			include[k] = true;
			/*while (k > 0) {
				include[k] = true;
				k = parent[k];
			}*/
		}
		
		int sum = 0;
		for (int w : tree[v]) {
			if (parent[w] == 0) {
				parent[w] = v;
				dfs(w, N, tree, parent, include, included);
				sum += included[w];
			}
		}
		included[v] = sum;
		if (include[v]) included[v] += 1;
	}
	
	public static void main(String[] args) throws Exception {
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in), 200000 * 20);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		Scanner in = new Scanner(System.in);
		
		int N = in.nextInt();
		int M = in.nextInt();
		
		while (N != 0) {
			
			List<Integer>[] cactus = new ArrayList[N+1];
			List<Integer>[] tree = new ArrayList[3*N + 1];
			for (int i=1; i<=N; i++) {
				cactus[i] = new ArrayList<Integer>();
				tree[i] = new ArrayList<Integer>();
			}
			tree[0] = new ArrayList<>();
			tree[0].add(N);
			
			for (int i=1; i<=M; i++) {
				int s = in.nextInt();
				int v = in.nextInt();
				for (int j=2; j<=s; j++) {
					int cur = in.nextInt();
					cactus[v].add(cur);
					cactus[cur].add(v);
					v = cur;
				}
			}
			
			int[] parent = new int[N+1];
			parent[1] = -1;
			int[] depth = new int[N+1];
			boolean[] in_cycle = new boolean[N+1];
			int[] cycles = new int[N+1];
			cactus_to_tree(1, cactus, parent, depth, tree, in_cycle, cycles);
			
			
			int back_edges = 0;
			for (int i=1; i<=N; i++) {
				for (int vv : cactus[i]) {
					if (depth[vv] < depth[i] && vv != parent[i]) {
						back_edges++;
					}
				}
			}
			//out.write(back_edges + "\n");
			
			if (cycled(1, cactus, parent, depth, cycles, back_edges)) {
				out.write("Yes\n");
				//out.flush();
				N = in.nextInt();
				M = in.nextInt();
				continue;
			}
			
			for (int i=1; i<=N; i++) {
				if (parent[i] >= 0 && !in_cycle[i]) {
					tree[i].add(parent[i]);
					tree[parent[i]].add(i);
				}
			}
			
			int tsize = tree[0].get(0);
			/*for (int i=1; i<=tsize; i++) {
				out.write(i + "");
				for (int v : tree[i]) {
					out.write(" " + v);
				}
				out.write("\n");
			}*/
			
			parent = new int[3 * N+1];
			parent[1] = -1;
			boolean[] include = new boolean[3*N + 1];
			int[] included = new int[3*N+1];
			dfs(1, N, tree, parent, include, included);
			
			int deg2 = -1;
			
			for (int v=1; v<=tsize; v++) {
				int forward = 0;
				for (int w : tree[v]) {
					if (w == parent[v])
						continue;
					if (included[w] > 0)
						forward++;
				}
				//out.write(v + " " + forward + " " + include[v] + "\n");
				if (forward > 2)
					deg2 = -2;
				else if (forward == 2) {
					if (deg2 == -1)
						deg2 = v;
					else
						deg2 = -2;
				}
			}
			
			if (deg2 == -1 || deg2 == 1) {
				out.write("Yes\n");
			} else if (deg2 == -2){
				out.write("No\n");
			} else {
				int count = 0;
				int k = parent[deg2];
				while (k != -1) {
					for (int w : tree[k]) {
						if (w == parent[k])
							continue;
						if (w > N)
							count++;
					}
					k = parent[k];
				}
				if (deg2 > N) count--;
				if (parent[deg2] > N) count--;
				if (count == 0) {
					out.write("Yes\n");
				} else {
					out.write("No\n");
				}
			}
			
			//out.flush();
			N = in.nextInt();
			M = in.nextInt();
		}
		
		
		//in.close();
		out.close();
	}
	
	
}
