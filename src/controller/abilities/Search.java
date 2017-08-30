package controller.abilities;

import java.util.ArrayList;

import controller.GameController;
import model.Card;
import model.Energy;
import model.Pokemon;
import model.Pokemon.Stage;
import view.SearchCards;

public class Search extends Ability{

	private String target;//your, opponent, choice
	private String source;//deck, discard
	private String filter1 = "";//top, bottom, energy, item, pokemon, evolves-from
	private String filter2 = "";//cat eg. basic, stage-1, psychic, colorless
	private int filterAmount = 0;//for top and bottom
	private int amount = 0;//no. of cards to draw
	private String choiceTarget = "";//your-pokemon-basic
	private String evolveTarget = "";//last
	
	
	@Override
	public boolean fire() {
		if(abInfo.searchResult == null){

			//source
			ArrayList<Card> sourc = null;
			if(target.equals("your")){
				if(source.equals("deck")){
					sourc = abInfo.owner.getDeck();
				}else if(source.equals("discard")){
					sourc = abInfo.owner.getDiscard();
				}
			}else if(target.equals("opponent")){
				if(source.equals("deck")){
					sourc = abInfo.opponent.getDeck();
				}else if(source.equals("discard")){
					sourc = abInfo.opponent.getDiscard();
				}
			}else if(target.equals("choice")){
				if(choiceTarget.contains("your")){
					if(source.equals("deck")){
						sourc = abInfo.owner.getDeck();
					}else if(source.equals("discard")){
						sourc = abInfo.owner.getDiscard();
					}
				}else if(choiceTarget.contains("opponent")){
					if(source.equals("deck")){
						sourc = abInfo.opponent.getDeck();
					}else if(source.equals("discard")){
						sourc = abInfo.opponent.getDiscard();
					}
				}
			} 
			
			abInfo.source = sourc;
			System.out.println(abInfo.source);
			//filters
			if(target.equals("choice")){
				if(choiceTarget.equals("your-pokemon-basic")){
					Pokemon p = null;
					if(abInfo.selectedCard == null){
						GameController.getController().getBoard().setNotification("Please select one of "+choiceTarget+" to filter search", false);
						return select();
					}else{
						p = (Pokemon)abInfo.selectedCard.getCard();
						abInfo.lastTarget = p;
						
					}
				}
			}
			ArrayList<Card> filteredSource = new ArrayList<Card>();
			if(filter1.equals("top")){
				for(int i = 0; i<filterAmount; i++){
					filteredSource.add(sourc.get(i));
				}
			}else if(filter1.equals("bottom")){
				for(int i = 0; i<filterAmount; i++){
					filteredSource.add(sourc.get(sourc.size()-1-i));
				}
			}else if(filter1.equals("energy")){
				for(int i = 0; i<sourc.size(); i++){
					if(sourc.get(i) instanceof Energy){
						if(filter2.equals("")){
							filteredSource.add(sourc.get(i));
						}else if(((Energy)sourc.get(i)).getCat() == Energy.getCategory(filter2)){
							filteredSource.add(sourc.get(i));
						}
					}
				}
			}else if(filter1.equals("item") || filter1.equals("trainer")){
				
			}else if(filter1.equals("pokemon")){
				for(int i = 0; i<sourc.size(); i++){
					if(sourc.get(i) instanceof Pokemon){
						if(filter2.equals("")){
							filteredSource.add(sourc.get(i));
						}else if(((Pokemon)sourc.get(i)).getStage() == Pokemon.getStage(filter2)){
							filteredSource.add(sourc.get(i));
						}else if(((Pokemon)sourc.get(i)).getCat() == Energy.getCategory(filter2)){
							filteredSource.add(sourc.get(i));
						}
					}
				}
				
			}else if(filter1.equals("evolves-from")){
				Pokemon target = null;
				if(evolveTarget.equals("last")){
					target = abInfo.lastTarget;
				}
				if(target != null){
					for(int i = 0; i<sourc.size(); i++){
						if(sourc.get(i) instanceof Pokemon && ((Pokemon)sourc.get(i)).getStage() == Stage.STAGE_1){
							if(((Pokemon)sourc.get(i)).getBasic().equals(target.getName())){
								filteredSource.add(sourc.get(i));
							}
						}
					}
				}
			}

			return search(abInfo.source, filteredSource, amount);
			
		}
		
		if(abInfo.searchResult.size()>0){
			if(filter1.equals("evolves-from") && evolveTarget.equals("last")){
				Pokemon p = GameController.getController().evolvePokemon(abInfo.lastTarget, (Pokemon)abInfo.searchResult.get(0));
				abInfo.selectedCard.setCard(p);
				abInfo.source.remove(p);
				abInfo.owner.refreshDiscard();
				abInfo.opponent.refreshDiscard();
			}else{
				//if(source.equals("deck"))
				System.out.println(abInfo.source);
				for(int i = 0; i<abInfo.searchResult.size(); i++){
					abInfo.owner.addCardToHand(abInfo.source, abInfo.searchResult.get(i));
				}
			}
			
			
		}
		System.out.println("null source");
		abInfo.selectedCard = null;
		abInfo.searchResult = null;
		abInfo.source = null;
		return afterAttack(true);
	}


	public boolean search(ArrayList<Card> s, ArrayList<Card> a, int amount){
		if(abInfo.parent != null){//part of composite
			abInfo.askForSearch = true;
			new SearchCards(s, abInfo.parent, a, amount);
			return false;
		}else{//alone
			new SearchCards(s, this, a, amount);
			return false;
		}
	}
	
	
	public String getTarget() {
		return target;
	}


	public void setTarget(String target) {
		this.target = target;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public String getFilter1() {
		return filter1;
	}


	public void setFilter1(String filter1) {
		this.filter1 = filter1;
	}


	public String getFilter2() {
		return filter2;
	}


	public void setFilter2(String filter2) {
		this.filter2 = filter2;
	}


	public int getFilterAmount() {
		return filterAmount;
	}


	public void setFilterAmount(int filterAmount) {
		this.filterAmount = filterAmount;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public String getChoiceTarget() {
		return choiceTarget;
	}


	public void setChoiceTarget(String choiceTarget) {
		this.choiceTarget = choiceTarget;
	}


	public String getEvolveTarget() {
		return evolveTarget;
	}


	public void setEvolveTarget(String evolveTarget) {
		this.evolveTarget = evolveTarget;
	}

	
	public String toString(){
		String s = "";
		s+="search ";
		if(filter1.equals("top") || filter1.equals("bottom")){
			s+="the "+filterAmount+" "+filter1+" cards of ";
			
		}
		
		if(!target.equals("choice")){
			s+=target+" ";
		}
		s+=source+" and choose "+amount;
		if(target.equals("choice")){
			s+=" card that evolve from one of "+choiceTarget+" and evolve it";
		}
		else{
			if(!filter1.equals("top") && !filter1.equals("bottom"))
			s+=" "+filter1;
			s+=" card";
			if(!filter2.equals("")){
				s+=" cat:"+filter2;
			}	
			s+=" and put in hand";
		}	
			
		
		//s+=" and put in hand";
		return s;
	}
}
