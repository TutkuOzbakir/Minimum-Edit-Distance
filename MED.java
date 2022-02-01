import java.io.File;
import java.util.*;

public class MED {

	public static void printMatrix(int[][] matrix, String first, String second) {

		int count = 0;

		for (int i = 0; i < second.length(); i++) {
			if (i == 0) {
				System.out.print("          " + second.charAt(i));
			} else {
				System.out.print("    " + second.charAt(i));
			}
		}

		System.out.println();

		for (int i = 0; i < matrix.length; i++) {

			if (i > 0) {
				System.out.print(first.charAt(i - 1));
			}

			for (int j = 0; j < matrix[0].length; j++) {

				if (i == 0 && count == 0) {
					System.out.print("     " + matrix[i][j]);
					count++;
				} else {
					System.out.print("    " + matrix[i][j]);
				}
			}
			System.out.println();
		}
	}
	////////////////////////////// MED VALUE////////////////////////////

	public static int med(String[][] directions, int[][] matrix, String first, String second) {

		int gapPenalty = 1;
		int matchPenalty = 0;
		int mismatchPenalty = 2;

		int medVal = Integer.MIN_VALUE;
		int diagonal = 0;
		int left = 0;
		int up = 0;

		for (int i = 0; i < matrix.length; i++) {

			for (int j = 0; j < matrix[0].length; j++) {

				if (i == 0 && j == 0) {

					matrix[i][j] = 0;
					directions[i][j] = "N";
				}

				else if (i == 0 && j != 0) { // First row

					matrix[i][j] = matrix[i][j - 1] + gapPenalty;
					directions[i][j] = "L";
				}

				else if (i != 0 && j == 0) { // First column

					matrix[i][j] = matrix[i - 1][j] + gapPenalty;
					directions[i][j] = "U";
				}

				else {

					if (first.charAt(i - 1) == second.charAt(j - 1)) {
						diagonal = matrix[i - 1][j - 1] + matchPenalty;
					}

					if (first.charAt(i - 1) != second.charAt(j - 1)) {
						diagonal = matrix[i - 1][j - 1] + mismatchPenalty;
					}

					left = matrix[i][j - 1] + gapPenalty;
					up = matrix[i - 1][j] + gapPenalty;

					if (left == up && left < diagonal || left == diagonal && left < up || diagonal == up && up > left
							|| diagonal == left && left == up || left < up && left < diagonal) {
						matrix[i][j] = left;
						directions[i][j] = "L";
					}

					else if (up == diagonal && up < left || diagonal == left && left > up
							|| up < left && up < diagonal) {
						matrix[i][j] = up;
						directions[i][j] = "U";
					}

					else if (up == left && left > diagonal || diagonal < left && diagonal < up) {
						matrix[i][j] = diagonal;
						directions[i][j] = "D";
					}

				}
			}
			//System.out.println("\nStep " + (i + 1));    //For part 2.
			//printMatrix(matrix, first, second);		  //For part 2.
		}

		medVal = matrix[matrix.length - 1][matrix[0].length - 1];
		//System.out.println("MED Result = " + medVal);		//For part 2.
		//resultString(directions, first, second);			//For part 2.
		return medVal;
	}

	public static void resultString(String[][] directions, String first, String second) {
		String lastElement = directions[directions.length - 1][directions[0].length - 1];
		String firstString = "";
		String secondString = "";
		int row = directions.length - 1;
		int column = directions[0].length - 1;

		while (lastElement != directions[0][0]) {

			if (lastElement == "L") {
				firstString += "-";
				secondString += second.charAt(column - 1);
				column--;
				lastElement = directions[row][column];

			}

			else if (lastElement == "U") {
				firstString += first.charAt(row - 1);
				secondString += "-";
				row--;
				lastElement = directions[row][column];
			}

			else if (lastElement == "D") {
				firstString += first.charAt(row - 1);
				secondString += second.charAt(column - 1);
				column--;
				row--;
				lastElement = directions[row][column];

			}
		}

		// Reverse string.
		StringBuilder a = new StringBuilder();
		a.append(firstString);
		a.reverse();
		System.out.println("First string:  " + a);
		a = new StringBuilder();
		a.append(secondString);
		a.reverse();
		System.out.println("Second string: " + a);
	}

	public static void main(String[] args) {

		int[][] matrix;
		String[][] directions;
		int rowNumber;
		int columnNumber;
		int medResult = Integer.MIN_VALUE;
		ArrayList<String> vocabulary = new ArrayList<String>();
		TreeMap<Integer, String> medResults = new TreeMap<Integer, String>();
		long before;
		long after;

		// Reading file

		String fileName = "vocabulary_tr.txt";
		
		try {
			File file = new File(fileName);
			Scanner reader = new Scanner(file);
			String data;
			while (reader.hasNextLine()) {
				data = reader.nextLine();
				vocabulary.add(data);
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("Error.");
		}

		
		// PART 1   //--> Lists 5 alternative correct words.
		System.out.println("Enter input word: ");
		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		sc.close();

		before = System.currentTimeMillis();

		for (int i = 0; i < vocabulary.size(); i++) {
			rowNumber = input.length();
			columnNumber = vocabulary.get(i).length();
			matrix = new int[rowNumber + 1][columnNumber + 1];
			directions = new String[rowNumber + 1][columnNumber + 1];
			medResult = med(directions, matrix, input.toLowerCase(), vocabulary.get(i).toLowerCase());
			medResults.put(medResult, vocabulary.get(i));
		}

		Set<Integer> keySet = new HashSet<Integer>();
		keySet = medResults.keySet();

		System.out.println("\nClosest words: ");
		int counter = 0;
		for (Integer s : keySet) {
			if (counter < 5) {
				System.out.println(medResults.get(s));
			}
			counter++;
		}

		after = System.currentTimeMillis() - before;
		System.out.println("\nTime (ms) :" + after);
		
		
		/*
		
		// PART 2 			//Finds MED Value and displays steps.
		before = System.currentTimeMillis();
		String first = "abcdef";
		String second = "dcba";
		rowNumber = first.length();
		columnNumber = second.length();
		matrix = new int[rowNumber + 1][columnNumber + 1];
		directions = new String[rowNumber + 1][columnNumber + 1];
		med(directions, matrix, first, second);
		after = System.currentTimeMillis() - before;
		System.out.println("\nTime (ms) :" + after);
		
		*/

	}

}
