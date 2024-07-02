package com.example.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

/* PropertierModel is used to 
 * return owner's info of an active.
 * LoadPropertierModel is used  in   to load owner's info of an active 
 * when creating or updating an active.
*/
public class LoadPropertierModel {
	@JsonProperty("IdPropertier")
	private String IdPropertier;
	
	@JsonProperty("nombre")
	private String nombre;
	
	@JsonProperty("calle")
	private String calle;
	
	@JsonProperty("numero")
	private String numero;
	
	@JsonProperty("colonia")
	private String colonia;
	
	@JsonProperty("municipio")
	private String municipio;
	
	@JsonProperty("estado")
	private String estado;
	
	@JsonProperty("cp")
	private String cp;
	
	public LoadPropertierModel() {
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

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getColonia() {
		return colonia;
	}

	public void setColonia(String colonia) {
		this.colonia = colonia;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}
	
}
