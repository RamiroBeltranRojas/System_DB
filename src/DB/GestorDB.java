package DB;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;

import DB.BarraMenu.PanelConexion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GestorDB extends JFrame implements ActionListener {
    private JComboBox<String> menuDesplegable;
    private JList<String> listaBasesDatos;
    private static contInfo panelDerecho;
    private static contenedor_DB panelIzquierdo;
    private JMenu menu;
    private JMenuItem menuItem1,menuItem2;   
    private static Connection connection;
    public static String user,password, host, port,DB;
    private static int numeColumnas=0;
    private ArrayList<JCheckBox> checkBoxes;
    private static JLabel consulta;
    private static JPanel cont;
    private static JSplitPane splitPane;
    
    
    

    public GestorDB() {
    	JSplitPane splitPane= new JSplitPane();
    	consulta= new JLabel();
        // Configurando el frame principal
        setTitle("Interfaz de Bases de Datos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);    
        panelDerecho = new contInfo();    	
        // Creando el panel izquierdo con lista de bases de datos
        panelIzquierdo = new contenedor_DB(); 
        JPanel mensajePanel = new JPanel(new GridBagLayout());
        mensajePanel.setOpaque(false); // Hace que el panel sea transparente
        mensajePanel.setPreferredSize(panelIzquierdo.getPreferredSize()); // Iguala el tamaño

        // Crea los JLabels para el mensaje
        JLabel parte1 = new JLabel("Please make sure");
        parte1.setFont(new Font("Arial", Font.BOLD, 20));
        parte1.setForeground(Color.white);
        JLabel parte2 = new JLabel("your connection");
        parte2.setFont(new Font("Arial", Font.BOLD, 20));
        parte2.setForeground(Color.white);

        // Configura los componentes dentro del mensajePanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 50, 0); // Ajusta los márgenes

        // Alinea los componentes en el centro verticalmente y horizontalmente
        gbc.anchor = GridBagConstraints.CENTER;

        mensajePanel.add(parte1, gbc);
        gbc.gridy = 1;
        mensajePanel.add(parte2, gbc);

        // Añade el mensajePanel al panel izquierdo
        panelIzquierdo.add(mensajePanel);
        addlogo();
        add(panelIzquierdo,BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);  

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.decode("#0C3757"));

        // Crea el menú
        menu = new JMenu("Opciones");
        menu.setForeground(Color.WHITE);
        menu.setBackground(Color.GRAY);

        // Crea los elementos del menú
        menuItem1 = new JMenuItem("Conectar");
        menuItem2 = new JMenuItem("Crear base de datos");
        

        // Agrega los elementos al menú
        menu.add(menuItem1);
        menu.add(menuItem2);

        // Agrega el menú a la barra de menú
        menuBar.add(menu);

        // Asigna la barra de menú al JFrame
        setJMenuBar(menuBar);

        // Agrega un ActionListener a los elementos del menú
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para la opción "Conectar"
                System.out.println("Conectar seleccionado");
                mostrarPanelConexion();
            }
        });

        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para la opción "Crear base de datos"
            	if(connection!=null) {
                System.out.println("Crear base de datos seleccionado");
                mostrarPanelCrearDB();
            	}else {
					JOptionPane.showMessageDialog(null, "No se ha establecido la conexion", "Error", JOptionPane.ERROR_MESSAGE);
				}
                
            }
        });
       
        
    }
    public void addlogo() {
    	String rutaLogo = "src/img/logo.gif";
        ImageIcon icono = new ImageIcon(rutaLogo);
        JLabel logoLabel = new JLabel(icono);
        panelDerecho.removeAll();
        panelDerecho.add(logoLabel);
        panelDerecho.revalidate();
        panelDerecho.repaint();
    }
    public void cargarInformacionBaseDatos(String seleccion) {
        PanelConexion pc = new PanelConexion();
        if (seleccion != null) {
            try {
                pc.connect(user, password, host, port, seleccion);
                // Consultar la información de la base de datos seleccionada
                String query = "SHOW TABLES FROM " + seleccion;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Crear un StringBuilder para almacenar la información
                ArrayList<String> info = new ArrayList<>();
                String encabezado = "Tablas en la base de datos " + seleccion + ":\n";
                JLabel enca = new JLabel(encabezado);
                enca.setForeground(Color.white);
                enca.setFont(new Font("Arial", Font.BOLD, 14));

                // Iterar sobre los resultados y agregarlos al StringBuilder
                while (resultSet.next()) {
                    info.add(resultSet.getString(1) + "\n");
                }

                // Mostrar la información en el panel derecho
                panelDerecho.removeAll();
                panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS)); // Establece BorderLayout

                JPanel panelEncabezado = new JPanel();
                panelEncabezado.setLayout(new BoxLayout(panelEncabezado,BoxLayout.X_AXIS)); // Establece el layout como FlowLayout
                panelEncabezado.setBackground(Color.decode("#156598"));
                panelEncabezado.setPreferredSize(new Dimension(800, 50)); // Establece un tamaño preferido fijo

                String rutaIcono = "src/img/createTabla.png"; 

                // Crea una instancia de ImageIcon
                   ImageIcon icono = new ImageIcon(rutaIcono);
                JButton crearTablaButton = new JButton("Crear Tabla");
                crearTablaButton.setForeground(Color.WHITE);
                crearTablaButton.setBackground(null);
                crearTablaButton.setBorderPainted(false);
                crearTablaButton.setIcon(icono);
                crearTablaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                crearTablaButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        crearTablaButton.setBackground(new Color(0x3498DB)); // Cambia el color cuando el mouse entra
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        crearTablaButton.setBackground(null);; // Restaura el color cuando el mouse sale
                    }
                });
                JButton botonR = new JButton();
                botonR.setBackground(null);
                botonR.setBorderPainted(false);
                panelEncabezado.add(enca,BorderLayout.EAST);
                panelEncabezado.add(botonR,BorderLayout.EAST);
                panelEncabezado.add(crearTablaButton,BorderLayout.WEST);

                panelDerecho.add(panelEncabezado, BorderLayout.NORTH);
                
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Siempre muestra la barra vertical

                JPanel panelBotones = new JPanel(); // Panel para contener los botones
                panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS)); // Usar BoxLayout para colocar verticalmente
                panelBotones.setBackground(Color.decode("#156598"));                

                panelBotones.add(Box.createHorizontalGlue());

                for (String nombreTabla : info) {
                	JButton boton = new JButton(nombreTabla);
                    boton.setAlignmentX(Component.LEFT_ALIGNMENT); // Alinea el botón a la izquierda
                    boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor
                    boton.setBorder(new LineBorder(new Color(0x2ECC71), 2)); // Establece un borde
                    boton.setBackground(new Color(0x2B83C4)); // Restaura el color de fondo al salir del botón
                    boton.setForeground(Color.WHITE); // Cambia el color del texto
                    boton.setFont(new Font("Arial", Font.BOLD, 14));
                    boton.setBorderPainted(false);
                    boton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
                 // Agrega un espacio vertical entre los botones
                    panelBotones.add(Box.createVerticalStrut(10));
                    
                    boton.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            boton.setBackground(new Color(0x2ECC71)); // Cambia el color de fondo al pasar el cursor
                            boton.setForeground(Color.WHITE); // Cambia el color del texto al pasar el cursor
                        }

                        public void mouseExited(java.awt.event.MouseEvent evt) {
                        	boton.setBackground(new Color(0x2B83C4)); // Restaura el color de fondo al salir del botón
                            boton.setForeground(Color.WHITE); // Restaura el color del texto al salir del botón
                        }
                    });
                    boton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Aquí debes cargar la tabla con los datos de la tabla seleccionada
                            cargarTabla(nombreTabla);
                        }
                    });

                    panelBotones.add(boton);
                }
                crearTablaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Lógica para la opción "Crear base de datos"
                    	//if(connection!=null) {
                       // System.out.println("Crear tabla seleccionado");
                        mostrarPanelCrearTabla();
                    	//}else {
        				//	JOptionPane.showMessageDialog(null, "No se ha establecido la conexion");
        				//}
                        
                    }
                });

                scrollPane.setViewportView(panelBotones); // Agrega el panel de botones al JScrollPane
                panelDerecho.add(scrollPane);
                

                panelDerecho.revalidate();
                panelDerecho.repaint();
            } catch (SQLException e) {
                e.printStackTrace(); // Manejar el error adecuadamente
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // Puedes agregar acciones para el menú desplegable aquí si lo necesitas
    }
    public void cargarTabla(String nombreTabla) {
        try {	        
            // Consultar los datos de la tabla seleccionada
            String query = "SELECT * FROM " + nombreTabla;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Obtener la información de las columnas
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numColumnas = metaData.getColumnCount();
            String[] nombresColumnas = new String[numColumnas];

            for (int i = 0; i < numColumnas; i++) {
                nombresColumnas[i] = metaData.getColumnName(i + 1);
            }

            // Obtener los datos
            ArrayList<Object[]> datos = new ArrayList<>();

            while (resultSet.next()) {
                Object[] fila = new Object[numColumnas];
                for (int i = 0; i < numColumnas; i++) {
                    fila[i] = resultSet.getObject(i + 1);
                }
                datos.add(fila);
            }

            // Crear el DefaultTableModel y JTable
            DefaultTableModel model = new DefaultTableModel(datos.toArray(new Object[0][0]), nombresColumnas);
            JTable tabla = new JTable(model);

            // Configurar el renderizador de los encabezados personalizado
            tabla.setTableHeader(new CustomTableHeaderRenderer(tabla.getColumnModel()));
            // Aplicar el renderizador personalizado a todas las columnas
            tabla.setDefaultRenderer(Object.class, new CustomCellRenderer());
            
            tabla.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                	int filaSeleccionada = tabla.getSelectedRow();
                	if (filaSeleccionada != -1) {
                	    int numColumnas = tabla.getColumnCount();
                	    Object[] nuevosDatos = new Object[numColumnas];
                	    
                	    for (int i = 0; i < numColumnas; i++) {
                	        Object valorCelda = tabla.getValueAt(filaSeleccionada, i);
                	        nuevosDatos[i] = valorCelda;
                	    }

                	    try {
                	        // Construye la consulta de actualización
                	        StringBuilder query = new StringBuilder("UPDATE " + nombreTabla + " SET ");
                	        for (int i = 0; i < numColumnas; i++) {
                	            query.append(nombresColumnas[i] + " = ");
                	            if (nuevosDatos[i] instanceof String) {
                	                query.append("'" + nuevosDatos[i] + "'");
                	            } else {
                	                query.append(nuevosDatos[i]);
                	            }
                	            if (i < numColumnas - 1) {
                	                query.append(", ");
                	            }
                	        }
                	        query.append(" WHERE " + nombresColumnas[0] + " = " + nuevosDatos[0]); // Asumiendo que la primera columna es la clave primaria
                	        
                	        Statement statement = connection.createStatement();
                	        statement.executeUpdate(query.toString());
                	        consulta=null; 
                            consulta= new JLabel(query.toString());                            
                	        JOptionPane.showMessageDialog(null, "Los datos se actualizaron correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                	        cargarTabla(nombreTabla);
                	    } catch (SQLException ex) {
                	        JOptionPane.showMessageDialog(null, "Error: " + ex, "Error de actualización", JOptionPane.ERROR_MESSAGE);                	       
                	    }                	    
                	} 
                }
            });

            // Mostrar la tabla en un JScrollPane
            JScrollPane scrollPane = new JScrollPane(tabla);
            JButton insertarButton1 = new JButton("Insertar Datos");
            insertarButton1.setBackground(new Color(0x2ECC71));
    	    insertarButton1.setForeground(Color.WHITE);
    	    insertarButton1.setBorderPainted(false);
    	    insertarButton1.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	    JButton agregarFilaButton = new JButton("Agregar Fila");
    	    agregarFilaButton.setBackground(new Color(0x2ECC71));
    	    agregarFilaButton.setForeground(Color.WHITE);
    	    agregarFilaButton.setBorderPainted(false);
    	    agregarFilaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	    JButton eliminarRegistroButton = new JButton("Eliminar Registro");
    	    eliminarRegistroButton.setBackground(new Color(0x2ECC71));
    	    eliminarRegistroButton.setForeground(Color.WHITE);
    	    eliminarRegistroButton.setBorderPainted(false);
    	    eliminarRegistroButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	    JButton consultaButton = new JButton("Crear Consulta");
    	    consultaButton.setBackground(new Color(0x2ECC71));
    	    consultaButton.setForeground(Color.WHITE);
    	    consultaButton.setBorderPainted(false);
    	    consultaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	       	   
    	        	    
    	    


    	    	consultaButton.addActionListener(new ActionListener() {
    	    	    @Override
    	    	    public void actionPerformed(ActionEvent e) {
    	    	    	mostrarPanelConsulta(nombreTabla, nombresColumnas);    	    	       
    	    	    }
    	    	});
    	    agregarFilaButton.addActionListener(new ActionListener() {
    	        @Override
    	        public void actionPerformed(ActionEvent e) {
    	            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    	            model.addRow(new Object[numColumnas]);
    	        }
    	    });

    	    
            // Limpiar el panelDerecho y agregar la tabla
    	    cont = new JPanel();
    	    cont.setBackground(Color.decode("#156598"));
    	    cont.add(agregarFilaButton, BorderLayout.WEST);
    	    cont.add(insertarButton1, BorderLayout.CENTER);
    	    cont.add(eliminarRegistroButton, BorderLayout.EAST);
    	    cont.add(consultaButton, BorderLayout.EAST);

    	    // Establecer el tamaño preferido
    	    Dimension preferredSize = new Dimension(200, 2);
    	    cont.setPreferredSize(preferredSize);
            panelDerecho.removeAll();
            consulta.setForeground(Color.WHITE);
            
            panelDerecho.add(cont, BorderLayout.NORTH);
            panelDerecho.add(scrollPane, BorderLayout.CENTER); 
            panelDerecho.add(consulta,BorderLayout.SOUTH);
            panelDerecho.revalidate();
            panelDerecho.repaint();
            insertarButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	try {
                		numeColumnas = metaData.getColumnCount();
                        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                        int ultimaFila = model.getRowCount() - 1;
                        System.out.println("Ultima fila: " + ultimaFila);
                        String[] tiposColumnas = new String[numeColumnas];
                        for (int i = 0; i < numeColumnas; i++) {
                            tiposColumnas[i] = metaData.getColumnTypeName(i + 1);
                            System.out.println("Num Col: " + tiposColumnas[i]);
                        }

                        // Obtener los nuevos datos de la última fila
                        String[] nuevosDatos = new String[numeColumnas];
                        for (int i = 0; i < numeColumnas; i++) {
                        	System.out.println("Num Col: " + numeColumnas);
                            String valor = String.valueOf(model.getValueAt(ultimaFila, i));
                            System.out.println("Columna " + i + ": " + valor);
                            nuevosDatos[i] = valor;
                        }

                        String query = "INSERT INTO " + nombreTabla + " VALUES (";
                        for (int i = 0; i < nuevosDatos.length; i++) {
                            if (tiposColumnas[i].equals("VARCHAR")) {
                                query += "'" + nuevosDatos[i] + "'";
                            } else {
                                query += nuevosDatos[i];
                            }
                            if (i < nuevosDatos.length - 1) {
                                query += ", ";
                            }
                        }
                        query += ")";

                        Statement statement = connection.createStatement();
                        statement.executeUpdate(query);
                        
                        // Limpiar la fila agregada
                        for (int i = 0; i < numeColumnas; i++) {
                            model.setValueAt("", ultimaFila, i);
                        }
                                   	        
                        JOptionPane.showMessageDialog(null, "Los datos se insertaron correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        consulta=null; 
                        consulta= new JLabel(query.toString());
                        cargarTabla(nombreTabla);                       
            	        
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error: Llene todos los campos e ingrese datos validos", "Error de inserción", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            eliminarRegistroButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int filaSeleccionada = tabla.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        try {
                            Object valorCelda = tabla.getValueAt(filaSeleccionada, 0); // Obtener el valor de la primera columna (asumiendo que es la clave primaria)
                            String clavePrimaria = String.valueOf(valorCelda);

                            // Construye la consulta de eliminación
                            String query = "DELETE FROM " + nombreTabla + " WHERE " + nombresColumnas[0] + " = '" + clavePrimaria + "'"; // Asumiendo que la primera columna es la clave primaria
                            Statement statement = connection.createStatement();
                            statement.executeUpdate(query);

                            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
                            model.removeRow(filaSeleccionada); // Elimina la fila de la tabla

                            JOptionPane.showMessageDialog(null, "El registro se eliminó correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            consulta=null; 
                            consulta= new JLabel(query.toString());
                            cargarTabla(nombreTabla);
                            
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error: " + ex, "Error de eliminación", JOptionPane.ERROR_MESSAGE);
                            
                        }
                    }
                }
            });
            
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error adecuadamente
        }
    }
    public ResultSet ejecutarConsulta(String consulta) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(consulta);
        } catch (SQLException e) {
            e.printStackTrace();
            // Aquí puedes manejar la excepción de acuerdo a tus necesidades
            return null; // Otra opción es lanzar una excepción personalizada
        }
    }

    public JTable crearTablaDesdeResultSet(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numColumnas = metaData.getColumnCount();
            String[] nombresColumnas = new String[numColumnas];

            for (int i = 0; i < numColumnas; i++) {
                nombresColumnas[i] = metaData.getColumnName(i + 1);
            }

            ArrayList<Object[]> datos = new ArrayList<>();

            while (resultSet.next()) {
                Object[] fila = new Object[numColumnas];
                for (int i = 0; i < numColumnas; i++) {
                    fila[i] = resultSet.getObject(i + 1);
                }
                datos.add(fila);
            }

            DefaultTableModel model = new DefaultTableModel(datos.toArray(new Object[0][0]), nombresColumnas);
            JTable tabla = new JTable(model);

            // Configurar el renderizador de los encabezados personalizado si es necesario
            tabla.setTableHeader(new CustomTableHeaderRenderer(tabla.getColumnModel()));
            // Aplicar el renderizador personalizado a todas las columnas si es necesario
            tabla.setDefaultRenderer(Object.class, new CustomCellRenderer());

            return tabla;
        } catch (SQLException e) {
            e.printStackTrace();
            // Aquí puedes manejar la excepción de acuerdo a tus necesidades
            return null; // Otra opción es lanzar una excepción personalizada
        }
    }

    
    private void mostrarPanelConexion() {
        // Crea y muestra el panel de conexión en el PanelDerecho
        PanelConexion panelConexion = new PanelConexion();
        panelDerecho.removeAll(); // Limpia el contenido actual
        panelDerecho.add(panelConexion); // Agrega el nuevo panel
        panelDerecho.revalidate();
        panelDerecho.repaint();
    }
    private void mostrarPanelCrearDB() {
        // Crea y muestra el panel de conexión en el PanelDerecho
        PanelCrearBaseDatos panelCrearDB = new PanelCrearBaseDatos();
        panelDerecho.removeAll(); // Limpia el contenido actual
        panelDerecho.add(panelCrearDB); // Agrega el nuevo panel
        panelDerecho.revalidate();
        panelDerecho.repaint();
    }
    private void mostrarPanelCrearTabla() {
        // Crea y muestra el panel de conexión en el PanelDerecho
        PanelCrearTabla panelCrearTabla = new PanelCrearTabla();
        panelDerecho.removeAll(); // Limpia el contenido actual
        panelDerecho.add(panelCrearTabla); // Agrega el nuevo panel
        panelDerecho.revalidate();
        panelDerecho.repaint();
    }
    private void mostrarPanelConsulta(String nombreTabla,String[] nombresColumnas) {
        // Crea y muestra el panel de conexión en el PanelDerecho
        PanelConsulta panelConsulta = new PanelConsulta(nombreTabla,nombresColumnas);
        panelDerecho.removeAll(); // Limpia el contenido actual
        panelDerecho.add(panelConsulta); // Agrega el nuevo panel
        panelDerecho.revalidate();
        panelDerecho.repaint();
    }
   

       
        
    public class CustomCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            cellComponent.setForeground(Color.WHITE);
            cellComponent.setBackground(Color.decode("#156598"));
            // Personaliza el aspecto de las celdas aquí
            if (isSelected) {
                cellComponent.setBackground(Color.decode("#2B83C4"));
                cellComponent.setForeground(Color.WHITE);
            } else {
                cellComponent.setBackground(Color.decode("#156598"));
                cellComponent.setForeground(Color.WHITE);
            }
            
            return cellComponent;
        }
    }
    public class CustomTableHeaderRenderer extends JTableHeader {

        private boolean isAscending = true;

        public CustomTableHeaderRenderer(TableColumnModel model) {
            super(model);
            setReorderingAllowed(true);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int colIdx = columnAtPoint(e.getPoint());
                    if (colIdx >= 0) {
                        JTable table = getTable();
                        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

                        // Obtener los datos y nombres de columna
                        ArrayList<Object[]> datos = new ArrayList<>();
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            Object[] fila = new Object[tableModel.getColumnCount()];
                            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                                fila[j] = tableModel.getValueAt(i, j);
                            }
                            datos.add(fila);
                        }

                        // Ordenar los datos
                        Collections.sort(datos, new Comparator<Object[]>() {
                            @Override
                            public int compare(Object[] fila1, Object[] fila2) {
                                int result = String.valueOf(fila1[colIdx]).compareTo(String.valueOf(fila2[colIdx]));
                                return isAscending ? result : -result;
                            }
                        });

                        // Limpiar el modelo actual y volver a llenarlo con los datos ordenados
                        tableModel.setRowCount(0);
                        for (Object[] fila : datos) {
                            tableModel.addRow(fila);
                        }

                        // Cambiar el estado de ordenamiento
                        isAscending = !isAscending;
                    }
                }
            });
        }
    }
    
    
    
    public class PanelConsulta extends JPanel {
    	private boolean almenosUnCheckboxSeleccionado;
        private JPanel panelDeCheckboxes;
        private JPanel consultaPanel;
        private StringBuilder consulta;
        private JLabel labelConsulta;

        public PanelConsulta(String nombreTabla, String[] nombresColumnas) {

            panelDeCheckboxes = new JPanel();
            panelDeCheckboxes.setLayout(new GridLayout(0, 1));

            consulta = new StringBuilder("SELECT ");
            JCheckBox checkBoxTodos = new JCheckBox("*");
            checkBoxTodos.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Si el checkbox "Todos" se selecciona, deselecciona los demás
                    if (checkBoxTodos.isSelected()) {
                        Component[] componentes = panelDeCheckboxes.getComponents();
                        for (Component componente : componentes) {
                            if (componente instanceof JCheckBox && componente != checkBoxTodos) {
                                ((JCheckBox) componente).setSelected(false);
                            }
                        }
                    }
                    actualizarConsulta(nombreTabla);
                }
            });
            panelDeCheckboxes.add(checkBoxTodos);

            for (String nombreColumna : nombresColumnas) {
            	if (!nombreColumna.equals("*")) {
                    JCheckBox checkBox = new JCheckBox(nombreColumna);
                    checkBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            checkBoxTodos.setSelected(false);
                            actualizarConsulta(nombreTabla);
                        }
                    });
                panelDeCheckboxes.add(checkBox);
            }
            }
            	

            consultaPanel = new JPanel();
            consultaPanel.setLayout(new BorderLayout());

            JButton consultaButton = new JButton("Crear Consulta");
            consultaButton.setBackground(new Color(0x2ECC71));
            consultaButton.setForeground(Color.WHITE);
            consultaButton.setBorderPainted(false);
            consultaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            consultaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (almenosUnCheckboxSeleccionado) {
                        try {
                            // Aquí deberías ejecutar la consulta y obtener los resultados
                            ResultSet resultSet = ejecutarConsulta(consulta.toString());

                            // Crear un nuevo JTable con los resultados
                            JTable nuevoJTable = crearTablaDesdeResultSet(resultSet);

                            // Verificar si hay una tabla anterior y quitarla
                            if (splitPane != null) {
                                remove(splitPane);
                            }

                            // Agregar la nueva tabla al panel de consulta
                            splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, consultaPanel, new JScrollPane(nuevoJTable));
                            splitPane.setResizeWeight(0.5);
                            splitPane.setOneTouchExpandable(true);
                            splitPane.setContinuousLayout(true);		               
                            add(splitPane, BorderLayout.CENTER);
                            revalidate();
                            repaint();

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar al menos una columna.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            consultaPanel.add(consultaButton, BorderLayout.NORTH);
            consultaPanel.add(panelDeCheckboxes, BorderLayout.CENTER);

            labelConsulta = new JLabel(consulta.toString());
            consultaPanel.add(labelConsulta, BorderLayout.SOUTH);

            add(consultaPanel,BorderLayout.SOUTH);
        }

        public void actualizarConsulta(String nombreTabla) {
            Component[] componentes = panelDeCheckboxes.getComponents();
            almenosUnCheckboxSeleccionado = false;
            consulta=null;
            consulta = new StringBuilder("SELECT ");

            for (Component componente : componentes) {
                if (componente instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) componente;
                    if (checkBox.isSelected()) {
                        consulta.append(checkBox.getText()).append(", ");
                        almenosUnCheckboxSeleccionado = true;
                    }
                }
            }
            consulta.delete(consulta.length() - 2, consulta.length());
            consulta.append(" FROM ").append(nombreTabla);

            // Si hay al menos un checkbox seleccionado, actualiza el texto en el label
            labelConsulta.setText(consulta.toString());

            // Revalida y repinta el panel
            consultaPanel.revalidate();
            consultaPanel.repaint();
        }

        // Método para ejecutar la consulta y obtener el ResultSet
        private ResultSet ejecutarConsulta(String consulta) {
            try {
            	Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                return statement.executeQuery(consulta);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        public JTable crearTablaDesdeResultSet(ResultSet resultSet) throws SQLException {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numColumnas = metaData.getColumnCount();
            String[] nombresColumnas = new String[numColumnas];

            for (int i = 0; i < numColumnas; i++) {
                nombresColumnas[i] = metaData.getColumnName(i + 1);
            }

            // Obtener el número de filas en el ResultSet
            resultSet.last();
            int numFilas = resultSet.getRow();
            resultSet.beforeFirst();

            // Obtener los datos
            Object[][] datos = new Object[numFilas][numColumnas];

            int fila = 0;
            while (resultSet.next() && fila < numFilas) {
                for (int i = 0; i < numColumnas; i++) {
                    datos[fila][i] = resultSet.getObject(i + 1);
                }
                fila++;
            }
            DefaultTableModel model = new DefaultTableModel(datos, nombresColumnas);
            return new JTable(model);
        }


    }
    public class CustomTableModel extends AbstractTableModel {
        private Object[][] data;
        private String[] columnNames;

        public CustomTableModel(Object[][] data, String[] columnNames) {
            this.data = data;
            this.columnNames = columnNames;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        // ... Otros métodos si es necesario (por ejemplo, para editar celdas, etc.)
    }
    
    public class PanelConexion extends JPanel {
    	private JTextField userField;
        private JTextField passwordField;
        private JTextField hostField;
        private JTextField portField;
        private JButton testButton;
        private JButton connectButton;

        public PanelConexion() {
        	setBackground(Color.decode("#156598"));
            setLayout(new GridBagLayout());
           
            GridBagConstraints gbc = new GridBagConstraints(); 
            
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            gbc.insets = new Insets(20, 5, 40,0);

            // Etiquetas y campos de texto
            JLabel userLabel = new JLabel("User:");
            userLabel.setForeground(Color.white);
            userLabel.setFont(new Font("Arial", Font.BOLD, 14));
            userField = new JTextField();
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setForeground(Color.white);
            passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
            passwordField = new JTextField();
            JLabel hostLabel = new JLabel("Host:");
            hostLabel.setForeground(Color.white);
            hostLabel.setFont(new Font("Arial", Font.BOLD, 14));
            hostField = new JTextField();
            JLabel portLabel = new JLabel("Port:");
            portLabel.setForeground(Color.white);
            portLabel.setFont(new Font("Arial", Font.BOLD, 14));
            portField = new JTextField();

            gbc.gridx = 0;
            gbc.gridy = 0;
            add(userLabel, gbc);
            gbc.gridx = 1;
            add(userField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            add(passwordLabel, gbc);
            gbc.gridx = 1;
            add(passwordField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            add(hostLabel, gbc);
            gbc.gridx = 1;
            add(hostField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            add(portLabel, gbc);
            gbc.gridx = 1;
            add(portField, gbc);

            // Botones
            testButton = new JButton("Test de Conexión");
            connectButton = new JButton("Conectar");

            testButton.setBackground(new Color(0x2ECC71)); // Cambia el color de fondo del botón
            testButton.setForeground(Color.WHITE); // Cambia el color del texto del botón
            testButton.setBorderPainted(false); // Quita el borde
            testButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            String rutaIcono = "src/img/conectDB.png"; 

            // Crea una instancia de ImageIcon
               ImageIcon icono = new ImageIcon(rutaIcono);
            connectButton.setBackground(new Color(0x3498DB)); // Cambia el color de fondo del botón
            connectButton.setForeground(Color.WHITE); // Cambia el color del texto del botón
            connectButton.setBorderPainted(false); // Quita el borde
            connectButton.setIcon(icono);
            connectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 5, 10, 5);
            add(testButton, gbc);

            gbc.gridy = 5;
            add(connectButton, gbc);
            setPreferredSize(new Dimension(600, 600));
            testButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	try {
						
					
                	String user = userField.getText();
                    String password = passwordField.getText();
                    String host = hostField.getText();
                    String port = portField.getText();

                    // Intenta conectar a la base de datos y muestra un mensaje de éxito o error
                    if (testConnection(user, password, host, port,"")) {
                        JOptionPane.showMessageDialog(null, "Conexión exitosa", "Test de Conexión", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al conectar", "Test de Conexión", JOptionPane.ERROR_MESSAGE);
                    }
                }catch (Exception e2) {
				// TODO: handle exception
			}
                }
            });
        
            connectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    user = userField.getText();
                    password = passwordField.getText();
                    host = hostField.getText();
                    port = portField.getText();

                    // Intenta conectar a la base de datos y muestra un mensaje de éxito o error
                    connection = connect(user, password, host, port,"");
                    if (connection != null) {
                        // Aquí puedes realizar operaciones con la base de datos
                        // Por ejemplo: Statement statement = connection.createStatement();                    
                        JOptionPane.showMessageDialog(null, "Conexión exitosa", "Conectar", JOptionPane.INFORMATION_MESSAGE);
                        cargarDB();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al conectar", "Conectar", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

                  
        }
        public void cargarDB() {
        	panelIzquierdo.removeAll();
            ArrayList<String> basesDatos = new ArrayList<>();
            basesDatos = obtenerBasesDatos(connection);
            System.out.println(basesDatos);
            for (String nombreBaseDatos : basesDatos) {
                basesDatosList boton = new basesDatosList(nombreBaseDatos);
                boton.addActionListener(e -> cargarInformacionBaseDatos(DB=boton.getNombreBaseDatos()));
                panelIzquierdo.add(boton); // Agrega el botón al panel izquierdo actual
            }
            panelIzquierdo.revalidate(); // Vuelve a validar el panel izquierdo
            panelIzquierdo.repaint(); // Vuelve a pintar el panel izquierdo
        }
        public static Connection connect(String user, String password, String host, String port, String db) {
            connection = null;

            try {
                // Construir la URL de conexión
                String url = "jdbc:mysql://" + host + ":" + port + "/"+db;
                
                // Intentar conectar
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Conexión exitosa");
            } catch (SQLException e) {
                e.printStackTrace();//Eliminar
                System.err.println("Error al conectar");
            }

            return connection;
        }

        public static boolean testConnection(String user, String password, String host, String port, String db) {
            try (Connection connection = connect(user, password, host, port,db)) {
                return connection != null;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Valores no validos");
                return false;
            }
        }
        public ArrayList<String> obtenerBasesDatos(Connection connection) {
            ArrayList<String> basesDatos = new ArrayList<>();

            try {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet resultSet = metaData.getCatalogs();

                while (resultSet.next()) {
                    String dbName = resultSet.getString(1);
                    basesDatos.add(dbName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return basesDatos;
        }
    }
    public class PanelCrearBaseDatos extends JPanel {
        private JTextField nombreBaseDatosField;
        private JComboBox<String> tipoBaseDatosComboBox;
        private JButton crearBaseDatosButton;

        public PanelCrearBaseDatos() {
        	setBackground(Color.decode("#156598"));
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(20, 5, 50, 0);

            // Etiquetas y campos de texto
            JLabel nombreLabel = new JLabel("Nombre de la Base de Datos:");
            nombreLabel.setForeground(Color.white);
            nombreLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nombreBaseDatosField = new JTextField();
            nombreBaseDatosField.setColumns(8);
            JLabel tipoLabel = new JLabel("Tipo de Base de Datos:");
            tipoLabel.setForeground(Color.white);
            tipoLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            // Agregar opciones al ComboBox
            String[] opcionesTipo = {"utf8", "latin1", "utf16"};
            tipoBaseDatosComboBox = new JComboBox<>(opcionesTipo);

            gbc.gridx = 0;
            gbc.gridy = 0;
            add(nombreLabel, gbc);
            gbc.gridx = 1;
            add(nombreBaseDatosField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            add(tipoLabel, gbc);
            gbc.gridx = 1;
            add(tipoBaseDatosComboBox, gbc);
            
            String rutaIcono = "src/img/crdb.png"; 

            // Crea una instancia de ImageIcon
               ImageIcon icono = new ImageIcon(rutaIcono);
            // Botón de crear base de datos
            crearBaseDatosButton = new JButton("Crear Base de Datos");
            crearBaseDatosButton.setBackground(null);
            crearBaseDatosButton.setForeground(Color.WHITE);
            crearBaseDatosButton.setBorderPainted(false);
            crearBaseDatosButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            crearBaseDatosButton.setIcon(icono);
            

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 5, 10, 5);
            add(crearBaseDatosButton, gbc);
            
            
            crearBaseDatosButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {				
		            
					String nameDB= nombreBaseDatosField.getText();
		            String tipoBaseDatos= getTipoBaseDatos();
		            if (nameDB.isEmpty()) {
		                JOptionPane.showMessageDialog(null, "Por favor ingrese un nombre para la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            String query = "CREATE DATABASE " + nameDB + " CHARACTER SET " + tipoBaseDatos + ";";


		            // Ejecutar la consulta SQL
		            try {
		                Statement statement = connection.createStatement();
		                statement.executeUpdate(query);
		                JOptionPane.showMessageDialog(null, "La base de datos se creó exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
		                PanelConexion panel=new PanelConexion();
		                panel.cargarDB();
		            } catch (SQLException ex) {
		                JOptionPane.showMessageDialog(null, "Error al crear la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		            }
					
					
				}
			});
        }

        // Agrega un ActionListener al botón de crear base de datos
        public void agregarActionListener(ActionListener listener) {
            crearBaseDatosButton.addActionListener(listener);
        }

        // Obtiene el nombre de la base de datos ingresado
        public String getNombreBaseDatos() {
            return nombreBaseDatosField.getText();
        }

        // Obtiene el tipo de base de datos seleccionado
        public String getTipoBaseDatos() {
            return (String) tipoBaseDatosComboBox.getSelectedItem();
        }
    }
    public class PanelCrearTabla extends JPanel {
        private JTextField nombreTablaField;
        private JSpinner numColumnasSpinner;
        private JButton crearTablaButton;

        public PanelCrearTabla() {
        	setBackground(Color.decode("#156598"));
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(20, 5, 40, 0);

            // Etiquetas y campos de texto
            JLabel nombreLabel = new JLabel("Nombre de la Tabla:");
            nombreLabel.setForeground(Color.white);
            nombreLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nombreTablaField = new JTextField();
            nombreTablaField.setColumns(8);
            JLabel numColumnasLabel = new JLabel("Número de Columnas: ");
            numColumnasLabel.setForeground(Color.white);
            numColumnasLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            // Configuración del Spinner
            SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
            numColumnasSpinner = new JSpinner(spinnerModel);

            gbc.gridx = 0;
            gbc.gridy = 0;
            add(nombreLabel, gbc);
            gbc.gridx = 1;
            add(nombreTablaField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            add(numColumnasLabel, gbc);
            gbc.gridx = 1;            
            add(numColumnasSpinner, gbc);
            
            String rutaIcono = "src/img/createTabla.png"; 

            // Crea una instancia de ImageIcon
               ImageIcon icono = new ImageIcon(rutaIcono);
            // Botón de crear tabla
            crearTablaButton = new JButton("Crear Tabla");
            crearTablaButton.setBackground(null);
            crearTablaButton.setForeground(Color.WHITE);
            crearTablaButton.setBorderPainted(false);
            crearTablaButton.setIcon(icono);
            crearTablaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            crearTablaButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    crearTablaButton.setBackground(new Color(0x3498DB)); // Cambia el color cuando el mouse entra
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    crearTablaButton.setBackground(null);; // Restaura el color cuando el mouse sale
                }
            });

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 5, 10, 5);
            add(crearTablaButton, gbc);
            
            crearTablaButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mostrarPanelAtributos();
					
					
				}
			});
        }
        private void mostrarPanelAtributos() {
            // Crea y muestra el panel de conexión en el PanelDerecho
        	int numColumnas = getNumColumnas();
        	String tabla=getNombreTabla();
        	PanelDatosColumnas panelConexion = new PanelDatosColumnas(numColumnas,tabla);
            panelDerecho.removeAll(); // Limpia el contenido actual
            panelDerecho.add(panelConexion); // Agrega el nuevo panel
            panelDerecho.revalidate();
            panelDerecho.repaint();
        }
        // Agrega un ActionListener al botón de crear tabla
        public void agregarActionListener(ActionListener listener) {
            crearTablaButton.addActionListener(listener);
        }

        // Obtiene el nombre de la tabla ingresado
        public String getNombreTabla() {
            return nombreTablaField.getText();
        }

        // Obtiene el número de columnas seleccionado
        public int getNumColumnas() {
            return (int) numColumnasSpinner.getValue();
        }
    }
    public class PanelDatosColumnas extends JPanel {
        private static int y;
        private JButton crearTablaButton;
        private JButton regresarButton;
        private JTextField[] nombreCampoFields;
        private JComboBox<String>[] tipoDatoCombos;
        private JSpinner[] longitudSpinners;
        private JCheckBox[] pkCheckBoxes;
        private JCheckBox[] fkCheckBoxes;
        private JCheckBox[] nnCheckBoxes;

        public PanelDatosColumnas(int numColumnas,String nameTabla) {
        	setBackground(Color.decode("#156598"));
        	setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            nombreCampoFields = new JTextField[numColumnas];
            tipoDatoCombos = new JComboBox[numColumnas];
            longitudSpinners = new JSpinner[numColumnas];
            pkCheckBoxes = new JCheckBox[numColumnas];
            fkCheckBoxes = new JCheckBox[numColumnas];
            nnCheckBoxes = new JCheckBox[numColumnas];
          

            // Encabezados
            JLabel Columnas = new JLabel("Columnas");
            Columnas.setForeground(Color.white);
            Columnas.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel nombreLabel = new JLabel("Nombre de la Base de Datos:");
            nombreLabel.setForeground(Color.white);
            nombreLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel Name = new JLabel("Name");
            Name.setForeground(Color.white);
            Name.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel Tipo = new JLabel("Tipo de Dato");
            Tipo.setForeground(Color.white);
            Tipo.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel Long = new JLabel("Long");
            Long.setForeground(Color.white);
            Long.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel PK = new JLabel("PK");
            PK.setForeground(Color.white);
            PK.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel FK = new JLabel("FK");
            FK.setForeground(Color.white);
            FK.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel NN = new JLabel("NN");
            NN.setForeground(Color.white);
            NN.setFont(new Font("Arial", Font.BOLD, 14));
            
            add(Columnas);
            add(Name);
            add(Tipo);
            add(Long);
            add(PK);
            add(FK);
            add(NN);

            for (int i = 0; i < numColumnas; i++) {
                // Agrega los componentes para cada columna
                JLabel label = new JLabel("Columna " + (i + 1) + ":");
                label.setForeground(Color.white);
                label.setFont(new Font("Arial", Font.BOLD, 14));
                nombreCampoFields[i] = new JTextField(10);
                tipoDatoCombos[i] = new JComboBox<>(new String[]{"INT","SMALLINT", "BIGINT", "TINYINT","DECIMAL(p, s)",
                		"FLOAT(p)", "REAL","CHAR(n)", "VARCHAR(n)", "TEXT", "VARCHAR", "CLOB","DATE", "TIME", "DATETIME", "TIMESTAMP","BOOLEAN", "BIT"}); // Agrega tus tipos de datos
                 longitudSpinners[i] = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
                 pkCheckBoxes[i] = new JCheckBox("PK");
                 fkCheckBoxes[i] = new JCheckBox("FK");
                 nnCheckBoxes[i] = new JCheckBox("NN");

                gbc.gridx = 0;
                gbc.gridy = i+1;
                add(label, gbc);

                gbc.gridx = 1;
                gbc.gridy = i+1;
                add(nombreCampoFields[i], gbc);

                gbc.gridx = 2;
                gbc.gridy = i+1;
                add(tipoDatoCombos[i], gbc);

                gbc.gridx = 3;
                gbc.gridy = i+1;
                add(longitudSpinners[i], gbc);

                gbc.gridx = 4;
                gbc.gridy = i+1;
                add(pkCheckBoxes[i], gbc);

                gbc.gridx = 5;
                gbc.gridy = i+1;
                add(fkCheckBoxes[i], gbc);

                gbc.gridx = 6;
                gbc.gridy = i+1;
                add(nnCheckBoxes[i], gbc);
                
                y=i+1;
            }
            String rutaIcono = "src/img/createTabla.png"; 

            // Crea una instancia de ImageIcon
               ImageIcon icono = new ImageIcon(rutaIcono);
               String rutaIcono2 = "src/img/reg.png"; 

               // Crea una instancia de ImageIcon
                  ImageIcon icono1 = new ImageIcon(rutaIcono2);
            // Botones
            crearTablaButton = new JButton("Crear Tabla");
            crearTablaButton.setBackground(null);
            crearTablaButton.setForeground(Color.WHITE);
            crearTablaButton.setBorderPainted(false);
            crearTablaButton.setIcon(icono);
            crearTablaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            regresarButton = new JButton();
            regresarButton.setBackground(null); // Cambia el color de fondo del botón
            regresarButton.setForeground(Color.WHITE); // Cambia el color del texto del botón
            regresarButton.setBorderPainted(false);
            regresarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            regresarButton.setIcon(icono1);
            crearTablaButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    crearTablaButton.setBackground(new Color(0x3498DB)); // Cambia el color cuando el mouse entra
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    crearTablaButton.setBackground(null);; // Restaura el color cuando el mouse sale
                }
            });
            gbc.gridx = 0;
            gbc.gridy = y+1;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 5, 10, 5);
            add(crearTablaButton,gbc);
            gbc.gridy = y+2;
            add(regresarButton,gbc);
            
            
            
              crearTablaButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {				
		            

		            if (nameTabla.isEmpty()) {
		                JOptionPane.showMessageDialog(null, "Por favor ingrese un nombre para la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            StringBuilder query = new StringBuilder("CREATE TABLE ");
		            query.append(DB).append(".").append(nameTabla).append(" (");

		            for (int i = 0; i < numColumnas; i++) {		            	

		                String nombreCampo = nombreCampoFields[i].getText();
		                String tipoDato = (String) tipoDatoCombos[i].getSelectedItem();
		                int longitud = (int) longitudSpinners[i].getValue();
		                boolean esPK = pkCheckBoxes[i].isSelected();
		                boolean esFK = fkCheckBoxes[i].isSelected();
		                boolean esNN = nnCheckBoxes[i].isSelected();

		                // Agregar la columna a la consulta SQL
		                query.append(nombreCampo).append(" ").append(tipoDato).append("(").append(longitud).append(")");

		                if (esPK) {
		                    query.append(" PRIMARY KEY");
		                }

		                if (esFK) {
		                    query.append(" FOREIGN KEY");
		                    // Agregar la lógica para las claves foráneas si es necesario
		                }

		                if (esNN) {
		                    query.append(" NOT NULL");
		                }

		                if (i < numColumnas - 1) {
		                    query.append(", ");
		                }
		            }

		            query.append(");");

		            // Ejecutar la consulta SQL
		            try {
		                Statement statement = connection.createStatement();
		                statement.executeUpdate(query.toString());
		                JOptionPane.showMessageDialog(null, "La tabla se creó exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
		                cargarTabla(nameTabla);
		            } catch (SQLException ex) {
		                JOptionPane.showMessageDialog(null, "Error al crear la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		            }	
					
					
				}
			});
              regresarButton.addActionListener(new ActionListener() {
  				
  				@Override
  				public void actionPerformed(ActionEvent e) {
  				mostrarPanelCrearTabla();
  				}
  		            

  			});
        }
        
        // Agrega un ActionListener a los botones
        private void crearTabla() {
             
            
        }
       
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	GestorDB interfaz = new GestorDB();
            interfaz.setVisible(true);
        });
    }
}
