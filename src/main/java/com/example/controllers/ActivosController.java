package com.example.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dao.ActivoModel;
import com.example.dao.ActivoService;
import com.example.dao.Dao;

@Controller
public class ActivosController {

	Dao objDao = new Dao();

	@Autowired
	private ActivoService activoService;


	@GetMapping({"/activos", "/"})
	public String activos(
			@RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size,
			Model objModel
			) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(4);

		Page<ActivoModel> activosPage = activoService.getAll(currentPage - 1, pageSize, objDao.getAllActives());
		objModel.addAttribute("activos", activosPage);

		int totalPages = activosPage.getTotalPages();

		if(totalPages > 0)
		{
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			objModel.addAttribute("pageNumbers", pageNumbers);

		}
		return "activos";
	}

	@GetMapping("/consult")
	public String consult(
			@RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size,
			@RequestParam(name = "nombre") String _nombre,
			@RequestParam(name = "status") String _status,
			@RequestParam(name = "ubicacion", defaultValue = "0") String _ubicacion,
			@RequestParam(name = "fecha_inicial", defaultValue ="1000-01-01") String _fechaInicial,
			@RequestParam(name = "fecha_final", defaultValue = "9999-12-31") String _fechaFinal,
			Model objModel
		){

		String ubicacion = checarUbicacion(_ubicacion);
		String status = checarEstatus(_status);
		String nombre = _nombre;		
		String fechaInicial = _fechaInicial.isEmpty() ? _fechaInicial : "1000-01-01";
		String fechaFinal = _fechaFinal.isEmpty() ? _fechaFinal : "9999-12-31";
		// for some reason user writes the date in the format dd/mm/yyyy but here is received as yyyy/mm/dd 
		// which is perfect because that's how in the DB is used (yyyy/mm/dd)

		ArrayList<ActivoModel> activos = objDao.getWithParams(
				nombre,
				status, 
				ubicacion,
				fechaInicial,
				fechaFinal
			);


		int currentPage = page.orElse(1);
		int pageSize = size.orElse(4);

		if(activos.isEmpty())
		{
			objModel.addAttribute("activos", new ArrayList<ActivoModel>());
		}
		else {
			Page<ActivoModel> activosPage = activoService.getAll(
					currentPage - 1, 
					pageSize, 
					activos
					);
			objModel.addAttribute("activos", activosPage);

			int totalPages = activosPage.getTotalPages();

			if(totalPages > 0)
			{
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
						.boxed()
						.collect(Collectors.toList());
				objModel.addAttribute("pageNumbers", pageNumbers);

			}
		}



		objModel.addAttribute("nombre", _nombre);
		objModel.addAttribute("status", _status);
		objModel.addAttribute("ubicacion", _ubicacion);
		objModel.addAttribute("fecha_inicial", _fechaInicial);
		objModel.addAttribute("fecha_final", _fechaFinal);

		return "activos";
	}

	public String checarUbicacion(String _ubicacion) {
		switch (_ubicacion) {
		case "1": return "CDMX";   
		case "2": return "Estado de Mexico";
		case "3": return "En remoto";
		default: return ""; // cualquiera
		}
	}

	public String checarEstatus(String estatus) {
		switch (estatus) {
		case "1": return "En uso";
		case "2": return "Descompuesto";
		case "3": return "Almacenado";
		case "4": return "No encontrado";
		default: return ""; // cualquiera
		}
	}
}

