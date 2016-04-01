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

import javax.swing.JTextArea;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class GUI {

	private JFrame mainFrame;
	private JTextField dimensionField;
	private JLabel lblDragons;
	private JTextField dragonNum;
	private JButton btnUp;
	private JButton btnLeft;
	private JButton btnDown;
	private JButton btnRight;
	private JLabel gameInfo;
	private MazeGraphicPlay panel_maze;
	private JPanel panel_directions;
	private JPanel panel_menu;
	private JPanel main_panel;
	private JFrame MBFrame;
	private Maze maze;


	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}


	public void setVisible(boolean visible) {
		mainFrame.setVisible(visible);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		mainFrame = new JFrame();
		mainFrame.setBounds(200, 200, 850, 700);
		mainFrame.setMinimumSize(new Dimension(850, 700));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));

		main_panel = new JPanel();

		JPanel panel_settings = new JPanel();
		panel_settings.setLayout(null);

		JLabel lblNewLabel = new JLabel("Dimension: ");
		lblNewLabel.setBounds(75, 47, 76, 19);
		panel_settings.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

		//Dimension of maze
		dimensionField = new JTextField();
		dimensionField.setBounds(224, 43, 136, 25);
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
		dragonNum.setBounds(224, 94, 136, 25);
		panel_settings.add(dragonNum);
		dragonNum.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dragonNum.setText("1");
		dragonNum.setColumns(10);


		JLabel lblDragonMode = new JLabel("Dragon Mode:");
		lblDragonMode.setBounds(78, 147, 93, 19);
		panel_settings.add(lblDragonMode);
		lblDragonMode.setFont(new Font("Tahoma", Font.PLAIN, 15));

		//Set Dragon Mode
		JComboBox<DRAGON_MODE> dragonMode = new JComboBox<DRAGON_MODE>();
		dragonMode.setBounds(224, 146, 93, 22);
		panel_settings.add(dragonMode);
		dragonMode.setModel(new DefaultComboBoxModel<DRAGON_MODE>(DRAGON_MODE.values()));

		panel_menu = new JPanel();
		panel_menu.setLayout(new BorderLayout(0, 0));

		JButton btnNewGame = new JButton("New Game");
		panel_menu.add(btnNewGame, BorderLayout.NORTH);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try{
					maze = new Maze(Integer.parseInt(dimensionField.getText()), 
							Integer.parseInt(dragonNum.getText()), (DRAGON_MODE)dragonMode.getSelectedItem());
					panel_maze.setMaze(maze);
					panel_maze.repaint();
					panel_maze.requestFocus();
				}
				catch (NumberFormatException n){
					gameInfo.setText("Invalid arguments");
					return;
				}
				catch (IllegalArgumentException i){
					gameInfo.setText(i.getMessage());
					return;
				}

				btnUp.setEnabled(true);
				btnLeft.setEnabled(true);
				btnDown.setEnabled(true);
				btnRight.setEnabled(true);
				gameInfo.setText("Go pick up the sword or you will die!");
			}
		});

		btnNewGame.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JButton btnNewButton = new JButton("Quit");
		panel_menu.add(btnNewButton, BorderLayout.SOUTH);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		panel_directions = new JPanel();

		btnUp = new JButton("UP");
		btnUp.setBounds(105, 39, 88, 31);
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.UP);
			}
		});
		btnUp.setEnabled(false);

		btnLeft = new JButton("LEFT");
		btnLeft.setBounds(12, 83, 88, 25);
		btnLeft.setEnabled(false);
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.LEFT);
			}
		});

		btnRight = new JButton("RIGHT");
		btnRight.setBounds(190, 83, 88, 25);
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.RIGHT);
			}
		});
		btnRight.setEnabled(false);

		btnDown = new JButton("DOWN");
		btnDown.setBounds(105, 121, 88, 31);
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.DOWN);
			}
		});
		btnDown.setEnabled(false);

		gameInfo = new JLabel("Can start a new game");
		gameInfo.setHorizontalAlignment(SwingConstants.CENTER);
		gameInfo.setFont(new Font("Tahoma", Font.PLAIN, 15));

		panel_maze = new MazeGraphicPlay();
		panel_maze.setPreferredSize(new Dimension(255,255));
		panel_maze.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

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
					panel_maze.repaint();
				}

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		gameInfo.setLabelFor(panel_maze);
		GroupLayout gl_main_panel = new GroupLayout(main_panel);
		gl_main_panel.setHorizontalGroup(
				gl_main_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_main_panel.createSequentialGroup()
						.addComponent(panel_settings, GroupLayout.PREFERRED_SIZE, 471, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
						.addComponent(panel_menu, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
						.addGap(147))
						.addGroup(Alignment.LEADING, gl_main_panel.createSequentialGroup()
								.addGap(23)
								.addGroup(gl_main_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(gameInfo, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
										.addComponent(panel_maze, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))
										.addGap(63)
										.addComponent(panel_directions, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
										.addGap(94))
				);
		gl_main_panel.setVerticalGroup(
				gl_main_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_main_panel.createSequentialGroup()
						.addGroup(gl_main_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_main_panel.createSequentialGroup()
										.addGap(35)
										.addComponent(panel_menu, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
										.addGap(159)
										.addComponent(panel_directions, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_main_panel.createSequentialGroup()
												.addComponent(panel_settings, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
												.addGap(24)
												.addComponent(gameInfo)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(panel_maze, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)))
												.addContainerGap())
				);

		panel_directions.setLayout(null);
		panel_directions.add(btnUp);
		panel_directions.add(btnLeft);
		panel_directions.add(btnRight);
		panel_directions.add(btnDown);
		main_panel.setLayout(gl_main_panel);
		mainFrame.getContentPane().add(main_panel);



		MBFrame = new JFrame();
		MBFrame.getContentPane().setLayout(new BorderLayout());
		MBFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MBFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		MBFrame.setResizable(false);

		JPanel adjustPanel = new JPanel();
		adjustPanel.setLayout(new BorderLayout());
		MBFrame.getContentPane().add(adjustPanel, BorderLayout.CENTER);

		MazeBuilderPanel MBPanel = new MazeBuilderPanel(11);
		MBPanel.setLayout(null);
		JButton MBFinish = new JButton("Finish");
		MBFinish.setBounds(10, (int) (MBFrame.getHeight() * 0.7), MazeBuilderPanel.sidebarWidth - 20, 50);
		MBFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				maze = new Maze(MBPanel.getBoard());
				panel_maze.setMaze(maze);
				panel_maze.repaint();
				panel_maze.requestFocus();
			}
		});


		MBPanel.add(MBFinish);		
		adjustPanel.add(MBPanel, BorderLayout.CENTER);
		MBFrame.setVisible(true);
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
			panel_maze.repaint();
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

		btnUp.setEnabled(false);
		btnLeft.setEnabled(false);
		btnDown.setEnabled(false);
		btnRight.setEnabled(false);	
		main_panel.requestFocus();

		if(e.Won())
			gameInfo.setText("Congratulations, you won!");
		else gameInfo.setText("Game over. Try again.");
	}
}
