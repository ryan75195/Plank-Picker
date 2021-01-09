package Farming_new;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.Condition;

import java.io.FileNotFoundException;
import java.util.LinkedList;

public class getPlanks extends Node {

    public getPlanks(main m) {
        super(m);
    }

    boolean tele = false;
    Area town = new Area(3123, 3639, 3154, 3617);
    boolean[] visited = new boolean[9];
    LinkedList<Position> pos = m.getGraveyardRoute();
    Area graveyard = new Area(3143, 3650, 3187, 3693);
    Position plankPos = m.getGraveyardRoute().get(0);
    boolean plankOnSpot;

    public static boolean walkExact(Script script, LinkedList<Position> path) {
        WalkingEvent event = new WalkingEvent();
        event.setMinDistanceThreshold(0);
        event.setMiniMapDistanceThreshold(0);
        event.setPath(path);
        script.execute(event);
        return script.execute(event).hasFinished();
    }

    @Override
    public boolean validate() {
        return !m.getInventory().isFull() && !town.contains(m.myPosition()) && ((m.getInventory().getAmount("Trout") > 1/* && m.getInventory().contains(i -> i.getName().contains("potion"))*/) ||
                (m.getStarterCash() && !m.isTimeToMule() && !m.getTimeToBank()));
    }

    @Override
    public int execute() throws InterruptedException, FileNotFoundException {
        m.log("getPlanks Node");
        m.paintArea = graveyard;
        if (!m.inGraveyard()) {
            plankPos = m.getGraveyardRoute().get(0);
            plankOnSpot = true;
            m.log("Walking to yard");
            if (m.myPosition().equals(new Position(3134, 3640, 0)) || m.myPosition().equals(new Position(3135, 3640, 0))) {
                LinkedList<Position> path = new LinkedList<>();
                path.add(new Position(3135, 3655, 0));
                path.add(new Position(3143, 3666, 0));
                path.add(new Position(3148, 3671, 0));
                walkExact(m.getBot().getScriptExecutor().getCurrent(), path);
            } else {
                m.getWalking().webWalk(new Position(3148, 3671, 0));
            }
        } else {

            boolean b = walk(plankPos);
            if (!b) {

                GroundItem plank = m.getGroundItems().closest(i -> i.getName().equals("Plank") && i.getPosition().equals(plankPos));
                if (plank != null) {
                    plankOnSpot = true;
                    plank.interact("Take");
                } else if (plank == null) {
                    if (plankOnSpot) {
                        m.setTotalPlanks(m.getTotalPlanks() + 1);
                    }
                    if (m.getGraveyardRoute().indexOf(plankPos) < m.getGraveyardRoute().size() - 1) {
                        plankPos = m.getGraveyardRoute().get(m.getGraveyardRoute().indexOf(plankPos) + 1);
                    } else {
                        plankPos = m.getGraveyardRoute().get(0);

                    }
                    plankOnSpot = false;
                }
                MethodProvider.sleep(50);
            }
        }

        //   m.getWalking().webWalk();

        // sleep(500);

        return 0;
    }

    Position getClosestPosition() {
        Position closet = new Position(0, 0, 0);
        for (Position p : m.getGraveyardRoute()) {
            if (p.distance(m.myPosition()) < closet.distance(m.myPosition())) {
                closet = p;
            }
        }

        return closet;
    }

    boolean walk(Position p) throws InterruptedException {
        m.setCurrentAction("Walking to Plank");
        boolean br = false;

//        Position closest = m.getGraveyardRoute().
        WalkingEvent e = new WalkingEvent(p);
        e.setMinDistanceThreshold(3);
        e.setMiniMapDistanceThreshold(3);
//        e.setPath(p);
        boolean brk = (m.getSettings().getRunEnergy() <= 50 && m.getInventory().contains(i -> i.getName().contains("potion")))
                || m.getInventory().isFull() || m.myPlayer().getHealthPercent() <= 50 || (m.getStarterCash() && !m.getTimeToBank() && m.getInventory().getAmount("Plank") >= 5);

        e.setBreakCondition(new Condition() {

            @Override
            public boolean evaluate() {
                return brk;
            }

        });

        m.execute(e);


        if (brk) {
            m.log(brk);
            br = brk;
        }

        if ((m.myPlayer().getHealthPercent() <= 50 && m.getInventory().getAmount("Trout") <= 1) || ((m.getStarterCash() && !m.getTimeToBank() && m.getInventory().getAmount("Plank") >= 5))) {
            m.log("gotta run");
            m.setStarterCash(false);
            m.setTimeToBank(true);
        } else {


            if (m.getInventory().getAmount("Trout") > 1 && (m.myPlayer().getHealthPercent() <= 50 || m.getInventory().isFull())) {

                if (m.myPlayer().getHealthPercent() <= 50) {
                    m.getInventory().interact("Eat", "Trout");
                    MethodProvider.sleep(50);
                }

                if (m.getInventory().isFull() && m.getInventory().contains("Vial")) {
                    m.getInventory().dropAll("Vial");
                    MethodProvider.sleep(50);
                }

            }

            if (m.getInventory().contains(i -> i.getName().contains("potion")) && m.getSettings().getRunEnergy() < 50) {
                m.getInventory().interact("Drink", i -> i.getName().contains("potion"));
                MethodProvider.sleep(50);
            }

        }


        return br;
    }


}
