import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class VentanaIngreso {
    private JTextField textStock;
    private JTextField textColor;
    private JTextField textPrecio;
    private JTextField textYear;
    private JTextField textMarca;
    private JTextField textModelo;
    private JButton btnRegresar;
    private JButton btnAgregar;
    private JTable table1;
    public JPanel VentanaIngreso;

    public VentanaIngreso() {
        /* Declaración del modelo de la tabla */
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{"ID", "MARCA", "MODELO", "AÑO", "PRECIO", "COLOR", "STOCK"});
        table1.setModel(modelo);

        btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una instancia de la ventana VentanaCarros
                JFrame CarrosFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaIngreso);
                CarrosFrame.dispose();

                JFrame loginFrame = new JFrame("Carros");
                loginFrame.setContentPane(new VentanaCarros().VentanaOne);
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(800, 600);
                loginFrame.setPreferredSize(new Dimension(800, 600));
                loginFrame.pack();
                loginFrame.setVisible(true);
            }
        });

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    /* Obtener los valores de los campos de texto */
                    String marca = textMarca.getText();
                    String modeloTexto = textModelo.getText();
                    int stock = Integer.parseInt(textStock.getText());
                    String color = textColor.getText();
                    double precio = Double.parseDouble(textPrecio.getText());
                    int year = Integer.parseInt(textYear.getText());

                    /* Validación de los campos (opcional) */
                    if (marca.isEmpty() || modeloTexto.isEmpty() || color.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    /* Generar un ID (puedes cambiar este comportamiento si tienes lógica de IDs en la base de datos) */
                    int id = modelo.getRowCount() + 1;

                    /* Agregar los valores a la tabla */
                    modelo.addRow(new Object[]{id, marca, modeloTexto, year, precio, color, stock});

                    /* Limpiar los campos de texto */
                    textMarca.setText("");
                    textModelo.setText("");
                    textStock.setText("");
                    textColor.setText("");
                    textPrecio.setText("");
                    textYear.setText("");

                    JOptionPane.showMessageDialog(null, "Carro agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingresa valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}