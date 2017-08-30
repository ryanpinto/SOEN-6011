package controller.abilities;

import controller.GameController;
import model.Pokemon;

public class Add extends Ability{

	private Ability ability;//heal
	private String trigger;//opponent-turn-end
	private String target;//your
	@Override
	public boolean fire() {
		return ability.fire();
	}
	
	public void reset(){
		abInfo.reset();
		ability.reset();
	}
	
	public Ability getAbility() {
		return ability;
	}
	public void setAbility(Ability ability) {
		this.ability = ability;
	}
	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	public String toString(){
		String s = ability.toString()+" when "+trigger;
		return s;
	}
	
	public void setPokemon(Pokemon p){
		abInfo.card = p;
		GameController.getController().registerAbility(this);
	}
	
	public void update(){
		if(trigger.equals("opponent-turn-end")){
			if(abInfo.card != null && abInfo.card.getOwner() == GameController.getController().getCurrentPlayer()){
				fire();
			}
		}
		
	}
	
}
