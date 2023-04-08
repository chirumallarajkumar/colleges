import java.util.concurrent.Semaphore;

class Classroom {
    // Classroom variables
    public String className;
    public int capacity;
    public String lecturer;
    private Semaphore llecturerSemaphore = new Semaphore(1);
    private Semaphore StudentAndVisitorSemaphore;
    public boolean LectRunning = false;
    public int filled = 0;
    public int filledVisitor = 0;

    // Constructor
    public Classroom(int permit, String className, int capacity) {
        this.StudentAndVisitorSemaphore = new Semaphore(permit);
        this.className = className;
        this.capacity = capacity;
    }

    // Check if class is full
    public boolean classIsFull() {
        if (capacity == filled) {
            return true;
        }
        return false;
    }

    public String getClassName() {
        return this.className;
    }

    // Getter method for semaphore
    public Semaphore getLecturerSemaphore() {
        return this.llecturerSemaphore;
    }

    // Getter method for semaphore
    public Semaphore getStudentAndVisitorSemaphore() {
        return this.StudentAndVisitorSemaphore;
    }
}