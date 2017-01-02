/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3task2client;

/**
 *
 * @author Jiaming
 */
public enum RequestType {
    High(1), Low(2), LastHigh(3), LastLow(4), allTemp(5);

    private int index;

    RequestType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
