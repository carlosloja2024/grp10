import javax.swing.SwingUtilities;

public class wordl_main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                game game = new game(); 
            }
        });
    }
}
