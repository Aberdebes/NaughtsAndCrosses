
public class Board {
	private BoardNode[][] board;
	private int width;
	private int height;
	//private Boolean checkered;
	
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.board = new BoardNode[height][width];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new BoardNode();
			}
		}
	}
	
	public Board(Board other){
		this.width = other.getBoardWidth();
		this.height = other.getBoardHeight();
		this.board = new BoardNode[this.height][this.width];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = new BoardNode(other.getNode(i, j));
			}
		}
	}
	
	public int getBoardHeight(){return height;}
	public int getBoardWidth(){return width;}
	
	/*
	 * Taking indices starting from 1 rather than 0.
	 */
	public BoardNode getNode(int row, int column){
		return board[row][column];
	}
	
	public BoardNode[] getRow(int row){
		return board[row];
	}
	
	public BoardNode[] getColumn(int column){
		BoardNode[] output = new BoardNode[height];
		for (int i = 0; i < height; i++) {
			output[i] = board[i][column];
		}
		return output;
	}
	
	public BoardNode[] getDiagonal(int row, int column, int direction){
		
		BoardNode[] output = new BoardNode[0];
		try{
			int short_side = Math.min(this.height, this.width);
			int min_row_col = (this.height <= this.width)? row : column;
			switch(direction){
				case 1:
					output = new BoardNode[short_side-(short_side-min_row_col)];
					for (int i = 0; i < output.length; i++) {
						output[i] = board[row-i-1][column++-1];
					}
					break;
				case 2:
					output = new BoardNode[short_side-(short_side-min_row_col)];
					for (int i = 0; i < output.length; i++) {
						output[i] = board[row-i-1][column---1];
					}
					break;
				case 3:
					output = new BoardNode[short_side-min_row_col+1];
					for (int i = 0; i < output.length; i++) {
						output[i] = board[row+i-1][column---1];
					}
					break;
				case 4:
					output = new BoardNode[short_side-min_row_col+1];
					for (int i = 0; i < output.length; i++) {
						output[i] = board[row+i-1][column++-1];
					}
					break;
				default:
					output = new BoardNode[0];	
			}
		} catch (Exception e) {System.out.println(e);}
		return output;
	}
	
	public void displayBoard(){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void displayBoard(Board[] others){
		for (int i = 0; i < others[0].getBoardHeight(); i++) {
			for (int j = 0; j < others.length; j++) {
				for (int k = 0; k < others[j].getBoardWidth(); k++) {
					System.out.print(others[j].getNode(i, k));
				}
				System.out.print("  ");
			}
			System.out.println();
		}
	}
	
	public boolean isNotOccupied(int row, int column){
		return getNode(row, column).isEmpty();
	}
	
	public void setNode(Unit unit, int row, int column){
		getNode(row, column).setUnit(unit);
	}
	
	public boolean equals(Board other){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (!board[i][j].equals(other.getNode(i, j))) return false;
			}
		}
		return true;
	}
	
}
