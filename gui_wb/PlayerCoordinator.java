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

public class PlayerCoordinator extends JPanel {
	
	
	private PlayerSelector playerSelectorLeft, playerSelectorRight;
	private JButton moveLeftButton;
	private JButton moveallrightButton;
	private JButton moveallleftButton;
	private JLabel playernameLabel;
	/**
	 * Create the panel.
	 */
	public PlayerCoordinator() {
		setLayout(null);
				
				playerSelectorRight = new PlayerSelector();
				playerSelectorRight.getScrollPane().setSize(349, 120);
				playerSelectorRight.getScrollPane().setLocation(0, 0);
				//playerSelectorRight.getScrollPane().setBounds(0, 0, 452, 72);
				playerSelectorRight.setBounds(517, 0, 349, 120);
				add(playerSelectorRight);
				
						
				JButton moverightButton = new JButton("Move -->");
				moverightButton.setBounds(370, 0, 132, 29);
				add(moverightButton);
						
				playerSelectorLeft = new PlayerSelector();
				playerSelectorLeft.getScrollPane().setBounds(0, 0, 354, 125);
				playerSelectorLeft.setBounds(0, 0, 355, 125);
				add(playerSelectorLeft);
				
				playerSelectorLeft.setPartner(playerSelectorRight);
				playerSelectorRight.setPartner(playerSelectorLeft);
				
				moveLeftButton = new JButton("<-- Move");
				moveLeftButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						transferData(playerSelectorRight);
					}
				});
				moveLeftButton.setBounds(370, 67, 132, 29);
				add(moveLeftButton);
				
				moveallrightButton = new JButton("Move All -->");
				moveallrightButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						transferAllData(playerSelectorLeft);
					}
				});
				moveallrightButton.setBounds(370, 30, 132, 29);
				add(moveallrightButton);
				
				moveallleftButton = new JButton("<-- Move All");
				moveallleftButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						transferAllData(playerSelectorRight);
					}
				});
				moveallleftButton.setBounds(370, 96, 132, 29);
				add(moveallleftButton);
				
				playernameLabel = new JLabel("");
				playernameLabel.setBounds(10, 140, 220, 51);
				add(playernameLabel);
				//playerSelectorLeft.getScrollPane().setSize(433, 109);
				//playerSelectorLeft.getScrollPane().setLocation(0, 36);
				moverightButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {		
						transferData(playerSelectorLeft);
					}
				});

	}
	
	public void transferData(PlayerSelector playerselector){

		int[] selectedIndices = playerselector.getTable().getSelectedRows();
		for (int i = 0; i < selectedIndices.length; i++) {
			selectedIndices[i] = playerselector.getTable().convertRowIndexToModel(selectedIndices[i]);
		}
		if(selectedIndices.length > 0){
			Object[][] selectedRowData = new Object[selectedIndices.length][playerselector.getTable().getColumnCount()];
			for(int i = 0; i < selectedIndices.length; i++){
				Object[] selectedRow = {playerselector.getTableModel().getValueAt(selectedIndices[i], 0),playerselector.getTableModel().getValueAt(selectedIndices[i], 1),
						playerselector.getTableModel().getValueAt(selectedIndices[i], 2), playerselector.getTableModel().getValueAt(selectedIndices[i], 3)};
				selectedRowData[i] = selectedRow;
				}
			playerselector.getPartner().addRows(selectedRowData);
			playerselector.removeRows(selectedIndices);
			}
	}
	
	public void transferAllData(PlayerSelector playerselector){
		int[] selectedIndices = IntStream.range(0, playerselector.getTable().getRowCount()).toArray(); // = [n-1] = alle Integers zwischen 0 und n-1
		if(selectedIndices.length > 0){
			Object[][] selectedRowData = new Object[selectedIndices.length][playerselector.getTable().getColumnCount()];
			for(int i = 0; i < selectedIndices.length; i++){
				Object[] selectedRow = {playerselector.getTableModel().getValueAt(selectedIndices[i], 0),playerselector.getTableModel().getValueAt(selectedIndices[i], 1),
						playerselector.getTableModel().getValueAt(selectedIndices[i], 2), playerselector.getTableModel().getValueAt(selectedIndices[i], 3)};
				selectedRowData[i] = selectedRow;
				}
			playerselector.getPartner().addRows(selectedRowData);
			playerselector.removeRows(selectedIndices);
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
		String matchfile = playername + "-" + playerSelectorRight.getTableModel().getValueAt(row, 0) + "-" + playerSelectorRight.getTableModel().getValueAt(row, 1) + ".csv";
		File file = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + matchfile);
		if(file.exists()){
			Match m = LoadMatchFromTennisAbstract.readCSVFile(new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + matchfile));
			return m;
		}
		else{
			matchfile = playerSelectorRight.getTableModel().getValueAt(row, 0) + "-" + playername + "-" + playerSelectorRight.getTableModel().getValueAt(row, 1) + ".csv";
			file = new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + matchfile);
			Match m = LoadMatchFromTennisAbstract.readCSVFile(new File("C:/Users/Niklas/TennisStatsData/SackmannRawData/" + matchfile));
			return m;
		}
	}
	
	public PlayerSelector getPlayerSelectorLeft() {
		return playerSelectorLeft;
	}
	public void setPlayerSelectorLeft(PlayerSelector playerSelectorLeft) {
		this.playerSelectorLeft = playerSelectorLeft;
	}
	public PlayerSelector getPlayerSelectorRight() {
		return playerSelectorRight;
	}
	public void setPlayerSelectorRight(PlayerSelector playerSelectorRight) {
		this.playerSelectorRight = playerSelectorRight;
	}

	public JLabel getPlayernameLabel() {
		return playernameLabel;
	}

	public void setPlayernameLabel(JLabel playernameLabel) {
		this.playernameLabel = playernameLabel;
	}
}
