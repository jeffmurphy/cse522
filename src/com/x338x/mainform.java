package com.x338x;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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

        ByteCodeTableModel bctm = new ByteCodeTableModel();
        bytecodeTable.setModel(bctm);
        bytecodeTable.getColumnModel().getColumn(0).setMaxWidth(36);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        bytecodeTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        memoryTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);


        VirtualMachine vm = new VirtualMachine(mtm, bctm, pcLabel, stLabel, aregLabel, bregLabel);

        Compiler compiler = new Compiler();

        compileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    compiler.compile(codeEditor.getText());
                    bctm.setBytecode(compiler.byteCodes);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(mainpanel.getParent(),
                            e1.toString(),
                            "Compilation Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("Compilation finished.");
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    vm.setByteCodes(compiler.byteCodes);
                    vm.run();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(mainpanel.getParent(),
                            e1.toString(),
                            "Execution Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("Execution finished.");
            }
        });

        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    vm.setByteCodes(compiler.byteCodes);
                    vm.reset();
                    vm.step();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(mainpanel.getParent(),
                            e1.toString(),
                            "Execution Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("Execution finished.");
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vm.reset();
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
