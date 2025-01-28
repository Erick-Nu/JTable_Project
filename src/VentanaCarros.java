import javax.swing.*;
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

    private final String url = "jdbc:mysql://localhost:3306/CarCenter";
    private final String username = "root";
    private final String password = "1234";

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

    // Metodo eliminar carros
    private void eliminarCarro() {
        int filaSeleccionada = tablaCarros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un carro para eliminar.");
            return;
        }

        int id = (int) tablaCarros.getValueAt(filaSeleccionada, 0);

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM carros WHERE ID = ?")) {

            ps.setInt(1, id);

            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Carro eliminado exitosamente.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al eliminar el carro.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el carro: " + e.getMessage());
        }
    }
    // Metodo actualizar carros
    private void actualizarCarro() {
        int filaSeleccionada = tablaCarros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un carro para actualizar.");
            return;
        }

        int id = (int) tablaCarros.getValueAt(filaSeleccionada, 0);
        String nuevaMarca = JOptionPane.showInputDialog("Nueva marca:", tablaCarros.getValueAt(filaSeleccionada, 1));
        String nuevoModelo = JOptionPane.showInputDialog("Nuevo modelo:", tablaCarros.getValueAt(filaSeleccionada, 2));

        if (nuevaMarca == null || nuevoModelo == null) {
            JOptionPane.showMessageDialog(null, "Actualización cancelada.");
            return;
        }

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE carros SET MARCA = ?, MODELO = ? WHERE ID = ?")) {

            ps.setString(1, nuevaMarca);
            ps.setString(2, nuevoModelo);
            ps.setInt(3, id);

            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Carro actualizado exitosamente.");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar el carro.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el carro: " + e.getMessage());
        }
    }


}
