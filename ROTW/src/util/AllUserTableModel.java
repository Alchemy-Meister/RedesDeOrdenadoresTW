package util;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class AllUserTableModel extends DefaultTableModel {
	public AllUserTableModel(int rowcount, int columncount) {
		super(rowcount, columncount);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    if (columnIndex == 2)
	        return Boolean.class;
	    else if(columnIndex == 3)
	    	return Boolean.class;
	    return super.getColumnClass(columnIndex);
	}
	
	@Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(columnIndex == 2) {
			if((Boolean)aValue) {
				super.setValueAt((!(Boolean)aValue), rowIndex, columnIndex + 1);
			}
			super.setValueAt(aValue, rowIndex, columnIndex);
		} else if(columnIndex == 3) {
			if((Boolean)aValue) {
				super.setValueAt((!(Boolean)aValue), rowIndex, columnIndex - 1);
			}
			super.setValueAt(aValue, rowIndex, columnIndex);
		} else {
			super.setValueAt(aValue, rowIndex, columnIndex);
		}
	}
}
