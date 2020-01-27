
public class getTrainingSupplies extends Node{

	public getTrainingSupplies(main m) {
		super(m);
	}

	@Override
	public boolean validate() {
		return !m.getInventory().contains("Mind rune");
	}

	@Override
	public int execute() {
		return 0;
	}

}
