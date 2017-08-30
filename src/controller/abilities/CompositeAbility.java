package controller.abilities;

import java.util.ArrayList;

import controller.GameController;

public class CompositeAbility extends Ability{
	
	private ArrayList<Ability> abilities;

	@Override
	public boolean fire() {
		System.out.println(abInfo.seq);
		for(int i = abInfo.seq; i<abilities.size(); i++){
			
				boolean done = abilities.get(i).fire();
				if(!done){
					if(abInfo.askForSelect){
						abInfo.askForSelect = false;
						GameController.getController().selectCard(abilities.get(i));
						return false;
					}else if(abInfo.askForSearch){
						abInfo.askForSearch = false;
						return false;
					}
				}
				abInfo.seq++;
			
		}
		if(abInfo.seq == abilities.size()){// done with all abilities
			return afterAttack(true);
		}
		return false;
		
	}

	

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}
	
	public void addAbility(Ability a){
		if(abilities == null){
			abilities = new ArrayList<Ability>();
		}
		abilities.add(a);
		a.setAbInfo(getAbInfo());
	}
	
	public String toString(){
		String s = "";
		int n = 0;
		int dam = 0;
		int start = -1;
		for(int i = 0; i<abilities.size(); i++){
			if(abilities.get(i) instanceof Cond && ((Cond)abilities.get(i)).getCondition().equals("flip") &&
					((Cond)abilities.get(i)).getYes() instanceof Dam){
				if(start == -1){
					start = i;
				}
				n++;
				dam = ((Dam)((Cond)abilities.get(i)).getYes()).getAmount();
			}
		}
		
		for(int i = 0; i<abilities.size(); i++){
			if(i != start){
				s+=abilities.get(i).toString();
				if(i != abilities.size()-1){
					s+=", ";
				}
			}
			else{
				s+="flip the coin "+n+" times, apply "+dam+" times no. of heads damage in addition";
				i+=n-1;
				if(i != abilities.size()-1){
					s+=", ";
				}
				
			}
		}
		return s;
	}

}
