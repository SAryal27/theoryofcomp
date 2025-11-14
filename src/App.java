import java.util.ArrayList;
import java.util.HashSet;
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

    //HELPER TO GET CURRENT VALUES FROM GUI
    private void updateValues(){
        input = GUI.getInput();
        alphabet = GUI.getAlphabet().toCharArray();
    }


    //CHECKING STATES INPUT AND TRASITION TABLE
    /*public boolean checkForDuplicates(String[] array){//state input and alpabet validity
        for(int i = 0; i < array.length; i++){
            for(int j = 0; i < array.length; j++){
                if(i == j){
                    //do nothing its the same spot on the string
                }else if(array[i].equals(array[j])){//if we have a duplicate name
                    GUI.outputArea.append("Duplicates Found");
                    return false;
                } 
            }
        }

        GUI.outputArea.append("No Duplicates Found");
        return true;
    }*/

    public boolean checkForDuplicates(String[] array){//state input and alpabet validity
        HashSet<String> checker = new HashSet<>();

        for(int i = 0; i < array.length; i++){
            if(checker.contains(array[i])){
                GUI.outputArea.append("Duplicates Found(Fix input/s)");
                return false;
            }
           checker.add(array[i]);//not a duplicate add to the list
        }

        GUI.outputArea.append("No Duplicates Found");
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
                GUI.outputArea.setText("Not a valid input\n");
                return false;
            }
        }

        GUI.outputArea.setText("Valid, Simulating input: " + input + "\n");
        return true;
    }

}

