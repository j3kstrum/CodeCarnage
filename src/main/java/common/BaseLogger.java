package main.java.common;

public class BaseLogger {
    private String _NAME;
    private String _FORMAT;

    public BaseLogger(String name) {
        this(name, null);
    }

    public BaseLogger(String name, String format) {
        if (format == null) {
            format = "[%(time)] %(name): %(message)";
        }
        this._NAME = name;
        this._FORMAT = format;
    }
}
