// frontend.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frontend extends JFrame {
    private JTextArea inputField;
    private JTextArea statesField;
    private JTextArea alphabetField;
    private JTextArea startField;
    private JTextArea acceptField;
    private JTextArea transitionsField;


    private JTextArea outputArea;
    private JButton stepButton, resetButton, removeButton;
    private JLabel statusLabel;
    
    public frontend() {
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
        JPanel statesPanel = createLabeledPanel("States", statesField = new JTextArea(1, 50));
        statesField.setText("q0, q1, q2");
        
        // Alphabet panel
        JPanel alphabetPanel = createLabeledPanel("Alphabet", alphabetField = new JTextArea(1, 50));
        alphabetField.setText("a, b");
        
        // Start state panel
        JPanel startPanel = createLabeledPanel("Start State", startField = new JTextArea(1, 50));
        startField.setText("q0");
        
        // Accept states panel
        JPanel acceptPanel = createLabeledPanel("Accept States", acceptField = new JTextArea(1, 50));
        acceptField.setText("q2");
        
        // Transitions panel
        JPanel transitionsPanel = createLabeledPanel("Transitions (ex: current state, transition, next state)", transitionsField = new JTextArea(2, 50));
        transitionsField.setText("q0,a->q1; q0,b->q0; q1,a->q2; q1,b->q1; q2,a->q2; q2,b->q2");
        
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
        mainPanel.add(transitionsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // More spacing
        mainPanel.add(outputPanel);
        
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
    
    
    // Simulation methods
    private void simulateInput() {
        String input = inputField.getText().trim();
        outputArea.setText("Simulating input: " + input + "\n");
        statusLabel.setText("Simulating: " + input);
    }
    
    private void stepSimulation() {
        outputArea.append("Step executed\n");
        statusLabel.setText("Step completed");
    }
    
    private void resetSimulation() {
        outputArea.setText("Simulation reset\n");
        statusLabel.setText("Ready");
    }
    
  
    private void removeStep() {
        outputArea.append("Step removed\n");
        statusLabel.setText("Step removed");
    }
}