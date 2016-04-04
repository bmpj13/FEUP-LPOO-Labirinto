package maze.gui;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import maze.logic.MovementInfo;

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
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

import maze.logic.Position;

public class GUI {

	private JFrame MainFrame;
	private JTextField dimensionField;
	private JLabel dragonsLabel;
	private JTextField dragonNumField;
	private JLabel MainInfo;
	private JPanel panel_menu;
	private SingleImagePanel main_panel;
	private JComboBox<DRAGON_MODE> dragonModeField;

	private GameGUI gameGUI;

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		gameGUI = new GameGUI(this);
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		MainFrame = new JFrame();
		MainFrame.getContentPane().setBackground(Color.WHITE);
		MainFrame.setSize(new Dimension(850, 570));
		MainFrame.setResizable(false);
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		MainFrame.setLocationRelativeTo(null);

		main_panel = new SingleImagePanel("res\\mazeBackground.png");
		main_panel.setFocusable(true);

		JPanel panel_settings = new JPanel();
		panel_settings.setBackground(Color.WHITE);
		panel_settings.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		panel_settings.setLayout(null);

		JLabel dimensionLabel = new JLabel("Dimension: ");
		dimensionLabel.setBounds(75, 47, 96, 19);
		panel_settings.add(dimensionLabel);
		dimensionLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 15));


		Color myCyan = new Color(0, 153, 153);
		Border txtFieldBorder = BorderFactory.createLineBorder(myCyan);

		//Dimension of maze
		dimensionField = new JTextField();
		dimensionField.setBackground(Color.WHITE);
		dimensionField.setHorizontalAlignment(SwingConstants.CENTER);
		dimensionField.setBounds(224, 44, 93, 25);
		panel_settings.add(dimensionField);
		dimensionField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		dimensionField.setText("11");
		dimensionField.setColumns(10);
		dimensionField.setBorder(txtFieldBorder);

		dragonsLabel = new JLabel("Dragons:");
		dragonsLabel.setBounds(78, 97, 93, 19);
		panel_settings.add(dragonsLabel);
		dragonsLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 15));

		//Number of dragons
		dragonNumField = new JTextField();
		dragonNumField.setBackground(Color.WHITE);
		dragonNumField.setHorizontalAlignment(SwingConstants.CENTER);
		dragonNumField.setBounds(224, 94, 93, 25);
		panel_settings.add(dragonNumField);
		dragonNumField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		dragonNumField.setText("1");
		dragonNumField.setColumns(10);
		dragonNumField.setBorder(txtFieldBorder);


		JLabel dragonModeLabel = new JLabel("Dragon Mode:");
		dragonModeLabel.setBounds(78, 147, 104, 19);
		panel_settings.add(dragonModeLabel);
		dragonModeLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 15));

		//Set Dragon Mode
		dragonModeField = new JComboBox<DRAGON_MODE>();
		dragonModeField.setBackground(Color.WHITE);
		dragonModeField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		dragonModeField.setBounds(224, 146, 112, 22);
		panel_settings.add(dragonModeField);
		dragonModeField.setModel(new DefaultComboBoxModel<DRAGON_MODE>(DRAGON_MODE.values()));
		dragonModeField.setSelectedIndex(2);
		dragonModeField.setBorder(txtFieldBorder);

		panel_menu = new JPanel();
		panel_menu.setLayout(null);
		panel_menu.setOpaque(false);


		MainInfo = new JLabel("Start a new game");
		MainInfo.setForeground(Color.WHITE);
		MainInfo.setHorizontalAlignment(SwingConstants.CENTER);
		MainInfo.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		GroupLayout gl_main_panel = new GroupLayout(main_panel);
		gl_main_panel.setHorizontalGroup(
				gl_main_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_main_panel.createSequentialGroup()
						.addGap(217)
						.addComponent(panel_settings, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
						.addGap(217))
						.addGroup(gl_main_panel.createSequentialGroup()
								.addContainerGap()
								.addComponent(MainInfo, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
								.addContainerGap())
								.addGroup(Alignment.LEADING, gl_main_panel.createSequentialGroup()
										.addGap(352)
										.addComponent(panel_menu, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
										.addContainerGap(347, Short.MAX_VALUE))
				);
		gl_main_panel.setVerticalGroup(
				gl_main_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_main_panel.createSequentialGroup()
						.addGap(46)
						.addComponent(panel_settings, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
						.addGap(41)
						.addComponent(panel_menu, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(MainInfo)
						.addGap(56))
				);


		GradientButton btnNewGame = new GradientButton("New Game", new Color(0, 153, 153));
		btnNewGame.setForeground(Color.WHITE);
		btnNewGame.setBackground(Color.BLACK);
		btnNewGame.setBounds(0, 0, 145, 27);
		panel_menu.add(btnNewGame);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					gameGUI.startGame(dimensionField, dragonNumField, dragonModeField);
				}
				catch (NumberFormatException n){
					MainInfo.setText("Invalid arguments");
					return;
				}
				catch (IllegalArgumentException i){
					MainInfo.setText(i.getMessage());
					return;
				}
			}
		});

		btnNewGame.setFont(new Font("Trebuchet MS", Font.BOLD, 17));


		GradientButton btnQuit = new GradientButton("Quit", new Color(0, 153, 153));
		btnQuit.setForeground(Color.WHITE);
		btnQuit.setBackground(Color.BLACK);
		btnQuit.setBounds(0, 123, 145, 31);
		panel_menu.add(btnQuit);
		btnQuit.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));


		GradientButton btnMB = new GradientButton("Build Maze", new Color(0, 153, 153));
		btnMB.setForeground(Color.WHITE);
		btnMB.setBackground(Color.BLACK);
		btnMB.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		btnMB.setBounds(0, 83, 145, 27);
		GUI thisGUI = this;
		btnMB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					new MazeBuilderGUI(thisGUI, gameGUI, Integer.parseInt(dimensionField.getText()));
				}
				catch (NumberFormatException n){
					MainInfo.setText("Invalid arguments");
					return;
				}
				catch (IllegalArgumentException i){
					MainInfo.setText(i.getMessage());
					return;
				}
			}
		});


		panel_menu.add(btnMB);

		GradientButton btnSaved = new GradientButton("Saved Game", new Color(0, 153, 153));
		btnSaved.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				Maze maze;
				try {
					maze = loadGame();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(MainFrame,
							"No game saved.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} 
				
				gameGUI.startGame(maze);
			}
		});
		btnSaved.setForeground(Color.WHITE);
		btnSaved.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		btnSaved.setBackground(Color.BLACK);
		btnSaved.setBounds(0, 43, 145, 27);
		panel_menu.add(btnSaved);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		main_panel.setLayout(gl_main_panel);
		MainFrame.getContentPane().add(main_panel, BorderLayout.CENTER);	

		main_panel.requestFocus();
	}



	protected Maze loadGame() throws FileNotFoundException, IOException, ClassNotFoundException {

		ObjectInputStream is = null;
		Maze maze = null;

		is = new ObjectInputStream(new FileInputStream("save\\maze.dat"));
		maze = (Maze) is.readObject();
		if (is != null) is.close();

		return maze;
	}


	public void setVisible(boolean visible) {
		MainFrame.setVisible(visible);
	}


	public void goTo() {
		MainInfo.setText("Start a new game");
		MainFrame.setVisible(true);
		main_panel.requestFocus();
	}
}
