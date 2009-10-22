package wolf.filesystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import wolf.project.woltest;
import android.util.Log;

public class Unit implements Constant {
	private static final String tag = "FsUnit";

	
	private static String androidPath = "/";	
	private static String serverPath = "C:\\";

	private int stepNumber;
	private static int onRunning = 0;
	private static int turn = FS;

	private ServerSocket androidSocket = null;
	private InetAddress serverAddress = null;	
	private Socket sendSocket = null;
	private Socket receiveSocket = null;

	void step(int stepNumber) {
		this.stepNumber = stepNumber;
		switch(stepNumber) {
		case STEP_INIT:
			init();
			break;
		// Android, Server
		case STEP_DELETE:
			delete();
			break;
			
		// Android -> Server
		case STEP_REQUEST_DIR:
			requestDir();
			break;
		case STEP_CREATE_DIR:
			createDir();
			break;

		// Android -> Android
		case STEP_SHOW_DIR:
			showDir();
			break;

		// Server <-> Android
		case STEP_COPY_FROM_SERVER:
			copyFile();
			break;
		case STEP_COPY_FROM_ANDROID:
			saveFile();
			break;
			
			
		

		case STEP_CLOSE:
			close();
		}		
	}
	
	// �����̵� ���丮�� delete �ϱ�
	void delete() {	


		File f = null;
		if (getTurn() == FS) {
			BufferedReader in = null;
			PrintWriter out = null;
			try {			
				sendSocket = new Socket(serverAddress, serverPortNumber);			
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));
				out.println("" + STEP_DELETE + " " + serverPath+"\n"); 
				out.flush();
				sendSocket.close();			
			} catch(IOException e) {				
				// do nothing
			}
			
			String tempServerPath = getServerPath();
			int indexOfSlash = tempServerPath.lastIndexOf('\\',tempServerPath.length()-2);
			if (indexOfSlash > 0) {
				tempServerPath = tempServerPath.copyValueOf(tempServerPath.toCharArray(), 0, indexOfSlash+1);				
			}
			setServerPath(tempServerPath);
		}
		else {			
			f = new File(getAndroidPath());
			setAndroidPath(f.getParent());
			f.delete();			
		}

	}

	// Android ���丮 list �����ֱ� 
	void showDir() {
		AndList.clear(); 

		File [] file = (new File(getAndroidPath())).listFiles();          
		for(int i=0;i<file.length;i++) { 
			if (file[i].isDirectory()) 
			{ 
				AndList.andDirCount ++; 
				AndList.arrayAndList.add(file[i].getName()); 
			}                        
			else  
				AndList.arrayAndFiles.add(file[i].getName());                    
		} 
		AndList.arrayAndList.addAll(AndList.arrayAndFiles); 
	}

	void saveFile() {
		
	}
	void copyFile() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {			
			sendSocket = new Socket(serverAddress, serverPortNumber);			
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));
			out.println("" + STEP_COPY_FROM_SERVER + " " + serverPath + FsList.listItemSelected +  "\n"); 
			out.flush();
			out.close();
			sendSocket.close();
			
			// receiveSocket���� ���
			receiveSocket = androidSocket.accept();

			DataInputStream dis = new DataInputStream(
					new BufferedInputStream(
					receiveSocket.getInputStream()));
			
			String strFileName = dis.readLine();

			File f = new File(getAndroidPath() + strFileName);
			f.createNewFile();
			
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(
					new FileOutputStream(f)));
			int c = 0;
			byte[] by = new byte[65535];
			char[] ch = new char[65535];
			while((c = dis.read(by)) != -1) // ���� ����
			{				
				dos.write(by, 0, c);				
			}			
			dos.flush();			
			receiveSocket.close();
		} catch(IOException e) {
			// do nothing
		}
		requestDir();
	}		

	// server ���丮 list �����ֱ�
	void requestDir() {		
		BufferedReader in = null;
		PrintWriter out = null;
		try {

			// dirPath ������
			sendSocket = new Socket(serverAddress, serverPortNumber);			
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));
			out.println("" + STEP_REQUEST_DIR + " " + serverPath+"\n"); 
			out.flush();
			sendSocket.close();

			// receiveSocket���� ���

			receiveSocket = androidSocket.accept();
			in = new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));	

			int fsListCount = Integer.valueOf(in.readLine());

			FsList.clear();

			for(int i=0;i<fsListCount;i++) {					
				String strTemp[] = in.readLine().split(" ", 2);
				if (strTemp == null) {
					break;
				}
				if (strTemp[0].equals(new String("D.")))
				{
					FsList.fsDirCount ++;
					FsList.arrayFsList.add(strTemp[1]);
				}
				else
				{			
					FsList.arrayFiles.add(strTemp[1]);
				}				
			}
			FsList.arrayFsList.addAll(FsList.arrayFiles);
			receiveSocket.close();			
		} catch(IOException e) {
			//do nothing
		}		
	}

	// Folder �����
	void createDir() {
		BufferedReader in = null;
		PrintWriter out = null;
		if (turn == FS) {
			try {			
				// dirPath ������
				sendSocket = new Socket(serverAddress, serverPortNumber);			
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));
				out.println("" + STEP_CREATE_DIR + " " + serverPath+"\n");			
				out.flush();
				sendSocket.close();			
			} catch(IOException e) {
				Log.e(tag, "createdir err");
				// do nothing
			}
		}
		else {
			File f = new File(androidPath);
			f.mkdir();
		}
	}

	//�ȵ���̵� ���� ���� ���� Ȯ��
	void init() {
		if (onRunning == 1) return;
		try {
			androidSocket = new ServerSocket(androidPortNumber);
			androidSocket.setSoTimeout(50000);  
		} catch (IOException e1) { 
			// do nothing
		}

		// woltest.get_selec_IP <- ������ ip����		
		// ���� �ּ� Ȯ��
		// TODO : ���� Ȯ�� �� ������ ��ٸ��� -nov
		try {
			serverAddress = InetAddress.getByName(woltest.get_selec_IP);
		} catch (UnknownHostException e2) {
			// do nothing
		}		
		onRunning = 1;
	}

	//�ȵ���̵� ���� ���� �ݱ�
	void close() {
		try {
			androidSocket.close();
		} catch (IOException e) {
			// do nothing
		}
	}	

	// server�� android�� path ����
	public String getServerPath() {
		return serverPath;
	}
	public void setServerPath(String serverPath) {		
		if (serverPath.charAt(serverPath.length()-1) != '\\') {
			serverPath = serverPath + '\\';
		}
		this.serverPath = serverPath;
	}
	public String getAndroidPath() {
		return androidPath;
	}
	public void setAndroidPath(String androidPath) {
		if (androidPath.charAt(androidPath.length()-1) != '/') {
			androidPath = androidPath + '/';
		}
		this.androidPath = androidPath;
	}
	// Ȱ��ȭ�Ǿ� �ִ� Activity�� ����?
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}	
}
