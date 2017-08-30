package TestSuite;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import controller.DeckParser;
import controller.GameController;
import controller.GameController.Status;
import controller.OpponentBrain;
import controller.abilities.Ability;
import model.Card;
import model.Pokemon;
import model.CardTile.Display;
import model.Energy;
import model.Player.Role;
import view.GameBoard;
import view.CardLabel.Place;



public class TestGameController {
	

	GameController controller = new GameController();
	GameBoard board = new GameBoard(controller);
 
	@Test
	 public void TestflipTheCoin() {
		int flip= controller.flipTheCoin();
		//controller.setBoard(new GameBoard(controller));
		
		
		assertThat(flip, anyOf(is(0),is(1)));
	}
	
	@Test
	 public void TestEvolvePokemon() {
		DeckParser parser = new DeckParser();
		ArrayList<Card> d = parser.getDeck(Role.YOU);
		Pokemon basic = (Pokemon)d.get(4);
		Energy e=(Energy)d.get(3);
		Energy e1=(Energy)d.get(3);
		basic.attachEnergy(e);
		basic.attachEnergy(e1);
		Pokemon stage1=(Pokemon)d.get(14);
		controller.evolvePokemon(basic, stage1);
		int flip= controller.flipTheCoin();
		assertEquals("Status",stage1.getAttachedE().size(),2);
		assertEquals("Status",stage1.getAttachedE().get(1),e1);
		assertEquals("Status",stage1.getBasicPo(),basic);
		
		
	}
	
 
	
	@Test
		public void TestSetStatusYOUR_FIRST_TURN() {
	
			controller.setBoard(new GameBoard(controller));
		 controller.setStatus(Status.YOUR_FIRST_TURN);;
		assertEquals("Status",Status.YOUR_FIRST_TURN,controller.getStatus());
			}
	
	@Test
	public void TestSetStatusNo_PLAY() {

		controller.setBoard(new GameBoard(controller));
	    controller.setStatus(Status.NO_PLAY);;
		assertEquals("Status",Status.NO_PLAY,controller.getStatus());
		}
	@Test
	public void TestSetStatusOP_FIRST_TURN() {
  
		controller.getYou().setObserver(board);
		controller.getOpponent().setObserver(board);
		controller.setBoard(board);
		controller.setBoard(new GameBoard(controller));
		controller.setStatus(Status.OP_FIRST_TURN);;
		 assertEquals("Status",Status.YOUR_FIRST_TURN,controller.getStatus());
		}
	
	
	@Test
	public void TestCanAttack() {
		DeckParser parser = new DeckParser();
		ArrayList<Card> d = parser.getDeck(Role.OPPONENT);
	
		Pokemon a =(Pokemon) d.get(1);
		System.out.println(a.getAbilities().get(1));
		Energy e=(Energy) d.get(0);
		ArrayList<Energy> attached = new ArrayList<Energy>();
		attached.add(e);
		attached.add(e);
		boolean b=controller.getOpponentBrain().canAttack(a.getAbilities().get(1),attached);
		assertEquals("Status",b,true);
		
		
		}
	
	
@Test
	public void TestSetStatusYOUR_TURN() {

		controller.setBoard(new GameBoard(controller));
	 controller.setStatus(Status.YOUR_TURN);;
		assertThat(controller.getStatus(), anyOf(is(Status.YOUR_TURN),is(Status.YOU_LOSE)));
		}
	
   @Test
	public void TestSetStatusOP_TURN(){

		controller.setBoard(new GameBoard(controller));
	    controller.setStatus(Status.OP_TURN);;
		 assertThat(controller.getStatus(), anyOf(is(Status.OP_TURN),is(Status.YOU_WIN)));
		}
	
    }