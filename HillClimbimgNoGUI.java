package noGUI;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class HillClimbimgNoGUI {
	//steepest ascent version

	private static int[] queenPositions;
	private static int boardsize = 8;
	private static int[][] board;

	public static void main(String[] args) {
		
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Enter the size of board: ");
		boardsize = reader.nextInt(); // Scans the next token of the input as an int.
		while(boardsize < 4) {
			System.out.println("Please enter size greater than 3, otherwise it is not possible to find.");
			boardsize = reader.nextInt(); // Scans the next token of the input as an int.			
		}
		if(boardsize > 3) {
			long startSystemTimeNano = getSystemTime( );
			long startUserTimeNano   = getUserTime( );
			long startCpuTimeNano    = getCpuTime();
			hillClimbing();
			long taskUserTimeNano    = getUserTime( ) - startUserTimeNano;
			long taskSystemTimeNano  = getSystemTime( ) - startSystemTimeNano;
			long taskCpuTimeNano     = getCpuTime() - startCpuTimeNano;
			System.out.println("User time is " + (taskUserTimeNano/(double)1000000) + " ms");
			System.out.println("System time is " + (taskSystemTimeNano/(double)1000000) + " ms");
			System.out.println("CPU time is " + (taskCpuTimeNano/(double)1000000) + " ms");
		}
		reader.close();
		//Boolean result = blackBoxTest();
		//System.out.println(result);
	}
	
	public static Boolean blackBoxTest() {
		boardsize = 5;
		//There are 10 possible result for n=5
		int[][] posResult1 =  {{0,0,0,1,0},{0,1,0,0,0},{0,0,0,0,1},{0,0,1,0,0},{1,0,0,0,0}};
		int[][] posResult2 =  {{0,1,0,0,0},{0,0,0,1,0},{1,0,0,0,0},{0,0,1,0,0},{0,0,0,0,1}};
		int[][] posResult3 =  {{1,0,0,0,0},{0,0,1,0,0},{0,0,0,0,1},{0,1,0,0,0},{0,0,0,1,0}};
		int[][] posResult4 =  {{1,0,0,0,0},{0,0,0,1,0},{0,1,0,0,0},{0,0,0,0,1},{0,0,1,0,0}};
		int[][] posResult5 =  {{0,0,0,0,1},{0,0,1,0,0},{1,0,0,0,0},{0,0,0,1,0},{0,1,0,0,0}};
		int[][] posResult6 =  {{0,0,1,0,0},{1,0,0,0,0},{0,0,0,1,0},{0,1,0,0,0},{0,0,0,0,1}};
		int[][] posResult7 =  {{0,0,1,0,0},{0,0,0,0,1},{0,1,0,0,0},{0,0,0,1,0},{1,0,0,0,0}};
		int[][] posResult8 =  {{0,0,0,0,1},{0,1,0,0,0},{0,0,0,1,0},{1,0,0,0,0},{0,0,1,0,0}};
		int[][] posResult9 =  {{0,1,0,0,0},{0,0,0,0,1},{0,0,1,0,0},{1,0,0,0,0},{0,0,0,1,0}};
		int[][] posResult10 = {{0,0,0,1,0},{1,0,0,0,0},{0,0,1,0,0},{0,0,0,0,1},{0,1,0,0,0}};
		hillClimbing();
		int count1 = 0, count2 = 0, count3 = 0, count4 = 0 ,count5 = 0, count6 = 0, count7 = 0, count8 = 0, count9 = 0, count10 = 0;
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if(posResult1[i][j] == board[i][j]) {
					count1 +=1;
				}
				if(posResult2[i][j] == board[i][j]) {
					count2 +=1;
				}
				if(posResult3[i][j] == board[i][j]) {
					count3 +=1;
				}
				if(posResult4[i][j] == board[i][j]) {
					count4 +=1;
				}
				if(posResult5[i][j] == board[i][j]) {
					count5 +=1;
				}
				if(posResult6[i][j] == board[i][j]) {
					count6 +=1;
				}
				if(posResult7[i][j] == board[i][j]) {
					count7 +=1;
				}
				if(posResult8[i][j] == board[i][j]) {
					count8 +=1;
				}
				if(posResult9[i][j] == board[i][j]) {
					count9 +=1;
				}
				if(posResult10[i][j] == board[i][j]) {
					count10 +=1;
				}
			}
		}
		System.out.println("1 " + count1 + "\n2 " + count2 + "\n3 " + count3 + "\n4 " + count4 + "\n5 " + count5 + "\n6 " + count6 + "\n7 " + count7 + "\n8 " + count8 + "\n9 " + count9 + "\n10 " + count10 );
		if(count1 == 25 || count2 == 25 || count3 == 25 || count4 == 25 || count5 == 25 || count6 == 25 || count7 == 25 || count8 == 25 || count9 == 25 || count10 == 25) {
			return true;
		}else {
			return false;
		}
	}


	public static void hillClimbing(){// O(n^4)
		boolean climb = true;
		int counter = 0;//to go out from infinite loop
		int trialNum = 3000;//if we want to limit
		while(climb) {
			counter++;
			if(counter%100 == 99) {
				System.out.println("Trying to find places... - Trial: " + (counter+1));
			}
			placeQueens(); // O(n^2)
			int[] bestQueenPos = new int[boardsize];// O(n)
			boolean best = false;
			// O(n^2)
			int targetPair = h();//current pair counts
			int prevPair = targetPair + 1;//to initialize previous conflicts in while comparison
			//number of pairs can be at most n-1
			System.out.println("New try:");
			int whilecount = 1;
			while(climb && (prevPair > targetPair)){ // prevPair > targetPair comparison to look does algorithm stuck at local minimum? // O(n^4)
				System.out.println(whilecount);
				System.out.println("conf: " + prevPair);
				whilecount +=1;
				prevPair = targetPair;
				// O(n^4)
				for(int j=0; j < boardsize; j++){ //n times
					best = false;
					for(int i=0; i<boardsize; i++){//n times
						//to skip calculating original position
						if(i != queenPositions[j]){
							moveQueen(i, j);
							//calculate current h then compare if best change
							int trialH = h(); //O(n^2)
							if(trialH < targetPair){//if it is better solution then 
								best = true;
								targetPair = trialH;
								bestQueenPos[j] = i;
							}
							//reset to original queen pos to calculate others
							resetQueen(i, j);// O(1)
						}					
					}
					//to make 2's again 1
					resetBoard(j);// O(n)
					if(best){
						placeBestQueen(j, bestQueenPos[j]);// O(n)
					}
				}
				if(h() == 0){// O(n^2)
					climb=false;
					System.out.println("Number of trials to find is " + counter);
					for(int i=0; i < boardsize; i++) {
						System.out.println(Arrays.toString(board[i]));
					}
				}
			}
			if(h() != 0 && counter%20 == 19){
				System.out.println("New trial to find solution...");
			}		
		}
		if(climb) {
			System.out.print("The solution could not be found in " + trialNum + " trial.");
		}
	}

	// O(n^2)
	public static int h(){ //heuristic

		int num_pairs = 0;

		//O(n^2)
		//Checks if the queens in the same row
		for(int i=0; i < boardsize; i++){//  n times iterates
			//in every row pairs becomes empty
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			for(int j=0; j < boardsize; j++){// n times iterates
				if(board[i][j] == 1){
					pairs.add(true);
				}				
			}
			//adds pair in the row to the total number of pairs
			if(pairs.size() != 0){// constant time
				num_pairs = num_pairs + (pairs.size()-1);
			}			
		}

		// O(n^2)
		//checks diagonally starting from top left
		int rows = boardsize;
		int column = boardsize;
		int maxSum = 2*boardsize -2;//-2 for starting from 0 so -1 for row,-1 for col

		for(int sum=0; sum <= maxSum; sum++){ // 2n times
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			for(int i=0; i < rows; i++){ // n times
				int j = sum -i;
				if(j >= 0 && j < rows && board[i][j] == 1){
					pairs.add(true);
				}			
			}
			if (pairs.size() != 0){
				num_pairs = num_pairs + (pairs.size() - 1);
			}
		}

		// O(n^2)
		//checks mirror diagonal starting from first row(column based)
		for(int i=0; i < column; i++){ // n times
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			Boolean check = true;
			int dummyCol = i, dummyRow = 0;

			if(board[dummyRow][dummyCol] == 1){
				pairs.add(true);
			}

			// n times
			while(check == true){
				dummyRow = dummyRow + 1;
				dummyCol = dummyCol + 1;
				if(dummyCol < boardsize && dummyRow < boardsize ){
					if(board[dummyRow][dummyCol] == 1){
						pairs.add(true);
					}
				}else{
					check = false;
				}				
			}
			if(pairs.size() != 0)
				num_pairs = num_pairs + (pairs.size() - 1);
		}

		// O(n^2)
		//checks mirror diagonal starting from first column(row based)
		for(int i=1; i < rows; i++){
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			Boolean check = true;
			int dummyRow = i, dummyCol = 0;

			if(board[dummyRow][dummyCol] == 1){
				pairs.add(true);				
			}

			while (check == true) {
				dummyRow = dummyRow + 1;
				dummyCol = dummyCol + 1;
				if(dummyCol < boardsize && dummyRow < boardsize ){
					if(board[dummyRow][dummyCol] == 1){
						pairs.add(true);
					}
				}else{
					check = false;
				}
			}
			if(pairs.size() != 0)
				num_pairs = num_pairs + (pairs.size() - 1);
		}		

		return num_pairs;
	}

	// O(1)
	public static void moveQueen(int row, int col){

		//makes original position 2 to indacate it is visited and icon null
		board[queenPositions[col]][col] = 2;
		board[row][col] = 1;
	}

	// O(1)
	public static void resetQueen(int row, int col){
		if(board[row][col] == 1){
			board[row][col] = 0;
		}
	}

	// O(n)
	public static void resetBoard(int col){
		for(int i=0; i< boardsize; i++){
			if(board[i][col] == 2){
				board[i][col] = 1;
			}
		}
	}

	// O(n)
	public static void placeBestQueen(int col, int queenPos) {

		for (int i = 0; i < board.length; i++) {
			if (board[i][col] == 1){
				board[i][col] = 2;
			}
		}
		board[queenPos][col] = 1;
		for (int i = 0; i < board.length; i++) {
			if (board[i][col] == 2){
				board[i][col] = 0;
			}
		}		
	}

	// O(n)
	private static int[] generateQueens(){

		int[] randPositions = new int[boardsize];
		Random rand = new Random();

		for(int i=0; i < boardsize; i++){
			randPositions[i] = rand.nextInt(boardsize);
		}

		return randPositions;

	}

	// O(n^2)
	public static void placeQueens(){
		board = new int[boardsize][boardsize];
		queenPositions = generateQueens();

		for(int i=0; i < boardsize; i++){
			board[queenPositions[i]][i] = 1;
		}
	}
	

	/** Get CPU time in nanoseconds. */
	public static long getCpuTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadCpuTime( ) : 0L;
	}
	 
	/** Get user time in nanoseconds. */
	public static long getUserTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadUserTime( ) : 0L;
	}

	/** Get system time in nanoseconds. */
	public static long getSystemTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        (bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime( )) : 0L;
	}
}
