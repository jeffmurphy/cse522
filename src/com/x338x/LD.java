package com.x338x;

public class LD extends Instruction {

    /*
     bytecode is 16 bits:

     0000 0000 0000 0000
     CCII R1R2 OPER AND.

     CC = 00 : assignment (LD/ST)

     II = 00 : LD
          01 : ST

     R1 = 00 : A
          01 : B
     R2 = 00 : ignored
          02 : ignored

     OPERAND = memory address

     */

    public static int convert(String arg1, String arg2) {
        int bc = Instruction.II_LD;

        if (arg1.equals("A"))
            bc |= Instruction.REGA << Instruction.R1_SHIFT;
        else if (arg1.equals("B"))
            bc |= Instruction.REGB << Instruction.R1_SHIFT;

        // R2 is ignored

        int memloc = Integer.parseInt(arg2);
        bc |= (Instruction.OPERAND_MASK & memloc);

        return bc;
    }

}
