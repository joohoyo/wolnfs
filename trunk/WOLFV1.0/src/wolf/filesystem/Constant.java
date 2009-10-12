package wolf.filesystem;

import android.view.Menu;

public interface Constant {	
	public int serverPortNumber = 12312;
	public int androidPortNumber = 12311;
	
	// 누구의 Activity가 활성화 되어 있나요
	public int FS = 1;
	public int AND = 2;
	
	// Menu & DIALOG
	public int MENU_CREATE_DIR = Menu.FIRST;
	
	public int CONTEXT_MENU_ITEM_DELETE = Menu.FIRST + 1;
	public int CONTEXT_MENU_ITEM_COPY = Menu.FIRST + 2;
	public int CONTEXT_MENU_ITEM_PLAY = Menu.FIRST + 3;
	
	public int DIALOG_CREATE_DIR = 1;
	public int DIALOG_DELETE = 2;
	public int DIALOG_COPY = 3;
	
	//STEP
	public int STEP_INIT = 0;	
	
	// Android, Server 
	public int STEP_DELETE = 10;
		
	// Android -> Server
	public int STEP_REQUEST_DIR = 20;	
	public int STEP_CREATE_DIR = 21;
		
	// Android -> Android
	public int STEP_SHOW_DIR = 30;   
	
	// Server <-> Android
	public int STEP_COPY_FROM_SERVER = 40;
	public int STEP_COPY_FROM_ANDROID = 41;
	

	public int STEP_CLOSE = 50;
	
	
}
