package Training;

import main.Node;
import main.grandExchangeHandler;
import main.main;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;

public class Train extends Node {

    grandExchangeHandler g;
    Area chickenPen;

    public Train(main m) {
        super(m);
        g = new grandExchangeHandler(m);
        chickenPen = new Position(3231, 3296, 0).getArea(6);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int execute() throws InterruptedException {
        m.log("Train");
        setup();
        train();

        return 0;
    }

    private void setup() throws InterruptedException {
        m.log("setup");
        if (!g.ge.contains(m.myPosition())) {
            m.getWalking().webWalk(g.ge.getCentralPosition());
        } else if (!m.getInventory().contains("Mind rune") || !(m.getInventory().contains("Staff of air") || m.getEquipment().contains("Staff of air"))) {
            buySupplies();
        } else if (m.getInventory().contains("Mind rune") && m.getInventory().contains("Staff of air")) {
            MethodProvider.sleep(3000);
            m.getInventory().interact("Wield", 1381);
            MethodProvider.sleep(500);
            m.setCurrentAction("Setup Complete");
            goToChickens();
        }
    }

    private void train() throws InterruptedException {
        m.log("train");


        if (chickenPen.contains(m.myPosition())) {


            m.setCurrentAction("Selecting spell");
            m.getTabs().open(Tab.ATTACK);
            m.getWidgets().get(593, 22).interact();
            MethodProvider.sleep(500);
            m.getWidgets().get(201, 1, 1).interact();


            do {
                NPC chicken = m.getNpcs().closest(i -> i.getName().equals("Chicken") && !i.isUnderAttack());

                if (chicken != null && !m.myPlayer().isUnderAttack()) {


                    do {
                        m.setCurrentAction("Killing Chickens!");
                        chicken.interact("Attack");
                        MethodProvider.sleep(5000);
                    } while (chicken.hasAction("Attack") && chicken.getHealthPercent() > 0);
                }
                //m.sleep(500);


            } while (m.getSkills().getVirtualLevel(Skill.MAGIC) < 25);
        }
    }

    private void goToChickens() throws InterruptedException {
        m.log("gotToChickens");
        m.setCurrentAction("Walking to chicken pen.");
        m.getMagic().castSpell(Spells.NormalSpells.HOME_TELEPORT);
        MethodProvider.sleep(15000);
        m.getWalking().webWalk(chickenPen);
        train();

    }

    private void buySupplies() throws InterruptedException {
        m.log("buySupplies");

        if (!m.getInventory().contains("Mind rune")) {
            g.buyItem("Mind rune", 558, 6, 2000);
        }

        if (!(m.getInventory().contains("Staff of air") || m.getEquipment().contains("Staff of air"))) {
            g.buyItem("Staff of air", 1381, 2000, 1);
        }

    }
}
