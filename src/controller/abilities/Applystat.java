package controller.abilities;

import model.Pokemon;
import model.Pokemon.Status;

public class Applystat extends Ability{

	private String status;//paralyzed, stuck, poisoned, asleep
	private String target;//opponent-active
	@Override
	public boolean fire() {
		Pokemon targ = null;
		if(target.equals("opponent-active")){
			targ = (Pokemon)abInfo.opponent.getActive1();
			if(targ != null){
				if(status.equals("paralyzed")){
					targ.setStatus(Status.PARALYZED);
					abInfo.lastTarget = targ;
				}else if(status.equals("stuck")){
					targ.setStatus(Status.STUCK);
					abInfo.lastTarget = targ;
				}else if(status.equals("poisoned")){
					targ.setStatus(Status.POISONED);
					abInfo.lastTarget = targ;
				}else if(status.equals("asleep")){
					targ.setStatus(Status.ASLEEP);
					abInfo.lastTarget = targ;
				}
			}else{//null
				return afterAttack(false);
			}
				
		}else{//not opponent active
			return afterAttack(false);
		}
		return afterAttack(true);
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	public String toString(){
		String s = "Apply status "+status+" to "+target;
		return s;
	}
	
}
