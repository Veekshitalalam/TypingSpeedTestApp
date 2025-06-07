import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class TypingSpeedTest extends JFrame {

    private JTextArea textToTypeArea;
    private JTextArea typingArea;
    private JLabel wpmLabel;
    private JButton startButton;
    private Timer timer;
    private int secondsElapsed;
    private boolean testRunning = false;

    // Sample text
    private final String sampleText = "The quick brown fox jumps over the lazy dog.";

    public TypingSpeedTest() {
        setTitle("Typing Speed Test");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textToTypeArea = new JTextArea(sampleText);
        textToTypeArea.setLineWrap(true);
        textToTypeArea.setWrapStyleWord(true);
        textToTypeArea.setEditable(false);
        textToTypeArea.setFont(new Font("Serif", Font.PLAIN, 18));

        typingArea = new JTextArea();
        typingArea.setLineWrap(true);
        typingArea.setWrapStyleWord(true);
        typingArea.setFont(new Font("Serif", Font.PLAIN, 18));
        typingArea.setEnabled(false);

        wpmLabel = new JLabel("WPM: 0");

        startButton = new JButton("Start Test");

        startButton.addActionListener(e -> startTest());

        typingArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (testRunning) updateStats();
            }
        });

        add(textToTypeArea, BorderLayout.NORTH);
        add(new JScrollPane(typingArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(wpmLabel, BorderLayout.WEST);
        bottomPanel.add(startButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void startTest() {
        typingArea.setText("");
        typingArea.setEnabled(true);
        typingArea.requestFocus();
        secondsElapsed = 0;
        testRunning = true;
        wpmLabel.setText("WPM: 0");

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                secondsElapsed++;
                SwingUtilities.invokeLater(() -> updateStats());
            }
        }, 1000, 1000);
    }

    private void updateStats() {
        String typed = typingArea.getText();
        int wordsTyped = typed.trim().split("\\s+").length;
        double minutes = secondsElapsed / 60.0;
        int wpm = minutes > 0 ? (int)(wordsTyped / minutes) : 0;
        wpmLabel.setText("WPM: " + wpm);

        if (typed.equals(sampleText)) {
            testRunning = false;
            timer.cancel();
            JOptionPane.showMessageDialog(this, "Test Completed!\nYour WPM: " + wpm);
            typingArea.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TypingSpeedTest().setVisible(true);
        });
    }
}
