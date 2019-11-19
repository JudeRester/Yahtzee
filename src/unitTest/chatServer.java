package unitTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class chatServer {
	public static final int PORT = 8888;
	
	public static void main(String[] args) {
		ServerSocket server = null;
		List<PrintWriter> listWiters = new ArrayList<>();
		
		try {
			server = new ServerSocket();
			
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			server.bind(new InetSocketAddress(hostAddress, PORT));
			System.out.println("server -"+Thread.currentThread().getId());
			System.out.println("연결 기다림 -"+hostAddress+":"+PORT);
			
			while(true) {
				Socket socket = server.accept();
				System.out.println("connected");
				new ChatServerProcessThread(socket,listWiters).start();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(server!=null&&!server.isClosed()) {
					server.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}
