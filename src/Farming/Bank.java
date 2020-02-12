package Farming;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;

import java.io.FileNotFoundException;

public class Bank extends Node {

    Position BankPostition = new Position(3185, 3437, 0);

    public Bank(main m) {
        super(m);
    }

    @Override
    public boolean validate() {
        return (!m.isTimeToMule() && !m.isTimeToSell()) && !m.isTimeToBuy() && (/*m.getInventory().contains("Plank") || */!(m.getInventory().contains("Fire rune") && m.getInventory().contains("Law rune") && m.getEquipment().isWieldingWeapon("Staff of air")));
    }

    @Override
    public int execute() throws InterruptedException, FileNotFoundException {
        m.log("Bank Node");

        if (m.getAccount("25flyberet") != null) {
            m.log(m.getAccount(m.myPlayer().getName()).toString());
        }

        if (!BankPostition.getArea(10).contains(m.myPosition())) {
            m.setCurrentAction("Walking to bank.");
            m.getWalking().webWalk(BankPostition);
        } else if (m.getBank().closest().isVisible()) {
            m.setCurrentAction("Setting up run.");
            m.getBank().open();
            m.getBank().depositAll();

            if(!(m.getBank().contains("Fire rune") && m.getBank().contains("Law rune") && m.getBank().contains("Salmon") && m.getBank().contains("Energy potion(4)"))){
                m.setTimeToBuy(true);
            }else if(m.getBank().getAmount("Coins") > 500000){
                m.getBank().withdrawAll("Coins");
                m.setTimeToMule(true);
            }else if(m.getBank().getAmount("Plank") > 300){
                m.getBank().withdrawAll("Coins");
                MethodProvider.sleep(200);
                m.getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_NOTE);
                MethodProvider.sleep(200);
                m.getBank().withdrawAll("Plank");
                MethodProvider.sleep(200);
                m.getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_ITEM);
                m.setTimeToSell(true);
            }else{
                m.getBank().withdrawAll("Staff of air");
                MethodProvider.sleep(50);
                m.getBank().withdraw("Law rune", 1);
                MethodProvider.sleep(50);
                m.getBank().withdraw("Fire rune", 1);
                MethodProvider.sleep(50);
                m.getBank().withdraw("Energy potion(4)", 3);
                MethodProvider.sleep(50);
                m.getBank().withdraw("Salmon", 4);
                MethodProvider.sleep(50);
                m.getBank().close();

                if (m.getInventory().contains("Staff of air")) {
                    m.getInventory().interact("Wield", "Staff of air");
                }
            }
        } else {
            m.setCurrentAction("Banking Error");

        }
        return 0;

    }
}
