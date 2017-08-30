package model;

import java.util.ArrayList;
import java.util.Observable;

import controller.abilities.Ability;

public class Card extends Observable{

	protected String type;// pokemon, enery, trainer
	protected String name;
	protected ArrayList<Ability> abilities;
	protected Player owner;
	
	public String getType(){
		return type;
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	public void setType(String t) {
		type = t;
		
	}
	
	public Player getOwner(){
		return owner;
	}
	
	public void setOwner(Player p){
		owner = p;
	}
	
}
