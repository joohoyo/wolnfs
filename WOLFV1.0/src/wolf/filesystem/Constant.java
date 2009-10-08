package wolf.filesystem;

import android.view.Menu;

public interface Constant {	
	public int serverPortNumber = 12312;
	public int androidPortNumber = 12311;
	
	// ������ Activity�� Ȱ��ȭ �Ǿ� �ֳ���
	public int FS = 1;
	public int AND = 2;
	
	// Menu & DIALOG
	public int MENU_CREATE_DIR = Menu.FIRST;
	
	public int CONTEXT_MENU_ITEM_DELETE = Menu.FIRST;
	public int CONTEXT_MENU_ITEM_SEND = Menu.FIRST + 1;
	
	public int DIALOG_CREATE_DIR = 1;
	public int DIALOG_DELETE = 2;

	//STEP
	public int STEP_INIT = 0;	
	
	// Android -> Server
	public int STEP_REQUEST_DIR = 10;	
	public int STEP_CREATE_DIR = 11;
	
	// Android -> Android
	public int STEP_SHOW_DIR = 20;   
	
	
	// Server -> Android
	public int STEP_COPY_FILE = 3;
	

	public int STEP_CLOSE = 5;
	
	
}
