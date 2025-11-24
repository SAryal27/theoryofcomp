// frontend.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frontend extends JFrame {
    private JTextArea inputField;
    private JTextArea statesField;
    private JTextArea alphabetField;
    private JTextArea startField;//start states input
    private JTextArea acceptField;//accept states input
    private JTable transitionsTable;

    private String[] alphabet;
    private String[] states;
    private String[] acceptStates;


    private App app;


    public JTextArea outputArea;
    private JButton stepButton, resetButton, removeButton;    
    public frontend(App app) {
        this.app = app;
        initializeUI();

    }
    
    private void initializeUI() {
        setTitle("DFA animator program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create main panel with border layout
        setLayout(new BorderLayout());
        
        // Create menu bar
        createMenuBar();
        
        // Create main content
        createMainContent();
        
        // Create control panel
        createControlPanel();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        
        // Editor menu
        JMenu editorMenu = new JMenu("Editor");
        JMenuItem simulateItem = new JMenuItem("Simulate");
        editorMenu.add(simulateItem);
        
        menuBar.add(fileMenu);
        menuBar.add(editorMenu);
        
        setJMenuBar(menuBar);
        
        // Add action listeners
        exitItem.addActionListener(e -> System.exit(0));
        simulateItem.addActionListener(e -> simulateInput());
    }
    
    private void createMainContent() {
        // Main panel using BoxLayout for vertical stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input panel
        JPanel inputPanel = createLabeledPanel("Input String", inputField = new JTextArea(1, 50));
        inputField.setText("aaaabb");
        
        // States panel
        JPanel statesPanel = createLabeledPanel("States(In form: NameOfState0 NameOfState1...)", statesField = new JTextArea(1, 50));
        statesField.setText("q0 q1 q2");
        
        // Alphabet panel
        JPanel alphabetPanel = createLabeledPanel("Alphabet(Can be anything but space)", alphabetField = new JTextArea(1, 50));
        alphabetField.setText("a b");
        
        // Start state panel
        JPanel startPanel = createLabeledPanel("Start State", startField = new JTextArea(1, 50));
        startField.setText("q0");
        
        // Accept states panel
        JPanel acceptPanel = createLabeledPanel("Accept States", acceptField = new JTextArea(1, 50));
        acceptField.setText("q2");
        
        // Transitions table
        alphabet = createLabels(getAlphabet());
        states = createLabels(getStates());

        // Create table model with +1 row/col for headers
        int rows = states.length + 1;
        int cols = alphabet.length + 1;
        DefaultTableModel transitionsModel = new DefaultTableModel(rows, cols);

        transitionsModel.setValueAt("States", 0, 0);

        // Top row: alphabet symbols
        for (int j = 0; j < alphabet.length; j++) {
            transitionsModel.setValueAt(alphabet[j], 0, j + 1);
        }

        // Left column: state names
        for (int i = 0; i < states.length; i++) {
            transitionsModel.setValueAt(states[i], i + 1, 0);
        }

        // Create JTable
        transitionsTable = new JTable(transitionsModel);
        transitionsTable.setTableHeader(null);
        transitionsTable.setRowHeight(25);
        transitionsTable.setFillsViewportHeight(true);
        transitionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Wrap in scroll pane
        JScrollPane tableScroll = new JScrollPane(transitionsTable);
        tableScroll.setPreferredSize(new Dimension(700, 150));

        // Add panel
        JPanel transitionsPanel = new JPanel(new BorderLayout());
        transitionsPanel.setBorder(BorderFactory.createTitledBorder("Transitions Table"));
        transitionsPanel.add(tableScroll, BorderLayout.CENTER);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(transitionsPanel);




        
        // Output panel - Simulation Steps (takes remaining space)
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Simulation Steps"));
        
        outputArea = new JTextArea(15, 50);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        
        // Add all panels to main panel in vertical order
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing
        mainPanel.add(statesPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing
        mainPanel.add(alphabetPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing
        mainPanel.add(startPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing
        mainPanel.add(acceptPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacing
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // More spacing
        mainPanel.add(outputPanel);

        addTextBoxListeners();

        
        // Wrap in scroll pane in case content overflows
        JScrollPane mainScroll = new JScrollPane(mainPanel);
        add(mainScroll, BorderLayout.CENTER);
    }
    
    // Helper method to create consistent labeled panels
    private JPanel createLabeledPanel(String title, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(0, textArea.getRows() * 20)); // Dynamic height
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    
    private void createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createEtchedBorder());
        
        stepButton = new JButton("Step");
        resetButton = new JButton("Reset"); 
        removeButton = new JButton("Remove");
        
        // Style buttons
        JButton[] buttons = {stepButton, resetButton, removeButton};
        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(80, 25));
        }
        
        // Add action listeners
        stepButton.addActionListener(e -> stepSimulation());
        resetButton.addActionListener(e -> resetSimulation());
        removeButton.addActionListener(e -> removeStep());


        
        controlPanel.add(stepButton);
        controlPanel.add(resetButton);
        controlPanel.add(removeButton);
        
        add(controlPanel, BorderLayout.SOUTH);
    }
    

    private void addTextBoxListeners() {
    DocumentListener listener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) { changeOnTable(); }
        @Override
        public void removeUpdate(DocumentEvent e) { changeOnTable(); }
        @Override
        public void changedUpdate(DocumentEvent e) { changeOnTable(); }
    };

    // Attach listener to your states and alphabet text areas
    statesField.getDocument().addDocumentListener(listener);
    alphabetField.getDocument().addDocumentListener(listener);
    acceptField.getDocument().addDocumentListener(listener);

}

    

    //Getter Methods for the input boxes
    public String getInput(){
        return inputField.getText();
    }

    public String getStates(){
        return statesField.getText();
    } 

    public String getAlphabet(){
        return alphabetField.getText();
    }
    
    public String getAcceptStates(){
        return acceptField.getText();
    }
    public String getStartState(){
        return startField.getText();
    }

    public TableModel getTransitions(){
        return transitionsTable.getModel();
    }
    //may need to add getters for the String[] state and alphabet

    // Simulation methods

    //helper method to get the labels for the alphabet and the states
    private String[] createLabels(String s){
        s.trim();
        String[] array =  s.split(" ");//will make string seperating by spaces

        for (int i = 0; i < array.length; i++){
            array[i] = array[i].trim();
        }

        return array;
    }

    private void changeOnTable(){ //refresh the table when someone types on it

        alphabet = createLabels(getAlphabet());
        states = createLabels(getStates());
        acceptStates = createLabels(acceptField.getText());

        DefaultTableModel model = (DefaultTableModel) transitionsTable.getModel();

        // Ensure the model has enough rows and columns
        int requiredRows = states.length + 1;
        int requiredCols = alphabet.length + 1;

        model.setRowCount(requiredRows);
        model.setColumnCount(requiredCols);

        model.setValueAt("States", 0, 0);

        // Top row = alphabet
        for (int i = 0; i < alphabet.length; i++) {
            model.setValueAt(alphabet[i], 0, i + 1);
        }

        // Left column = states
        for (int i = 0; i < states.length; i++) {
            model.setValueAt(states[i], i + 1, 0);
        }

    }

    //Button Clicks
    private void simulateInput() {
        outputArea.setText("");

        if (app.checkForDuplicates(alphabet)){//check if alphabet is valid(no duplicates)
            outputArea.append("Alphabet Input Is Valid\n");

            if(app.inputIsValid()){//check if input is valid
                outputArea.append("Input String Is Valid\n");

                if(app.checkForDuplicates(states)){////check if states input is valid(no dupes)
                    outputArea.append("States Input Is Valid\n");

                    if(app.startStateIsValid()){
                        outputArea.append("Start State Input Is Valid\n");
                        
                        if(app.acceptStatesAreValid(acceptStates)){
                            outputArea.append("Accept States Input Is Valid\n");
                            if(app.checkTransitionTable()){
                                outputArea.append("Tranitions Are Valid\n");
                                /*if(){
                                    //make the graph everything is valid
                                }*/
                            }else{outputArea.append("Transition Table Is Not Valid Fix Before Simulating\n");}
                        }else{outputArea.append("Accept States Input Is Not Valid Fix Before Simulating\n");}
                    }else{outputArea.append("Start State Is Not Valid Fix Before Simulating\n");}
                }else{outputArea.append("States Input Is Not Valid Fix Before Simulating\n");}
            }else{outputArea.append("Input String Is Not Valid Fix Before Simulating\n");}
        }else{outputArea.append("Alphabet Is Not Valid Fix Before Simulating\n");}
        

    }
    
    private void stepSimulation() {
        outputArea.append("Step executed\n");
    }
    
    private void resetSimulation() {
        //outputArea.setText("Simulation reset\n");
        outputArea.removeAll();
    }
  
    private void removeStep() {
        outputArea.append("Step removed\n");//add this to a new line
    }

    
}