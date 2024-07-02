package com.example.dao;

public class ActivoModel {
	private String IdActivo;
	private String nombre;
	private float total;
	private float IVA;
	private float valor_depreciado;
	private String fecha_adqui;
	private String status;
	private String detalle;
	private String descripcion;
	private String ubicacion;
	private String fecha_revisado;
	
	public ActivoModel() {
		super();
	}
	
	public String getIdActivo() {
		return IdActivo;
	}
	public void setIdActivo(String idActivo) {
		IdActivo = idActivo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public float getIVA() {
		return IVA;
	}
	public void setIVA(float iVA) {
		IVA = iVA;
	}
	public float getValor_depreciado() {
		return valor_depreciado;
	}
	public void setValor_depreciado(float valor_depreciado) {
		this.valor_depreciado = valor_depreciado;
	}
	public String getFecha_adqui() {
		return fecha_adqui;
	}
	public void setFecha_adqui(String fecha_adqui) {
		this.fecha_adqui = fecha_adqui;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFecha_revisado() {
		return fecha_revisado;
	}
	
	public void setFecha_revisado(String fecha_revisado) {
		this.fecha_revisado = fecha_revisado;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

}
