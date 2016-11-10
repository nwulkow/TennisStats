package gui_wb;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import tools.Tools;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class PlayerSelector extends JPanel {
	
	private PlayerSelector partner;
	
	private JTable table;
	private JScrollPane scrollPane;
	private DefaultTableModel tableModel;
	private Object[][] data = {
		    /*{"Kathy", "Smith",},
			    {"John", "Doe",},*/
			};
	private String[] columnNames = {"Name",
        "Wins",
        "Losses",
        "Points Won %"};

	/**
	 * Create the panel.
	 */
	public PlayerSelector() {
		
		tableModel = new DefaultTableModel(data, columnNames){
			// Soll verhindern, dass JTable in der GUI veraendert wird. Klappt aber noch nicht
			public boolean isCellEditable(int row, int column){
			     return false;
			}
		};
		setLayout(null);
		
		table = new JTable(tableModel);
		RowSorter<TableModel> sorter =
		  new TableRowSorter<TableModel>(tableModel);
		table.setRowSorter(sorter);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 452, 170);
		add(scrollPane);
		
		scrollPane.setViewportView(table);
		table.setAutoCreateRowSorter(true);
		
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//table.getColumnModel().getColumn(2).setPreferredWidth(50);

	}
	
	
	public void reinit(){
		tableModel = new DefaultTableModel(data, columnNames);
		table.setModel(tableModel);
		RowSorter<TableModel> sorter =
				  new TableRowSorter<TableModel>(tableModel);
				table.setRowSorter(sorter);
	}

	public Object[][] getData(){
		return data;
	}
	
	public void setData(Object[][] data){
		this.data = data;
		reinit();
	}
	
	public void addRow(Object[] row){
		if(data.length == 0){
			Object[][] newData = new Object[1][row.length];
			newData[0] = row;
			data = newData;
		}
		else{
			Object[][] updatedData = new Object[data.length+1][data[0].length];
			for(int i = 0; i < data.length; i++){
				updatedData[i] = data[i];
			}
			updatedData[data.length] = row;
			data = updatedData;
		}
		reinit();
	}
	
	public void addRows(Object[][] rows){
		for(Object[] row : rows){
			addRow(row);
		}
	}
	
	public void removeRow(Object[] row){
		if(data.length == 0){
			return;
		}
		// Erstmal die Zeile finden im urspruenglichen Datensatz
		int rowIndex = -1;
		for(int i = 0; i < data.length; i++){
			boolean rowFound = true;
			for(int j = 0; j < data[0].length; j++){
				if(row[j].equals(data[i][j])){
					rowFound = false;
					break;
				}
			}
			if(rowFound){
				rowIndex = i;
				break;
			}
		}
		removeRow(rowIndex);
	}
	
	public void removeRow(int rowIndex){
		if(data.length > 0){
			Object[][] updatedData = new Object[data.length-1][data[0].length];
			for(int i = 0; i < rowIndex; i++){
				updatedData[i] = data[i];
			}
			for(int i = rowIndex; i < updatedData.length; i++){
				updatedData[i] = data[i+1];
			}
			data = updatedData;
				reinit();
		}
	}
	
	public void removeRows(Object[][] rows){
		for(Object[] row : rows){
			removeRow(row);
		}
	}
	
	public void removeRows(int[] rowIndices){
		for(int i = 0; i < rowIndices.length; i++){
			removeRow(rowIndices[i]);
			for(int j = i + 1; j < rowIndices.length; j++){
					rowIndices[j] -= 1;
			}
		}
	}
	
	
	public void addColumn(Object[] column){
		Object[][] updatedData = new Object[data.length][data[0].length+1];
		for(int i = 0; i < data.length; i++){
			for(int j = 0; j < data[0].length; j++){
				updatedData[i][j] = data[i][j];
			}
			updatedData[i][data[0].length] = column[i];
		}
		data = updatedData;
		reinit();
	}
	
	public void deleteData(){
		if(data.length > 0){
			data = new Object[0][0];
		}
		reinit();
	}
	
	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}


	public PlayerSelector getPartner() {
		return partner;
	}


	public void setPartner(PlayerSelector partner) {
		this.partner = partner;
	}
	
	
}
