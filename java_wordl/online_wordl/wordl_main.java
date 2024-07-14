import javax.swing.SwingUtilities;

public class wordl_main {
    public static void main(String[] args) {
        System.out.println("Arrancando Wordle...");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Iniciando Juego...");
                game game = new game();
                System.out.println("Juego iniciado...");
            }
        });
    }
}