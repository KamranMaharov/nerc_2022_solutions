import java.io.*;
import java.math.BigInteger;
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
	
	private static void recurse(int number, int cur_idx, int last_idx, int fq_idx,
			int[] wildcard, int[] P3) {
		if (cur_idx == last_idx) {
			if (fq_idx >= 0) {
				wildcard[number] = wildcard[number - 2 * P3[last_idx - 1 - fq_idx]] +
						wildcard[number - P3[last_idx - 1 - fq_idx]];
			}
			return;
		}
		for (int digit = 0; digit < 3; digit++) {
			recurse(number * 3 + digit, cur_idx + 1, last_idx,
					(fq_idx >= 0 ? fq_idx : ( (digit == 2) ? cur_idx : -1)), wildcard, P3);
		}
	}
	
	public static void main(String[] args) throws Exception {
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in), 200000 * 20);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		Scanner in = new Scanner(System.in);
		
		int N = in.nextInt();
		int M = in.nextInt();
		//int N = 0;
		//int M = 17;
		double[] dp = new double[1<<M];
		int[] transition = new int[(1<<M)];
		int MP3 = (int)(Math.pow(3, M) + 1e-9);
		int[] wildcard = new int[MP3];
		int[] bitcount = new int[1<<M];
		int[] ver3 = new int[1<<M];
		int[] P3 = new int[M];
		
		int num3;
		
		
		for (int i=1; i<=N; i++) {
			num3 = 0;
			for (int j=0; j<M; j++) {
				char charAtJ = (char)in.getc();
				while (charAtJ != '0' && charAtJ != '1')
					charAtJ = (char)in.getc();
				num3 *= 3;
				if (charAtJ == '1') {
					num3 += 1;
				}
			}
			++wildcard[num3];
			
			
		}
		
		P3[0] = 1;
		for (int i=1; i<M; i++) {
			P3[i] = P3[i-1] * 3;
		}
		
		//long t1 = System.currentTimeMillis();
		//if (M == 17)
		recurse(0, 0, M, -1, wildcard, P3);
		//out.write((System.currentTimeMillis() - t1) + "\n");
		/*int total_it = 0;
		int jumbo_it;
		for(int w=0; w<MP3; w++) {
			int num = w;
			jumbo_it = 0;
			while (num > 0) {
				if (num % 3 == 2) {
					wildcard[w] = wildcard[w - 2 * P3[jumbo_it]] +
							wildcard[w - P3[jumbo_it]];
					break;
				}
				num /= 3;
				jumbo_it++;
				total_it++;
			}
		}
		out.write(total_it + "\n");*/
		
		
		ver3[(1<<0)] = P3[M-1];
		bitcount[(1<<0)] = 1;
		for (int i=1; i<M; i++) {
			P3[i] = P3[i-1] * 3;
			ver3[(1<<i)] = P3[M-1-i];
			bitcount[(1<<i)] = 1;
		}
		
		int allones = (1<<M)-1;
		for (int mask=0; mask<=allones; mask++) {
			if (ver3[mask] > 0)
				continue;

			bitcount[mask] = bitcount[mask&(mask-1)] + 1;
			ver3[mask] = ver3[mask & (mask - 1)] + ver3[mask ^ (mask & (mask - 1))];


		}
		
		int total = 0;
		
		
		for (int bc=1; bc<=M; bc++) {
			
			
			for (int mask=1; mask<=allones; mask++) {
				if (bitcount[mask] != bc)
					continue;
				
				if ((mask & 1) == 0) {
					dp[mask] = 0.0;
					
				} else if (transition[mask] == 0) {
					dp[mask] = 1.0;
					
				} else {
					dp[mask] /= transition[mask];
				}
				
				//if (dp[mask] < 1e-19)
				//	continue;
				
				int supermask = mask + 1;
				while (supermask <= allones) {
					if ((supermask & mask) != mask) {
						supermask = (supermask | mask);
					}
					
					if ((supermask & 1) == 0) {
						supermask++;
					}
					
					int query = ver3[allones^supermask] * 2 + ver3[mask];
					transition[supermask] += wildcard[query];
					dp[supermask] += dp[mask] * wildcard[query];

					supermask++;
				}
				
			}
			
			
			
		}
		
		out.write(dp[(1<<M) - 1] + "\n");
		
		
		//in.close();
		out.close();
	}
}
