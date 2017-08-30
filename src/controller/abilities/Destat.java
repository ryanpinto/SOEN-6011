package controller.abilities;

import model.Pokemon;

public class Destat extends Ability{

	private String target;//last
	
	@Override
	public boolean fire() {
		Pokemon targ = null;
		if(target.equals("last")){
			targ = abInfo.lastTarget;
		}
		if(targ != null){
			targ.destat();
			return afterAttack(true);
		}else{
			return afterAttack(false);
		}
	}
	
	
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	

	public String toString(){
		String s = "";
		s+="remove all states from the pokemon";
		return s;
	}
}
