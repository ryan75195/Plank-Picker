package Training;

import main.Node;
import main.grandExchangeHandler;
import main.main;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class getNets extends Node {

    Position currentLocation;
    Position netSpawn = new Position(3245, 3155, 0);
    Position Bank = new Position(3209, 3218, 2);

    public getNets(main m) {
        super(m);
        currentLocation = m.myPosition();

    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int execute() throws InterruptedException {

        for (int i = 0; i < 9; i++) {
            m.log("Runs:" + i);
            getNets();
            bankNets();
        }

        sellNets();
        m.setNextNode();


        return 0;
    }


    public void getNets() throws InterruptedException {

        if (inLumbridge() && isFreshSpawn() && !m.getInventory().isFull()) {
            m.setCurrentAction("Walking to nets");
            m.getWalking().webWalk(netSpawn);
            m.setCurrentAction("Picking up nets");
            if (m.getGroundItems().get(3245, 3156) != null && !m.getInventory().isFull()) {
//                for(int i = 0; i < m.getGroundItems().getAll().size(); i++){
//                    m.log(m.getObjects().getAll().get(i).getName());
//                }
                do {
                    m.getObjects().get(3245, 3156).get(0).interact("Take");
                    MethodProvider.sleep(500);
                } while (!m.getInventory().isFull());
            }
        } else if (m.getInventory().isFull()) {

            m.log("Logged in with full inventory, time to bank");
        } else {
            m.setCurrentAction("Not in Lumbridge, error");
            m.log(String.format("%s,%s,%s", inLumbridge(), isFreshSpawn(), !m.getInventory().isFull()));
        }

    }

    public void bankNets() throws InterruptedException {
        if (m.getInventory().isFull()) {
            m.setCurrentAction("Banking nets");
            m.getWalking().webWalk(Bank);
            if (m.getBank().closest() != null) {
                m.getBank().open();
                if (m.getWidgets().get(664, 28) != null && m.getWidgets().get(664, 28).isVisible()) {
                    m.getWidgets().get(664, 28).interact();
                }
                m.getBank().depositAll();
                m.getBank().close();
            }
        }
    }

    public boolean inLumbridge() {
        return m.myPosition().getX() < 3255 && m.myPosition().getY() < 3235;
    }

    public boolean isFreshSpawn() {
        return m.getSkills().getExperience(Skill.MAGIC) == 9 && !m.getInventory().contains(i -> i.getName().contains("rune"));
    }

    public void sellNets() throws InterruptedException {
        grandExchangeHandler g = new grandExchangeHandler(m);
        g.sellItem("Small fishing net", 1, 9999);
    }

}
