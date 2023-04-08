import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class Execute implements Runnable {

    public static Random rand;
    public static Classroom W201;
    public static Classroom W202;
    public static Classroom W101;
    public static Classroom W102;
    public static Classroom classrooms[] = new Classroom[4];
    public static Visitor visitors[] = new Visitor[5];
    public static Student students[] = new Student[90];
    public static Lecturer lecturers[] = new Lecturer[6];

    // function to create and execute different threads

    public static void prep() {
        rand = new Random();
        // Define classrooms
        Classroom[] classrooms = {
                new Classroom(20, "W101", 20),
                new Classroom(30, "W102", 30),
                new Classroom(60, "W201", 60),
                new Classroom(60, "W202", 60)
        };

        // Define visitors
        Visitor[] visitors = new Visitor[5];
        for (int i = 0; i < visitors.length; i++) {
            visitors[i] = new Visitor(classrooms[rand.nextInt(classrooms.length)]);
        }

        // Define lecturers
        Lecturer[] lecturers = new Lecturer[6];
        String[] lecturerNames = { "Osama", "Barry", "Faheem", "Alex", "Aqeel", "Waseem" };
        IntStream.range(0, lecturers.length)
                .forEach(i -> lecturers[i] = new Lecturer(lecturerNames[i], classrooms[rand.nextInt(classrooms.length)],
                        students));

        // Loop where students enter class
        try {
            Thread[] studentThreads = new Thread[students.length];
            for (int i = 0; i < students.length; i++) {
                students[i] = new Student(i, classrooms[rand.nextInt(4)]);
                studentThreads[i] = new Thread(students[i]);
                studentThreads[i].start();
            }

            // Wait for all student threads to finish
            for (int i = 0; i < studentThreads.length; i++) {
                try {
                    studentThreads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Loop where visitors enter class
            for (Visitor visitor : visitors) {
                visitor.start();
            }

            // Loop where lecturer enter class
            List<Thread> lecturerThreads = new ArrayList<>();

            for (Lecturer lecturer : lecturers) {
                Thread thread = new Thread(() -> {
                    lecturer.start();
                    lecturer.startLecture();
                });
                thread.start();
                lecturerThreads.add(thread);
            }

            for (Thread thread : lecturerThreads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Monitor thread for printing status of classes
            Monitor monitor = new Monitor(classrooms);
            monitor.start();
            monitor.join();
            // Wait for all lecturer threads to finish and then signal that they have left
            for (Lecturer lecturer : lecturers) {
                lecturer.join();
                lecturer.leave();
            }

            // Release loop
            for (Classroom classroom : classrooms) {
                if (!classroom.LectRunning) {
                    for (Student student : students) {
                        if (!student.isSitting) {
                            student.leave();
                        }
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    public boolean flag = true; // Flag to end loop upon interrupt

    // stopThread method changes the value of flag to false
    public void stopThread() {
        flag = false;
    }

    @Override
    public void run() {
        System.out.println("=======================================================================");
        System.out.println("Classroom\tLecturer\tInSession\tStudents\tVisitor");
        System.out.println("=======================================================================");

        while (flag) {
            try {
                prep();
                TimeUnit.SECONDS.sleep(2); // Pause execution of thread for 2 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status on catch
            }
        }
    }

}

class College {
    public static void main(String[] args) throws IOException, InterruptedException {
        Execute object = new Execute();
        Thread execThread = new Thread(object);
        execThread.start();

        System.in.read(); // Waits for user input
        object.stopThread();
        execThread.join();

        System.out.println("Stopped!");
    }

}