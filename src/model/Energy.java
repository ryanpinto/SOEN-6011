package model;

public class Energy extends Card{
	
	public enum Category {COLORLESS, WATER, LIGHTNING, PSYCHIC, FIGHTING};
	private Category cat;
	
	public Energy(String name, Category cat){
		type = "Energy";
		this.name = name;	
		this.setCat(cat);
	}

	public Energy() {
	
	}
	
	public static Category getCategory(int ordinal){
		if(ordinal == 0)
			return Category.COLORLESS;
		if(ordinal == 4)
			return Category.FIGHTING;
		if(ordinal == 3)
			return Category.PSYCHIC;
		if(ordinal == 2)
			return Category.LIGHTNING;
		if(ordinal == 1)
			return Category.WATER;
		return null;
	}

	public static Category getCategory(String name){
		//System.out.println(name);
		if(name.equals("colorless"))
			return Category.COLORLESS;
		if(name.equals("fight"))
			return Category.FIGHTING;
		if(name.equals("psychic"))
			return Category.PSYCHIC;
		if(name.equals("lightning"))
			return Category.LIGHTNING;
		if(name.equals("water"))
			return Category.WATER;
		return null;
	}

	public void setCat(String s) {
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

	public Category getCat() {
		return cat;
	}

	public void setCat(Category cat) {
		this.cat = cat;
	}

}
