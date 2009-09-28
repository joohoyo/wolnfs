/* TODO:���α׷� �����Ű�� Ʈ���� ���������� �ֱ�
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
		//�⺻��Ʈ(12312)�� ���� �����ϸ� ��Ʈ�ѹ� ����
		while(true) {
			boolean checkPort = true;
			try {
				serverSocket = new ServerSocket(serverPortNumber);
			} catch (IOException e) {
				// �ش� ��Ʈ�� ���� �ִ� ���
				checkPort = false;
				serverPortNumber ++;				
			}			
			if (checkPort) break;
		}		
		//���� �غ� �Ϸ�
		System.out.println("serverPortnumber = " + serverPortNumber);		
	}
	void waitForAndroid() {
		try {
			Socket receiveSocket = serverSocket.accept(); // wolf�κ��� ���� �����
			//androidAddress = receiveSocket.getInetAddress();
			androidAddress = InetAddress.getByName("127.0.0.1");
			//androidPortNumber = receiveSocket.getPort();
			System.out.println("address : " + androidAddress);
			System.out.println("portNumber : " + androidPortNumber);			
			System.out.println("������ ���� : " + receiveSocket.toString());
			
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
