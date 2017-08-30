package controller.abilities;

import model.AbilityInfo;
import model.Energy;
import model.Pokemon;

public class Counter {

	private String target;//your-bench, your-active, opponent-active, your-hand, opponent-hand, last-source
	private String countFor = "";//energy, damage
	private String cat = "";//psychic, water, ...
	private int times = 1;
	
	
	public int count(AbilityInfo abInfo){
		if(target.equals("your-bench")){
			int n = abInfo.owner.getBench().size();
			return n*times;
		}else if(target.equals("your-hand")){
			int n = abInfo.owner.getHand().size();
			return n*times;
		}else if(target.equals("opponent-hand")){
			int n = abInfo.opponent.getHand().size();
			return n*times;
		}else if(target.equals("your-active")){
			int counter = 0;
			Pokemon p = (Pokemon)abInfo.owner.getActive1();
			if(countFor.equals("energy")){
				if(!cat.equals("")){
					counter = p.getAttachedE().size();
				}else{
					for(int i =0 ; i<p.getAttachedE().size(); i++){
						if(p.getAttachedE().get(i).getCat() == Energy.getCategory(cat)){
							counter++;
						}
					}
					return counter*times;
				}
			}else if(countFor.equals("damage")){
				return (p.getTotalHP()-p.getHP())*times;
			}
		}else if(target.equals("opponent-active")){
			int counter = 0;
			Pokemon p = (Pokemon)abInfo.opponent.getActive1();
			if(countFor.equals("energy")){
				if(!cat.equals("")){
					counter = p.getAttachedE().size();
				}else{
					for(int i =0 ; i<p.getAttachedE().size(); i++){
						if(p.getAttachedE().get(i).getCat() == Energy.getCategory(cat)){
							counter++;
						}
					}
					return counter*times;
				}
			}else if(countFor.equals("damage")){
				return (p.getTotalHP()-p.getHP())*times;
			}
		}else if(target.equals("last-source")){
			int counter = 0;
			Pokemon p = abInfo.lastSource;
			if(p != null){
				if(countFor.equals("energy")){
					if(!cat.equals("")){
						counter = p.getAttachedE().size();
					}else{
						for(int i =0 ; i<p.getAttachedE().size(); i++){
							if(p.getAttachedE().get(i).getCat() == Energy.getCategory(cat)){
								counter++;
							}
						}
						return counter*times;
					}
				}else if(countFor.equals("damage")){
					return (p.getTotalHP()-p.getHP())*times;
				}
			}
			
		}
		return 0;
	}
	
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getCountFor() {
		return countFor;
	}
	public void setCountFor(String countFor) {
		this.countFor = countFor;
	}
	public String getCat() {
		return cat;
	}
	public void setCat(String cat) {
		this.cat = cat;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	
	public String toString(){
		String s = "";
		if(times != 1){
			s+=times+" times the count of ";
		}
		if(!countFor.equals("")){
			s+=countFor;
			if(!cat.equals("")){
				s+=" cat: "+cat;
			}
			s+=" of ";
		}
		
		s+=target;
		return s;
	}
}
