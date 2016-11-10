package gui_wb;

import javax.swing.JPanel;

import counts.Match;
import dataload.LoadMatchFromTennisAbstract;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class MatchCoordinator extends JPanel {
	private MatchSelector matchSelectorLeft, matchSelectorRight;
	private JButton moveLeftButton;
	private JButton moveallrightButton;
	private JButton moveallleftButton;
	private JLabel playernameLabel;
	/**
	 * Create the panel.
	 */
	public MatchCoordinator() {
		setLayout(null);
				
				matchSelectorRight = new MatchSelector();
				matchSelectorRight.getScrollPane().setSize(349, 120);
				matchSelectorRight.getScrollPane().setLocation(0, 0);
				//matchSelectorRight.getScrollPane().setBounds(0, 0, 452, 72);
				matchSelectorRight.setBounds(517, 0, 349, 120);
				add(matchSelectorRight);
				
						
				JButton moverightButton = new JButton("Move -->");
				moverightButton.setBounds(370, 0, 132, 29);
				add(moverightButton);
						
				matchSelectorLeft = new MatchSelector();
				matchSelectorLeft.getScrollPane().setBounds(0, 0, 354, 125);
				matchSelectorLeft.setBounds(0, 0, 355, 125);
				add(matchSelectorLeft);
				
				matchSelectorLeft.setPartner(matchSelectorRight);
				matchSelectorRight.setPartner(matchSelectorLeft);
				
				moveLeftButton = new JButton("<-- Move");
				moveLeftButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						transferData(matchSelectorRight);
					}
				});
				moveLeftButton.setBounds(370, 67, 132, 29);
				add(moveLeftButton);
				
				moveallrightButton = new JButton("Move All -->");
				moveallrightButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						transferAllData(matchSelectorLeft);
					}
				});
				moveallrightButton.setBounds(370, 30, 132, 29);
				add(moveallrightButton);
				
				moveallleftButton = new JButton("<-- Move All");
				moveallleftButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						transferAllData(matchSelectorRight);
					}
				});
				moveallleftButton.setBounds(370, 96, 132, 29);
				add(moveallleftButton);
				
				playernameLabel = new JLabel("");
				playernameLabel.setBounds(10, 140, 220, 51);
				add(playernameLabel);
				//matchSelectorLeft.getScrollPane().setSize(433, 109);
				//matchSelectorLeft.getScrollPane().setLocation(0, 36);
				moverightButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {		
						transferData(matchSelectorLeft);
					}
				});

	}
	
	public void transferData(MatchSelector matchSelector){

		int[] selectedIndices = matchSelector.getTable().getSelectedRows();
		for (int i = 0; i < selectedIndices.length; i++) {
			selectedIndices[i] = matchSelector.getTable().convertRowIndexToModel(selectedIndices[i]);
		}
		if(selectedIndices.length > 0){
			Object[][] selectedRowData = new Object[selectedIndices.length][matchSelector.getTable().getColumnCount()];
			for(int i = 0; i < selectedIndices.length; i++){
				Object[] selectedRow = {matchSelector.getTableModel().getValueAt(selectedIndices[i], 0),matchSelector.getTableModel().getValueAt(selectedIndices[i], 1),
						matchSelector.getTableModel().getValueAt(selectedIndices[i], 2), matchSelector.getTableModel().getValueAt(selectedIndices[i], 3)};
				selectedRowData[i] = selectedRow;
				}
			matchSelector.getPartner().addRows(selectedRowData);
			matchSelector.removeRows(selectedIndices);
			}
	}
	
	public void transferAllData(MatchSelector matchSelector){
		int[] selectedIndices = IntStream.range(0, matchSelector.getTable().getRowCount()).toArray(); // = [n-1] = alle Integers zwischen 0 und n-1
		if(selectedIndices.length > 0){
			Object[][] selectedRowData = new Object[selectedIndices.length][matchSelector.getTable().getColumnCount()];
			for(int i = 0; i < selectedIndices.length; i++){
				Object[] selectedRow = {matchSelector.getTableModel().getValueAt(selectedIndices[i], 0),matchSelector.getTableModel().getValueAt(selectedIndices[i], 1),
						matchSelector.getTableModel().getValueAt(selectedIndices[i], 2), matchSelector.getTableModel().getValueAt(selectedIndices[i], 3)};
				selectedRowData[i] = selectedRow;
				}
			matchSelector.getPartner().addRows(selectedRowData);
			matchSelector.removeRows(selectedIndices);
			}
	}
	
	public ArrayList<Match> matchFilesFromSelectorFormat(int[] rows, String playername) throws IOException{
		ArrayList<Match> matches = new ArrayList<Match>();
		for(int i = 0; i < rows.length; i++){
			matches.add(matchFileFromSelectorFormat(rows[i], playername));
		}
		return matches;
	}
	
	public Match matchFileFromSelectorFormat(int row, String playername) throws IOException{
		String matchfile = playername + "-" + matchSelectorRight.getTableModel().getValueAt(row, 0) + "-" + matchSelectorRight.getTableModel().getValueAt(row, 1) + ".csv";
		File file = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + matchfile);
		if(file.exists()){
			Match m = LoadMatchFromTennisAbstract.readCSVFile(new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + matchfile));
			return m;
		}
		else{
			matchfile = matchSelectorRight.getTableModel().getValueAt(row, 0) + "-" + playername + "-" + matchSelectorRight.getTableModel().getValueAt(row, 1) + ".csv";
			file = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + matchfile);
			Match m = LoadMatchFromTennisAbstract.readCSVFile(new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + matchfile));
			return m;
		}
	}
	
	public MatchSelector getMatchSelectorLeft() {
		return matchSelectorLeft;
	}
	public void setMatchSelectorLeft(MatchSelector matchSelectorLeft) {
		this.matchSelectorLeft = matchSelectorLeft;
	}
	public MatchSelector getMatchSelectorRight() {
		return matchSelectorRight;
	}
	public void setMatchSelectorRight(MatchSelector matchSelectorRight) {
		this.matchSelectorRight = matchSelectorRight;
	}

	public JLabel getPlayernameLabel() {
		return playernameLabel;
	}

	public void setPlayernameLabel(JLabel playernameLabel) {
		this.playernameLabel = playernameLabel;
	}
}
