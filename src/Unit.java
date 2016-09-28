
public class Unit {
	private String unitToken;
	private boolean AI = false;
	
	public Unit(String token){
		this.unitToken = token;
	}
	
	public Unit(Unit other){
		this.unitToken = other.getToken();
		this.AI = other.isAI();
	}
	
	public String getToken() {
		return unitToken;
	}
	
	public boolean equals(Unit otherUnit){
		return unitToken==otherUnit.getToken();
	}
	
	public boolean equals(String string){
		return unitToken.equals(string);
	}
	
	public String toString(){
		return unitToken;
	}
	
	public void setAI(){
		AI = !AI;
	}
	
	public boolean isAI(){
		return AI;
	}
	
}
