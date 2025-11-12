// frontend.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frontend extends JFrame {
    private JTextArea inputArea;
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
        
        // Create status bar
        createStatusBar();
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
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left panel - Input
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));
        
        inputArea = new JTextArea(10, 30);
        inputArea.setText("aaaabb");
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane inputScroll = new JScrollPane(inputArea);
        
        inputPanel.add(inputScroll, BorderLayout.CENTER);
        
        // Right panel - Output/Simulation
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Simulation Steps"));
        
        outputArea = new JTextArea(10, 30);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        
        mainPanel.add(inputPanel);
        mainPanel.add(outputPanel);
        
        add(mainPanel, BorderLayout.CENTER);
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
    
    private void createStatusBar() {
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusLabel, BorderLayout.NORTH);
    }
    
    // Simulation methods
    private void simulateInput() {
        String input = inputArea.getText().trim();
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