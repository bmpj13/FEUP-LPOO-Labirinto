package maze.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;

import maze.exceptions.EndGame;
import maze.logic.Maze;
import maze.logic.Maze.DIRECTION;
import maze.logic.Maze.DRAGON_MODE;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

public class GUI {

	private JFrame MainFrame;
	private JTextField dimensionField;
	private JLabel lblDragons;
	private JTextField dragonNum;
	private JLabel gameInfo;
	private JPanel panel_menu;
	private JPanel main_panel;
	private Maze maze;
	private JComboBox<DRAGON_MODE> dragonMode;
	private MazeGraphicPlay ShowGamePanel;
	private JFrame GameFrame;
	private JButton btnUP;
	private JButton btnLEFT;
	private JButton btnRIGHT;
	private JButton btnDOWN;
	private JLabel gameInfoLabel;

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}


	public void setVisible(boolean visible) {
		MainFrame.setVisible(visible);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		MainFrame = new JFrame();
		MainFrame.getContentPane().setBackground(Color.WHITE);
		MainFrame.setMinimumSize(new Dimension(850, 570));
		MainFrame.setSize(MainFrame.getMinimumSize());
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		MainFrame.setLocationRelativeTo(null);

		main_panel = new JPanel();
		main_panel.setFocusable(true);

		JPanel panel_settings = new JPanel();
		panel_settings.setBackground(Color.WHITE);
		panel_settings.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		panel_settings.setLayout(null);

		JLabel lblNewLabel = new JLabel("Dimension: ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(75, 47, 76, 19);
		panel_settings.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

		//Dimension of maze
		dimensionField = new JTextField();
		dimensionField.setBackground(UIManager.getColor("Button.background"));
		dimensionField.setHorizontalAlignment(SwingConstants.CENTER);
		dimensionField.setBounds(224, 44, 93, 25);
		panel_settings.add(dimensionField);
		dimensionField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dimensionField.setText("11");
		dimensionField.setColumns(10);

		lblDragons = new JLabel("Dragons:");
		lblDragons.setBounds(78, 97, 59, 19);
		panel_settings.add(lblDragons);
		lblDragons.setFont(new Font("Tahoma", Font.PLAIN, 15));

		//Number of dragons
		dragonNum = new JTextField();
		dragonNum.setBackground(UIManager.getColor("Button.background"));
		dragonNum.setHorizontalAlignment(SwingConstants.CENTER);
		dragonNum.setBounds(224, 94, 93, 25);
		panel_settings.add(dragonNum);
		dragonNum.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dragonNum.setText("1");
		dragonNum.setColumns(10);


		JLabel lblDragonMode = new JLabel("Dragon Mode:");
		lblDragonMode.setBounds(78, 147, 93, 19);
		panel_settings.add(lblDragonMode);
		lblDragonMode.setFont(new Font("Tahoma", Font.PLAIN, 15));

		//Set Dragon Mode
		dragonMode = new JComboBox<DRAGON_MODE>();
		dragonMode.setBounds(224, 146, 93, 22);
		panel_settings.add(dragonMode);
		dragonMode.setModel(new DefaultComboBoxModel<DRAGON_MODE>(DRAGON_MODE.values()));
		dragonMode.setSelectedIndex(2);

		panel_menu = new JPanel();
		panel_menu.setLayout(null);

		JButton btnNewGame = new JButton("New Game");
		btnNewGame.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		btnNewGame.setBounds(0, 0, 145, 27);
		panel_menu.add(btnNewGame);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				showGameFrame();
				gameInfoLabel.setText("Go pick up the sword!");
				gameInfoLabel.setForeground(Color.RED);
			}
		});

		btnNewGame.setFont(new Font("Tahoma", Font.PLAIN, 15));

		gameInfo = new JLabel("Can start a new game");
		gameInfo.setHorizontalAlignment(SwingConstants.CENTER);
		gameInfo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout gl_main_panel = new GroupLayout(main_panel);
		gl_main_panel.setHorizontalGroup(
				gl_main_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_main_panel.createSequentialGroup()
						.addGap(196)
						.addComponent(panel_settings, GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
						.addGap(223))
						.addGroup(gl_main_panel.createSequentialGroup()
								.addGap(333)
								.addComponent(panel_menu, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(354, Short.MAX_VALUE))
								.addGroup(gl_main_panel.createSequentialGroup()
										.addComponent(gameInfo, GroupLayout.DEFAULT_SIZE, 808, Short.MAX_VALUE)
										.addGap(24))
				);
		gl_main_panel.setVerticalGroup(
				gl_main_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_main_panel.createSequentialGroup()
						.addGap(47)
						.addComponent(panel_settings, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
						.addGap(29)
						.addComponent(panel_menu, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
						.addGap(38)
						.addComponent(gameInfo)
						.addGap(79))
				);

		JButton btnQuit = new JButton("Quit");
		btnQuit.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		btnQuit.setBounds(0, 84, 145, 27);
		panel_menu.add(btnQuit);
		btnQuit.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JButton btnMB = new JButton("Build Maze");
		btnMB.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		btnMB.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnMB.setBounds(0, 40, 145, 27);
		btnMB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createMBFrame();
			}
		});


		panel_menu.add(btnMB);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		main_panel.setLayout(gl_main_panel);
		MainFrame.getContentPane().add(main_panel, BorderLayout.CENTER);







		/*
		 * Frame for GAMEPLAY
		 */
		GameFrame = new JFrame();
		GameFrame.getContentPane().setLayout(new BorderLayout());

		int fWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.95);
		int fHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9);
		GameFrame.setSize(new Dimension(fWidth, fHeight));
		GameFrame.setMinimumSize(new Dimension(700, 500));
		GameFrame.setLocationRelativeTo(null);

		GameFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				GameFrame.setVisible(false);
				MainFrame.setVisible(true);
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
		btnUP.setEnabled(true);
		btnUP.setFocusable(false);
		btnUP.setBounds(54, 13, 88, 31);
		buttonPanel.add(btnUP);

		btnLEFT = new JButton("LEFT");
		btnLEFT.setEnabled(true);
		btnLEFT.setFocusable(false);
		btnLEFT.setBounds(0, 57, 80, 25);
		buttonPanel.add(btnLEFT);

		btnRIGHT = new JButton("RIGHT");
		btnRIGHT.setEnabled(true);
		btnRIGHT.setFocusable(false);
		btnRIGHT.setBounds(100, 57, 80, 25);
		buttonPanel.add(btnRIGHT);

		btnDOWN = new JButton("DOWN");
		btnDOWN.setEnabled(true);
		btnDOWN.setFocusable(false);
		btnDOWN.setBounds(54, 95, 88, 31);
		buttonPanel.add(btnDOWN);


		GroupLayout gl_sidePanel = new GroupLayout(sidePanel);
		gl_sidePanel.setHorizontalGroup(
				gl_sidePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sidePanel.createSequentialGroup()
						.addGap(30)
						.addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
						.addContainerGap())
						.addComponent(gameInfoLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
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
					dir = DIRECTION.RIGHT; 
					break;

				case KeyEvent.VK_UP: 
					dir = DIRECTION.UP; 
					break;

				case KeyEvent.VK_DOWN: 
					dir = DIRECTION.DOWN; 
					break;

				case KeyEvent.VK_ESCAPE:
					GameFrame.setVisible(false);
					MainFrame.setVisible(true);
					main_panel.requestFocus();
					break;

				default:
					return;
				}


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


		main_panel.requestFocus();
	}



	public void moveAction(DIRECTION dir) {

		try{
			maze.update(dir);
		}
		catch (EndGame e){
			finishGame(e);
			return;
		}
		finally {
			ShowGamePanel.repaint();
		}


		if (maze.getDragonList().size() == 0) {
			gameInfoLabel.setText("Proceed to the exit.");
			gameInfoLabel.setForeground(new Color(0, 128, 0));
		}
		else if (maze.heroHasSword()) {
			gameInfoLabel.setForeground(Color.BLUE);
			gameInfoLabel.setText("Go slay the dragons.");
		}	
	}



	void finishGame(EndGame e) {

		btnUP.setEnabled(false);
		btnLEFT.setEnabled(false);
		btnDOWN.setEnabled(false);
		btnRIGHT.setEnabled(false);	

		ShowGamePanel.setFocusable(false);
		main_panel.requestFocus();

		if(e.Won())
			gameInfoLabel.setText("Congratulations, you won!");
		else gameInfoLabel.setText("Game over. Try again.");
	}



	void createMBFrame() {

		JFrame MBFrame = new JFrame();
		MBFrame.getContentPane().setLayout(new BorderLayout());

		int fWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.95);
		int fHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9);
		MBFrame.setSize(new Dimension(fWidth, fHeight));
		MBFrame.setMinimumSize(new Dimension(700, MBFrame.getHeight()));
		MBFrame.setLocationRelativeTo(null);

		MBFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				MBFrame.setVisible(false);
				MainFrame.setVisible(true);
			}
		});


		MazeBuilderPanel MBPanel = new MazeBuilderPanel(Integer.parseInt(dimensionField.getText()));
		MBPanel.setLayout(null);
		JButton MBFinish = new JButton("Finish");
		MBFinish.setBounds(10, (int) (MBFrame.getHeight() * 0.7), MazeBuilderPanel.sidebarWidth - 20, 50);
		MBFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				showGameFrame(MBPanel.getBoard());
				GameFrame.setVisible(true);
				MBFrame.setVisible(false);
			}
		});
		MBPanel.add(MBFinish);	

		MBFrame.getContentPane().add(MBPanel, BorderLayout.CENTER);
		MBFrame.setVisible(true);
	}






	void prepareGame() {

		ShowGamePanel.setFocusable(true);
		ShowGamePanel.requestFocus();

		MainFrame.setVisible(false);
		GameFrame.setVisible(true);

		btnUP.setEnabled(true);
		btnLEFT.setEnabled(true);
		btnRIGHT.setEnabled(true);
		btnDOWN.setEnabled(true);
	}



	void showGameFrame() {

		prepareGame();

		try {
			maze = new Maze(Integer.parseInt(dimensionField.getText()), 
					Integer.parseInt(dragonNum.getText()), (DRAGON_MODE)dragonMode.getSelectedItem());
		}
		catch (NumberFormatException n){
			gameInfo.setText("Invalid arguments");
			return;
		}
		catch (IllegalArgumentException i){
			gameInfo.setText(i.getMessage());
			return;
		}


		ShowGamePanel.setMaze(maze);
	}




	void showGameFrame(char[][] board) {

		prepareGame();
		maze = new Maze(board);
		ShowGamePanel.setMaze(maze);
	}
}
