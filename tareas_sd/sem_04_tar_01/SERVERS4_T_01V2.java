import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SERVERS4_T_01V2 extends JFrame {
    private static final int PORT = 28;
    private static List<Integer> sharedList = new ArrayList<>();
    private static Lock listLock = new ReentrantLock();
    private JTextArea textArea;

    public SERVERS4_T_01V2() {
        setTitle("Servidor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);

        new Thread(this::startServer).start();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            textArea.append("Servidor iniciado en el puerto " + PORT + "\n");
            textArea.append("Esperando conexiones...\n");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                textArea.append("Cliente conectado: " + clientSocket.getInetAddress() + "\n");

                // Manejar la conexión del cliente en un nuevo hilo
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
            ) {
                // Leer el dato numérico proporcionado por el cliente
                int clientNumber = in.readInt();
                textArea.append("Número recibido del cliente: " + clientNumber + "\n");

                // Añadir el dato a la lista compartida
                listLock.lock();
                try {
                    sharedList.add(clientNumber);
                } finally {
                    listLock.unlock();
                }

                // Enviar la lista actualizada al cliente
                out.writeObject(sharedList);
                out.flush();

                textArea.append("Lista actualizada enviada al cliente: " + sharedList + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SERVERS4_T_01V2::new);
    }
}
