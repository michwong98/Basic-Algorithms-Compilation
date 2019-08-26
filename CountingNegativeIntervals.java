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
     * An interval is a contiguous subset of numbers in the list
     * Want to compute the number of intervals for which the numbers in the intervals sum to a negative number
     *
     * Input format:
     * N describes the length of the list
     * each subsequent line will contain a single number n which will be an element of the list
     * 
     * Sample input:
     * 6
     * -1
     * -2
     * 4
     * 3
     * 0
     * -1
     *
    */

    public static long countIntervals(List<Integer> li) {
        
        int n = li.size();
        if (n == 0)
            return 0;
        
        /*
         * sum(i, j) = sum(0, j) - sum(0, i - 1), a negative subarray occurs whenever sum(0, j) < sum(0, i - 1)
         * we're looking for transpositions in the array of sums, each transposition is an instance of this inequality
         * of course, if the array sum starts negative, there are no transpositions, so we will need to count the total
         * number of negative elements in the sum array
         * */
        
        List<Integer> sumList = new ArrayList<Integer>(); //creating the sum array
        int s = li.get(0);
        sumList.add(s);
        for (int i = 1; i < n; i++) {
            s += li.get(i);
            sumList.add(s);
        }
        
        long count = 0;
        count += merge_sort(sumList, 0, n - 1);

        return count; //transpositions + number of negs
        
    } //end countIntervals method
    
    public static long merge_sort(List<Integer> arr, int low, int high) {
        
        if (high < low) //base cases
            return 0;
        if (low == high) {
            if (arr.get(high) < 0){
                return 1; //single element, is negative
            } else {
                return 0;
            } //end if
        } //end if
        long count = 0;
        int mid = (low + high) / 2;
        
        count += merge_sort(arr, low, mid) + merge_sort(arr, mid + 1, high); //divide into two subarrays, recursive call
        
        List<Integer> A = new ArrayList<Integer>(), B = new ArrayList<Integer>(); //copy of the sorted subarrays
        for (int i = low; i < mid + 1; i ++) {
            A.add(arr.get(i));
        } //endfor
        for (int i = mid + 1; i < high + 1; i++) {
            B.add(arr.get(i));
        } //endfor
        
        int aSize = A.size(), bSize = B.size();
        int a = 0, b = 0, i = low;
        int aValue, bValue;
        while ( a < aSize && b < bSize ) { //both are nonempty
            if ( (aValue = A.get(a)) <= (bValue = B.get(b)) ) { //a <= b
                arr.set(i, aValue);
                a++; i++;
            } else {
                arr.set(i, bValue); //counting transpositions
                b++; count += aSize - a; i++;
            } //endif
        } //endwhile
        while ( a < aSize ) {
            arr.set(i, A.get(a));
            a++; i++;
        } //endwhile
        while ( b < bSize ) {
            arr.set(i, B.get(b));
            b++; i++;
        } //endwhile

        return count;
        
    } //end merge_sort method
}

public class CountingNegativeIntervals {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> arr = IntStream.range(0, n).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .map(String::trim)
            .map(Integer::parseInt)
            .collect(toList());

        long result = Result.countIntervals(arr);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
