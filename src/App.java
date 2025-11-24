import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.*;

public class App {
    private frontend GUI;
    private String input;
    private char[] alphabet;
    private List<Character> alphabetList = new ArrayList<>();
    private HashSet<String> checker;//contains the hashSet of states(since its called second)



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

    //HELPER TO GET CURRENT VALUES FROM GUI
    private void updateValues(){
        input = GUI.getInput();
        alphabet = GUI.getAlphabet().toCharArray();
    }


    //CHECKING STATES INPUT
    public boolean checkForDuplicates(String[] array){//state input and alpabet validity
        checker = new HashSet<>();

        for(int i = 0; i < array.length; i++){
            

            if(checker.contains(array[i])){
                return false;
            }
           checker.add(array[i]);//not a duplicate add to the list
        }

        return true;
    }

    //CHECKING IF THE ACCEPT STATES AND START STATE IS VALID

    public boolean startStateIsValid(){
       return checker.contains(GUI.getStartState());
    }

    public boolean acceptStatesAreValid(String[] array){
        for(int i = 0; i < array.length; i++){
            if(!checker.contains(array[i])){
                return false;
            }
        }
        return true;
    }

    public boolean checkTransitionTable(){
        for(int i = 1; i < GUI.getTransitions().getRowCount(); i++){//first row and col have display values(we don't check)
            for(int j = 1; j < GUI.getTransitions().getColumnCount(); j++){
                if(!checker.contains(GUI.getTransitions().getValueAt(i, j))){
                    return false;
                }
            }
        }

        return true;
    }


    ///BELOW IS THE CODE FOR CHECKING IF INPUT IS VALID
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

