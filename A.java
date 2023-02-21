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
		
		
		int T = in.nextInt();
		
		int[] A = new int[100100];
		int[] noway = new int[100100];
		int[] forward = new int[100100];
		int[] P = new int[100100];
		int[] Q = new int[100100];
		int[] xl = new int[100100];
		int[] xr = new int[100100];

		StringBuilder ans = new StringBuilder();

		for (int ts=1; ts<=T; ts++) {
			int N = in.nextInt();
			
			
			for (int i=1; i<=N; i++) {
				A[i] = in.nextInt();
				noway[A[i]] = i;
				forward[i] = 0;
				
				xl[i] = i-1;
				xr[i] = i+1;
			}
			xr[0] = 1;
			xl[N+1] = N;
			
			for (int i=1; i<=N; i++) {
				int y = xr[0];
				while (y <= N) {
					if (y != i && y != noway[i]) {
						forward[i] = y;
						
						xr[xl[y]] = xr[y];
						xl[xr[y]] = xl[y];
						break;
					}
					y = xr[y];
				}
			}
			
			for (int i=1; i<=N; i++) {
				if (forward[i] != 0) {
					continue;
				}
				for (int j=1; j<=Math.min(10, N); j++) {
					if (forward[i] != 0)
						break;
					if (forward[j] == 0) {
						continue;
					}
					if (!(forward[j] != i && forward[j] != noway[i])) {
						continue;
					}
					int y = xr[0];
					while (y <= N) {
						if (y != j && y != noway[j]) {
							forward[i] = forward[j];
							forward[j] = y;
							
							xr[xl[y]] = xr[y];
							xl[xr[y]] = xl[y];
							break;
						}
						y = xr[y];
					}
				}
			}
			
			boolean possible = true;
			for (int i=1; i<=N; i++) {
				if (forward[i] == 0) {
					possible = false;
				}
				P[forward[i]] = noway[i];
				Q[i] = forward[i];
			}
			
			
			
			if (!possible) {
				ans.append("Impossible\n");
			} else {
				ans.append("Possible\n");
				for (int i=1; i<N; i++) {
					ans.append(P[i]).append(" ");
				}
				ans.append(P[N]).append("\n");
				
				for (int i=1; i<N; i++) {
					ans.append(Q[i]).append(" ");
				}
				ans.append(Q[N]).append("\n");
			}
			
		}
		out.write(ans.toString());
		
		out.close();
	}

}
