package controller.abilities;

import controller.GameController;
import model.Card;
import model.Pokemon;
import model.Player.Role;

public class Redamage extends Ability{

	private String source;//opponent : you choose
	private String dest;//opponent : you choose
	private int amount = 0;
	private Counter counter = null;
	
	@Override
	public boolean fire() {

		Pokemon s = null;
		Pokemon d = null;
		
			if(source.equals("opponent")){
				if(abInfo.owner.getRole() == Role.YOU){
					if(abInfo.selectedCard == null){
						GameController.getController().getBoard().setNotification("Please select one opponent pokemon to move damage from", false);
						return select();
					}else{
						s = (Pokemon)abInfo.selectedCard.getCard();
						abInfo.lastSource = s;
						//abInfo.selectedCard = null;
					}
				}else{//opponent
					abInfo.selectedCard = GameController.getController().getOpponentBrain().selectCard(abInfo.opponent, source);
					s = (Pokemon)abInfo.selectedCard.getCard();
					abInfo.lastSource = s;
					//abInfo.selectedCard = null;
				}
			}else{//not opponent
				return afterAttack(false);
			}
		
		
		
			if(dest.equals("opponent")){
				if(abInfo.owner.getRole() == Role.YOU){
					if(abInfo.selectedCard2 == null){
						GameController.getController().getBoard().setNotification("Please select one opponent pokemon to move damage to", false);
						return select();
					}else{
						d = (Pokemon)abInfo.selectedCard2.getCard();
						//abInfo.selectedCard2 = null;
					}
				}else{//opponent
					abInfo.selectedCard2 = GameController.getController().getOpponentBrain().selectCard(abInfo.opponent, dest);
					d = (Pokemon)abInfo.selectedCard2.getCard();
					//abInfo.selectedCard2 = null;
				}
			
			}else{//not opponent
				return afterAttack(false);	
			}
		
			if(counter != null){
				amount = counter.count(abInfo);
			}
			
			s.increaseHP(amount);
			d.decreaseHP(amount);
		abInfo.selectedCard = null;
		abInfo.selectedCard2 = null;
		return afterAttack(true);
	
		
	}

	public String getSource() {
		return source;
	}

	public void setSource(String sourceTarget) {
		this.source = sourceTarget;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
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
		s+="move damage as much as ";
		if(counter != null){
			s+=counter.toString();
		}
		else{
			s+=amount;
		}
		s+="  from one of "+source+" pokemons to another one of "+dest+" pokemons";
		return s;
	}

}
