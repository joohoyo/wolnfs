package wolf.server;

import java.io.*;
import java.net.*;

public class Server {
	
	private static int portNumber = 12312;
	private static ServerSocket ss = null;
	private static Socket soc = null;
	
	public static void main(String[] args) {
		//���α׷� �����Ű�� Ʈ���� ���������� �ֱ�
		
		
		//�⺻��Ʈ(12312)�� ���� �����ϸ� ��Ʈ�ѹ� ����
		while(true) {
			boolean checkPort = true;
			try {
				ss = new ServerSocket(portNumber);
			} catch (IOException e) {
				// �ش� ��Ʈ�� ���� �ִ� ���
				checkPort = false;
				portNumber ++;				
			}			
			if (checkPort) break;
		}
		
		System.out.println("portnumber = " + portNumber);
		try {
			soc = ss.accept(); // wolf�κ��� ���� �����
			
			System.out.println("������ ���� : " + soc.toString());			
			BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			
			String strFromAndroid[] = in.readLine().split(" ");
						
			//testcode
			for(int i=0;i<strFromAndroid.length;i++)
				System.out.println(strFromAndroid[0]);
			System.out.println("----------");
			//testcode end
			
			if (strFromAndroid[0].equals(new String("1."))) {
				//dirlist
				dirList(strFromAndroid[1]);								
			}
			else if (strFromAndroid[1].equals(new String("3."))) {
				//file
				fileTransfer(strFromAndroid[1]);
			}
			
			
			soc.close();
		} catch(IOException e) {			
			// do nothing
		}
	}
	
	static void fileTransfer(String fileName) {
		
	}
	
	static void dirList(String dirName) {
		File F = new File(dirName);
		File [] Fl = F.listFiles();
		/*
		System.out.println(F.isFile());
		System.out.println(F.isDirectory());
		System.out.println(Fl.length);
		int k = Fl.length;
		String str = "";
		*/
		InetAddress ia = soc.getInetAddress();		
		Socket sendSocket = null;
		PrintWriter out = null;
		try {
			sendSocket = new Socket(ia,portNumber);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		for(int i=0;i<Fl.length;i++)
		{	
			out.println(Fl[i].getName()+"\n");
			System.out.println(Fl[i].getName());			
			//str += Fl[i].getName() + " ";		
		}
		out.println("\n");
		out.close();
		//System.out.println("str = " + str);
	}
	/*
	 * parser
	 * 
	 * send dirlist
	 * 
	 * send file
	 */
}
