package src;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.Condition;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ScriptManifest(author = "Ryan", info = "Who doesn't love a cool beer?", name = "Plank Scrounger", version = 1, logo = "")


public class main extends Script {


    Position Bank = new Position(3185, 3437, 0);
    boolean hasTeleported;
    Area graveyard = new Position(3166, 3674, 0).getArea(20);
    ArrayList<Position> graveyardPlanks = new ArrayList<>();

    public long startTime;
    long timeRan;
    long totalTripTime;
    long startTripTime;
    String currentAction = null;
    int totalPlanks = 0;
    boolean lockPlank;

    Position lastPlank;
    Position nextPlank;
    int supplyCost = 0;
    
    ArrayList<Node> trainingNodes;
    ArrayList<Node> farmingNodes;

    @Override
    public void onStart() {
        graveyardPlanks.add(new Position(3148, 3671, 0));
        graveyardPlanks.add(new Position(3154, 3670, 0));
        graveyardPlanks.add(new Position(3154, 3659, 0));
        // graveyardPlanks.add(graveyard.getCentralPosition());
        graveyardPlanks.add(new Position(3171, 3680, 0));
        graveyardPlanks.add(new Position(3182, 3669, 0));

        startTime = System.currentTimeMillis();
        
        trainingNodes = new ArrayList<>();
        trainingNodes.add(new getTrainingSupplies(this));
        trainingNodes.add(new killChickens(this));


        farmingNodes = new ArrayList<>();


        
    }


    @Override
    public int onLoop() throws InterruptedException {
    

	if(getSkills().getVirtualLevel(Skill.MAGIC) < 25) {
		
		for(Node n : trainingNodes) {
			if(n.validate()) {
				n.execute();
			}
		}
		
		
		
	}else {
		
  		for(Node n : farmingNodes) {
			if(n.validate()) {
				n.execute();
			}
		}
		
	}
	return 0;
    }
	
	//hello world
    

  

	public void onPaint(Graphics2D g) {

        timeRan = System.currentTimeMillis() - startTime;
        totalTripTime = System.currentTimeMillis() - startTripTime;
        long timeMins = Math.round(timeRan/1000/60);

        g.drawString("Time Ran: " + formatTime(timeRan), 15, 300);
        g.drawString("Planks Scrounged: " + totalPlanks, 15, 320);
        g.drawString("Total Profit: " + ((totalPlanks * 369) - supplyCost) + "gp   (" + (totalPlanks*369)/timeMins * 60 + " GP/h)", 15, 340);
        g.drawString("Current Action: " + currentAction, 15, 280);

    }
    public String formatTime(long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60;
        m %= 60;
        h %= 24;
        return (String.format("%02d:%02d:%02d", h, m, s));
    }
    
    public void setCurrentAction(String s) {
    	
    	currentAction = s;
    }
    
   


    boolean isSetup() {
        return getInventory().contains("Fire rune") && getInventory().contains("Law rune") && getEquipment().isWieldingWeapon("Staff of air");
    }

    Position getNextPlank(Position p){


        lastPlank = p;
        nextPlank = graveyardPlanks.get(graveyardPlanks.indexOf(p) + 1);
        lockPlank = true;





        return nextPlank;
    }
    
    public void walk(Position p, String Step) throws InterruptedException {
    	
    	if(Step == "Graveyard") {
   		 currentAction = "Walking to Plank";

    
             WebWalkEvent e = new WebWalkEvent(p);
             e.setBreakCondition(new Condition() {

					@Override
					public boolean evaluate() {
						return getSettings().isRunning() || (getInventory().isFull() && (!getInventory().contains(i -> i.getName().contains("Vial") || i.getName().contains("Salmon"))) || getInventory().isFull());
					}
             	
             });
             execute(e);

             if (myPlayer().getHealthPercent() < 50 && (getInventory().isFull() && getInventory().contains("Salmon"))) {
                 currentAction = "Eating Food";

                 getInventory().interact("Eat", "Salmon");
                 sleep(500);
             } 
             
             if(getInventory().isFull() && (getInventory().contains("Salmon") || getInventory().contains("Vial"))) {
             	
             	if(getInventory().contains("Vial")) {
             		currentAction = "Dropping vials";
             		getInventory().dropAll("Vial");
             	}else if(getInventory().contains("Salmon")) {                 
                     currentAction = "Eating Food";
                     getInventory().interact("Eat", "Salmon");
                     sleep(500);	
             	}

             }
             
             if (getInventory().isFull() && !getInventory().contains("Salmon")) {
                 currentAction = "Casting Teleport";
                 if (getMagic().castSpell(Spells.NormalSpells.VARROCK_TELEPORT)) {
                     getMagic().castSpell(Spells.NormalSpells.VARROCK_TELEPORT);
                     sleep(500);
                     supplyCost = supplyCost + 1363 ;
                 } else {
                     currentAction = "Error cant teleport";
                 }
             }
             
             if (getSettings().isRunning()) {
                 currentAction = "No running in the graveyard!";
                 getSettings().setRunning(false);
                 sleep(50);
             } 
    		
    	
    	}else if(Step == "Bank") {
            currentAction = "Walking to the graveyard.";
            
        
            WebWalkEvent w = new WebWalkEvent(p);
            w.setBreakCondition(new Condition() {
                @Override
                public boolean evaluate() {
                    return getInventory().contains(i -> i.getName().contains("Energy")) && getSettings().getRunEnergy() < 20;
                }
            });
            execute(w);
            if(getInventory().contains(i -> i.getName().contains("Energy")) && getSettings().getRunEnergy() < 20){
                getInventory().interact("Drink", i -> i.getName().contains("Energy"));
                sleep(250);
            }
    	}
    	
    }
    
 public void executeFarming() throws InterruptedException {
	 try{

	        if (getInventory().isFull()) {


	            if (getBank().closest() != null){

	                if (getBank().closest().getPosition().distance(myPosition()) > 5) {
	                    currentAction = "Walking to bank.";
	                    getWalking().webWalk(Bank);
	                } else {

	                    currentAction = "Setting up next run.";
	                    getBank().open();
	                    getBank().depositAll();
	                    sleep(50);
	                    getBank().withdraw("Law rune", 1);
	                    sleep(50);
	                    getBank().withdraw("Fire rune", 1);
	                    sleep(50);
	                    getBank().withdraw("Salmon", 5);
	                    sleep(50);
	                    getBank().withdraw("Energy potion(4)", 3);
	                    sleep(50);
	                    getBank().close();




	                }
	            } else {

	                if (getMagic().canCast(Spells.NormalSpells.VARROCK_TELEPORT)) {
	                    currentAction = "Teleporting to Varrock.";
	                    getMagic().castSpell(Spells.NormalSpells.VARROCK_TELEPORT);
	                    sleep(3000);
	                    //hasTeleported = true;
	                } else {
	                    currentAction = "Walking to bank.";
	                    getWalking().webWalk(Bank);
	                }

	                if (Bank.getArea(10).contains(myPosition())) {
	                    hasTeleported = false;
	                }

	            }
	        } else if (isSetup()) {
	            if (!graveyard.contains(myPosition())) {
	            		walk(new Position(3148, 3671, 0), "Bank");

	                //getWalking().webWalk(graveyard.getCentralPosition());
	            } else {

	                for(Position plankPosition : graveyardPlanks) {

	                        List<GroundItem> plank = getGroundItems().get(plankPosition.getX(), plankPosition.getY());
	                        if (plank != null) {
	                        	
	                        	do{
	                        		walk(plankPosition,"Graveyard");
	                        	}while(plankPosition.distance(myPosition()) >= 3);

	                                if (plankPosition.distance(myPosition()) < 3) {

	                            for (GroundItem i : plank) {
	                                if (i.getName().equals("Plank")) {

	                                    do {
	                                        
	                                        currentAction = "Picking up plank";
	                                        i.interact("Take");
	                                        sleep(200);
	                                        if(!i.exists()) {
	                                            totalPlanks++;
	                                        }
	                                        
	                                    }
	                                    while (i.exists() && i != null && !getInventory().isFull());


	                                }
	                            }}


	                        }

	                    }



	            }

	            sleep(500);


	        } else {
	            currentAction = "Walking to bank.";
	            getWalking().webWalk(Bank);
	            if (getBank().closest().isVisible()) {
	                currentAction = "Setting up run.";
	                getBank().open();
	                getBank().depositAll();
	                sleep(50);
	                getBank().withdraw("Law rune", 1);
	                sleep(50);
	                getBank().withdraw("Fire rune", 1);
	                sleep(50);
	                getBank().withdraw("Salmon", 5);
	                sleep(50);
	                getBank().withdraw("Energy potion(4)", 3);
	                sleep(50);
	                getBank().close();
	            }
	        }

	        }catch (InterruptedException n){

	         throw n;

	        }catch (Exception e) {
	        	log(e.getStackTrace()[0].getLineNumber());
	        }
    }
}
