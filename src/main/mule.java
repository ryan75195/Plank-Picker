package main;

import main.Node;
import main.main;

public class mule extends Node {

    public mule(main m) {
        super(m);
    }

    @Override
    public boolean validate() {
        return m.isTimeToMule();
    }

    @Override
    public int execute() throws InterruptedException {
        return 0;
    }
}
