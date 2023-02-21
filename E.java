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
	
	public static void main(String[] args) throws Exception {
		//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		Scanner in = new Scanner(System.in);
		
		
		int N = in.nextInt();
		List<Integer>[] blocks = new ArrayList[N+1];
		List<Integer> all = new ArrayList<>();
		
		for (int i=1; i<=N; i++) {
			int K = in.nextInt();
			blocks[i] = new ArrayList<>();
			for (int j=1; j<=K; j++) {
				int num = in.nextInt();
				blocks[i].add(num);
				all.add(num);
			}
		}
		
		Collections.sort(all);
		/*for (final Integer x : all) {
			out.write(x + "\n");
		}*/
		HashMap<Integer, Integer> indices = new HashMap<>();
		for (int i=0; i<all.size(); i++) {
			indices.put(all.get(i), i);
		}
		
		int split = 0;
		for (int i=1; i<=N; i++) {
			for (int j=0; j+1<blocks[i].size(); j++) {
				int idx1 = indices.get(blocks[i].get(j));
				int idx2 = indices.get(blocks[i].get(j+1));
				if(idx2 != idx1 + 1)
					++split;
			}
		}
		out.write(split + " " + (N-1+split) + "\n");
		
		//in.close();
		out.close();
	}
}
