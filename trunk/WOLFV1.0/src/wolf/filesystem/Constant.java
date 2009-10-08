package wolf.filesystem;

public interface Constant {	
	public int serverPortNumber = 12312;
	public int androidPortNumber = 12311;
		
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
