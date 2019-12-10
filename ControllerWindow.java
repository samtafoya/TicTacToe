import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Random;

public class ControllerWindow extends JFrame implements ChangeListener, ActionListener {
	private JSlider slider;
	private JButton oButton, xButton;
	private Board board;
	private int lineThickness = 4;
	private Color oColor = Color.BLUE, xColor = Color.RED;
	static final char BLANK = ' ', O = 'O', X = 'X';
	private char position[] = { // Board position (BLANK, O, or X)
			BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK, BLANK };
	private int wins = 0, losses = 0, draws = 0; // game count by user

	// Observable Subject
	GameState gameState;

	// Initialize
	public ControllerWindow(HumanPlayer hPlayer, ComputerPlayer cPlayer) {

		// create observable subject and add observer
		gameState = new GameState();
		gameState.addObserver(hPlayer);
		gameState.addObserver(cPlayer);

		// create the board
		buildBoard();

	}

	public void buildBoard() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(new JLabel("Line Thickness:"));
		topPanel.add(slider = new JSlider(SwingConstants.HORIZONTAL, 1, 20, 4));
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.addChangeListener(this);
		topPanel.add(oButton = new JButton("O Color"));
		topPanel.add(xButton = new JButton("X Color"));
		oButton.addActionListener(this);
		xButton.addActionListener(this);
		add(topPanel, BorderLayout.NORTH);
		add(board = new Board(), BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
	}

	// nested board
	private class Board extends JPanel implements MouseListener {
		private Random random = new Random();
		private int rows[][] = { { 0, 2 }, { 3, 5 }, { 6, 8 }, { 0, 6 }, { 1, 7 }, { 2, 8 }, { 0, 8 }, { 2, 6 } };

		public Board() {
			addMouseListener(this);
		}

		// new board
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int w = getWidth();
			int h = getHeight();
			Graphics2D g2d = (Graphics2D) g;

			// draw grid
			g2d.setPaint(Color.WHITE);
			g2d.fill(new Rectangle2D.Double(0, 0, w, h));
			g2d.setPaint(Color.BLACK);
			g2d.setStroke(new BasicStroke(lineThickness));
			g2d.draw(new Line2D.Double(0, h / 3, w, h / 3));
			g2d.draw(new Line2D.Double(0, h * 2 / 3, w, h * 2 / 3));
			g2d.draw(new Line2D.Double(w / 3, 0, w / 3, h));
			g2d.draw(new Line2D.Double(w * 2 / 3, 0, w * 2 / 3, h));

			// draw x and o
			for (int i = 0; i < 9; ++i) {
				double xpos = (i % 3 + 0.5) * w / 3.0;
				double ypos = (i / 3 + 0.5) * h / 3.0;
				double xr = w / 8.0;
				double yr = h / 8.0;
				if (position[i] == O) {
					g2d.setPaint(oColor);
					g2d.draw(new Ellipse2D.Double(xpos - xr, ypos - yr, xr * 2, yr * 2));
				} else if (position[i] == X) {
					g2d.setPaint(xColor);
					g2d.draw(new Line2D.Double(xpos - xr, ypos - yr, xpos + xr, ypos + yr));
					g2d.draw(new Line2D.Double(xpos - xr, ypos + yr, xpos + xr, ypos - yr));
				}
			}
		}

		// human moves
		public void mouseClicked(MouseEvent e) {
			int xpos = e.getX() * 3 / getWidth();
			int ypos = e.getY() * 3 / getHeight();
			int pos = xpos + 3 * ypos;
			if (pos >= 0 && pos < 9 && position[pos] == BLANK) {
				position[pos] = O;
				repaint();
				putX();
				repaint();
			}
		}

		// Ignore other mouse events
		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		// check if game ended
		void putX() {
			if (won(O)) {
				newGame(O);
			} else if (isDraw()) {
				newGame(BLANK);
			} else {
				nextMove();
				if (won(X)) {
					newGame(X);
				} else if (isDraw()) {
					newGame(BLANK);
				}
			}

		}

		// check if theres a win
		boolean won(char player) {
			for (int i = 0; i < 8; ++i)
				if (testRow(player, rows[i][0], rows[i][1]))
					return true;
			return false;
		}

		boolean testRow(char player, int a, int b) {
			return position[a] == player && position[b] == player && position[(a + b) / 2] == player;
		}

		// draw X in random spot
		void nextMove() {
			int r;
			do
				r = random.nextInt(9);
			while (position[r] != BLANK);
			position[r] = X;
		}
		
		// check if all spots are full, if theres a draw, the human wins
		boolean isDraw() {
			for (int i = 0; i < 9; ++i)
				if (position[i] == BLANK)
					return false;
			return true;
		}

		// start a new game
		void newGame(char winner) {
			repaint();
			String data;
			// send game data to observers
			if (winner == O) {
				data = "0";
				gameState.changeData(data);
				++wins;
			} else if (winner == X) {
				data = "1";
				gameState.changeData(data);
				++losses;
			} else {
				data = "2";
				gameState.changeData(data);
				++draws;
			}

			// clear the board
			for (int j = 0; j < 9; ++j)
				position[j] = BLANK;

			// switch off who starts
			if ((wins + losses + draws) % 2 == 1)
				nextMove();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}
}