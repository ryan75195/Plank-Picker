package Farming;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.Condition;

public class walkToGraveyard extends Node {
    public walkToGraveyard(main m) {
        super(m);
    }

    @Override
    public boolean validate() {
        return !m.isTimeToBuy() && !m.isTimeToMule() && !m.isTimeToMule() && m.getInventory().contains("Fire rune") && m.getInventory().contains("Law rune") && m.getEquipment().isWieldingWeapon("Staff of air")
                && !m.inGraveyard();

    }

    @Override
    public int execute() throws InterruptedException {
        m.log("walkingtoGraveyard Node");

        m.setCurrentAction("Walking to the graveyard.");


        WebWalkEvent w = new WebWalkEvent(new Position(3148, 3671, 0));
        w.setBreakCondition(new Condition() {
            @Override
            public boolean evaluate() {
                return m.getInventory().contains(i -> i.getName().contains("Energy")) && m.getSettings().getRunEnergy() < 20;
            }
        });
        m.execute(w);
        if (m.getInventory().contains(i -> i.getName().contains("Energy")) && m.getSettings().getRunEnergy() < 20) {
            m.getInventory().interact("Drink", i -> i.getName().contains("Energy"));
            MethodProvider.sleep(250);
        }


        return 0;
    }
}
