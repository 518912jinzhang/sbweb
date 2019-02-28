package com.sbweb.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private static PrintWriter pw = null;
	private static BufferedReader br = null;
	private static Socket s;
	static Scanner scanner = new Scanner(System.in);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket s = new Socket("www.suixianyunwei.top", 23859);
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			InputStream is = s.getInputStream();
			while (true) {
				System.out.println("Client端请输入");
				String str = scanner.next();
				str = "BIDE";
				pw.print((str));
				pw.flush();
				// String string=br.readLine();
				byte b[] = new byte[1024];
				byte id[] = new byte[4];
				int len = is.read(b);
				System.out.println("server len:" + len);
				// System.out.println("server send1:"+ndtt.ByteArrayToInt(b));
				System.out.println("server send1:" + new String(b));

				System.arraycopy(b, 2, id, 0, 4);
				System.out.println("server id len:" + id.length);
				System.out.println("server send2:" + byteArrayToInt(id));

				// 获取经度
				byte[] latLen = new byte[4];
				System.arraycopy(b, 6, latLen, 0, 4);
				int lenLat = byteArrayToInt(latLen);
				byte[] latStr = new byte[lenLat];
				System.arraycopy(b, 10, latStr, 0, lenLat);
				System.out.println("server send3 lat:" + new String(latStr));
				// 获取纬度
				byte[] lonLen = new byte[4];
				System.arraycopy(b, 10 + lenLat, lonLen, 0, 4);
				int lenLon = byteArrayToInt(lonLen);
				byte[] lonStr = new byte[lenLon];
				System.arraycopy(b, 10 + lenLat + 4, lonStr, 0, lenLon);
				System.out.println("server send4 lon:" + new String(lonStr));

				int prate = br.read();
				System.out.println("prate" + prate);
				if (str.equals("exit")) {
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			br.close();
			pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void toBinary(String str) {
		char[] strChar = str.toCharArray();
		String result = "";
		for (int i = 0; i < strChar.length; i++) {
			result += Integer.toBinaryString(strChar[i]) + " ";
		}
		System.out.println(result);
	}

	
	public static int byteArrayToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}
}
