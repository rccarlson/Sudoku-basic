import java.util.Scanner;

public class solver {
	public static Scanner in = new Scanner(System.in);

	public static void main(String[] args) {
		Puzzle p = new Puzzle();
		p.print();
		p.solve();
		p.print();
		//p.test();
	}

}
