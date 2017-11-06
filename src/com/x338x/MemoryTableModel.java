package com.x338x;
import javax.swing.table.DefaultTableModel;


public class MemoryTableModel extends DefaultTableModel {
    private String[] columnNames = {"Cell", "Dec", "Hex"};
    char[] memory;

    public MemoryTableModel() {
    }

    public void updateMemory(char[] mem) {
        memory = mem;
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    @Override
    public int getRowCount() {
        if (memory != null)
            return memory.length;
        return 0;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Class getColumnClass(int c) {
        if (c == 2) return String.class;
        return Integer.class;
    }

    public Object getValueAt(int row, int col) {
        if (col == 0) return row;
        if (col == 2) return Integer.toHexString((int)memory[row]);
        return (int) memory[row];
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        memory[row] = (char)value;
        fireTableCellUpdated(row, col);
    }
}
