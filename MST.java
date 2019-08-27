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
     * Minimum spanning tree for laying cable (minimizing cost)
     *
     * Input format:
     * n m, n cities and m edges
     * x y z
     * x y z
     * x y z
     * ...
     * x city 1, y city 2, z is cable cost
     *
     *
     * Sample Input:
     * 4 5
     * 1 4 2
     * 1 3 3
     * 2 3 2
     * 2 4 4
     * 3 4 3
     *
     * Sample Output:
     * 7
     */

    public static int lay_cable(int nodes, List<List<Integer>> edges) {
        int cost = 0;
        Subset[] subsets = new Subset[nodes];
        Vertex[] vertices = new Vertex[nodes];
        for (int i = 1; i <= nodes; i++) {
            make_set(subsets, vertices, i);
        }
        sortEdges(edges); //sorts according to ascending cost
        for (int i = 0; i < edges.size(); i++) {
            int u = edges.get(i).get(0); //subset containing u
            int v = edges.get(i).get(1); //subset containing v
            if (find(subsets, vertices, u) != find(subsets, vertices,v)) { //== if they belong to the same subset
                union(subsets, vertices, u, v); //union the two subsets, u and v are now in the same subset
                cost += edges.get(i).get(2); //increment cost by the weight of the edge
            }
        } //end for
        return cost; //total weight of the MST
    } //end lay_cable
    
    static class Vertex {
        int data;
        Vertex p;
    };
    
    static class Subset {
        Vertex root;
        int height;
    };
    
    static void make_set(Subset[] subsets, Vertex[] vertices, int vertex) {
        Vertex newVertex = new Vertex();
        newVertex.data = vertex;
        newVertex.p = null;
        vertices[vertex - 1] = newVertex;
        
        Subset newSubset = new Subset();
        newSubset.root = newVertex;
        newSubset.height = 1;
        subsets[vertex - 1] = newSubset;
    } //end make_set
    
    //finds the root of the subset that the vertex is contained in
    static Subset find(Subset[] subsets, Vertex[] vertices, int vertex) {
        Vertex v = vertices[vertex - 1];
        while (v.p != null) {
            v = vertices[v.p.data - 1];
        }
        return subsets[v.data - 1];
    }
    
    //sorts edges in ascending order based on cost
    static void sortEdges (List<List<Integer>> edges) {
        Collections.sort(edges, new Sortbycost());
    } //end sortEdges
    
    static void union(Subset[] subsets, Vertex[] vertices, int vertex1, int vertex2) {
        Subset s = find(subsets, vertices, vertex1);
        Subset r = find(subsets, vertices, vertex2);
        if (s == r)
            return;
        if (s.height > r.height) { //merge r into s
            r.root.p = s.root;
        } else if (s.height < r.height) { //merge s into r
            s.root.p = r.root;
        } else { //merge r into s, height of s increases
            r.root.p = s.root;
            s.height++;
        } //end if else
    } //end union
    
    public static class Sortbycost implements Comparator<List<Integer>> {
        public int compare(List<Integer> edge1, List<Integer> edge2) {
            return edge1.get(2) - edge2.get(2);
        }
    }

}

public class MST {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int m = Integer.parseInt(firstMultipleInput[1]);

        List<List<Integer>> edges = new ArrayList<>();

        IntStream.range(0, m).forEach(i -> {
            try {
                edges.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        int cost = Result.lay_cable(n, edges);

        bufferedWriter.write(String.valueOf(cost));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
