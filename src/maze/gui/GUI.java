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
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

import maze.logic.Position;

public class GUI {

	private JFrame MainFrame;
	private JTextField dimensionField;
	private JLabel lblDragons;
	private JTextField dragonNum;
	private JLabel MainInfo;
	private JPanel panel_menu;
	private JPanel main_panel;
	private Maze maze;
	private JComboBox<DRAGON_MODE> dragonMode;

	private GameGUI gameGUI;
	private MazeBuilderGUI mazeBuilderGUI;

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

				try {
					gameGUI.startGame(dimensionField, dragonNum, dragonMode);
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


		btnNewGame.setFont(new Font("Tahoma", Font.PLAIN, 15));

		MainInfo = new JLabel("Can start a new game");
		MainInfo.setHorizontalAlignment(SwingConstants.CENTER);
		MainInfo.setFont(new Font("Tahoma", Font.PLAIN, 15));
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
										.addComponent(MainInfo, GroupLayout.DEFAULT_SIZE, 808, Short.MAX_VALUE)
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
						.addComponent(MainInfo)
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
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		main_panel.setLayout(gl_main_panel);
		MainFrame.getContentPane().add(main_panel, BorderLayout.CENTER);	


		main_panel.requestFocus();
	}


	public void setVisible(boolean visible) {
		MainFrame.setVisible(visible);
	}


	public void goTo() {
		MainFrame.setVisible(true);
		main_panel.requestFocus();
	}
}
