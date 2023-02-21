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
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		
		
		int N = in.nextInt();
		int K = in.nextInt();
		
		if (N == 1 && K == 1) {
			out.write("YES\n0\n");
			out.close();
			return;
		}
		
		if (N == K) {
			out.write("NO\n");
			out.close();
			return;
		}
		
		if (K == 1 && N > 2) {
			out.write("YES\n");
			out.write(N + "\n");
			for (int i=1; i<=N; i++) {
				out.write(i + " " + ((i+1>N)?1:(i+1)) + "\n");
			}
			out.close();
			return;
		}
		
		
		boolean[][] G = new boolean[N+5][N+5];
		
		int[] A = new int[K+2];
		for (int i=1, j=K+1; i<j; i++, j--) {
			for (int t=i+1; t<=j; t++) {
				G[i][t] = G[t][i] = true;
				//out.write(i + " " + t + "\n");
			}
		}
		
		for (int j=K+2; j<=N; j++) {
			G[1][j] = G[j][1] = true;
		}
		
		
		int count = 0;
		for (int i=1; i<=N; i++) {
			for (int j=i; j<=N; j++) {
				if (G[i][j])
					count++;
			}
			//out.write(i + " " + deg + "\n");
		}
		out.write("YES\n");
		out.write(count + "\n");
		for (int i=1; i<=N; i++) {
			for (int j=i; j<=N; j++) {
				if (G[i][j]) {
					out.write(i + " " + j + "\n");
				}
			}
		}
		
		//out.write("Kamran trying");
		
		/*int T = Integer.parseInt(in.readLine());
		
		for (int ts=1; ts<=T; ts++) {
			String st = in.readLine();
			
			String[] keys = new String[]{"r", "g", "b"};
			boolean can = true;
			for (String key : keys) {
				int key_idx = st.indexOf(key);
				int door_idx = st.indexOf(key.toUpperCase());
				if (key_idx > door_idx)
					can = false;
			}
			out.write(can ? "YES\n" : "NO\n");
		}*/
		
		//in.close();
		out.close();
	}
 
}
