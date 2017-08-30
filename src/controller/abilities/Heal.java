package controller.abilities;

import controller.GameController;
import model.Player.Role;
import model.Pokemon;

public class Heal extends Ability{

	private String target;//your-active, self(the pokemon has this ability), choice
	private String chooseFrom = "";//your
	private int amount = 0;
	private Counter counter = null;
	@Override
	public boolean fire() {
		Pokemon p = null;
		System.out.println(abInfo.owner);
		if(target.equals("your-active")){
			if(abInfo.owner.getActive1() != null){
				p = (Pokemon)abInfo.owner.getActive1();
				abInfo.lastTarget = p;
			}else{
				return afterAttack(false);
			}
		}else if(target.equals("self")){
			if(abInfo.card != null){
				p = abInfo.card;
				abInfo.lastTarget = p;
			}else{
				return afterAttack(false);
			}
		}else if(target.equals("choice")){
			if(chooseFrom.equals("your")){
				if(abInfo.owner.getRole() == Role.YOU){
					if(abInfo.selectedCard == null){
						GameController.getController().getBoard().setNotification("Please select one of your pokemons to heal", false);
						return select();
					}else{
						p = (Pokemon)abInfo.selectedCard.getCard();
						abInfo.lastTarget = p;
					}
					
				}else{//opponent
					abInfo.selectedCard = GameController.getController().getOpponentBrain().selectCard(abInfo.owner, chooseFrom);
					abInfo.lastTarget = (Pokemon)abInfo.selectedCard.getCard();
					p = (Pokemon)abInfo.selectedCard.getCard();
				}
			}else{
				return afterAttack(false);
			}
		}else{
			return afterAttack(false);
		}
		
		if(counter != null){
			amount = counter.count(abInfo);
		}
		p.increaseHP(amount);
		p.setHealed(true);
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
	public String getChooseFrom() {
		return chooseFrom;
	}
	public void setChooseFrom(String chooseFrom) {
		this.chooseFrom = chooseFrom;
	}
	
	public String toString(){
		String s = "";
		s+="heal ";
		if(counter != null){
			s+=counter.toString();
		}
		else{
			s+=amount;
		}
		s+=" damage from ";
		if(target.equals("choice")){
			s+="one of your pokemon";
		}
		else if(target.equals("your-active")){
			s+=target;
		}
		else{
			s+="the pokemon";
		}
		return s;
	}

}
