import java.util.Observable;

public class GameState extends Observable {

	GameState() {
		super();
	}

	void changeData(Object data) {
		// The two methods of Observable class
		setChanged();
		notifyObservers(data);
	}

}
