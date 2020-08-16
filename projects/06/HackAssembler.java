import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class HackAssembler {
    int nextAddress = 16;
    SymbolTable st;
    HashMap<String, String> compMap = new HashMap<>() {
        {
            put("0", "0101010");
            put("1", "0111111");
            put("-1", "0111010");
            put("D", "0001100");
            put("A", "0110000");
            put("!D", "0001101");
            put("!A", "0110001");
            put("-D", "0001111");
            put("-A", "0110011");
            put("D+1", "0011111");
            put("A+1", "0110111");
            put("D-1", "0001110");
            put("A-1", "0110010");
            put("D+A", "0000010");
            put("D-A", "0010011");
            put("A-D", "0000111");
            put("D&A", "0000000");
            put("D|A", "0010101");
            put("M", "1110000");
            put("!M", "1110001");
            put("-M", "1110011");
            put("M+1", "1110111");
            put("M-1", "1110010");
            put("D+M", "1000010");
            put("D-M", "1010011");
            put("M-D", "1000111");
            put("D&M", "1000000");
            put("D|M", "1010101");
        }
    };

    HashMap<String, String> jmpMap = new HashMap<>() {
        {
            put("JGT", "001");
            put("JEQ", "010");
            put("JGE", "011");
            put("JLT", "100");
            put("JNE", "101");
            put("JLE", "110");
            put("JMP", "111");
        }
    };

    public HackAssembler() {
        st = SymbolTable.getInstance();
    }

    public String parseAInstruction(String instruction) {
        String address = instruction.substring(1, instruction.length());
        // System.out.println("A instruction: " + address);
        int value;
        try {
            value = Integer.parseInt(address);
        } catch (NumberFormatException e) {
            
            if (!st.contains(address)) {
                // add var sysmbol
                st.addEntry(address, nextAddress);
                nextAddress++;
            }
            value = st.getAddress(address);
        }
        String valInBinary = convertToBinary(value, 15);
        StringBuilder sb = new StringBuilder();
        sb.append("0");
        sb.append(valInBinary);

        System.out.println(sb.toString());
        return sb.toString();
    }

    public String parseCInstruction(String instruction) {
        instruction = instruction.trim().replaceAll("\\s", "");

        String opCode = "111";

        String dest = null;
        String comp = null;
        String jmp = null;

        String[] tokens = new String[2];

        if (instruction.contains("=")) {
            tokens = instruction.split("=");
            dest = tokens[0];
            instruction = tokens[1];
        }
        if (instruction.contains(";")) {
            tokens = instruction.split(";");
            comp = tokens[0];
            jmp = tokens[1];
        } else {
            comp = instruction;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(opCode);
        sb.append(getBinaryComp(comp));
        sb.append(getBinaryDest(dest));
        sb.append(getBinaryJmp(jmp));

        return sb.toString();
    }

    public String getBinaryDest(String dest) {
        if (dest == null)
            return "000";
        char[] dests = { '0', '0', '0' };
        if (dest.contains("M")) {
            dests[2] = '1';
        }
        if (dest.contains("D")) {
            dests[1] = '1';
        }

        if (dest.contains("A")) {
            dests[0] = '1';
        }

        return new String(dests);
    }

    public String getBinaryJmp(String jmp) {
        if (jmp == null)
            return "000";
        return jmpMap.get(jmp);
    }

    public String getBinaryComp(String comp) {
        return compMap.get(comp);
    }

    public String convertToBinary(int x, int len) {
        StringBuilder sb = new StringBuilder();

        for (int i = len - 1; i >= 0; i--) {
            int mask = 1 << i;
            sb.append((x & mask) != 0 ? 1 : 0);
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        // initializes I/O files and drives the process
        String fileName = args[0];
        Scanner reader = new Scanner(new FileInputStream(fileName));
        
        ArrayList<String> instructions = new ArrayList<>();
        while (reader.hasNextLine()) {
            // remove new line comments
            String line = reader.nextLine();
            if (!line.startsWith("//") && !line.isEmpty()){
                // remove inline comments
                if (line.contains("//")) {
                    line = line.substring(0, line.indexOf("//"));
                }
                instructions.add(line.trim());
            }
        }

        // Add label symbol
        int lineNum = 0;
        for (int i = 0; i < instructions.size(); i++) {
            String instr = instructions.get(i);
            if (instr.startsWith("(") && instr.endsWith(")")) {
                String symbol = instr.substring(1, instr.indexOf(")"));
                int address = i - lineNum;
                SymbolTable.getInstance().addEntry(symbol, address);
                lineNum++;
            }
        }

        String outName = fileName.substring(0,fileName.indexOf(".")) + ".hack";

        FileWriter writer = new FileWriter(outName);
        HackAssembler ha = new HackAssembler();
        String binaryInstruction = "";
        for (String instr : instructions) {
            if (instr.startsWith("(")) {
                continue;
            } else if (instr.startsWith("@")) {
                binaryInstruction = ha.parseAInstruction(instr);
            } else {
                binaryInstruction = ha.parseCInstruction(instr);
            }
            writer.write(binaryInstruction);
            writer.write(System.getProperty("line.separator"));
        }
        writer.close();
    }
}