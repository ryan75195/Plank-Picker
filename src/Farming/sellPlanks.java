package Farming;

import main.Node;
import main.main;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.map.Position;

import java.util.ArrayList;

public class sellPlanks extends Node {

    public sellPlanks(main m) {
        super(m);
    }

    Position grandExchange = new Position(3165, 3487, 0);
    @Override
    public boolean validate() {
        return m.isTimeToSell();
    }

    @Override
    public int execute() throws InterruptedException {

        m.log("sellPLanks");

        if(!grandExchange.getArea(25).contains(m.myPosition())){
            m.setCurrentAction("walking to ge");

            m.getWalking().webWalk(grandExchange);
        }

        if(!m.getInventory().contains("Plank")){
            m.setCurrentAction("getting planks");

            m.getBank().open();
            m.getBank().depositAll();
            m.sleep(200);
            m.getBank().withdrawAll("Coins");
            m.sleep(200);
            m.getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_NOTE);
            m.sleep(200);
            m.getBank().withdrawAll("Plank");
            m.sleep(200);
            m.getBank().enableMode(org.osbot.rs07.api.Bank.BankMode.WITHDRAW_ITEM);
        }

        if(!m.getGrandExchange().isOpen()){
            m.getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
        }

        if(m.getGrandExchange().isOpen()){
            m.setCurrentAction("selling planks");

            if(m.getInventory().contains("Plank")){
            m.getGrandExchange().sellItem(961,1,1000);
                m.sleep(200);

            }else if(saleFinished()){
                m.getGrandExchange().collect();
                m.sleep(200);
                m.getGrandExchange().close();
            }
        }

        if(m.getInventory().contains("Coins") && !m.getInventory().contains("Plank")){
            m.setTimeToSell(false);
        }else{
            m.setTimeToSell(true);
        }




        return 0;
    }

    public boolean saleFinished(){

        ArrayList<GrandExchange.Box> boxes = new ArrayList<>();
        boxes.add(GrandExchange.Box.BOX_1);
        boxes.add(GrandExchange.Box.BOX_2);
        boxes.add(GrandExchange.Box.BOX_3);
        boolean t = false;

        for(GrandExchange.Box b : boxes){

            if(m.getGrandExchange().getStatus(b).equals(GrandExchange.Status.FINISHED_SALE)){
                t = true;
                break;
            }

        }

        return t;
    }

}
