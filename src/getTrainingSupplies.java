package src;

public class getTrainingSupplies extends Node{

	public getTrainingSupplies(main m) {
		super(m);
	}

	@Override
	public boolean validate() {
		return !m.getInventory().contains("Mind rune") || !m.getEquipment().contains("Staff of air");
	}

	@Override
	public int execute() throws InterruptedException {

	    if(m.getInventory().contains("Staff of air")){
	        m.getInventory().interact("Wield", "Staff of air");
        }

	    if(m.getBank().closest() != null && !m.getBank().isOpen()){
	        m.getBank().open();
	        m.getBank().depositAll();
        }

        if(m.getBank().isOpen()) {

            if (!m.getInventory().contains("Mind rune") && !m.getBank().contains("Mind rune")) {
                buyMind = true;
            }else if(m.getBank().contains("Mind rune")){
                m.getBank().withdrawAll("Mind rune");
            }

            if(!m.getEquipment().contains("Staff of air") && !m.getInventory().contains("Staff of air") && !m.getBank().contains("Staff of air")){
                buyStaff = true;
            }else if(m.getBank().contains("Staff of air")){
                m.getBank().withdraw("Staff of air", 1);
            }

        }
		return 0;
	}

}
