package controller.abilities;

import java.util.ArrayList;

import controller.GameController;
import model.AbilityInfo;

public abstract class Ability {
	private ArrayList<int[][]> neededEnergy = new ArrayList<int[][]>();
	private String name;
	protected AbilityInfo abInfo;
	
	
	public abstract boolean fire();


	public boolean afterAttack(boolean done){
		System.out.println("after attack: "+name+"- "+abInfo.owner.getRole());
		if(abInfo.parent != null && !(this instanceof CompositeAbility)){//part of composite
			return done;
		}else{//alone
			if(abInfo.belongTo.equals("pokemon")){
				GameController.getController().afterAttack();
			}
			return done;
		}
	}
	
	public void reset(){
		abInfo.reset();
	}
	
	public boolean select(){
		if(abInfo.parent != null){//part of composite
			abInfo.askForSelect = true;
			return false;
		}else{//alone
			GameController.getController().selectCard(this);
			return false;
		}
	}
	
	
	
	public ArrayList<int[][]> getNeededEnergy() {
		return neededEnergy;
	}


	public void setNeededEnergy(ArrayList<int[][]> neededEnergy) {
		this.neededEnergy = neededEnergy;
	}
	
	public void addEnergy(int[][] e){
		neededEnergy.add(e);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public AbilityInfo getAbInfo() {
		return abInfo;
	}


	public void setAbInfo(AbilityInfo abInfo) {
		this.abInfo = abInfo;
	}
	
	public void update(){
		
	}

}
