package Inventario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AplicacionInventariosOliveira extends JFrame {

    private final JTextField txtId;
    private final JTextField txtNombre;
    private final JTextField txtSerial;
    private final JTextField txtValor;
    private final JTextField txtProveedor;
    private final JButton btnAgregar;
    private final JButton btnModificar;
    private final JButton btnBorrar;
    private final JTable tblInventario;
    private final DefaultTableModel modeloTabla;
    private final Productos[] productos;
    private final int[][] stock;

    public AplicacionInventariosOliveira() {
        productos = new Productos[100]; // Puedes ajustar el tamaño según tus necesidades
        stock = new int[100][1];

        setTitle("Comercializadora OliVanders");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);      
        Image img = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/Imagenes/Logo.jpg"));
        this.setIconImage(img);

        JPanel panelEntradas = new JPanel(new GridLayout(6, 2));
        txtId = new JTextField();
        txtNombre = new JTextField();
        txtSerial = new JTextField();
        txtValor = new JTextField();
        txtProveedor = new JTextField();
        panelEntradas.add(new JLabel("ID:"));
        panelEntradas.add(txtId);        
        panelEntradas.add(new JLabel("Nombre:"));
        panelEntradas.add(txtNombre);
        panelEntradas.add(new JLabel("Número de serie:"));
        panelEntradas.add(txtSerial);
        panelEntradas.add(new JLabel("Valor:"));
        panelEntradas.add(txtValor);
        panelEntradas.add(new JLabel("Proveedor:"));
        panelEntradas.add(txtProveedor);
        panelEntradas.setBackground(Color.lightGray);

        btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener((ActionEvent e) -> {
            agregarProductos();
        });
        btnModificar = new JButton("Modificar");
        btnModificar.addActionListener((ActionEvent e) -> {
            modificarProductos();
        });
        btnBorrar = new JButton("Eliminar");
        btnBorrar.addActionListener((ActionEvent e) -> {
            borrarProductos();
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnBorrar);
        panelBotones.setBackground(Color.lightGray);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Serial", "Valor", "Proveedor"}, 0);
        tblInventario = new JTable(modeloTabla);

        JScrollPane tableScrollPane = new JScrollPane(tblInventario);

        Container panelTabla = getContentPane();
        panelTabla.setLayout(new BorderLayout());
        panelTabla.add(panelEntradas, BorderLayout.NORTH);
        panelTabla.add(tableScrollPane, BorderLayout.CENTER);
        panelTabla.add(panelBotones, BorderLayout.SOUTH);
        panelTabla.setBackground(Color.lightGray);
    }

    private void agregarProductos() {
        int id = Integer.parseInt(txtId.getText());
        String name = txtNombre.getText();
        String serial = txtSerial.getText();
        double value = Double.parseDouble(txtValor.getText());
        String provider = txtProveedor.getText();

        Productos producto = new Productos(id, name, serial, value, provider);
        int indexExistente = encontrarIndexProducto(id);

        if (indexExistente != -1) {
            productos[indexExistente] = producto;
            stock[indexExistente][0]++;
        } else {
            int indexVacio = encontrarIndexVacio();
            if (indexVacio != -1) {
                productos[indexVacio] = producto;
                stock[indexVacio][0]++;
            } else {
                JOptionPane.showMessageDialog(this, "No hay espacio para agregar más productos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        actualizarProductos();
        limpiarCampos();
    }
    private void modificarProductos() {
    int selectedRow = tblInventario.getSelectedRow();
    if (selectedRow != -1) {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            String serial = txtSerial.getText();
            double valor = Double.parseDouble(txtValor.getText());
            String proveedor = txtProveedor.getText();

            productos[selectedRow].setId(id);
            productos[selectedRow].setNombre(nombre);
            productos[selectedRow].setSerial(serial);
            productos[selectedRow].setValor(valor);
            productos[selectedRow].setProveedor(proveedor);

            actualizarProductos();
            limpiarCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa valores válidos en los campos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Selecciona un producto para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}

    private void borrarProductos() {
        int selectedRow = tblInventario.getSelectedRow();
        if (selectedRow != -1) {
            productos[selectedRow] = null;
            stock[selectedRow][0] = 0;
            actualizarProductos();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    private int encontrarIndexProducto(int id) {
        for (int i = 0; i < productos.length; i++) {
            if (productos[i] != null && productos[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private int encontrarIndexVacio() {
        for (int i = 0; i < productos.length; i++) {
            if (productos[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private void actualizarProductos() {
        modeloTabla.setRowCount(0);
        for (int i = 0; i < productos.length; i++) {
            if (productos[i] != null) {
                modeloTabla.addRow(new Object[]{productos[i].getId(), productos[i].getNombre(), productos[i].getSerial(),productos[i].getValor(),
                    productos[i].getProveedor() ,stock[i][0]});
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtSerial.setText("");
        txtValor.setText("");
        txtProveedor.setText("");
    }

    private class Productos {
        private int id;
        private String nombre;
        private String serial;
        private double valor;
        private String proveedor;

        public Productos(int id, String nombre, String serial, double valor, String proveedor) {
            this.id = id;
            this.nombre = nombre;
            this.serial = serial;
            this.valor = valor;
            this.proveedor = proveedor;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public double getValor() {
            return valor;
        }

        public void setValor(double valor) {
            this.valor = valor;
        }

        public String getProveedor() {
            return proveedor;
        }

        public void setProveedor(String proveedor) {
            this.proveedor = proveedor;
        }        
       
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AplicacionInventariosOliveira app = new AplicacionInventariosOliveira();
            app.setVisible(true);
        });
    }
}