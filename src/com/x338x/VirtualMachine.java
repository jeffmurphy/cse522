package com.x338x;

import javax.swing.*;

public class VirtualMachine {
    char[] memory;
    char[] bytecode;

    Registers registers;
    MemoryTableModel mtm;

    JLabel pcLabel, stLabel, aregLabel, bregLabel;

    public VirtualMachine(MemoryTableModel _mtm,
                          JLabel _pcLabel,
                          JLabel _stLabel,
                          JLabel _aregLabel,
                          JLabel _bregLabel) {
        memory = new char[10];
        registers = new Registers();

        mtm = _mtm;
        mtm.updateMemory(memory);
        pcLabel = _pcLabel;
        stLabel = _stLabel;
        aregLabel = _aregLabel;
        bregLabel = _bregLabel;

        visualizeRegisters();
    }

    public void visualizeRegisters() {
        pcLabel.setText("PC: " + registers.getPC());
        stLabel.setText("ST: " + registers.getST_asString());
        aregLabel.setText(" A: " + registers.getA());
        bregLabel.setText(" B: " + registers.getB());
    }


    public char[] getBytecode() {
        return bytecode;
    }

    public void setBytecode(char[] bytecode) {
        this.bytecode = bytecode;
    }

    public void step() {
        // use registers.getPC() setPC()
    }

    public void run() {
        registers.reset();

        if (bytecode != null || bytecode.length > 0) {
            System.out.println("running..");
        }
        else {
            System.out.println("Bytecode empty. Compile first?");
        }
    }
}
