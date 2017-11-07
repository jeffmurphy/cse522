package com.x338x;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Instruction {

    // could do a CMP REG, [REG | LIT] ..

    private String[] iPats = {
            "^(LD)\\s([AB])\\s*,\\s*([@]{0,1})(\\d+)$",
            "^(ST)\\s([AB])\\s*,\\s*([@]{1})(\\d+)$",
            "^(ADD)\\s([AB])\\s*,\\s*([AB])$",
            "^(SUB)\\s([AB])\\s*,\\s*([AB])$",
            "^(MUL)\\s([AB])\\s*,\\s*([AB])$",
            "^(DIV)\\s([AB])\\s*,\\s*([AB])$",
            "^(BEQ)\\s([AB])\\s*,\\s*([AB])\\s*,\\s*(\\S+)$",
            "^(BGT)\\s([AB])\\s*,\\s*([AB])\\s*,\\s*(\\S+)$",
            "^(BLT)\\s([AB])\\s*,\\s*([AB])\\s*,\\s*(\\S+)$",
            "^(BNZ)\\s([AB])\\s*,\\s*(\\S+)$",
            "^(HALT)$"
    };

    /*
     bytecode is 16 bits:

     0000 0000 0000 0000
     CCII R1R2 OPER AND.

     CC = 00 : assignment (LD/ST)
          01 : ALU  (ADD/SUB/MUL/DIV)
          10 : branch (BEQ/BGT/BLT/BNZ)
          11 : HALT

     II = 00 : instruction (depends on CC)

     R1 = 00 : A
          01 : B
          10 : LITERAL
          11 : MEM REF
     R2 = 00 : A
          01 : B
          10 : LITERAL
          11 : MEMREF

     OPERAND = value depends on CC and II

     */

    public final static int CC_SHIFT      = 14;
    public final static int II_SHIFT      = 12;
    public final static int R1_SHIFT      = 10;
    public final static int R2_SHIFT      =  8;
    public final static int CC_MASK       = 0xC000;
    public final static int II_MASK       = 0x3000;
    public final static int R1_MASK       = 0x0C00;
    public final static int R2_MASK       = 0x0300;
    public final static int OPERAND_MASK  = 0x00FF;

    public final static int INS_MASK      = CC_MASK | II_MASK;

    public final static int CC_ASSIGN     = 0x0;
    public final static int II_LD         = (CC_ASSIGN << CC_SHIFT) | (0x0 << II_SHIFT);
    public final static int II_ST         = (CC_ASSIGN << CC_SHIFT) | (0x1 << II_SHIFT);

    public final static int CC_ALU        = 0x1;
    public final static int II_ADD        = (CC_ALU << CC_SHIFT) | (0x0 << II_SHIFT);
    public final static int II_SUB        = (CC_ALU << CC_SHIFT) | (0x1 << II_SHIFT);
    public final static int II_MUL        = (CC_ALU << CC_SHIFT) | (0x2 << II_SHIFT);
    public final static int II_DIV        = (CC_ALU << CC_SHIFT) | (0x3 << II_SHIFT);

    public final static int CC_BRANCH     = 0x2;
    public final static int II_BEQ        = (CC_BRANCH << CC_SHIFT) | (0x0 << II_SHIFT);
    public final static int II_BGT        = (CC_BRANCH << CC_SHIFT) | (0x1 << II_SHIFT);
    public final static int II_BLT        = (CC_BRANCH << CC_SHIFT) | (0x2 << II_SHIFT);
    public final static int II_BNZ        = (CC_BRANCH << CC_SHIFT) | (0x3 << II_SHIFT);

    public final static int CC_HALT       = 0x3;
    public final static int II_HALT       = (CC_HALT << CC_SHIFT);

    public final static int REGA          = 0b00;
    public final static int REGB          = 0b01;
    public final static int LITERAL       = 0b10;
    public final static int ADDRESS       = 0b11;

    private Boolean pending = false;
    private String pendingLabel;

    private String i;
    private Map<String, LabelNode> labelMap;

    private int labelNum = -1;
    private String instruction = "";
    private String arg1 = "";
    private String arg2 = "";
    private Boolean addrRef = false;

    private int byteCode;

    public Instruction() {

    }

    public Instruction(String i, Map<String, LabelNode> labelMap) throws Exception {
        this.i = i.trim().replaceAll(" +", " ");
        this.labelMap = labelMap;
        parse();
    }

    public void parse() throws Exception {
        boolean err = true;

        for (String ip : iPats) {
            Pattern lp = Pattern.compile(ip);
            Matcher m = lp.matcher(i);

            if (m.find()) {
                err = false;

                instruction = m.group(1);

                // if it's an instruction that uses a label, try to resolve the label

                if (instruction.equals("BEQ") || instruction.equals("BGT") || instruction.equals("BLT")) {
                    LabelNode ln = labelMap.get(m.group(4));
                    arg1 = m.group(2);
                    arg2 = m.group(3);
                    if (ln == null) {
                        pending = true;
                        pendingLabel = m.group(3);
                    }
                    else {
                        labelNum = ln.iNum;
                        convert(instruction);
                    }
                }

                else if (instruction.equals("BNZ")) {
                    arg1 = m.group(2);
                    LabelNode ln = labelMap.get(m.group(3));
                    if (ln == null) {
                        pending = true;
                        pendingLabel = m.group(3);
                    }
                    else {
                        labelNum = ln.iNum;
                        convert(instruction);
                    }
                }

                else if (instruction.equals("LD") || instruction.equals("ST")) {
                    arg1 = m.group(2);
                    addrRef = false;
                    if (m.group(3).equals("@"))
                        addrRef = true;
                    arg2 = m.group(4);
                    pending = false;
                    labelNum = -1;
                    convert(instruction);
                }

                else if (instruction.equals("ADD") || instruction.equals("SUB") ||
                        instruction.equals("MUL") || instruction.equals("DIV")
                        ) {
                    arg1 = m.group(2);
                    arg2 = m.group(3);
                    pending = false;
                    labelNum = -1;
                    convert(instruction);
                }

                else if (instruction.equals("HALT")) {
                    convert(instruction);
                }
            }
        }

        if (err)
            throw new Exception("Can't parse line: " + i);
    }

    public void convert(String i) {
        if (i.equals("LD"))
            byteCode = LD.convert(arg1, arg2, addrRef);
        else if (i.equals("ST"))
            byteCode = ST.convert(arg1, arg2);
        else if (i.equals("ADD"))
            byteCode = ADD.convert(arg1, arg2);
        else if (i.equals("SUB"))
            byteCode = SUB.convert(arg1, arg2);
        else if (i.equals("MUL"))
            byteCode = MUL.convert(arg1, arg2);
        else if (i.equals("DIV"))
            byteCode = DIV.convert(arg1, arg2);
        else if (i.equals("BNE"))
            byteCode = BEQ.convert(arg1, arg2, labelNum);
        else if (i.equals("BGT"))
            byteCode = BGT.convert(arg1, arg2, labelNum);
        else if (i.equals("BLT"))
            byteCode = BLT.convert(arg1, arg2, labelNum);
        else if (i.equals("BNZ"))
            byteCode = BNZ.convert(arg1, labelNum);
        else if (i.equals("HALT"))
            byteCode = HALT.convert();
    }

    public static void execute(Registers registers, int[] memory, int instruction) throws Exception {
        int nextIns = -1;

        switch (instruction & INS_MASK) {
            case II_LD:
                LD.execute(registers, memory, (instruction & R1_MASK) >> R1_SHIFT, (instruction & R2_MASK) >> R2_SHIFT, instruction & OPERAND_MASK);
                break;
            case II_ST:
                ST.execute(registers, memory, (instruction & R1_MASK) >> R1_SHIFT, instruction & OPERAND_MASK);
                break;
            case II_ADD:
                ADD.execute(registers, (instruction & R1_MASK) >> R1_SHIFT, (instruction & R2_MASK) >> R2_SHIFT);
                break;
            case II_SUB:
                SUB.execute(registers, (instruction & R1_MASK) >> R1_SHIFT, (instruction & R2_MASK) >> R2_SHIFT);
                break;
            case II_MUL:
                MUL.execute(registers, (instruction & R1_MASK) >> R1_SHIFT, (instruction & R2_MASK) >> R2_SHIFT);
                break;
            case II_DIV:
                DIV.execute(registers, (instruction & R1_MASK) >> R1_SHIFT, (instruction & R2_MASK) >> R2_SHIFT);
                break;
            case II_BEQ:
                nextIns = BEQ.execute(registers, (instruction & R1_MASK) >> R1_SHIFT, (instruction & R2_MASK) >> R2_SHIFT, instruction & OPERAND_MASK);
                break;
            case II_BGT:
                nextIns = BGT.execute(registers, (instruction & R1_MASK) >> R1_SHIFT, (instruction & R2_MASK) >> R2_SHIFT, instruction & OPERAND_MASK);
                break;
            case II_BLT:
                nextIns = BLT.execute(registers, (instruction & R1_MASK) >> R1_SHIFT, (instruction & R2_MASK) >> R2_SHIFT, instruction & OPERAND_MASK);
                break;
            case II_BNZ:
                nextIns = BNZ.execute(registers, (instruction & R1_MASK) >> R1_SHIFT, instruction & OPERAND_MASK);
                break;
            case II_HALT:
                HALT.execute(registers);
                break;
            default:
                throw new Exception("Unknown instruction in CC|II fields: " + (instruction & INS_MASK));
        }

        if (nextIns == -1)
            registers.setPC(registers.getPC() + 1);
        else
            registers.setPC(nextIns);
    }

    public Boolean getPending() {
        return pending;
    }

    public int getByteCode() {
        return byteCode;
    }

    public void setByteCode(int byteCode) {
        this.byteCode = byteCode;
    }

    public int getLabelNum() {
        return labelNum;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getPendingLabel() {
        return pendingLabel;
    }


    public static int getregval(Registers r, int reg) throws Exception {
        switch (reg) {
            case Instruction.REGA:
                return r.getA();
            case Instruction.REGB:
                return r.getB();
            default:
                throw new Exception("Invalid register: " + reg);
        }
    }

    public static void setregval(Registers r, int reg, int val) throws Exception {
        switch (reg) {
            case Instruction.REGA:
                r.setA(val);
                break;
            case Instruction.REGB:
                r.setB(val);
                break;
            default:
                throw new Exception("Invalid register: " + reg);
        }
    }

}
