/* TODO:프로그램 실행시키고 트라이 아이콘으로 넣기
 * parser
 * 
 * send dirlist
 * 
 * send file
 */

package wolf.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class ServerApp {
	private int serverPortNumber = 12312;
	private int androidPortNumber = 12311;
	private ServerSocket serverSocket = null;	
	private InetAddress androidAddress = null;

	//STEP
	private final int STEP_INIT = 0;
	// Android, Server 
	private final int STEP_DELETE = 10;
	// Android -> Server
	private final int STEP_REQUEST_DIR = 20;	
	private final int STEP_CREATE_DIR = 21;
	// Android -> Android
	private final int STEP_SHOW_DIR = 30;
	// Server <-> Android
	private final int STEP_COPY_FILE = 40;
	// Close
	private final int STEP_CLOSE = 50;

	public ServerApp() {
		initSocket(); //once
		while(true) {
			waitForAndroid();	//many - times  or  not	
		}

	}
	
	//서버 프로그램 소켓 설정
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
		System.out.println("server = " + serverSocket.toString());		
	}
	
	// wolf로부터 정보 대기 후 파싱
	void waitForAndroid() {  
		try {
			Socket receiveSocket = serverSocket.accept(); // wolf로부터 정보 대기중
			//androidAddress = receiveSocket.getInetAddress();
			androidAddress = InetAddress.getByName("127.0.0.1");	
			System.out.println("접속자 정보 : " + receiveSocket.toString()); ////////

			BufferedReader in = new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));

			String strFromAndroid[] = in.readLine().split(" ", 2);

			//testcode
			System.out.println("strFromAndroid.length = " + strFromAndroid.length);
			for(int i=0;i<strFromAndroid.length;i++) 
				System.out.println(strFromAndroid[i]);
				
			System.out.println("----------");
			
			//testcode end


			receiveSocket.close();
			switch(intFromAndroid(strFromAndroid[0])) {
			case STEP_REQUEST_DIR:
				dirList(strFromAndroid[1]);
				break;
			case STEP_COPY_FILE:
				fileTransfer(strFromAndroid[1]);
				break;
			case STEP_DELETE:
				delete(strFromAndroid[1]);
				break;
			case STEP_CREATE_DIR:
				createDir(strFromAndroid[1]);
				break;			
			}
		} catch(IOException e) {			
			// do nothing
		}
	}	
	private int intFromAndroid(String str) {
		return Integer.valueOf(str);
	}
	
	void createDir(String dirName) {
		File f = new File(dirName);
		f.mkdir();
	}
	void delete(String str) {
		File f = new File(str);
		System.out.println(str);
		f.delete();		
	}
	
	void dirList(String dirName) {		
		File [] file = (new File(dirName)).listFiles();

		try {
			Socket sendSocket = null;
			while(sendSocket == null) 
				sendSocket = new Socket(androidAddress, androidPortNumber);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));

			System.out.println("sendSocket.toString = " + sendSocket.toString());

			out.println(file.length);
			out.flush();			
			for(int i=0;i<file.length;i++)
			{	
				if (file[i].isDirectory()) 
					out.print("D. ");
				else 
					out.print("F. ");				
				out.println(file[i].getName());
				out.flush();							
			}		
			sendSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void fileTransfer(String fileName) {
		File f = new File(fileName);
		
		try {			
			Socket sendSocket = new Socket(androidAddress, androidPortNumber);
			/*
			BufferedReader br = new BufferedReader(
								new FileReader(f));			
			PrintWriter out = new PrintWriter(
							new BufferedWriter(
							new OutputStreamWriter(
							sendSocket.getOutputStream())));
			*/
			DataInputStream dis = new DataInputStream(
					new BufferedInputStream(
					new FileInputStream(f)));
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(
					sendSocket.getOutputStream()));
			System.out.println("sendSocket.toString = " + sendSocket.toString());

			dos.writeBytes(f.getName()+"\n");			
			
			int c = 0;
			byte[] by = new byte[65535];
			char[] ch = new char[65535];
			while((c = dis.read(by)) != -1) //파일 읽고 보내기
			{				
				System.out.println(c);
				dos.write(by, 0, c);
				
			}			
			dos.flush();
			sendSocket.close();
			System.out.println("filetranfer end");
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}

public class Server {
	public static void main(String[] args) {
		ServerTrayIcon sti = new ServerTrayIcon();
		ServerApp sa = new ServerApp();		
	}
}

