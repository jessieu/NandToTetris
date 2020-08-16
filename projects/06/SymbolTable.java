import java.util.HashMap;

public class SymbolTable {
    HashMap<String, Integer> symTable;

    private static SymbolTable instance;

    private SymbolTable() {
        symTable = new HashMap<String, Integer>() {
            {
                put("R0", 0);
                put("R1", 1);
                put("R2", 2);
                put("R3", 3);
                put("R4", 4);
                put("R5", 5);
                put("R6", 6);
                put("R7", 7);
                put("R8", 8);
                put("R9", 9);
                put("R10", 10);
                put("R11", 11);
                put("R12", 12);
                put("R13", 13);
                put("R14", 14);
                put("R15", 15);
                put("SCREEN", 16384);
                put("KBD", 24576);
                put("SP", 0);
                put("LCL", 1);
                put("ARG", 2);
                put("THIS", 3);
                put("THAT", 4);
            }
        };
    }

    // singleton -- only one symbol table
    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    // add the pair (symbol, address) to the table
    public void addEntry(String symbol, int address) {
        symTable.put(symbol, address);
    }

    public boolean contains(String symbol) {
        return symTable.containsKey(symbol);
    }

    public Integer getAddress(String symbol) {
        if (contains(symbol)) {
            return symTable.get(symbol);
        }
        return null;
    }
}