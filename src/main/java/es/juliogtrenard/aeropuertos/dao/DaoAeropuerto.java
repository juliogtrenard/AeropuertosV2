package es.juliogtrenard.aeropuertos.dao;

import es.juliogtrenard.aeropuertos.db.DBConnect;
import es.juliogtrenard.aeropuertos.modelos.Aeropuerto;
import es.juliogtrenard.aeropuertos.modelos.Direccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

/**
 * Ejecuta las consultas para la tabla Aeropuertos
 */
public class DaoAeropuerto {
    /**
     * Busca un aeropuerto por medio de su id
     *
     * @param id id del aeropuerto a buscar
     * @return aeropuerto o null
     */
    public static Aeropuerto getAeropuerto(int id) {
        DBConnect connection;
        Aeropuerto aeropuerto = null;
        try {
            connection = new DBConnect();
            String consulta = "SELECT id,nombre,anio_inauguracion,capacidad,id_direccion,imagen FROM aeropuertos WHERE id = ?";
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
                aeropuerto = new Aeropuerto(id_aeropuerto,nombre,anio_inauguracion,capacidad,direccion,imagen);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return aeropuerto;
    }

    /**
     * Carga los datos de la tabla Aeropuertos y los devuelve para usarlos en un listado de aeropuertos
     *
     * @return listado de aeropuertos para cargar en un tableview
     */
    public static ObservableList<Aeropuerto> cargarListado() {
        DBConnect connection;
        ObservableList<Aeropuerto> airportList = FXCollections.observableArrayList();
        try{
            connection = new DBConnect();
            String consulta = "SELECT id,nombre,anio_inauguracion,capacidad,id_direccion,imagen FROM aeropuertos";
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
                Aeropuerto airport = new Aeropuerto(id,nombre,anio_inauguracion,capacidad,direccion,imagen);
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
    public static boolean modificar(Aeropuerto aeropuerto, Aeropuerto aeropuertoNuevo) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "UPDATE aeropuertos SET nombre = ?,anio_inauguracion = ?,capacidad = ?,id_direccion = ?,imagen = ? WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, aeropuertoNuevo.getNombre());
            pstmt.setInt(2, aeropuertoNuevo.getAnio_inauguracion());
            pstmt.setInt(3, aeropuertoNuevo.getCapacidad());
            pstmt.setInt(4, aeropuertoNuevo.getDireccion().getId());
            pstmt.setBlob(5, aeropuertoNuevo.getImagen());
            pstmt.setInt(6, aeropuerto.getId());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Crea un nuevo aeropuerto en la BD
     *
     * @param aeropuerto Aeropuerto con datos nuevos
     * @return id/-1
     */
    public  static int insertar(Aeropuerto aeropuerto) {
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "INSERT INTO aeropuertos (nombre,anio_inauguracion,capacidad,id_direccion,imagen) VALUES (?,?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, aeropuerto.getNombre());
            pstmt.setInt(2, aeropuerto.getAnio_inauguracion());
            pstmt.setInt(3, aeropuerto.getCapacidad());
            pstmt.setInt(4, aeropuerto.getDireccion().getId());
            pstmt.setBlob(5, aeropuerto.getImagen());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pstmt.close();
                    connection.closeConnection();
                    return id;
                }
            }
            pstmt.close();
            connection.closeConnection();
            return -1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    /**
     * Elimina un aeropuerto
     *
     * @param aeropuerto Aeropuerto a eliminar
     * @return boolean Si se puede eliminar o no
     */
    public  static boolean eliminar(Aeropuerto aeropuerto){
        DBConnect connection;
        PreparedStatement pstmt;
        try {
            connection = new DBConnect();
            String consulta = "DELETE FROM aeropuertos WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, aeropuerto.getId());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConnection();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Convierte un objeto File a Blob
     *
     * @param file fichero imagen
     * @return blob el blob de imagen
     * @throws SQLException Error de sql
     * @throws IOException Error de E/S de datos
     */
    public static Blob convertFileToBlob(File file) throws SQLException, IOException {
        DBConnect connection = new DBConnect();

        try (Connection conn = connection.getConnection();
             FileInputStream inputStream = new FileInputStream(file)) {

            Blob blob = conn.createBlob();

            byte[] buffer = new byte[1024];
            int bytesRead;

            try (var outputStream = blob.setBinaryStream(1)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return blob;
        }
    }
}
