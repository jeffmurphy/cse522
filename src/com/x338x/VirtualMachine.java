package com.x338x;

import javax.swing.JLabel;
import java.util.List;

public class VirtualMachine {
    private char[] memory;
    private List<Integer> byteCodes;

    Registers registers;
    MemoryTableModel mtm;
    ByteCodeTableModel bctm;


    JLabel pcLabel, stLabel, aregLabel, bregLabel;

    public VirtualMachine(MemoryTableModel _mtm,
                          ByteCodeTableModel _bctm,
                          JLabel _pcLabel,
                          JLabel _stLabel,
                          JLabel _aregLabel,
                          JLabel _bregLabel) {
        memory = new char[10];
        registers = new Registers();

        bctm = _bctm;
        mtm = _mtm;
        mtm.updateMemory(memory);
        pcLabel = _pcLabel;
        stLabel = _stLabel;
        aregLabel = _aregLabel;
        bregLabel = _bregLabel;

        visualizeRegisters();
    }

    private void visualizeRegisters() {
        pcLabel.setText("PC: " + registers.getPC());
        stLabel.setText("ST: " + registers.getST_asString());
        aregLabel.setText(" A: " + registers.getA());
        bregLabel.setText(" B: " + registers.getB());
    }

    public List<Integer> getByteCodes() {
        return byteCodes;
    }

    public void setByteCodes(List<Integer> byteCodes) {
        this.byteCodes = byteCodes;
    }

    public void reset() {
        registers.reset();
        memory = new char[10];
        mtm.updateMemory(memory);
        visualizeRegisters();
    }

    public void step() {
        // use registers.getPC() && setPC() to advance each instruction
    }

    public void run() {
        if (byteCodes != null && byteCodes.size() > 0) {
            System.out.println("running..");
            // XXX fill me in
        }
        else {
            System.out.println("Bytecode empty. Compile first?");
        }
    }
}
