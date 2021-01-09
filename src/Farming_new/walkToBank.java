package Farming_new;

import main.main;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

public class walkToBank {
    main m;
    Area town = new Area(3123, 3639, 3154, 3617);


    public walkToBank(main m) {
        this.m = m;
    }

//    public void gotToBank() throws InterruptedException {
//
//
//    }

    public void enterFromTop() throws InterruptedException {

        if (m.myPosition().getY() > 3644) {
            WebWalkEvent w = new WebWalkEvent(new Position(3134, 3640, 0));
            w.setBreakCondition(new Condition() {
                @Override
                public boolean evaluate() {
                    return m.getInventory().getAmount("Trout") > 1 && m.myPlayer().getHealthPercent() < 50;
                }
            });
            m.execute(w);
            if (m.getInventory().getAmount("Trout") > 1) {
                m.getInventory().interact("Eat", "Trout");
            }
        } else {
//        Position topBarrier = new Position(3134,3639,0);
            if (m.myPosition().equals(new Position(3134, 3640, 0)) || m.myPosition().equals(new Position(3135, 3640, 0))) {
                m.log("passing");
                m.getCamera().moveYaw(0);
                m.getCamera().movePitch(22);
                new ConditionalSleep(2000) {
                    @Override
                    public boolean condition() {
                        return m.getCamera().getPitchAngle() == 22 && m.getCamera().getYawAngle() == 0;
                    }
                }.sleep();
                m.myPosition().hover(m.getBot());
                MethodProvider.sleep(500);
                m.getMouse().click(false);
                new ConditionalSleep(2000) {
                    @Override
                    public boolean condition() {
                        return !town.contains(m.myPosition());
                    }
                }.sleep();
            } else {
                m.log("Walking to barrier");
                WalkingEvent w = new WalkingEvent(new Position(3134, 3640, 0));
                w.setMinDistanceThreshold(0);
                m.execute(w);
            }
//        MethodProvider.sleep(1500);

        }
    }


    public void enterFromBottom() throws InterruptedException {
        if (m.myPosition().getY() < 3605) {
            WebWalkEvent w = new WebWalkEvent(new Position(3134, 3610, 0));
            w.setBreakCondition(new Condition() {
                @Override
                public boolean evaluate() {
                    return m.getInventory().getAmount("Trout") > 1 && m.myPlayer().getHealthPercent() < 50;
                }
            });
            m.execute(w);
            if (m.getInventory().getAmount("Trout") > 1) {
                m.getInventory().interact("Eat", "Trout");
            }
        } else {


//        m.getWalking().walk(new Position(3134, 3616, 0));

//        Position bottomBarrier = new Position(3134,3617,0);
            if (m.myPosition().equals(new Position(3134, 3616, 0)) || m.myPosition().equals(new Position(3134, 3616, 0))) {
                m.log("passing");
                m.getCamera().moveYaw(180);
                m.getCamera().movePitch(22);
                new ConditionalSleep(2000) {
                    @Override
                    public boolean condition() {
                        return m.getCamera().getPitchAngle() == 22 && m.getCamera().getYawAngle() == 180;
                    }
                }.sleep();
                m.myPosition().hover(m.getBot());
                MethodProvider.sleep(500);
                m.getMouse().click(false);
                new ConditionalSleep(2000) {
                    @Override
                    public boolean condition() {
                        return town.contains(m.myPosition());
                    }
                }.sleep();
            } else {
                m.log("Walking to barrier");
                m.getWalking().walk(new Position(3134, 3616, 0));
            }
//        MethodProvider.sleep(1500);

        }
    }
}
