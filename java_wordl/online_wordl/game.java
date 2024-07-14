import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class game implements Serializable {
    private List<String> guesses;
    private String wordToGuess;
    private List<String> dictionary;

    public game() {
        this.guesses = new ArrayList<>();
        loadDictionary("C:/Users/Laptop/Desktop/Carlos Loja UEES/Sistemas_Distribuidos/ProyectoSDistribuido2024/grp10/java_wordl/online_wordl/src/diccionario.txt");
        selectRandomWord();
        wordl_UI ui = new wordl_UI(this);
        ui.setVisible(true);
        System.out.println("Juego creado");
    }

    // Remove the duplicate method makeGuess(String)

    public String getGameState() {
        StringBuilder sb = new StringBuilder();
        for (String guess : guesses) {
            sb.append(guess).append("\n");
        }
        return sb.toString();
    }

    public void saveGame(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
            System.out.println("Avance Guardado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static game loadGame(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (game) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void loadDictionary(String fileName) {
        dictionary = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectRandomWord() {
    Random random = new Random();
    wordToGuess = dictionary.get(random.nextInt(dictionary.size()));
    }

    public void makeGuess(String guess) {
        guesses.add(guess);
    }
}
