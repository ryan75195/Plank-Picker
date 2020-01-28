package Farming;

import main.Node;
import main.main;

public class buyRunSupplies extends Node {
    public buyRunSupplies(main m) {
        super(m);
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public int execute() throws InterruptedException {
        m.log("Buy supplies Node");

        return 0;
    }
}
