package com.example.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.connection.MySqlConnection;

public class Dao {
	private MySqlConnection objMySqlConnection;
	public ArrayList<ActivoModel> totalOfActivos;

	public Dao() {
		objMySqlConnection = new MySqlConnection();
	}


	public ArrayList<ActivoModel> getAllActives(){
		ArrayList<ActivoModel> activos = new ArrayList<ActivoModel>();
		objMySqlConnection.open();

		if(!objMySqlConnection.isError()) {
			ResultSet result = objMySqlConnection.executeSelect(
					"SELECT a.*, l.estado AS ubicacion\r\n"
							+ "FROM activo a\r\n"
							+ "LEFT JOIN ubicacion l ON a.id_ubicacion = l.IdUbicacion\r\n"
							+ "order by IdActivo desc"
					);

			try {
				while (result.next()) {
					ActivoModel objActivoModel = new ActivoModel();
					objActivoModel.setIdActivo(result.getString("IdActivo"));
					objActivoModel.setNombre(result.getString("nombre"));
					objActivoModel.setTotal(result.getFloat("total"));
					objActivoModel.setIVA(objActivoModel.getTotal() * 0.16f);
					objActivoModel.setValor_depreciado(objActivoModel.getTotal() - objActivoModel.getIVA());
					objActivoModel.setFecha_adqui(result.getString("fecha_adqui"));
					objActivoModel.setStatus(result.getString("status"));
					objActivoModel.setDetalle(result.getString("detalle"));
					objActivoModel.setDescripcion(result.getString("descripcion"));
					objActivoModel.setUbicacion(result.getString("ubicacion"));
					objActivoModel.setFecha_revisado(result.getString("fecha_revisado"));
					activos.add(objActivoModel);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				objMySqlConnection.close();
			}
		}
		totalOfActivos = activos;
		return activos;
	}
	
	public ActivoModel getOneActive(int IdActivo) {
		ActivoModel objActivoModel = new ActivoModel();
        objMySqlConnection.open();
        if(!objMySqlConnection.isError()) {
            ResultSet result = objMySqlConnection.executeSelect("SELECT * FROM activo WHERE IdActivo = " + IdActivo);
            try {
                if(result != null && result.next()){
					objActivoModel.setIdActivo(result.getString("IdActivo"));
					objActivoModel.setNombre(result.getString("nombre"));
					objActivoModel.setTotal(result.getFloat("total"));
					objActivoModel.setIVA(objActivoModel.getTotal() * 0.16f);
					objActivoModel.setValor_depreciado(objActivoModel.getTotal() - objActivoModel.getIVA());
					objActivoModel.setFecha_adqui(result.getString("fecha_adqui"));
					objActivoModel.setStatus(result.getString("status"));
					objActivoModel.setDetalle(result.getString("detalle"));
					objActivoModel.setDescripcion(result.getString("descripcion"));
					objActivoModel.setUbicacion(result.getString("id_ubicacion"));
					objActivoModel.setFecha_revisado(result.getString("fecha_revisado"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				objMySqlConnection.close();
			}
		}
		return objActivoModel;
	}
	

	public boolean checkIfAnActiveAlreadyExists(int IdActivo) {
		boolean activeExists = false;	
		objMySqlConnection.open();

		if(!objMySqlConnection.isError()) {

			ResultSet result = objMySqlConnection.executeSelect("SELECT * FROM activo WHERE IdActivo = " + IdActivo);
			try {
				if (result != null && result.next()) {
					activeExists = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				objMySqlConnection.close();
			}
		}
		return activeExists;
	}

	public boolean checkIfOwnerExists(int IdPropietario) {
		boolean ownerExists = false;
		objMySqlConnection.open();

		if(!objMySqlConnection.isError()) {

			ResultSet result = objMySqlConnection.executeSelect("SELECT * FROM propietario WHERE IdPropietario = " + IdPropietario);
			try {
				if (result != null && result.next()) {
					ownerExists = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				objMySqlConnection.close();
			}
		}
		return ownerExists;
	}

	public ArrayList<ActivoModel> getWithParams(
			String nombre,
			String status,
			String ubicacion,
			String fechaInicial,
			String fechaFinal
			){
		ArrayList<ActivoModel> activos = new ArrayList<ActivoModel>();
		objMySqlConnection.open();

		if(!objMySqlConnection.isError()) {
			ResultSet result;

			if (ubicacion.equals("En remoto")) {
				// if ubicacion is "En remoto" then filter out CDMX and Estado de Mexico
				result = objMySqlConnection.executeSelect(
						"SELECT a.*, l.estado AS ubicacion "
								+ "FROM activo a "
								+ "LEFT JOIN ubicacion l ON a.id_ubicacion = l.IdUbicacion "
								+ "where nombre like \"%" + nombre + "%\" "
								+ "and status like \"%" + status + "%\" "
								+ "and l.estado not like \"%CDMX%\" " 
								+ "and l.estado not like \"%Estado de Mexico%\" "
								+ "and fecha_adqui >= \"" + fechaInicial + "\" "
								+ "and fecha_adqui <= \"" + fechaFinal + "\" "
								+ "order by IdActivo desc"
						);
			}
			else if(status.equals("No encontrado") || status.equals("")){
				result = objMySqlConnection.executeSelect(
						"SELECT a.*, COALESCE(l.estado, 'no encontrado') AS ubicacion "
								+ "FROM activo a "
								+ "LEFT JOIN ubicacion l ON a.id_ubicacion = l.IdUbicacion "
								+ "where nombre like \"%" + nombre + "%\" "
								+ "and status like \"%" + status + "%\" "
								+ "and (l.estado is null or l.estado like \"%" + ubicacion + "%\")"
								+ "and fecha_adqui >= \"" + fechaInicial + "\" "
								+ "and fecha_adqui <= \"" + fechaFinal + "\" "
								+ "order by IdActivo desc"
						);
			}else {
				result = objMySqlConnection.executeSelect(
						"SELECT a.*, l.estado AS ubicacion "
								+ "FROM activo a "
								+ "LEFT JOIN ubicacion l ON a.id_ubicacion = l.IdUbicacion "
								+ "where nombre like \"%" + nombre + "%\" "
								+ "and status like \"%" + status + "%\" "
								+ "and l.estado like \"%" + ubicacion + "%\" " 
								+ "and fecha_adqui >= \"" + fechaInicial + "\" "
								+ "and fecha_adqui <= \"" + fechaFinal + "\" "
								+ "order by IdActivo desc"
						);
			}

			try {
				while (result.next()) {
					ActivoModel objActivoModel = new ActivoModel();
					objActivoModel.setIdActivo(result.getString("IdActivo"));
					objActivoModel.setNombre(result.getString("nombre"));
					objActivoModel.setTotal(result.getFloat("total"));
					objActivoModel.setIVA(objActivoModel.getTotal() * 0.16f);
					objActivoModel.setValor_depreciado(objActivoModel.getTotal() - objActivoModel.getIVA());
					objActivoModel.setFecha_adqui(result.getString("fecha_adqui"));
					objActivoModel.setStatus(result.getString("status"));
					objActivoModel.setDetalle(result.getString("detalle"));
					objActivoModel.setDescripcion(result.getString("descripcion"));
					objActivoModel.setUbicacion(result.getString("ubicacion"));
					objActivoModel.setFecha_revisado(result.getString("fecha_revisado"));
					activos.add(objActivoModel);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				objMySqlConnection.close();
			}
		}

		return activos;
	}

	public void addAnActive(ActivoModel objActivoModel) {
		try {
			objMySqlConnection.open();

			if (!objMySqlConnection.isError()) {
				String sql = "INSERT INTO activo ("
						+ "IdActivo, nombre, total, IVA, valor_depreciado, fecha_adqui, status, detalle, descripcion, id_ubicacion, fecha_revisado) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = objMySqlConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, objActivoModel.getIdActivo());
				pstmt.setString(2, objActivoModel.getNombre());
				pstmt.setDouble(3, objActivoModel.getTotal());
				pstmt.setDouble(4, objActivoModel.getIVA());
				pstmt.setDouble(5, objActivoModel.getValor_depreciado());
				pstmt.setString(6, objActivoModel.getFecha_adqui());
				pstmt.setString(7, objActivoModel.getStatus());

				if ("no encontrado".equals(objActivoModel.getStatus())) {
					System.out.println("Ubicacion no encontrada");
					pstmt.setNull(8, java.sql.Types.VARCHAR);
					pstmt.setNull(9, java.sql.Types.VARCHAR);
					pstmt.setNull(10, java.sql.Types.INTEGER);
				} else {
					pstmt.setString(8, objActivoModel.getDetalle());
					pstmt.setString(9, objActivoModel.getDescripcion());
					pstmt.setInt(10, Integer.parseInt(objActivoModel.getUbicacion()));
				}
				pstmt.setString(11, objActivoModel.getFecha_revisado());

				pstmt.executeUpdate();

				ResultSet generatedKeys = pstmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					int id = generatedKeys.getInt(1);
					System.out.println("Nuevo activo insertado con ID: " + id);
				} else {
					System.out.println("No se pudo obtener el ID generado para el nuevo activo");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			objMySqlConnection.close();
		}
	}

	// return the generated key of ubicacion
	public int addAnOwner(
		    int IdPropertier,
		    String nombre,
		    String calle,
		    String numero,
		    String colonia,
		    String municipio,
		    String estado,
		    String cp
		) {
		    objMySqlConnection.open();
		    ResultSet newUbicacion = null;

		    if (!objMySqlConnection.isError()) {
		        String sql = "INSERT INTO ubicacion (IdUbicacion, calle, numero, colonia, municipio, estado, cp) VALUES (default, '" + calle + "', '" + numero + "', '" + colonia + "', '" + municipio + "', '" + estado + "', '" + cp + "')";
		        newUbicacion = objMySqlConnection.executeInsert(sql);

		        // imprime el ResultSet antes de cerrarlo
	            int generatedKey = 0; // Variable para almacenar la clave generada

	            // Obtener la clave generada de ubicacion
	            try {
					if (newUbicacion != null && newUbicacion.next()) {
					    generatedKey = newUbicacion.getInt("GENERATED_KEY");
					    System.out.println("Nueva ubicación insertada con ID: " + generatedKey);
					} else {
					    System.out.println("No se pudo obtener la clave generada para la nueva ubicación.");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	            // Insertar en la tabla propietario usando la clave generada de ubicacion
	            sql = "INSERT INTO propietario (IdPropietario, nombre, id_ubicacion) VALUES ('" + IdPropertier + "', '" + nombre + "', '" + generatedKey + "')";
	            objMySqlConnection.executeInsert(sql);
			    return generatedKey;

		    }
		    
		    return 0;
		 }

		public void addAnActiveWithAnExistingOwner(
				String NewIdActive, 
				String newIdOwner
			) {
			objMySqlConnection.open();
			if (!objMySqlConnection.isError()) {
				String sql = "INSERT INTO activopropietario (id_activo, id_propietario) VALUES ('" + NewIdActive + "', '" + newIdOwner + "')";
                objMySqlConnection.executeInsert(sql);
			}
		}


	// uncomment if you actually want to delete an active
	public boolean deleteActive(int IdActivo) {
		objMySqlConnection.open();
		boolean activeDeleted = false;

		if (!objMySqlConnection.isError()) {
			activeDeleted = objMySqlConnection.executeUpdateOrDelete("DELETE FROM activo WHERE IdActivo = " + IdActivo) == 1;
			ResultSet activeInActivoPropietarioTable = objMySqlConnection.executeSelect("select * from activopropietario where id_activo = " + IdActivo);

			try {
				// if the active is in the activopropietario table, delete it
				if (activeInActivoPropietarioTable != null && activeInActivoPropietarioTable.next()) {
					System.out.println("El activo " + IdActivo + " esta en la tabla activopropietario");
					objMySqlConnection.executeUpdateOrDelete("DELETE FROM activopropietario WHERE id_activo = " + IdActivo);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			objMySqlConnection.close();
		}

		return activeDeleted;
	}



	public LoadPropertierModel LoadOwnerInfo(int IdPropertier) {
		objMySqlConnection.open();
		LoadPropertierModel objLoadPropertierModel = new LoadPropertierModel();
		if(!objMySqlConnection.isError()) {
			ResultSet result = objMySqlConnection.executeSelect(
					"select * from propietario "
							+ "inner join ubicacion where ubicacion.IdUbicacion = propietario.id_ubicacion"
							+ " and IdPropietario = " + IdPropertier
					);
			try {
				if(result != null && result.next()){
					objLoadPropertierModel.setIdPropertier("IdPropietario");
					objLoadPropertierModel.setNombre(result.getString("nombre"));
					objLoadPropertierModel.setCalle(result.getString("calle"));
					objLoadPropertierModel.setNumero(result.getString("numero"));
					objLoadPropertierModel.setColonia(result.getString("colonia"));
					objLoadPropertierModel.setMunicipio(result.getString("municipio"));
					objLoadPropertierModel.setEstado(result.getString("estado"));
					objLoadPropertierModel.setCp(result.getString("cp"));
				}
			}catch (SQLException e) {
				e.printStackTrace();
			} finally {
				objMySqlConnection.close();
			}
		}
		return objLoadPropertierModel;

	}
	public PropertierModel findOwner(int idActivo) {
		objMySqlConnection.open();
		PropertierModel objPropertierModel = new PropertierModel();

		if(!objMySqlConnection.isError()) {
			ResultSet result = objMySqlConnection.executeSelect(
					"SELECT "
							+ "IdPropietario, "
							+ "CONCAT(nombre, ' ', apellido_paterno, ' ', apellido_materno) AS nombre, "
							+ "CONCAT(calle, ' #', numero, ' ', colonia, ' ', municipio, ' ', estado, ' ', cp) AS ubicacion "
							+ "FROM propietario, ubicacion "
							+ "WHERE IdPropietario IN (SELECT id_propietario FROM activopropietario where id_activo = " + idActivo + ") "
							+ "and IdUbicacion in "
							+ "(select id_ubicacion from activo where IdActivo in "
							+ "(select id_activo from activopropietario where id_activo = " + idActivo + "))");
			try {
				if(result != null && result.next()){
					objPropertierModel.setIdPropertier(String.valueOf(result.getInt("IdPropietario")));
					objPropertierModel.setNombre(result.getString("nombre"));
					objPropertierModel.setUbicacion(result.getString("ubicacion"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Error en la consulta");
			} 
			catch (NullPointerException e) {
				e.printStackTrace();
				System.out.println("NullPointer en la consulta");
			}
			finally {
				objMySqlConnection.close();
			}
		}

		return objPropertierModel;
	}


}
