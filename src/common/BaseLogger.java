package common;

import java.io.PrintStream;
import java.time.LocalDateTime;

public class BaseLogger {
    private String _NAME;
    private String _FORMAT;
    private PrintStream _STREAM;

    public BaseLogger(String name) {
        this(name, System.err);
    }

    public BaseLogger(String name, PrintStream stream) {
        this(name, stream, "%(level) [%(time)] %(name): %(message)");
    }

    public BaseLogger(String name, PrintStream stream, String format) {
        this._NAME = name;
        this._STREAM = stream;
        this._FORMAT = format;
    }

    public void debug(String message) {
        this.print(message, "DEBUG");
    }

    public void info(String message) {
        this.print(message, "INFO");
    }

    public void warning(String message) {
        this.print(message, "WARNING");
    }

    public void critical(String message) {
        this.print(message, "CRITICAL");
    }

    public void fatal(String message) {
        this.print(message, "FATAL");
    }

    private void print(String message, String level) {
        String msg = this.format(this._FORMAT, level);
        if (msg.contains("%(message)")) {
            msg = msg.replace("%(message)", message);
        }
        this._STREAM.println(msg);
    }

    private String format(String formattable, String level) {
        formattable = this.formatName(formattable);
        formattable = this.formatTime(formattable);
        if (level != null) {
            formattable = this.formatLevel(formattable, level);
        }
        return formattable;
    }

    private String formatName(String formattable) {
        if (formattable.contains("%(name)")) {
            return formattable.replace("%(name)", this._NAME);
        }
        return formattable;
    }

    private String formatLevel(String formattable, String level) {
        if (formattable.contains("%(level)")) {
            return formattable.replace("%(level)", level);
        }
        return formattable;
    }

    private String formatTime(String formattable) {
        if (formattable.contains("%(time)")) {
            return formattable.replace("%(time)", LocalDateTime.now().toString().replace('T', ' '));
        }
        return formattable;
    }
}
