package gui.scripting.enumerations;

public enum Conditional {
    IF("If"),
    THEN("Then"),
    AND("And");

    private String text;

    Conditional(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
