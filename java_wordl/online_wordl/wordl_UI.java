import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class wordl_UI extends JFrame {
    private game game;
    private JTextField inputField;
    private JTextArea gameArea;

    public wordl_UI(game game) {
        this.game = game;
        initUI();
    }

    private void initUI() {
        setTitle("Wordle");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        gameArea = new JTextArea();
        gameArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gameArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String guess = inputField.getText();
                inputField.setText("");
                processGuess(guess);
            }
        });

        panel.add(inputField, BorderLayout.SOUTH);
        add(panel);
    }

    private void processGuess(String guess) {
        game.makeGuess(guess);
        updateGameArea();
    }

    private void updateGameArea() {
        gameArea.setText(game.getGameState());
    }
}
