package model;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import view.CardLabel.Place;

public class CardTile extends Observable{

	private Card card = null;
	private Place place;
	private int seqNo;
	public enum Display {FACE, BACK};
	private Display display;
	private Observer observerLabel = null;
	private boolean selected = false;
	
	public CardTile(Place place, int seqNo, Display display, Card c){
		
		this.place = place;
		this.seqNo = seqNo;
		this.display = display;
		card = c;
	}
	
	

	public void setCard(Card c){
		card = c;
		// check if card is instance of pokemon
		if (card instanceof Pokemon){
			((Pokemon) card).setObserverNotifier(this);
		}
		notifyObserver();
	}
	
	public Card getCard(){
		return card;
	}
	
	public void setPlace(Place p){
		place = p;
	}
	
	public Place getPlace(){
		return place;
	}
	
	public void setSeqNo(int n){
		seqNo = n;
	}
	
	public int getSeqNo(){
		return seqNo;
	}
	
	public void setDisplay(Display d){
		display = d;
		notifyObserver();
	}
	
	public Display getDisplay(){
		return display;
	}
	
	public void addObserverLabel(Observer o){
		observerLabel = o;
	}
	
	public Observer getObserverLabel(){
		return observerLabel;
	}
	
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		notifyObserver();
	}
	

	//update the drawing of the card in the cardlabel
	public void notifyObserver(){
		observerLabel.update(this, null);
	}


	
	
	
	

}
