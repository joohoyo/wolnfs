package wolf.server;

import java.awt.*;
import java.awt.event.*;

public class ServerTrayIcon {
	// 트레이 아이콘의 초기설정을 해줍니다.
	private String strTrayTitle = "WOLF";
	private SystemTray systemTray = SystemTray.getSystemTray();
	private TrayIcon trayIcon;

	public ServerTrayIcon() {
		// 트레이 아이콘의 아이콘 역할을 할 이미지 입니다. 
		Image image = Toolkit.getDefaultToolkit().getImage("images/tray_icon.jpg");

		// Popup Menu 설정
		ActionListener exitListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exiting...");
                System.exit(0);
            }
        };

		PopupMenu popup = new PopupMenu();
        MenuItem defaultItem = new MenuItem("Exit");
        defaultItem.addActionListener(exitListener);
        popup.add(defaultItem);

		// TrayIcon을 생성합니다.		
        trayIcon = new TrayIcon(image, strTrayTitle, popup);
        trayIcon.setImageAutoSize(true);

		try	{
			systemTray.add(trayIcon);
		} 
		catch (AWTException e1)	{
			// do nothing
		}
	}
}
