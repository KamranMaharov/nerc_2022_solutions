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
	
	
	private static class Edge {
		int u, v, x;
		Edge(int u, int v, int x) {
			this.u = u;
			this.v = v;
			this.x = x;
		}
		
		int other(int a) {
			if (a == u) return v;
			else return u;
		}
	}
	
	// parent[] - parent edge for each vertex, since there can be multiple edges
	private static void dfs(int v, List<Integer>[] graph, Edge[] edges,
			int[] parent, int[] depth, int[] hg, int[] back_edge) {
		
		for (Integer ei : graph[v]) {
			if (parent[v] == ei)
				continue;
			
			int w = edges[ei].other(v);
			if (parent[w] == 0) {
				parent[w] = ei;
				depth[w] = depth[v] + 1;
				dfs(w, graph, edges, parent, depth, hg, back_edge);
				continue;
			}
			
			if (depth[w] < depth[v]) {
				int k = v;
				while (edges[parent[k]].other(k) != w) {
					k = edges[parent[k]].other(k);
				}
				int higher = parent[k];
				
				int higher_cost = depth[edges[higher].u] + depth[edges[higher].v];
				
				if (hg[ei] == 0 || depth[edges[hg[ei]].u] + depth[edges[hg[ei]].v] > higher_cost) {
					hg[ei] = higher;
				}
				
				k = v;
				while (edges[parent[k]].other(k) != w) {
					int ee = parent[k];
					if (hg[ee] == 0 || depth[edges[hg[ee]].u] + depth[edges[hg[ee]].v] > higher_cost) {
						hg[ee] = higher;
						back_edge[ee] = ei;
					}
					k = edges[parent[k]].other(k);
				}
			}
		}
		
	}
	
	
	private static int find_h(int v, int[] higher) {
		if (higher[v] == 0) return v;
		return find_h(higher[v], higher);
	}
	
	public static void main(String[] args) throws Exception {
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in), 200000 * 20);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		Scanner in = new Scanner(System.in);
		
		int N = in.nextInt();
		int M = in.nextInt();
		int P = in.nextInt();
		
		Edge[] edges = new Edge[M + 1];
		List<Integer>[] graph = new ArrayList[N+1];
		
		for (int i=1; i<=M; i++) {
			Edge e = new Edge(in.nextInt(), in.nextInt(), in.nextInt());
			edges[i] = e;
			if (graph[e.u] == null) {
				graph[e.u] = new ArrayList<Integer>();
			}
			if (graph[e.v] == null) {
				graph[e.v] = new ArrayList<Integer>();
			}
			graph[e.u].add(i);
			graph[e.v].add(i);
		}
		
		int[] parent = new int[N+1];
		parent[1] = -1;
		int[] depth = new int[N+1];
		int[] higher = new int[M+1];
		int[] back_edge = new int[M+1];
		dfs(1, graph, edges, parent, depth, higher, back_edge);
		
		for (int i=1; i<=N; i++) {
			//out.write(i + " " + parent[i] + "\n");
		}
		
		// check that higher[i] == 0 indicates separate biconnected component
		for (int i=1; i<=M; i++) {
			//out.write(i + " " + higher[i] + "\n");
			//out.write(i + " " + back_edge[i] + "\n");
			//out.write("\n");
		}
		
		int sum = -1;
		
		for (int i=1; i<=M; i++) {
			if (higher[i] == 0) { // new biconnected component found
				int total = 0;
				Set<Integer> vertices = new HashSet<>();
				for (int j=1; j<=M; j++) {
					if (find_h(j, higher) == i) {
						total = (total + edges[j].x) % P;
						vertices.add(edges[j].u);
						vertices.add(edges[j].v);
					}
				}
				
				
				if ((vertices.size() - 1) % P == 0) {
					if (total == 0) {
						// S can be anything
					} else {
						// no S is suitable
						sum = -2;
					}
				} else if (sum != -2) {
					int S = (int)(1l * total * pow(vertices.size() - 1, P-2, P) % P);
					
					if (sum == -1) {
						sum = S;
					} else if (sum != S) {
						sum = -2;
					}
				}
				
				
				/*int S = (int)(1l * total * pow(vertices.size() - 1, P-2, P) % P);
				
				if (sum == -1) {
					sum = S;
				} else if (sum != S) {
					sum = -2;
				}*/
				
				//out.write(i + " " + total + " " + S + "\n");
			}
		}
		
		if (sum == -2) {
			out.write("-1\n");
			out.close();
			return;
		}
		if (sum == -1) {
			sum = 0;
		}
		
		List<String> ops = new ArrayList<>();
		
		if (sum != 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(sum + "");
			//out.write((P-sum) % P + "");
			for (int i=1; i<=N; i++) {
				if (parent[i] >= 0) {
					sb.append(" " + parent[i]);
					//out.write(" " + parent[i]);
					edges[parent[i]].x = (edges[parent[i]].x + P - sum) % P;
				}
			}
			ops.add(sb.toString());
			//out.write("\n");
			//out.flush();
		}
		//out.write("HERE\n");
		
		Set<Integer> sourced = new HashSet<>();
		Set<Integer> tree_edges = new HashSet<>();
		for (int i=1; i<=N; i++) {
			if (parent[i] >= 0) {
				tree_edges.add(parent[i]);
			}
		}
		
		while (true) {
			
			int source = 0;
			
			for (int i=1; i<=M; i++) {
				if (edges[i].x != 0 && !tree_edges.contains(i)) {
					source = i;
				}
			}
			
			if (source == 0) {
				int max_depth = -1;
				for (int i=1; i<=M; i++) {
					if (edges[i].x != 0 && higher[i] != 0) {
						if (depth[edges[i].u] + depth[edges[i].v] > max_depth) {
							max_depth = depth[edges[i].u] + depth[edges[i].v];
							source = i;
						}
					}
				}
				if (max_depth < 0) break;
			}
			
			
			if (sourced.contains(source))
				throw new Exception("come on");
			sourced.add(source);
			
			int a = edges[source].u;
			int b = edges[source].v;
			if (depth[a] < depth[b]) {
				int tmp = a;
				a = b;
				b = tmp;
			}
			if (parent[a] != source) {
				StringBuilder sb = new StringBuilder();
				sb.append(edges[source].x % P + "");
				//out.write((P - edges[source].x) % P + "");
				for (int i=1; i<=N; i++) {
					if (parent[i] >= 0 && parent[i] != higher[source]) {
						sb.append(" " + parent[i]);
						//out.write(" " + parent[i]);
					}
				}
				sb.append(" " + source);
				ops.add(sb.toString());
				//out.write(" " + source);
				//out.write("\n");
				//out.flush();
				
				sb = new StringBuilder();
				sb.append((P - edges[source].x) % P + "");
				//out.write(edges[source].x % P + "");
				for (int i=1; i<=N; i++) {
					if (parent[i] >= 0) {
						sb.append(" " + parent[i]);
						//out.write(" " + parent[i]);
					}
				}
				ops.add(sb.toString());
				//out.write("\n");
				//out.flush();
				edges[higher[source]].x = (edges[higher[source]].x + edges[source].x) % P;
				edges[source].x = 0;
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(edges[source].x % P + "");
				//out.write((P - edges[source].x) % P + "");
				for (int i=1; i<=N; i++) {
					if (parent[i] >= 0 && parent[i] != higher[source]) {
						sb.append(" " + parent[i]);
						//out.write(" " + parent[i]);
					}
				}
				sb.append(" " + back_edge[source]);
				ops.add(sb.toString());
				//out.write(" " + back_edge[source]);
				//out.write("\n");
				//out.flush();

				sb = new StringBuilder();
				sb.append((P - edges[source].x) % P + "");
				//out.write(edges[source].x % P + "");
				for (int i=1; i<=N; i++) {
					if (parent[i] >= 0 && parent[i] != source) {
						sb.append(" " + parent[i]);
						//out.write(" " + parent[i]);
					}
				}
				sb.append(" " + back_edge[source]);
				ops.add(sb.toString());
				//out.write(" " + back_edge[source]);
				//out.write("\n");
				//out.flush();
				edges[higher[source]].x = (edges[higher[source]].x + edges[source].x) % P;
				edges[source].x = 0;
			}
			
		}
		
		out.write(ops.size() + "\n");
		for(String op : ops) {
			out.write(op + "\n");
		}
		//in.close();
		out.close();
	}
	
	static long pow(long a, long b, long p) {
		if (b == 0) return 1;
		if (b % 2 == 0) {
			long t = pow(a, b / 2, p);
			return t * t % p;
		}
		return (pow(a, b-1, p) * a) % p;
	}
}
