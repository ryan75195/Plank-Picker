package Training;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;

import java.io.FileNotFoundException;

public class getNets extends Node {

    boolean getNets;
    Area Lumbridge;
    boolean pickup;
    Position tutor;
    int runs = 0;
    Position lumbridgeBank;

    public getNets(main m) {
        super(m);
        //pickup = false;
        Lumbridge = new Position(3220, 3218, 0).getArea(20);
        tutor = new Position(3244, 3157, 0);
        lumbridgeBank = new Position(3208, 3218, 2);
    }

    @Override
    public boolean validate() {
        return (Lumbridge.contains(m.myPosition()) && runs < 5) || m.getPlanks;
    }

    @Override
    public int execute() throws InterruptedException, FileNotFoundException {

        if (!(m.getEquipment().contains("Staff of air") && m.getInventory().contains("Mind rune")) && !m.getInventory().isFull()) {
            m.setGetPlanks(true);
        }

        if (tutor.distance(m.myPosition()) > 5) {
            m.setCurrentAction("Walking to net spawn.");
            m.getWalking().webWalk(tutor);
        }

        if (!m.getInventory().isFull() && m.getGroundItems().get(3245,3156) != null) {
            m.setCurrentAction("Taking nets");
            m.getObjects().get(3245,3156).get(0).interact("Take");
            m.sleep(500);
        }


        if (m.getInventory().isFull() && runs <= 5) {
            m.setCurrentAction("Walking to bank");
            m.getWalking().webWalk(lumbridgeBank);
            m.setCurrentAction("Depositing");
            m.getBank().open();
            m.getBank().depositAll();
            runs++;
            m.log(runs);

            if (runs > 5) {
                m.log("runs complete");
                m.setGetPlanks(false);
            }


        }

        return 0;
    }
}
