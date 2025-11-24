// State.java
public class State {
    private String name;
    private boolean isStart;
    private boolean isAccept;
    private int x;
    private int y;

    public State(String name) {
        this.name = name;
        this.isStart = false;
        this.isAccept = false;
        this.x = 0;
        this.y = 0;
    }

    //getters
    public String getName() { return name; }
    public boolean isStart() { return isStart; }
    public boolean isAccept() { return isAccept; }
    public int getX() { return x; }
    public int getY() { return y; }

    //setters
    public void setStart(boolean start) { this.isStart = start; }
    public void setAccept(boolean accept) { this.isAccept = accept; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }

    @Override
    public String toString() {
        return name;
    }
}
