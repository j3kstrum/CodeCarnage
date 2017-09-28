package main.java.interpreter;


// The actual command will be defined when implmenting the abstract class
public abstract class AbstractCommand {

    private int priority;
    private Check check;

    public AbstractCommand(Check check, int priority){
        this.check = check;
        this.priority = priority;
    }
    
    public abstract void operation();

}
