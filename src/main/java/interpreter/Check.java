/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package interpreter;

import interpreter.enumerations.Data;
import interpreter.enumerations.Operator;
import utilties.models.Game;

/**
 * Class used to compare data, in order to evaluate if a function should be allowed to proceed.
 *
 * How to use: after parsing the GUI for conditional input, create appropriate enums, and pass them
 * into the constructor of the Check class. Then, create a list of all of the checks, and pass them
 * into the appropriate command class.
 * For user input, use the appropriate constructor with an integer input.
 *
 * The method 'conditionIsTrue' is used by the AbstractCommand to determine if the command should proceed.
 * Each check has to return true for the command to execute.
 *
 */
public class Check {

    private String data1;
    private String data2;
    private Operator operator;

    /**
     *
     * Default constructor when not dealing with user input.
     *
     * @param data1 First data element
     * @param data2 Second data element
     * @param operator  Operator for comparison
     */
    public Check(String data1, String data2, Operator operator) {
        this.data1 = data1;
        this.data2 = data2;
        this.operator = operator;
    }

    public String getData1() {
        return data1;
    }

    public String getData2() {
        return data2;
    }

    public Operator getOperator() {
        return operator;
    }


    /**
     * Based on the current game state data, returns if condition is true.
     *
     * @param game takes in the model
     * @return if defined condition is true
     */
    boolean conditionIsTrue(Game game) {

        int data1 = getData(this.data1, game);
        int data2 = getData(this.data2, game);

        switch (this.operator){
            case LESS_THAN:
                return data1 < data2;
            case GREATER_THAN:
                return data1 > data2;
            case LESS_THAN_OR_EQUAL_TO:
                return data1 <= data2;
            case GREATER_THAN_OR_EQUAL_TO:
                return data1 >= data2;
            case EQUALS:
                return data1 == data2;
            default:
                return false;  // should never happen, but Java is Java
        }
    }


    /**
     * Based on the current game state data, returns if condition is true.
     *
     * @param data takes in the data to be returned
     * @param game is the pointer to the game model
     * @return the integer value of the game data that has been looked up
     */
    public int getData(String data, Game game) {
        if (data.equals(Data.USER_HEALTH.text())) {
            return game.getPlayer(Game.PLAYER_ID).getHealth();
        } else if (data.equals(Data.OPPONENT_HEALTH.text())) {
            return game.getPlayer(Game.OPPONENT_ID).getHealth();
        } else if (data.equals(Data.DISTANCE_FROM_OPPONENT.text())) {
            return (int) Math.round(game.distanceToOpponent(Game.PLAYER_ID, Game.OPPONENT_ID));
        } else {
            try {
                return Integer.parseInt(data);
            } catch (Exception ex) {
                System.out.println("Unable to parse '" + data + "' as Data.");
                return -1;
            }
        }
    }
}
