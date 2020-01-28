package src;

import org.osbot.rs07.api.Magic;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.MagicSpell;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.api.ui.Tab;

public class killChickens extends Node{

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
                m.sleep(500);
                m.getWidgets().get(201, 1, 1).interact();




            do {

                if (m.getNpcs().closest(i -> i.getName().equals("Chicken") && !i.isUnderAttack()) != null && m.getMagic().isSpellSelected() && !m.myPlayer().isUnderAttack()) {
                    m.setCurrentAction("Killing Chickens!");
                    m.getNpcs().closest(i -> i.getName().equals("Chicken") && !i.isUnderAttack()).interact("Attack");
                }

                m.sleep(500);


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