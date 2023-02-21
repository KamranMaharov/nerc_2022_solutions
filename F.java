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
		int A = in.nextInt();
		int B = in.nextInt();
		
		if (N == 1) {
			out.write(((A == B) ? 1 : 0) + "\n");
			out.write(A + ":" + B + "\n");
			out.close();
			return;
		}
		
		if (N > (A+B)) {
			out.write((N - (A+B)) + "\n");
			for (int i=1; i<=A; i++) {
				out.write("1:0\n");
			}
			for (int i=1; i<=B; i++) {
				out.write("0:1\n");
			}
			for (int i=1; i<=(N-(A+B)); i++) {
				out.write("0:0\n");
			}
		} else {
			// N < A + B
			out.write("0\n");
			while (A > 0) {
				if ((N == 2 && B > 0) || N == 1) {
					//out.write("H1\n");
					out.write(A+":0\n");
					A = 0;
				} else {
					//out.write("H2\n");
					out.write("1:0\n");
					A--;
				}
				N--;
			}
			
			while (B > 0) {
				if (N == 1) {
					out.write("0:" + B + "\n");
					B = 0;
				} else {
					out.write("0:" + 1 + "\n");
					B--;
				}
				N--;
			}
		}
		
		//in.close();
		out.close();
	}
}
