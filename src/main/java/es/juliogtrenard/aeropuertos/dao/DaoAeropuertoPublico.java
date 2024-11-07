package es.juliogtrenard.aeropuertos.dao;

import es.juliogtrenard.aeropuertos.db.DBConnect;
import es.juliogtrenard.aeropuertos.modelos.Aeropuerto;
import es.juliogtrenard.aeropuertos.modelos.AeropuertoPublico;
import es.juliogtrenard.aeropuertos.modelos.Direccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ejecuta las consultas para la tabla Aeropuertos Públicos
 */
public class DaoAeropuertoPublico {
    /**
     * Busca un aeropuerto público por medio de su id
     *
     * @param id id del aeropuerto a buscar
     * @return aeropuerto público o null
     */
    public static AeropuertoPublico getAeropuerto(int id) {
        DBConnect connection;
        AeropuertoPublico aeropuerto = null;
        try {
            connection = new DBConnect();
            String consulta = "SELECT id,nombre,anio_inauguracion,capacidad,id_direccion,imagen,financiacion,num_trabajadores FROM aeropuertos,aeropuertos_publicos WHERE id=id_aeropuerto AND id = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_aeropuerto = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int anio_inauguracion = rs.getInt("anio_inauguracion");
                int capacidad = rs.getInt("capacidad");
                int id_direccion = rs.getInt("id_direccion");
                Direccion direccion = DaoDireccion.getDireccion(id_direccion);
                Blob imagen = rs.getBlob("imagen");
                Aeropuerto airport = new Aeropuerto(id_aeropuerto,nombre,anio_inauguracion,capacidad,direccion,imagen);
                BigDecimal financiacion = rs.getBigDecimal("financiacion");
                int num_trabajadores = rs.getInt("num_trabajadores");
                aeropuerto = new AeropuertoPublico(airport,financiacion,num_trabajadores);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return aeropuerto;
    }

    /**
     * Carga los datos de la tabla AeropuertoPublico y los devuelve para usarlos en un listado de aeropuertos
     *
     * @return listado de aeropuertos públicos para cargar en un tableview
     */
    public static ObservableList<AeropuertoPublico> cargarListado() {
        DBConnect connection;
        ObservableList<AeropuertoPublico> airportList = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT id,nombre,anio_inauguracion,capacidad,id_direccion,imagen,financiacion,num_trabajadores FROM aeropuertos,aeropuertos_publicos WHERE id=id_aeropuerto";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int anio_inauguracion = rs.getInt("anio_inauguracion");
                int capacidad = rs.getInt("capacidad");
                int id_direccion = rs.getInt("id_direccion");
                Direccion direccion = DaoDireccion.getDireccion(id_direccion);
                Blob imagen = rs.getBlob("imagen");
                Aeropuerto aeropuerto = new Aeropuerto(id,nombre,anio_inauguracion,capacidad,direccion,imagen);
                BigDecimal financiacion = rs.getBigDecimal("financiacion");
                int num_trabajadores = rs.getInt("num_trabajadores");
                AeropuertoPublico airport = new AeropuertoPublico(aeropuerto,financiacion,num_trabajadores);
                airportList.add(airport);
            }
            rs.close();
            connection.closeConnection();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return airportList;
    }

    /**
     * Modifica los datos de un aeropuerto en la BD
     *
     * @param aeropuerto Aeropuerto con datos nuevos
     * @param aeropuertoNuevo Nuevos datos del aeropuerto a modificar
     * @return true/false
     */
    public static boolean modificar(AeropuertoPublico aeropuerto, AeropuertoPublico aeropuertoNuevo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE aeropuertos_publicos SET financiacion = ?,num_trabajadores = ? WHERE id_aeropuerto = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setBigDecimal(1, aeropuertoNuevo.getFinanciacion());
            pstmt.setInt(2, aeropuertoNuevo.getNum_trabajadores());
            pstmt.setInt(3, aeropuerto.getAeropuerto().getId());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("hello");
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Crea un nuevo aeropuerto en la BD
     *
     * @param aeropuerto Aeropuerto con datos nuevos
     * @return true/false
     */
    public  static boolean insertar(AeropuertoPublico aeropuerto) {
        DBConnect connection;
        PreparedStatement pstmt;

        try {
            connection = new DBConnect();

            String consulta = "INSERT INTO aeropuertos_publicos (id_aeropuerto,financiacion,num_trabajadores) VALUES (?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta);

            pstmt.setInt(1, aeropuerto.getAeropuerto().getId());
            pstmt.setBigDecimal(2, aeropuerto.getFinanciacion());
            pstmt.setInt(3, aeropuerto.getNum_trabajadores());

            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un aeropuerto
     *
     * @param aeropuerto aeropuerto a eliminar
     * @return a boolean
     */
    public  static boolean eliminar(AeropuertoPublico aeropuerto){
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM aeropuertos_publicos WHERE id_aeropuerto = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, aeropuerto.getAeropuerto().getId());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

}
