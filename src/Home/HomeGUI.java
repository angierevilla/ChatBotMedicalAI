package Home;

import Chat.ChatGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class HomeGUI {
    private JPanel mainHome; // Ensure this field matches your form
    private JButton button1;
    private JTextField ingreseDNITextField;
    private JTextArea esteEsUnChatTextArea;

    public HomeGUI() {
        // Initialize the GUI components here
        JFrame frame = new JFrame("Chat Bot Medical AI");
        frame.setContentPane(mainHome); // Assuming mainHome is your main panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack(); // Adjusts the frame size to fit its components
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true); // Make the frame visible

        setPlaceholder(ingreseDNITextField, "my home");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String  valor = ingreseDNITextField.getText();
                if (valor != null && valor.matches("[0-9]+") && valor.length() == 8) {
                    new ChatGUI();
                    // Opcionalmente, cerrar la ventana actual
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainHome);
                    topFrame.dispose();
                }
                else if (valor == null) {
                    JOptionPane.showMessageDialog(mainHome, "Por favor, ingrese un dni.", "Error en el Ingreso de DNI vacio", JOptionPane.ERROR_MESSAGE);
                }
                else if (valor.length() != 8) {
                    JOptionPane.showMessageDialog(mainHome, "Por favor, ingrese un dni valido.(8 digitos)", "Error en el Ingreso de DNI", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(mainHome, "Por favor, ingrese solo números.", "Error en el Ingreso de DNI", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY); // Set placeholder color

        textField.setText(placeholder); // Set placeholder text
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText(""); // Clear placeholder on focus
                    textField.setForeground(Color.BLACK); // Change text color
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY); // Reset placeholder color
                    textField.setText(placeholder); // Restore placeholder
                }
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomeGUI(); // Create an instance of HomeGUI to display it
            }
        });
    }
}
