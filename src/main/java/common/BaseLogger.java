/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package common;

import java.io.PrintStream;
import java.time.LocalDateTime;

/**
 * A base class for logging. Can be instantiated to provide a quick and informative way of producing
 * console or other stream output.
 *
 * @author jacob.ekstrum
 */
public class BaseLogger {
    private String _name;
    private String _format;
    private PrintStream _stream;

    /**
     * Constructor for the logger. At its most basic, produces a logger with the given name
     * piped to standard error with the default format.
     *
     * @param name The name to be used for logging statements with this logger.
     */
    public BaseLogger(String name) {
        this(name, System.err);
    }

    /**
     * Constructor for the logger. Uses the basic format, but pipes output to the specified stream
     * with the specified name.
     *
     * @param name   The name to be used when producing output.
     * @param stream The stream to which output should be sent.
     */
    public BaseLogger(String name, PrintStream stream) {
        this(name, stream, "%(level) [%(time)] %(name): %(message)");
    }

    /**
     * Constructor for the logger.
     *
     * @param name   The name to be used when producing output.
     * @param stream The stream that output should be sent to.
     * @param format The format to be used for the logger's output.
     */
    public BaseLogger(String name, PrintStream stream, String format) {
        this._name = name;
        this._stream = stream;
        this._format = format;
    }

    /**
     * Outputs a message using the level string "DEBUG".
     *
     * @param message The message to be sent.
     */
    public void debug(String message) {
        this.print(message, "DEBUG");
    }

    /**
     * Outputs a message using the level string "INFO".
     *
     * @param message The message to be sent.
     */
    public void info(String message) {
        this.print(message, "INFO");
    }

    /**
     * Outputs a message using the level string "WARNING".
     *
     * @param message The message to be sent.
     */
    public void warning(String message) {
        this.print(message, "WARNING");
    }

    /**
     * Outputs a message using the level string "CRITICAL".
     *
     * @param message The message to be sent.
     */
    public void critical(String message) {
        this.print(message, "CRITICAL");
    }

    /**
     * Outputs a message using the level string "FATAL".
     *
     * @param message The message to be sent.
     */
    public void fatal(String message) {
        this.print(message, "FATAL");
    }

    /**
     * Generates and outputs the logging string containing the specified message.
     *
     * @param message The message to be attached to the logging string, or placed inside if formatting approves.
     * @param level   The level to log at.
     */
    private void print(String message, String level) {
        String msg = this.format(this._format, level);
        if (msg.contains("%(message)")) {
            msg = msg.replace("%(message)", message);
        } else {
            msg = msg + " " + message;
        }
        this._stream.println(msg);
    }

    /**
     * Formats the provided malleable string with the specified level.
     *
     * @param formattable The string optionally containing formattable characters that must be formatted.
     * @param level       The level to be placed in the formattable string, if appropriate.
     * @return The formatted string.
     */
    private String format(String formattable, String level) {
        formattable = this.formatName(formattable);
        formattable = this.formatTime(formattable);
        if (level != null) {
            formattable = this.formatLevel(formattable, level);
        }
        return formattable;
    }

    /**
     * Places the logger's name in the formattable string, if appropriate.
     *
     * @param formattable The string that may or may not have the name inserted.
     * @return The formattable string with all name tokens formatted.
     */
    private String formatName(String formattable) {
        if (formattable.contains("%(name)")) {
            return formattable.replace("%(name)", this._name);
        }
        return formattable;
    }

    /**
     * Places the logger's level in the formattable string, if appropriate.
     *
     * @param formattable The string that may or may not have the level inserted.
     * @return The formattable string with all level tokens formatted.
     */
    private String formatLevel(String formattable, String level) {
        if (formattable.contains("%(level)")) {
            return formattable.replace("%(level)", level);
        }
        return formattable;
    }

    /**
     * Places the time of the call in the formattable string, if appropriate.
     *
     * @param formattable The string that may or may not have the time inserted.
     * @return The formattable string with all time tokens formatted.
     */
    private String formatTime(String formattable) {
        if (formattable.contains("%(time)")) {
            return formattable.replace("%(time)", LocalDateTime.now().toString().replace('T', ' '));
        }
        return formattable;
    }
}
