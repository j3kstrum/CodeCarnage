package interpreter.enumerations;

import java.util.Arrays;
import java.util.List;

public enum ScriptingTypes {
    CONDITIONAL(Arrays.asList("If", "And", "Then")),
    COMMAND(Arrays.asList("Approach", "Attack", "Defend", "Dodge", "Evade", "Heal", "Do Nothing")),
    DATA(Arrays.asList("User Health", "Opponent Health", "Distance From Opponent", "User Input")),
    OPERATOR(Arrays.asList("Is Less Than", "Is Greater Than", "Is Equal To", "Is Greater Than Or Equal To", "Is Less Than Or Equal To"));

    private List<String> list;

    ScriptingTypes(List<String> list) {
        this.list = list;
    }

    public List<String> list() {
        return list;
    }
}
