import java.math.MathContext;
import java.util.Arrays;

public class DecisionTree {

	public String name;
	private Board board;
	private DecisionTree inEdge;
	private DecisionTree[] outEdges;
	private Float[] outEdgeWeights;
	private Float weight = null;
	private Unit current_player;
	private int depth = 0;
	
	public DecisionTree(Board board){
		this.board = board;
	}
	
	public DecisionTree(Board board, String name){
		this.board = board;
		this.name = name;
	}
	
	public void addOutEdge(DecisionTree newTree){
		DecisionTree[] newOutEdges;
		if (outEdges != null){
			newOutEdges = new DecisionTree[this.outEdges.length+1];
			for (int i = 0; i < this.outEdges.length; i++) {
				newOutEdges[i] = this.outEdges[i];
			}
		} else { newOutEdges = new DecisionTree[1]; }
		newOutEdges[newOutEdges.length-1] = newTree;
		newTree.setInEdge(this);
		this.outEdges = newOutEdges;
	}
	
	public void setInEdge(DecisionTree ancestorTree){
		this.inEdge = ancestorTree;
	}
	
	public Float getWeight(){
		if (this.weight == null){
			float sum = 0;
			for (DecisionTree decisionTree : outEdges) {
				sum += decisionTree.getWeight();
			}
			this.weight = sum;
		}
		return this.weight;
	}
	
	public void setWeight(Float weight){
		this.weight = weight;
	}
	
	
	public Board getBoard(){
		return this.board;
	}
	
	public void setCurrent_player(Unit current_player){
		this.current_player = current_player;
	}
	
	public Unit getCurrent_player(){
		return this.current_player;
	}
	
	public void setDepth(int depth){
		this.depth = depth;
	}
	
	public int getDepth(){
		return this.depth;
	}
	
	public int getNumOutEdges(){
		if (this.outEdges == null){
			return 0;
		}
		return this.outEdges.length;
	}
	
	public DecisionTree[] getOutEdges(){
		return this.outEdges;
	}
	
	public void weighOutEdges(){
		outEdgeWeights = new Float[outEdges.length];
		for (int i = 0; i < outEdgeWeights.length; i++) {
			outEdgeWeights[i] = outEdges[i].getWeight();
		}
	}
	
	public Board[] getChildBoards(){
		Board[] children = new Board[outEdges.length];
		for (int i = 0; i < outEdges.length; i++) {
			children[i] = outEdges[i].getBoard();
		}
		return children;
	}
	
	public int getDecision(){
		weighOutEdges();
		int index = 0;
		
		//Choosing closest to the average.
		int counter = 0;
		float avg = 0;
		float temp = 0;
		for (Float dbl : outEdgeWeights) {
			avg += dbl;
		}
		avg /= outEdgeWeights.length;
		temp = avg;
		for (Float dbl : outEdgeWeights) {
			temp = (Math.abs(dbl-avg)<temp)? dbl: temp;
		}
		for (Float dbl : outEdgeWeights) {
			if (dbl == temp){
				index = counter;
				break;
			}
			counter++;
		}
		
		//Choosing highest/lowest.
//		System.out.println("Current player: " + current_player);
//		if (current_player.equals("O")){
//			int counter = 0;
//			float max = 0;
//			for (Float flt : outEdgeWeights) {
//				max += flt;
//			}
//			max /= outEdgeWeights.length;
//			for (Float flt : outEdgeWeights) {
//				max = Math.max(max, flt);
//			}
//			for (DecisionTree decisionTree : outEdges) {
//				if (NaughtsAndCrosses.gameEnd(decisionTree.getBoard(), NaughtsAndCrosses.x)) {
//					index = counter;
//					break;
//				}
//				counter++;
//			}
//			counter = 0;
//			for (Float flt : outEdgeWeights) {
//				if (flt == max) {
//					index = counter;
//					break;
//				}
//				counter++;
//			}
//		}
//		else {
//			int counter = 0;
//			float min = 0;
//			for (Float flt : outEdgeWeights) {
//				min += flt;
//			}
//			min /= outEdgeWeights.length;
//			for (Float flt : outEdgeWeights) {
//				min = Math.min(min, flt);
//			}
//			for (DecisionTree decisionTree : outEdges) {
//				if (NaughtsAndCrosses.gameEnd(decisionTree.getBoard(), NaughtsAndCrosses.o)){
//					index = counter;
//					break;
//				}
//				counter++;
//			}
//			counter = 0;
//			for (Float flt : outEdgeWeights) {
//				if (flt == min){
//					index = counter;
//					break;
//				}
//				counter++;
//			}
//		}
		return index;
	}
	
	public DecisionTree getChild(int index){
		return outEdges[index];
	}
	
	public Float[] getWeights(){
		return outEdgeWeights;
	}
}
