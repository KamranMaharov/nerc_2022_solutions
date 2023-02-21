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
	
	private static void close(BufferedReader in, BufferedWriter out) throws Exception {
		in.close();
		out.close();
	}
	
	public static void main(String[] args) throws Exception {
		//Scanner in = new Scanner(System.in);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
				
		out.write(0 + " " + 0 + "\n");
		out.flush();
		String sameDistance = in.readLine();
		if (sameDistance.endsWith("!")) {
			close(in, out);
			return;
		}
		
		out.write(0 + " " + 0 + "\n");
		out.flush();
		sameDistance = in.readLine();
		
		out.write(1 + " " + 1 + "\n");
		out.flush();
		String closer = in.readLine();
		if (closer.endsWith("!")) {
			close(in, out);
			return;
		}
		
		
		if (closer.equals(sameDistance)) {
			out.write(0 + " " + 1 + "\n");
			out.flush();
			String endgame = in.readLine();
			if (endgame.endsWith("!")) {
				close(in, out);
				return;
			}
			
			out.write(1 + " " + 0 + "\n");
			out.flush();
			endgame = in.readLine();
			if (endgame.endsWith("!")) {
				close(in, out);
				return;
			}
			return;
		}
		
		int xl = 0, xr = 1000000;
		int yl = 0, yr = 1000000;
		
		int it = 1;
		while (it <= 100) {
			it++;
			int xmid = (xl + xr) / 2;
			int ymid = (yl + yr) / 2;
			out.write(xmid + " " + ymid + "\n");
			out.flush();
			String resp1 = in.readLine();
			if (resp1.endsWith("!")) {
				close(in, out);
				return;
			}
			
			if (xmid < 1000000) {
				out.write((xmid + 1) + " " + ymid + "\n");
				out.flush();
				resp1 = in.readLine();
				if (resp1.endsWith("!")) {
					close(in, out);
					return;
				}
				if (resp1.equals(closer)) {
					xl = xmid + 1;
				} else {
					xr = xmid;
				}
				
				
				if (ymid < 1000000) {
					out.write((xmid + 1) + " " + (ymid + 1) + "\n");
					out.flush();
					resp1 = in.readLine();
					if (resp1.endsWith("!")) {
						close(in, out);
						return;
					}
					if (resp1.equals(closer)) {
						yl = ymid + 1;
					} else {
						yr = ymid;
					}
				}
			} else if (ymid < 1000000) {
				out.write(xmid + " " + (ymid + 1) + "\n");
				out.flush();
				resp1 = in.readLine();
				if (resp1.endsWith("!")) {
					close(in, out);
					return;
				}
				if (resp1.equals(closer)) {
					yl = ymid + 1;
				} else {
					yr = ymid;
				}
			}
			
			
		}
		
		
		
		
		close(in, out);
	}
}
