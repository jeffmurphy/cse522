package com.x338x;
import javax.swing.table.DefaultTableModel;

public class ByteCodeTableModel extends DefaultTableModel {
    private String[] columnNames = {"Cell", "Hex", "Bin"};
    char[] bytecode;

    public ByteCodeTableModel() {
    }

    public void updateByteCode(char[] bytecode) {
        this.bytecode = bytecode;
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    @Override
    public int getRowCount() {
        if (bytecode != null)
            return bytecode.length;
        return 0;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Class getColumnClass(int c) {
        if (c > 0) return String.class;
        return Integer.class;
    }

    public Object getValueAt(int row, int col) {
        if (col == 0) return row;
        if (col == 1) return Integer.toHexString((int)bytecode[row]);

        String s = String.format("%16.16s", Integer.toBinaryString(bytecode[row]));
        s = s.replace(' ', '0');

        return (int) bytecode[row];
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        bytecode[row] = (char)value;
        fireTableCellUpdated(row, col);
    }
}
