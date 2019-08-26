import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class StudentPriorityQueue {

    /*
     * Priority queue implementation of students.
     * Students have a unique string student_id, unique GPA
     * Higher GPA corresponds to higher priority
     *
     * Input format:
     * n descirbing the total number of events
     * n lines of the two following forms: "ADD_STUDENT id GPA" and "INVITE"
     *
     * "ADD_STUDENT id GPA" adds the student to the priority queue
     * "INVITE" invites the student in the queue with the highest priority
     *
     * this implementation can be easily adapted to fit any priority queue, not limited to just student invitations
     * 
     * Outputs the ids of the invited students
     */

	public static ArrayList<Student> createPriorityQueue() {
        ArrayList<Student> priority_queue = new ArrayList<Student>();
        return priority_queue;
    }

    public static void addStudent(ArrayList<Student> priority_queue, String string, double gpa) {
        int i = priority_queue.size();
        Student newStudent = new Student(string, gpa);
        priority_queue.add(newStudent);
        while (i != 0) {
            int parentIndex = (i-1)/2;
            Student parentStudent = priority_queue.get(parentIndex);
            if (parentStudent.gpa < newStudent.gpa) {
                priority_queue.set(parentIndex, newStudent);
                priority_queue.set(i, parentStudent);
                i = parentIndex;
            } else break;
        }
    }

  public static String invite(ArrayList<Student> priority_queue) {
        int i = priority_queue.size() - 1;
        Student maxStudent = priority_queue.get(0);
        priority_queue.set(0, priority_queue.get(i));
        Student targetStudent = priority_queue.get(i);
        priority_queue.remove(i);
        int size = i;
        i = 0;
        while ( ( (2 * i) + 1 ) < size ) {
            Student leftChild = priority_queue.get(2 * i + 1);
            if ( ( (2 * i) + 2 ) < size ) {
                Student rightChild = priority_queue.get(2 * i + 2);
                if (targetStudent.gpa < leftChild.gpa) {
                    if (leftChild.gpa < rightChild.gpa) {
                        priority_queue.set(i, rightChild);
                        i = 2 * i + 2;
                        priority_queue.set(i, targetStudent);
                        continue;
                    } else {
                        priority_queue.set(i, leftChild);
                        i = 2 * i + 1;
                        priority_queue.set(i, targetStudent);
                        continue;
                    }
                } else if (targetStudent.gpa < rightChild.gpa) {
                    priority_queue.set(i, rightChild);
                    i = 2 * i + 2;
                    priority_queue.set(i, targetStudent);
                    continue;
                } else {
                    break;
                }
            } else if (targetStudent.gpa < leftChild.gpa) {
                priority_queue.set(i, leftChild);
                i = 2 * i + 1;
                priority_queue.set(i, targetStudent);
                continue;
            } else {
                break;
            }
        }
        return maxStudent.name;
    }

    public static class Student {
        
        public double gpa;
        public String name;
        
        public Student(String name, double gpa) {
            this.gpa = gpa;
            this.name = name;
        }
        
	}

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

		int n = Integer.parseInt(bufferedReader.readLine().trim());

		List<String> inputs = IntStream.range(0, n).mapToObj(i -> {
			try {
				return bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).filter(x -> x != null).map(Object::toString).collect(toList());

		ArrayList<Student> priority_queue = createPriorityQueue();

		int invites = 0;
		for (String line : inputs) {
			String[] parts = line.split(" ");
			if (parts[0].equals("ADD_STUDENT")) {
				addStudent(priority_queue, parts[1], Double.parseDouble(parts[2]));
			} else {
				invite(priority_queue);
				invites += 1;
			}
		}
		bufferedWriter.write(IntStream.range(0, n - 2 * invites).mapToObj(i -> invite(priority_queue))
				.map(Object::toString).collect(joining("\n")) + "\n");

		bufferedReader.close();
		bufferedWriter.close();
	}
}