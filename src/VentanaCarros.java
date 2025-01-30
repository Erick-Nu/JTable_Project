import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class VentanaCarros {
    private JTextField textBuscar;
    private JButton btnBuscar;
    private JTable tablaCarros;
    private JButton btnEliminar;
    private JButton btnUpdate;
    public JPanel VentanaOne;
    public JButton btnAdd;

    private final String url = "jdbc:mysql://localhost:3306/CarCenter";
    private final String username = "root";
    private final String password = "root";

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos: " + e.getMessage());
            return null;
        }
    }


    public VentanaCarros() {
        // Forma de iniciar una tabla con un modelo vacio
        tablaCarros.setModel(new DefaultTableModel(
                new Object[]{"ID", "MARCA", "MODELO", "AÑO", "PRECIO", "COLOR", "STOCK"},
                0
        ));
        // Cargar los datos en la tablas
        cargarDatos();
        // Llamamos los metodos a los botones
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCarros();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCarro();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCarro();
            }
        });


        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame CarrosFrame = (JFrame) SwingUtilities.getWindowAncestor(VentanaOne);
                CarrosFrame.dispose();

                JFrame loginFrame = new JFrame("Agregar Carro");
                loginFrame.setContentPane(new VentanaIngreso().VentanaIngreso);
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(1024, 768);
                loginFrame.setPreferredSize(new Dimension(768, 1360));
                loginFrame.pack();
                loginFrame.setVisible(true);
            }
        });
    }
    // Metodo cargar datos en la tabla
    private void cargarDatos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaCarros.getModel();
        modelo.setRowCount(0); // Limpiar la tabla

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM carros")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("MARCA"),
                        rs.getString("MODELO"),
                        rs.getInt("YEARS"),
                        rs.getDouble("PRECIO"),
                        rs.getString("COLOR"),
                        rs.getInt("STOCK")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
        }
    }
    // Metodo buscar carros
    private void buscarCarros() {
        String termino = textBuscar.getText();

        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un término de búsqueda.");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tablaCarros.getModel();
        modelo.setRowCount(0); // Se limpia la tabla

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM carros WHERE MARCA LIKE ? OR MODELO LIKE ?")) {

            ps.setString(1, "%" + termino + "%");
            ps.setString(2, "%" + termino + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("MARCA"),
                        rs.getString("MODELO"),
                        rs.getInt("YEARS"),
                        rs.getDouble("PRECIO"),
                        rs.getString("COLOR"),
                        rs.getInt("STOCK")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar los datos: " + e.getMessage());
        }
    }

    private void eliminarCarro() {
        int filaSeleccionada = tablaCarros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un auto para eliminar.");
            return;
        }

        // Obtener el ID del carro seleccionado
        int id = (int) tablaCarros.getValueAt(filaSeleccionada, 0);

        // Mostrar el cuadro de confirmación
        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Estás seguro de que quieres eliminar este Auto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        // Si el usuario hace clic en "Sí", proceder con la eliminación
        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection connection = getConnection();
                 PreparedStatement ps = connection.prepareStatement("DELETE FROM carros WHERE ID = ?")) {

                ps.setInt(1, id);
                int resultado = ps.executeUpdate();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Auto eliminado exitosamente.");
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el auto.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el auto: " + e.getMessage());
            }
        } else {
            // Si el usuario hace clic en "No", no hacer nada
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
        }
    }

    // Método para actualizar carros
    private void actualizarCarro() {
        int filaSeleccionada = tablaCarros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un auto para actualizar los datos.");
            return;
        }

        int id = (int) tablaCarros.getValueAt(filaSeleccionada, 0);

        // Obtener valores originales de la tabla
        String marcaOriginal = (String) tablaCarros.getValueAt(filaSeleccionada, 1);
        String modeloOriginal = (String) tablaCarros.getValueAt(filaSeleccionada, 2);
        int yearOriginal = (int) tablaCarros.getValueAt(filaSeleccionada, 3);
        double precioOriginal = (double) tablaCarros.getValueAt(filaSeleccionada, 4);
        String colorOriginal = (String) tablaCarros.getValueAt(filaSeleccionada, 5);
        int stockOriginal = (int) tablaCarros.getValueAt(filaSeleccionada, 6);

        // Pedir nuevos datos
        String nuevaMarca = JOptionPane.showInputDialog("Ingrese la marca:", marcaOriginal);
        String nuevoModelo = JOptionPane.showInputDialog("Ingrese el modelo:", modeloOriginal);
        String nuevoYearStr = JOptionPane.showInputDialog("Ingrese el año:", yearOriginal);
        String nuevoPrecioStr = JOptionPane.showInputDialog("Ingrese el precio:", precioOriginal);
        String nuevoColor = JOptionPane.showInputDialog("Ingrese el color:", colorOriginal);
        String nuevoStockStr = JOptionPane.showInputDialog("Ingrese el stock:", stockOriginal);

        // Si el usuario cancela o deja el campo en blanco, se utiliza el dato original
        nuevaMarca = (nuevaMarca != null && !nuevaMarca.isEmpty()) ? nuevaMarca : marcaOriginal;
        nuevoModelo = (nuevoModelo != null && !nuevoModelo.isEmpty()) ? nuevoModelo : modeloOriginal;
        nuevoColor = (nuevoColor != null && !nuevoColor.isEmpty()) ? nuevoColor : colorOriginal;

        // Convertir los valores numéricos si no están vacíos
        int nuevoYear = yearOriginal;
        double nuevoPrecio = precioOriginal;
        int nuevoStock = stockOriginal;

        try {
            if (nuevoYearStr != null && !nuevoYearStr.isEmpty()) {
                nuevoYear = Integer.parseInt(nuevoYearStr);
            }
            if (nuevoPrecioStr != null && !nuevoPrecioStr.isEmpty()) {
                nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
            }
            if (nuevoStockStr != null && !nuevoStockStr.isEmpty()) {
                nuevoStock = Integer.parseInt(nuevoStockStr);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: El Año, Precio y Stock deben ser valores numéricos válidos.");
            return;
        }

        // Para verificar si el usuario modificó algún dato
        if (nuevaMarca.equals(marcaOriginal) &&
                nuevoModelo.equals(modeloOriginal) &&
                nuevoYear == yearOriginal &&
                nuevoPrecio == precioOriginal &&
                nuevoColor.equals(colorOriginal) &&
                nuevoStock == stockOriginal) {

            JOptionPane.showMessageDialog(null, "No se realizaron cambios.");
            return;
        }

        // Ejecutar la actualización en la base de datos
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE carros SET MARCA = ?, MODELO = ?, YEARS = ?, PRECIO = ?, COLOR = ?, STOCK = ? WHERE ID = ?")) {

            ps.setString(1, nuevaMarca);
            ps.setString(2, nuevoModelo);
            ps.setInt(3, nuevoYear);
            ps.setDouble(4, nuevoPrecio);
            ps.setString(5, nuevoColor);
            ps.setInt(6, nuevoStock);
            ps.setInt(7, id);

            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Auto actualizado exitosamente.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar el auto.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el auto: " + e.getMessage());
        }
    }


}



