package interpreter.enumerations;

public enum Operator {
    LESS_THAN("Is Less Than"),
    GREATER_THAN("Is Greater Than"),
    EQUALS("Is Equal To"),
    GREATER_THAN_OR_EQUAL_TO("Is Greater Than Or Equal To"),
    LESS_THAN_OR_EQUAL_TO("Is Less Than Or Equal To");

    private String text;

    Operator(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
