import java.sql.Time;
import java.util.Scanner;

public class NaughtsAndCrosses {

	public static Unit x = new Unit("X");
	public static Unit o = new Unit("O");
	public static Scanner keyboard = new Scanner(System.in);
	public static DecisionTreeBuilder AI = new DecisionTreeBuilder();
	
	public static void main(String[] args) {
		while(true) gameLoop();
	}
	
	private static void gameLoop(){
		Unit current_player = x;
		Unit current_enemy = o;
		Board game_board = new Board(3,3);
		int counter = 1;
		setPlayerAI(x);
		setPlayerAI(o);
//		if (x.isAI()) NaughtsAndCrossesAI(x, game_board);
//		if (o.isAI()) NaughtsAndCrossesAI(o, game_board);
		
		while (true){
			if (current_player.isAI()){
				game_board.displayBoard();
				int decision = AI.getCurrent().getDecision();
				////////
				Board[] children = AI.getCurrent().getChildBoards();
				Float[] weights = AI.getCurrent().getWeights();
				System.out.println();
//				System.out.println("The current game_board is equal to the AI board: " + game_board.equals(AI.getCurrent().getBoard()));
				System.out.print("Weights: ");
				for (float dbl: weights) {
					System.out.print(dbl + "  ");
				}
				System.out.println("Selected board index: " + decision);
				System.out.println();
				Board.displayBoard(children);
				/////////
				AI.setCurrent(AI.getCurrent().getChild(decision).getBoard());
				game_board = new Board(AI.getCurrent().getBoard());
//				NaughtsAndCrossesAI bot = new NaughtsAndCrossesAI(current_player, current_enemy, game_board);
//				int selection[] = bot.getSelection();
//				game_board.setNode(current_player, selection[0], selection[1]);
//				System.out.print("Selection: ");
//				System.out.print(selection[0]);System.out.println(selection[1]);
				System.out.println();
				//search for float ups first
				//if none then try for middle
				//else play first adjacent
			}
			else{
				while (true){
					game_board.displayBoard();
					int selection[] = gameLoopText(current_player);
					if (game_board.isNotOccupied(selection[0]-1, selection[1]-1)){
						game_board.setNode(current_player, selection[0]-1, selection[1]-1);
						AI.setCurrent(game_board);
						break;
					}
					else {
						System.out.println("Sorry your selected square is already occupied, please try another.");
						System.out.println();
					}
				}
			}
			if (gameEnd(game_board, current_player)){
				game_board.displayBoard();
				System.out.print(current_player);
				System.out.println(" : You win..");
				break;
			}
			if (counter == 9){
				game_board.displayBoard();
				System.out.println("Game drawn..");
				break;
			}
			
			current_player = switchPlayer(current_player);
			current_enemy = switchPlayer(current_enemy);
			counter++;
		}
		AI.resetTree();
		if (x.isAI()) x.setAI();
		if (o.isAI()) o.setAI();
	}
	
	public static boolean gameEnd(Board game_board, Unit current_player){
		boolean output = false;
		for (int i = 0; i < game_board.getBoardHeight(); i++) {
			if (gameEndTestHorizontal(game_board.getRow(i), current_player)) return true;
		}
		for (int i = 0; i < game_board.getBoardWidth(); i++) {
			if (gameEndTestVertical(game_board.getColumn(i), current_player)) return true;
		}
		if (gameEndTestDiagonal(game_board.getDiagonal(1, 1, 4), current_player)) return true;
		if (gameEndTestDiagonal(game_board.getDiagonal(1, 3, 3), current_player)) return true;
		return output;
	}
	
	private static boolean gameEndTestHorizontal(BoardNode[] row, Unit current_player){
		boolean output = false;
		for (BoardNode boardNode : row) {
			try{
				if (boardNode.getUnit().equals(current_player)) output = true;
				else return false;}
			catch(Exception e){ return false;}
			
		}
		return output;
	}
	
	private static boolean gameEndTestVertical(BoardNode[] column, Unit current_player){
		boolean output = false;
		for (BoardNode boardNode : column){
			try{
				if (boardNode.getUnit().equals(current_player)) output = true;
				else return false;}
			catch(Exception e){return false;}
		}
		return output;
	}
	
	private static boolean gameEndTestDiagonal(BoardNode[] diagonal, Unit current_player){
		boolean output = false;
		for (BoardNode boardNode : diagonal) {
			try{
				if (boardNode.getUnit().equals(current_player)) output = true;
				else return false;}
			catch(Exception e){return false;}
		}
		return output;
	}
	
	private static int[] gameLoopText(Unit current){
		int[] selection = new int[2];
		while (true){
			System.out.println();
			System.out.print(current);
			System.out.println(" is playing.");
			System.out.print("Choose a row:");
			selection[0] = keyboard.nextInt();
			System.out.print("Choose a column:");
			selection[1] = keyboard.nextInt();
			System.out.println();
			if (selection[0]>=1 && selection[0]<=3){
				if (selection[1]>=1 && selection[1]<=3){
					break;
				}
			}
			System.out.println("Sorry your selection was invalid, please try again.");
		}
		return selection;
	}
	
	private static Unit switchPlayer(Unit current_player){
		if (current_player.equals("X")) return o;
		return x;
	}
	
	private static void setPlayerAI(Unit unit){
		int selection = 0;
		while (true){
			System.out.print(unit);
			System.out.println(" : Play as AI? 0 = No, 1 = Yes :");
			selection = keyboard.nextInt();
			if (selection == 1) {
				unit.setAI();
				break;
			}
			else if (selection == 0){
				break;
			}
			System.out.println("Sorry your selection was invalid, please try again.");
		}
	}
}

class NaughtsAndCrossesAI{
	
	private Unit bot;
	private Unit enemy;
	private Board game_board;
	
	public NaughtsAndCrossesAI(Unit bot, Unit enemy, Board game_board){
		this.bot = bot;
		this.enemy = enemy;
		this.game_board = game_board;
//		System.out.println(bot);
//		System.out.println(enemy);
	}
	//search for float ups first
	//if none then try for middle
	//else play first adjacent
	public int[] getSelection(){
		int[] output = tryTwoInLine();
		if (game_board.isNotOccupied(output[0], output[1])) return output;
		else {
			output = nextAvailable();
			
		}
		return output;
	}
	
	private int[] nextAvailable(){
		int[] output = new int[2];
		for (int i = 0; i < game_board.getBoardHeight(); i++) {
			for (int j = 0; j < game_board.getBoardWidth(); j++) {
				if (game_board.isNotOccupied(i,j)) {
					//System.out.println("Next adjacent");
					output = new int[]{i, j};
					return output;
				}
			}
		}
		return output;
	}
	
	private int[] tryTwoInLine(){//if two in a row see if other node is free and if it is then choose this one
		int[] output = {1,1};
		for (int i = 0; i < game_board.getBoardHeight(); i++) {
			if (lineChecker(game_board.getRow(i), 2)) {
				System.out.print("indices: ");System.out.print(i); System.out.print(" "); System.out.println(findOpenIndex(game_board.getRow(i)));
				System.out.println("twoinline row, switch 2");
				return output = new int[]{i, findOpenIndex(game_board.getRow(i))};
			}
		}
		for (int i = 0; i < game_board.getBoardWidth(); i++) {
			if (lineChecker(game_board.getColumn(i), 2)){
				System.out.print("indices: "); System.out.print(findOpenIndex(game_board.getRow(i)));System.out.print(" ");System.out.println(i); 
				System.out.println("twoinline column, switch 2");
				return output = new int[]{findOpenIndex(game_board.getColumn(i)), i};
			}
		}
		
		if (lineChecker(game_board.getDiagonal(1, 1, 4), 2)) {
			int i = findOpenIndex(game_board.getDiagonal(1, 1, 4));
			System.out.print("indices: "); System.out.print(i);System.out.print(" ");System.out.println(i);
			System.out.println("twoinline diag(1,1,4), switch 2");
			return output = new int[]{i,i};
		}
		
		if (lineChecker(game_board.getDiagonal(1, 3, 3), 2)) {
			int i = findOpenIndex(game_board.getDiagonal(1, 3, 3));
			System.out.print("indices: "); System.out.print(i);System.out.print(" ");System.out.println(2-i);
			System.out.println("twoinline diag(1,3,3), switch 2");
			//System.out.print(i); System.out.print(", "); System.out.print(3-i); System.out.println();
			return output = new int[]{i,2-i};
		}
//		for (int i = 0; i < game_board.getBoardHeight(); i++) {
//			if (lineChecker(game_board.getRow(i), 3)) {
//				System.out.print("indices: ");System.out.print(i); System.out.print(" "); System.out.println(findOpenIndex(game_board.getRow(i)));
//				System.out.println("twoinline row, switch 3");
//				return output = new int[]{i, findOpenIndex(game_board.getRow(i))};
//			}
//		}
//		for (int i = 0; i < game_board.getBoardWidth(); i++) {
//			if (lineChecker(game_board.getColumn(i), 3)){
//				System.out.print("indices: "); System.out.print(findOpenIndex(game_board.getRow(i)));System.out.print(" ");System.out.println(i); 
//				System.out.println("twoinline column, switch 3");
//				return output = new int[]{findOpenIndex(game_board.getColumn(i)), i};
//			}
//		}
//		
//		if (lineChecker(game_board.getDiagonal(1, 1, 4), 3)) {
//			int i = findOpenIndex(game_board.getDiagonal(1, 1, 4));
//			System.out.print("indices: "); System.out.print(i);System.out.print(" ");System.out.println(i);
//			System.out.println("twoinline diag(1,1,4), switch 3");
//			return output = new int[]{i,i};
//		}
//		
//		if (lineChecker(game_board.getDiagonal(1, 3, 3), 3)) {
//			int i = findOpenIndex(game_board.getDiagonal(1, 3, 3));
//			System.out.print("indices: "); System.out.print(i);System.out.print(" ");System.out.println(2-i);
//			System.out.println("twoinline diag(1,3,3), switch 3");
//			//System.out.print(i); System.out.print(", "); System.out.print(3-i); System.out.println();
//			return output = new int[]{i,2-i};
//		}
//		
		for (int i = 0; i < game_board.getBoardHeight(); i++) {
			if (lineChecker(game_board.getRow(i), 1)) {
				System.out.print("indices: ");System.out.print(i); System.out.print(" "); System.out.println(findOpenIndex(game_board.getRow(i)));
				System.out.println("twoinline row, switch 1");
				return output = new int[]{i, findOpenIndex(game_board.getRow(i))};
			}
		}
		for (int i = 0; i < game_board.getBoardWidth(); i++) {
			if (lineChecker(game_board.getColumn(i), 1)){
				System.out.print("indices: "); System.out.print(findOpenIndex(game_board.getRow(i)));System.out.print(" ");System.out.println(i); 
				System.out.println("twoinline column, switch 1");
				return output = new int[]{findOpenIndex(game_board.getColumn(i)), i};
			}
		}
		
		if (lineChecker(game_board.getDiagonal(1, 1, 4), 1)) {
			int i = findOpenIndex(game_board.getDiagonal(1, 1, 4));
			System.out.print("indices: "); System.out.print(i);System.out.print(" ");System.out.println(i);
			System.out.println("twoinline diag(1,1,4), switch 1");
			return output = new int[]{i,i};
		}
		
		
		if (lineChecker(game_board.getDiagonal(1, 3, 3), 1)) {
			int i = findOpenIndex(game_board.getDiagonal(1, 3, 3));
			System.out.print("indices: "); System.out.print(i);System.out.print(" ");System.out.println(2-i);
			System.out.println("twoinline diag(1,3,3), switch 1");
			//System.out.print(i); System.out.print(", "); System.out.print(3-i); System.out.println();
			return output = new int[]{i,2-i};
		}
		
		System.out.println("Centre");
		return output;
	}
	
	private boolean lineChecker(BoardNode[] line, int check_type){
		boolean output = false;
		int player_counter = 0;
		int enemy_counter = 0;
		for (BoardNode boardNode : line) {
			if (!boardNode.isEmpty()){
				if (boardNode.getUnit().equals(enemy)) enemy_counter++;
				if (boardNode.getUnit().equals(bot)) player_counter++;
			}
		}
//		System.out.print("player_counter: "); System.out.println(player_counter);
//		System.out.print("enemy_counter:"); System.out.println(enemy_counter);
		switch(check_type){
			case 1:
				if (enemy_counter>1 && player_counter == 0) {System.out.println("case 1");return true;}
				break;
			case 2:
				if (enemy_counter == 0 && player_counter == 1) {System.out.println("case 2");return true;}
				break;
			case 3:
				if (player_counter > 1) {System.out.println("case 3");return true;}
				break;
		}
		
		return output;
	}
	
	private int findOpenIndex(BoardNode[] line){
		int output = 0;
		for (int i = 0; i < line.length; i++) {
			if (line[i].isEmpty()) return i;
		}
		return output;
	}
}

class DecisionTreeBuilder {
	
	private DecisionTree root;
	private DecisionTree current;
	private Integer counter = 0;
	int xCount = 0;
	int oCount = 0;
	int neutral = 0;
	int total = 0;
	
	public DecisionTreeBuilder(){
		Board game_board = new Board(3,3);
		root = new DecisionTree(game_board);
		root.name = "root";
		root.setCurrent_player(NaughtsAndCrosses.x);
		createChildren(root, NaughtsAndCrosses.x);
//		System.out.println("Total nodes in graph: " + counter);
		root.getWeight();
		System.out.println("Weight of Root: " + root.getWeight());
		current = root;
		System.out.println("X: " + xCount + "  O: " + oCount + "  N: " + neutral + "  Total: " + total);
//		float test = 1/2f;
//		System.out.println(String.format("%.200f",test));
	}
	
	public DecisionTree getCurrent(){
		return this.current;
	}
	
	public void setCurrent(Board current_board){
		DecisionTree[] children = current.getOutEdges();
		int counter = 0;
		for (DecisionTree decisionTree : children) {
			if (decisionTree.getBoard().equals(current_board)){
				current = decisionTree;
				return;
			}
			counter++;
		}
		if (counter == children.length){
			System.out.println();
			System.out.println("Counter: " + counter + "  children.length: " + children.length);
			System.out.println("-------Something went wrong when setting current DecisionTree");
			System.out.println();
		}
	}
	
	public void resetTree(){
		current = root;
	}
	
	private void createChildren(DecisionTree root, Unit unit){
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
//				System.out.println("i: "+i+"  j: "+j);
				if (root.getBoard().isNotOccupied(i, j)){
					Board new_board = new Board(root.getBoard());
					new_board.setNode(unit, i, j);
					DecisionTree newBranch = new DecisionTree(new_board, counter.toString());
					counter++;
					newBranch.setCurrent_player(unit);
					newBranch.setDepth(root.getDepth()+1);
					root.addOutEdge(newBranch);
//					System.out.println("Root name: " + root.name);
//					System.out.println("Player: "+ root.getCurrent_player());
//					root.getBoard().displayBoard();
//					System.out.println();
//					System.out.println("New name: " + newBranch.name);
//					System.out.println("Player: "+ newBranch.getCurrent_player());
//					newBranch.getBoard().displayBoard();//Debug
//					System.out.println();
//					System.out.println("Depth: " + newBranch.getDepth());
//					NaughtsAndCrosses.keyboard.nextLine();
					if (!NaughtsAndCrosses.gameEnd(new_board, unit) && newBranch.getDepth() < 9){
						Unit newUnit = ((unit.equals("X") ? NaughtsAndCrosses.o : NaughtsAndCrosses.x));
						createChildren(newBranch, newUnit);
						} 
					else {
//						newBranch.getBoard().displayBoard();
						if (NaughtsAndCrosses.gameEnd(newBranch.getBoard(), NaughtsAndCrosses.x)){
//							System.out.println("1");
//							System.out.println();
							xCount++;
							total++;
							newBranch.setWeight((float) (77904/131184f));
						}
						else if (NaughtsAndCrosses.gameEnd(newBranch.getBoard(), NaughtsAndCrosses.o)){
//							System.out.println("-1");
//							System.out.println();
							newBranch.setWeight((float) (-1));//and this
							oCount++;
							total++;
						}
						else if (newBranch.getDepth() == 9){
//							System.out.println("0");
//							System.out.println();	
							newBranch.setWeight((float) (0));
							neutral++;
							total++;
							}
//						break;//I have a feeling this shouldn't be here maybe...
						}
				}
			}
		}
	}
}