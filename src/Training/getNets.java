package Training;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;

import java.io.FileNotFoundException;

public class getNets extends Node {

    boolean getPlanks;
    Area Lumbridge;
    boolean pickup;
    Position tutor;

    public getNets(main m) {
        super(m);
        getPlanks = true;
        //pickup = false;
        Lumbridge = new Position(3220, 3218, 0).getArea(20);
        tutor = new Position(3244, 3157, 0);
    }

    @Override
    public boolean validate() {
        return Lumbridge.contains(m.myPosition()) && !m.getInventory().isFull() || getPlanks;
    }

    @Override
    public int execute() throws InterruptedException, FileNotFoundException {

        if (Lumbridge.contains(m.myPosition()) && !(m.getEquipment().contains("Staff of air") && m.getInventory().contains("Mind rune")) && !m.getInventory().isFull()) {
            getPlanks = true;
        }

        if (tutor.distance(m.myPosition()) > 5) {
            m.getWalking().webWalk(tutor);
        } else {

            if (m.getNpcs().closest("Fishing tutor") != null) {
                m.log(pickup);

                if (m.getGroundItems().get(m.myPosition().getX(), m.myPosition().getY()).size() > 27) {
                    m.log("Time to pick up");
                    pickup = true;
                }

                if (pickup) {

                    do {
                        m.log("Taking");
                        m.getGroundItems().closest("Small fishing net").interact("Take");
                        MethodProvider.sleep(500);
                    } while (m.getGroundItems().get(m.myPosition().getX(), m.myPosition().getY()).size() != 0);
                    if (m.getGroundItems().get(m.myPosition().getX(), m.myPosition().getY()).size() == 0) {
                        m.log("no items left");

                        //pickup = false;
                    }


                } else {

                    if (!m.getDialogues().inDialogue()) {
                        m.getNpcs().closest("Fishing tutor").interact("Talk-to");
                        MethodProvider.sleep(500);
                    } else {

                        m.getDialogues().selectOption(1);
                        MethodProvider.sleep(500);
                        m.getDialogues().clickContinue();
                        if (m.getInventory().contains(i -> i.getName().contains("net"))) {
                            m.getInventory().drop("Small fishing net");
                            MethodProvider.sleep(500);
                        }


                    }
                }


            }


        }


        return 0;
    }
}
