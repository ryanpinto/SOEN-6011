package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import controller.abilities.Ability;
import controller.abilities.Add;
import controller.abilities.Applystat;
import controller.abilities.CompositeAbility;
import controller.abilities.Cond;
import controller.abilities.Counter;
import controller.abilities.Dam;
import controller.abilities.Deck;
import controller.abilities.Deenergize;
import controller.abilities.Destat;
import controller.abilities.Draw;
import controller.abilities.Heal;
import controller.abilities.Redamage;
import controller.abilities.Reenergize;
import controller.abilities.Search;
import controller.abilities.Shuffle;
import controller.abilities.Swap;
import model.AbilityInfo;
import model.Card;
import model.Energy;
import model.Energy.Category;
import model.Player.Role;
import model.Pokemon;
import model.Pokemon.Stage;
import model.Trainer;

public class DeckParser {
	private ArrayList<String> cards;
	private ArrayList<String> abilities;
	//private ArrayList<Card> deck;
	
	public DeckParser(){
		cards = readFile("cards");
		abilities = readFile("abilities");	
	}
	
	
	
	public ArrayList<Card> getDeck(Role role){
		String fileName = "deck1";//your player
		if(role == Role.OPPONENT){
			fileName = "deck2";
		}
		ArrayList<String> deckFile = readFile(fileName);
		return preparePlayerDeck(deckFile, role);
	}
	
	
	
	public ArrayList<Card> preparePlayerDeck(ArrayList<String> a , Role role) {
		//ArrayList<Card> deck = new ArrayList<Card>();
		ArrayList<Card> ar = new ArrayList<Card>();
		try{
			for(String s: a){
				int x = Integer.parseInt(s);
				ar.add(createCard(x-1, role));
			}
		}catch(NumberFormatException e){
			System.out.println("Error while parsing integer");
			System.out.println(e.getLocalizedMessage());
			//System.out.println(e.toString());
		}
		
		
		return ar;
	}

	
	
	private ArrayList<String> readFile(String fileName){
		ArrayList<String> file = new ArrayList<String>();
		FileReader reader;
		try {
			reader = new FileReader(new File("src/parserFiles/"+fileName+".txt"));
			BufferedReader buffReader = new BufferedReader(reader);
			
			String line;
		    while ((line = buffReader.readLine()) != null) {
		    	//System.out.println(line);
		    	//if(!line.equals("#"))
		        file.add(line);
		    }
		    return file;
		    
		} catch (Exception e) {
			System.out.println("Error while reading a file");
			return null;
		}
        
	}

	
	
	public Card createCard(int i, Role role){
		//ArrayList<Card> deck = new ArrayList<Card>();
		String line;
		String[] parts;
		
			line = cards.get(i);
			//System.out.println(line);
			//Parsing Pokemon......................................................
			if(line.contains("pokemon")){
				parts = line.split(":");
				
				Pokemon p = new Pokemon();
				p.setName(parts[0]);
				p.setType(parts[1]);
				p.setStage(parts[3]);
				
				if(p.getStage() == Stage.BASIC){
					p.setPokemonCat(parts[5]);
					int hp = Integer.parseInt(parts[6]);
					p.setTotalHP(hp);
					p.setRetreatEType(parts[9]);
					int cost = Integer.parseInt(parts[10]);
					p.setRetreatCost(cost);
					//attacks parsing
					ArrayList<Ability> abilities = parseAttacks(line.substring(line.indexOf("attacks")), role);
					p.setAbilities(abilities);
				}else{
					p.setBasic(parts[4]);
					p.setPokemonCat(parts[6]);
					int hp = Integer.parseInt(parts[7]);
					p.setTotalHP(hp);
					if(parts[8].equals("retreat")){
						p.setRetreatEType(parts[10]);
						int cost = Integer.parseInt(parts[11]);
						p.setRetreatCost(cost);
					}
					//attacks parsing
					ArrayList<Ability> abilities = parseAttacks(line.substring(line.indexOf("attacks")), role);
					p.setAbilities(abilities);
				}
				return p;
			}// Parsing Energy.....................................................................
			else if(line.contains("energy")){
				parts = line.split(":");
				Energy e = new Energy();
				e.setName(parts[0]);
				e.setType(parts[1]);
				e.setCat(parts[3]);
				return e;
			}// Parsing Trainer.....................................................................
			else if(line.contains("trainer")){
				parts = line.split(":");
				Trainer t = new Trainer();
				t.setName(parts[0]);
				t.setType(parts[1]);
				t.setCat(parts[3]);
				int abilityNo = Integer.parseInt(parts[4]);
				Ability a = parseAbility(abilityNo, role);
				a.getAbInfo().belongTo = "trainer";
				t.setAbility(a);
				return t;
			}
			else{
				return new Card();//in case of #
			}
		
		//return deck;
	}

	
	
	private Ability parseAbility(int abilityNo, Role role) {
		String line = abilities.get(abilityNo-1);
		String line2="";
		boolean open = false;
		for(int i = 0; i<line.length() ; i++){
			if(line.charAt(i) == '('){
				line2+=line.charAt(i);
				open = true;
			}else if(line.charAt(i) == ')'){
				line2+=line.charAt(i);
				open = false;
			}else if(open){
				if(line.charAt(i) == ','){
					line2+= ";";
				}else{
					line2+=line.charAt(i);
				}
			}else if(!open){
				line2+=line.charAt(i);
			}
		}
		
		String[] all = line2.split(","); //all abilities in the line
		String[] ability;//one ability from the line
		String abilityName = "";
		ability = all[0].split(":");// first ability which contains the name
		abilityName = ability[0];
		String[]ab = new String[ability.length-1];
		for(int i = 0; i<ab.length ; i++){
			ab[i] = ability[i+1];
		}
//		System.out.println(line2);
//		System.out.println(all.length);
//		System.out.println(ab.length);
		Ability a = null;
		AbilityInfo af = new AbilityInfo();
		if(role == Role.YOU){
			af.owner= GameController.getController().getYou();
			af.opponent = GameController.getController().getOpponent();
		}else{
			af.opponent= GameController.getController().getYou();
			af.owner = GameController.getController().getOpponent();
		}
		//one ability................................................................
		if(all.length == 1){
			a = getAbility(ab);	
			a.setName(abilityName);
			af.parent = null;
			a.setAbInfo(af);
		}//more than one ability......................................
		else if(all.length > 1){
			CompositeAbility ca = new CompositeAbility();
			af.parent = ca;
			ca.setAbInfo(af);
			ca.setName(abilityName);
			ca.addAbility(getAbility(ab));
			for(int i = 1 ; i<all.length ; i++){
				ab = all[i].split(":");
				ca.addAbility(getAbility(ab));
			}
			
			a = ca;
		}
		
		return a;
	}
	
	
	
	private Ability getAbility(String[] ability ){
		if(ability[0].equals("dam")){
			return dam(ability);
		}
		else if(ability[0].equals("deck")){
			return deck(ability);
		}
		else if(ability[0].equals("cond")){
			return cond(ability);
		}
		else if(ability[0].equals("heal")){
			return heal(ability);	
		}
		else if(ability[0].equals("deenergize")){
			return deenergize(ability);
		}
		else if(ability[0].equals("reenergize")){
			return reenergize(ability);
		}
		else if(ability[0].equals("redamage")){
			return redamage(ability);
		}
		else if(ability[0].equals("swap")){
			return swap(ability);
		}
		else if(ability[0].equals("destat")){
			return destat(ability);
		}
		else if(ability[0].equals("applystat")){
			return applystat(ability);
		}
		else if(ability[0].equals("draw")){
			return draw(ability);
		}
		else if(ability[0].equals("search")){
			return search(ability);
		}
		else if(ability[0].equals("shuffle")){
			return shuffle(ability);
		}
		else if(ability[0].equals("add")){
			return add(ability);
		}else{
			return null;
		}
	}

	private Ability add(String[] parts2) {
		Add a = new Add();
		a.setTarget(parts2[2]);
		a.setTrigger(parts2[4]+"-"+parts2[5]);
		parts2[6] = parts2[6].replace("(", "");
		parts2[parts2.length-1] = parts2[parts2.length-1].replace(")", "");
		String ab = getString(parts2, 6, parts2.length-1, ":");
		a.setAbility(getAbility(ab.split(":")));
		return a;
	}

	private Ability shuffle(String[] parts2) {
		Shuffle s = new Shuffle();
		s.setTarget(parts2[2]);
		return s;
	}

	private Ability search(String[] parts) {
		Search s = new Search();
		s.setTarget(parts[2]);
		if(parts[2].equals("choice")){
			int i =3;
			String c = "";
			while(!parts[i].equals("source")){
				if(!parts[i].equals("cat")){
					c+=parts[i];
					if(!parts[i+1].equals("source")){
						c+= "-";
					}
				}
				i++;
			}
			s.setChoiceTarget(c);//your-pokemon-basic
			s.setSource(parts[i+1]);
			if(parts.length == i+3){//no filter
				int amount = Integer.parseInt(parts[i+2]);
				s.setAmount(amount);
			}
			else{//filter
				if(parts[i+3].equals("top") || parts[i+3].equals("bottom")){
					s.setFilter1(parts[i+3]);
					int n = Integer.parseInt(parts[i+4]);
					s.setFilterAmount(n);
					int amount = Integer.parseInt(parts[i+5]);
					s.setAmount(amount);
				}
				else if(parts[i+3].equals("cat")){
					s.setFilter1(parts[i+4]);
					int amount = Integer.parseInt(parts[i+5]);
					s.setAmount(amount);
				}
				else if(parts[i+3].equals("energy") || parts[i+3].equals("pokemon") || parts[i+3].equals("item")){
					s.setFilter1(parts[i+3]);
					if(parts[i+4].equals("cat")){
						s.setFilter2(parts[i+5]);
						int amount = Integer.parseInt(parts[i+6]);
						s.setAmount(amount);
					}else{
						int amount = Integer.parseInt(parts[i+4]);
						s.setAmount(amount);
					}
				}
				else if(parts[i+3].equals("evolves-from")){
					s.setFilter1(parts[i+3]);
					s.setEvolveTarget(parts[i+5]);
					int amount = Integer.parseInt(parts[i+6]);
					s.setAmount(amount);
				}
			}
		}
		else{//no choice
			s.setSource(parts[4]);
			if(parts[5].equals("filter")){
				if(parts[6].equals("top") || parts[6].equals("bottom")){
					s.setFilter1(parts[6]);
					int n = Integer.parseInt(parts[7]);
					s.setFilterAmount(n);
					int amount = Integer.parseInt(parts[8]);
					s.setAmount(amount);
				}
				else if(parts[6].equals("cat")){
					s.setFilter1(parts[7]);
					int amount = Integer.parseInt(parts[8]);
					s.setAmount(amount);
				}
				else if(parts[6].equals("energy") || parts[6].equals("pokemon") || parts[6].equals("item")){
					s.setFilter1(parts[6]);
					if(parts[7].equals("cat")){
						s.setFilter2(parts[8]);
						int amount = Integer.parseInt(parts[9]);
						s.setAmount(amount);
					}
					else{
						int amount = Integer.parseInt(parts[7]);
						s.setAmount(amount);
					}
				}
				else if(parts[6].equals("evolves-from")){
					s.setFilter1(parts[6]);
					s.setEvolveTarget(parts[8]);
					int amount = Integer.parseInt(parts[9]);
					s.setAmount(amount);
				}
			}
			else{//no filter
				int amount = Integer.parseInt(parts[5]);
				s.setAmount(amount);
			}
		}
		return s;
	}

	private Ability draw(String[] parts2) {
		Draw d = new Draw();
		if(parts2.length == 2){//draw:3
			int amount = Integer.parseInt(parts2[1]);
			d.setAmount(amount);
		}
		else if(parts2.length == 3){//draw:opponent:3
			int amount = Integer.parseInt(parts2[2]);
			d.setAmount(amount);
			d.setPlayer(parts2[1]);
		}
		return d;
	}

	private Ability applystat(String[] parts2) {
		Applystat a = new Applystat();
		a.setStatus(parts2[2]);
		a.setTarget(parts2[3]);
		return a;
	}

	private Ability destat(String[] parts2) {
		Destat d = new Destat();
		d.setTarget(parts2[2]);
		return d;
	}

	private Ability swap(String[] parts2) {
		Swap s = new Swap();
		s.setSource(parts2[2]);
		s.setDest(parts2[4]);
		if(parts2[4].equals("choice")){
			s.setChooseFrom(parts2[5]);
		}
		return s;
	}

	private Ability redamage(String[] parts2) {
		Redamage r = new Redamage();
		r.setSource(parts2[3]);
		r.setDest(parts2[5]);
		if(!parts2[6].contains("count")){//no count
			int amount = Integer.parseInt(parts2[6]);
			r.setAmount(amount);
		}
		else{//count
			String s =getString(parts2, 6, parts2.length-1, ":");
			Counter c = counter(s.split(":"));
			r.setCounter(c);
		}
		return r;
	}

	private Ability reenergize(String[] parts2) {
		Reenergize r = new Reenergize();
		r.setSource(parts2[2]);
		int next = 2;
		if(parts2[next].equals("choice")){
			next++;
			r.setChooseSourceFrom(parts2[next]);
			next+=3;// to skip word 1:target
		}
		r.setDest(parts2[next]);
		if(parts2[next].equals("choice")){
			next++;
			r.setChooseDestFrom(parts2[next]);
			
		}
		return r;
	}

	private Ability deenergize(String[] parts2) {
		Deenergize d = new Deenergize();
		d.setTarget(parts2[2]);
		if(!parts2[3].contains("count")){//no count
			int amount = Integer.parseInt(parts2[3]);
			d.setAmount(amount);
		}
		else{
			String s = getString(parts2, 3, parts2.length-1, ":");
			Counter c = counter(s.split(":"));
			d.setCounter(c);
		}
		return d;
	}

	private Ability heal(String[] parts2) {
		
		Heal h = new Heal();
		h.setTarget(parts2[2]);
		int next = 3;
		if(parts2[2].equals("choice")){
			h.setChooseFrom(parts2[next]);
			next++;
		}
		if(parts2[next].contains("count")){
			String s = getString(parts2, next, parts2.length, ":");
			Counter c = counter(s.split(":"));
			h.setCounter(c);
		}
		else{
			int amount = Integer.parseInt(parts2[next]);
			h.setAmount(amount);
		}
		return h;
	}

	private Ability cond(String[] parts) {
		Cond c = new Cond();
		c.setCondition(parts[1]);
		int yesIndex = 2;//flip, choice
		int noIndex = parts.length;
		for(int i = 0; i<parts.length; i++){
			if(parts[i].equals("else")){
				noIndex = i;
				break;
			}
		}
		if(parts[1].equals("healed")){
			c.setHealedTarget(parts[3]);
			yesIndex = 4;
		}
		else if(parts[1].equals("ability")){
			
			int endCond = 0;
			for(int i = 3; i<parts.length; i++){
				if(parts[i].contains("(")){
					endCond = i-1;
					yesIndex = i;
					break;
				}
			}
			String[] condAb = new String[endCond-1]; 
			for(int i =0 ; i<condAb.length ; i++){
				condAb[i] = parts[2+i];
			}
			c.setCondAbility(getAbility(condAb));
		}
		else if(parts[1].contains("count")){
			c.setCondition("count");
			String[] count = new String[10];
			count[0] = parts[1];
			int i =0 ;
			for(i=2 ; i<parts.length ; i++){
				count[i-1] = parts[i];
				if(parts[i].contains(">") || parts[i].contains("<") || parts[i].contains("=") || parts[i].contains("!=") 
						|| parts[i].contains(">=") || parts[i].contains("<=")){
					
					if(parts[i].contains(">=")){
						count[i-1] = parts[i].substring(0, parts[i].indexOf(">="));
						int n = Integer.parseInt(parts[i].substring(parts[i].indexOf(">=")+2));
						c.setCompareTo(n);
						c.setComparisonRule(">=");
						break;
					}
					else if(parts[i].contains("<=")){
						count[i-1] = parts[i].substring(0, parts[i].indexOf("<"));	
						int n = Integer.parseInt(parts[i].substring(parts[i].indexOf("<=")+2));
						c.setCompareTo(n);
						c.setComparisonRule("<=");
						break;
					}
					else if(parts[i].contains("=")){
						count[i-1] = parts[i].substring(0, parts[i].indexOf("="));
						int n = Integer.parseInt(parts[i].substring(parts[i].indexOf("=")+1));
						c.setCompareTo(n);
						c.setComparisonRule("=");
						break;
					}
					else if(parts[i].contains("!=")){
						count[i-1] = parts[i].substring(0, parts[i].indexOf("!"));
						int n = Integer.parseInt(parts[i].substring(parts[i].indexOf("!=")+2));
						c.setCompareTo(n);
						c.setComparisonRule("!=");
						break;
					}
					else if(parts[i].contains(">")){
						count[i-1] = parts[i].substring(0, parts[i].indexOf(">"));	
						int n = Integer.parseInt(parts[i].substring(parts[i].indexOf(">")+1));
						c.setCompareTo(n);
						c.setComparisonRule(">");
						break;
					}
					else if(parts[i].contains("<")){
						count[i-1] = parts[i].substring(0, parts[i].indexOf("<"));	
						int n = Integer.parseInt(parts[i].substring(parts[i].indexOf("<")+1));
						c.setCompareTo(n);
						c.setComparisonRule("<");
						break;
					}
						
				}
			}
			String c1 = getString(count, 0 , i-1, ":");
			c.setCounter(counter(c1.split(":")));
			yesIndex = i+1;
		}
		if(parts[yesIndex].contains("(")){
			parts[yesIndex] = parts[yesIndex].replace("(", "");
			parts[noIndex-1] = parts[noIndex-1].replace(")", "");
			String yes = getString(parts, yesIndex, noIndex-1, ":");
			String[] yesParts = yes.split(";");
		
			if(yesParts.length>1){
				CompositeAbility a = new CompositeAbility();
				for(int i=0; i<yesParts.length; i++){
					a.addAbility(getAbility(yesParts[i].split(":")));
				}
				c.setYes(a);
			}
			else{
				Ability a = getAbility(yesParts[0].split(":"));
				c.setYes(a);
			}
		}
		else{//no ( 
			String ab = getString(parts, yesIndex, noIndex-1, ":");
			Ability a = getAbility(ab.split(":"));
			c.setYes(a);
		}
		
		if(noIndex < parts.length){//index of else
			if(parts[noIndex+1].contains("(")){
				parts[noIndex+1].replace("(", "");
				parts[parts.length-1].replace(")", "");
				String no = getString(parts, noIndex+1, parts.length-1, ":");
				String[] noParts = no.split(";");
			
				if(noParts.length>1){
					CompositeAbility a = new CompositeAbility();
					for(int i=0; i<noParts.length; i++){
						a.addAbility(getAbility(noParts[i].split(":")));
					}
					c.setNo(a);
				}
				else{
					Ability a = getAbility(noParts[0].split(":"));
					c.setNo(a);
				}
		    }
			else{//no ( 
				String ab = getString(parts, noIndex+1, parts.length-1, ":");
				Ability a = getAbility(ab.split(":"));
				c.setNo(a);
			}
	}
		return c;
	}

	private String getString(String[] a, int from, int to, String c){
		String s = "";
		for(int i=from; i<=to; i++){
			s+= a[i];
			if(i != to){
				s+=c;
			}
		}
		return s;
	}
	
	private Ability deck(String[] parts2) {
		Deck d = new Deck();
		d.setTarget(parts2[2]);
		d.setDest(parts2[4]);
		
		if(parts2[5].equals("bottom") || parts2[5].equals("top")){
			d.setDestTopBottom(parts2[5]);
			int next = 6;
			if(parts2[next].equals("choice")){
				d.setChoice(true);
				next++;
				d.setChoiceTarget(parts2[next]);
				next++;
				if(parts2[next].equals("target")){
					next++;
				}
			}
			if(parts2[next].startsWith("count")){
				String co = getString(parts2, next, parts2.length-1, ":");
				Counter c = counter(co.split(":"));
				d.setCounter(c);
			}
			else{
				int amount = Integer.parseInt(parts2[next]);
				d.setAmount(amount);
			}
		}
		else if(parts2[5].equals("choice")){
			d.setChoice(true);
			d.setChoiceTarget(parts2[6]);
			
			if(parts2[7].startsWith("count")){
				String co = getString(parts2, 7, parts2.length-1, ":");
				Counter c = counter(co.split(":"));
				d.setCounter(c);
			}
			else{
				int amount = Integer.parseInt(parts2[7]);
				d.setAmount(amount);
			}
			
		}
		else if(parts2[5].startsWith("count")){
			String co = getString(parts2, 5, parts2.length-1, ":");
			Counter c = counter(co.split(":"));
			d.setCounter(c);
		}
		else{
			int amount = Integer.parseInt(parts2[5]);
			d.setAmount(amount);
		}
		return d;
	}

	private Ability dam(String[] parts) {
		Dam d = new Dam();
		d.setTarget(parts[2]);
		
		if(parts[2].equals("choice")){
			d.setChooseFrom(parts[3]);
			
			if(parts[4].contains("count")){
				String count = getString(parts, 4, parts.length, ":");
				Counter c = counter(count.split(":"));
				d.setCount(c);
			}
			else{
				int amount = Integer.parseInt(parts[4]);
				d.setAmount(amount);
			}
		}
		else{
			if(parts[3].contains("count")){
				String count = getString(parts, 3, parts.length-1, ":");
				Counter c = counter(count.split(":"));
				d.setCount(c);
			}
			else{
				int amount = Integer.parseInt(parts[3]);
				d.setAmount(amount);
			}	
		}
		return d;
	}
	
	
	
	private Counter counter(String[] parts){//index of count word
		Counter c = new Counter();
		int size = parts.length;
		int times = 1;
		String[] s = parts[0].split("\\*");//20*count(target:... 
		if(s.length == 2){
			times = Integer.parseInt(s[0]);
		}
		else{
			s = parts[size-1].split("\\*");//count(...)*20
			if(s.length == 2){
				times = Integer.parseInt(s[1]);
			}
		}
		c.setTimes(times);
		parts[size-1] = parts[size-1].substring(0, parts[size-1].indexOf(")"));
		
		if(size == 1){//count(your-hand), count(opponent-hand)
			c.setTarget(parts[0].substring(parts[0].indexOf("(")+1));
		}
		else if(size == 2){
			c.setTarget(parts[1]);
		}
		else if(size == 3){
			c.setTarget(parts[1]);
			c.setCountFor(parts[2]);
		}
		else if(size == 4){
			if(parts[1].equals("last")){
				c.setTarget(parts[1]+"-"+parts[2]);
				c.setCountFor(parts[3]);
			}
			else{
				c.setTarget(parts[1]);
				c.setCountFor(parts[2]);
				c.setCat(parts[3]);
			}
		}
		
		
		return c;
	}

	
	// for pokemon
	private ArrayList<Ability> parseAttacks(String line, Role role) {
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		ArrayList<int[][]> neededEnergy = new ArrayList<int[][]>();
		line = line.replaceAll(",", "");
		String[] parts1 = line.split("cat:");
		String[] parts2;
		
		for(int i = 1; i<parts1.length ; i++){// i=1 to skip "attacks:" part
			parts2 = parts1[i].split(":");
			if(parts2.length == 2){
				int[][] e = new int[1][2];
				e[0][0] = Energy.getCategory(parts2[0]).ordinal();
				e[0][1] = Integer.parseInt(parts2[1]);
				neededEnergy.add(e);
			}
			else if(parts2.length == 3){
				int[][] e = new int[1][2];
				e[0][0] = Energy.getCategory(parts2[0]).ordinal();
				e[0][1] = Integer.parseInt(parts2[1]);
				neededEnergy.add(e);
				int no = Integer.parseInt(parts2[2]);
				Ability a = parseAbility(no, role);
				a.getAbInfo().belongTo = "pokemon";
				a.setNeededEnergy(neededEnergy);
				neededEnergy = new ArrayList<int[][]>();
				abilities.add(a);
			}
		}
		
		return abilities;
	}
	
	
	public static void main(String[] args){
		System.out.println(Category.FIGHTING);
//		DeckParser parser = new DeckParser();
//		ArrayList<Card> d = parser.getDeck(Role.YOU);
//		for(int i = 0; i<d.size(); i++){
//			if(d.get(i) instanceof Pokemon){
//				Pokemon p = (Pokemon)d.get(i);
//				for(int j = 0; j<p.getAbilities().size(); j++){
//					System.out.println(p.getAbilities().get(j).getName()+": "+p.getAbilities().get(j).toString());
//				}
//			}
//			else if(d.get(i) instanceof Trainer){
//				Trainer t = (Trainer)d.get(i);
//				System.out.println(t.getAbility().getName()+": "+t.getAbility().toString());
//			}
//			
//		}
		
	}

	
}
