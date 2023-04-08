public class Lecturer extends Thread {

    public boolean LectRunning = false;
    public String lecturerName;
    public Classroom obj;
    public Student array[];

    // Constructor
    Lecturer(String lecturerName, Classroom obj, Student array[]) {
        this.lecturerName = lecturerName;
        if (obj.getStudentAndVisitorSemaphore().availablePermits() > 0) {
            this.obj = obj;
        }
        this.array = array;
    }

    public boolean getIsLectureRunning() {
        return this.LectRunning;
    }

    // Enter class function
    public void enter() {
        // Checks whether classroom is full. If not full lecturer can enter
        if (obj.getLecturerSemaphore().availablePermits() > 0) {
            try {
                this.obj.lecturer = lecturerName;
                obj.getLecturerSemaphore().acquire();// Semaphore acquired
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Start lecture function
    public void startLecture() {
        int count = 0;
        // Loop to count the number of students sitting in class
        for (Student stu : array) {
            if (stu.obj != null && obj != null) {
                if (stu.obj.className == obj.className) {
                    if (stu.isSitting) {
                        count++;
                    }
                }
            }
        }

        // Lecture starts if all students are sitting
        if (obj != null) {
            if (obj.filled == count) {
                this.obj.LectRunning = true;
                LectRunning = true;
            }
        }
    }

    // End lecture function
    public void leave() {
        synchronized (obj) {
            if (obj.LectRunning) {
                obj.LectRunning = false;
                obj.lecturer = "";
                obj.notifyAll(); // Notify waiting threads
            }
        }
    }
    

    // Override run method of Thread class
    @Override
    public void run() {
        enter();
    }
}