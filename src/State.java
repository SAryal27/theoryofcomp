public class State {

    int alphabetLength = 2;
    State(){
        boolean isAccepting = false; //is it an accepting state
        String name = ""; //name of the state
        State[] nextState = null; //contains the states its linked to
    }
}
