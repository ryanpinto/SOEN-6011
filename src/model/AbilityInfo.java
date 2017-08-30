package model;

import java.util.ArrayList;

import controller.abilities.CompositeAbility;

public class AbilityInfo {

	public Player owner;
	public Player opponent;
	public Pokemon card = null;//pokemon has the ability in case of add
	public Pokemon lastTarget = null;
	public Pokemon lastSource = null;
	public CompositeAbility parent = null;
	public int seq = 0;//for composite
	public boolean continu = true;
	public CardTile selectedCard = null;
	public CardTile selectedCard2 = null;
	public ArrayList<CardTile> selectedCards = null;
	public boolean askForSelect = false;
	public boolean askForSearch = false;
	public String belongTo = "";
	public ArrayList<Card> searchResult = null;
	public ArrayList<Card> source = null;
	
	public void reset(){
		card = null;
		lastTarget = null;
		lastSource = null;
		seq = 0;
		continu = true;
		selectedCard = null;
		selectedCard2 = null;
		selectedCards = null;
		askForSelect = false;
		askForSearch = false;
		searchResult = null;
		source = null;
	}
	
	
}
