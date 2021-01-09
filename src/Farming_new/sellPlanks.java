package Farming_new;

import main.main;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

import java.util.ArrayList;

public class sellPlanks {

    main m;
    Position grandExchange = new Position(3165, 3487, 0);


    public sellPlanks(main m) {
        this.m = m;
    }

    public void sell() throws InterruptedException {
        m.log("sellPLanks");

        if (!grandExchange.getArea(25).contains(m.myPosition())) {
            m.setCurrentAction("walking to ge");

            m.getWalking().webWalk(grandExchange);
        }

        if (!m.getInventory().contains("Plank")) {
            m.setCurrentAction("getting planks");

            m.getBank().open();
            m.getBank().depositAll();
            MethodProvider.sleep(200);
            m.getBank().withdrawAll("Coins");
            MethodProvider.sleep(200);
            m.getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_NOTE);
            MethodProvider.sleep(200);
            m.getBank().withdrawAll("Plank");
            MethodProvider.sleep(200);
            m.getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_ITEM);
        }

        if (!m.getGrandExchange().isOpen()) {
            m.getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
        }

        if (m.getGrandExchange().isOpen()) {
            m.setCurrentAction("selling planks");

            if (m.getInventory().contains("Plank")) {
                m.getGrandExchange().sellItem(961, 1, 1000);
            }

            m.log(saleFinished());
            ConditionalSleep s = new ConditionalSleep(10000) {
                @Override
                public boolean condition() {
                    return saleFinished();
                }
            };

            s.sleep();
            m.log(saleFinished());

            if (saleFinished()) {
                m.log("done " + saleFinished());

                m.getGrandExchange().collect();
                MethodProvider.sleep(1000);
                m.getGrandExchange().close();
            }
        }


    }

    public boolean saleFinished() {

        ArrayList<GrandExchange.Box> boxes = new ArrayList<>();
        boxes.add(GrandExchange.Box.BOX_1);
        boxes.add(GrandExchange.Box.BOX_2);
        boxes.add(GrandExchange.Box.BOX_3);
        boolean t = false;

        for (GrandExchange.Box b : boxes) {

            if (m.getGrandExchange().getStatus(b).equals(GrandExchange.Status.FINISHED_SALE)) {
                t = true;
                break;
            }

        }

        return t;
    }

}
