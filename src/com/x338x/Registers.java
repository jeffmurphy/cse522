package com.x338x;

public class Registers {
    public final static int MASK      = 0xff;
    public final static int OVERFLOW  = 128;
    public final static int UNDERFLOW = 64;
    public final static int BUSERROR  = 32;
    public final static int CARRY     = 16;
    public final static int HALT      = 1;

    // even tho these are ints, ST/A/B are masked to only
    // first byte. we allow the PC to be a full int.

    private int PC;
    private int ST;
    private int A;
    private int B;

    public Registers() {
        reset();
    }

    public void reset() {
        PC = 0;
        ST = 0;
        A = 0;
        B = 0;
    }

    public int getPC() {
        return PC;
    }

    public int getST() {
        /*
           Overflow
           |Underflow
           ||BusError
           |||Carry
           ||||   Halt
           ||||   |
           0000xxx0
           x = not used
         */
        return ST;
    }

    public void setBuserror() {
        ST |= BUSERROR;
    }

    public void setOverflow() {
        ST |= OVERFLOW;
    }

    public void setUnderflow() {
        ST |= UNDERFLOW;
    }

    public void setCarry() {
        ST |= CARRY;
    }

    public void setHalt() {
        ST |= HALT;
    }

    public String getST_asString() {
        char[] s = new char[] {'0','0','0','0','x','x','x','0'};

        if ((this.ST & OVERFLOW) == OVERFLOW)
            s[0] = 'O';
        if ((this.ST & UNDERFLOW) == UNDERFLOW)
            s[1] = 'U';
        if ((this.ST & BUSERROR) == BUSERROR)
            s[2] = 'B';
        if ((this.ST & CARRY) == CARRY)
            s[2] = 'C';
        if ((this.ST & HALT) == HALT)
            s[7] = 'H';
        return new String(s);
    }

    public int getA() {
        return A;
    }

    public int getB() {
        return B;
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public void setST(int ST) {
        this.ST = ST & MASK;
    }

    public void setA(int a) {
        A = a & MASK;
    }

    public void setB(int b) {
        B = b & MASK;
    }

}
