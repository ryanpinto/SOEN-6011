package controller.abilities;

import javax.swing.JOptionPane;

import controller.GameController;
import model.AbilityInfo;
import model.Pokemon;

public class Cond extends Ability{

	private String condition;//flip, ability, count, choice, healed
	private Ability yes;
	private Ability no = null;
	private String healedTarget = "";//your-active
	private Ability condAbility = null;
	private Counter counter = null;
	private int compareTo = 0;
	private String comparisonRule = "";// >, <, =, >=, <=, !=
	
	
	@Override
	public boolean fire() {
		
		if(condition.equals("flip")){
			int x = GameController.getController().flipTheCoin();
			if(x == 1){//head
				return yes.fire();
			}else if(x == 0){//tail
				if(no != null){
					return no.fire();
				}else{
					return true;
				}
			}
			
			
			
		}else if(condition.equals("ability")){
			if(condAbility.fire()){
				return yes.fire();
			}else{
				if(no != null){
					return no.fire();
				}else{
					return false;	
				}
			}
			
			
			
		}else if(condition.equals("count")){
			int x = counter.count(abInfo);
			
			if(comparisonRule.equals(">")){
				if(x > compareTo){
					return yes.fire();
				}else{
					if(no != null){
						return no.fire();
					}
					return true;
				}
				
			}else if(comparisonRule.equals(">=")){
				if(x >= compareTo){
					return yes.fire();
				}else{
					if(no != null){
						return no.fire();
					}
					return true;
				}
				
			}else if(comparisonRule.equals("<")){
				if(x < compareTo){
					return yes.fire();
				}else{
					if(no != null){
						return no.fire();
					}
					return true;
				}
				
			}else if(comparisonRule.equals("<=")){
				if(x <= compareTo){
					return yes.fire();
				}else{
					if(no != null){
						return no.fire();
					}
					return true;
				}
				
			}else if(comparisonRule.equals("=")){
				if(x == compareTo){
					return yes.fire();
				}else{
					if(no != null){
						return no.fire();
					}
					return true;
				}
				
			}else if(comparisonRule.equals("!=")){
				if(x != compareTo){
					return yes.fire();
				}else{
					if(no != null){
						return no.fire();
					}
					return true;
				}
			}
			
			
			
		}else if(condition.equals("choice")){
			if (JOptionPane.showConfirmDialog(null, this.toString(), "Choice",
			        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			    return yes.fire();
			} else {
			    return true;
			}
			
			
			
		}else if(condition.equals("healed")){
			if(healedTarget.equals("your-active")){
				Pokemon target = (Pokemon)abInfo.owner.getActive1();
				if(target.isHealed()){
					return yes.fire();
				}else{
					if(no != null){
						return no.fire();
					}
					return true;
				}
			}
		}
		return false;
	}

	public void reset(){
		abInfo.reset();
		yes.reset();
		if(no != null)
			no.reset();
		if(condAbility != null)
			condAbility.reset();
		
	}
	
	public void setAbInfo(AbilityInfo ab){
		abInfo = ab;
		yes.setAbInfo(ab);
		if(no != null){
			no.setAbInfo(ab);
		}
		if(condAbility != null){
			condAbility.setAbInfo(ab);
		}
	}
	
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Ability getYes() {
		return yes;
	}

	public void setYes(Ability yes) {
		this.yes = yes;
		//this.yes.setAbInfo(abInfo);
	}

	public Ability getNo() {
		return no;
	}

	public void setNo(Ability no) {
		this.no = no;
		//this.no.setAbInfo(abInfo);
	}

	public String getHealedTarget() {
		return healedTarget;
	}

	public void setHealedTarget(String healedTarget) {
		this.healedTarget = healedTarget;
	}

	public Ability getCondAbility() {
		return condAbility;
	}

	public void setCondAbility(Ability condAbility) {
		this.condAbility = condAbility;
		//this.condAbility.setAbInfo(abInfo);
	}

	public Counter getCounter() {
		return counter;
	}

	public void setCounter(Counter counter) {
		this.counter = counter;
	}

	public int getCompareTo() {
		return compareTo;
	}

	public void setCompareTo(int compareTo) {
		this.compareTo = compareTo;
	}

	public String getComparisonRule() {
		return comparisonRule;
	}

	public void setComparisonRule(String comparisonRule) {
		this.comparisonRule = comparisonRule;
	}

	public String toString(){
		String s = "(";
		if(condition.equals("flip")){
			s+="Flip the coin, if head "+yes.toString();
			if(no != null){
				s+=", else "+no.toString();
			}
		}
		else if(condition.equals("ability")){
			s+=condAbility.toString()+", if you do, "+yes.toString();
		}
		else if(condition.equals("count")){
			s+="if "+counter.toString()+" "+comparisonRule+" "+compareTo+", "+yes.toString();
			if(no != null){
				s+=", else "+no.toString();
			}
		}
		else if(condition.equals("choice")){
			s+="You choose if "+yes.toString();
		}
		else if(condition.equals("healed")){
			s+="if "+healedTarget+" was healed, "+yes.toString();
		}
		s+=")";
		return s;
	}
	
}
