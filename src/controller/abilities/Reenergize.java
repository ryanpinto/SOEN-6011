package controller.abilities;



import controller.GameController;
import model.Card;
import model.Energy;
import model.Player.Role;
import model.Pokemon;

public class Reenergize extends Ability{

	private String source;//choice
	private String dest;//choice
	private String chooseSourceFrom;//your
	private String chooseDestFrom;//your
	
	@Override
	public boolean fire() {
		Pokemon s = null;
		Pokemon d = null;
		if(source.equals("choice")){
			if(chooseSourceFrom.equals("your")){
				if(abInfo.owner.getRole() == Role.YOU){
					if(abInfo.selectedCard == null){
						GameController.getController().getBoard().setNotification("Please select one of your pokemons to take energy from", false);
						return select();
					}else{
						s = (Pokemon)abInfo.selectedCard.getCard();
						abInfo.lastSource = s;
						//abInfo.selectedCard = null;
					}
				}else{//opponent
					abInfo.selectedCard = GameController.getController().getOpponentBrain().selectCard(abInfo.owner, chooseSourceFrom);
					s = (Pokemon)abInfo.selectedCard.getCard();
					abInfo.lastSource = s;
					//abInfo.selectedCard = null;
				}
			}else{//not your
				return afterAttack(false);
			}
		}else{//not choice
			return afterAttack(false);
		}
		
		if(dest.equals("choice")){
			if(chooseDestFrom.equals("your")){
				if(abInfo.owner.getRole() == Role.YOU){
					if(abInfo.selectedCard2 == null){
						GameController.getController().getBoard().setNotification("Please select one of your pokemons to reenergize", false);
						return select();
					}else{
						d = (Pokemon)abInfo.selectedCard2.getCard();
						//abInfo.selectedCard2 = null;
					}
				}else{//opponent
					abInfo.selectedCard2 = GameController.getController().getOpponentBrain().selectCard(abInfo.owner, chooseDestFrom);
					d = (Pokemon)abInfo.selectedCard2.getCard();
					//abInfo.selectedCard2 = null;
				}
			
			}else{//not your
				return afterAttack(false);	
			}
		}else{//not choice
			return afterAttack(false);
		}
		Energy c = s.deenergize();
		d.attachEnergy(c);
		abInfo.selectedCard = null;
		abInfo.selectedCard2 = null;
		return afterAttack(true);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String sourceTarget) {
		this.source = sourceTarget;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getChooseSourceFrom() {
		return chooseSourceFrom;
	}

	public void setChooseSourceFrom(String chooseSourceFrom) {
		this.chooseSourceFrom = chooseSourceFrom;
	}

	public String getChooseDestFrom() {
		return chooseDestFrom;
	}

	public void setChooseDestFrom(String chooseDestFrom) {
		this.chooseDestFrom = chooseDestFrom;
	}

	public String toString(){
		String s = "";
		s+="move one energy from one of "+chooseSourceFrom+" pokemons to one of "+chooseDestFrom+" pokemons";
		return s;
	}
}
