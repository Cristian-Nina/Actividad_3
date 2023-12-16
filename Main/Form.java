import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Form extends JFrame {
    private JTextField nombreTextField, apellidoTextField, fechaVencimientoTextField;
    private JCheckBox cuotaPagadaCheckBox;
    private JButton addButton, updateButton, deleteButton, showButton;
    private JTable resultTable;

    private Connection connection;

    public Form() {
        initializeUI(); // Inicializa la interfaz gráfica
        initDatabase(); // Inicializa la conexión a la base de datos
        showClientes(); // Muestra los clientes al iniciar la aplicación
    }

    private void initializeUI() {
        // Configuración de la ventana principal
        setTitle("Gimnasio CRUD");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel para la entrada de datos
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Nombre:"));
        nombreTextField = new JTextField();
        inputPanel.add(nombreTextField);
        inputPanel.add(new JLabel("Apellido:"));
        apellidoTextField = new JTextField();
        inputPanel.add(apellidoTextField);
        inputPanel.add(new JLabel("Cuota Pagada:"));
        cuotaPagadaCheckBox = new JCheckBox();
        inputPanel.add(cuotaPagadaCheckBox);
        inputPanel.add(new JLabel("Fecha Vencimiento (yyyy-MM-dd):"));
        fechaVencimientoTextField = new JTextField();
        inputPanel.add(fechaVencimientoTextField);
        addButton = new JButton("Agregar");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCliente();
            }
        });
        inputPanel.add(addButton);
        updateButton = new JButton("Actualizar");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCliente();
            }
        });
        inputPanel.add(updateButton);
        deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCliente();
            }
        });
        inputPanel.add(deleteButton);
        showButton = new JButton("Mostrar Clientes");
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showClientes();
            }
        });
        inputPanel.add(showButton);
        add(inputPanel, BorderLayout.NORTH);

        // Tabla para mostrar clientes
        resultTable = new JTable();
        resultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow >= 0) {
                    mostrarDetallesCliente(selectedRow);
                }
            }
        });
        add(new JScrollPane(resultTable), BorderLayout.CENTER);
    }

    private void initDatabase() {
        try {
            // Configuración de la conexión a la base de datos MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/gimnasio";
            String user = "Cris";
            String password = "1234";
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validarDatosCliente(String nombre, String apellido, String fechaVencimientoStr) {
        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || fechaVencimientoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar el formato de la fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateFormat.parse(fechaVencimientoStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Utiliza yyyy-MM-dd", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void addCliente() {
        try {
            // Obtener datos del cliente desde los campos de la interfaz gráfica
            String nombre = nombreTextField.getText();
            String apellido = apellidoTextField.getText();
            boolean cuotaPagada = cuotaPagadaCheckBox.isSelected();
            String fechaVencimientoStr = fechaVencimientoTextField.getText();

            // Validar datos del cliente antes de agregarlo
            if (!validarDatosCliente(nombre, apellido, fechaVencimientoStr)) {
                return;
            }

            // Convertir la fecha de vencimiento a un objeto Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaVencimiento = dateFormat.parse(fechaVencimientoStr);

            // Preparar la sentencia SQL para agregar un nuevo cliente a la base de datos
            PreparedStatement statement = connection.prepareStatement("INSERT INTO clientes (nombre, apellido, cuota_pagada, fecha_vencimiento) VALUES (?, ?, ?, ?)");
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setBoolean(3, cuotaPagada);
            statement.setDate(4, new java.sql.Date(fechaVencimiento.getTime()));

            // Ejecutar la sentencia SQL
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Cliente agregado correctamente");
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar cliente");
        }

        // Actualizar la tabla después de agregar un cliente
        showClientes();
    }

    private void updateCliente() {
        try {
            // Obtener datos del cliente desde los campos de la interfaz gráfica
            String nombre = nombreTextField.getText();
            String apellido = apellidoTextField.getText();
            boolean cuotaPagada = cuotaPagadaCheckBox.isSelected();
            String fechaVencimientoStr = fechaVencimientoTextField.getText();

            // Validar datos del cliente antes de actualizarlo
            if (!validarDatosCliente(nombre, apellido, fechaVencimientoStr)) {
                return;
            }

            // Convertir la fecha de vencimiento a un objeto Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaVencimiento = dateFormat.parse(fechaVencimientoStr);

            // Obtener el ID del cliente seleccionado
            int clienteId = obtenerIdClienteSeleccionado();

            // Preparar la sentencia SQL para actualizar la información del cliente en la base de datos
            PreparedStatement statement = connection.prepareStatement("UPDATE clientes SET nombre=?, apellido=?, cuota_pagada=?, fecha_vencimiento=? WHERE id=?");
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setBoolean(3, cuotaPagada);
            statement.setDate(4, new java.sql.Date(fechaVencimiento.getTime()));
            statement.setInt(5, clienteId);

            // Ejecutar la sentencia SQL
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente");
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar cliente");
        }

        // Actualizar la tabla después de actualizar un cliente
        showClientes();
    }

    private void deleteCliente() {
        try {
            // Obtener el ID del cliente seleccionado
            int clienteId = obtenerIdClienteSeleccionado();

            // Preparar la sentencia SQL para eliminar al cliente de la base de datos
            PreparedStatement statement = connection.prepareStatement("DELETE FROM clientes WHERE id=?");
            statement.setInt(1, clienteId);

            // Ejecutar la sentencia SQL
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar cliente");
        }

        // Actualizar la tabla después de eliminar un cliente
        showClientes();
    }

    private void showClientes() {
        try {
            // Crear una declaración para ejecutar consultas SQL
            Statement statement = connection.createStatement();

            // Ejecutar la consulta para obtener todos los clientes de la base de datos
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clientes");

            // Crear un modelo de tabla para almacenar los datos de los clientes
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Nombre");
            model.addColumn("Apellido");
            model.addColumn("Cuota Pagada");
            model.addColumn("Fecha Vencimiento");

            // Iterar a través de los resultados y agregar filas al modelo de tabla
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                boolean cuotaPagada = resultSet.getBoolean("cuota_pagada");
                Date fechaVencimiento = resultSet.getDate("fecha_vencimiento");

                // Verificar si la cuota está vencida y resaltar la fila si es el caso
                boolean cuotaVencida = esCuotaVencida(fechaVencimiento);
                Object[] row = {id, nombre, apellido, cuotaPagada, fechaVencimiento};
                model.addRow(row);

                if (cuotaVencida) {
                    // Puedes cambiar el color de fondo de la fila o el color del texto
                    resultTable.setSelectionBackground(Color.RED);
                }
            }

            // Establecer el modelo de tabla en la tabla de resultados
            resultTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al mostrar clientes");
        }
    }

    private void mostrarDetallesCliente(int rowIndex) {
        try {
            // Crear una declaración para ejecutar consultas SQL
            Statement statement = connection.createStatement();

            // Ejecutar la consulta para obtener todos los clientes de la base de datos
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clientes");
            int currentRow = 0;

            // Iterar hasta llegar a la fila seleccionada
            while (resultSet.next()) {
                if (currentRow == rowIndex) {
                    // Obtener los detalles del cliente seleccionado
                    int id = resultSet.getInt("id");
                    String nombre = resultSet.getString("nombre");
                    String apellido = resultSet.getString("apellido");
                    boolean cuotaPagada = resultSet.getBoolean("cuota_pagada");
                    Date fechaVencimiento = resultSet.getDate("fecha_vencimiento");

                    // Mostrar detalles del cliente en un cuadro de diálogo
                    JOptionPane.showMessageDialog(this,
                            "ID: " + id + "\nNombre: " + nombre + "\nApellido: " + apellido +
                                    "\nCuota Pagada: " + cuotaPagada + "\nFecha Vencimiento: " + fechaVencimiento,
                            "Detalles del Cliente", JOptionPane.INFORMATION_MESSAGE);

                    break;
                }

                currentRow++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al mostrar detalles del cliente");
        }
    }

    private int obtenerIdClienteSeleccionado() {
        // Obtener el índice de la fila seleccionada en la tabla
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el valor de la columna "ID" de la fila seleccionada
            return Integer.parseInt(resultTable.getValueAt(selectedRow, 0).toString());
        } else {
            // Mostrar un mensaje si no se ha seleccionado ninguna fila
            JOptionPane.showMessageDialog(this, "Selecciona un cliente primero");
            return -1;
        }
    }

    private boolean esCuotaVencida(Date fechaVencimiento) {
        // Obtener la fecha actual
        Date fechaActual = new Date();

        // Verificar si la fecha de vencimiento es anterior a la fecha actual
        return fechaVencimiento.before(fechaActual);
    }
}

