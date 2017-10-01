package interpreter;

import java.util.List;
import javafx.scene.control.Toggle;

// The actual command will be defined when implmenting the abstract class
public abstract class AbstractCommand implements Toggle {

    private int priority;
    private List<Check> checks;

    public AbstractCommand(List<Check> checks, int priority){
        this.checks = checks;
        this.priority = priority;
    }
    
    public abstract void operation();

}
