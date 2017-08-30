package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import controller.abilities.Add;
import model.CardTile.Display;
import model.Energy.Category;
import model.Pokemon.Stage;
import view.CardLabel.Place;

public class Player extends Observable{
	
	private ArrayList<Card> deck;
	private ArrayList<Card> hand;
	private ArrayList<Card> bench;
	private ArrayList<Card> discard;
	private ArrayList<Card> prize;
	private Card active1;//pokemon
	private Card active2;//item or supporter trainer
	private ArrayList<CardTile> cardTiles;//active+bench+prize+discard tiles
	private ArrayList<CardTile> handTiles;//only hand tiles
	public enum Role {YOU, OPPONENT};
	private Role role;
	private Observer observer = null;
	
	public Player(Role role){
		this.role = role;
		deck = new ArrayList<Card>();
		hand = new ArrayList<Card>();
		bench = new ArrayList<Card>();
		discard = new ArrayList<Card>();
		prize = new ArrayList<Card>();
		//active = new ArrayList<Card>();
		handTiles = new ArrayList<CardTile>();
		initTiles();
			}
	
	private void initTiles(){
		cardTiles = new ArrayList<CardTile>();
		if(role == Role.YOU){//your tiles
			for(int i=0; i<14; i++){
				if(i<2){//0 to 1
					cardTiles.add(new CardTile(Place.MY_ACTIVE, i+1, Display.FACE, null));
				}
				else if(i<7){//2 to 6
					cardTiles.add(new CardTile(Place.MY_BENCH, i-1, Display.FACE, null));
				}
				else if(i<13){//7 to 12
					cardTiles.add(new CardTile(Place.MY_PRIZE, i-6, Display.BACK, null));
				}
				else{//13
					cardTiles.add(new CardTile(Place.MY_DISCARD, 1, Display.FACE, null));
				}
			}
		}
		else if(role == Role.OPPONENT){//opponent tiles
			for(int i=0; i<14; i++){
				if(i<2){
					cardTiles.add(new CardTile(Place.OP_ACTIVE, i+1, Display.FACE, null));
				}
				else if(i<7){
					cardTiles.add(new CardTile(Place.OP_BENCH, i-1, Display.FACE, null));
				}
				else if(i<13){
					cardTiles.add(new CardTile(Place.OP_PRIZE, i-6, Display.BACK, null));
				}
				else{
					cardTiles.add(new CardTile(Place.OP_DISCARD, 1, Display.FACE, null));
				}
			}
			
		}
	}
	
	public void setRole(Role r){
		role = r;
	}
	
	public Role getRole(){
		return role;
	}
	
	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	public void setDeck(ArrayList<Card> deck) {
		this.deck = deck;
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}
	
	public ArrayList<Card> getBench() {
		return bench;
	}
	
	public void setBench(ArrayList<Card> bench) {
		this.bench = bench;
	}
	
	public ArrayList<CardTile> getCardTiles() {
		return cardTiles;
	}
	
	public void setCardTiles(ArrayList<CardTile> cardTiles) {
		this.cardTiles = cardTiles;
	}
	
	public CardTile getCardTile(Place place, int no){
		for(CardTile c : cardTiles){
			if(c.getPlace() == place && c.getSeqNo() == no){
				return c;
			}
		}
		for(CardTile c : handTiles){
			if(c.getPlace() == place && c.getSeqNo() == no){
				return c;
			}
		}
		
		return null;
	}

	public ArrayList<Card> getDiscard() {
		return discard;
	}

	public void setDiscard(ArrayList<Card> discard) {
		this.discard = discard;
	}
	
	public void addCardToHand(ArrayList<Card> source, Card c){//source can be deck, discard or prize
		hand.add(c);
		source.remove(c);
		if(source == discard){
			refreshDiscard();
		}
		if(role == Role.YOU){
			handTiles.add(new CardTile(Place.MY_HAND, handTiles.size()+1, Display.FACE, c));
		}
		else
			handTiles.add(new CardTile(Place.OP_HAND, handTiles.size()+1, Display.BACK, c));
		if(source == prize){
			
		}
		notifyObserver();
	}
	
	public void addCardToPrize(ArrayList<Card> src, Card c, int seq){// src is deck in most cases
		prize.add(c);
		src.remove(c);
		getCardTile(cardTiles.get(7).getPlace(), seq).setCard(c);// 7 because prize cards start from index 7 in cardTiles
	}
	
	public boolean draw(int no){
		if(deck.size()>=no){
			for(int i=0; i<no; i++)
				addCardToHand(deck, deck.get(0));
		}else if(deck.size()>0){
			for(int i=0; i<deck.size(); i++)
				addCardToHand(deck, deck.get(0));
		}else{
			return false;
		}
		return true;
	}
	
	public void setPrizeCards(int no){
		int x = 0;
		for(int i=0; i<6; i++){
			x = (int)(Math.random()*deck.size());
			addCardToPrize(deck, deck.get(x), i+1);
		}
	}

	public ArrayList<Card> getPrize() {
		return prize;
	}

	public void setPrize(ArrayList<Card> prize) {
		this.prize = prize;
	}

	public ArrayList<CardTile> getHandTiles() {
		return handTiles;
	}

	public void setHandTiles(ArrayList<CardTile> handTiles) {
		this.handTiles = handTiles;
	}

	public Observer getObserver() {
		return observer;
	}

	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	public void notifyObserver(){//observer is gameboard
		observer.update(this, null);
	}

	public void moveCardBtwTiles(CardTile fromTile, CardTile toTile, String place) { // move cards between tiles hand, bench, active, prize and discard
		Place from = fromTile.getPlace();
		Place to = toTile.getPlace();
		ArrayList<Card> src = null;
		ArrayList<Card> dest = null;
		
		//allocate source
		if(from == Place.MY_BENCH || from == Place.OP_BENCH){
			src = bench;
		}
		else if(from == Place.MY_DISCARD || from == Place.OP_DISCARD){
			src = discard;
		}
		else if(from == Place.MY_HAND || from == Place.OP_HAND){
			src = hand;
		}
		else if(from == Place.MY_PRIZE || from == Place.OP_PRIZE){
			src = prize;
		}
//		else if(from == Place.MY_ACTIVE || from == Place.OP_ACTIVE){
//			src = active;
//		}
		
		//allocate destination
		if(to == Place.MY_BENCH || to == Place.OP_BENCH){
			dest = bench;
		}
		else if(to == Place.MY_DISCARD || to == Place.OP_DISCARD){
			dest = discard;
		}
		else if(to == Place.MY_HAND || to == Place.OP_HAND){
			dest = hand;
		}
		else if(to == Place.MY_PRIZE || to == Place.OP_PRIZE){
			dest = prize;
		}
//		else if(to == Place.MY_ACTIVE || to == Place.OP_ACTIVE){
//			dest = active;
//		}
		
		if(src != dest){
			if(dest == discard && fromTile.getCard() instanceof Pokemon){
				movePokemonToDiscad((Pokemon)fromTile.getCard() ,place);
			}
			else if(dest == discard && fromTile.getCard() instanceof Trainer){
				moveTrainerToDiscad((Trainer)fromTile.getCard() ,place);
			}else if(dest != null){//not active
				if(place.equals("top")){
					dest.add(0, fromTile.getCard());
				}else{//bottom
					dest.add(fromTile.getCard());
				}
			}
			if(src != null)//not active
				src.remove(fromTile.getCard());
		}

		toTile.setCard(fromTile.getCard());
		if((toTile.getPlace() == Place.MY_ACTIVE || toTile.getPlace() == Place.OP_ACTIVE) && toTile.getSeqNo() == 1){
			active1 = toTile.getCard();
		}else if((toTile.getPlace() == Place.MY_ACTIVE || toTile.getPlace() == Place.OP_ACTIVE) && toTile.getSeqNo() == 2){
			active2 = toTile.getCard();
		}
		if(src != discard){
			fromTile.setCard(null);
			if((fromTile.getPlace() == Place.MY_ACTIVE || fromTile.getPlace() == Place.OP_ACTIVE) && fromTile.getSeqNo() == 1){
				active1 = fromTile.getCard();
			}else if((fromTile.getPlace() == Place.MY_ACTIVE || fromTile.getPlace() == Place.OP_ACTIVE) && fromTile.getSeqNo() == 2){
				active2 = fromTile.getCard();
			}
		}else if(src == discard && discard.size()>0)
			fromTile.setCard(discard.get(0));
		
		if(src == hand){
			removeHandTile(fromTile);
		}
		
	}
	
	private void moveTrainerToDiscad(Trainer card, String place) {
		card.getAbility().reset();
		if(place.equals("top")){
			discard.add(0, card);
		}else{//bottom
			discard.add(card);
		}
		
	}
	
	

	private void movePokemonToDiscad(Pokemon card, String place) {
		if(card.getAttachedE().size()>0){
			for(int i=0; i<card.getAttachedE().size(); i++){
				if(place.equals("top")){
					discard.add(0, card.getAttachedE().get(i));
				}else{//bottom
					discard.add(card.getAttachedE().get(i));
				}
			}
		}
		
		if(card.getAttachedItem().size()>0){
			for(int i=0; i<card.getAttachedItem().size(); i++){
				((Add)card.getAttachedItem().get(i).getAbility()).reset();
				if(place.equals("top")){
					discard.add(0, card.getAttachedE().get(i));
				}else{//bottom
					discard.add(card.getAttachedE().get(i));
				}
			}
		}
		
		if(card.getBasicPo() != null){
			card.getBasicPo().reset();
			if(place.equals("top")){
				discard.add(0, card.getBasicPo());
			}else{//bottom
				discard.add(card.getBasicPo());
			}
		}
		card.reset();
		if(place.equals("top")){
			discard.add(0, card);
		}else{//bottom
			discard.add(card);
		}
		
	}

	public void removeHandTile(CardTile ct){
		handTiles.remove(ct);
		for(int i=0; i<handTiles.size(); i++){
			handTiles.get(i).setSeqNo(i+1);
		}
		
		notifyObserver();
	}

	
	
	public boolean handContainBasic(){
		for(int i=0; i<hand.size(); i++){
			if(hand.get(i) instanceof Pokemon && ((Pokemon)hand.get(i)).getStage() == Stage.BASIC){
				return true;
			}
		}
		return false;
	}
	
	public void mulligans(){
		//return all 7 cards to deck
		int size = handTiles.size();
		for(int i=0; i<size; i++){
			returnHandCardToDeck(handTiles.get(0), "bottom"); //get(0))
		}
		shuffle();
		draw(7);
	}
	
	public void shuffle(){
		Collections.shuffle(deck);
	}
	
	public void returnHandCardToDeck(CardTile ct, String place){
		hand.remove(ct.getCard());
		System.out.println("return hand to deck "+deck.size());
		if(place.equals("top")){
			deck.add(0,ct.getCard());
		}else{
			deck.add(ct.getCard());
		}
		System.out.println("return hand to deck "+deck.size());
		
		removeHandTile(ct);
	}

	public Card getActive1() {
		return active1;
	}

	public void setActive1(Card active1) {
		this.active1 = active1;
		this.getHandTiles().get(0).setCard(active1);
	}

	public Card getActive2() {
		return active2;
	}

	public void setActive2(Card active2) {
		this.active2 = active2;
		this.getHandTiles().get(1).setCard(active2);
	}

	public void refreshDiscard() {
		if(discard.size()>0)
			cardTiles.get(13).setCard(discard.get(0));
		else
			cardTiles.get(13).setCard(null);
	}
	
	public void removeRetreatCost(Pokemon p) {
		if(p.getRetreatCost()>0){
			if(p.getRetreatEType() == Category.COLORLESS){
				for(int j=0; j<p.getRetreatCost(); j++){
					p.getAttachedE().remove(0);
				}
			}else{
				int c = 0;
				for(int j=0; j<p.getAttachedE().size(); j++){
					if(p.getAttachedE().get(j).getCat() == p.getRetreatEType()){
						c++;
						discard.add(0, (p.getAttachedE().get(j)));
						cardTiles.get(13).setCard(discard.get(0));
						if(c == p.getRetreatCost()){
							break;
						}
					}
				}
				System.out.println("E before c="+c+"/"+ p.getAttachedE().size());
				for(int j=0; j<c; j++){
					p.getAttachedE().remove((Energy)discard.get(j));
				}
				System.out.println("E after"+ p.getAttachedE().size());
			}
			
		}
		
	}
}
