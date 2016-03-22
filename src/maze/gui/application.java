package maze.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;

import maze.exceptions.EndGame;
import maze.logic.Hero.HERO_STATE;
import maze.logic.Maze;
import maze.logic.Maze.DIRECTION;
import maze.logic.Maze.DRAGON_MODE;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class application {

	private JFrame frame;
	private JTextField dimensionField;
	private JLabel lblDragons;
	private JTextField dragonNum;
	private JTextArea mazePrint;
	private JButton btnUp;
	private JButton btnLeft;
	private JButton btnDown;
	private JButton btnRight;
	private JLabel gameInfo;
	
	private Maze maze;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					application window = new application();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 803, 706);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Dimension: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(58, 13, 94, 22);
		frame.getContentPane().add(lblNewLabel);
		
		//Dimension of maze
		dimensionField = new JTextField();
		dimensionField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dimensionField.setText("11");
		dimensionField.setBounds(164, 13, 116, 22);
		frame.getContentPane().add(dimensionField);
		dimensionField.setColumns(10);
		
		lblDragons = new JLabel("Dragons:");
		lblDragons.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDragons.setBounds(58, 59, 94, 22);
		frame.getContentPane().add(lblDragons);
		
		//Number of dragons
		dragonNum = new JTextField();
		dragonNum.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dragonNum.setText("1");
		dragonNum.setBounds(164, 60, 116, 22);
		frame.getContentPane().add(dragonNum);
		dragonNum.setColumns(10);
		
		
		JLabel lblDragonMode = new JLabel("Dragon Mode:");
		lblDragonMode.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDragonMode.setBounds(58, 104, 94, 22);
		frame.getContentPane().add(lblDragonMode);
		
		//Set Dragon Mode
		JComboBox dragonMode = new JComboBox();
		dragonMode.setModel(new DefaultComboBoxModel(DRAGON_MODE.values()));
		dragonMode.setBounds(164, 104, 126, 32);
		frame.getContentPane().add(dragonMode);
		
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
				maze = new Maze(Integer.parseInt(dimensionField.getText()), Integer.parseInt(dragonNum.getText()), (DRAGON_MODE)dragonMode.getSelectedItem());
				}
				catch (NumberFormatException n){
					gameInfo.setText("Invalid arguments");
					return;
				}
				catch (IllegalArgumentException i){
					gameInfo.setText("Dimension cannot be even or inferior to 5");
					return;
				}
				mazePrint.setText(maze.toString());
				btnUp.setEnabled(true);
				btnLeft.setEnabled(true);
				btnDown.setEnabled(true);
				btnRight.setEnabled(true);
				gameInfo.setText("Go pick up the sword or you will die");
			}
		});
	
		btnNewGame.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewGame.setBounds(543, 51, 116, 38);
		frame.getContentPane().add(btnNewGame);
		
		JButton btnNewButton = new JButton("Quit");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnNewButton.setBounds(540, 104, 119, 32);
		frame.getContentPane().add(btnNewButton);
		
		mazePrint = new JTextArea();
		mazePrint.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				mazePrint.setBorder(mazePrint.getBorder());
				//mazePrint.
			}
		});
		mazePrint.setFont(new Font("Courier New", Font.PLAIN, 22));
		mazePrint.setEditable(false);
		mazePrint.setBounds(58, 213, 414, 414);
		frame.getContentPane().add(mazePrint);
		
		
		
		
		btnUp = new JButton("UP");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.UP);
			}
		});
		btnUp.setEnabled(false);
		btnUp.setBounds(586, 296, 97, 25);
		frame.getContentPane().add(btnUp);
		
		btnLeft = new JButton("LEFT");
		btnLeft.setEnabled(false);
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.LEFT);
			}
		});
		btnLeft.setBounds(533, 334, 97, 25);
		frame.getContentPane().add(btnLeft);
		
		btnRight = new JButton("RIGHT");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.RIGHT);
			}
		});
		btnRight.setEnabled(false);
		btnRight.setBounds(642, 334, 97, 25);
		frame.getContentPane().add(btnRight);
		
		btnDown = new JButton("DOWN");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveBtnAction(DIRECTION.DOWN);
			}
		});
		btnDown.setEnabled(false);
		btnDown.setBounds(586, 372, 97, 25);
		frame.getContentPane().add(btnDown);
		
		gameInfo = new JLabel("Can start a new game");
		gameInfo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		gameInfo.setBounds(58, 149, 414, 66);
		frame.getContentPane().add(gameInfo);
	}
	
	
	
	public void moveBtnAction(DIRECTION dir) {
		
		
		try{
			maze.update(dir);
			if(maze.getDragonList().size()!=0 && maze.heroHasSword())
				gameInfo.setText("Go slay the dragons");
			else gameInfo.setText("Proceed to the exit");
		}
		catch (EndGame e){
			btnUp.setEnabled(false);
			btnLeft.setEnabled(false);
			btnDown.setEnabled(false);
			btnRight.setEnabled(false);	
			if(e.Won())
				gameInfo.setText("Congratulations you won!");
			else gameInfo.setText("Game over. Try again");
			
		}

			
		mazePrint.setText(maze.toString());
	}
}
