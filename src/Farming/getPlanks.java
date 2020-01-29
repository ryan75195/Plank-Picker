package Farming;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.Condition;

import java.util.List;

import static java.lang.Thread.sleep;

public class getPlanks extends Node {

    public getPlanks(main m) {
        super(m);
    }

    @Override
    public boolean validate() {
        return new Position(3166, 3674, 0).getArea(25).contains(m.myPosition()) && m.getInventory().contains("Fire rune") && m.getInventory().contains("Law rune") && m.getEquipment().isWieldingWeapon("Staff of air");
    }

    @Override
    public int execute() throws InterruptedException {
        m.log("getPlanks Node");

        Area graveyard = new Position(3166, 3674, 0).getArea(20);

        if (!graveyard.contains(m.myPosition())) {
            walk(m.getGraveyardRoute().get(0));

            //getWalking().webWalk(graveyard.getCentralPosition());
        } else {

            for (Position nextStep : m.getGraveyardRoute()) {
                List<GroundItem> items = m.getGroundItems().get(nextStep.getX(), nextStep.getY());

                if(nextStep.distance(m.myPosition()) != 0){
                    walk(nextStep);
                }





/*
                    do {
                        walk(plankPosition);
                        if (!new Position(3166, 3674, 0).getArea(20).contains(m.myPosition())) {
                            break;
                        }
                    } while (plankPosition.distance(m.myPosition()) >= 1);*/

                    if (items.size() > 0) {

                        for (GroundItem i : items) {
                            if (i.getName().equals("Plank")) {

                                do {

                                    m.setCurrentAction("Picking up plank");
                                    i.interact("Take");
                                    sleep(200);
                                    if (!i.exists()) {
                                        m.setTotalPlanks(m.getTotalPlanks() + 1);
                                    }

                                }
                                while (i.exists() && !m.getInventory().isFull());



                        }
                    }
                }
            }

            //   m.getWalking().webWalk();
        }

       // sleep(500);

        return 0;
    }


    void walk(Position p) throws InterruptedException {
        m.setCurrentAction("Walking to Plank");



            WalkingEvent e = new WalkingEvent(p);
            e.setBreakCondition(new Condition() {

                @Override
                public boolean evaluate() {
                    return /*m.getSettings().isRunning() ||*/ (m.getInventory().isFull() && (!m.getInventory().contains(i -> i.getName().contains("Vial") || i.getName().contains("Salmon")))) || m.getInventory().isFull() || m.myPlayer().getHealthPercent() < 50;
                }

            });
            m.execute(e);



        if (m.myPlayer().getHealthPercent() < 50 ) {
            m.setCurrentAction("Eating Food");
            m.getInventory().interact("Eat", "Salmon");
            m.sleep(1000);

            if (m.myPlayer().getHealthPercent() < 50 && !m.getInventory().contains("Salmon")) {
                if (m.getMagic().castSpell(Spells.NormalSpells.VARROCK_TELEPORT)) {
                    m.getWalking().walk(new Position(m.myPosition().getX(), m.myPosition().getY() - 5, 0));
                    m.getMagic().castSpell(Spells.NormalSpells.VARROCK_TELEPORT);
                    m.sleep(500);
                    m.setSupplyCost(m.getCurrentSupplyCost() + 1363);
                }
            }
        }

        if (m.getInventory().isFull() && (m.getInventory().contains("Salmon") || m.getInventory().contains("Vial"))) {

            if (m.getInventory().contains("Vial")) {
                m.setCurrentAction("Dropping vials");
                m.getInventory().dropAll("Vial");
            } else if (m.getInventory().contains("Salmon")) {
                m.setCurrentAction("Eating Food");
                m.getInventory().interact("Eat", "Salmon");
                m.sleep(1000);
            }

        }

        if (m.getInventory().isFull() && !m.getInventory().contains("Salmon")) {
            m.setCurrentAction("casting Teleport");
            if (m.getMagic().castSpell(Spells.NormalSpells.VARROCK_TELEPORT)) {
                m.getWalking().walk(new Position(m.myPosition().getX(), m.myPosition().getY() - 5, 0));
                m.getMagic().castSpell(Spells.NormalSpells.VARROCK_TELEPORT);
                m.sleep(500);
                m.setSupplyCost(m.getCurrentSupplyCost() + 1363);
            } else {
                m.setCurrentAction("Error cant teleport");
            }
        }

//        if (m.getSettings().isRunning()) {
//            m.setCurrentAction("No running in the graveyard!");
//            m.getSettings().setRunning(false);
//            MethodProvider.sleep(500);
//        }
    }
}
