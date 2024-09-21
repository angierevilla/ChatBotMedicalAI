package Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

public class ChatGUI {

    private JPanel mainPanel;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton uploadButton;
    private boolean fileUploaded; // Variable para rastrear si se ha subido un archivo
    private String[] botResponses; // Arreglo de respuestas del bot
    private Random random; // Generador de números aleatorios

    public ChatGUI() {
        // Configuración de la ventana principal
        JFrame frame = new JFrame("Chat Bot");
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Área de texto para mostrar el chat
        chatArea = new JTextArea(15, 30);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel para el campo de entrada y botones
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(20);
        sendButton = new JButton("Enviar");
        uploadButton = new JButton(new ImageIcon("resources/archive.png")); // Cambia el path al icono deseado
        uploadButton.setText("Sube archivo");
        uploadButton.setToolTipText("Subir archivo");

        // Inicializar fileUploaded como false
        fileUploaded = false;

        // Inicializar respuestas del bot
        botResponses = new String[]{
                "¿Cómo puedo ayudarte hoy?",
                "Recuerda beber suficiente agua.",
                "Es importante descansar adecuadamente.",
                "Siempre es bueno hacer ejercicio.",
                "Si tienes dudas, consulta a un profesional.",
                "La salud mental es tan importante como la física."
        };
        random = new Random();

        // Agregar ActionListener al botón de enviar
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Agregar ActionListener al campo de entrada para detectar "Enter"
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Agregar ActionListener al botón de subir archivo
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadFile();
            }
        });

        // Agregar los componentes al panel de entrada
        inputPanel.add(inputField);
        inputPanel.add(sendButton);
        inputPanel.add(uploadButton);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Configurar el JFrame
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!fileUploaded) {
            JOptionPane.showMessageDialog(mainPanel, "Por favor, suba un archivo antes de enviar un mensaje.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!message.trim().isEmpty()) {
            chatArea.append("Tú: " + message + "\n"); // Mostrar el mensaje en el área de chat
            inputField.setText(""); // Limpiar el campo de entrada

            // Respuesta del medicoBOT
            String botResponse = botResponses[random.nextInt(botResponses.length)];
            chatArea.append("medicoBOT: " + botResponse + "\n"); // Mostrar la respuesta del bot
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Por favor, ingrese un mensaje.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(mainPanel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            chatArea.append("Archivo subido: " + selectedFile.getName() + "\n");
            fileUploaded = true; // Marcar que se ha subido un archivo
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatGUI());
    }
}
