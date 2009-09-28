/* TODO:프로그램 실행시키고 트라이 아이콘으로 넣기
 * parser
 * 
 * send dirlist
 * 
 * send file
 */

package wolf.server;

import java.io.*;
import java.net.*;

class ServerApp {
	private int serverPortNumber = 12312;
	private int androidPortNumber = 12313;	
	private ServerSocket serverSocket = null;	
	private InetAddress androidAddress = null;
	
	
	public ServerApp() {
		initSocket(); //once
		waitForAndroid();	//many - times  or  not 
	}
	void initSocket() {
		//기본포트(12312)로 열고 실패하면 포트넘버 증가
		while(true) {
			boolean checkPort = true;
			try {
				serverSocket = new ServerSocket(serverPortNumber);
			} catch (IOException e) {
				// 해당 포트가 열려 있는 경우
				checkPort = false;
				serverPortNumber ++;				
			}			
			if (checkPort) break;
		}		
		//소켓 준비 완료
		System.out.println("serverPortnumber = " + serverPortNumber);		
	}
	void waitForAndroid() {
		try {
			Socket receiveSocket = serverSocket.accept(); // wolf로부터 정보 대기중
			//androidAddress = receiveSocket.getInetAddress();
			androidAddress = InetAddress.getByName("127.0.0.1");
			//androidPortNumber = receiveSocket.getPort();
			System.out.println("address : " + androidAddress);
			System.out.println("portNumber : " + androidPortNumber);			
			System.out.println("접속자 정보 : " + receiveSocket.toString());
			
			BufferedReader in = new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));

			String strFromAndroid[] = in.readLine().split(" ", 2);

			//testcode
			System.out.println("strFromAndroid.length = " + strFromAndroid.length);
			for(int i=0;i<strFromAndroid.length;i++)
				System.out.println(strFromAndroid[i]);
			System.out.println("----------");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				System.out.println("Thread.sleep error");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//testcode end

			
			receiveSocket.close();
			if (strFromAndroid[0].equals(new String("1."))) {
				//dirlist
				dirList(strFromAndroid[1]);								
			}
			else if (strFromAndroid[1].equals(new String("3."))) {
				//file
				fileTransfer(strFromAndroid[1]);
			}
						
		} catch(IOException e) {			
			// do nothing
		}
	}

	void dirList(String dirName) {
		File F = new File(dirName);
		File [] Fl = F.listFiles();
		/*
			System.out.println(F.isFile());
			System.out.println(F.isDirectory());
			System.out.println(Fl.length);
			int k = Fl.length;
			String str = "";
		 */

		try {			
			//Socket sendSocket = new Socket(androidAddress, androidPortNumber);
			Socket sendSocket = new Socket(androidAddress, androidPortNumber);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));
			
			System.out.println("sendSocket.toString = " + sendSocket.toString());
			
			for(int i=0;i<Fl.length;i++)
			{	
				out.println(Fl[i].getName());
				out.flush();
				System.out.println(Fl[i].getName());			
				//str += Fl[i].getName() + " ";		
			}		
			sendSocket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void fileTransfer(String fileName) {

	}
}

public class Server {
	public static void main(String[] args) {
		ServerApp sa = new ServerApp();		
	}
}
