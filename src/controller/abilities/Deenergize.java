package controller.abilities;

import model.Card;
import model.Player;
import model.Pokemon;

public class Deenergize extends Ability{

	private String target;//your-active, opponent-active
	private int amount = 0;
	private Counter counter = null;
	@Override
	public boolean fire() {
		Pokemon p = null;
		Player player = null;
		if(target.equals("your-active")){
			if(abInfo.owner.getActive1() != null){
				p = (Pokemon)abInfo.owner.getActive1();
				player = abInfo.owner;
			}else{
				return afterAttack(false);
			}
		}else if(target.equals("opponent-active")){
			if(abInfo.opponent.getActive1() != null){
				p = (Pokemon)abInfo.opponent.getActive1();
				player = abInfo.opponent;
			}else{
				return afterAttack(false);
			}
		}else{
			return afterAttack(false);
		}
		abInfo.lastTarget = p;
		if(counter != null){
			amount = counter.count(abInfo);
		}
		for(int i = 0; i<amount; i++){
			Card c = p.deenergize();
			if(c == null){
				return afterAttack(false);
			}else{
				player.getDiscard().add(c);
				player.getCardTiles().get(13).setCard(c);
			}
		}
		return afterAttack(true);	
	}
	
	
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Counter getCounter() {
		return counter;
	}
	public void setCounter(Counter counter) {
		this.counter = counter;
	}

	public String toString(){
		String s = "";
		s+="remove ";
		if(counter != null){
			s+=counter.toString();
		}
		else{
			s+=amount+" energy";
		}
		s+=" from "+target;
		return s;
	}
	
}
