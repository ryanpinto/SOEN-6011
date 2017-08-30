package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import controller.GameController.Status;
import controller.abilities.Ability;
import controller.abilities.Add;
import controller.abilities.Dam;
import controller.abilities.Deck;
import controller.abilities.Heal;
import controller.abilities.Redamage;
import controller.abilities.Reenergize;
import controller.abilities.Search;
import controller.abilities.Swap;
import model.Card;
import model.CardTile;
import model.Energy;
import model.Player;
import model.Player.Role;
import model.Pokemon;
import model.Pokemon.Stage;
import model.Trainer;
import model.Trainer.Category;
import view.CardLabel;
import view.CardLabel.Place;
import view.GameBoard;

public class GameController {
	
	private CardTile selectedTile;
	//private CardTile selectedCard = null;
	public enum Status {NO_PLAY, YOUR_FIRST_TURN, OP_FIRST_TURN, YOUR_TURN, OP_TURN, RETREAT, YOU_WIN, YOU_LOSE, YOU_WIN_PRIZE, OP_WIN_PRIZE, SELECT_CARD, NO_WINNER};
	private Status status;
	private Player you;
	private Player opponent;
	private OpponentBrain opponentBrain;
	private int turnCount = 0;
	private GameBoard board = null;
	private int counter = 0;
	private Timer timer;
	private boolean need = true;
	private boolean opNeed = true;
	private boolean energyUse = false;
	private boolean supporterUse = false;
	private Player currentPlayer;
	private Ability currentAb = null;
	private ArrayList<Ability> registeredAb = new ArrayList<Ability>();
	private static GameController controller = new GameController();
	
	private GameController(){
		selectedTile = null;
		status = Status.NO_PLAY;
		you = new Player(Role.YOU);
		opponent = new Player(Role.OPPONENT);
		opponentBrain = new OpponentBrain(this);

	}
	
	public void parseDeck(){
		DeckParser p = new DeckParser();
		you.setDeck(p.getDeck(Role.YOU));
		opponent.setDeck(p.getDeck(Role.OPPONENT));
	}
	
	public static GameController getController(){
		if(controller == null){
			controller = new GameController();
		}
		return controller;
	}
	
	
	public void handleMouseClicked(MouseEvent e, CardLabel clickedLabel){

		// you can only play if it is your turn
		if(status == Status.YOUR_TURN || status == Status.YOUR_FIRST_TURN){
			CardTile clickedTile = you.getCardTile(clickedLabel.getPlace(), clickedLabel.getSeqNo());
			if(clickedTile == null)
				clickedTile = opponent.getCardTile(clickedLabel.getPlace(), clickedLabel.getSeqNo());
			// if you clicked on one of your card labels and not on your opponent card labels
			if(clickedTile.getPlace() == Place.MY_ACTIVE || clickedTile.getPlace() == Place.MY_BENCH || clickedTile.getPlace() == Place.MY_DECK || 
					clickedTile.getPlace() == Place.MY_DISCARD || clickedTile.getPlace() == Place.MY_HAND || clickedTile.getPlace() == Place.MY_PRIZE){
				
				// if no card was selected yet
				if(selectedTile == null){
					selectTile(clickedTile);
				}
				else{ // if you have already selected a card and this click is after that
					// if you clicked again on the selected tile you will unselect it
					if(clickedTile.getPlace() == selectedTile.getPlace() && clickedTile.getSeqNo() == selectedTile.getSeqNo()){
						unSelectTile();
					}
					else{ // if you clicked on different tile than the selected one
						moveCard(clickedTile);
					}
				}
			}
		}else if(status == Status.YOU_WIN_PRIZE){
			if(clickedLabel.getPlace() == Place.MY_PRIZE && you.getCardTile(Place.MY_PRIZE, clickedLabel.getSeqNo()).getCard() != null){
				you.addCardToHand(you.getPrize(), you.getCardTile(Place.MY_PRIZE, clickedLabel.getSeqNo()).getCard());
				you.getCardTile(clickedLabel.getPlace(), clickedLabel.getSeqNo()).setCard(null);
				if(selectedTile != null){
					unSelectTile();
				}
				//board.updateInfo("");
				setStatus(Status.OP_TURN);
			}
		}else if(status == Status.OP_WIN_PRIZE){
			if(clickedLabel.getPlace() == Place.MY_BENCH && you.getCardTile(Place.MY_BENCH, clickedLabel.getSeqNo()).getCard() != null){
				you.moveCardBtwTiles(you.getCardTile(Place.MY_BENCH, clickedLabel.getSeqNo()), you.getCardTile(Place.MY_ACTIVE, 1), "");
				setStatus(Status.YOUR_TURN);
			}
		}else if(status == Status.SELECT_CARD){
			CardTile clickedTile = you.getCardTile(clickedLabel.getPlace(), clickedLabel.getSeqNo());
			if(clickedTile == null){
				clickedTile = opponent.getCardTile(clickedLabel.getPlace(), clickedLabel.getSeqNo());
			}
			if(currentAb instanceof Dam){
				System.out.println(((Dam) currentAb).getChooseFrom());
				if(((Dam) currentAb).getChooseFrom().equals("opponent") || ((Dam) currentAb).getTarget().equals("opponent")){
					if(clickedTile.getCard() != null && (clickedTile.getPlace() == Place.OP_BENCH || 
							(clickedTile.getPlace() == Place.OP_ACTIVE && clickedTile.getSeqNo() == 1))){
						currentAb.getAbInfo().selectedCard = clickedTile;
						cardSelected();
					}
				}else if(((Dam) currentAb).getChooseFrom().equals("opponent-bench")){
					
					if(clickedTile.getCard() != null && (clickedTile.getPlace() == Place.OP_BENCH)){
						currentAb.getAbInfo().selectedCard = clickedTile;
						cardSelected();
					}
				}
			}else if(currentAb instanceof Deck){
				if(currentAb.getAbInfo().selectedCards.size()<((Deck)currentAb).getAmount()){
					if(clickedTile.getCard() != null && clickedTile.getPlace() == Place.MY_HAND){
						if(clickedTile.isSelected()){
							clickedTile.setSelected(false);
							currentAb.getAbInfo().selectedCards.remove(clickedTile);
						}else{
							clickedTile.setSelected(true);
							currentAb.getAbInfo().selectedCards.add(clickedTile);
							if(currentAb.getAbInfo().selectedCards.size()==((Deck)currentAb).getAmount()){
								cardSelected();
							}
						}
					}
				}
			}else if(currentAb instanceof Heal){
				if(((Heal) currentAb).getChooseFrom().equals("your")){
					if(clickedTile.getCard() != null && (clickedTile.getPlace() == Place.MY_BENCH || 
							(clickedTile.getPlace() == Place.MY_ACTIVE && clickedTile.getSeqNo() == 1))){
						if(((Pokemon)clickedTile.getCard()).getHP() < ((Pokemon)clickedTile.getCard()).getTotalHP()){
							currentAb.getAbInfo().selectedCard = clickedTile;
							cardSelected();
						}
					}
				}	
			}else if(currentAb instanceof Redamage){
				if(((Redamage)currentAb).getSource().equals("opponent")){
					if(currentAb.getAbInfo().selectedCard == null){
						if(clickedTile.getCard() != null && (clickedTile.getPlace() == Place.OP_BENCH || 
								(clickedTile.getPlace() == Place.OP_ACTIVE && clickedTile.getSeqNo() == 1))){
							currentAb.getAbInfo().selectedCard = clickedTile;
							cardSelected();
						}
					}
				}else if(((Redamage)currentAb).getDest().equals("opponent")){
					if(currentAb.getAbInfo().selectedCard2 == null){
						if(clickedTile.getCard() != null && (clickedTile.getPlace() == Place.OP_BENCH || 
								(clickedTile.getPlace() == Place.OP_ACTIVE && clickedTile.getSeqNo() == 1))){
							currentAb.getAbInfo().selectedCard2 = clickedTile;
							cardSelected();
						}
					}
				}
			}else if(currentAb instanceof Reenergize){
				if(((Reenergize)currentAb).getChooseSourceFrom().equals("your") && currentAb.getAbInfo().selectedCard == null){
					if(currentAb.getAbInfo().selectedCard == null){
						if(clickedTile.getCard() != null && (clickedTile.getPlace() == Place.MY_BENCH || 
								(clickedTile.getPlace() == Place.MY_ACTIVE && clickedTile.getSeqNo() == 1))){
							currentAb.getAbInfo().selectedCard = clickedTile;
							cardSelected();
						}
					}
				}else if(((Reenergize)currentAb).getChooseDestFrom().equals("your")){
					if(currentAb.getAbInfo().selectedCard2 == null){
						if(clickedTile.getCard() != null && (clickedTile.getPlace() == Place.MY_BENCH || 
								(clickedTile.getPlace() == Place.MY_ACTIVE && clickedTile.getSeqNo() == 1))){
							currentAb.getAbInfo().selectedCard2 = clickedTile;
							cardSelected();
						}
					}
				}
			}else if(currentAb instanceof Search){
				if(((Search)currentAb).getChoiceTarget().equals("your-pokemon-basic")){
					if(clickedTile.getCard() != null && (clickedTile.getPlace() == Place.MY_BENCH || 
							(clickedTile.getPlace() == Place.MY_ACTIVE && clickedTile.getSeqNo() == 1))){
						if(((Pokemon)clickedTile.getCard()).getStage() == Stage.BASIC){
							currentAb.getAbInfo().selectedCard = clickedTile;
							cardSelected();	
						}
					}
				}
			}else if(currentAb instanceof Swap){
					if(clickedTile.getCard() != null && clickedTile.getPlace() == Place.MY_BENCH){
							currentAb.getAbInfo().selectedCard = clickedTile;
							cardSelected();	
					}
				
			}
		}else if(status == Status.RETREAT){
			if(clickedLabel.getPlace() == Place.MY_BENCH && you.getCardTile(Place.MY_BENCH, clickedLabel.getSeqNo()).getCard() != null){
				Card p = you.getActive1();
				you.moveCardBtwTiles(you.getCardTile(Place.MY_BENCH, clickedLabel.getSeqNo()), you.getCardTile(Place.MY_ACTIVE, 1), "");
				you.getCardTile(Place.MY_BENCH, clickedLabel.getSeqNo()).setCard(p);
				you.getBench().add(p);
				you.removeRetreatCost((Pokemon)p);
				board.setNotification("", false);
				setStatus(Status.YOUR_TURN);
			}
		}
	}
	
	
	public void selectTile(CardTile ct){
		// to select a tile it should contain a card
		if(ct.getCard() != null){ 
			/*For Viewing Selected Card information*/
//			int HP;
//			ArrayList<Card> attached;
//			String cards_att = "";
//			if (ct.getCard() instanceof Pokemon){
//				HP = ((Pokemon)ct.getCard()).getHP();
//				attached = ((Pokemon)ct.getCard()).getAttachedCards(); 
//				for (int l=0; l< attached.size(); l++){
//					cards_att +=  l+1 + ": " + attached.get(l).getName() + "<br />";
//				}
//			} else {
//				HP = 0;
//				attached = new ArrayList<Card>();
//				cards_att = "NA";
//			}
//			board.updateInfo("<html><p>" + "Card location: "+ ct.getPlace() + "<br />" 
//								+ "Seq No: "+ ct.getSeqNo() + "<br />" 
//								+ "Card Name: "+ ct.getCard().getName() + "<br />"
//								+ "Card Type: "+ ct.getCard().getType() + "<br />"
//								+ "Attached cards: " + "<br />" + cards_att + "<br />" 
//								+ "HP: "+ HP + "<br />"
//								+ "</p></html>");
			/*EO For Viewing Selected Card information*/
			
			// we can in the normal case select cards from the hand or the bench or the active
			// later we can add more cases as needed but this is for normal play
			if(ct.getPlace() == Place.MY_ACTIVE || ct.getPlace() == Place.MY_BENCH || ct.getPlace() == Place.MY_HAND){
				selectedTile = ct;
				ct.setSelected(true);
				displayCardInfo(ct);
			}
		}
	}
	
	public void unSelectTile(){
		if(selectedTile != null){
			selectedTile.setSelected(false);
			selectedTile = null;
		}
		
	}
	
	public void moveCard(CardTile dest){
		
		if(status == Status.YOUR_FIRST_TURN){
			if(selectedTile.getPlace() == Place.MY_HAND && ((dest.getPlace() == Place.MY_ACTIVE && dest.getSeqNo() == 1) || dest.getPlace() == Place.MY_BENCH) ){ //&& dest.getSeqNo() == 1
				if(selectedTile.getCard() instanceof Pokemon && ((Pokemon)selectedTile.getCard()).getStage() == Stage.BASIC){
					dragCard(you, selectedTile, dest);
					//setTurnCount(1);
//					if(turnCount == 1)
//						setStatus(Status.OP_FIRST_TURN);
//					else if(turnCount == 2)
//						setStatus(Status.OP_FIRST_TURN); //OP_TURN
				}
			}
		}
		else if(status == Status.YOUR_TURN){
			
			if(selectedTile.getPlace() == Place.MY_HAND && dest.getPlace() == Place.MY_ACTIVE && dest.getSeqNo() == 1){ // attach energy to active or evolve active
				if(selectedTile.getCard() instanceof Energy && !energyUse){ // attach energy to pokemon one per turn
					((Pokemon)dest.getCard()).attachEnergy((Energy)selectedTile.getCard());
					you.removeHandTile(selectedTile);
					energyUse = true;
					unSelectTile();
				}
				else if(selectedTile.getCard() instanceof Pokemon && ((Pokemon)selectedTile.getCard()).getStage() == Stage.STAGE_1 && 
						((Pokemon)dest.getCard()).getStage() == Stage.BASIC){// evolve active pokemon
					Pokemon p = evolvePokemon((Pokemon)dest.getCard(), (Pokemon)selectedTile.getCard());
					dest.setCard(p);
					you.removeHandTile(selectedTile);
					selectedTile = null;
				}else if(selectedTile.getCard() instanceof Trainer && ((Trainer)selectedTile.getCard()).getCat() == Category.ITEM){ // use trainer cards
					if(((Trainer)selectedTile.getCard()).getAbility() instanceof Add){
						((Pokemon)dest.getCard()).getAttachedItem().add((Trainer)selectedTile.getCard());
						((Add)((Trainer)selectedTile.getCard()).getAbility()).setPokemon((Pokemon)dest.getCard());
						you.removeHandTile(selectedTile);
						unSelectTile();
					}
				}
			}
			else if(selectedTile.getPlace() == Place.MY_HAND && dest.getPlace() == Place.MY_ACTIVE && dest.getSeqNo() == 2){ // use trainer cards
				if(selectedTile.getCard() != null && selectedTile.getCard() instanceof Trainer){
					if(!(((Trainer)selectedTile.getCard()).getAbility() instanceof Add)){
						you.moveCardBtwTiles(selectedTile, you.getCardTiles().get(1), "");
						((Trainer)you.getCardTiles().get(1).getCard()).getAbility().fire();
						you.moveCardBtwTiles(you.getCardTiles().get(1), you.getCardTiles().get(13), "");//to discard
						unSelectTile();
					}
				}
			}
			else if(selectedTile.getPlace() == Place.MY_HAND && dest.getPlace() == Place.MY_BENCH){ //bench pokemon or add energy to benched pokemon or evolve pokemon
				if(dest.getCard() != null && selectedTile.getCard() instanceof Energy && !energyUse){ // attach energy
					((Pokemon)dest.getCard()).attachEnergy((Energy)selectedTile.getCard());
					you.removeHandTile(selectedTile);
					energyUse = true;
					unSelectTile();
				}
				else if(dest.getCard() == null && selectedTile.getCard() instanceof Pokemon && ((Pokemon)selectedTile.getCard()).getStage() == Stage.BASIC){//bench pokemon
					dragCard(you, selectedTile, dest);
				}
				else if(dest.getCard() != null && selectedTile.getCard() instanceof Pokemon && ((Pokemon)selectedTile.getCard()).getStage() == Stage.STAGE_1 && 
						((Pokemon)dest.getCard()).getStage() == Stage.BASIC){// evolve pokemon
					Pokemon p = evolvePokemon((Pokemon)dest.getCard(), (Pokemon)selectedTile.getCard());
					dest.setCard(p);
					you.getBench().remove(p.getBasicPo());
					you.getBench().add(p);
					you.removeHandTile(selectedTile);
					unSelectTile();
				}else if(selectedTile.getCard() instanceof Trainer && ((Trainer)selectedTile.getCard()).getCat() == Category.ITEM){ // use trainer cards
					if(((Trainer)selectedTile.getCard()).getAbility() instanceof Add){
						((Pokemon)dest.getCard()).getAttachedItem().add((Trainer)selectedTile.getCard());
						((Add)((Trainer)selectedTile.getCard()).getAbility()).setPokemon((Pokemon)dest.getCard());
						you.removeHandTile(selectedTile);
						unSelectTile();
					}
				}
			}
			
		}

	}

	public Pokemon evolvePokemon(Pokemon basic, Pokemon stage1){
		stage1.setBasicPo(basic);
		stage1.setAttachedE(basic.getAttachedE());
		stage1.setAttachedItem(basic.getAttachedItem());
		if(basic.getHP() < basic.getTotalHP()){//damage exist
			stage1.decreaseHP(basic.getTotalHP()-basic.getHP());
		}
		
		return stage1;
	}
	
	public void startGame(){
		
		board.setNotification("Draw 7 hand cards and prize cards...", true);
		ActionListener notificationPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	if(counter == 0){
					drawCards(7);
					setPrizeCards(6);
					board.setNotification("We are going to check if players have mulligans...", false);
					counter++;
				}
				else if(counter == 1){
					need = !(you.handContainBasic());
					opNeed = !(opponent.handContainBasic());
					if(need && opNeed){
						board.setNotification("Both Players have Mulligan...Both will shuffle and redraw", false);
						you.mulligans();//shuffle and draw 7 again
						opponent.mulligans();
					}
					else if(need && !opNeed){
						board.setNotification("You have mulligan but your opponent doesn't. Opponent will draw a card", false);
						you.mulligans();
						opponent.draw(1);
					}
					else if(!need && opNeed){
						board.setNotification("You Don't have mulligan but your opponent does. You will draw a card", false);
						opponent.mulligans();
						you.draw(1);
					}
					if(!need && !opNeed){
						board.setNotification("None of players have mulligan, Ready to start...", false);
						counter++;
					}
					
				}
				else if(counter == 2){
					board.setNotification("Each player should select active and bench their Pokemon !", true);
					counter++;
				}
				else if(counter == 3){
					setStatus(Status.OP_FIRST_TURN);
					//chooseFirstPlayer();
					counter++;
					timer.stop();
				}
            }
        };
        timer = new Timer(3000 ,notificationPerformer); //2000 for  timer initially
        timer.start();
	
	}
	
	public void chooseFirstPlayer(){
	
		board.setNotification("Flip the coin to select first player..", true);
		int flip = flipTheCoin();
		if(flip == 0){
			setStatus(Status.YOUR_TURN);//YOUR_FIRST_TURN
		}
		else if(flip == 1){
			setStatus(Status.OP_TURN);//OP_FIRST_TURN
		}
			
		
	}
	
	public void drawCards(int no){
		you.draw(no);
		opponent.draw(no);
	}
	
	public void setPrizeCards(int no){
		you.setPrizeCards(no);
		opponent.setPrizeCards(no);
	}
	
	
	
	public void setNotification(String message){
		board.setNotification(message, true);
	}
	
	public int flipTheCoin(){
		int x = (int)(Math.random()+0.5);
		return x;
	}

	public void dragCard(Player cardOwner, CardTile from, CardTile to){
		cardOwner.moveCardBtwTiles( from, to, "");
		unSelectTile();
				
	}
	
	public void handleMouseEntered(MouseEvent e, CardLabel enteredLabel){
		if(selectedTile == null){
			CardTile ct = null;
			if(enteredLabel.getPlace() == Place.MY_ACTIVE || enteredLabel.getPlace() == Place.MY_BENCH || 
					enteredLabel.getPlace() == Place.MY_HAND){
				ct = you.getCardTile(enteredLabel.getPlace(), enteredLabel.getSeqNo());
			}else if(enteredLabel.getPlace() == Place.OP_ACTIVE || enteredLabel.getPlace() == Place.OP_BENCH){
				ct = opponent.getCardTile(enteredLabel.getPlace(), enteredLabel.getSeqNo());
			}
			if(ct != null && ct.getCard() != null){
				displayCardInfo(ct);
			}
			
		}
	}
	

	public void displayCardInfo(CardTile ct){
		board.updateCardInfo(ct);
	}
	
	public void handleMouseExited(MouseEvent e, CardLabel exitedLabel){
		if(selectedTile == null){
			displayCardInfo(null);
		}
	}


	public CardTile getSelectedCard() {
		return selectedTile;
	}


	public void setSelectedCard(CardTile selectedCard) {
		this.selectedTile = selectedCard;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		
		this.status = status;
		if(status == Status.YOUR_FIRST_TURN){
			turnCount++;
			currentPlayer = you;
			board.setAll(you.getDeck().size()+"", you.getDiscard().size()+"", opponent.getDeck().size()+"", opponent.getDiscard().size()+"", status+"");
		}
		else if(status == Status.YOUR_TURN){
			System.out.println("change to your turn");
			if(you.draw(1)){
				board.setAll(you.getDeck().size()+"", you.getDiscard().size()+"", opponent.getDeck().size()+"", opponent.getDiscard().size()+"", status+"");
				energyUse = false;
				turnCount++;
				unSelectTile();
				//board.setNotification("YOUR TURN... GO AHEAD", false);
				currentPlayer = you;
			}else{
				setStatus(Status.YOU_LOSE);
			}
		}
		
		else if(status == Status.OP_FIRST_TURN){
			turnCount++;
			board.setAll(you.getDeck().size()+"", you.getDiscard().size()+"", opponent.getDeck().size()+"", opponent.getDiscard().size()+"", status+"");
			currentPlayer = opponent;
			opponentBrain.nexMovement();
			//board.setNotification("OPPONENT FIRST TURN...", true);
		
		} else if (	status == Status.OP_TURN) {
			//board.setNotification("OPPONENT TURN...", false);
			System.out.println("op turn");
			turnCount++;
			if(opponent.draw(1)){
				board.setAll(you.getDeck().size()+"", you.getDiscard().size()+"", opponent.getDeck().size()+"", opponent.getDiscard().size()+"", status+"");
				unSelectTile();
				energyUse = false;
				currentPlayer = opponent;
				opponentBrain.nexMovement();
			}else{
				board.setAll(you.getDeck().size()+"", you.getDiscard().size()+"", opponent.getDeck().size()+"", opponent.getDiscard().size()+"", status+"");
				setStatus(Status.YOU_WIN);
			}
			
			
		}else if(status == Status.YOU_WIN_PRIZE){
			System.out.println("you win prize1");
			//board.setNotification("Select a prize card!", true);
			//board.updateInfo("Select a prize card!");
			opponentBrain.nexMovement();//choose bench pokemon
			board.setAll(you.getDeck().size()+"", you.getDiscard().size()+"", opponent.getDeck().size()+"", opponent.getDiscard().size()+"", status+"");
		}else if(status == Status.YOU_WIN){
			//board.setNotification("GONGRATULATION...YOU WON THE GAME", false);
			board.setAll(you.getDeck().size()+"", you.getDiscard().size()+"", opponent.getDeck().size()+"", opponent.getDiscard().size()+"", status+"");
			//board.updateInfo("<Html>GONGRATULATION.<br>..YOU WON THE GAME :)<br> we should get FULL mark..");
		}else if(status == Status.OP_WIN_PRIZE){
			//board.setNotification("Opponent select a prize card!", false);
			opponentBrain.nexMovement();//choose prize card
			board.setAll(you.getDeck().size()+"", you.getDiscard().size()+"", opponent.getDeck().size()+"", opponent.getDiscard().size()+"", status+"");
			//setStatus(Status.YOUR_TURN);
			
		}else if(status == Status.YOU_LOSE){
			System.out.println("you lose");
			//board.setNotification("GAME OVER", false);
			board.setAll(you.getDeck().size()+"", you.getDiscard().size()+"", opponent.getDeck().size()+"", opponent.getDiscard().size()+"", status+"");
			//board.updateInfo("GAME OVER :(");
		}	
		
		
	}


	public Player getOpponent() {
		return opponent;
	}


	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}


	public Player getYou() {
		return you;
	}


	public void setYou(Player you) {
		this.you = you;
	}


	public OpponentBrain getOpponentBrain() {
		return opponentBrain;
	}


	public void setOpponentBrain(OpponentBrain opponentBrain) {
		this.opponentBrain = opponentBrain;
	}


	public int getTurnCount() {
		return turnCount;
	}


	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}


	public GameBoard getBoard() {
		return board;
	}


	public void setBoard(GameBoard board) {
		this.board = board;
	}
	
//	public void attack(){ 
//		
//		if (turnCount == 3){
//			
//			board.setNotification("First player cant attack.. ", true);
//		} else if (status == Status.YOUR_TURN){ 
//			
//			if (opponent.getActive().size() > 0) {// if there is active pokemon
//				if (((Pokemon)you.getActive().get(0)).getAttachedCards().size() > 0) { //if there is energy attached
//			
//			//Check if active is set on both benches (you, opponent)
//			if (((Pokemon)opponent.getActive().get(0)).getHP() > 0){
//				
//				((Pokemon)opponent.getActive().get(0)).decreaseHP(10);
//				
//				board.setNotification("Damage Caused", true);
//				
//				if (((Pokemon)opponent.getActive().get(0)).getHP() <= 0) {
//					
//					opponent.moveCardBtwTiles(opponent.getCardTiles().get(0), opponent.getCardTiles().get(13));
//					board.setNotification("Opponent Pokemon dead", true);
//					if(you.getPrize().size() == 1 || opponent.getBench().size() == 0){//opponent does not have bench or we finished all prize
//						setStatus(Status.YOU_WIN);
//					}
//					else if(opponent.getBench().size()>0){//opponent has bench pokemon
//						setStatus(Status.YOU_WIN_PRIZE);
//						
//					}
//					
//				}else{
//					
//					setStatus(Status.OP_TURN);
//				}
//				
//				//System.out.println("Decreased HP 10");
//			} 
//			
//		} else {//Notify that there is no energy attached
//			board.setNotification("No Energy attached to your active pokemon!", true);
//			//setStatus(Status.OP_TURN);
//		}
//	} //Active Pokemon found on opponent bench
//	else {
//		board.setNotification("No Active pokemon detected for opponent!", true);
//	}
//		}// Attack
//	}
	
//	public void opponentAttack(){ 
//		
//		if (turnCount == 3){
//			System.out.println("turn 3 opponent");
//			board.setNotification("First player cant attack.. ", false);
//			setStatus(Status.YOUR_TURN);
//		} else if (status == Status.OP_TURN){ 
//			if (you.getActive().size() > 0) {// if there is active pokemon
//		       System.out.println("if there is active pokemon");
//		       if (((Pokemon)opponent.getActive().get(0)).getAttachedCards().size() > 0) { //if there is energy attached
//			//Check if active is set on both benches (you, opponent)
//			if (((Pokemon)you.getActive().get(0)).getHP() > 0){ //Damage
//				System.out.println("damage opponent");
//				((Pokemon)you.getActive().get(0)).decreaseHP(10);
//				board.setNotification("Damage Caused", true);
//				if (((Pokemon)you.getActive().get(0)).getHP() <= 0) { //Die
//					System.out.println("die opponent");
//					you.moveCardBtwTiles(you.getCardTiles().get(0), you.getCardTiles().get(13));//send to discard
//					board.setNotification("Opponent Pokemon dead", true);
//					if(opponent.getPrize().size() == 1 || you.getBench().size() == 0){//you does not have bench or we finished all prize
//						System.out.println("you loooose");
//						setStatus(Status.YOU_LOSE);
//					}
//					else if(opponent.getBench().size()>0){//you has bench pokemon
//						setStatus(Status.OP_WIN_PRIZE);
//						
//					}
//				// die	
//				}else{
//					System.out.println("your turn after attack");
//					setStatus(Status.YOUR_TURN);	
//				}
//				
//				//System.out.println("Decreased HP 10");
//			}//damage 
//			
//		} else {//Notify that there is no energy attached
//			System.out.println("no energy opponent");
//			board.setNotification("Not able to attack!", true);
//			setStatus(Status.YOUR_TURN);
//		}
//	} //Active Pokemon found on opponent bench
//	else {
//		board.setNotification("No Active pokemon detected for opponent!", true);
//		setStatus(Status.YOUR_TURN);
//	}
//		}// Attack
//	}	
	
	
	
	public void doneWithTurn(){
		if (status == Status.YOUR_FIRST_TURN){
			if (you.getCardTile(Place.MY_ACTIVE, 1).getCard() == null) {
				board.setNotification("Please select active pokemon!", true);
			} else {
				chooseFirstPlayer();
			}
		} else if (status == Status.YOUR_TURN) {
			setStatus(Status.OP_TURN);
		}
	}

	public void setEnergyUse(boolean b){
		energyUse = b;
	}
	
	public boolean getEnergyUse(){
		return energyUse;
	}
	
	public void registerAbility(Ability a){
		registeredAb.add(a);
	}
	
	public void unregisterAbility(Ability a){
		registeredAb.remove(a);
	}
	
	public void notifyAbs(){
		for(Ability a: registeredAb){
			a.update();
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
		notifyAbs();
	}

	public boolean afterAttack() {	
		if(you.getCardTiles().get(0).getCard() == null && opponent.getCardTiles().get(0).getCard() == null){
			int ywin = 0;
			int opwin = 0;
			if(you.getPrize().size() == 1){
				ywin++;
			}
			if(opponent.getPrize().size() == 1){
				opwin++;
			}
			if(you.getBench().size()>0){
				ywin++;
			}
			if(opponent.getBench().size()>0){
				opwin++;
			}
			if(you.getDeck().size()>0){
				ywin++;
			}
			if(opponent.getDeck().size()>0){
				opwin++;
			}
			if(ywin>opwin){
				setStatus(Status.YOU_WIN);
			}else if(ywin<opwin){
				setStatus(Status.YOU_LOSE);
			}else{
				setStatus(Status.NO_WINNER);
			}
			
			
		}else if(you.getCardTiles().get(0).getCard() == null){
			if(opponent.getPrize().size()==1){
				setStatus(Status.YOU_LOSE);
			}else if(you.getBench().size()==0){
				setStatus(Status.YOU_LOSE);
			}else if(you.getBench().size()>0){
				setStatus(Status.OP_WIN_PRIZE);
			}
			
			
		}else if(opponent.getCardTiles().get(0).getCard() == null){
			if(you.getPrize().size()==1){
				setStatus(Status.YOU_WIN);
			}else if(opponent.getBench().size()==0){
				setStatus(Status.YOU_WIN);
			}else if(opponent.getBench().size()>0){
				setStatus(Status.YOU_WIN_PRIZE);
			}
		}else{
			if(currentPlayer.getRole() == Role.YOU){
				setStatus(Status.OP_TURN);
			}else{
				setStatus(Status.YOUR_TURN);
			}
		}
		return true;
	}
	
	public void selectCard(Ability a){
		currentAb = a;
		setStatus(Status.SELECT_CARD);
	}
	
//	public void cardSelected(CardTile card){
//		if(status == Status.CARD_SELECTED){
//			if(currentPlayer == you){
//				setStatus(Status.YOUR_TURN);
//			}
//			currentAb.getAbInfo().selectedCard = card;
//			currentAb.fire();
//			afterAttack();
//		}
//	}
	
	public void cardSelected( ){
		System.out.println("card selected");
		board.setNotification("", false);
		if(currentPlayer.getRole() == Role.YOU){
			status = Status.YOUR_TURN;
		}else{
			status = Status.OP_TURN;
		}
		if(currentAb.getAbInfo().parent != null){
			currentAb.getAbInfo().parent.fire();
		}else{
			currentAb.fire();
		}
	}

	public void fireA1() {
		((Pokemon)you.getActive1()).getAbilities().get(0).fire();
	}

	public void fireA2() {
		((Pokemon)you.getActive1()).getAbilities().get(1).fire();
	}

	public void retreat() {
		board.setNotification("Select a bench pokemon to swap with the active", false);
		setStatus(Status.RETREAT);
	}



}
