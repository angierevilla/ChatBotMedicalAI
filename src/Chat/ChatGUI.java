package Chat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatGUI {

    private JPanel mainPanel;
    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private JTextField inputField;
    private JButton sendButton;
    private JButton uploadButton;
    private boolean fileUploaded; // Variable para rastrear si se ha subido un archivo
    private String[] botGeneralResponses; // Arreglo de respuestas generales del bot
    private Map<String, String> keywordResponses; // Mapa de palabras clave y respuestas
    private Random random; // Generador de números aleatorios

    public ChatGUI() {
        // Configuración de la ventana principal
        JFrame frame = new JFrame("Chat Bot");
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(600,400));

        // Panel para mostrar los mensajes (chat panel)
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS)); // Usar BoxLayout para alinear los globos verticalmente
        chatPanel.setBackground(Color.BLACK);

        // Añadir un JScrollPane para que el área de chat sea desplazable
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        addChatBubble("Bienvenido, sube el archivo de tu historial clínico", new Color(173, 216, 230), FlowLayout.RIGHT); // Globo del usuario (color celeste)

        // Panel para el campo de entrada y botones
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputField = new JTextField(20);
        sendButton = new JButton("Enviar");
        uploadButton = new JButton(); // Cambia el path al icono deseado
        uploadButton.setText("Sube archivo");
        uploadButton.setToolTipText("Subir archivo");

        // Inicializar fileUploaded como false
        fileUploaded = false;

        // Inicializar respuestas generales del bot
        botGeneralResponses = new String[]{
                "Recuerda llevar una dieta balanceada.",
                "Es importante descansar adecuadamente.",
                "Si tienes dudas, consulta a un profesional.",
                "Recuerda hacer chequeos periódicos.",
                "Mantén una vida activa para mejorar tu salud física.",
                "La salud mental también es importante, busca apoyo si lo necesitas."
        };

        // Inicializar palabras clave y respuestas específicas del médico
        keywordResponses = new HashMap<>();
        keywordResponses.put("dolor", "¿Dónde sientes el dolor y qué tan intenso es?");
        keywordResponses.put("fiebre", "Es importante mantener la hidratación, ¿has tomado algún antipirético?");
        keywordResponses.put("tos", "¿Hace cuánto tienes la tos? ¿Es seca o con flema?");
        keywordResponses.put("cansancio", "El cansancio puede ser causado por varias razones, ¿has estado durmiendo bien?");
        keywordResponses.put("mareo", "Los mareos pueden estar relacionados con varios factores, es recomendable que te sientes o recuestes y te hidrates.");
        keywordResponses.put("gracias", "De nada, siempre estoy aquí para ayudarte.");

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
        String message = inputField.getText().trim();
        if (!fileUploaded) {
            JOptionPane.showMessageDialog(mainPanel, "Por favor, suba un archivo antes de enviar un mensaje.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!message.isEmpty()) {
            addChatBubble("Tú: " + message, new Color(173, 216, 230), FlowLayout.RIGHT); // Globo del usuario (color celeste)
            inputField.setText(""); // Limpiar el campo de entrada

            // Responder basado en palabras clave
            String botResponse = getBotResponse(message);
            addChatBubble("medicoBOT: " + botResponse, new Color(211, 211, 211), FlowLayout.LEFT); // Globo del bot (color gris claro)
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Por favor, ingrese un mensaje.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getBotResponse(String userMessage) {
        // Recorrer las palabras clave para buscar una coincidencia
        for (String keyword : keywordResponses.keySet()) {
            if (userMessage.toLowerCase().contains(keyword)) {
                return keywordResponses.get(keyword);
            }
        }
        // Si no se encuentra una palabra clave, dar una respuesta general aleatoria
        return botGeneralResponses[random.nextInt(botGeneralResponses.length)];
    }

    private void addChatBubble(String message, Color color, int alignment) {
        JPanel bubblePanel = new JPanel(new FlowLayout(alignment)); // Panel para el globo de chat
        JLabel messageLabel = new JLabel("<html><p style='width: 150px;'>" + message + "</p></html>"); // Envolver el texto para el ajuste de línea
        messageLabel.setOpaque(true);
        messageLabel.setBackground(color);
        messageLabel.setBorder(new LineBorder(Color.BLACK, 1, true)); // Borde redondeado
        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Espacio alrededor del texto
        bubblePanel.add(messageLabel);

        chatPanel.add(bubblePanel);
        chatPanel.revalidate(); // Refrescar el panel de chat para mostrar el nuevo mensaje
        chatPanel.repaint(); // Repintar el área para reflejar los cambios

        // Desplazarse automáticamente hacia el final del JScrollPane
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    private void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(mainPanel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            addChatBubble("Historial subido correctamente: " + selectedFile.getName(), new Color(173, 216, 230), FlowLayout.RIGHT);
            fileUploaded = true; // Marcar que se ha subido un archivo
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatGUI());
    }
}
