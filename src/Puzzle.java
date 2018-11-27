import java.util.BitSet;

public class Puzzle {
	private int xdim,ydim,maxNum,xBlock,yBlock;
	private int[][] puzzle;
	private BitSet[][] pos;
	private int unsolved=0;
	public Puzzle() {
		xdim=9;ydim=9;maxNum=9;xBlock=3;yBlock=3;
		puzzle = new int[xdim][ydim];
		getPuzzle();
		populatePos();
		unsolved=numUnsolved();
	}
	@SuppressWarnings("unused")
	private void getPuzzle() {
		System.out.println("no spaces\n'x' for unknown\nenter at end of row");
		String[] input = new String[ydim];
		for(int i=0;i<xdim;i++) {
			input[i]=solver.in.nextLine();	//get all lines of puzzle
			if(input[i].length()!=xdim) {
				System.err.println("incorrect num of characters. please try again");
				System.out.print("");
				getPuzzle();
				return;
			}
		}
		for(int j=0;j<9;j++) for(int i=0;i<9;i++) {
			char c = input[j].charAt(i);
			if(c!='x') {
				puzzle[i][j] = Character.getNumericValue(c);
			}else {
				puzzle[i][j] = -1;
			}
		}
	}
	@SuppressWarnings("unused")
	private void loadTestPuzzle() {
		String[] input = {	"xxx87xx6x",
							"xxx5x4xxx",
							"4xxxxx9x8",
							"x6x7xx19x",
							"x7xxxxx2x",
							"x28xx5x4x",
							"2x5xxxxx1",
							"xxx2x6xxx",
							"x4xx93xxx"};
		for(int j=0;j<9;j++) for(int i=0;i<9;i++) {
			char c = input[j].charAt(i);
			if(c!='x') {
				puzzle[i][j] = Character.getNumericValue(c);
			}else {
				puzzle[i][j] = -1;
			}
		}
	}
	public void populatePos() {
		pos = new BitSet[xdim][ydim];
		for(int i=0;i<xdim;i++) {
			for(int j=0;j<ydim;j++) {
				pos[i][j] = new BitSet(maxNum);
				pos[i][j].set(0, maxNum);
			}
		}
	}
	public void print() {
		for(int j=0;j<puzzle[0].length;j++) {
			for(int i=0;i<puzzle.length;i++) {
				if(puzzle[i][j]!=-1)
					System.out.print(puzzle[i][j]+" ");
				else
					System.out.print("_ ");
			}
			System.out.println("");
		}
	}
	public void printPosAt(int x,int y) {
		for(int i=0;i<9;i++)
			if(pos[x][y].get(i))
				System.out.print(i+1);
		System.out.println("");
	}
	private int numUnsolved() {
		int blank=0;
		for(int i=0;i<xdim;i++)
			for(int j=0;j<ydim;j++)
				if(puzzle[i][j]==-1)
					blank++;
		return blank;
	}
	
	//conditional functions
	@SuppressWarnings("unused")
	private boolean isPossibleHere(int x,int y,int num) {
		return pos[x][y].get(num-1);
	}
	private boolean isRowClear(int x,int y,int num) {
		for(int i=0;i<xdim;i++) {
			if(puzzle[i][y]==num && i!=x)
				return false;
		}
		return true;
	}
	private boolean isColumnClear(int x,int y,int num) {
		for(int i=0;i<ydim;i++) {
			if(puzzle[x][i]==num && i!=y)
				return false;
		}
		return true;
	}
	private boolean isBlockClear(int x,int y,int num) {
		for(int i=0;i<xBlock;i++)
			for(int j=0;j<yBlock;j++)
				if(puzzle[(int) Math.floor(x/xBlock)*xBlock+i][(int) Math.floor(y/yBlock)*yBlock+j]==num && !(i==x && j==y)) 
					return false;
		return true;
	}
	private boolean isPossibleOnRow(int x, int y,int num) {
		for(int i=0;i<xdim;i++) {
			if(i!=y)
				if(pos[i][y].get(num-1))
					return true;
		}
		return false;
	}
	private boolean isPossibleOnColumn(int x,int y,int num) {
		for(int i=0;i<xdim;i++) {
			if(i!=x)
				if(pos[x][i].get(num-1))
					return true;
		}
		return false;
	}
	private boolean isPossibleOnBlock(int x,int y,int num) {
		for(int i=0;i<xBlock;i++)
			for(int j=0;j<yBlock;j++)
				if(!(((int) Math.floor(x/xBlock))*xBlock+i==x && (int) Math.floor(y/yBlock)*yBlock+j==y)) {
					//System.out.println("checking position ("+(((int) Math.floor(x/xBlock))*xBlock+i)+","+((int) Math.floor(y/yBlock)*yBlock+j)+") relative to position ("+x+","+y+")");
					if(pos[((int) Math.floor(x/xBlock))*xBlock+i][(int) Math.floor(y/yBlock)*yBlock+j].get(num-1)) 
						return true;
				}
		return false;
	}
	
	//possibility functions
	private void cleanSolvedPos() {
		for(int i=0;i<xdim;i++)
			for(int j=0;j<ydim;j++)
				if(puzzle[i][j] != -1)
					for(int k=1;k<maxNum+1;k++)
						if(k!=puzzle[i][j])
							pos[i][j].clear(k-1);
	}
	private void removeImpossiblePos() {	//turn bits to false in pos where possible
		for(int i=0;i<xdim;i++)
			for(int j=0;j<ydim;j++) {	//begin loop through full grid
				if(puzzle[i][j]!=-1) removeAllPossibleExceptActive(i,j);
				for(int numToTest=1;numToTest<=maxNum;numToTest++) {
					if(pos[i][j].get(numToTest-1) ) {	//if numToTest is still a possible answer that needs to be checked this iteration
						//begin testing
						if(!isRowClear(i,j,numToTest))	//puzzle already has this number solved on this row
							pos[i][j].clear(numToTest-1);
						if(!isColumnClear(i,j,numToTest))	//puzzle already has this number solved on this column
							pos[i][j].clear(numToTest-1);
						if(!isBlockClear(i,j,numToTest))	//puzzle already has this number solved on this block
							pos[i][j].clear(numToTest-1);
					}//end testing
				}//end numToTest loop
			}//end full grid loop
	}
	private int countPossible(int x,int y) {
		int count=0;
		for(int i=0;i<maxNum;i++)
			if(pos[x][y].get(i))
				count++;
		return count;
	}
	private int getOnlyPossible(int x,int y) {	//if a non 1 number of possible values are found, returns -1
		if(countPossible(x,y)!=1) {
			System.err.println("getOnlyPossible() was called on a location containing multiple possibilities");
			return -1;
		}
		for(int i=0;i<maxNum;i++)
			if(pos[x][y].get(i))
				return i+1;
		System.err.println("Unknown error in getOnlyPossible(): a value is possible, but could not be retreived");
		return -1;
	}
	private void removeAllPossibleExceptActive(int x,int y) {
		if(puzzle[x][y] == -1) return;
		pos[x][y].clear(0, maxNum);
		pos[x][y].set(puzzle[x][y]-1);
	}
	private void checkForPosError() {
		for(int i=0;i<xdim;i++)
			for(int j=0;j<ydim;j++)
				if(countPossible(i,j)<1 && puzzle[i][j]==-1) {
					System.err.println("Lost all possible answers on ("+i+","+j+")");
				}
	}
	
	//puzzle functions
	private void applySolved() {
		for(int i=0;i<xdim;i++)
			for(int j=0;j<ydim;j++) {
				if(countPossible(i,j)==1)	//if only one possibility exists, apply it
					puzzle[i][j]=getOnlyPossible(i,j);
			}
	}
	private void applyUniquePos() {
		for(int i=0;i<xdim;i++)
		for(int j=0;j<ydim;j++)
		for(int k=1;k<=maxNum;k++) {
			if(!isPossibleOnBlock(i,j,k) && pos[i][j].get(k-1) && puzzle[i][j]==-1) {
				//System.out.println("applyUniquePos() found " + k + " at ("+i+","+j+")");
				puzzle[i][j]=k;
			}
			if(!isPossibleOnRow(i,j,k) && pos[i][j].get(k-1) && puzzle[i][j]==-1) {
				//System.out.println("applyUniquePos() found " + k + " at ("+i+","+j+")");
				puzzle[i][j]=k;
			}
			if(!isPossibleOnColumn(i,j,k) && pos[i][j].get(k-1) && puzzle[i][j]==-1) {
				//System.out.println("applyUniquePos() found " + k + " at ("+i+","+j+")");
				puzzle[i][j]=k;
			}
		}
	}
	public boolean isSolved() {
		for(int i=0;i<xdim;i++)
			for(int j=0;j<ydim;j++)
				if(puzzle[i][j] == -1)
					return false;
		return true;
	}
	
	//miscellaneous functions
	public void test() {
		for(int y=0;y<ydim;y++)
			for(int x=0;x<xdim;x++)
				if(puzzle[x][y]==-1) {
					System.out.print("At ("+x+","+y+"): ");
					printPosAt(x,y);
				}
	}
	public void solve() {
		int iterations = 0;
		do{
			cleanSolvedPos();
			removeImpossiblePos();
			applyUniquePos();
			applySolved();
			checkForPosError();
			iterations++;
		}while(!isSolved() && iterations<2000);
		if(isSolved())
			System.out.println("Solved after "+iterations+" iterations");
		else
			System.out.println("Failed to solve puzzle\nDiscovered "+(100-Math.round((double)(numUnsolved()) * 100.0 / (double) unsolved))+"% of unsolved spaces");
	}
}
