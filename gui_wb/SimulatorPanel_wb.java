package gui_wb;

import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.IntStream;

import javax.swing.JScrollPane;

import dataload.LoadValues;
import player.Player;
import simulator.Simulator;
import simulator.TestingSimulator;

import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import analytics.PlayerStandardStats;
import counts.Match;

import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JRadioButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;

public class SimulatorPanel_wb extends JPanel {

	
	String player1name = "", player2name = "";
	private JTextField setstowinTextField;
	private JTextField matchesTextField;
	private ButtonGroup mode_buttongroup, direction_buttongroup;
	private JScrollPane matchesScrollPane1;
	//private JList matcheslist1 = new JList(), matcheslist2 = new JList();
	private ArrayList<String> matchesP1 = new ArrayList<String>(), matchesP2 = new ArrayList<String>();
	private MatchCoordinator matchCoordinator1, matchCoordinator2;
	JTabbedPane tabbedPane;
	
	private Object[][] matchTableData = new Object[3][4];
	
	int noMatchesP1, noMatchesP2;
	/**
	 * Create the panel.
	 */
	public SimulatorPanel_wb() {
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 255, 255));
		panel.setBounds(0, 0, 906, 683);
		add(panel);
		panel.setLayout(null);
		
		ArrayList<String> playernames = LoadValues.loadAllPlayernames();
		String[] playernames_array = playernames.toArray(new String[0]);
	    JList playerlist1 = new JList(playernames_array);
	    playerlist1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // Bedeutet, dass Shift+Click nicht mehrere auswaehlt
	    JList playerlist2 = new JList(playernames_array);
	    playerlist2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//--
		
	    
	    playerlist1.addListSelectionListener(new ListSelectionListener() {
	    	public void valueChanged(ListSelectionEvent arg0) {
	    		matchesP1 = new ArrayList<String>();
	    		player1name = playerlist1.getSelectedValue().toString();
	    		
	    		// All Matches List aktualisieren Spieler 1
	    		ArrayList<String> matchesstrings1 = LoadValues.loadAllMatchFilesOfPlayer(player1name);
	    		noMatchesP1 = matchesstrings1.size();
	    		try {
					matchCoordinator1.getMatchSelectorRight().setData(LoadValues.loadMatchDataForMatchSelectorFromStrings(player1name, matchesstrings1));
					matchCoordinator1.getMatchSelectorLeft().deleteData();
					matchCoordinator1.getPlayernameLabel().setText(player1name);
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setTitleAt(0, player1name);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

	    	}
	    });
		JScrollPane scrollPane = new JScrollPane(playerlist1);
		scrollPane.setBounds(19, 42, 220, 131);
		panel.add(scrollPane);
		

	    
	    playerlist2.addListSelectionListener(new ListSelectionListener() {
	    	public void valueChanged(ListSelectionEvent arg0) {
	    		//matchesP2 = new ArrayList<String>();
	    		player2name = playerlist2.getSelectedValue().toString();
	    		//noMatchesP2 = matchesP2.size();
	    		// All Matches List aktualisieren Spieler 2
	    		ArrayList<String> matchesstrings2 = LoadValues.loadAllMatchFilesOfPlayer(player2name);
	    		noMatchesP2 = matchesstrings2.size();
	    		try {
					matchCoordinator2.getMatchSelectorRight().setData(LoadValues.loadMatchDataForMatchSelectorFromStrings(player2name, matchesstrings2));
					matchCoordinator2.getMatchSelectorLeft().deleteData();
					matchCoordinator2.getPlayernameLabel().setText(player2name);
					tabbedPane.setSelectedIndex(1);
					tabbedPane.setTitleAt(1, player2name);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

	    	}
	    });
		JScrollPane scrollPane_1 = new JScrollPane(playerlist2);
		scrollPane_1.setBounds(258, 42, 212, 131);
		panel.add(scrollPane_1);
		
		JLabel p1winLabel = new JLabel("Wins Player 1");
		p1winLabel.setBounds(116, 409, 111, 45);
		panel.add(p1winLabel);
		
		JLabel p2winLabel = new JLabel("Wins Player 2");
		p2winLabel.setBounds(291, 409, 111, 45);
		panel.add(p2winLabel);
		
		setstowinTextField = new JTextField();
		setstowinTextField.setText("2");
		setstowinTextField.setBounds(485, 81, 71, 26);
		panel.add(setstowinTextField);
		setstowinTextField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Sets to win");
		lblNewLabel.setBounds(485, 55, 87, 20);
		panel.add(lblNewLabel);
		
		JLabel matchesLabel = new JLabel("Matches");
		matchesLabel.setBounds(485, 123, 69, 20);
		panel.add(matchesLabel);
		
		matchesTextField = new JTextField();
		matchesTextField.setText("100");
		matchesTextField.setBounds(485, 147, 71, 26);
		panel.add(matchesTextField);
		matchesTextField.setColumns(10);
		
		// JRadioButtons fuer den mode der Simulation
		JRadioButton shotbyshotRadioButton = new JRadioButton("Shot by Shot");
		shotbyshotRadioButton.setBounds(116, 572, 151, 29);
		shotbyshotRadioButton.setOpaque(false);
		panel.add(shotbyshotRadioButton);
		
		JRadioButton pointwinRadioButton = new JRadioButton("Point Win %");
		pointwinRadioButton.setBackground(new Color(224, 255, 255));
		pointwinRadioButton.setSelected(true);
		pointwinRadioButton.setBounds(116, 498, 151, 29);
		pointwinRadioButton.setOpaque(false);
		panel.add(pointwinRadioButton);
		
		JRadioButton servereturnwinRadioButton = new JRadioButton("Serve/Return Win %");
		servereturnwinRadioButton.setBounds(117, 535, 173, 29);
		servereturnwinRadioButton.setOpaque(false);
		panel.add(servereturnwinRadioButton);
		
		mode_buttongroup = new ButtonGroup();
		mode_buttongroup.add(shotbyshotRadioButton);
		mode_buttongroup.add(pointwinRadioButton);
		mode_buttongroup.add(servereturnwinRadioButton);
		
		
		// JRadioButtons fuer withDirection bei shot-by-shot Simulation
		JRadioButton withoutDirectionRadioButton = new JRadioButton("without Direction");
		withoutDirectionRadioButton.setBounds(275, 555, 127, 25);
		panel.add(withoutDirectionRadioButton);
		withoutDirectionRadioButton.setOpaque(false);
		withoutDirectionRadioButton.setVisible(false);
		
		JRadioButton withDirectionRadioButton = new JRadioButton("with Direction");
		withDirectionRadioButton.setBounds(275, 585, 127, 25);
		panel.add(withDirectionRadioButton);
		withDirectionRadioButton.setOpaque(false);
		withDirectionRadioButton.setVisible(false);	
		withDirectionRadioButton.setSelected(true);
		
		direction_buttongroup = new ButtonGroup();
		direction_buttongroup.add(withoutDirectionRadioButton);
		direction_buttongroup.add(withDirectionRadioButton);
		
		ActionListener modebutton_listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(e.getSource().equals(shotbyshotRadioButton)){
					withDirectionRadioButton.setVisible(true);
					withoutDirectionRadioButton.setVisible(true);
				}
				else if(e.getSource().equals(pointwinRadioButton)){
					withDirectionRadioButton.setVisible(false);
					withoutDirectionRadioButton.setVisible(false);
				}
				if(e.getSource().equals(servereturnwinRadioButton)){
					withDirectionRadioButton.setVisible(false);
					withoutDirectionRadioButton.setVisible(false);
				}						
			}
		};
		pointwinRadioButton.addActionListener(modebutton_listener);
		servereturnwinRadioButton.addActionListener(modebutton_listener);
		shotbyshotRadioButton.addActionListener(modebutton_listener);

		JButton simulateButton = new JButton("Simulate");
		simulateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedRows1 = IntStream.range(0, matchCoordinator1.getMatchSelectorRight().getTable().getRowCount()).toArray();
				ArrayList<Match> selectedMatches1 = new ArrayList<Match>();
				try {
					selectedMatches1 = matchCoordinator1.matchFilesFromSelectorFormat(selectedRows1, player1name);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				int[] selectedRows2 = IntStream.range(0, matchCoordinator2.getMatchSelectorRight().getTable().getRowCount()).toArray();
				
				ArrayList<Match> selectedMatches2 = new ArrayList<Match>();
				try {
					selectedMatches2 = matchCoordinator2.matchFilesFromSelectorFormat(selectedRows2, player2name);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(!player1name.equals("") && !player2name.equals("")){
					int mode = 1;
					if(servereturnwinRadioButton.isSelected()){
						mode = 2;
					}
					if(shotbyshotRadioButton.isSelected()){
						if(withoutDirectionRadioButton.isSelected()){
							mode = 3;
						}
						else if(withDirectionRadioButton.isSelected()){
							mode = 4;
						}
					}
					double p1winpercentage = 0;
					try {
						try {
							boolean currentP1 = !(noMatchesP1 == selectedMatches1.size());
							boolean currentP2 = !(noMatchesP2 == selectedMatches2.size());
							p1winpercentage = TestingSimulator.winPercentageOfSimulation(player1name, selectedMatches1, player2name, selectedMatches2, Integer.parseInt(setstowinTextField.getText()), Integer.parseInt(matchesTextField.getText()), mode, currentP1, currentP2);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					p1winLabel.setText(p1winpercentage + "");
					p2winLabel.setText((1-p1winpercentage) + "");
				}
			}
		});
		simulateButton.setBounds(225, 638, 115, 29);
		panel.add(simulateButton);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(19, 193, 872, 207);
		tabbedPane.setOpaque(false);
		panel.add(tabbedPane);
		
		matchCoordinator1 = new MatchCoordinator();
		tabbedPane.addTab("Player 1", null, matchCoordinator1, null);
		
		matchCoordinator2 = new MatchCoordinator();
		tabbedPane.addTab("Player 2", null, matchCoordinator2, null);


		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(245, 245, 220));
		panel_1.setBounds(905, 0, 244, 342);
		add(panel_1);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 250, 205));
		panel_2.setBounds(905, 341, 230, 342);
		add(panel_2);
		panel_2.setLayout(null);

	}
}
