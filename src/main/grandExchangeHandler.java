package main;

import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;

import java.util.ArrayList;

public class grandExchangeHandler {


    public Area ge;
    main m;

    public grandExchangeHandler(main m) {
        this.m = m;
        this.ge = new Position(3165, 3485, 0).getArea(10);

    }

    public void buyItem(String Item, int ItemID, int Price, int Quantity) throws InterruptedException {
        if (!ge.contains(m.myPosition())) {
            m.setCurrentAction("Walking to GE");
            m.getWalking().webWalk(ge.getCentralPosition());
        } else if (!m.getInventory().contains("Coins")) {
            m.setCurrentAction("Getting Coins");
            m.getBank().open();
            m.getBank().depositAll();
            MethodProvider.sleep(500);
            m.getBank().withdrawAll("Coins");
            MethodProvider.sleep(500);
            m.getBank().close();
        } else if (!m.getInventory().contains(Item) || m.getGrandExchange().isOpen()) {

            if (!m.getGrandExchange().isOpen()) {
                m.setCurrentAction("Opening GE");
                m.getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
                MethodProvider.sleep(2000);
            } else {

                if (!m.getInventory().contains(Item)) {
                    m.setCurrentAction("Buying " + Item);
                    m.getGrandExchange().buyItem(ItemID, Item, Price, Quantity);
                }


                boolean Bought = false;

                MethodProvider.sleep(3000);

                ArrayList<GrandExchange.Box> box = new ArrayList<>();
                box.add(GrandExchange.Box.BOX_1);
                box.add(GrandExchange.Box.BOX_2);
                box.add(GrandExchange.Box.BOX_3);
                box.add(GrandExchange.Box.BOX_4);
                box.add(GrandExchange.Box.BOX_5);
                box.add(GrandExchange.Box.BOX_6);
                box.add(GrandExchange.Box.BOX_7);
                box.add(GrandExchange.Box.BOX_8);

                for (int i = 0; i < 7; i++) {
                    if (m.getGrandExchange().getStatus(box.get(i)).equals(GrandExchange.Status.FINISHED_BUY)) {
                        Bought = true;
                    }
                }

                if (Bought) {
                    m.setCurrentAction("Collecting Item");
                    m.getGrandExchange().collect();
                    MethodProvider.sleep(2000);

                }

                if (Bought && m.getInventory().contains("Coins") && !m.getInventory().contains(Item)) {
                    m.setCurrentAction("Item Bought");
                    m.getGrandExchange().close();
                }


            }
        }
    }

    public void sellItem(String Item, int Price, int Quantity) throws InterruptedException {

        if (!ge.contains(m.myPosition())) {
            m.setCurrentAction("Walking to GE");
            m.getWalking().webWalk(ge.getCentralPosition());
        } else if (!m.getInventory().contains(Item)) {
            m.setCurrentAction("Getting " + Item);
            m.getBank().open();
            m.getBank().depositAll();
            MethodProvider.sleep(500);
            m.getBank().enableMode(Bank.BankMode.WITHDRAW_NOTE);
            MethodProvider.sleep(500);
            m.getBank().withdrawAll(Item);
            MethodProvider.sleep(500);
            m.getBank().withdrawAll("Coins");
            MethodProvider.sleep(500);
            m.getBank().close();
            m.log("1");
            if (m.getInventory().contains(Item) || m.getGrandExchange().isOpen()) {

                if (!m.getGrandExchange().isOpen()) {
                    m.log("2");
                    m.setCurrentAction("Opening GE");
                    m.getNpcs().closest("Grand Exchange Clerk").interact("Exchange");
                    MethodProvider.sleep(2000);


                    if (m.getInventory().contains(Item)) {
                        m.setCurrentAction("Selling " + Item);
                        m.getGrandExchange().sellItem(m.getInventory().getItem(Item).getId(), Price, Quantity);
                    }


                    boolean sold = false;

                    MethodProvider.sleep(3000);

                    ArrayList<GrandExchange.Box> box = new ArrayList<>();
                    box.add(GrandExchange.Box.BOX_1);
                    box.add(GrandExchange.Box.BOX_2);
                    box.add(GrandExchange.Box.BOX_3);
                    box.add(GrandExchange.Box.BOX_4);
                    box.add(GrandExchange.Box.BOX_5);
                    box.add(GrandExchange.Box.BOX_6);
                    box.add(GrandExchange.Box.BOX_7);
                    box.add(GrandExchange.Box.BOX_8);

                    for (int i = 0; i < 7; i++) {
                        if (m.getGrandExchange().getStatus(box.get(i)).equals(GrandExchange.Status.FINISHED_SALE)) {
                            sold = true;
                        }
                    }

                    if (sold) {
                        m.setCurrentAction("Collecting Cash");
                        m.getGrandExchange().collect();
                        MethodProvider.sleep(2000);

                    }

                    if (sold && m.getInventory().contains("Coins") && !m.getInventory().contains(Item)) {
                        m.setCurrentAction("Item Sold");
                        m.getGrandExchange().close();
                    }


                }


            }

        }
    }
}
