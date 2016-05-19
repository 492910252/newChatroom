package cn.edu.qdu.text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerT {
	static List<Client> clientList = new ArrayList<Client>();

	public static void main(String[] args) {
		ServerSocket serversocket = null;
		Socket socket = null;

		try {
			// 启动
			serversocket = new ServerSocket(8088);
			System.out.println("服务器启动！");
			while (true) {
				// 连接
				socket = serversocket.accept();// 进行多次的接受请求
				Client c = new ServerT().new Client(socket);
				clientList.add(c);
				c.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 内部类：搭载通道
	class Client extends Thread {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		Socket socket = null;

		public Client(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				while (true) {
					dos = new DataOutputStream(socket.getOutputStream());
					dis = new DataInputStream(socket.getInputStream());
					String str = null;
					if ((str = dis.readUTF()) != null) {
						System.out.println("客户端口说：" + str);
					}
					// dos.writeUTF(socket.getPort() + "server say" + str);
					for (int i = 0; i < clientList.size(); i++) {
						new DataOutputStream(clientList.get(i).socket.getOutputStream())
								.writeUTF(socket.getPort() + ":" + str);
					}
				}
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
}