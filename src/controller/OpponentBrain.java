package controller;

import java.util.ArrayList;

import controller.GameController.Status;
import controller.abilities.Ability;
import model.Card;
import model.CardTile;
import model.Energy;
import model.Energy.Category;
import model.Player;
import model.Pokemon;
import model.Pokemon.Stage;
import view.CardLabel.Place;

public class OpponentBrain {
	private GameController controller;
	private Player pc;//computer
	private Player opponent;//human
	//private Player opponent;
	
	public OpponentBrain(GameController c){
		controller = c;
		pc = controller.getOpponent();
		opponent = controller.getYou();
	}
	
	
	public void nexMovement(){
		if(controller.getStatus() == GameController.Status.OP_FIRST_TURN){
			
			for (int i=0; i<pc.getHandTiles().size(); i++){
				if(pc.getHandTiles().get(i).getCard() instanceof Pokemon && 
						((Pokemon)pc.getHandTiles().get(i).getCard()).getStage() == Stage.BASIC){
					
						 CardTile from = pc.getCardTile(Place.OP_HAND, i+1);
						 CardTile dest = null;
						 if(controller.getOpponent().getActive1() == null){
							 dest = pc.getCardTile(Place.OP_ACTIVE, 1);
						 }else if(pc.getBench().size()<5){
							 dest = pc.getCardTile(Place.OP_BENCH, pc.getBench().size()+1);
						 }
						 
						if(dest != null){
							 pc.moveCardBtwTiles(from, dest, ""); 
						}else{
							break;
						}
				}
			}
			controller.setStatus(Status.YOUR_FIRST_TURN);	
		
		}else if(controller.getStatus() == GameController.Status.OP_TURN){
			ArrayList<CardTile> hand = new ArrayList<CardTile>();
			for(int i = 0; i<pc.getHandTiles().size(); i++){
				hand.add(pc.getHandTiles().get(i));
			}
			
			//bench pokemons or evolve
			for(int i=0; i<hand.size(); i++){
				Card c = hand.get(i).getCard();
				if(c instanceof Pokemon){
					if(((Pokemon)c).getStage() == Stage.BASIC){
						if(pc.getBench().size()<5){
							for(int j = 2; j<7; j++){
								if(pc.getCardTiles().get(j).getCard() == null){
									pc.moveCardBtwTiles(hand.get(i), pc.getCardTiles().get(j), "");
									break;
								}
							}
						}else{//no space
							continue;
						}
					}else{//stage 1
						if(((Pokemon)pc.getActive1()).getStage() == Stage.BASIC){
							if(pc.getActive1().getName().equals(((Pokemon)c).getBasic())){
								Pokemon p = controller.evolvePokemon((Pokemon)pc.getActive1(), (Pokemon)c);
								pc.setActive1(p);
								pc.getCardTiles().get(0).setCard(p);
								pc.removeHandTile(hand.get(i));	
							}
						}else if(pc.getBench().size()>0){
							for(int j = 2; j<7; j++){
								if(pc.getCardTiles().get(j).getCard() != null && 
										((Pokemon)pc.getCardTiles().get(j).getCard()).getStage() == Stage.BASIC){
									if(pc.getCardTiles().get(j).getCard().getName().equals(((Pokemon)c).getBasic())){
										Pokemon p = controller.evolvePokemon((Pokemon)pc.getCardTiles().get(j).getCard(), (Pokemon)c);
										pc.getCardTiles().get(j).setCard(p);
										pc.getBench().remove(p.getBasicPo());
										pc.getBench().add(p);
										pc.removeHandTile(hand.get(i));
										break;
									}
									
								}
							}
						}
					}
				}
			}
			
			hand = new ArrayList<CardTile>();
			for(int i = 0; i<pc.getHandTiles().size(); i++){
				hand.add(pc.getHandTiles().get(i));
			}
			
			//attach energy to pokemons
			if(!controller.getEnergyUse()){
				for(int i=0; i<hand.size(); i++){
					Card c = hand.get(i).getCard();
					if(c instanceof Energy && !controller.getEnergyUse()){//attach energy......
						if(needE(((Energy)c).getCat(), (Pokemon)pc.getActive1())){
							((Pokemon)pc.getActive1()).attachEnergy((Energy)c);
							pc.removeHandTile(hand.get(i));
							controller.setSelectedCard(null);
							controller.setEnergyUse(true);
							break;
						}else if(pc.getBench().size()>0){
							for(int j=0; j<pc.getBench().size(); j++){
								CardTile ct = pc.getCardTiles().get(j+2);//first bench tile is 2
								if(ct.getCard() != null){
									if(needE(((Energy)c).getCat(), (Pokemon)ct.getCard())){
										((Pokemon)ct.getCard()).attachEnergy((Energy)c);
										pc.removeHandTile(hand.get(i));
										controller.setEnergyUse(true);
										break;
									}
								}
							}
							if(controller.getEnergyUse()){
								break;
							}
						}
					}else{
						continue;
					}	
				}
			}
			
		Pokemon p = (Pokemon)pc.getActive1();
		int index = -1;
		for(int i=p.getAbilities().size()-1; i>=0; i--){
			if(canAttack(p.getAbilities().get(i), p.getAttachedE())){
				index = i;
				break;
			}
		}
		if(index != -1 && controller.getTurnCount()>3){
			System.out.println("op attack");
			p.getAbilities().get(index).fire();
		}else{
			System.out.println("op brain before set your turn");
			controller.setStatus(Status.YOUR_TURN);
		}
			
			
		}else if(controller.getStatus() == GameController.Status.YOU_WIN_PRIZE){//choose bench pokemon
			for(int i=0; i<5; i++){
				if(pc.getCardTile(Place.OP_BENCH, i+1).getCard() != null){
					pc.moveCardBtwTiles(pc.getCardTile(Place.OP_BENCH, i+1), pc.getCardTile(Place.OP_ACTIVE, 1), "");
					break;
				}
			}
			
		}else if(controller.getStatus() == GameController.Status.OP_WIN_PRIZE){
			for(int i=0;i<6;i++){
				if(controller.getOpponent().getCardTile(Place.OP_PRIZE, i+1).getCard() != null){
					pc.addCardToHand(pc.getPrize(), pc.getCardTile(Place.OP_PRIZE, i+1).getCard());
					pc.getCardTile(Place.OP_PRIZE, i+1).setCard(null);
					break;
				}
			}
			
		}
	}
	
	public boolean canAttack(Ability a, ArrayList<Energy> attached){
		ArrayList<int[][]> e = a.getNeededEnergy();
		ArrayList<Energy> t = new ArrayList<Energy>();
		int index = -1;//index of colorless
		for(int j=0; j<attached.size(); j++){
			t.add(attached.get(j));
		}
		
		for(int j=0; j<e.size(); j++){
			int count = e.get(j)[0][1];
			Category cat = Energy.getCategory(e.get(j)[0][0]);
			if(cat == Category.COLORLESS){
				index = j;
				continue;
			}
			for(int i=0; i<attached.size(); i++){
				 if(attached.get(i).getCat() == cat){
					count--;
					t.remove(attached.get(i));
					if(count == 0){
						break;
					}
				}
			}
			if(count > 0){
				return false;//cannot attack
			}
		}
		if(index != -1){//colorless exists
			int count = e.get(index)[0][1];
			if(t.size()>=count){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public boolean needE(Category cat, Pokemon p){
		//check retreat
		int count = p.getRetreatCost();
		for(int i =0; i<p.getAttachedE().size(); i++){
			if(p.getAttachedE().get(i).getCat() == p.getRetreatEType()){
				count--;
				if(count == 0){
					break;
				}
			}
		}
		if(count > 0){
			return true;//need energy of type cat
		}
		
		//check for abilities
		for(int i=0; i<p.getAbilities().size(); i++){
			ArrayList<int[][]> e = p.getAbilities().get(i).getNeededEnergy();
			for(int j=0; j<e.size(); j++){
				if(Energy.getCategory(e.get(j)[0][0]) == cat){
					count = e.get(j)[0][1];
					for(int k =0; k<p.getAttachedE().size(); k++){
						if(p.getAttachedE().get(k).getCat() == p.getRetreatEType()){
							count--;
							if(count == 0){
								break;
							}
						}
					}
					if(count > 0){
						return true;//need energy of type cat
					}
					break;
				}	
			}
		}
		return false;//no need
	}

	public GameController getController() {
		return controller;
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}


	public CardTile selectCard(Player opponent, String chooseFrom) {//opponent(active,bench), opponent-bench, your(active,bench), your-bench
		if(chooseFrom.equals("opponent") || chooseFrom.equals("your")){
			for(int i=2; i<7; i++){
				int r = (int)(Math.random()*5)+2;
				if(opponent.getCardTiles().get(r).getCard() != null){//bench
					return opponent.getCardTiles().get(r);
				}
			}
			
			if(opponent.getCardTiles().get(0).getCard() != null){//active
				return opponent.getCardTiles().get(0);
			}
			
			for(int i=2; i<7; i++){
				if(opponent.getCardTiles().get(i).getCard() != null){//bench
					return opponent.getCardTiles().get(i);
				}
			}
				
		}else if(chooseFrom.equals("opponent-bench") || chooseFrom.equals("your-bench")){
			for(int i=2; i<7; i++){
				int r = (int)(Math.random()*5)+2;
				if(opponent.getCardTiles().get(r).getCard() != null){//bench
					return opponent.getCardTiles().get(r);
				}
			}
			
			for(int i=2; i<7; i++){
				if(opponent.getCardTiles().get(i).getCard() != null){//bench
					return opponent.getCardTiles().get(i);
				}
			}
		}
		return null;
	}


	public ArrayList<CardTile> selectCard(Player owner, int amount) {
		ArrayList<CardTile> selected = new ArrayList<CardTile>();
		int c = Math.min(amount, owner.getHand().size());
		for(int i=0; i<c; i++){
			selected.add(owner.getHandTiles().get(0));
		}
		return selected;
	}
	
}
