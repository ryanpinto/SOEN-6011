package model;

import java.util.ArrayList;
import java.util.Observer;

import controller.abilities.Ability;
import model.Energy.Category;
import model.Pokemon.Stage;

public class Pokemon extends Card{
	
	public enum Stage {BASIC, STAGE_1}; 
	private Stage stage;
	private Category cat;
	public enum Status {PARALYZED, ASLEEP, STUCK, POISONED};
	private ArrayList<Status> status = new ArrayList<Status>();
	private ArrayList<Energy> attachedEnergy = new ArrayList<Energy>();//energy
	private ArrayList<Trainer> attachedItem = new ArrayList<Trainer>();//item
	private int HP;
	private int totalHP;
	private CardTile observerNotifier;//notify for hp
	private Category retreatEType = Category.COLORLESS;
	private int retreatCost = 0;
	private ArrayList<Ability> abilities = new ArrayList<Ability>();
	private String basic = "";
	private Pokemon basicPo = null;
	private boolean healed = false;
	
	
	
	public Pokemon() {
		
	}

	public Pokemon(String name, String type, Stage stage, Category cat, int totalHP, Category retreatEType,
			int retreatCost, String basic, ArrayList<Ability> abilities) {
		this.name = name;
		this.type = type;
		this.stage = stage;
		this.cat = cat;
		this.totalHP = totalHP;
		HP = totalHP;
		this.retreatEType = retreatEType;
		this.retreatCost = retreatCost;
		this.basic = basic;
		this.abilities = abilities;
	}
	
	public void reset(){
		status = new ArrayList<Status>();
		attachedEnergy = new ArrayList<Energy>();
		attachedItem = new ArrayList<Trainer>();
		HP = totalHP;
		observerNotifier = null;
		healed = false;
		basicPo = null;
		for(int i =0; i<abilities.size(); i++){
			abilities.get(i).reset();
		}
		
	}

	public void attachEnergy(Energy c){
		attachedEnergy.add(c);
		//System.out.println("Card type" + c.getType() + " - " + attachedCards.size());
	}
	
	public void detachEnergy(Energy c){
		attachedEnergy.remove(c);
	}
	
	public Energy deenergize(){
		if(attachedEnergy.size()>0){
			Energy c = attachedEnergy.remove(0);
			return c;
		}
		return null;
	}
	
	public void setAttachedE(ArrayList<Energy> e){
		attachedEnergy = e;
	}
	
	
	
	public void increaseHP(int inc){
		HP+= inc;
		if(HP > totalHP){
			HP = totalHP;
		}
		notifyObserver();
	}
	
	public boolean decreaseHP(int dec){
		System.out.println("Dam op active "+ HP);
		HP-= dec;
		System.out.println("Dam op active "+ HP);
		if(HP < 0){
			HP = 0;
		}
		notifyObserver();
		if(HP > 0){
			return false;
		}
		return true;
	}
	
	public int getHP(){
		return HP;
	}
	
	public void setHP(int h){
		HP = h;
	}
	
	public int getTotalHP(){
		return totalHP;
	}
	
	public void notifyObserver(){
		observerNotifier.notifyObserver();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage s) {
		this.stage = s;
	}
	
	public ArrayList<Energy> getAttachedE(){
		return attachedEnergy;
	}
	
	public ArrayList<Trainer> getAttachedItem(){
		return attachedItem;
	}
	
	public void setAttachedItem(ArrayList<Trainer> t){
		attachedItem = t;
	}

	public CardTile getObserverNotifier() {
		return observerNotifier;
	}

	public void setObserverNotifier(CardTile observerNotifier) {
		this.observerNotifier = observerNotifier;
	}

	public void setStage(String s) {
		if(s.equals("basic")){
			setStage(Stage.BASIC);
		}
		else if(s.equals("stage-one")){
			setStage(Stage.STAGE_1);
		}
		
	}

	public static Stage getStage(String s) {
		if(s.equals("basic")){
			return Stage.BASIC;
		}
		else if(s.equals("stage-one")){
			return Stage.STAGE_1;
		}
		return null;
	}
	
	public void setPokemonCat(String s) {
		if(s.equals("colorless")){
			setCat(Category.COLORLESS);
		}
		else if(s.equals("water")){
			setCat(Category.WATER);
		}
		else if(s.equals("lightning")){
			setCat(Category.LIGHTNING);
		}
		else if(s.equals("psychic")){
			setCat(Category.PSYCHIC);
		}
		else if(s.equals("fight")){
			setCat(Category.FIGHTING);
		}
	}

	public void setTotalHP(int hp2) {
		totalHP = hp2;	
		HP = hp2;
	}

	public void setRetreatEType(String s) {
		if(s.equals("colorless")){
			setRetreatEType(Category.COLORLESS);
		}
		else if(s.equals("water")){
			setRetreatEType(Category.WATER);
		}
		else if(s.equals("lightning")){
			setRetreatEType(Category.LIGHTNING);
		}
		else if(s.equals("psychic")){
			setRetreatEType(Category.PSYCHIC);
		}
		else if(s.equals("fight")){
			setRetreatEType(Category.FIGHTING);
		}
		
	}

	public void setRetreatCost(int c) {
		retreatCost = c;
	}
	
	public int getRetreatCost() {
		return retreatCost;
	}

	public void setBasic(String s) {
		basic = s;
	}
	
	public String getBasic(){
		return basic;
	}

	public void setAbilities(ArrayList<Ability> a) {
		abilities = a;	
	}
	
	public ArrayList<Ability> getAbilities() {
		return abilities;	
	}
	
	public void attachAbility(Ability a){
		abilities.add(a);
	}
	

	public Category getCat() {
		return cat;
	}

	public void setCat(Category cat) {
		this.cat = cat;
	}

	public Category getRetreatEType() {
		return retreatEType;
	}

	public void setRetreatEType(Category retreatEType) {
		this.retreatEType = retreatEType;
	}

	public ArrayList<Status> getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status.add(status);
	}
	
	public void destat(){
		status = new ArrayList<Status>();
	}

	public boolean isHealed() {
		return healed;
	}

	public void setHealed(boolean healed) {
		this.healed = healed;
	}

	public Pokemon getBasicPo() {
		return basicPo;
	}

	public void setBasicPo(Pokemon basicPo) {
		this.basicPo = basicPo;
	}

	

}
