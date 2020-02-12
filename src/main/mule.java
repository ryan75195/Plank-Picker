package main;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class mule extends Node {

    public mule(main m) {
        super(m);
    }

    Position grandExchange = new Position(3165, 3487, 0);
    String mule = "75cowsodin";

    @Override
    public boolean validate() {
        return m.isTimeToMule();
    }

    @Override
    public int execute() throws InterruptedException {

        if (!grandExchange.getArea(20).contains(m.myPosition())) {
            m.getWalking().webWalk(grandExchange);
        } else if (m.getPlayers().closest(mule) != null) {

            if (m.getSkills().getVirtualLevel(Skill.MAGIC) >= 25 && m.getInventory().contains("Coins")) {
                giveGold();
            } else {
                getGold();
            }


        } else {
            m.log("no mule available");
        }


        return 0;
    }

    void giveGold() throws InterruptedException {
        m.log("Giving Coins");
        if (!m.getTrade().isCurrentlyTrading()) {
            m.getPlayers().closest(mule).interact("Trade-with");
            MethodProvider.sleep(3000);
        } else {

            if (m.getTrade().isFirstInterfaceOpen()) {

                if (!m.getTrade().getOurOffers().contains("Coins")) {
                    m.getTrade().offer(995, (int) m.getInventory().getAmount("Coins"));
                    MethodProvider.sleep(3000);

                }

                if (m.getTrade().didOtherAcceptTrade()) {
                    m.getTrade().acceptTrade();
                    MethodProvider.sleep(3000);

                }

            }

            if (m.getTrade().isSecondInterfaceOpen()) {

                if (m.getTrade().didOtherAcceptTrade()) {
                    m.getTrade().acceptTrade();
                    MethodProvider.sleep(3000);

                }
            }


            if (!m.getInventory().contains("Coins")) {

                m.setTimeToMule(false);
            }

        }

    }

    void getGold() throws InterruptedException {
        m.log("Getting gold");
        if (!m.getTrade().isCurrentlyTrading()) {
            m.getPlayers().closest(mule).interact("Trade with");
            MethodProvider.sleep(3000);

        } else {

            if (m.getTrade().isFirstInterfaceOpen()) {

                if (m.getTrade().getTheirOffers().contains("Coins")) {
                    m.getTrade().acceptTrade();
                    MethodProvider.sleep(3000);

                }


            }

            if (m.getTrade().isSecondInterfaceOpen()) {

                if (m.getTrade().didOtherAcceptTrade()) {
                    m.getTrade().acceptTrade();
                    MethodProvider.sleep(3000);

                }
            }

            if (m.getInventory().contains("Coins")) {

                m.setTimeToMule(false);
            }
        }

    }

}


