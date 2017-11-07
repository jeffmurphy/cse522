package com.x338x;

public class HALT extends Instruction {

    /*
     bytecode is 16 bits:

     0000 0000 0000 0000
     CCII R1R2 OPER AND.

     CC = 11 : HALT

     II = xx : not used

     R1 = xx : not used
     R2 = xx : not used

     OPERAND = not used

     */

    public static int convert() {
        int bc = Instruction.II_HALT;
        return bc;
    }


    public static void execute(Registers r) {
        r.setST(r.getST() | Registers.HALT);
    }


}
