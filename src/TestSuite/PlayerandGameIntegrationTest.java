package TestSuite;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import controller.GameController;
import controller.GameController.Status;
import model.Card;
import model.CardTile;
import model.Player;
import model.CardTile.Display;
import model.Player.Role;
import view.GameBoard;
import view.CardLabel.Place;
import org.junit.experimental.categories.Category;

public class PlayerandGameIntegrationTest {
	
	
	
	
	
//This test checks for the AI functionality and verifies if it is makig moves after taking the control from main 
	//player or not,
	//Special Instructions: TO run this test we need to comment notifyobserver in model.player.notifyObserver()
	//comment notifyobserver() in add card to hand.
	@Test
	public final void testopponentBrain() {
		GameController controller = new GameController();
		controller.setBoard(new GameBoard(controller));
		controller.parseDeck();
		controller.getOpponent().draw(7);
		System.out.println("lsjdbv");
		int a=controller.getOpponent().getHandTiles().size();
		Card c=controller.getOpponent().getActive1();
		controller.setStatus(Status.OP_FIRST_TURN);	
		Card d=controller.getOpponent().getActive1();
		assertEquals("Status",d.getName(),"Pikachu");
		}

		
	}