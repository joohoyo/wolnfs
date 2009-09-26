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

import wolf.project.R;
import wolf.project.woltest;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

//public class FsUnit {
public class FsUnit extends Activity {
	private static final String tag = "FsUnit";

	private static int stepNumber;
	private static int portNumber = 12312;
	private static final int STEP_INIT = 0;
	private static final int STEP_REQUEST_DIR = 1;
	private static final int STEP_REQUEST_FILE = 3;
	private static final int STEP_CLOSE = 5;
	
	public InetAddress ia = null;
	public Socket soc = null;
	public PrintWriter out = null;
	
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
			ia = InetAddress.getByName(woltest.get_selec_IP);
		} catch (UnknownHostException e2) {
			// do nothing
		}
		
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
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
		} catch (IOException e) {
			// do nothing
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
				soc = new Socket(ia, portNumber+i);
			} catch (IOException e1) {
				checkServer = false;
				// ���� ������ �غ���� �ʾ���
				// ��� ���� ������� ���� ������
			}
			if (checkServer) return true;
		}		
		return false;
	}
	
	void requestDir() {
		ServerSocket ss = null;
		Socket so = null;
		BufferedReader in = null;
		try {
			ss = new ServerSocket(portNumber);
			out.println("1. "+FsList.dirPath+"\n");
			out.flush();

			Log.d(tag,"println(1.dirpath)");
			so = ss.accept();
			Log.d(tag,"accept");
			in = new BufferedReader(new InputStreamReader(so.getInputStream()));	
			
			TextView t = (TextView) findViewById(R.id.FS_TextView_DIR);
			String strAll = null;
			while(true) {
				String str = in.readLine();
				if (str.equals(new String("\n"))) {
					break;
				}
				strAll += str;				
			}
			t.setText(strAll);
			
			
		} catch(IOException e) {
			//do nothing
		}
		
		
		
		
	}
	
	void requestFile() {
		
	}
	
	void close() {
		out.close();
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
