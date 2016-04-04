package maze.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import maze.logic.Maze;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Color;

import javax.swing.border.LineBorder;

import java.awt.Font;

public class MazeBuilderGUI {

	private GUI MenuGUI;
	private GameGUI gameGUI;
	private JFrame MBFrame;
	private MazeBuilderPanel MBPanel;


	/**
	 * Create the application.
	 */
	public MazeBuilderGUI(GUI MenuGUI, GameGUI gameGUI, int dimension) {

		this.MenuGUI = MenuGUI;
		this.gameGUI = gameGUI;
		initialize(dimension);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int dimension) {

		MBFrame = new JFrame();
		MBFrame.getContentPane().setLayout(new BorderLayout());

		int fWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.9);
		int fHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9);
		MBFrame.setSize(new Dimension(fWidth, fHeight));
		MBFrame.setMinimumSize(new Dimension(1200, 800));
		MBFrame.setLocationRelativeTo(null);

		MBFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				MBFrame.setVisible(false);
				MenuGUI.goTo();
			}
		});


		MBPanel = new MazeBuilderPanel(new Maze(dimension));
		MBPanel.setFocusable(true);
		MBPanel.requestFocus();

		JButton MBFinish = new JButton("Finish");
		MBFinish.setFocusable(false);
		MBFinish.setForeground(Color.BLACK);
		MBFinish.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
		MBFinish.setBackground(Color.RED);
		MBFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					finishBuild();
				}
				catch(IllegalArgumentException i){
					JOptionPane.showMessageDialog(MBFrame, i.getMessage(), "Builder Log", JOptionPane.WARNING_MESSAGE);
				}

			}
		});

		MBFrame.getContentPane().add(MBPanel, BorderLayout.CENTER);

		SingleImagePanel pathPanel = new SingleImagePanel("res\\path.png");
		pathPanel.setBorder(new LineBorder(Color.RED, 3));
		pathPanel.setBackground(Color.WHITE);

		SingleImagePanel dragonPanel = new SingleImagePanel("res\\dragonActive.png", 0, 0, 4, 4);
		dragonPanel.setBorder(new LineBorder(Color.RED, 3));
		dragonPanel.setBackground(Color.WHITE);

		SingleImagePanel heroPanel = new SingleImagePanel("res\\heroUnarmed.png", 2, 1, 4, 9);
		heroPanel.setBorder(new LineBorder(Color.RED, 3));
		heroPanel.setBackground(Color.WHITE);

		SingleImagePanel swordPanel = new SingleImagePanel("res\\sword.png");
		swordPanel.setBorder(new LineBorder(Color.RED, 3));
		swordPanel.setBackground(Color.WHITE);
		GroupLayout gl_MBPanel = new GroupLayout(MBPanel);
		gl_MBPanel.setHorizontalGroup(
				gl_MBPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MBPanel.createSequentialGroup()
						.addGroup(gl_MBPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_MBPanel.createSequentialGroup()
										.addContainerGap()
										.addComponent(MBFinish, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_MBPanel.createParallelGroup(Alignment.TRAILING, false)
												.addGroup(Alignment.LEADING, gl_MBPanel.createSequentialGroup()
														.addGap(10)
														.addComponent(dragonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
														.addGroup(Alignment.LEADING, gl_MBPanel.createSequentialGroup()
																.addGap(10)
																.addComponent(pathPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
																.addGroup(gl_MBPanel.createSequentialGroup()
																		.addGap(10)
																		.addComponent(heroPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
																		.addGroup(gl_MBPanel.createSequentialGroup()
																				.addGap(10)
																				.addComponent(swordPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
																				.addContainerGap(1090, Short.MAX_VALUE))
				);
		gl_MBPanel.setVerticalGroup(
				gl_MBPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_MBPanel.createSequentialGroup()
						.addGap(10)
						.addComponent(pathPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addGap(40)
						.addComponent(dragonPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addGap(40)
						.addComponent(heroPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addGap(40)
						.addComponent(swordPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
						.addComponent(MBFinish, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addGap(85))
				);
		MBPanel.setLayout(gl_MBPanel);
		MBFrame.setVisible(true);


		MBPanel.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent k) {	}

			@Override
			public void keyReleased(KeyEvent k) {}

			@Override
			public void keyPressed(KeyEvent k) {

				switch(k.getKeyCode()){

				case KeyEvent.VK_ESCAPE:
					MBFrame.setVisible(false);
					MenuGUI.goTo();
					break;

				case KeyEvent.VK_ENTER:
					finishBuild();
					break;
				}
			}
		});
	}


	protected void finishBuild() {

		gameGUI.startGame(MBPanel.getBoard());
		MBFrame.setVisible(false);
	}
}
