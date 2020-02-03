package Training;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Position;

public class buyTrainingSupplies extends Node {

    public buyTrainingSupplies(main m) {
        super(m);
    }

    Position grandExchange = new Position(3165, 3487, 0);

    @Override
    public boolean validate() {

        return !m.getInventory().contains("Mind rune") || !m.getEquipment().contains("Staff of air");
    }

    @Override
    public int execute() throws InterruptedException {

        m.log("buytraining");

        if(grandExchange.distance(m.myPosition()) > 20){
            m.setCurrentAction("walking to ge");

            m.getWalking().webWalk(grandExchange);
        }else {

            if (!m.getInventory().contains("Coins")) {
                m.setCurrentAction("getting cash");

                m.getBank().open();
                m.getBank().withdrawAll("Coins");
                m.getBank().close();
            }

            if (m.getInventory().contains("Staff of air")) {
                m.setCurrentAction("wielding staff");
                m.getInventory().interact("Wield", "Staff of air");
            }

            if (!m.getGrandExchange().isOpen()) {
                m.getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
            }

            if (m.getGrandExchange().isOpen() && !m.getInventory().contains(i -> i.getName().equals("Mind rune") || i.getName().equals("Staff of air"))) {

                if (!m.getInventory().contains("Mind rune")) {
                    m.setCurrentAction("buying runes");

                    m.getGrandExchange().buyItem(558, "Mind rune", 6, 1180);
                    m.sleep(3000);
                }

                if (!m.getInventory().contains("Staff of air")) {
                    m.setCurrentAction("buying staff");

                    m.getGrandExchange().buyItem(1381, "Staff of air", 2000, 1);
                    m.sleep(3000);
                }

                m.getGrandExchange().collect();

            }

        }


        return 0;
    }
}
