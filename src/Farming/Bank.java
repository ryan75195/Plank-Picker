package Farming;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;

public class Bank extends Node {

    Position BankPostition = new Position(3185, 3437, 0);

    public Bank(main m) {
        super(m);
    }

    @Override
    public boolean validate() {
        return (m.getInventory().contains("Plank") && !new Position(3166, 3674, 0).getArea(20).contains(m.myPosition())) || !(m.getInventory().contains("Fire rune") && m.getInventory().contains("Law rune") && m.getEquipment().isWieldingWeapon("Staff of air"));
    }

    @Override
    public int execute() throws InterruptedException {
        m.log("Bank Node");
        if (!BankPostition.getArea(10).contains(m.myPosition())) {
            m.setCurrentAction("Walking to bank.");
            m.getWalking().webWalk(BankPostition);
        } else if (m.getBank().closest().isVisible()) {
            m.setCurrentAction("Setting up run.");
            m.getBank().open();
            m.getBank().depositAll();
            MethodProvider.sleep(50);
            m.getBank().withdraw("Law rune", 1);
            MethodProvider.sleep(50);
            m.getBank().withdraw("Fire rune", 1);
            MethodProvider.sleep(50);
            m.getBank().withdraw("Salmon", 8);
            MethodProvider.sleep(50);
            m.getBank().withdraw("Energy potion(4)", 3);
            MethodProvider.sleep(50);
            m.getBank().close();
        } else {
            m.setCurrentAction("Banking Error");

        }
        return 0;

    }
}
