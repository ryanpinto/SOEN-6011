package controller.abilities;



import controller.GameController;
import model.Card;
import model.CardTile;
import model.Player.Role;

public class Swap extends Ability{

	private String source;//your-active
	private String dest;//choice
	private String chooseFrom = "";//your-bench
	
	
	@Override
	public boolean fire() {
		if(abInfo.owner.getBench().size() == 0){
			return afterAttack(false);
		}else{//we have bench bokemons
			if(abInfo.owner.getRole() == Role.OPPONENT){
				CardTile bench = GameController.getController().getOpponentBrain().selectCard(abInfo.owner, chooseFrom);
				Card active = abInfo.owner.getActive1();
				abInfo.owner.moveCardBtwTiles(bench, abInfo.owner.getCardTiles().get(0), "");//move bench to active
				bench.setCard(active);
				abInfo.owner.getBench().add(active);
			}else{//you
				if(abInfo.selectedCard == null){
					GameController.getController().getBoard().setNotification("Please select one of your bench pokemons to swap with the active", false);
					return select();
				}else{
					Card active = abInfo.owner.getActive1();
					abInfo.owner.moveCardBtwTiles(abInfo.selectedCard, abInfo.owner.getCardTiles().get(0), "");//move bench to active
					abInfo.selectedCard.setCard(active);
					abInfo.owner.getBench().add(active);
					abInfo.selectedCard = null;
				}
			}
		}
		return afterAttack(true);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getChooseFrom() {
		return chooseFrom;
	}

	public void setChooseFrom(String chooseFrom) {
		this.chooseFrom = chooseFrom;
	}

	public String toString(){
		String s = "";
		s+="swap "+source+" with one of "+chooseFrom+" pokemons";
		return s;
	}
}
