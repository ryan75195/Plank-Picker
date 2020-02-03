package Farming;

import main.Node;
import main.main;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.map.Position;

import java.util.ArrayList;
import java.util.HashMap;

public class buyRunSupplies extends Node {
    public buyRunSupplies(main m) {
        super(m);
    }

    Position grandExchange = new Position(3165, 3487, 0);

    ArrayList<String> Items = new ArrayList<>();
    HashMap<String, Integer> itemPrice = new HashMap<>();
    HashMap<String, Integer> itemID = new HashMap<>();
    HashMap<String, Integer> itemQuantity = new HashMap<>();


    @Override
    public boolean validate() {
        return m.isTimeToBuy();
    }

    @Override
    public int execute() throws InterruptedException {
        m.log("Buyrunsupplies ");

        Items.add("Fire rune");
        Items.add("Law rune");
        Items.add("Salmon");
        Items.add("Energy potion(4)");

        itemPrice.put("Fire rune", 6);
        itemPrice.put("Law rune", 165);
        itemPrice.put("Salmon", 50);
        itemPrice.put("Energy potion(4)", 400);

        itemID.put("Fire rune",554);
        itemID.put("Law rune",563);
        itemID.put("Salmon",329);
        itemID.put("Energy potion(4)",3008);


        itemQuantity.put("Fire rune", 100);
        itemQuantity.put("Law rune", 100);
        itemQuantity.put("Salmon", 400);
        itemQuantity.put("Energy potion(4)", 300);






        if(grandExchange.distance(m.myPosition()) > 20){
            m.setCurrentAction("Walking to ge");
            m.getWalking().webWalk(grandExchange);
        }else if(!m.getInventory().contains("Coins")){
            m.setCurrentAction("getting items");

            m.getBank().open();
            m.getBank().withdrawAll("Coins");
            m.getBank().enableMode(Bank.BankMode.WITHDRAW_NOTE);
            m.sleep(500);
            m.getBank().withdrawAll("Fire rune");
            m.sleep(500);
            m.getBank().withdrawAll("Law rune");
            m.sleep(500);
            m.getBank().withdrawAll("Salmon");
            m.sleep(500);
            m.getBank().withdrawAll("Energy potion(4)");
            m.sleep(500);
            m.getBank().close();
        }else if(!m.getGrandExchange().isOpen()){
            m.getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
        }else if(m.getGrandExchange().isOpen()){


                for(String item : Items){

                    m.setCurrentAction("buying " + item);

                    if(m.getInventory().getAmount(item) >= itemPrice.get(item)){
                        break;
                    }else {

                        m.getGrandExchange().buyItem(itemID.get(item), item, itemPrice.get(item), itemQuantity.get(item) - (int) m.getInventory().getAmount(item));
                        m.sleep(500);
                        m.getGrandExchange().collect();
                    }

                }


            m.getGrandExchange().collect();

                if(m.getGrandExchange().isOpen()){
                    m.getGrandExchange().close();
                }

        }

        if(m.getInventory().contains("Fire rune") && m.getInventory().contains("Law rune") && m.getInventory().contains("Salmon") && m.getInventory().contains("Energy potion (4)")){
            m.setTimeToBuy(false);
        }




        return 0;
    }
}

