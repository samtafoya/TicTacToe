import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

public class ComputerPlayer implements Observer {

	@Override
	public void update(Observable o, Object data) {
		if ((String) data == "1") {
			if (JOptionPane.showConfirmDialog(null,
					"You have lost! " + "Play again?", "Loser!",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
		
	}

}
