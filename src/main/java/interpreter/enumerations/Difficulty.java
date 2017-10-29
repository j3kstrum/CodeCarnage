package interpreter.enumerations;

/**
 * Created by wiiSo on 10/23/2017.
 */
public enum Difficulty {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard");

    private String text;

    Difficulty(String text) { this.text = text; }

    public String text() { return text; }
}
