package Training;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;

public class buyTrainingSupplies extends Node {

    public buyTrainingSupplies(main m) {
        super(m);
    }

    Position grandExchange = new Position(3165, 3487, 0);

    @Override
    public boolean validate() {

        return !m.getPlanks && (!m.getInventory().contains("Mind rune") || !m.getEquipment().contains("Staff of air"));
    }

    @Override
    public int execute() throws InterruptedException {

        m.log("buy training");

        if(grandExchange.distance(m.myPosition()) > 20){
            m.setCurrentAction("walking to ge");
            m.getWalking().webWalk(grandExchange);
        }else {

            if(m.getInventory().contains("Small fishing net")){
                getNets();
                sellNets();
            }else{
                buyItems();
                if(m.getInventory().contains("Staff of air")){

                    if(m.getGrandExchange().isOpen()){
                        m.getGrandExchange().close();
                    }

                    m.getInventory().interact("Wield","Staff of air");
                }
            }
        }


        return 0;
    }

    void getNets(){




    }


    void sellNets() throws InterruptedException {

        if (!m.getGrandExchange().isOpen()) {
            m.getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
            m.sleep(1500);
        }

        if (m.getInventory().contains("Small fishing net")) {
            m.setCurrentAction("Selling Nets");
            m.getGrandExchange().sellItem(304, 1, 999);
            MethodProvider.sleep(3000);
            m.getGrandExchange().collect();
            m.sleep(1000);
        }

    }

   void buyItems() throws InterruptedException {

       if (!m.getGrandExchange().isOpen()) {
           m.getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
           m.sleep(1500);
       }

       if (m.getGrandExchange().isOpen() && (!m.getInventory().contains("Mind rune") || !m.getEquipment().contains("Staff of air"))) {



               if (!m.getInventory().contains("Mind rune")) {
                   m.setCurrentAction("buying runes");
                   m.getGrandExchange().buyItem(558, "Mind rune", 6, 1180);
                   MethodProvider.sleep(3000);
                   m.getGrandExchange().collect();
                   m.sleep(1000);

               }

               if (!m.getInventory().contains("Staff of air") && !m.getEquipment().contains("Staff of air")) {
                   m.setCurrentAction("buying staff");
                   m.getGrandExchange().buyItem(1381, "Staff of air", 2000, 1);
                   MethodProvider.sleep(3000);
                   m.getGrandExchange().collect();
                   m.sleep(1000);

               }
           }
           // m.getGrandExchange().collect();

       }
    }

