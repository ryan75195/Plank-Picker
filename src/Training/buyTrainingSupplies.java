package Training;

import main.Node;
import main.main;
public class buyTrainingSupplies extends Node {

    public buyTrainingSupplies(main m) {
        super(m);
    }

    @Override
    public boolean validate() {

        return !m.getItemsToBuy().isEmpty();
    }

    @Override
    public int execute() throws InterruptedException {

        return 0;
    }
}
