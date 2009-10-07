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
import java.util.ArrayList;

import wolf.project.woltest;
import android.util.Log;

//public class FsUnit {
public class FsUnit implements FSConstant {
	private static final String tag = "FsUnit";

	private static int stepNumber;

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
		case STEP_REQUEST_DIR:
			requestDir();
			break;
		case STEP_REQUEST_FILE:
			requestFile();
			break;
		case STEP_CREATE_DIR:
			createDir();
		case STEP_CLOSE:
			close();
		}
	}

	void init() {
		//안드로이드 폰의 서버 소켓 확보
		try {
			androidSocket = new ServerSocket(androidPortNumber);
			androidSocket.setSoTimeout(50000);  // ?
		} catch (IOException e1) { 
			// do nothing
		}

		// woltest.get_selec_IP <- 서버의 ip정보		
		// 서버 주소 확보
		// TODO : 서버 확보 될 때까지 기다리기 -nov
		try {
			serverAddress = InetAddress.getByName(woltest.get_selec_IP);
		} catch (UnknownHostException e2) {
			// do nothing
		}				 
	}


	void requestDir() {		
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			
			// dirPath 보내기
			sendSocket = new Socket(serverAddress, serverPortNumber);			
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));
			out.println("" + STEP_REQUEST_DIR + " " + FsList.dirPath+"\n"); 
			out.flush();
			sendSocket.close();
			
			
			// receiveSocket으로 대기
						
			receiveSocket = androidSocket.accept();
			Log.d(tag,"accept");
			in = new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));	
					
			int fsListCount = Integer.valueOf(in.readLine());
			
						
			ArrayList<String> arrayFiles = new ArrayList<String>();
			
			FsList.arrayFsList.clear();
			FsList.fsDirCount = 0;
			
			/*
			 * 			String strFromAndroid[] = in.readLine().split(" ", 2);
			 * 			if (strFromAndroid[0].equals(new String("1."))) {
				//dirlist
				dirList(strFromAndroid[1]);
			}

			 * 
			 * 
			 */
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
					arrayFiles.add(strTemp[1]);
				}				
			}
			FsList.arrayFsList.addAll(arrayFiles);
			receiveSocket.close();			
		} catch(IOException e) {
			Log.d(tag, "what'up" + e.toString());
			//do nothing
		}		
	}
	
	void createDir() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			
			// dirPath 보내기
			sendSocket = new Socket(serverAddress, serverPortNumber);			
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream())));
			out.println("" + STEP_CREATE_DIR + " " + FsList.dirPath+"\n"); 
			out.flush();
			sendSocket.close();			
		} catch(IOException e) {
			Log.d(tag, "createdir err");
			// do nothing
		}
		requestDir();

	}

	void requestFile() {

	}

	void close() {
		//안드로이드 폰의 소켓 닫기
		try {
			androidSocket.close();
		} catch (IOException e) {
			// do nothing
		}
	}
	/*
	 * 0. 초기화(tcp 설정)
	 * 1. a->c dir 요청
	 * 2. c->a 내용 응답
	 * 3. a->c file 카피 요청
	 * 4. c->a file 전송
	 * 5. 종료(tcp 해제)
	 */

}
