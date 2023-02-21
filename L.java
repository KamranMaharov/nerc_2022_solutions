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
	
	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in), 200000 * 20);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out), 50000000);
		//Scanner in = new Scanner(System.in);
		
		String[] line = in.readLine().split(" ");
		int N = Integer.parseInt(line[0]);
		int K = Integer.parseInt(line[1]);
		
		int[] b = new int[N+1];
		line = in.readLine().split(" ");
		for (int i=1; i<=N; i++) {
			b[i] = Integer.parseInt(line[i-1]);
		}
		
		long L = 1000005;
		long L2 = L * L;
		
		long[][] states = new long[N+1][6];
		byte[][] parent = new byte[N+1][6];
		
		byte[] fwd = new byte[13];
		int[] rfwd = new int[6];
		fwd[0 * 10 + 0] = 0;
		rfwd[0] = 0 * 10 + 0;

		fwd[0 * 10 + 1] = 1;
		rfwd[1] = 0 * 10 + 1;
		
		fwd[0 * 10 + 2] = 2;
		rfwd[2] = 0 * 10 + 2;
		
		fwd[1 * 10 + 0] = 3;
		rfwd[3] = 1 * 10 + 0;
		
		fwd[1 * 10 + 1] = 4;
		rfwd[4] = 1 * 10 + 1;
		
		fwd[1 * 10 + 2] = 5;
		rfwd[5] = 1 * 10 + 2;
		
		for (int i=0; i<=N; i++)
			Arrays.fill(states[i], -1);
		states[0][fwd[0 * 10 + 1]] = 0 * L2 + 0 * L + 0;
		
		b[0] = b[1];
		
		for (int i=1; i<=N; i++) {
			//out.write(i + "\n");
			//out.flush();
			
			for (int idx = 0; idx < 6; idx++) {
				if (states[i-1][idx] < 0)
					continue;
				int du = rfwd[idx];
			//for (final Integer du : states[i-1].keySet()) {
				int d = du / 10;
				int u = du % 10;
				
				long mts = states[i-1][idx];
				int m = (int)(mts / L2);
				int t = (int)((mts - m * L2) / L);
				int s = (int)(mts % L);
				
				for (int nu=0; nu<=2; nu++) {
					int prev = b[i-1];
					if (u == 0) prev = 100000;
					else if (u == 2) prev = 0;
					
					int cur = b[i];
					if (nu == 0)cur = 100000;
					else if (nu == 2) cur = 0;
					
					int nt, ns, nd;
					if (cur == prev) {
						nt = t + 1;
						ns = s + 1;
						nd = d;
					} else if (cur < prev) {
						nd = 1;
						if (d == 0) {
							nt = s;
						} else {
							nt = t;
						}
						nt += 1;
						ns = 1;
					} else {
						nd = 0;
						if (d == 1) {
							nt = s;
						} else {
							nt = t;
						}
						nt += 1;
						ns = 1;
					}
					
					if (nt == K) continue;
					
					int nm = m;
					if (nu != 1) nm += 1;
					
					if (states[i][fwd[nd * 10 + nu]] < 0) {
						states[i][fwd[nd * 10 + nu]] =
										nm * L2 + nt * L + ns;
						parent[i][fwd[nd * 10 + nu]] =
										fwd[d * 10 + u];
					} else {
						long current = states[i][fwd[nd * 10 + nu]];
						int cnm = (int)(current / L2);
						int cnt = (int)((current - cnm * L2) / L);
						int cns = (int)(current % L);
						if (nm < cnm || (nm == cnm && nt < cnt) || 
								(nm == cnm && nt == cnt && ns < cns)) {
							states[i][fwd[nd * 10 + nu]] =
									nm * L2 + nt * L + ns;
							parent[i][fwd[nd * 10 + nu]] =
									fwd[d * 10 + u];
						}
					}
					
				}
			}
		}
		
		
		/*for (final List<Integer> du : states[N].keySet()) {
			out.write(du.get(0) + " " + du.get(1) + "     ->     ");
			List<Integer> mts = states[N].get(du);
			out.write(mts.get(0) + ", " + mts.get(1) + ", " + mts.get(2) + "\n");
		}*/
		
		int ansM = Integer.MAX_VALUE;
		int ansT = Integer.MAX_VALUE;
		int ansS = Integer.MAX_VALUE;
		int d = 0, u = 0;
		
		for (int idx =0; idx<6; idx++) {
		//for (final Integer key : states[N].keySet()) {
			if (states[N][idx] < 0)
				continue;
			long val = states[N][idx];
			int m = (int) (val / L2);
			int t = (int) ((val - m * L2) / L);
			int s = (int) (val % L);
			if (m < ansM || (m == ansM && t < ansT) ||
					(m == ansM && t == ansT && s < ansS)) {
				ansM = m;
				ansT = t;
				ansS = s;
				d = rfwd[idx] / 10;
				u = rfwd[idx] % 10;
			}
		}
		
		for (int i=N; i>=1; i--) {
			//out.write(i + " -> " + d  + " " + u + "\n");
			//out.write(states[i].get(Arrays.asList(d, u)).get(0) + "\n\n");
			if (u == 0) b[i] = 100000;
			else if (u == 2) b[i] = 0;
			Integer pdu = rfwd[parent[i][fwd[d * 10 + u]]];
			d = pdu / 10;
			u = pdu % 10;
		}
		
		out.write(ansM + "\n");
	
		out.write(b[1] + "");
		for (int i=2; i<=N; i++) {
			out.write(" " + b[i]);
		}
		out.write("\n");
		
		in.close();
		out.close();
	}
}
