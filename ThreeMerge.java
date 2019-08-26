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

	public static List<Integer> three_merge(List<Integer> input_list) {


		/*
		 * Recursive sort by splitting an array into 3 equal parts.
		 *
		 * Input form:
		 * n
		 * x x x x ...
		 * 
		 * n is the number of integers to be sorted, while the following line will contain all n integers seperated by a single space
		 */
		
		if (input_list.size() <= 1) {
			return input_list;
			//base case: List is empty or 1 element
			
		} else {
			int n = input_list.size();
			int size = (n/3) + ( (n%3) / 2 );
			//determines the size of the smaller lists, n/3 (+1) elements
			
			List<Integer> A = new ArrayList<Integer>(input_list.subList(0, size));
			List<Integer> B = new ArrayList<Integer>(input_list.subList(size, size*2));
			List<Integer> C = new ArrayList<Integer>(input_list.subList(size*2, input_list.size()));
			
			A = three_merge(A);
			B = three_merge(B);
			C = three_merge(C);
			//recursive calls
			
			List<Integer> D = new ArrayList<Integer>(n); // new merged list
			
			int a = 0, b = 0, c = 0; // index markers
			
			while (true) {
				if ( a < A.size() ) { // A is nonempty
					if ( b < B.size() ) { // B is nonempty
						if ( A.get(a) <= B.get(b) ) { // a < b
							if ( c < C.size() && C.get(c) <= A.get(a) ) { // c < a < b
								D.add(C.get(c)); c++;
							} else { // a < c < b
								D.add(A.get(a)); a++;
							}
						} else if ( c < C.size() && C.get(c) <= B.get(b) ) { // c < b < a
							D.add(C.get(c)); c++;
						} else {
							D.add(B.get(b)); b++; // b < c < a
						}
					} else if ( c < C.size() && C.get(c) <= A.get(a) ) { //  c < a, B empty
						D.add(C.get(c)); c++;
					} else { // a < c, B empty
						D.add(A.get(a)); a++;
					}
				} else if ( b < B.size() ) { //A empty, B non empty
					if ( c < C.size() && C.get(c) <= B.get(b) ) { // c < b, A empty
						D.add(C.get(c)); c++;
					} else { // b < c, A empty
						D.add(B.get(b)); b++;
					}
				} else if ( c < C.size() ) { // A and B empty
					D.add(C.get(c)); c++;
				} else { // all empty
					break;
				}
			}
			
			return D;
			//merges A + B, then merges (A + B) + C
		}
	}

}

public class ThreeMerge {
	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

		int n = Integer.parseInt(bufferedReader.readLine().trim());

		List<Integer> unsorted_list = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
			.map(Integer::parseInt)
			.collect(toList());

		List<Integer> sorted_list = Result.three_merge(unsorted_list);

		bufferedWriter.write(
			sorted_list.stream()
				.map(Object::toString)
				.collect(joining(" "))
			+ "\n"
		);

		bufferedReader.close();
		bufferedWriter.close();
	}
}
