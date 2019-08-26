import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

	/*
	 * Multiplies two polynomials by an implementation of Karatsuba's algorithm
	 * Input:
	 * n
	 * a0, a1, ... an
	 * b0, b1, ... bn
	 *
	 * n is degree of the two polynomials, and the two following lines consisted of n + 1 intergers separated by a space which are the coefficients
	 *
	*/

	  public static List<Integer> multiplyPolynomial(List<Integer> a, List<Integer> b) {
		List<Integer> c = new ArrayList<Integer>();
		if (a.size() <= 2) { //base case
			switch (a.size()) {
				case 0: //0
					return c;
				case 1: //just multiply
					c.add( a.get(0) * b.get(0));
					return c;
				case 2: //just multiply
					c.add(a.get(0) * b.get(0));
					c.add(a.get(0)*b.get(1)+a.get(1)*b.get(0));
					c.add(a.get(1)*b.get(1));
					return c;
				default:
					return c;
			} //endswitch   
		} else {

			int n = a.size() / 2 + a.size() % 2; //splits the polynomial

			List<Integer> a1 = a.subList(0, n), a2 = a.subList(n, a.size()), //a1 size >= a2 size
					b1 = b.subList(0, n), b2 = b.subList(n, b.size()); //b1 size >= b2 size
			List<Integer> a3 = new ArrayList<Integer>(), b3 = new ArrayList<Integer>();

			for (int i = 0; i < a2.size() && i < b2.size(); i++) {
				a3.add(a1.get(i) + a2.get(i)); //a3 = a1 + a2
				b3.add(b1.get(i) + b2.get(i)); //b3 = b1 + b2
			} //endfor
			if (a1.size() > a2.size() ) { //if sizes are not equal
				a3.add(a1.get(a1.size()-1));
				b3.add(b1.get(b1.size()-1));
			} //endif
			

			List<Integer> c1 = multiplyPolynomial(a1, b1), //recursive calls
					c2 = multiplyPolynomial(a2, b2),
					c3 = multiplyPolynomial(a3, b3);
			for (int i = 0; i < 2*a.size() - 1; i++) { //creates the polynomial
				c.add(0);
			} //endfor
			for (int i = 0; i < c1.size(); i++) {
				c.set(i, c.get(i) + c1.get(i));
				c3.set(i, c3.get(i) - c1.get(i));
			} //endfor
			for (int i = 0; i < c2.size(); i++) {
				c.set(2*n + i, c.get(2*n + i) + c2.get(i));
				c3.set(i, c3.get(i) - c2.get(i));
			} //endfor
			for (int i = 0; i < c3.size(); i++) {
				c.set(n + i, c.get(n + i) + c3.get(i));
			} //endfor
			return c;
		} //endif
	} //end multiplyPolynomial method

}

public class PolynomialMultiplication {
	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

		int n = Integer.parseInt(bufferedReader.readLine().trim());

		List<Integer> a = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
			.map(Integer::parseInt)
			.collect(toList());

		List<Integer> b = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
			.map(Integer::parseInt)
			.collect(toList());

		List<Integer> result = Result.multiplyPolynomial(a, b);

		bufferedWriter.write(
			result.stream()
				.map(Object::toString)
				.collect(joining(" "))
			+ "\n"
		);

		bufferedReader.close();
		bufferedWriter.close();
	}
}
