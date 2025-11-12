import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class App {
    private frontend GUI;
    private String input;
    private char[] alphabet;
    private List<Character> alphabetList = new ArrayList<>();



    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            App app = new App();  // create the controller
            app.start();// start the GUI
        });        

    }

    public void start() {
            GUI = new frontend(this);  //pass App instance to frontend
            GUI.setVisible(true);
            
            input = GUI.getInput();
            alphabet = GUI.getAlphabet().toCharArray();
        }
    
    private void updateValues(){//helper to get current values from GUI
        input = GUI.getInput();
        alphabet = GUI.getAlphabet().toCharArray();
    }

    private void populateAlphabetList(){
        alphabetList.clear();//if they change the input string

         for (char c : alphabet) {
            if(c != ' '){//as long as its not a space its a character so add it to the list
                alphabetList.add(c);
            } 
        }
    }
    public boolean inputIsValid(){
        updateValues();
        populateAlphabetList();//make sure that we create our alphabet list

        if(alphabetList.isEmpty()){return false;}//can't run it if the alphabet has no characters

        for(int i = 0; i < input.length(); i++){
            if(!alphabetList.contains(input.charAt(i))){
                return false;
            }
        }

        return true;
    }

}

