package Training;

import main.Node;
import main.main;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;

public class killChickens extends Node {

	public killChickens(main m) {
		super(m);
	}
	
	Area chickenPen = new Position(3231,3296,0).getArea(6);

	@Override
	public boolean validate() {
		return isValid();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int execute() throws InterruptedException {
		
		if(chickenPen.contains(m.myPosition())) {


                m.setCurrentAction("Selecting spell");
                m.getTabs().open(Tab.ATTACK);
                m.getWidgets().get(593, 22).interact();
            MethodProvider.sleep(500);
                m.getWidgets().get(201, 1, 1).interact();




            do {

                NPC chicken = m.getNpcs().closest(i -> i.getName().equals("Chicken") && !i.isUnderAttack());
                if (chicken != null && !m.myPlayer().isUnderAttack()) {


                    do {
                        m.setCurrentAction("Killing Chickens!");
                        chicken.interact("Attack");
                        MethodProvider.sleep(5000);
                    } while (chicken != null);
                }

                //m.sleep(500);


            } while (m.getSkills().getVirtualLevel(Skill.MAGIC) < 25);

        }else {
            m.setCurrentAction("Walking to pen");
            m.getWalking().webWalk(chickenPen);
        }
		
		return 0;
	}





boolean isValid() {
	
	return m.getInventory().contains("Mind rune") && m.getEquipment().contains("Staff of air") && m.getSkills().getVirtualLevel(Skill.MAGIC) < 25;
	}

}