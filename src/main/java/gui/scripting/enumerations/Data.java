package gui.scripting.enumerations;

public enum Data {
    USER_HEALTH("User Health"),
    OPPONENT_HEALTH("Opponent Health"),
    DISTANCE_FROM_OPPONENT("Distance From Opponent"),
    USER_INPUT("User Input");

    private String text;

    Data(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
