package controller.abilities;

import java.util.ArrayList;

import controller.GameController;
import model.CardTile;
import model.Player.Role;

public class Deck extends Ability{

	private String target;//your, opponent
	private String dest;//deck, discard
	private String destTopBottom = "";//bottom, top
	private boolean choice = false;
	private String choiceTarget = "";//them(op hand), you(your hand), random(not in the file but in the email)
	private int amount = 0;//no. of cards
	private Counter counter = null;
	
	@Override
	public boolean fire() {
		if(counter != null){
			amount = counter.count(abInfo);
			if(counter.getTarget().equals("your-hand")){
				int x = Math.min(amount, abInfo.owner.getHandTiles().size());
				for(int i= 0; i<x; i++){
					if(dest.equals("deck")){
						abInfo.owner.returnHandCardToDeck(abInfo.owner.getHandTiles().get(0), destTopBottom);
					}else if(dest.equals("discard")){
						abInfo.owner.moveCardBtwTiles(abInfo.owner.getHandTiles().get(0), abInfo.owner.getCardTiles().get(13), destTopBottom);
					}
				}
			}else if(counter.getTarget().equals("opponent-hand")){
				int x = Math.min(amount, abInfo.owner.getHandTiles().size());
				for(int i= 0; i<x; i++){
					if(dest.equals("deck")){
						abInfo.opponent.returnHandCardToDeck(abInfo.opponent.getHandTiles().get(0), destTopBottom);
					}else if(dest.equals("discard")){
						abInfo.opponent.moveCardBtwTiles(abInfo.opponent.getHandTiles().get(0), abInfo.opponent.getCardTiles().get(13), destTopBottom);
					}
				}
			}
		}else if(choice){
			if(target.equals("your") && abInfo.owner.getRole() == Role.YOU){
				if(abInfo.owner.getHandTiles().size()<=amount){
					for(int i= 0; i<abInfo.owner.getHandTiles().size(); i++){
						if(dest.equals("deck")){
							abInfo.owner.returnHandCardToDeck(abInfo.owner.getHandTiles().get(0), destTopBottom);
						}else if(dest.equals("discard")){
							abInfo.owner.moveCardBtwTiles(abInfo.owner.getHandTiles().get(0), abInfo.owner.getCardTiles().get(13), destTopBottom);
						}
					}
				}else if(abInfo.selectedCards == null){
					abInfo.selectedCards = new ArrayList<CardTile>();
					GameController.getController().getBoard().setNotification("Please select "+amount+" hand card to return to "+dest, false);
					return select();
				}else{
					for(int i= 0; i<amount; i++){
						if(dest.equals("deck")){
							abInfo.owner.returnHandCardToDeck(abInfo.selectedCards.get(i), destTopBottom);
						}else if(dest.equals("discard")){
							abInfo.owner.moveCardBtwTiles(abInfo.selectedCards.get(i), abInfo.owner.getCardTiles().get(13), destTopBottom);
						}
					}
					abInfo.selectedCards = null;
				}
				
			}else if(target.equals("opponent") && abInfo.opponent.getRole() == Role.YOU){
				if(abInfo.opponent.getHandTiles().size()<=amount){
					for(int i= 0; i<abInfo.opponent.getHandTiles().size(); i++){
						if(dest.equals("deck")){
							abInfo.opponent.returnHandCardToDeck(abInfo.opponent.getHandTiles().get(0), destTopBottom);
						}else if(dest.equals("discard")){
							abInfo.opponent.moveCardBtwTiles(abInfo.opponent.getHandTiles().get(0), abInfo.opponent.getCardTiles().get(13), destTopBottom);
						}
					}
				}else if(abInfo.selectedCards == null){
					abInfo.selectedCards = new ArrayList<CardTile>();
					GameController.getController().getBoard().setNotification("Please select "+amount+" hand card to return to "+dest, false);
					return select();
				}else{
					for(int i= 0; i<amount; i++){
						if(dest.equals("deck")){
							abInfo.opponent.returnHandCardToDeck(abInfo.selectedCards.get(i), destTopBottom);
						}else if(dest.equals("discard")){
							abInfo.opponent.moveCardBtwTiles(abInfo.selectedCards.get(i), abInfo.opponent.getCardTiles().get(13), destTopBottom);
						}
					}
					abInfo.selectedCards = null;
				}
			}else if(target.equals("your") && abInfo.owner.getRole() == Role.OPPONENT){
				abInfo.selectedCards = GameController.getController().getOpponentBrain().selectCard(abInfo.owner, amount);
				for(int i= 0; i<amount; i++){
					if(dest.equals("deck")){
						abInfo.owner.returnHandCardToDeck(abInfo.selectedCards.get(i), destTopBottom);
					}else if(dest.equals("discard")){
						abInfo.owner.moveCardBtwTiles(abInfo.selectedCards.get(i), abInfo.owner.getCardTiles().get(13), destTopBottom);
					}
					abInfo.selectedCards = null;
				}
			}else if(target.equals("opponent") && abInfo.owner.getRole() == Role.OPPONENT){
				abInfo.selectedCards = GameController.getController().getOpponentBrain().selectCard(abInfo.owner, amount);
				for(int i= 0; i<amount; i++){
					if(dest.equals("deck")){
						abInfo.opponent.returnHandCardToDeck(abInfo.selectedCards.get(i), destTopBottom);
					}else if(dest.equals("discard")){
						abInfo.opponent.moveCardBtwTiles(abInfo.selectedCards.get(i), abInfo.opponent.getCardTiles().get(13), destTopBottom);
					}
					abInfo.selectedCards = null;
				}
			}
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

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getDestTopBottom() {
		return destTopBottom;
	}

	public void setDestTopBottom(String destTopBottom) {
		this.destTopBottom = destTopBottom;
	}

	public boolean isChoice() {
		return choice;
	}

	public void setChoice(boolean choice) {
		this.choice = choice;
	}

	public String getChoiceTarget() {
		return choiceTarget;
	}

	public void setChoiceTarget(String choiceTarget) {
		this.choiceTarget = choiceTarget;
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
		if(target.equals("opponent")){
			s+=target+" ";
		}
		s+="put ";
		if(counter != null){
			s+=counter.toString()+" ";
		}
		else{
			s+=amount+" ";
		}
		s+="cards from hand on ";
		if(!destTopBottom.equals("")){
			s+=destTopBottom+" of ";
		}
		s+=dest;
		return s;
	}
}
