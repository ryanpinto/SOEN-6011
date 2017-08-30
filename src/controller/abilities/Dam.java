package controller.abilities;

import controller.GameController;
import model.Player.Role;
import model.Pokemon;

public class Dam extends Ability{

	private String target;//opponent-active, your-active, choice, your-bench, opponent(you choose one of)
	private String chooseFrom = "";//opponent, opponent-bench
	private int amount = 0;
	private Counter count = null;
	
	
	@Override
	public boolean fire() {
		
		if(target.equals("opponent")){
			if(abInfo.owner.getRole() == Role.OPPONENT){
				abInfo.selectedCard = GameController.getController().getOpponentBrain().selectCard(abInfo.opponent, "opponent");
				Pokemon p = (Pokemon)abInfo.selectedCard.getCard();
				abInfo.lastTarget = p;
				if(count != null){
					amount = count.count(abInfo);
				}
				boolean died = p.decreaseHP(amount);
				if(died){
					abInfo.owner.moveCardBtwTiles(abInfo.selectedCard, abInfo.opponent.getCardTiles().get(13), "");
				}
				abInfo.selectedCard = null;
				
			}else{
				if(abInfo.selectedCard == null){
					GameController.getController().getBoard().setNotification("Please select one of opponent pokemon to damage", false);
					return select();
				}else{
					Pokemon p = (Pokemon)abInfo.selectedCard.getCard();
					abInfo.lastTarget = p;
					if(count != null){
						amount = count.count(abInfo);
					}
					boolean died = p.decreaseHP(amount);
					if(died){
						abInfo.owner.moveCardBtwTiles(abInfo.selectedCard, abInfo.opponent.getCardTiles().get(13), "");
					}
					abInfo.selectedCard = null;
				}
			}
			
			
			
		}else if(target.equals("opponent-active")){
			if(abInfo.opponent.getActive1() != null){
				Pokemon p = (Pokemon)abInfo.opponent.getActive1();
				System.out.println("op active "+p.getName()+" HP "+p.getHP());
				abInfo.lastTarget = p;
				if(count != null){
					amount = count.count(abInfo);
				}
				
				boolean died = p.decreaseHP(amount);
					if(died){
						abInfo.opponent.moveCardBtwTiles(abInfo.opponent.getCardTiles().get(0), abInfo.opponent.getCardTiles().get(13), "top");
					}
					//return afterAttack(true);
			}else{
				return afterAttack(false);
			}
			
			
			
		}else if(target.equals("your-active")){
			if(abInfo.owner.getActive1() != null){
				Pokemon p = (Pokemon)abInfo.owner.getActive1();
				abInfo.lastTarget = p;
				if(count != null){
					amount = count.count(abInfo);
				}
				boolean died = p.decreaseHP(amount);
					if(died){
						abInfo.owner.moveCardBtwTiles(abInfo.owner.getCardTiles().get(0), abInfo.owner.getCardTiles().get(13), "top");
					}	
			}else{//null
				return afterAttack(false);
			}
			
			
			
		}else if(target.equals("your-bench")){
			if(count != null){
				amount = count.count(abInfo);
			}
			for(int i = 2; i<7; i++){
				if(abInfo.owner.getCardTiles().get(i).getCard() != null){
					boolean died = ((Pokemon)abInfo.owner.getCardTiles().get(i).getCard()).decreaseHP(amount);
					if(died){
						abInfo.owner.moveCardBtwTiles(abInfo.owner.getCardTiles().get(i), abInfo.owner.getCardTiles().get(13), "top");
					}
				}	
			}
			
			
		}else if(target.equals("choice")){
			
			if(abInfo.owner.getRole() == Role.OPPONENT){
				abInfo.selectedCard = GameController.getController().getOpponentBrain().selectCard(abInfo.opponent, chooseFrom);
				Pokemon p = (Pokemon)abInfo.selectedCard.getCard();
				abInfo.lastTarget = p;
				if(count != null){
					amount = count.count(abInfo);
				}
				boolean died = p.decreaseHP(amount);
				if(died){
					abInfo.owner.moveCardBtwTiles(abInfo.selectedCard, abInfo.opponent.getCardTiles().get(13), "top");
				}
				abInfo.selectedCard = null;
				
			}else{//you
				if(abInfo.selectedCard == null){
					GameController.getController().getBoard().setNotification("Please select one of "+ chooseFrom+" pokemon to damage", false);
					return select();
				}else{
					Pokemon p = (Pokemon)abInfo.selectedCard.getCard();
					abInfo.lastTarget = p;
					if(count != null){
						amount = count.count(abInfo);
					}
					boolean died = p.decreaseHP(amount);
					if(died){
						abInfo.owner.moveCardBtwTiles(abInfo.selectedCard, abInfo.opponent.getCardTiles().get(13), "top");
					}
					abInfo.selectedCard = null;
				}
			}
			
			
		}else{
			System.out.println("no attack applied");
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


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public Counter getCount() {
		return count;
	}


	public void setCount(Counter count) {
		this.count = count;
	}


	public String getChooseFrom() {
		return chooseFrom;
	}


	public void setChooseFrom(String chooseFrom) {
		this.chooseFrom = chooseFrom;
	}

	public String toString(){
		String s = "";
		
		s+="Apply ";
		if(count != null){
			s+="damage equals to "+count.toString();
		}
		else{
			s+=amount+" damage";
		}
		//s+=" damage";
		if(target.equals("choice")){
			s+=" to one of "+chooseFrom+" pokemons";
		}
		else if(target.equals("opponent")){
			s+=" to one of "+target+" pokemons";
		}
		else{
			s+=" to "+target+" pokemon";
		}
		
		return s;
	}
}
