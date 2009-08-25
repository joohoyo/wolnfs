package wolf.project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class woltest extends Activity {
	private EditText mCommentText;
	private EditText mIpText;
	private EditText mMacText;
	private Long mRowId;
	private EventsData mDbHelper;
	
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		CharSequence cs = "start ";
		

		//CharSequence cs2 = ipAddr2;


		String ipAddr = "192.168.0.4";
		//String ipAddr = "255.255.255.255";
		String macAddr = "00-0C-F1-6F-C9-4D";
		int port = 28002;
		byte[] macBytes = new byte[6];
		DatagramSocket socket = null;
		InetAddress host = null;		
		int i;
		StringTokenizer tokenizer = new StringTokenizer(macAddr, "-");		

		try {
			host = InetAddress.getByName(ipAddr);
		} catch (UnknownHostException euhe)
		{
			cs = cs + "UnknownHostException ";
			//doing something 
		}

		for(i=0;i<6;i++)
		{
			String byteToken = tokenizer.nextToken();
			macBytes[i] = (byte)Integer.parseInt(byteToken, 16);
		}


		try {
			socket = new DatagramSocket();			
		} catch(SocketException ese)
		{
			cs = cs + "SocketException ";
			//doing something
		}

		byte[] wakeupFrame = new byte[6 + 16 * macBytes.length];

		Arrays.fill(wakeupFrame, 0, 6, (byte)0xFF);

		for(i=6;i<wakeupFrame.length;i+= macBytes.length)
		{
			System.arraycopy(macBytes, 0, wakeupFrame, i, macBytes.length);
		}	

		DatagramPacket packet = new DatagramPacket(wakeupFrame, wakeupFrame.length, host, port);
		try {
			socket.send(packet);
		} catch (IOException eioe)
		{
			cs = cs + "IOException ";
			//doing something			
		}

		cs = cs + " :GOOD" + ipAddr;

		TextView tv = new TextView(this);
		tv.setText(cs);
		setContentView(tv);  


	}
}


