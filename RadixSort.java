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
	 * Radix sort algorithm, with the capability of sorting on digits in the order provided
	 *
	 * Input format:
	 * d the number of digits to be sorted
	 * n how many numbers will be sorted
	 * <permutation of the numbers 0, ..., d-1 separted by spaces, dictates the order of digits the algorithm will sort on>
	 * <n non-negative integers separated by spaces>
	 *
	 * O(n + k) subroutine
	 */

	public static List<Integer> radix_sort(List<Integer> input_list, List<Integer> d_permutation) {
		int maximum = input_list.get(0);
		int num;
		for (int i = 0; i < input_list.size(); i++) { //finding the maximum
			if ((num = input_list.get(i)) > maximum) {
				maximum = num;
			}
		}
		int bit_count = 0; //the numbers of bits needed to represent the maximum
		while (maximum != 0) {
			bit_count++;
			maximum = maximum>>1;
		}
		int bit_length; //number of bits that are being compared at a time
		if (bit_count % d_permutation.size() == 0)
			bit_length = bit_count / d_permutation.size();
		else
			bit_length = bit_count / d_permutation.size() + 1;
		for (int i = 0; i < d_permutation.size(); i++) {
			input_list = counting_sort(input_list, d_permutation.get(i), bit_length);
		} //end for


		return input_list;
	} //end radix_sort

	public static List<Integer> counting_sort(List<Integer> input_list, int index, int bit_length) {
		//count records the number of elements in the input_list with a bit value of 0 and 1 at the index bit
		int[] count = new int[1<<bit_length];

		int comparison = 1;
		for (int i = 1; i < bit_length; i++) {
			comparison = comparison<<1;
			comparison++;
		}
		comparison = comparison<<(index * bit_length); //a sequence of 1s that is used for comparison

		for (int i = 0; i < input_list.size(); i++) {
			int bit_value = (input_list.get(i) & comparison)>>(bit_length * index);
			count[bit_value]++;
		} //end for
		for (int i = 0; i < count.length - 1; i++) {
			count[i + 1] = count[i] + count[i+1];
		}
		Integer[] output_array = new Integer[input_list.size()];

		//fills the new output_array
		for (int i = output_array.length - 1; i >= 0; i--) {
			int num = input_list.get(i);
			int bit_value = (num & comparison)>>(bit_length * index);
			output_array[count[bit_value] - 1] = num;
			count[bit_value]--;
		} //end for

		//turns out_array into a list and returns
		return new ArrayList<>(Arrays.asList(output_array));
	} //end counting_sort

}

public class RadixSort {
	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

		int d = Integer.parseInt(bufferedReader.readLine().trim());

		int n = Integer.parseInt(bufferedReader.readLine().trim());

		List<Integer> d_permutation = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
			.map(Integer::parseInt)
			.collect(toList());

		List<Integer> unsorted_list = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
			.map(Integer::parseInt)
			.collect(toList());

		List<Integer> sorted_list = Result.radix_sort(unsorted_list, d_permutation);

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
