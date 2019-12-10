import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

public class HumanPlayer implements Observer {
	
	//ControllerWindow cw = new ControllerWindow();
	
	public HumanPlayer() {
		
	}

	@Override
	public void update(Observable o, Object data) {
		if ((String) data == "0") {
			System.out.println("you won!");
			if (JOptionPane.showConfirmDialog(null,
					"You have won! " + "Play again?", "Winner!",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}

	}

}
