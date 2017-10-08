package interpreter.enumerations;

public enum Operator {
    LESS_THAN("Less Than"),
    GREATER_THAN("Greater Than"),
    EQUALS("Equals"),
    GREATER_THAN_OR_EQUAL_TO("Greater Than Or Equal To"),
    LESS_THAN_OR_EQUAL_TO("Less Than Or Equal To");

    private String text;

    Operator(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
