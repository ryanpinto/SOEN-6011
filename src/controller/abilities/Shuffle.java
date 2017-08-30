package controller.abilities;

public class Shuffle extends Ability{

	private String target;//your, you, opponent
	@Override
	public boolean fire() {
		if(target.equals("your") || target.equals("you")){
			abInfo.owner.shuffle();
		}else if(target.equals("opponent")){
			abInfo.opponent.shuffle();
		}else{
			return afterAttack(false);
		}
		return afterAttack(true);
	}
	
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	public String toString(){
		String s = "";
		s+=target+" shuffle deck";
		return s;
	}
}
