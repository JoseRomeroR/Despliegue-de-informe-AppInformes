/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package AppInformes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Kyle7
 */
public class AppInformes extends Application {

    public static Connection conexion = null;

    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //establecemos la conexión con la BD
        conectaBD();

        // Crea el menú
        Menu fileMenu = new Menu("Informes");
        MenuItem openFacturas = new MenuItem("Facturas");
        MenuItem openFacturasCliente = new MenuItem("Facturas por cliente");
        MenuItem openVentasTotales = new MenuItem("Ventas totales");
        MenuItem openSubInformeFacturas = new MenuItem("Subinforme listado facturas");
        fileMenu.getItems().addAll(openFacturas, openVentasTotales, openFacturasCliente, openSubInformeFacturas);

        // Crea la barra de menú
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        openFacturas.setOnAction(event -> {
            generaInformeFacturas();
        });

        openVentasTotales.setOnAction(event -> {
            generaInformeVentasTotales();
        });

        openFacturasCliente.setOnAction(event -> {
            TextInputDialog td = new TextInputDialog();
            td.setTitle("Parámetro:");
            td.setHeaderText("Introduzca el parámetro");

            td.showAndWait();
            generaInformePorCliente(td.getEditor().getText());
        });

        openSubInformeFacturas.setOnAction(event -> {
            generaSubInforme();
        });

        VBox root = new VBox();
        root.getChildren().addAll(menuBar);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Obtener informe");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        try {

            DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/sampleDB");
        } catch (Exception ex) {
            System.out.println("No se pudo cerrar la conexion a la BD");
        }

    }

    public void conectaBD() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //Establecemos conexión con la BD
        String baseDatos = "jdbc:hsqldb:hsql://localhost/SampleDB";
        String usuario = "sa";
        String clave = "";

        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            conexion = DriverManager.getConnection(baseDatos, usuario, clave);

        } catch (ClassNotFoundException cnfe) {
            System.err.println("Fallo al cargar JDBC");
            System.exit(1);
        } catch (SQLException sqle) {
            System.err.println("No se pudo conectar a BD");
            System.exit(1);
        } catch (java.lang.InstantiationException sqlex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        } catch (Exception ex) {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }

    }

    public void generaInformeFacturas() {
        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("Facturas.jasper "));
            //Map de parámetros
            Map parametros = new HashMap();
            //int nproducto = Integer.valueOf(tintro.getText());
            //int nproducto = 1;
            //parametros.put("cod_cliente", nproducto);

            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,
                    parametros, conexion);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {

            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void generaInformeVentasTotales() {
        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("Ventas_totales.jasper "));
            //Map de parámetros
            Map parametros = new HashMap();

            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,
                    parametros, conexion);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {

            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void generaInformePorCliente(String tintro) {
        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("Facturas_por_cliente.jasper "));
            //Map de parámetros
            Map parametros = new HashMap();
            int nproducto = Integer.valueOf(tintro);
            parametros.put("cod_cliente", nproducto);

            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,
                    parametros, conexion);
            JasperViewer.viewReport(jp);
        } catch (JRException ex) {

            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void generaSubInforme() {

        try {
            JasperReport jr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("Subinforme_Listado_Facturas.jasper"));
            JasperReport jsr = (JasperReport) JRLoader.loadObject(AppInformes.class.getResource("subInformeFactura.jasper"));
//Map de parámetros
            Map parametros = new HashMap();
            parametros.put("subInformeParameter", jsr);
//Ya tenemos los datos para instanciar un objeto JasperPrint que permite ver,
//imprimir o exportar a otros formatos
            JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr, parametros,
                    conexion);
            JasperViewer.viewReport(jp, false);


        } catch (JRException ex) {

            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
