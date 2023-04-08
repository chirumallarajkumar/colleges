public class Monitor extends Thread {

    private final Classroom[] classroom;

public Monitor(Classroom[] classroom) {
    this.classroom = classroom;
}

    // Run method for monitor class
    @Override
    public void run() {

        StringBuilder sb = new StringBuilder();
        for (Classroom room : classroom) {
            sb.append(room.className).append("\t\t");
        
            if (room.lecturer != null) {
                sb.append(room.lecturer);
                sb.append("\t\t").append(room.LectRunning).append("\t\t");
            }
            else{
                sb.append(room.LectRunning).append("\t\t");
            }
            
            if (room.lecturer != null) {
                sb.append(room.filled).append("\t\t").append(room.filledVisitor);
            }
        
            sb.append("\n");
        }
        System.out.print(sb.toString());
        
    }
}