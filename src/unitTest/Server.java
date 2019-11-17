package unitTest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class Server {
	HashMap<String, HashMap<String, ServerRecThread>> globalMap;

	class ServerRecThread extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		String name = "";
		String loc = "";
		String toNameTmp = null;
		String fileServerIP;
		String filePath;
		boolean chatMode;

		public ServerRecThread(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public String showuserList() {
			StringBuilder output = new StringBuilder("==접속기록===\r\n");
			Iterator it = globalMap.get(loc).keySet().iterator();
			while (it.hasNext()) {
				try {
					String key = (String) it.next();
					if (key.equals(name)) {
						key += " (*) ";
					}
					output.append(key + "\r\n");
				} catch (Exception e) {

				}
			}
			System.out.println(output.toString());
			return output.toString();
		}
		
		public String[] getMsgParse(String msg) {
			System.out.println("msgParse():msg?"+msg);
			String[] tmpArr=msg.split("[|]");
			return tmpArr;
		}
		
		@Override
		public void run() {
			HashMap<String, ServerRecThread> clientMap=null;
			try {
				while(in!=null) {
					String msg=in.readUTF();
					String[] msgArr=getMsgParse(msg.substring(msg.indexOf("|")+1));
					
					if(msg.startsWith("req_logon")) {
						if(!(msgArr[0].trim().equals("")&&!isNameGlobal(msgArr[0]))) {
							name = msgArr[0];
							MultiServer.connUserCount++;
							out.writeUTF("logon#yes|"+getEachMapSize());
						}else {
							out.writeUTF("logon#no|err01");
						}
					}else if(msg.startsWith("req_enterRoom")){
						loc=msgArr[1];
						
						if(isNameGlobal(msgArr))
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				if(clientMap!=null) {
					clientMap.remove(name);
					sendGroupMsg(loc,"##" + name+"님이 퇴장");
					System.out.println("##현제 서버에 접속된 유저는"+(--MultiServer.connUserCount)+"명 입니다.");
					
				}
			}
		}
	}
}
