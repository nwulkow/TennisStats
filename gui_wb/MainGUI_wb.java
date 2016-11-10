package gui_wb;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import tools.OutputTools;

import javax.swing.JTabbedPane;

public class MainGUI_wb extends JFrame {

	private JPanel contentPane;
	private final JPanel panel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI_wb frame = new MainGUI_wb();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public MainGUI_wb() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 1032, 806);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel.setBounds(0, 0, 1014, 783);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 47, 1014, 733);
		panel.add(tabbedPane);
		
		SimulatorPanel_wb simulatorPanel_wb = new SimulatorPanel_wb();
		tabbedPane.addTab("Simulator", null, simulatorPanel_wb, null);
		
		MatchPanel matchPanel = new MatchPanel();
		tabbedPane.addTab("New tab", null, matchPanel, null);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	exitMethod();
		    }
		});
		
	}
	
	
	private void exitMethod(){
		OutputTools.deleteFilesInFolder("C:/Users/Niklas/TennisStatsData/Current");		
	}
}
