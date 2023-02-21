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
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		
		
		int[][] factorials = new int[5983][2050];
		int[] len = new int[5983];
		factorials[1][0] = 1;
		len[1] = 1;
		
		for (int num=2; num < 5983; num++) {
			int pos = 0;
			int remNum = num;
			while (remNum > 0) {
				int dig = remNum % 10;
				int carry = 0;
				for (int j=0; j<len[num-1]; j++) {
					int total = dig * factorials[num-1][j] + factorials[num][pos+j] + carry;
					factorials[num][pos+j] = total % 10;
					carry = total / 10;
				}
				int toCarry = pos + len[num-1];
				while (carry > 0) {
					factorials[num][toCarry] = carry % 10;
					carry /= 10;
					toCarry++;
				}
				pos++;
				remNum /= 10;
			}
			
			len[num] = len[num-1] + 20;
			while (factorials[num][len[num]] == 0)
				len[num] -= 1;
			if (len[num] > 2020)
				len[num] = 2020;
			len[num] += 1;

		}

		
		int T = Integer.parseInt(in.readLine());
		
		for (int ts=1; ts<=T; ts++) {
			
			
			
			out.write("? " + 1601 + "\n");

			out.flush();
			Integer response = Integer.parseInt(in.readLine());
			ArrayList<Integer> remaining = new ArrayList<>();
			
			for (int xxx=1; xxx<=5982; xxx++) {
				if (factorials[xxx][1601] == response) {
					remaining.add(xxx);
				}
			}
			
			int[] bucket = new int[10];
			
			for (int guess=2; guess<=10; guess++) {
				int minMaxSize = Integer.MAX_VALUE;
				int choice = -1;
				
				for (int dig=0; dig<=300; dig++) {
					
					for (final Integer num : remaining) {
						bucket[factorials[num][dig]]++;
					}
					int maxSize = 0;
					for (int dd=0; dd<10; dd++) {
						if (bucket[dd] > maxSize) {
							maxSize = bucket[dd];
						}
						bucket[dd] = 0;
					}
					if (maxSize < minMaxSize) {
						minMaxSize = maxSize;
						choice = dig;
					}
				}
				
				for (int dig=1200; dig<=1500; dig++) {
					
					for (final Integer num : remaining) {
						bucket[factorials[num][dig]]++;
					}
					int maxSize = 0;
					for (int dd=0; dd<10; dd++) {
						if (bucket[dd] > maxSize) {
							maxSize = bucket[dd];
						}
						bucket[dd] = 0;
					}
					if (maxSize < minMaxSize) {
						minMaxSize = maxSize;
						choice = dig;
					}
				}
				
				out.write("? " + choice + "\n");

				out.flush();
				response = Integer.parseInt(in.readLine());
				ArrayList<Integer> newList = new ArrayList<>();
				for (final Integer xxx : remaining) {
					if (factorials[xxx][choice] == response) {
						newList.add(xxx);
					}
				}
				remaining = newList;
			}
			
			out.write("! " + remaining.get(0) + "\n");
			out.flush();
			String yes = in.readLine();

		}
		
		
		
		
		
		
		in.close();
		out.close();
	}
}
