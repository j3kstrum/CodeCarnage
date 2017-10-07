package interpreter.enumerations;

public enum Command {
    APPROACH("Approach"),
    ATTACK("Attack"),
    DEFEND("Defend"),
    DODGE("Dodge"),
    EVADE("Evade"),
    HEAL("Heal"),
    DO_NOTHING("Do Nothing");

    private String text;

    Command(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
