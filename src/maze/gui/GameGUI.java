package maze.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import maze.exceptions.EndGame;
import maze.logic.Maze;
import maze.logic.MovementInfo;
import maze.logic.Maze.DIRECTION;
import maze.logic.Maze.DRAGON_MODE;

import java.awt.SystemColor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameGUI {

	private boolean gameFinished;

	private JFrame GameFrame;
	private MazeGraphicPlay ShowGamePanel;
	private JLabel gameInfoLabel;
	private JButton btnUP;
	private JButton btnLEFT;
	private JButton btnRIGHT;
	private JButton btnDOWN;

	private Maze maze;
	private GUI MenuGUI;


	/**
	 * Create the application.
	 */
	public GameGUI(GUI MenuGUI) {
		this.MenuGUI = MenuGUI;
		gameFinished = false;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		GameFrame = new JFrame();
		GameFrame.getContentPane().setLayout(new BorderLayout());

		int fWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.95);
		int fHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9);
		GameFrame.setSize(new Dimension(fWidth, fHeight));
		GameFrame.setMinimumSize(new Dimension(900, 500));
		GameFrame.setLocationRelativeTo(null);

		GameFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				
				goBack();
			}
		});


		JPanel GamePanel = new JPanel();
		GameFrame.getContentPane().add(GamePanel, BorderLayout.CENTER);

		ShowGamePanel = new MazeGraphicPlay();
		ShowGamePanel.setFocusable(true);
		ShowGamePanel.setBackground(Color.WHITE);


		JPanel sidePanel = new JPanel();
		GroupLayout gl_GamePanel = new GroupLayout(GamePanel);
		gl_GamePanel.setHorizontalGroup(
				gl_GamePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_GamePanel.createSequentialGroup()
						.addComponent(ShowGamePanel, GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(sidePanel, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE))
				);
		gl_GamePanel.setVerticalGroup(
				gl_GamePanel.createParallelGroup(Alignment.LEADING)
				.addComponent(ShowGamePanel, GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
				.addComponent(sidePanel, GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
				);

		gameInfoLabel = new JLabel("");
		gameInfoLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		gameInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);

		btnUP = new JButton("UP");
		btnUP.setBackground(SystemColor.textHighlight);
		btnUP.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		btnUP.setEnabled(true);
		btnUP.setFocusable(false);
		btnUP.setBounds(44, 13, 88, 31);
		buttonPanel.add(btnUP);

		btnLEFT = new JButton("LEFT");
		btnLEFT.setBackground(SystemColor.textHighlight);
		btnLEFT.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		btnLEFT.setEnabled(true);
		btnLEFT.setFocusable(false);
		btnLEFT.setBounds(0, 57, 80, 25);
		buttonPanel.add(btnLEFT);

		btnRIGHT = new JButton("RIGHT");
		btnRIGHT.setBackground(SystemColor.textHighlight);
		btnRIGHT.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		btnRIGHT.setEnabled(true);
		btnRIGHT.setFocusable(false);
		btnRIGHT.setBounds(100, 57, 80, 25);
		buttonPanel.add(btnRIGHT);

		btnDOWN = new JButton("DOWN");
		btnDOWN.setBackground(SystemColor.textHighlight);
		btnDOWN.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		btnDOWN.setEnabled(true);
		btnDOWN.setFocusable(false);
		btnDOWN.setBounds(44, 97, 88, 31);
		buttonPanel.add(btnDOWN);


		GroupLayout gl_sidePanel = new GroupLayout(sidePanel);
		gl_sidePanel.setHorizontalGroup(
				gl_sidePanel.createParallelGroup(Alignment.LEADING)
				.addComponent(gameInfoLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
				.addGroup(gl_sidePanel.createSequentialGroup()
						.addGap(30)
						.addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
						.addGap(27))
				);
		gl_sidePanel.setVerticalGroup(
				gl_sidePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_sidePanel.createSequentialGroup()
						.addGap(85)
						.addComponent(gameInfoLabel, GroupLayout.PREFERRED_SIZE, 16, Short.MAX_VALUE)
						.addGap(107)
						.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
						.addGap(104))
				);
		sidePanel.setLayout(gl_sidePanel);
		GamePanel.setLayout(gl_GamePanel);

		GameFrame.setVisible(false);


		ShowGamePanel.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent k) {

				DIRECTION dir = null;

				switch(k.getKeyCode()){
				case KeyEvent.VK_LEFT: 
					dir = DIRECTION.LEFT; 
					break;

				case KeyEvent.VK_RIGHT: 
					if (!gameFinished)
						dir = DIRECTION.RIGHT; 
					break;

				case KeyEvent.VK_UP: 
					dir = DIRECTION.UP; 
					break;

				case KeyEvent.VK_DOWN: 
					dir = DIRECTION.DOWN; 
					break;

				case KeyEvent.VK_ESCAPE:
					goBack();
					return;

				case KeyEvent.VK_ENTER:
					if (gameFinished) {
						goBack();
					}
					return;

				default:
					return;
				}


				if (!gameFinished)
					moveAction(dir);
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}
		});



		btnUP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveAction(DIRECTION.UP);
			}
		});

		btnLEFT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveAction(DIRECTION.LEFT);
			}
		});

		btnRIGHT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveAction(DIRECTION.RIGHT);
			}
		});

		btnDOWN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveAction(DIRECTION.DOWN);
			}
		});
	}





	protected void processGame() {

		if (gameFinished) {
			try {
				Files.deleteIfExists(new File("save\\maze.dat").toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {

			ObjectOutputStream os = null;

			try {
				os = new ObjectOutputStream(new FileOutputStream("save\\maze.dat"));
				os.writeObject(maze);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				if (os != null)
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}



	public void moveAction(DIRECTION dir) {

		if (!ShowGamePanel.animationAllowed())
			return;

		try {
			maze.update(dir);
		}
		catch (EndGame e){
			finishGame(e);
			return;
		}
		finally {
			ShowGamePanel.updateImage(maze.getMovementInfo());
		}


		if (maze.getDragonList().size() == 0) {
			gameInfoLabel.setText("Proceed to the exit.");
			gameInfoLabel.setForeground(new Color(0, 128, 0));
		}
		else if (maze.getHero().hasSword()) {
			gameInfoLabel.setForeground(Color.BLUE);
			gameInfoLabel.setText("Go slay the dragons.");
		}	
	}





	void startGame(JTextField dimensionField, JTextField dragonNum, JComboBox<DRAGON_MODE> dragonMode) 
			throws NumberFormatException, IllegalArgumentException {


		maze = new Maze(Integer.parseInt(dimensionField.getText()), 
				Integer.parseInt(dragonNum.getText()), (DRAGON_MODE)dragonMode.getSelectedItem());


		setVisible(true);
		play(maze);
	}




	void startGame(char[][] board) {

		maze = new Maze(board);

		setVisible(true);
		play(maze);
	}


	void startGame(Maze maze) {

		this.maze = maze;
		setVisible(true);
		play(maze);
	}


	void finishGame(EndGame e) {

		gameFinished = true;
		ShowGamePanel.gameFinished();

		btnUP.setEnabled(false);
		btnLEFT.setEnabled(false);
		btnDOWN.setEnabled(false);
		btnRIGHT.setEnabled(false);	

		if(e.Won())
			gameInfoLabel.setText("Congratulations, you won!");
		else
			gameInfoLabel.setText("Game over. Try again.");
	}




	void prepareGame() {

		gameInfoLabel.setText("Go pick up the sword!");
		gameInfoLabel.setForeground(Color.RED);

		gameFinished = false;
		ShowGamePanel.gameStarted();
		ShowGamePanel.setFocusable(true);
		ShowGamePanel.requestFocus();

		MenuGUI.setVisible(false);
		GameFrame.setVisible(true);

		btnUP.setEnabled(true);
		btnLEFT.setEnabled(true);
		btnRIGHT.setEnabled(true);
		btnDOWN.setEnabled(true);
	}



	public void play(Maze maze) {

		prepareGame();
		this.maze = maze;
		ShowGamePanel.setMaze(maze);
	}

	public void setVisible(boolean visible) {
		GameFrame.setVisible(visible);
	}
	
	
	
	public void goBack() {
		
		GameFrame.setVisible(false);
		processGame();
		MenuGUI.goTo();
	}
}
