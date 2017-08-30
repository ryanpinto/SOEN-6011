package model;

import controller.abilities.Ability;

public class Trainer extends Card{ 
	
	public enum Category {STADIUM, SUPPORTER, ITEM};  
	private Category cat;
	private Ability ability;
	//private Observer observer;
	
	public Trainer(String name, Ability ability, Category cat){
		type = "Trainer";
		this.name = name;
		this.setAbility(ability);
		
		this.cat = cat;
	}

	public Trainer() {
		
	}

	

	public void setCat(String s) {
		if(s.equals("item")){
			cat = Category.ITEM;
		}
		else if(s.equals("supporter")){
			cat = Category.SUPPORTER;
		}
		else if(s.equals("stadium")){
			cat = Category.STADIUM;
		}
	}

	public static Category getCategory(String s) {
		if(s.equals("item")){
			return Category.ITEM;
		}
		else if(s.equals("supporter")){
			return Category.SUPPORTER;
		}
		else if(s.equals("stadium")){
			return Category.STADIUM;
		}
		return null;
	}
	
	public Category getCat(){
		return cat;
	}
	
	
	public void setAbility(Ability a) {
		ability = a;
		
	}

	public Ability getAbility() {
		return ability;
	}
}
