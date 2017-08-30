package controller.abilities;

public class Draw extends Ability{

	private String player = "you";//you, opponent
	private int amount;
	@Override
	public boolean fire() {
		if(player.equals("you")){
			if(abInfo.owner.getDeck().size()>0){
				int n = Math.min(amount, abInfo.owner.getDeck().size());
				abInfo.owner.draw(n);
			}
		}else if(player.equals("opponent")){
			if(abInfo.opponent.getDeck().size()>0){
				int n = Math.min(amount, abInfo.owner.getDeck().size());
				abInfo.opponent.draw(n);
			}
		}else{
			return afterAttack(false);
		}
		return afterAttack(true);
	}
	
	
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String toString(){
		String s = "";
		s+=player+" draw "+amount+" cards from the deck";
		return s;
	}
}
