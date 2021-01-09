package main;

import Farming_new.walkToBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class mule extends Node {

    public mule(main m) {
        super(m);
    }

    Position grandExchange = new Position(3165, 3487, 0);
    public Position meetSpot = new Position(3134, 3599, 0);
    String mule = null;
    String PID = "";
    Area town = new Area(3123, 3639, 3154, 3617);
    boolean successful = false;
    Socket soc = null;
    boolean requestSent = false;

    @Override
    public boolean validate() {
        return m.isTimeToMule();
    }

    @Override
    public int execute() throws InterruptedException, IOException {

        if (!m.getInventory().contains("Coins")) {
            walkToBank w = new walkToBank(m);
            if (m.myPosition().getY() > 3639) {
                w.enterFromTop();
            } else if (m.myPosition().getY() < 3617) {
                w.enterFromBottom();
            }
            if (town.contains(m.myPosition())) {
                m.getBank().open();
                if (m.getBank().isOpen()) {
                    m.getBank().depositAll();
                    MethodProvider.sleep(500);
                    m.getBank().withdrawAll("Coins");
                    m.getBank().close();
                }
            }
        } else {

            if (soc == null) {
                soc = new Socket("localhost", 6000);
            }

            if (m.myPlayer().getHealthPercent() == 0 || successful) {
                successful = true;
                m.setTimeToMule(false);
                m.log(m.isTimeToMule());
                if (!soc.isClosed() && soc.isConnected()) {
                    soc.close();
                }
            } else if (m.myPosition().distance(meetSpot) > 5) {
                m.log("3");
                walkToSpot();
            } else {

                m.log("Muling");
                Area lumbridge = new Position(3219, 3218, 0).getArea(20);
                socketHandler s = new socketHandler(soc, m);

                if (mule == null) {
                    if (!requestSent) {
                        ArrayList<String> response = s.getNameAndPID(s.sendRequest("1," + m.myPlayer().getName() + "," + "ge," + "301" + "," + "true"));
                        mule = response.get(0);
                        PID = response.get(1);
                        requestSent = true;
                    }
                    m.log("Mule: " + mule + " is on route to your location.");
                    new ConditionalSleep(10000000) {
                        @Override
                        public boolean condition() {
                            return m.getPlayers().closest(mule) != null;
                        }
                    }.sleep();
                }

                if (m.getPlayers().closest(mule) != null) {
                    giveGold(mule);
//            String val = s.sendRequest("2," + PID);
//            m.log(val);
                } else {
                    m.log("no mule available");
                }
            }
        }

        return 50;
    }

    void giveGold(String muleName) {
        m.log("Giving Coins");
        Player mule = m.getPlayers().closest(muleName);
        m.log(5);

        if (m.getInventory().contains("Coins") && mule != null) {
            m.log(1);
            if (m.myPlayer().getSkullIcon() == -1) {
                m.log(2);
                mule.interact("Attack");
            }
            if (m.myPlayer().getSkullIcon() != -1 && mule.isHitBarVisible()) {
                m.log(3);

                m.myPlayer().interact();
            }
        }

        if (!m.getInventory().contains("Coins") && mule == null) {
            m.log(4);

                m.setTimeToMule(false);
            }

        }


    void getGold(String muleName) throws InterruptedException {
        m.log("Getting gold");
        if (!m.getTrade().isCurrentlyTrading()) {
            m.getPlayers().closest(muleName).interact("Trade with");
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

//            if (m.getInventory().contains("Coins")) {
//
//                m.setTimeToMule(false);
//            }
        }

    }


    public void walkToSpot() throws InterruptedException {
//        log(myPosition().getY() >= 3617);
        WebWalkEvent w = new WebWalkEvent(new Position(meetSpot.getX(), meetSpot.getY(), 0));
        w.setBreakCondition(new Condition() {
            @Override
            public boolean evaluate() {
                return (town.contains(m.myPosition())) /*&& bottomBarrier.getPosition().distance(myPosition()) < 5)*/;
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

}


