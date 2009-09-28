package wolf.filesystem;


import java.io.BufferedReader;
import java.io.BufferedWriter;
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

//public class FsUnit {
public class FsUnit {
	private static final String tag = "FsUnit";

	private static int stepNumber;
	private static int serverPortNumber = 12312;
	private static int androidPortNumber = 12313; 
	private static final int STEP_INIT = 0;
	private static final int STEP_REQUEST_DIR = 1;
	private static final int STEP_REQUEST_FILE = 3;
	private static final int STEP_CLOSE = 5;

	public InetAddress serverAddress = null;
	public Socket receiveSocket = null;
	
	public Socket sendSocket = null;

	public BufferedReader in = null;

	void step(int stepNumber) {
		this.stepNumber = stepNumber;
		switch(stepNumber) {
		case STEP_INIT:
			init();
			break;
		case STEP_REQUEST_DIR:
			requestDir();
			break;
		case STEP_REQUEST_FILE:
			requestFile();
			break;
		case STEP_CLOSE:
			close();
		}
	}

	void init() {
		//woltest.get_selec_IP <- ������ ip����

		//�ּ� Ȯ��
		try {
			serverAddress = InetAddress.getByName(woltest.get_selec_IP);
		} catch (UnknownHostException e2) {
			// do nothing
		}
		//����Ȯ��
		int iteration = 1;
		while(true) {
			if (isPortNumber(iteration)) break;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// do nothing
			}
			iteration++;
		}
		
		//������� ���� ���� ����		
	}

	boolean isPortNumber(int iteration) {
		//�ʿ������� �𸣰����� �������� �⺻��Ʈ�ѹ�(12312)�� �����Ͽ������
		//���ʴ�� ���� ��Ű�鼭 �˻��Ѵ�. 1, 12, 123 �̷�������		
		for(int i=0;i<iteration;i++)
		{
			boolean checkServer = true;
			try {			
				sendSocket = new Socket(serverAddress, serverPortNumber+i);
			} catch (IOException e1) {
				checkServer = false;
				// ���� ������ �غ���� �ʾ���
				// ��� ���� ������� ���� ������
			}
			if (checkServer) {
				serverPortNumber += i;
				return true;
			}
		}		
		return false;
	}

	void requestDir() {
		ServerSocket androidSocket = null;
		Socket so = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));

			out.println("1. "+FsList.dirPath+"\n"); 
			out.flush();

			//androidPortNumber = sendSocket.getLocalPort();
			sendSocket.close();
			Log.d(tag,"println(1.dirpath) portNumber:"+androidPortNumber);
			
			androidSocket = new ServerSocket(androidPortNumber);
			androidSocket.setSoTimeout(5000);

			Log.d(tag,"println(1.dirpath) portNumber:"+androidPortNumber);
			so = androidSocket.accept();
			Log.d(tag,"accept");
			in = new BufferedReader(new InputStreamReader(so.getInputStream()));	

			//TextView t = (TextView) Activity.findViewById(R.id.FS_TextView_DIR);
			
			String strAll = null;
			/*
			while(true) {
			
				String str = in.readLine();
				if (str.equals(new String("\n"))) {
					break;
				}
				strAll += str;				
			}
			 */
			int debugi=0;
			
			while(true) {
				
				debugi++;
				
				String str = in.readLine();
				Log.d(tag,debugi + str);
				if (str == null) {
					break;
				}
				FsList.fsList = FsList.fsList + str + "\n";				
			}
			so.close();
			
		} catch(IOException e) {
			Log.d(tag, "what'up");
			//do nothing
		}
		
		
		
	}

	void requestFile() {

	}

	void close() {
		//out.close();
	}
	/*
	 * 0. �ʱ�ȭ(tcp ����)
	 * 1. a->c dir ��û
	 * 2. c->a ���� ����
	 * 3. a->c file ī�� ��û
	 * 4. c->a file ����
	 * 5. ����(tcp ����)
	 */

}
