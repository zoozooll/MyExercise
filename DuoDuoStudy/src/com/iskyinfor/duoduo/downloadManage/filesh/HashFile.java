package com.iskyinfor.duoduo.downloadManage.filesh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;



public class HashFile {

	private String strFileName; // �ļ����
	private String strFilePath; // �ļ�·��
	private String strFileType; // �ļ�����
	private long nFileSize; // �ļ���С
	private int nPartCount; // PART����
	private String TypeTable[][]; // �ļ����Ͷ��ձ�
	private byte[][] iPartHash; // PART HASH
	private String strFileHash; // �ļ�HASH
	private String strFullPath; // �ļ�ȫ·��
	private static int nBlockSize = 2048; // ÿ�ζ�д�ļ���Ĵ�С
	private static int nPartSize = 9728000; // PART��С
	private static int nMaxPartSize = 441; // ���PART��Ŀ

	public HashFile() {
		TypeTable = new String[151][2];

		TypeTable[0][0] = ".z";
		TypeTable[0][1] = "Arc";
		TypeTable[1][0] = ".7z";
		TypeTable[1][1] = "Arc";
		TypeTable[2][0] = ".gz";
		TypeTable[2][1] = "Arc";
		TypeTable[3][0] = ".ace";
		TypeTable[3][1] = "Arc";
		TypeTable[4][0] = ".alz";
		TypeTable[4][1] = "Arc";
		TypeTable[5][0] = ".arj";
		TypeTable[5][1] = "Arc";
		TypeTable[6][0] = ".bz2";
		TypeTable[6][1] = "Arc";
		TypeTable[7][0] = ".cab";
		TypeTable[7][1] = "Arc";
		TypeTable[8][0] = ".cbr";
		TypeTable[8][1] = "Arc";
		TypeTable[9][0] = ".cbz";
		TypeTable[9][1] = "Arc";
		TypeTable[10][0] = ".hqx";
		TypeTable[10][1] = "Arc";
		TypeTable[11][0] = ".lha";
		TypeTable[11][1] = "Arc";
		TypeTable[12][0] = ".lzh";
		TypeTable[12][1] = "Arc";
		TypeTable[13][0] = ".msi";
		TypeTable[13][1] = "Arc";
		TypeTable[14][0] = ".rar";
		TypeTable[14][1] = "Arc";
		TypeTable[15][0] = ".sea";
		TypeTable[15][1] = "Arc";
		TypeTable[16][0] = ".sit";
		TypeTable[16][1] = "Arc";
		TypeTable[17][0] = ".tar";
		TypeTable[17][1] = "Arc";
		TypeTable[18][0] = ".tgz";
		TypeTable[18][1] = "Arc";
		TypeTable[19][0] = ".uc2";
		TypeTable[19][1] = "Arc";
		TypeTable[20][0] = ".zip";
		TypeTable[20][1] = "Arc";
		TypeTable[21][0] = "0.669";
		TypeTable[21][1] = "Audio";
		TypeTable[22][0] = ".au";
		TypeTable[22][1] = "Audio";
		TypeTable[23][0] = ".it";
		TypeTable[23][1] = "Audio";
		TypeTable[24][0] = ".ra";
		TypeTable[24][1] = "Audio";
		TypeTable[25][0] = ".xm";
		TypeTable[25][1] = "Audio";
		TypeTable[26][0] = ".aac";
		TypeTable[26][1] = "Audio";
		TypeTable[27][0] = ".aif";
		TypeTable[27][1] = "Audio";
		TypeTable[28][0] = ".amf";
		TypeTable[28][1] = "Audio";
		TypeTable[29][0] = ".ams";
		TypeTable[29][1] = "Audio";
		TypeTable[30][0] = ".ape";
		TypeTable[30][1] = "Audio";
		TypeTable[31][0] = ".dbm";
		TypeTable[31][1] = "Audio";
		TypeTable[32][0] = ".dmf";
		TypeTable[32][1] = "Audio";
		TypeTable[33][0] = ".dsm";
		TypeTable[33][1] = "Audio";
		TypeTable[34][0] = ".far";
		TypeTable[34][1] = "Audio";
		TypeTable[35][0] = ".m4a";
		TypeTable[35][1] = "Audio";
		TypeTable[36][0] = ".mdl";
		TypeTable[36][1] = "Audio";
		TypeTable[37][0] = ".med";
		TypeTable[37][1] = "Audio";
		TypeTable[38][0] = ".mid";
		TypeTable[38][1] = "Audio";
		TypeTable[39][0] = ".mka";
		TypeTable[39][1] = "Audio";
		TypeTable[40][0] = ".mod";
		TypeTable[40][1] = "Audio";
		TypeTable[41][0] = ".mol";
		TypeTable[41][1] = "Audio";
		TypeTable[42][0] = ".mp1";
		TypeTable[42][1] = "Audio";
		TypeTable[43][0] = ".mp2";
		TypeTable[43][1] = "Audio";
		TypeTable[44][0] = ".mp3";
		TypeTable[44][1] = "Audio";
		TypeTable[45][0] = ".mp4";
		TypeTable[45][1] = "Audio";
		TypeTable[46][0] = ".mpa";
		TypeTable[46][1] = "Audio";
		TypeTable[47][0] = ".mpc";
		TypeTable[47][1] = "Audio";
		TypeTable[48][0] = ".mpp";
		TypeTable[48][1] = "Audio";
		TypeTable[49][0] = ".mtm";
		TypeTable[49][1] = "Audio";
		TypeTable[50][0] = ".nst";
		TypeTable[50][1] = "Audio";
		TypeTable[51][0] = ".ogg";
		TypeTable[51][1] = "Audio";
		TypeTable[52][0] = ".okt";
		TypeTable[52][1] = "Audio";
		TypeTable[53][0] = ".psm";
		TypeTable[53][1] = "Audio";
		TypeTable[54][0] = ".ptm";
		TypeTable[54][1] = "Audio";
		TypeTable[55][0] = ".rmi";
		TypeTable[55][1] = "Audio";
		TypeTable[56][0] = ".s3m";
		TypeTable[56][1] = "Audio";
		TypeTable[57][0] = ".stm";
		TypeTable[57][1] = "Audio";
		TypeTable[58][0] = ".ult";
		TypeTable[58][1] = "Audio";
		TypeTable[59][0] = ".umx";
		TypeTable[59][1] = "Audio";
		TypeTable[60][0] = ".wav";
		TypeTable[60][1] = "Audio";
		TypeTable[61][0] = ".wma";
		TypeTable[61][1] = "Audio";
		TypeTable[62][0] = ".wow";
		TypeTable[62][1] = "Audio";
		TypeTable[63][0] = ".aiff";
		TypeTable[63][1] = "Audio";
		TypeTable[64][0] = ".flac";
		TypeTable[64][1] = "Audio";
		TypeTable[65][0] = ".midi";
		TypeTable[65][1] = "Audio";
		TypeTable[66][0] = ".ps";
		TypeTable[66][1] = "Doc";
		TypeTable[67][0] = ".chm";
		TypeTable[67][1] = "Doc";
		TypeTable[68][0] = ".css";
		TypeTable[68][1] = "Doc";
		TypeTable[69][0] = ".diz";
		TypeTable[69][1] = "Doc";
		TypeTable[70][0] = ".doc";
		TypeTable[70][1] = "Doc";
		TypeTable[71][0] = ".dot";
		TypeTable[71][1] = "Doc";
		TypeTable[72][0] = ".hlp";
		TypeTable[72][1] = "Doc";
		TypeTable[73][0] = ".htm";
		TypeTable[73][1] = "Doc";
		TypeTable[74][0] = ".nfo";
		TypeTable[74][1] = "Doc";
		TypeTable[75][0] = ".pdf";
		TypeTable[75][1] = "Doc";
		TypeTable[76][0] = ".pps";
		TypeTable[76][1] = "Doc";
		TypeTable[77][0] = ".ppt";
		TypeTable[77][1] = "Doc";
		TypeTable[78][0] = ".rtf";
		TypeTable[78][1] = "Doc";
		TypeTable[79][0] = ".txt";
		TypeTable[79][1] = "Doc";
		TypeTable[80][0] = ".wri";
		TypeTable[80][1] = "Doc";
		TypeTable[81][0] = ".xls";
		TypeTable[81][1] = "Doc";
		TypeTable[82][0] = ".xml";
		TypeTable[82][1] = "Doc";
		TypeTable[83][0] = ".html";
		TypeTable[83][1] = "Doc";
		TypeTable[84][0] = ".emulecollection";
		TypeTable[84][1] = "EmuleCollection";
		TypeTable[85][0] = ".bmp";
		TypeTable[85][1] = "Image";
		TypeTable[86][0] = ".dcx";
		TypeTable[86][1] = "Image";
		TypeTable[87][0] = ".emf";
		TypeTable[87][1] = "Image";
		TypeTable[88][0] = ".gif";
		TypeTable[88][1] = "Image";
		TypeTable[89][0] = ".ico";
		TypeTable[89][1] = "Image";
		TypeTable[90][0] = ".jpg";
		TypeTable[90][1] = "Image";
		TypeTable[91][0] = ".pct";
		TypeTable[91][1] = "Image";
		TypeTable[92][0] = ".pcx";
		TypeTable[92][1] = "Image";
		TypeTable[93][0] = ".pic";
		TypeTable[93][1] = "Image";
		TypeTable[94][0] = ".png";
		TypeTable[94][1] = "Image";
		TypeTable[95][0] = ".psd";
		TypeTable[95][1] = "Image";
		TypeTable[96][0] = ".psp";
		TypeTable[96][1] = "Image";
		TypeTable[97][0] = ".tga";
		TypeTable[97][1] = "Image";
		TypeTable[98][0] = ".tif";
		TypeTable[98][1] = "Image";
		TypeTable[99][0] = ".wmf";
		TypeTable[99][1] = "Image";
		TypeTable[100][0] = ".xif";
		TypeTable[100][1] = "Image";
		TypeTable[101][0] = ".jpeg";
		TypeTable[101][1] = "Image";
		TypeTable[102][0] = ".pict";
		TypeTable[102][1] = "Image";
		TypeTable[103][0] = ".tiff";
		TypeTable[103][1] = "Image";
		TypeTable[104][0] = ".bin";
		TypeTable[104][1] = "Iso";
		TypeTable[105][0] = ".bwa";
		TypeTable[105][1] = "Iso";
		TypeTable[106][0] = ".bwi";
		TypeTable[106][1] = "Iso";
		TypeTable[107][0] = ".bws";
		TypeTable[107][1] = "Iso";
		TypeTable[108][0] = ".bwt";
		TypeTable[108][1] = "Iso";
		TypeTable[109][0] = ".ccd";
		TypeTable[109][1] = "Iso";
		TypeTable[110][0] = ".cue";
		TypeTable[110][1] = "Iso";
		TypeTable[111][0] = ".dmg";
		TypeTable[111][1] = "Iso";
		TypeTable[112][0] = ".dmz";
		TypeTable[112][1] = "Iso";
		TypeTable[113][0] = ".img";
		TypeTable[113][1] = "Iso";
		TypeTable[114][0] = ".iso";
		TypeTable[114][1] = "Iso";
		TypeTable[115][0] = ".mdf";
		TypeTable[115][1] = "Iso";
		TypeTable[116][0] = ".mds";
		TypeTable[116][1] = "Iso";
		TypeTable[117][0] = ".nrg";
		TypeTable[117][1] = "Iso";
		TypeTable[118][0] = ".sub";
		TypeTable[118][1] = "Iso";
		TypeTable[119][0] = ".toast";
		TypeTable[119][1] = "Iso";
		TypeTable[120][0] = ".bat";
		TypeTable[120][1] = "Pro";
		TypeTable[121][0] = ".cmd";
		TypeTable[121][1] = "Pro";
		TypeTable[122][0] = ".com";
		TypeTable[122][1] = "Pro";
		TypeTable[123][0] = ".exe";
		TypeTable[123][1] = "Pro";
		TypeTable[124][0] = ".qt";
		TypeTable[124][1] = "Video";
		TypeTable[125][0] = ".rm";
		TypeTable[125][1] = "Video";
		TypeTable[126][0] = ".rv";
		TypeTable[126][1] = "Video";
		TypeTable[127][0] = ".ts";
		TypeTable[127][1] = "Video";
		TypeTable[128][0] = ".asf";
		TypeTable[128][1] = "Video";
		TypeTable[129][0] = ".avi";
		TypeTable[129][1] = "Video";
		TypeTable[130][0] = ".m1v";
		TypeTable[130][1] = "Video";
		TypeTable[131][0] = ".m2v";
		TypeTable[131][1] = "Video";
		TypeTable[132][0] = ".mkv";
		TypeTable[132][1] = "Video";
		TypeTable[133][0] = ".mov";
		TypeTable[133][1] = "Video";
		TypeTable[134][0] = ".mpe";
		TypeTable[134][1] = "Video";
		TypeTable[135][0] = ".mpg";
		TypeTable[135][1] = "Video";
		TypeTable[136][0] = ".mps";
		TypeTable[136][1] = "Video";
		TypeTable[137][0] = ".mpv";
		TypeTable[137][1] = "Video";
		TypeTable[138][0] = ".ogm";
		TypeTable[138][1] = "Video";
		TypeTable[139][0] = ".ram";
		TypeTable[139][1] = "Video";
		TypeTable[140][0] = ".rv9";
		TypeTable[140][1] = "Video";
		TypeTable[141][0] = ".vob";
		TypeTable[141][1] = "Video";
		TypeTable[142][0] = ".wmv";
		TypeTable[142][1] = "Video";
		TypeTable[143][0] = ".divx";
		TypeTable[143][1] = "Video";
		TypeTable[144][0] = ".mp1v";
		TypeTable[144][1] = "Video";
		TypeTable[145][0] = ".mp2v";
		TypeTable[145][1] = "Video";
		TypeTable[146][0] = ".mpeg";
		TypeTable[146][1] = "Video";
		TypeTable[147][0] = ".mpv1";
		TypeTable[147][1] = "Video";
		TypeTable[148][0] = ".mpv2";
		TypeTable[148][1] = "Video";
		TypeTable[149][0] = ".vivo";
		TypeTable[149][1] = "Video";
		TypeTable[150][0] = ".xvid";
		TypeTable[150][1] = "Video";
		TypeTable[151][0] = ".apk";
		TypeTable[151][1] = "Pro";
	}

	public String GetFileExtName(String fileName) {
		String strExtName = "";
		/* ȡ���ļ���չ�� */
		int nPos = fileName.lastIndexOf(".");
		if (nPos >= 0) {
			strExtName = fileName.substring(nPos);
		}
		return strExtName;
	}

	public String GetFileType(String fileName) {
		String fileType = "";
		/* ȡ���ļ���չ�� */
		String strExtName = GetFileExtName(fileName);
		for (int i = 0; i < TypeTable.length; i++) {
			if (strExtName.compareToIgnoreCase(TypeTable[i][0]) == 0) {
				fileType = TypeTable[i][1];
				break;
			}
		}
		return fileType;
	}

	/**
	 * @param args
	 */
	public boolean Hash() {
		try {
			File file = new File(strFullPath);
			/* ���ļ�ʧ�� */
			if (file == null) {
				return false;
			}
			/* ����ļ������ڷ���ʧ�� */
			if (!file.exists()) {
				return false;
			}
			/* ������ļ��򷵻�ʧ�� */
			if (!file.isFile()) {
				return false;
			}
			/* ȡ���ļ����� */
			strFileName = file.getName();
			strFilePath = file.getPath();
			strFileType = GetFileType(strFileName);
			nFileSize = file.length();
			nPartCount = (int) ((nFileSize + nPartSize - 1) / nPartSize);
			/* ���PART��Ŀ���������򷵻�ʧ�� */
			if (nPartCount > nMaxPartSize || nPartCount <= 0) {
				return false;
			}
			/* ��ʼ��PART HASH���� */
			iPartHash = new byte[nPartCount][16];
			/* ���ļ� */
			FileInputStream fp = new FileInputStream(strFullPath);
			/* ���ļ�ʧ�� */
			if (fp == null) {
				return false;
			}
			/* �����ļ������� */
			byte[] buffer = new byte[nBlockSize];
			/* ��ÿһ��PART����HASH */
			for (int nPart = 0; nPart < nPartCount; nPart++) {
				MD4 md4 = new MD4();
				for (int nBlock = 0; nBlock < nPartSize / nBlockSize; nBlock++) {
					/* �����ļ����� */
					int len = fp.read(buffer);
					/* ������ʧ�����˳� */
					if (len <= 0) {
						break;
					} else {
						md4.add(buffer, len);
					}
				}
				md4.finish();
				/* ����PART HASH */
				iPartHash[nPart] = md4.gethash();
			}

			if (nPartCount > 1 || nFileSize == nPartSize) {
				/* ����PART HASH������ */
				int nHashsSize = nPartCount * 16;
				if (nFileSize % nPartSize == 0) {
					nHashsSize += 16;
				}
				byte[] hashbuff = new byte[nHashsSize];
				/* ����PART HASH */
				for (int nPart = 0; nPart < nPartCount; nPart++) {
					copy16byte(hashbuff, nPart * 16, iPartHash[nPart]);
				}
				/* ����ļ������ǽڵ����� */
				if (nFileSize % nPartSize == 0) {
					MD4 md4a = new MD4();
					md4a.add(null, 0);
					md4a.finish();
					copy16byte(hashbuff, nPartCount * 16, md4a.gethash());
				}
				/* �����ļ�HASH */
				MD4 md4 = new MD4();
				md4.add(hashbuff, nHashsSize);
				md4.finish();
				strFileHash = byte2hex(md4.gethash());
			} else {
				strFileHash = byte2hex(iPartHash[0]);
			}


			/* ���سɹ� */
			return true;
		} catch (FileNotFoundException e) {
		} catch (NullPointerException e) {
		} catch (IOException e) {
		}

		return false;
	}

	public void copy16byte(byte[] out, int offset, byte in[]) {
		for (int i = 0; i < 16; i++) {
			out[offset + i] = in[i];
		}
	}

	public String byte2hex(byte[] b) // ������ת�ַ�
	{
		String hs = "";
		String stmp = "";
		for (int k = 0; k < b.length; k++) {
			stmp = (java.lang.Integer.toHexString(b[k] & 0XFF));
			if (stmp.length() == 1)
				hs += "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public long getNFileSize() {
		return nFileSize;
	}

	public String getStrFileName() {
		return strFileName;
	}

	public String getStrHash() {
		return strFileHash;
	}

	public String getStrFullPath() {
		return strFullPath;
	}

	public void setStrFullPath(String strFullPath) {
		this.strFullPath = strFullPath;
	}

	public String getStrFileType() {
		return strFileType;
	}

	public String getStrFilePath() {
		return strFilePath;
	}


	
}
