package com.example.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertierModel {
	@JsonProperty("IdPropertier")
	private String IdPropertier;
	
	@JsonProperty("nombre")
	private String nombre;
	
	@JsonProperty("ubicacion")
	private String ubicacion;
	

	public PropertierModel() {
		super();
	}
	
	public String getIdPropertier() {
		return IdPropertier;
	}
	public void setIdPropertier(String idPropertier) {
		IdPropertier = idPropertier;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	
}
