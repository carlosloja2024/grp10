import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public client(String address, int port) throws IOException {
        socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(String message) throws IOException {
        out.writeObject(message);
    }

    public String receiveMessage() throws IOException, ClassNotFoundException {
        return (String) in.readObject();
    }

    public static void main(String[] args) {
        try {
            client client = new client("localhost", 12345);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while ((message = reader.readLine()) != null) {
                client.sendMessage(message);

                if (message.startsWith("Guardar Juego") || message.startsWith("Cargar Juego")) {
                    System.out.println("Enviado.");
                } else {
                    String response = client.receiveMessage();
                    System.out.println(response);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
