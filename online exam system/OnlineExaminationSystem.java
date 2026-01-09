import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class OnlineExaminationSystem extends JFrame {
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;
    private static final int EXAM_DURATION = 300; // 5 minutes in seconds
    
    // Exam data
    private String[] questions = {
        "What is the capital of France?",
        "Which planet is closest to the Sun?",
        "What is the largest ocean on Earth?",
        "Who wrote 'Romeo and Juliet'?",
        "What is the chemical symbol for Gold?"
    };
    
    private String[][] options = {
        {"Paris", "London", "Berlin", "Madrid"},
        {"Venus", "Mercury", "Mars", "Jupiter"},
        {"Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"},
        {"William Shakespeare", "Jane Austen", "Charles Dickens", "Mark Twain"},
        {"Au", "Go", "Gd", "Ag"}
    };
    
    private int[] correctAnswers = {0, 1, 2, 0, 0};
    
    // UI Components
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // User data
    private String studentName;
    private int[] selectedAnswers;
    private int currentQuestionIndex;
    private int timeRemaining;
    private Timer examTimer;
    
    // Screens
    private JPanel startScreen;
    private JPanel examScreen;
    private JPanel resultScreen;
    
    public OnlineExaminationSystem() {
        setTitle("Online Examination System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        createStartScreen();
        createExamScreen();
        
        cardPanel.add(startScreen, "START");
        cardPanel.add(examScreen, "EXAM");
        
        add(cardPanel);
        setVisible(true);
    }
    
    private void createStartScreen() {
        startScreen = new JPanel();
        startScreen.setBackground(new Color(245, 245, 245));
        startScreen.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        // Title
        JLabel titleLabel = new JLabel("Online Examination System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(33, 33, 33));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        startScreen.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Welcome to the Online Examination Platform");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        startScreen.add(subtitleLabel, gbc);
        
        // Student Name Label
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        startScreen.add(nameLabel, gbc);
        
        // Student Name Input
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(250, 35));
        startScreen.add(nameField, gbc);
        
        // Exam Info Panel
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(new LineBorder(new Color(200, 220, 240), 2));
        infoPanel.setPreferredSize(new Dimension(400, 120));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel infoTitle = new JLabel("Exam Details:");
        infoTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        infoPanel.add(infoTitle);
        
        JLabel infoText1 = new JLabel("• Total Questions: " + questions.length);
        infoText1.setFont(new Font("SansSerif", Font.PLAIN, 11));
        infoPanel.add(infoText1);
        
        JLabel infoText2 = new JLabel("• Duration: " + (EXAM_DURATION / 60) + " minutes");
        infoText2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        infoPanel.add(infoText2);
        
        JLabel infoText3 = new JLabel("• Question Type: Multiple Choice");
        infoText3.setFont(new Font("SansSerif", Font.PLAIN, 11));
        infoPanel.add(infoText3);
        
        startScreen.add(infoPanel, gbc);
        
        // Start Button
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JButton startButton = new JButton("Start Exam");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        startButton.setPreferredSize(new Dimension(200, 45));
        startButton.setBackground(new Color(66, 133, 244));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(new LineBorder(new Color(66, 133, 244), 1));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        startButton.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name", "Validation Error", JOptionPane.WARNING_MESSAGE);
            } else {
                startExam(nameField.getText().trim());
            }
        });
        
        startScreen.add(startButton, gbc);
    }
    
    private void createExamScreen() {
        examScreen = new JPanel(new BorderLayout());
        examScreen.setBackground(new Color(245, 245, 245));
        
        // Top panel with timer
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Online Examination System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 33, 33));
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel timerLabel = new JLabel("Time: 05:00");
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        timerLabel.setForeground(new Color(200, 0, 0));
        topPanel.add(timerLabel, BorderLayout.EAST);
        
        examScreen.add(topPanel, BorderLayout.NORTH);
        
        // Center panel for questions
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(new EmptyBorder(20, 80, 20, 80));
        centerPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Question panel
        JPanel questionPanel = new JPanel();
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        
        JLabel questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        questionNumberLabel.setForeground(new Color(100, 100, 100));
        questionPanel.add(Box.createVerticalStrut(10));
        questionPanel.add(questionNumberLabel);
        
        JLabel questionTextLabel = new JLabel();
        questionTextLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        questionTextLabel.setForeground(new Color(33, 33, 33));
        questionTextLabel.setMaximumSize(new Dimension(600, 100));
        questionTextLabel.setPreferredSize(new Dimension(600, 60));
        questionPanel.add(Box.createVerticalStrut(10));
        questionPanel.add(questionTextLabel);
        questionPanel.add(Box.createVerticalStrut(15));
        
        // Options panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton[] radioButtons = new JRadioButton[4];
        
        for (int i = 0; i < 4; i++) {
            radioButtons[i] = new JRadioButton();
            radioButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 14));
            radioButtons[i].setBackground(Color.WHITE);
            radioButtons[i].setForeground(new Color(50, 50, 50));
            radioButtons[i].setBorder(new EmptyBorder(8, 8, 8, 8));
            final int index = i;
            radioButtons[i].addActionListener(e -> {
                if (selectedAnswers != null && index < selectedAnswers.length) {
                    selectedAnswers[currentQuestionIndex] = index;
                }
            });
            buttonGroup.add(radioButtons[i]);
            optionsPanel.add(radioButtons[i]);
            optionsPanel.add(Box.createVerticalStrut(5));
        }
        
        questionPanel.add(optionsPanel);
        questionPanel.add(Box.createVerticalStrut(10));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        centerPanel.add(questionPanel, gbc);
        
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBackground(new Color(245, 245, 245));
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));
        scrollPane.setBorder(null);
        examScreen.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JButton previousButton = createStyledButton("Previous", new Color(158, 158, 158));
        previousButton.addActionListener(e -> previousQuestion());
        
        JButton nextButton = createStyledButton("Next", new Color(66, 133, 244));
        nextButton.addActionListener(e -> nextQuestion());
        
        JButton submitButton = createStyledButton("Submit Exam", new Color(244, 81, 30));
        submitButton.addActionListener(e -> submitExam());
        
        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);
        
        examScreen.add(bottomPanel, BorderLayout.SOUTH);
        
        examScreen.putClientProperty("timerLabel", timerLabel);
        examScreen.putClientProperty("questionNumberLabel", questionNumberLabel);
        examScreen.putClientProperty("questionTextLabel", questionTextLabel);
        examScreen.putClientProperty("radioButtons", radioButtons);
        examScreen.putClientProperty("buttonGroup", buttonGroup);
        examScreen.putClientProperty("previousButton", previousButton);
        examScreen.putClientProperty("nextButton", nextButton);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(color, 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void startExam(String name) {
        studentName = name;
        selectedAnswers = new int[questions.length];
        for (int i = 0; i < selectedAnswers.length; i++) {
            selectedAnswers[i] = -1;
        }
        currentQuestionIndex = 0;
        timeRemaining = EXAM_DURATION;
        
        updateExamScreen();
        cardLayout.show(cardPanel, "EXAM");
        startTimer();
    }
    
    private void startTimer() {
        examTimer = new Timer(1000, e -> {
            timeRemaining--;
            updateTimer();
            
            if (timeRemaining <= 0) {
                examTimer.stop();
                submitExam();
            }
        });
        examTimer.start();
    }
    
    private void updateTimer() {
        JLabel timerLabel = (JLabel) examScreen.getClientProperty("timerLabel");
        if (timerLabel != null) {
            int minutes = timeRemaining / 60;
            int seconds = timeRemaining % 60;
            timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
            
            if (timeRemaining <= 60) {
                timerLabel.setForeground(new Color(255, 0, 0));
            }
        }
    }
    
    private void updateExamScreen() {
        JLabel questionNumberLabel = (JLabel) examScreen.getClientProperty("questionNumberLabel");
        JLabel questionTextLabel = (JLabel) examScreen.getClientProperty("questionTextLabel");
        JRadioButton[] radioButtons = (JRadioButton[]) examScreen.getClientProperty("radioButtons");
        ButtonGroup buttonGroup = (ButtonGroup) examScreen.getClientProperty("buttonGroup");
        JButton previousButton = (JButton) examScreen.getClientProperty("previousButton");
        JButton nextButton = (JButton) examScreen.getClientProperty("nextButton");
        
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.length);
        questionTextLabel.setText("<html>" + questions[currentQuestionIndex] + "</html>");
        
        String[] currentOptions = options[currentQuestionIndex];
        for (int i = 0; i < 4; i++) {
            radioButtons[i].setText(currentOptions[i]);
            radioButtons[i].setSelected(selectedAnswers[currentQuestionIndex] == i);
        }
        
        previousButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < questions.length - 1);
        
        // Reset button group selection
        buttonGroup.clearSelection();
        if (selectedAnswers[currentQuestionIndex] >= 0) {
            radioButtons[selectedAnswers[currentQuestionIndex]].setSelected(true);
        }
    }
    
    private void previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            updateExamScreen();
        }
    }
    
    private void nextQuestion() {
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            updateExamScreen();
        }
    }
    
    private void submitExam() {
        if (examTimer != null) {
            examTimer.stop();
        }
        
        int correctCount = 0;
        for (int i = 0; i < questions.length; i++) {
            if (selectedAnswers[i] == correctAnswers[i]) {
                correctCount++;
            }
        }
        
        showResultScreen(correctCount);
    }
    
    private void showResultScreen(int correctCount) {
        if (resultScreen != null) {
            cardPanel.remove(resultScreen);
        }
        
        resultScreen = new JPanel();
        resultScreen.setBackground(new Color(245, 245, 245));
        resultScreen.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Title
        JLabel titleLabel = new JLabel("Examination Completed");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(33, 33, 33));
        gbc.gridx = 0;
        gbc.gridy = 0;
        resultScreen.add(titleLabel, gbc);
        
        // Result Panel
        gbc.gridy = 1;
        JPanel resultPanel = new JPanel();
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(new LineBorder(new Color(220, 220, 220), 1));
        resultPanel.setPreferredSize(new Dimension(400, 250));
        resultPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints resultGbc = new GridBagConstraints();
        resultGbc.insets = new Insets(15, 15, 15, 15);
        resultGbc.gridx = 0;
        
        JLabel studentLabel = new JLabel("Student Name: " + studentName);
        studentLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultGbc.gridy = 0;
        resultPanel.add(studentLabel, resultGbc);
        
        JLabel totalLabel = new JLabel("Total Questions: " + questions.length);
        totalLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultGbc.gridy = 1;
        resultPanel.add(totalLabel, resultGbc);
        
        JLabel correctLabel = new JLabel("Correct Answers: " + correctCount);
        correctLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        correctLabel.setForeground(new Color(34, 139, 34));
        resultGbc.gridy = 2;
        resultPanel.add(correctLabel, resultGbc);
        
        int wrongCount = questions.length - correctCount;
        JLabel wrongLabel = new JLabel("Wrong Answers: " + wrongCount);
        wrongLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        wrongLabel.setForeground(new Color(255, 0, 0));
        resultGbc.gridy = 3;
        resultPanel.add(wrongLabel, resultGbc);
        
        double percentage = (correctCount * 100.0) / questions.length;
        JLabel scoreLabel = new JLabel(String.format("Final Score: %.2f%%", percentage));
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        scoreLabel.setForeground(new Color(66, 133, 244));
        resultGbc.gridy = 4;
        resultGbc.insets = new Insets(20, 15, 15, 15);
        resultPanel.add(scoreLabel, resultGbc);
        
        gbc.gridy = 1;
        resultScreen.add(resultPanel, gbc);
        
        // Exit Button
        gbc.gridy = 2;
        JButton exitButton = createStyledButton("Exit", new Color(66, 133, 244));
        exitButton.setPreferredSize(new Dimension(150, 40));
        exitButton.addActionListener(e -> System.exit(0));
        resultScreen.add(exitButton, gbc);
        
        cardPanel.add(resultScreen, "RESULT");
        cardLayout.show(cardPanel, "RESULT");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OnlineExaminationSystem());
    }
}
