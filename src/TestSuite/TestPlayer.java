package TestSuite;

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

public class TestPlayer {
	
	
	GameController controller = new GameController();
	GameBoard board = new GameBoard(controller);
	
	
	

@Test
	public final void testSetRole() {
		Player play=new Player(Role.YOU);
		play.setRole(Role.YOU);
		Role role=play.getRole();
		assertEquals("Status",Role.YOU,role);
		
	}

	
	@Test
	public final void testSetDeck() {
		controller.parseDeck();
		
		ArrayList<Card> deck = new ArrayList<Card>();
		Card c=controller.getOpponent().getDeck().get(1);
		deck.add(c);
		controller.getOpponent().setDeck(deck);
		assertEquals("Status",deck,controller.getOpponent().getDeck());
	}
	@Test
	public final void testSetHand() {
		controller.parseDeck();
		
		ArrayList<Card> hand = new ArrayList<Card>();
		Card c=controller.getOpponent().getDeck().get(1);
		hand.add(c);
		controller.getOpponent().setHand(hand);
		assertEquals("Status",hand,controller.getOpponent().getHand());
	}
	
	@Test
	public final void testSetDiscard() {
		controller.parseDeck();
		
		ArrayList<Card> discard = new ArrayList<Card>();
		Card c=controller.getOpponent().getDeck().get(1);
		discard.add(c);
		controller.getOpponent().setDiscard(discard);
		assertEquals("Status",discard,controller.getOpponent().getDiscard());
	}
	@Test
	public final void testSetBench() {
		controller.parseDeck();
		
		ArrayList<Card> bench= new ArrayList<Card>();
		Card c=controller.getOpponent().getDeck().get(1);
		bench.add(c);
		controller.getOpponent().setBench(bench);
		assertEquals("Status",bench,controller.getOpponent().getBench());
	}

	@Test
	public final void testSetCardTiles() {
		controller.parseDeck();
	
		ArrayList<CardTile> cardtiles = new ArrayList<CardTile>();
		CardTile c=controller.getOpponent().getCardTiles().get(1);
		cardtiles.add(c);
		controller.getOpponent().setCardTiles(cardtiles);
		assertEquals("Status",cardtiles,controller.getOpponent().getCardTiles());
		
	}


	//This test checks for the moving card between tiles and verifies if it is making the moves after taking the control from main 
		//player or not,either to the bench or the active area
		//Special Instructions: TO run this test we need to comment notifyobserver in model.player.notifyObserver()
		//comment notifyobserver() in add card to hand.

	@Test
	public final void testMoveCardBtwTiles() {
		
		controller.parseDeck();
			Card c=controller.getOpponent().getDeck().get(0);	
			controller.getOpponent().getHand().add(c);
			CardTile from = new CardTile(Place.OP_HAND,0,Display.BACK,c);
			CardTile dest = controller.getOpponent().getCardTile(Place.OP_ACTIVE, 1);
			Card finalcard=controller.getOpponent().getHand().get(0);	
			controller.getOpponent().moveCardBtwTiles(from, dest, null);	 
		 	Card car=controller.getOpponent().getActive1();
			assertEquals("Status",finalcard,car);
			}

}