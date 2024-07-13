import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class server {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(25);
        System.out.println("Servidor inicia en el puerto 25.");

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private game game;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.game = new game();
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                String message;
                while ((message = (String) in.readObject()) != null) {
                    if (message.startsWith("Guardar Juego")) {
                        String fileName = message.split(" ")[1];
                        game.saveGame(fileName);
                    } else if (message.startsWith("Cargar Juego")) {
                        String fileName = message.split(" ")[1];
                        game = game.loadGame(fileName);
                    } else {
                        game.makeGuess(message);
                        out.writeObject(game.getGameState());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
