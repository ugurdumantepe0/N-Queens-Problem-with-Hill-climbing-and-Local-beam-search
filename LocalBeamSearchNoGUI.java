package noGUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.lang.management.*;

public class LocalBeamSearchNoGUI {

	private static int[][] stateQueenPositions;
	private static int[][] successorQueenPositions;
	private static int[] h_values;
	private static int[] successorH;
	private static int boardsize = 8;
	private static int state_count;
	private static int[][] board;
	private static int[][][] states;
	private static int[][][] successorStates;
	private static Boolean resulted = false;
	
	public static void main(String[] args) {
		
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Enter the size of board greater than 3: ");
		boardsize = reader.nextInt(); // Scans the next token of the input as an int.
		while(boardsize < 4) {
			System.out.println("Please enter size greater than 3, otherwise it is not possible to find.");
			boardsize = reader.nextInt(); // Scans the next token of the input as an int.			
		}
		System.out.println("Enter the number of states: ");
		state_count = reader.nextInt(); // Scans the next token of the input as an int.
		while(state_count < 1) {
			System.out.println("Please enter state count greater than 0, otherwise it is not possible to find.");
			state_count = reader.nextInt(); // Scans the next token of the input as an int.			
		}
		if(boardsize > 3 && state_count > 0) {
			long startSystemTimeNano = getSystemTime( );
			long startUserTimeNano   = getUserTime( );
			long startCpuTimeNano    = getCpuTime();
			int trialCount = 0;
			while(resulted == false) {
				trialCount += 1;
				lbs();
			}
			long taskUserTimeNano    = getUserTime( ) - startUserTimeNano;
			long taskSystemTimeNano  = getSystemTime( ) - startSystemTimeNano;
			long taskCpuTimeNano     = getCpuTime() - startCpuTimeNano;
			System.out.println("Number of trials is " + trialCount);
			System.out.println("User time is " + (taskUserTimeNano/(double)1000000) + " ms");
			System.out.println("System time is " + (taskSystemTimeNano/(double)1000000) + " ms");
			System.out.println("CPU time is " + (taskCpuTimeNano/(double)1000000) + " ms");
		}
		reader.close();

		Boolean result = blackBoxTest();
		System.out.println(result);
	}	

	public static Boolean blackBoxTest() {
		boardsize = 5;
		state_count = 10;
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
		while(resulted == false) {
			lbs();
		}
		int count1 = 0, count2 = 0, count3 = 0, count4 = 0 ,count5 = 0, count6 = 0, count7 = 0, count8 = 0, count9 = 0, count10 = 0;
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if(posResult1[i][j] == board[i][j]) 
					count1 +=1;
				if(posResult2[i][j] == board[i][j])
					count2 +=1;
				if(posResult3[i][j] == board[i][j])
					count3 +=1;
				if(posResult4[i][j] == board[i][j]) 
					count4 +=1;
				if(posResult5[i][j] == board[i][j])
					count5 +=1;
				if(posResult6[i][j] == board[i][j])
					count6 +=1;
				if(posResult7[i][j] == board[i][j])
					count7 +=1;
				if(posResult8[i][j] == board[i][j])
					count8 +=1;
				if(posResult9[i][j] == board[i][j])
					count9 +=1;
				if(posResult10[i][j] == board[i][j])
					count10 +=1;
			}
		}
		//System.out.println("1 " + count1 + "\n2 " + count2 + "\n3 " + count3 + "\n4 " + count4 + "\n5 " + count5 + "\n6 " + count6 + "\n7 " + count7 + "\n8 " + count8 + "\n9 " + count9 + "\n10 " + count10 );
		if(count1 == 25 || count2 == 25 || count3 == 25 || count4 == 25 || count5 == 25 
				|| count6 == 25 || count7 == 25 || count8 == 25 || count9 == 25 || count10 == 25) {
			return true;
		}else {
			return false;
		}
	}

	public static void lbs(){// O(k*n^2*(k + n^2))
		board = new int[boardsize][boardsize];
		placeQueens(); // O(k*n^2)
	    h();// O(n^2 * k)
	    statePicker();// O(k*n^2)
	    int targetPair = h_Board(board);// O(n^2)
	    int prevPair = targetPair+1;
	    if(targetPair > 0){
	    	while((prevPair > targetPair) && resulted == false){// prevPair > targetPair comparison to look does algorithm stuck at local minimum?// O(k*n^2*(k + n^2))
	    		prevPair = targetPair;
	    		determineSuccessors();// O(k*n^2*(k + n^2))
	    		for(int i=0; i < state_count; i++){// O(k*n^2)
	    			for(int j=0; j < boardsize; j++){
	    				states[i][j] = successorStates[i][j].clone(); // successor states is 3d array that keeps all the successorStates of each state
	    			}
	    		}
	    		stateQueenPositions = successorQueenPositions.clone(); // O(n)
	    		h_values = successorH.clone();// O(n)
	    		statePicker();// O(n^2 + k)
	    		targetPair = h_Board(board);// O(n^2)
	    		if(targetPair == 0){
	    			resulted = true;
	    			System.out.println("Queens in board:");
		    		//print queens in here which means result found
		    		for(int i=0; i < boardsize; i++) {
						System.out.println(Arrays.toString(board[i]));
					}
	    		}
	    	}
			if(resulted == false){
	    		System.out.println("New trial to find solution...");
	    	}
	    }
	}
	
	// O(n^2)
	public static int[][] generateQueens(){
		int[][] randPositions = new int[state_count][boardsize];
		Random random = new Random();
		
		for(int i=0; i < state_count; i++){
			for(int j=0; j < boardsize; j++){
				randPositions[i][j] = random.nextInt(boardsize);
			}			
		}
		return randPositions;		
	}
	
	// O(k*n^2)
	public static void placeQueens(){
		
	    board = new int[boardsize][boardsize];
	    states = new int[state_count][boardsize][boardsize];
	    h_values = new int[state_count];
	    
	    // O(n^2)
	    stateQueenPositions = generateQueens().clone();
	    
	    // O(k*n^2)
	    for(int i=0; i < state_count; i++){
	    	for(int j=0; j < boardsize; j++){
	    		states[i][stateQueenPositions[i][j]][j]=1;	    		
	    	}
	    }
	}

	// O(k*n^2)
	// to calculate initial conflicts
	public static void h(){ // heuristic
		int num_pairs = 0;
		h_values = new int[state_count];
		
		int[][] dummyBoard = new int[boardsize][boardsize];
		
		for(int s=0; s < state_count; s++){
			
			dummyBoard = states[s].clone();
		
			//Checks if the queens in the same row
			for(int i=0; i < boardsize; i++){
				//in every row pairs becomes empty
				ArrayList<Boolean> pairs = new ArrayList<Boolean>();
				for(int j=0; j < boardsize; j++){
					if(dummyBoard[i][j] == 1){
						pairs.add(true);
					}				
				}
				//adds pair in the row to the total number of pairs
				if(pairs.size() != 0){
					num_pairs = num_pairs + (pairs.size()-1);
				}			
			}
		
			//checks diagonally starting from top left
			int rows = boardsize;
			int column = boardsize;
			int maxSum = 2*boardsize -2;//-2 for starting from 0 so -1 for row,-1 for col
		
			for(int sum=0; sum <= maxSum; sum++){
				ArrayList<Boolean> pairs = new ArrayList<Boolean>();
				for(int i=0; i < rows; i++){
					int j = sum -i;
					if(j >= 0 && j < rows && board[i][j] == 1){
						pairs.add(true);
					}				
				}
				if (pairs.size() != 0){
					num_pairs = num_pairs + (pairs.size() - 1);
				}
			}
		
			//checks mirror diagonal starting from first row(column based)
			for(int i=0; i < column; i++){
				ArrayList<Boolean> pairs = new ArrayList<Boolean>();
				Boolean check = true;
				int dummyCol = i, dummyRow = 0;
			
				if(dummyBoard[dummyRow][dummyCol] == 1){
					pairs.add(true);
				}
				
				while(check == true){
					dummyRow = dummyRow + 1;
					dummyCol = dummyCol + 1;
					if(dummyCol < boardsize && dummyRow < boardsize ){
						if(dummyBoard[dummyRow][dummyCol] == 1){
							pairs.add(true);
						}
					}else{
						check = false;
					}				
				}
				if(pairs.size() != 0)
					num_pairs = num_pairs + (pairs.size() - 1);
			}
		
			//checks mirror diagonal starting from first column(row based)
			for(int i=1; i < rows; i++){
				ArrayList<Boolean> pairs = new ArrayList<Boolean>();
				Boolean check = true;
				int dummyRow = i, dummyCol = 0;
			
				if(dummyBoard[dummyRow][dummyCol] == 1){
					pairs.add(true);				
				}
			
				while (check == true) {
					dummyRow = dummyRow + 1;
					dummyCol = dummyCol + 1;
					if(dummyCol < boardsize && dummyRow < boardsize ){
						if(dummyBoard[dummyRow][dummyCol] == 1){
							pairs.add(true);
						}
					}else{
						check = false;
					}
				}
				if(pairs.size() != 0)
					num_pairs = num_pairs + (pairs.size() - 1);
			}
			h_values[s] = num_pairs;
			num_pairs = 0;
		}
	}

	// O(n^2) ==> same as hill climbing
	public static int h_Board(int[][] myboard){
		
		int num_pairs = 0;
		int[][] dummyBoard = new int[boardsize][boardsize];
		dummyBoard= myboard.clone();
		
		for(int i=0; i < boardsize; i++){
			//in every row pairs becomes empty
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			for(int j=0; j < boardsize; j++){
				if(dummyBoard[i][j] == 1){
					pairs.add(true);
				}				
			}
			//adds pair in the row to the total number of pairs
			if(pairs.size() != 0){
				num_pairs = num_pairs + (pairs.size()-1);
			}			
		}
		
		//checks diagonally starting from top left
		int rows = boardsize;
		int column = boardsize;
		int maxSum = 2*boardsize -2;//-2 for starting from 0 so -1 for row,-1 for col
		
		for(int sum=0; sum <= maxSum; sum++){
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			for(int i=0; i < rows; i++){
				int j = sum -i;
				if(j >= 0 && j < rows && dummyBoard[i][j] == 1){
					pairs.add(true);
				}					
			}
			if (pairs.size() != 0){
				num_pairs = num_pairs + (pairs.size() - 1);
			}
		}
					//checks mirror diagonal starting from first row(column based)
		for(int i=0; i < column; i++){
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			Boolean check = true;
			int dummyCol = i, dummyRow = 0;
		
			if(dummyBoard[dummyRow][dummyCol] == 1){
				pairs.add(true);
			}
				
			while(check == true){
				dummyRow = dummyRow + 1;
				dummyCol = dummyCol + 1;
				if(dummyCol < boardsize && dummyRow < boardsize ){
					if(dummyBoard[dummyRow][dummyCol] == 1){
						pairs.add(true);
					}
				}else{
					check = false;
				}				
			}
			if(pairs.size() != 0)
				num_pairs = num_pairs + (pairs.size() - 1);
		}
		
		//checks mirror diagonal starting from first column(row based)
		for(int i=1; i < rows; i++){
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			Boolean check = true;
			int dummyRow = i, dummyCol = 0;
			
			if(dummyBoard[dummyRow][dummyCol] == 1){
				pairs.add(true);				
			}
		
			while (check == true) {
				dummyRow = dummyRow + 1;
				dummyCol = dummyCol + 1;
				if(dummyCol < boardsize && dummyRow < boardsize ){
					if(dummyBoard[dummyRow][dummyCol] == 1){
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

	// O(n^2 + k)
	public static void statePicker(){
		
		int bestHPos = 0;
		int bestH = 0;
		// O(k)
		for(int i=0; i < state_count; i++){//picks lowest h value from the array and keeps its location
			if(i == 0){
				bestHPos = 0;
				bestH = h_values[0];
			}else{
				if(bestH > h_values[i]){
					bestH = h_values[i];
					bestHPos = i;
				}				
			}
		}
		// O(n^2)
		for(int j=0; j < boardsize;j++){
			board[j] = states[bestHPos][j].clone();
		}
	}

	// O(1)
	public static int[][] moveQueen(int[][] myboard,int[] positions, int row,int col){
		
		myboard[positions[col]][col] = 2;
		
		myboard[row][col] = 1;
		
		return myboard;
	}
	
	// O(1)
	public static int[][] resetQueen(int[][] board,int[] positions, int row,int col){
		
		if(board[row][col] == 1)
			board[row][col] = 0;
		
		return board;
	}
	
	// O(n)
	public static int[][] resetBoard(int[][] board, int col){
		
		for(int i=0; i< boardsize; i++){
			if(board[i][col] == 2){
				board[i][col] = 1;
			}
		}
		return board;
	}
	
	// O(n)
	public static int[][] resetSuccessorBoard(int[][] board, int col){
		
		for(int i=0; i< boardsize; i++){
			if(board[i][col] == 2){
				board[i][col] = 0;
			}
		}
		return board;
	}
	
	// O(k*n^2*(k + n^2))
	public static void determineSuccessors(){
		
		successorStates = new int[state_count][boardsize][boardsize];
		successorQueenPositions = new int[state_count][boardsize];
		successorH = new int[state_count];
		// O(k)
		for(int i=0; i < state_count; i++){
			successorH[i] = Integer.MAX_VALUE;
		}
		
		int[][] dummyBoard = new int[boardsize][boardsize];
		int[] dummyPositions = new int[boardsize];
		
		for(int i=0; i < state_count; i++){// O(k*n^2*(k + n^2))
			
			// O(n^2)
			for(int l=0; l<boardsize; l++){
				dummyBoard[l] = states[i][l].clone();
			}
			dummyPositions = stateQueenPositions[i].clone();
			
			for(int j=0; j < boardsize; j++){// O(n^2*(k + n^2))
				
				for(int k=0; k < boardsize; k++){ // O(n*(k + n^2))
					
					if(k != dummyPositions[j]){//to skip original position
						dummyBoard = moveQueen(dummyBoard, dummyPositions,k , j).clone();
						dummyPositions[j] = k;
						//make move to the position
						int counter = 0;
						int dummyH = h_Board(dummyBoard);// O(n^2)
						Boolean found = false;
						while(found == false && counter < state_count){// O(k + n^2)
							//compare with other h's to pick best ones
							if(successorH[counter] > dummyH){//if better option found to take best top k successor
								successorH[counter] = dummyH;
								resetSuccessorBoard(dummyBoard, j);// O(n)
								//O(n^2)
								for(int d=0; d < boardsize; d++){//deep copy of the successorStates from dummyBoard
									successorStates[counter][d] = dummyBoard[d].clone();
								}
								// O(n)
								successorQueenPositions[counter] = dummyPositions.clone();//copies queen positions
								found = true;
							}
							counter++;
						}
						dummyPositions = stateQueenPositions[i].clone();
						// O(n^2)
						for(int l=0; l<boardsize; l++){
							dummyBoard[l] = states[i][l].clone();
						}
					}					
				}
				// O(n)
				resetBoard(dummyBoard, j);
			}			
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
