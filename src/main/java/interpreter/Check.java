/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package interpreter;

import utilties.entities.*;
import utilties.models.*;

public class Check {

    public Data data1;
    public Data data2;
    public Operator operator;

    public Check(Data data1, Data data2, Operator operator) {
        this.data1 = data1;
        this.data2 = data2;
        this.operator = operator;
    }

    /**
     *
     * @param game takes in the model
     * @return if defined condition is true
     */
    public boolean conditionIsTrue(Game game){



        return true;
    }


}
