
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Random;

public class CLIENTES4_T_01V2 extends JFrame {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 28;

    private JTextArea textArea;
    private JProgressBar progressBar;

    public CLIENTES4_T_01V2() {
        setTitle("Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH);

        setVisible(true);

        // Iniciar la conexión al servidor en un hilo separado
        new Thread(this::connectToServer).start();
    }

    @SuppressWarnings("unchecked")
    private void connectToServer() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            textArea.append("Conectado al servidor\n");

            // Generar un número aleatorio y enviarlo al servidor
            int randomNumber = new Random().nextInt(100);
            textArea.append("Enviando número al servidor: " + randomNumber + "\n");
            out.writeInt(randomNumber);
            out.flush();

            // Recibir la lista actualizada del servidor
            List<Integer> updatedList = (List<Integer>) in.readObject();
            textArea.append("Lista recibida del servidor: " + updatedList + "\n");

            // Actualizar la barra de progreso
            progressBar.setValue(100);
            progressBar.setString("Proceso Completo");
        } catch (IOException | ClassNotFoundException e) {
            textArea.append("Error: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CLIENTES4_T_01V2::new);
    }
}
