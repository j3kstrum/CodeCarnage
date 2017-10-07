/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package interpreter;

import interpreter.enumerations.Data;
import interpreter.enumerations.Operator;

public class Check {

    private Data data1;
    private Data data2;
    private Operator operator;

    public Check(Data data1, Data data2, Operator operator) {
        this.data1 = data1;
        this.data2 = data2;
        this.operator = operator;
    }

    public Data getData1() {
        return data1;
    }

    public Data getData2() {
        return data2;
    }

    public Operator getOperator() {
        return operator;
    }
}
