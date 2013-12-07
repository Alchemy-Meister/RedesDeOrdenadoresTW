package util;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class UserTableModel extends DefaultTableModel {
	public UserTableModel(int rowcount, int columncount) {
		super(rowcount, columncount);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    if (columnIndex == 2)
	        return Boolean.class;
	    return super.getColumnClass(columnIndex);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
	    return (col == 2); 
	}
}
