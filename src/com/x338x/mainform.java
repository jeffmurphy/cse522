package com.x338x;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainform {
    private JPanel mainpanel;
    private JTextArea codeEditor;
    private JButton compileButton;
    private JButton runButton;
    private JButton stepButton;
    private JButton resetButton;
    private JTable bytecodeTable;
    private JTable memoryTable;
    private JLabel pcLabel;
    private JLabel stLabel;
    private JLabel aregLabel;
    private JLabel bregLabel;

    public mainform() {

        MemoryTableModel mtm = new MemoryTableModel();
        memoryTable.setModel(mtm);
        mtm.fireTableDataChanged();
        memoryTable.getColumnModel().getColumn(0).setMaxWidth(36);

        VirtualMachine vm = new VirtualMachine(mtm, pcLabel, stLabel, aregLabel, bregLabel);

        Compiler compiler = new Compiler();
        compiler.compile(codeEditor.getText());
        compileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        mainform mf = new mainform();

        JFrame mainpanel = new JFrame("Compiler");
        mainpanel.setContentPane(mf.mainpanel);
        mainpanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainpanel.pack();
        mainpanel.setVisible(true);
    }
}
