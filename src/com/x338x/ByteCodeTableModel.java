package com.x338x;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;


public class ByteCodeTableModel extends DefaultTableModel {
    private String[] columnNames = {"Inst#", "Dec", "Bin"};
    List<Integer> byteCodes = new ArrayList<>();

    public void setBytecode(List<Integer> byteCodes) {
        this.byteCodes = byteCodes;
        fireTableDataChanged();
    }

    public ByteCodeTableModel() {
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    @Override
    public int getRowCount() {
        if (byteCodes != null && byteCodes.isEmpty() == false)
            return byteCodes.size();
        return 0;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Class getColumnClass(int c) {
        if (c == 2) return String.class;
        return Integer.class;
    }

    public String itob(int i) {
        return String.format("%16.16s", Integer.toBinaryString(i)).replace(' ', '0');
    }

    public Object getValueAt(int row, int col) {
        if (col == 0) return row;
        if (col == 2) return itob((int)byteCodes.get(row)); //Integer.toHexString((int)byteCodes.get(row));
        return (int) byteCodes.get(row);
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        byteCodes.set(row, (int)value);
        fireTableCellUpdated(row, col);
    }
}
