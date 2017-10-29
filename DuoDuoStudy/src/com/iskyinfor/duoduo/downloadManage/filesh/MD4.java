package com.iskyinfor.duoduo.downloadManage.filesh;
public class MD4 {
	private int A = 0x67452301;
	private int B = 0xefcdab89;
	private int C = 0x98badcfe;
	private int D = 0x10325476;
	private int Length = 0;
	private byte[] BUFFER = new byte[128];
	private int OFFSET;

	private int X[] = new int[16];

	private int F(int X, int Y, int Z) {
		return (X & Y) | ((~X) & Z);
	}

	private int G(int X, int Y, int Z) {
		return (X & Y) | (X & Z) | (Y & Z);
	}

	private int H(int X, int Y, int Z) {
		return X ^ Y ^ Z;
	}

	private int lshift(int x, int s) {
		if (s == 0)
			return x;
		return (((x << s) & 0xFFFFFFFF) | ((x >> (32 - s)) & (0x7FFFFFFF >> (31 - s))));
	}

	private int ROUND1(int a, int b, int c, int d, int k, int s) {
		return (lshift(a + F(b, c, d) + X[k], s));
	}

	private int ROUND2(int a, int b, int c, int d, int k, int s) {
		return (lshift(a + G(b, c, d) + X[k] + (int) 0x5A827999, s));
	}

	private int ROUND3(int a, int b, int c, int d, int k, int s) {
		return (lshift(a + H(b, c, d) + X[k] + (int) 0x6ED9EBA1, s));
	}

	/* this applies md4 to 64 byte chunks */
	public void mdfour64(int M[]) {
		int j;
		int AA, BB, CC, DD;

		for (j = 0; j < 16; j++)
			X[j] = M[j];

		AA = A;
		BB = B;
		CC = C;
		DD = D;

		A = ROUND1(A, B, C, D, 0, 3);
		D = ROUND1(D, A, B, C, 1, 7);
		C = ROUND1(C, D, A, B, 2, 11);
		B = ROUND1(B, C, D, A, 3, 19);
		A = ROUND1(A, B, C, D, 4, 3);
		D = ROUND1(D, A, B, C, 5, 7);
		C = ROUND1(C, D, A, B, 6, 11);
		B = ROUND1(B, C, D, A, 7, 19);
		A = ROUND1(A, B, C, D, 8, 3);
		D = ROUND1(D, A, B, C, 9, 7);
		C = ROUND1(C, D, A, B, 10, 11);
		B = ROUND1(B, C, D, A, 11, 19);
		A = ROUND1(A, B, C, D, 12, 3);
		D = ROUND1(D, A, B, C, 13, 7);
		C = ROUND1(C, D, A, B, 14, 11);
		B = ROUND1(B, C, D, A, 15, 19);

		A = ROUND2(A, B, C, D, 0, 3);
		D = ROUND2(D, A, B, C, 4, 5);
		C = ROUND2(C, D, A, B, 8, 9);
		B = ROUND2(B, C, D, A, 12, 13);
		A = ROUND2(A, B, C, D, 1, 3);
		D = ROUND2(D, A, B, C, 5, 5);
		C = ROUND2(C, D, A, B, 9, 9);
		B = ROUND2(B, C, D, A, 13, 13);
		A = ROUND2(A, B, C, D, 2, 3);
		D = ROUND2(D, A, B, C, 6, 5);
		C = ROUND2(C, D, A, B, 10, 9);
		B = ROUND2(B, C, D, A, 14, 13);
		A = ROUND2(A, B, C, D, 3, 3);
		D = ROUND2(D, A, B, C, 7, 5);
		C = ROUND2(C, D, A, B, 11, 9);
		B = ROUND2(B, C, D, A, 15, 13);

		A = ROUND3(A, B, C, D, 0, 3);
		D = ROUND3(D, A, B, C, 8, 9);
		C = ROUND3(C, D, A, B, 4, 11);
		B = ROUND3(B, C, D, A, 12, 15);
		A = ROUND3(A, B, C, D, 2, 3);
		D = ROUND3(D, A, B, C, 10, 9);
		C = ROUND3(C, D, A, B, 6, 11);
		B = ROUND3(B, C, D, A, 14, 15);
		A = ROUND3(A, B, C, D, 1, 3);
		D = ROUND3(D, A, B, C, 9, 9);
		C = ROUND3(C, D, A, B, 5, 11);
		B = ROUND3(B, C, D, A, 13, 15);
		A = ROUND3(A, B, C, D, 3, 3);
		D = ROUND3(D, A, B, C, 11, 9);
		C = ROUND3(C, D, A, B, 7, 11);
		B = ROUND3(B, C, D, A, 15, 15);

		A += AA;
		B += BB;
		C += CC;
		D += DD;

		A &= 0xFFFFFFFF;
		B &= 0xFFFFFFFF;
		C &= 0xFFFFFFFF;
		D &= 0xFFFFFFFF;
	}

	public void copy64(int M[], byte in[], int offset) {
		int i;

		for (i = 0; i < 16; i++) {
			M[i] = ((in[offset + i * 4 + 3] << 24) & 0xFF000000)
					| ((in[offset + i * 4 + 2] << 16) & 0xFF0000)
					| ((in[offset + i * 4 + 1] << 8) & 0xFF00)
					| (((int) in[offset + i * 4 + 0]) & 0xFF);
		}
	}

	public void copy64(int M[], byte in[]) {
		copy64(M, in, 0);
	}

	public void copy4(byte out[], int offset, int x) {
		out[offset] = (byte) (x & 0xFF);
		out[1 + offset] = (byte) ((x >> 8) & 0xFF);
		out[2 + offset] = (byte) ((x >> 16) & 0xFF);
		out[3 + offset] = (byte) ((x >> 24) & 0xFF);
	}

	public void copy1(byte out[], int offset, byte x) {
		out[offset] = (byte) (x & 0xFF);
	}

	/* produce a md4 message digest from data of length n bytes */
	public void add(byte in[], int len) {
		int[] M = new int[16];
		int n = len;
		int offset = 0;

		if (OFFSET > 0 && OFFSET + n >= 64) {
			for (int pos = OFFSET; pos < 64; pos++) {
				copy1(BUFFER, OFFSET + pos, in[pos - OFFSET]);
				offset++;
			}
			copy64(M, BUFFER);
			mdfour64(M);
			OFFSET = 0;
			n -= 64 - OFFSET;
		}

		while (n >= 64) {
			copy64(M, in, offset);
			mdfour64(M);
			n -= 64;
			offset += 64;
		}

		if (n > 0) {
			for (int pos = 0; pos < n; pos++) {
				copy1(BUFFER, OFFSET, in[offset]);
				OFFSET++;
				offset++;
			}
		}

		Length += len;
	}

	public void finish() {
		int b = Length * 8;
		int[] M = new int[16];

		BUFFER[OFFSET] = (byte) 0x80;

		if (OFFSET <= 55) {
			copy4(BUFFER, 56, b);
			copy64(M, BUFFER);
			mdfour64(M);
		} else {
			copy4(BUFFER, 120, b);
			copy64(M, BUFFER);
			mdfour64(M);
			copy64(M, BUFFER, 64);
			mdfour64(M);
		}
	}

	public byte[] gethash() {
		byte[] out = new byte[16];

		copy4(out, 0, A);
		copy4(out, 4, B);
		copy4(out, 8, C);
		copy4(out, 12, D);

		return out;
	}
}
