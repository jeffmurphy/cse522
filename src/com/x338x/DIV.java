package com.x338x;

public class DIV extends Instruction {

    /*
     bytecode is 16 bits:

     0000 0000 0000 0000
     CCII R1R2 OPER AND.

     CC = 01 : ALU (ADD/SUB/MUL/DIV)

     II = 00 : ADD
          01 : SUB
          11 : MUL
          10 : DIV

     R1 = 00 : A
          01 : B
     R2 = 00 : A
          01 : B

     OPERAND = ignored

     */

    public static int convert(String arg1, String arg2) {
        int bc = Instruction.II_DIV;

        // ADD A, A is ok

        if (arg1.equals("A"))
            bc |= Instruction.REGA << Instruction.R1_SHIFT;
        else if (arg1.equals("B"))
            bc |= Instruction.REGB << Instruction.R1_SHIFT;

        if (arg2.equals("A"))
            bc |= Instruction.REGA << Instruction.R2_SHIFT;
        else if (arg2.equals("B"))
            bc |= Instruction.REGB << Instruction.R2_SHIFT;

        return bc;
    }

}
