// DFA.java
import java.util.*;

import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

public class DFA {
    //structural
    private LinkedHashMap<String, State> states = new LinkedHashMap<>();
    private LinkedHashSet<Character> alphabet = new LinkedHashSet<>();

    private HashMap<String, HashMap<Character, String>> transitions = new HashMap<>();
    private String startState;
    private HashSet<String> acceptStates = new HashSet<>();

    private String input = "";
    private int position = 0; // next symbol index
    private String currentState = null;

    private Character lastSymbol = null;
    private String lastFrom = null;
    private String lastTo = null;

    public DFA() { }


    public static DFA fromFrontend(frontend f) {
        DFA dfa = new DFA();

        //states
        String statesText = f.getStates();
        String[] stArr = (statesText == null || statesText.trim().isEmpty()) ? new String[0] : statesText.trim().split("\\s+");
        for (String s : stArr) {
            if (!s.trim().isEmpty()) dfa.addState(s.trim());
        }

        //alphabet->take first char of each token
        String alphaText = f.getAlphabet();
        String[] aArr = (alphaText == null || alphaText.trim().isEmpty()) ? new String[0] : alphaText.trim().split("\\s+");
        for (String a : aArr) {
            if (!a.trim().isEmpty()) dfa.addAlphabet(a.trim().charAt(0));
        }

        //start
        String start = f.getStartState();
        if (start != null && !start.trim().isEmpty()) dfa.setStartState(start.trim());

        //accept states
        String accText = f.getAcceptStates();
        if (accText != null && !accText.trim().isEmpty()) {
            String[] accArr = accText.trim().split("\\s+");
            for (String a : accArr) {
                if (!a.trim().isEmpty()) dfa.addAcceptState(a.trim());
            }
        }

        //Transitions table: model layout matches frontend
        TableModel model = f.getTransitions();
        if (model != null) {
            int rows = model.getRowCount();
            int cols = model.getColumnCount();
            // header row: alphabet tokens at row 0, col > 0
            String[] colAlpha = new String[cols];
            for (int j = 1; j < cols; j++) {
                Object v = model.getValueAt(0, j);
                colAlpha[j] = v == null ? "" : v.toString().trim();
            }
            // rows: row 1..rows-1 contain state names in col 0 and destin. in col>0
            for (int i = 1; i < rows; i++) {
                Object rowLabel = model.getValueAt(i, 0);
                String src = rowLabel == null ? "" : rowLabel.toString().trim();
                if (src.isEmpty()) continue;
                dfa.addState(src);
                for (int j = 1; j < cols; j++) {
                    String alphTok = colAlpha[j];
                    if (alphTok == null || alphTok.isEmpty()) continue;
                    char sym = alphTok.charAt(0);
                    Object cell = model.getValueAt(i, j);
                    String destText = cell == null ? "" : cell.toString().trim();
                    if (destText.isEmpty()) continue;
                    // choose first comma-separated dest (DFA)
                    String dest = destText.split(",")[0].trim();
                    if (!dest.isEmpty()) {
                        dfa.addTransition(src, sym, dest);
                    }
                }
            }
        }

        //input string
        String in = f.getInput();
        if (in != null) dfa.setInput(in.trim());

        dfa.reset();
        return dfa;
    }

    //structural methods
    public void addState(String name) {
        if (!states.containsKey(name)) {
            State s = new State(name);
            states.put(name, s);
            transitions.put(name, new HashMap<>());
        }
    }
    public Collection<State> getStates() { return states.values(); }
    public State getState(String name) { return states.get(name); }

    public void addAlphabet(char c) { alphabet.add(c); }
    public Set<Character> getAlphabet() { return Collections.unmodifiableSet(alphabet); }

    public void addTransition(String src, char symbol, String dest) {
        addState(src); addState(dest);
        alphabet.add(symbol);
        HashMap<Character, String> row = transitions.get(src);
        if (row == null) { row = new HashMap<>(); transitions.put(src, row); }
        row.put(symbol, dest);
    }

    public HashMap<String, HashMap<Character, String>> getTransitions() { return transitions; }

    public void setStartState(String s) {
        addState(s);
        startState = s;
        // mark State object
        for (State st : states.values()) st.setStart(false);
        State st = states.get(s);
        if (st != null) st.setStart(true);
    }
    public String getStartState() { return startState; }

    public void addAcceptState(String s) {
        addState(s);
        acceptStates.add(s);
        State st = states.get(s);
        if (st != null) st.setAccept(true);
    }
    public Set<String> getAcceptStates() { return Collections.unmodifiableSet(acceptStates); }

    //simulation which is done at run time
    public void setInput(String in) { this.input = (in == null) ? "" : in; }
    public String getInput() { return input; }
    public int getPosition() { return position; }
    public String getCurrentState() { return currentState; }

    public void reset() {
        this.position = 0;
        this.currentState = startState;
        this.lastSymbol = null;
        this.lastFrom = null;
        this.lastTo = null;
    }

    //step one symbol
    //returns true if step happened
    public boolean step() {
        if (position >= input.length()) return false;
        lastSymbol = input.charAt(position);
        lastFrom = currentState;
        Map<Character, String> row = transitions.getOrDefault(currentState, new HashMap<>());
        String dest = row.get(lastSymbol);
        if (dest == null || dest.isEmpty()) {
            lastTo = null;
            currentState = null; // dead
        } else {
            lastTo = dest;
            currentState = dest;
        }
        position++;
        return true;
    }

    //run the whole string no breaks
    public boolean run() {
        while (position < input.length()) {
            if (!step()) break;
        }
        return isAccepting();
    }





    //undo-ish rebuild and step to target(frontend remove)
    public void rebuildAndStepTo(int targetPosition, DFA originalStructure) {
        // originalStructure contains the structural info (states, transitions, start)
        // copy structure
        this.states.clear();
        for (State s : originalStructure.getStates()) {
            State ns = new State(s.getName());
            ns.setAccept(s.isAccept());
            ns.setStart(s.isStart());
            ns.setPosition(s.getX(), s.getY());
            this.states.put(ns.getName(), ns);
        }
        this.transitions.clear();
        for (Map.Entry<String, HashMap<Character, String>> e : originalStructure.transitions.entrySet()) {
            this.transitions.put(e.getKey(), new HashMap<>(e.getValue()));
        }
        this.startState = originalStructure.startState;
        this.acceptStates = new HashSet<>(originalStructure.acceptStates);
        this.input = originalStructure.input;
        this.alphabet = new LinkedHashSet<>(originalStructure.alphabet);

        //now step to targetPosition
        this.reset();
        while (this.position < targetPosition && this.position < this.input.length()) {
            this.step();
        }
    }

    public boolean isAccepting() {
        return currentState != null && acceptStates.contains(currentState);
    }

    public Character getLastSymbol() { return lastSymbol; }
    public String getLastFrom() { return lastFrom; }
    public String getLastTo() { return lastTo; }

    public String stepReport() {
        if (lastSymbol == null) return "No step performed yet.";
        String sym = lastSymbol.toString();
        String from = (lastFrom == null ? "<dead>" : lastFrom);
        String to = (lastTo == null ? "<dead>" : lastTo);
        return String.format("Read '%s' : %s -> %s", sym, from, to);
    }

    public String finalReport() {
        String cur = (currentState == null ? "<dead>" : currentState);
        return String.format("Finished at state: %s. ACCEPTING? %s", cur, isAccepting());
    }
}
