package com.x338x;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//http://jakubdziworski.github.io/java/2016/04/01/antlr_visitor_vs_listener.html

public class Compiler {
    private int iNum = 0;

    /* map 'label' to instruction number / instruction */
    private Map<String, LabelNode> labelMap = new HashMap<String, LabelNode>();
    /* map labels pending resolution to inst num / instruction */
    private Map<String, LabelNode> pendingLabelMap = new HashMap<String, LabelNode>();

    private class LabelNode {
        int iNum;
        String instruction;
        LabelNode(int n, String i) {
            iNum = n;
            instruction = i;
        }
    }


    public void compile(String program) {
        try {
            String tp = new String("    LD A, 10\n" +
                    "    LD B, 1\n" +
                    "l1: SUB A, B\n" +
                    "    BNZ A, :l1 \n" +
                    "\n" +
                    "\n");

            for (String line : tp.split("\n")) {
                System.out.println(line);
                parseLine(line);
            }
        } catch(Exception e) {
            System.out.println("parse failed: " + e);
            e.printStackTrace();
        }
    }

    public void parseLine(String line) throws Exception {
        String label_pattern = "(\\S+):\\s*(.*)";
        Pattern lp = Pattern.compile(label_pattern);
        Matcher m = lp.matcher(line);
        String instruction = line;
        String label = "";

        if (m.find()) {
            label = m.group(1);
            instruction = m.group(2);

            if (labelMap.get(label) != null) {
                throw new Exception("duplicate label");
            }

            labelMap.put(label, new LabelNode(iNum, instruction));
        }

        System.out.println("lab: " + label + " ins: " + instruction);



    }

    public void LD(String reg, String addr) {
        System.out.println(iNum + ": LD " + reg + " " + addr);
    }

    public void ST(String reg, String addr) {
        System.out.println(iNum + ": ST " + reg + " " + addr);
    }

    public void ADD(String dst, String src) {
        System.out.println(iNum + ": ADD " + dst + " " + src);
    }



}
