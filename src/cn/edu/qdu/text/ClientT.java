package cn.edu.qdu.text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientT {

	static DataInputStream dis = null;

	public static void main(String[] args) {
		Socket socket = null;
		DataOutputStream dos = null;
		String s = null;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);

		try {
			socket = new Socket("127.0.0.1", 8088);
			do {
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
				// 匿名内部类：显示服务端信息
				new Thread() {
					@Override
					public void run() {
						try {
							String str = null;
							if ((str = dis.readUTF()) != null) {//java.net.SocketException: socket closed
								System.out.println(str);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				s = input.nextLine();
				dos.writeUTF(s);
			} while (!s.equals("88"));
			System.out.println("服务器停止");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭
			try {
				dis.close();
				dos.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
