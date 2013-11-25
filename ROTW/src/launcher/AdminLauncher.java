package launcher;

import userInterface.server.AdminClient;

public class AdminLauncher {
	public static void main(String[] argv) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				new AdminClient();
			}
		});
	}
}
