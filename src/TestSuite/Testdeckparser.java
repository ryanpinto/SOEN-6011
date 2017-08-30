package TestSuite;

import static org.junit.Assert.*;
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
import model.Card;
import model.CardTile.Display;
import model.Energy;
import model.Player.Role;
import model.Pokemon;
import model.Trainer;
import model.Trainer.Category;
import view.GameBoard;
import view.CardLabel.Place;

import org.junit.Before;
import org.junit.Test;

public class Testdeckparser {
	@Test
	public void TestTrainercard() {
		
		DeckParser parser = new DeckParser();
		ArrayList<Card> d = parser.getDeck(Role.YOU);
		Trainer t = (Trainer)d.get(0);
		assertEquals(t.getName(), "Shauna");
		assertEquals(t.getType(), "trainer");
		String a=t.getCat().toString();
		assertEquals(a, "SUPPORTER");
		String b=t.getAbility().toString();
		assertEquals(b, "put your-hand cards from hand on deck, you shuffle deck, you draw 5 cards from the deck");
		
		//this test is for testing whether trainer card is parsed properly or not
	}
	
	@Test
	public void TestEnergycard() {
		
		DeckParser parser = new DeckParser();
		ArrayList<Card> d = parser.getDeck(Role.OPPONENT);
		Energy e = (Energy)d.get(0);
		assertEquals(e.getName(), "Lightning");
		assertEquals(e.getType(), "energy");
		
		
		//this test is for testing whether energy card is parsed properly or not
	}
	@Test
	public void TestPokemoncard() {
		
		DeckParser parser = new DeckParser();
		ArrayList<Card> d = parser.getDeck(Role.YOU);
		Pokemon p = (Pokemon)d.get(4);
		assertEquals(p.getName(), "Hitmonlee");
		assertEquals(p.getType(), "pokemon");
		assertEquals(p.getStage().toString(), "BASIC");
		assertEquals(p.getHP(), 90);
		assertEquals(p.getAbilities().toString(), "[Apply 30 damage to one of opponent-bench pokemons, Apply 30 damage to opponent-active pokemon]");
		assertEquals(p.getRetreatCost(), 1);
		assertEquals(p.getRetreatEType().toString(), "COLORLESS");
		
		//this test is for testing whether pokemon cards are parsed properly or not
	}
	
	

	@Test
	public void testdeck() {
		DeckParser parser = new DeckParser();
		ArrayList<Card> d = parser.getDeck(Role.YOU);
		
		assertThat(d.get(0).getName(), anyOf(is("Shauna"),is("Lightning")));
		assertThat(d.get(1).getName(), anyOf(is("PokÃ©mon Fan Club"),is("Pikachu")));
		assertThat(d.get(2).getName(), anyOf(is("Switch"),is("Electrike")));
		assertThat(d.get(3).getName(), anyOf(is("Psychic"),is("Glameow")));
		assertThat(d.get(4).getName(), anyOf(is("Hitmonlee"),is("Lightning")));
		assertThat(d.get(5).getName(), anyOf(is("Slowpoke"),is("Raichu")));
		assertThat(d.get(6).getName(), anyOf(is("Machop"),is("Shellder")));
		assertThat(d.get(7).getName(), anyOf(is("Meowth"),is("Seaking")));
		assertThat(d.get(8).getName(), anyOf(is("Fight"),is("Seaking")));
		assertThat(d.get(9).getName(), anyOf(is("Fight"),is("Purugly")));
		assertThat(d.get(10).getName(), anyOf(is("Doduo"),is("Lightning")));
		assertThat(d.get(11).getName(), anyOf(is("Dodrio"),is("Water")));
		assertThat(d.get(12).getName(), anyOf(is("Machop"),is("Lightning")));
		assertThat(d.get(13).getName(), anyOf(is("Machop"),is("Lightning")));
		assertThat(d.get(14).getName(), anyOf(is("Hitmonchan"),is("Goldeen")));
		assertThat(d.get(15).getName(), anyOf(is("Machoke"),is("Tierno")));
		assertThat(d.get(16).getName(), anyOf(is("Meowstic"),is("Frogadier")));
		assertThat(d.get(17).getName(), anyOf(is("Meowth"),is("Cloyster")));
		assertThat(d.get(18).getName(), anyOf(is("Machoke"),is("Lightning")));
		assertThat(d.get(19).getName(), anyOf(is("Espurr"),is("Froakie")));
		assertThat(d.get(20).getName(), anyOf(is("Jynx"),is("Pikachu")));
		assertThat(d.get(21).getName(), anyOf(is("Fight"),is("Froakie")));
		assertThat(d.get(22).getName(), anyOf(is("Psychic"),is("Suicune")));
		assertThat(d.get(23).getName(), anyOf(is("Fight"),is("Goldeen")));
		assertThat(d.get(24).getName(), anyOf(is("Fight"),is("Water")));
		assertThat(d.get(25).getName(), anyOf(is("Red Card"),is("Water")));
		assertThat(d.get(26).getName(), anyOf(is("Psychic"),is("Swanna")));
		assertThat(d.get(27).getName(), anyOf(is("Wally"),is("Lightning")));

		 
		 d = parser.getDeck(Role.OPPONENT);
		 assertThat(d.get(0).getName(), anyOf(is("Shauna"),is("Lightning")));
			assertThat(d.get(1).getName(), anyOf(is("Pokémon Fan Club"),is("Pikachu")));
			assertThat(d.get(2).getName(), anyOf(is("Switch"),is("Electrike")));
			assertThat(d.get(3).getName(), anyOf(is("Psychic"),is("Glameow")));
			assertThat(d.get(4).getName(), anyOf(is("Hitmonlee"),is("Lightning")));
			assertThat(d.get(5).getName(), anyOf(is("Slowpoke"),is("Raichu")));
			assertThat(d.get(6).getName(), anyOf(is("Machop"),is("Shellder")));
			assertThat(d.get(7).getName(), anyOf(is("Meowth"),is("Seaking")));
			assertThat(d.get(8).getName(), anyOf(is("Fight"),is("Seaking")));
			assertThat(d.get(9).getName(), anyOf(is("Fight"),is("Purugly")));
			assertThat(d.get(10).getName(), anyOf(is("Doduo"),is("Lightning")));
			assertThat(d.get(11).getName(), anyOf(is("Dodrio"),is("Water")));
			assertThat(d.get(12).getName(), anyOf(is("Machop"),is("Lightning")));
			assertThat(d.get(13).getName(), anyOf(is("Machop"),is("Lightning")));
			assertThat(d.get(14).getName(), anyOf(is("Hitmonchan"),is("Goldeen")));
			assertThat(d.get(15).getName(), anyOf(is("Machoke"),is("Tierno")));
			assertThat(d.get(16).getName(), anyOf(is("Meowstic"),is("Frogadier")));
			assertThat(d.get(17).getName(), anyOf(is("Meowth"),is("Cloyster")));
			assertThat(d.get(18).getName(), anyOf(is("Machoke"),is("Lightning")));
			assertThat(d.get(19).getName(), anyOf(is("Espurr"),is("Froakie")));
			assertThat(d.get(20).getName(), anyOf(is("Jynx"),is("Pikachu")));
			assertThat(d.get(21).getName(), anyOf(is("Fight"),is("Froakie")));
			assertThat(d.get(22).getName(), anyOf(is("Psychic"),is("Suicune")));
			assertThat(d.get(23).getName(), anyOf(is("Fight"),is("Goldeen")));
			assertThat(d.get(24).getName(), anyOf(is("Fight"),is("Water")));
			assertThat(d.get(25).getName(), anyOf(is("Red Card"),is("Water")));
			assertThat(d.get(26).getName(), anyOf(is("Psychic"),is("Swanna")));
			assertThat(d.get(27).getName(), anyOf(is("Wally"),is("Lightning")));
		}
	

	
}
