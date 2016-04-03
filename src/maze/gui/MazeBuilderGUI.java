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
		MBFrame.setLocationRelativeTo(null);

		MBFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				MBFrame.setVisible(false);
				MenuGUI.setVisible(true);
			}
		});


		MBPanel = new MazeBuilderPanel(new Maze(dimension));
		MBPanel.setLayout(null);
		JButton MBFinish = new JButton("Finish");
		MBFinish.setBounds(10, (int) (MBFrame.getHeight() * 0.7), MazeBuilderPanel.sidebarWidth - 20, 50);
		MBFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				gameGUI.startGame(MBPanel.getBoard());
				MBFrame.setVisible(false);
			}
		});
		MBPanel.add(MBFinish);	

		MBFrame.getContentPane().add(MBPanel, BorderLayout.CENTER);
		MBFrame.setVisible(true);
	}
}
