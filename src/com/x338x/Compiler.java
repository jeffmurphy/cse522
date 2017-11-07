package com.x338x;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//http://jakubdziworski.github.io/java/2016/04/01/antlr_visitor_vs_listener.html

public class Compiler {
    private int iNum = 0;
    List<Integer> byteCodes = new ArrayList<>();

    /* map 'label' to instruction number / instruction */
    private Map<String, LabelNode> labelMap = new HashMap<String, LabelNode>();
    /* map labels pending resolution to inst num / instruction */
    private Map<String, LabelNode> pendingLabelMap = new HashMap<String, LabelNode>();

    public void compile(String program) throws Exception {
        try {
            labelMap.clear();
            pendingLabelMap.clear();
            iNum = 0;
            byteCodes.clear();

            for (String line : program.split("\n")) {
                parseLine(line);
            }

            Iterator it = pendingLabelMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                LabelNode ln = pendingLabelMap.get(pair.getKey());

                System.out.println("plab: " + pair.getKey());
                if (labelMap.containsKey(pair.getKey())) {
                    iNum = ln.iNum;
                    parseLine(ln.instruction);
                }
                else {
                    System.out.println("Tried to recompile '" + ln.instruction +
                            "' but label is still not known: " + pair.getKey());
                    throw new Exception("Failed to resolve label " + pair.getKey());
                }
            }

        } catch(Exception e) {
            System.out.println("parse failed: " + e);
            e.printStackTrace();
            throw e;
        }
    }

    public void parseLine(String line) throws Exception {
        String label_pattern = "(\\S+):\\s*(.*)";
        Pattern lp = Pattern.compile(label_pattern);
        Matcher m = lp.matcher(line);
        String instruction = line;
        String label = null;

        line = line.trim().replaceAll(" +", " ");
        if (line.equals("") || line.startsWith(";")) return;

        if (m.find()) {
            label = m.group(1);
            instruction = m.group(2);

            if (labelMap.get(label) == null)
                labelMap.put(label, new LabelNode(iNum, instruction));
            else {
                LabelNode ln = labelMap.get(label);
                if (ln.iNum != iNum)
                    throw new Exception("Duplicate label " + ln.iNum + " and " + iNum);
            }
        }

        System.out.println(iNum + ") lab: " + label + " ins: " + instruction);
        Instruction ins = new Instruction(instruction, labelMap);

        if (ins.getPending()) {
            pendingLabelMap.put(ins.getPendingLabel(), new LabelNode(iNum, instruction));
            byteCodes.add(0); // placeholder
        }
        else {
            if (byteCodes.size() == iNum)
                byteCodes.add(ins.getByteCode());
            else
                byteCodes.set(iNum, ins.getByteCode());
        }

        iNum += 1;
    }


}
