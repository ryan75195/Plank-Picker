import org.osbot.rs07.api.Magic;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.MagicSpell;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;

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
	public int execute() {
		
		if(chickenPen.contains(m.myPosition())) {
			
		
				try {
					if(m.getMagic().canCast(Spells.NormalSpells.WIND_STRIKE)) {
						m.getMagic().castSpell(Spells.NormalSpells.WIND_STRIKE);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(m.getNpcs().closest(i -> i.getName().equals("Chicken") && !i.isUnderAttack()) != null && m.getMagic().isSpellSelected() && !m.myPlayer().isUnderAttack()) {
				m.setCurrentAction("Killing Chickens!");
				m.getNpcs().closest(i -> i.getName().equals("Chicken") && !i.isUnderAttack()).interact("Attack");
				try {
					m.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}else m.getWalking().webWalk(chickenPen);
		
		
		
		
		
		return 0;
	}





boolean isValid() {
	
	return m.getInventory().contains("Mind rune") && m.getEquipment().contains("Staff of air") && m.getSkills().getVirtualLevel(Skill.MAGIC) < 25;
	}

}