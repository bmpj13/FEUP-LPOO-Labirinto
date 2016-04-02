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
		dimensionField.setBounds(224, 44, 84, 25);
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
		dragonNum.setBounds(224, 94, 84, 25);
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
		dragonMode.setBounds(224, 146, 84, 22);
		panel_settings.add(dragonMode);
		dragonMode.setModel(new DefaultComboBoxModel<DRAGON_MODE>(DRAGON_MODE.values()));

		panel_menu = new JPanel();
		panel_menu.setLayout(null);

		JButton btnNewGame = new JButton("New Game");
		btnNewGame.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		btnNewGame.setBounds(0, 0, 145, 27);
		panel_menu.add(btnNewGame);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				showGameFrame();
				gameInfo.setText("Go pick up the sword or you will die!");
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
		ShowGamePanel.setBackground(Color.WHITE);
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

				default:
					return;
				}

				try {
					maze.update(dir);
				}
				catch (EndGame e) {	
					finishGame(e);
					return;
				}
				finally {
					ShowGamePanel.repaint();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}
		});


		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(null);

		btnUP = new JButton("UP");
		btnUP.setBounds(54, 13, 88, 31);
		btnUP.setFocusable(false);
		buttonPanel.add(btnUP);

		btnLEFT = new JButton("LEFT");
		btnLEFT.setBounds(0, 57, 80, 25);
		btnLEFT.setFocusable(false);
		buttonPanel.add(btnLEFT);

		btnRIGHT = new JButton("RIGHT");
		btnRIGHT.setBounds(100, 57, 80, 25);
		btnRIGHT.setFocusable(false);
		buttonPanel.add(btnRIGHT);

		btnDOWN = new JButton("DOWN");
		btnDOWN.setBounds(54, 95, 88, 31);
		btnDOWN.setFocusable(false);
		buttonPanel.add(btnDOWN);

		JLabel gameInfoLabel = new JLabel("New label");
		gameInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_GamePanel = new GroupLayout(GamePanel);
		gl_GamePanel.setHorizontalGroup(
				gl_GamePanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_GamePanel.createSequentialGroup()
						.addComponent(ShowGamePanel, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
						.addGap(33)
						.addGroup(gl_GamePanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(gameInfoLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
								.addGap(29))
				);
		gl_GamePanel.setVerticalGroup(
				gl_GamePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_GamePanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(gameInfoLabel, GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
						.addGap(138)
						.addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
						.addGap(108))
						.addComponent(ShowGamePanel, GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
				);
		GamePanel.setLayout(gl_GamePanel);


		main_panel.requestFocus();
	}



	public void moveBtnAction(DIRECTION dir) {

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


		if (maze.getDragonList().size() == 0)
			gameInfo.setText("Proceed to the exit.");
		else {
			if (maze.heroHasSword())
				gameInfo.setText("Go slay the dragons.");
			else gameInfo.setText("Go pick up the sword or you will die!");		
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
			gameInfo.setText("Congratulations, you won!");
		else gameInfo.setText("Game over. Try again.");
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
		GameFrame.setVisible(true);
		
		btnUP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.UP);
			}
		});
		btnUP.setEnabled(true);


		btnLEFT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.LEFT);
			}
		});
		btnLEFT.setEnabled(true);


		btnRIGHT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.RIGHT);
			}
		});
		btnRIGHT.setEnabled(true);


		btnDOWN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.DOWN);
			}
		});
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
