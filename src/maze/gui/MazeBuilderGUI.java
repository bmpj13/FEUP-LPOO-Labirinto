package maze.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import maze.logic.Maze;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

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

		int fWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.95);
		int fHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9);
		MBFrame.setSize(new Dimension(fWidth, fHeight));
		MBFrame.setMinimumSize(new Dimension(1200, 800));
		MBFrame.setLocationRelativeTo(null);

		MBFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				MBFrame.setVisible(false);
				MenuGUI.setVisible(true);
			}
		});


		MBPanel = new MazeBuilderPanel(new Maze(dimension));
		JButton MBFinish = new JButton("Finish");
		MBFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				gameGUI.startGame(MBPanel.getBoard());
				MBFrame.setVisible(false);
			}
		});

		MBFrame.getContentPane().add(MBPanel, BorderLayout.CENTER);
		GroupLayout gl_MBPanel = new GroupLayout(MBPanel);
		gl_MBPanel.setHorizontalGroup(
			gl_MBPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MBPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(MBFinish, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(590, Short.MAX_VALUE))
		);
		gl_MBPanel.setVerticalGroup(
			gl_MBPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_MBPanel.createSequentialGroup()
					.addContainerGap(318, Short.MAX_VALUE)
					.addComponent(MBFinish, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addGap(85))
		);
		MBPanel.setLayout(gl_MBPanel);
		MBFrame.setVisible(true);
	}
}
