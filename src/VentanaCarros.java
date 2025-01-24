import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VentanaCarros {
    private JTextField textBuscar;
    private JButton btnBuscar;
    private JTable table1;
    private JButton btnEliminar;
    private JButton btnUpdate;
    public JPanel VentanaOne;


    public VentanaCarros() {
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Connection connection = null;

                String url = "jdbc:mysql://localhost:3306/carcenter";
                String username = "root";
                String password = "1234";
                try {
                    connection = DriverManager.getConnection(url, username, password);
                    System.out.println("Conexión exitosa a la base de datos");

                    /* Obtenemos los datos ingresados por el usuario */

                    String busqueda = textBuscar.getText();

                    /* Consulta */

                    String query = "SELECT * FROM CARROS WHERE MARCA = '?'";

                    /* Preparamos la consulta */

                    /*try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, correoIngresado);
                        stmt.setString(2, contrasenaIngresada);

                        /* Ejecutamos la consulta */
                        /* ResultSet rs = stmt.executeQuery(); */

                } catch(SQLException error) {
                    System.err.println("Error de conexión");
                } finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e1) {
                            System.err.println("Error al cerrar la conexión");
                        }
                    }
                }
            }
        });
    }
}
