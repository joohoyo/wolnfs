package wolf.server;

import java.awt.*;
import java.awt.event.*;

public class ServerTrayIcon {
	// Ʈ���� �������� �ʱ⼳���� ���ݴϴ�.
	private String strTrayTitle = "WOLF";
	private SystemTray systemTray = SystemTray.getSystemTray();
	private TrayIcon trayIcon;

	public ServerTrayIcon() {
		// Ʈ���� �������� ������ ������ �� �̹��� �Դϴ�. 
		Image image = Toolkit.getDefaultToolkit().getImage("images/tray_icon.jpg");

		// Popup Menu ����
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

		// TrayIcon�� �����մϴ�.		
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
