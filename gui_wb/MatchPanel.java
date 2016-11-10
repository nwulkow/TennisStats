package gui_wb;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import analytics.PlayerComparison;
import dataload.LoadValues;
import graphics.MatrixImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MatchPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	PlayerCoordinator playerCoordinator;
	public MatchPanel() throws IOException {
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 906, 683);
		add(panel);
		panel.setLayout(null);
		
		playerCoordinator = new PlayerCoordinator();
		playerCoordinator.setBounds(0, 38, 868, 177);
		panel.add(playerCoordinator);
		//playerCoordinator.getPlayerSelectorRight().setData(LoadValues.loadPlayerDataForPlayerSelector());
		
		JPanel matriximagePanel = new JPanel();
		matriximagePanel.setBounds(140, 198, 455, 210);
		panel.add(matriximagePanel);
		
		JButton makematrixButton = new JButton("Matrix");
		makematrixButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> playernames = new ArrayList<String>();
				for(int i = 0; i < playerCoordinator.getPlayerSelectorRight().getTableModel().getRowCount(); i++){
					playernames.add(playerCoordinator.getPlayerSelectorRight().getTableModel().getValueAt(i, 0).toString());
				}
				double[][] resultRatioMatrix = new double[0][0];
				try {
					resultRatioMatrix = PlayerComparison.createResultMatrix(playernames,false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				MatrixImage image = new MatrixImage(resultRatioMatrix, 400, 400, "", false);
				image.setContainer(matriximagePanel);
				image.addAMouseListener(matriximagePanel);
				matriximagePanel.add(new JLabel(new ImageIcon(image)));
			}
		});
		makematrixButton.setBounds(12, 254, 97, 25);
		panel.add(makematrixButton);
		
		


	}
}
