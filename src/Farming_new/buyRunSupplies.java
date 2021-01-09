package Farming_new;

import main.Node;
import main.main;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

import java.util.ArrayList;
import java.util.HashMap;

public class buyRunSupplies extends Node {
    Position grandExchange = new Position(3165, 3487, 0);
    ArrayList<String> Items = new ArrayList<>();
    HashMap<String, Integer> itemPrice = new HashMap<>();
    HashMap<String, Integer> itemID = new HashMap<>();
    HashMap<String, Integer> itemQuantity = new HashMap<>();
    Area town = new Area(3123, 3639, 3154, 3617);

    public buyRunSupplies(main m) {
        super(m);
    }

    @Override
    public boolean validate() {
        return m.isTimeToBuy() && !m.isTimeToMule();
    }

    @Override
    public int execute() throws InterruptedException {
        m.log("Buyrunsupplies ");

        Items.add("Trout");
        Items.add("Energy potion(4)");


        itemPrice.put("Trout", 150);
        itemPrice.put("Energy potion(4)", 600);


        itemID.put("Trout", 333);
        itemID.put("Energy potion(4)", 3008);


        if (!grandExchange.getArea(10).contains(m.myPosition())) {
            m.log(m.myPosition().getY() >= 3617);
            WebWalkEvent w = new WebWalkEvent(grandExchange);
            w.setBreakCondition(new Condition() {
                @Override
                public boolean evaluate() {
                    return (town.contains(m.myPosition())) /*&& bottomBarrier.getPosition().distance(m.myPosition()) < 5)*/;
                }
            });
            m.execute(w);
            m.log("Break");
            if (m.myPosition().getY() >= new Position(3134, 3617, 0).getY()) {
                if (new Position(3134, 3617, 0).distance(m.myPosition()) > 1) {
                    m.log("Walking to barrier");
                    m.getWalking().walk(new Position(3134, 3617, 0));
                }
                m.log("Interacting");
                m.getCamera().moveYaw(0);
                m.getCamera().movePitch(22);
                new ConditionalSleep(2000) {
                    @Override
                    public boolean condition() {
                        return m.getCamera().getPitchAngle() == 22 && m.getCamera().getYawAngle() == 0;
                    }
                }.sleep();
                new Position(3134, 3617, 0).hover(m.getBot());
                MethodProvider.sleep(500);
                m.getMouse().click(false);
//                   bottomBarrier.interact("Pass-Through");
                new ConditionalSleep(2000) {
                    @Override
                    public boolean condition() {
                        return !town.contains(m.myPosition());
                    }
                }.sleep();
            }
        }

        if (grandExchange.getArea(10).contains(m.myPosition())) {


            if (!m.getInventory().contains("Coins")) {
                m.setCurrentAction("getting items");
                m.getBank().open();
                m.getBank().withdrawAll("Coins");
                m.getBank().enableMode(Bank.BankMode.WITHDRAW_NOTE);
                m.getBank().withdrawAll("Trout");
                MethodProvider.sleep(500);
                m.getBank().withdrawAll("Energy potion(4)");
                MethodProvider.sleep(500);
                m.getBank().withdrawAll("Plank");
//                   m.getBank().withdraw("Plank");
                MethodProvider.sleep(500);
                m.getBank().close();
            }

            if (!m.getGrandExchange().isOpen()) {
                m.getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
                new ConditionalSleep(10000) {
                    @Override
                    public boolean condition() {
                        return m.getGrandExchange().isOpen();
                    }
                }.sleep();
            }

            if (m.getGrandExchange().isOpen()) {

                if (m.getInventory().contains("Plank")) {
                    sellPlanks s = new sellPlanks(m);
                    s.sell();
                }


                int amountRunsToBuy = getPurchaseAmount(m.getInventory().getAmount("Coins"), m.getInventory().getAmount("Energy potion(4)"), m.getInventory().getAmount("Trout"));
                itemQuantity.put("Trout", amountRunsToBuy * 4);
                itemQuantity.put("Energy potion(4)", amountRunsToBuy * 4);

                for (String item : Items) {

                    m.setCurrentAction("buying " + item);


                    if (m.getInventory().getAmount(item) < itemQuantity.get(item)) {
                        m.getGrandExchange().buyItem(itemID.get(item), item, itemPrice.get(item), itemQuantity.get(item) - (int) m.getInventory().getAmount(item));
                        new ConditionalSleep(10000) {
                            @Override
                            public boolean condition() {
                                return buyFinished();
                            }
                        }.sleep();
                        m.getGrandExchange().collect();

                    }


                }


                m.getGrandExchange().collect();

                if (m.getGrandExchange().isOpen()) {
                    m.getGrandExchange().close();
                }

            }

            if (m.getInventory().contains(i -> i.getName().equals("Trout") && i.getAmount() >= itemQuantity.get("Trout")) && m.getInventory().contains(i -> i.getName().equals("Energy potion(4)") && i.getAmount() >= itemQuantity.get("Energy potion(4)"))) {
                MethodProvider.sleep(1000);
                m.getBank().open();
                MethodProvider.sleep(1000);
                m.getBank().depositAll();
                MethodProvider.sleep(1000);
                m.setTimeToBuy(false);
                if (m.getBank().getAmount("Coins") >= 500000) {
                    m.setTimeToMule(true);
                }
                m.getBank().close();
                if (!m.isTimeToBuy()) {
                    m.log("done");
                }
            }
        }

        return 0;
    }

    public boolean buyFinished() {

        ArrayList<GrandExchange.Box> boxes = new ArrayList<>();
        boxes.add(GrandExchange.Box.BOX_1);
        boxes.add(GrandExchange.Box.BOX_2);
        boxes.add(GrandExchange.Box.BOX_3);
        boolean t = false;

        for (GrandExchange.Box b : boxes) {

            if (m.getGrandExchange().getStatus(b).equals(GrandExchange.Status.FINISHED_BUY)) {
                t = true;
                break;
            }

        }

        return t;
    }


    private int getPurchaseAmount(long CashStack, long currentPotions, long currentTrout) {

        int potionsPerRun = 4;
        int TroutPerRun = 4;
        int multiplier = 1;
        int potionsNeeded = potionsPerRun * 600;
        int TroutNeeded = TroutPerRun * 150;

        if ((multiplier * potionsPerRun) - currentPotions < 0) {
            potionsNeeded = 0;
        }

        if ((multiplier * TroutPerRun) - currentTrout < 0) {
            TroutNeeded = 0;
        }

        while (true) {

            if ((potionsNeeded + TroutNeeded) * multiplier < CashStack && multiplier <= 50) {
                multiplier++;
            } else {
                break;
            }

        }

        return multiplier - 1;


    }
}

