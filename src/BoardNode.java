
public class BoardNode {
	private boolean isEmpty;
	private Unit unit;
	
	public BoardNode(){
		this.isEmpty = true;
	}
	
	public BoardNode(BoardNode other){
		this.isEmpty = other.isEmpty();
		this.unit = other.getUnit();
	}
	
	public String toString(){
		if (isEmpty) {return "| |";}
		else {return "|" + unit + "|";}
	}
	
	public boolean isEmpty(){
		return this.isEmpty;
	}
	
	public void setUnit(Unit unit){
		this.unit = unit;
		isEmpty = !isEmpty;
	}
	
	public Unit getUnit(){
		return unit;
	}
	
	public boolean equals(BoardNode other){
		if (isEmpty || other.isEmpty()){
			return isEmpty==other.isEmpty();
		}
		return this.unit.equals(other.getUnit());
	}

}
