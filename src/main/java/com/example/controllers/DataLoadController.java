package com.example.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.ActivoModel;
import com.example.dao.Dao;
import com.example.dao.LoadPropertierModel;
import com.example.dao.PropertierModel;

@RestController
public class DataLoadController {

	private Dao objDao = new Dao();

	@PostMapping("/loadData")
	@ResponseBody
	public ResponseEntity<PropertierModel> loadData(
			@RequestParam(name = "IdActivo") int IdActivo
			) {  

		PropertierModel objPropertierModel = objDao.findOwner(IdActivo);

		return ResponseEntity.ok(objPropertierModel);
	}

	@PostMapping("/deleteActive")
	@ResponseBody
	public ResponseEntity<Boolean> deleteActive(@RequestParam(name = "IdActivo") int IdActivo) {
		System.out.println("Deleting in contorller: " + IdActivo);

		return ResponseEntity.ok(objDao.deleteActive(IdActivo));
	}

	@PostMapping("/searchAnActive")
	@ResponseBody
	public ResponseEntity<Boolean> searchAnActive(@RequestParam(name = "NewIdActive") int NewIdActive) {
		if (NewIdActive == 0) {
			return ResponseEntity.ok(false); // Return false, input is empty
		} else if(NewIdActive < 0){
			return ResponseEntity.ok(true); // Return true, input is negative
		}else{
			return ResponseEntity.ok(objDao.checkIfAnActiveAlreadyExists(NewIdActive));
		}
	}

	@GetMapping("/checkNextActiveIdAvailable")
	@ResponseBody
	public ResponseEntity<Integer> checkNextActiveIdAvailable() {
		ArrayList<ActivoModel> AllActives = objDao.getAllActives();
		List<Integer> list = AllActives.stream()
				.map(activo -> Integer.parseInt(activo.getIdActivo()))
				.collect(Collectors.toList());	

		Collections.sort(list);

		if (list.isEmpty()) {
			return ResponseEntity.ok(1);
		}else {
			System.out.println("List: " + list);
			int nextIdAvailable = list.get(list.size() - 1);
			// 1, 2, 3, 4, 5, 7, 8, 9, 10, 15 
			// 11 next Id
			for (int i = list.get(0) ; i < list.size() - 1; i++) {
				if(
						list.get(i) + 1 != list.get(i + 1)
						&& list.get(i) < nextIdAvailable
						) {
					nextIdAvailable = list.get(i);
				}
			}

			System.out.println("Next ID: " + (nextIdAvailable + 1));
			return ResponseEntity.ok(nextIdAvailable + 1);
		}
	}


	@PostMapping("/checkIfNewOwnerExists")
	@ResponseBody
	public ResponseEntity<Boolean> checkIfOwnerExists(@RequestParam(name = "IdPropietario", defaultValue = "0") String IdPropietario) {
		System.out.println("Checking if owner exists: " + IdPropietario);
		return ResponseEntity.ok(objDao.checkIfOwnerExists(Integer.parseInt(IdPropietario)));
	}

	@PostMapping("/loadOwnerInfo")
	@ResponseBody
	public ResponseEntity<LoadPropertierModel> loadOwnerInfo(@RequestParam(name = "IdPropietario") String IdPropietario) {
		return ResponseEntity.ok(objDao.LoadOwnerInfo(Integer.parseInt(IdPropietario)));
	}

	@PostMapping("/addAnActive")
	@ResponseBody
	public ResponseEntity<Boolean> addAnActive(
			@RequestParam(name = "NewIdActive") int NewIdActive, 
			@RequestParam(name = "NewActiveName") String NewActiveName,
			@RequestParam(name = "ActiveValue") float ActiveValue, 
			@RequestParam(name = "purchaseDate") String purchaseDate,
			@RequestParam(name = "IvaValue") float IvaValue,
			@RequestParam(name = "valueDepreciated") String valueDepreciated,
			@RequestParam(name = "activeLocation") String activeLocation,
			@RequestParam(name = "activeStatus") String activeStatus, 
			@RequestParam(name = "activeChecked") String activeChecked,
			@RequestParam(name = "activeDetails") String activeDetails,
			@RequestParam(name = "activeDescription") String activeDescription
			){

		ActivoModel newActivoModel = new ActivoModel();
		newActivoModel.setIdActivo(String.valueOf(NewIdActive));
		newActivoModel.setNombre(NewActiveName);
		newActivoModel.setTotal(ActiveValue);
		newActivoModel.setFecha_adqui(purchaseDate);
		newActivoModel.setIVA(IvaValue);
		newActivoModel.setValor_depreciado(Float.parseFloat(valueDepreciated));
		newActivoModel.setUbicacion(activeLocation);
		newActivoModel.setStatus(activeStatus);
		newActivoModel.setFecha_revisado(activeChecked);
		newActivoModel.setDetalle(activeDetails);
		newActivoModel.setDescripcion(activeDescription);

		objDao.addAnActive(newActivoModel);

		return ResponseEntity.ok(true);
	}
	
	@PostMapping("/addAnOwnerAndAnActive")
	@ResponseBody
	// returns the key of the new owner's location in the database
	public ResponseEntity<Integer> addAnOwner(
			// data from the owner
			@RequestParam(name = "IdPropertier") int IdPropertier,
			@RequestParam(name = "nombre") String nombre, 
			@RequestParam(name = "calle") String calle,
			@RequestParam(name = "numero") String numero,
			@RequestParam(name = "colonia") String colonia,
			@RequestParam(name = "municipio") String municipio,
			@RequestParam(name = "estado") String estado,
			@RequestParam(name = "cp") String cp,
			
			// data from the active
			@RequestParam(name = "NewIdActive") int NewIdActive,
			@RequestParam(name = "NewActiveName") String NewActiveName,
			@RequestParam(name = "ActiveValue") float ActiveValue,
			@RequestParam(name = "purchaseDate") String purchaseDate,
			@RequestParam(name = "IvaValue") float IvaValue,
			@RequestParam(name = "valueDepreciated") String valueDepreciated,
			@RequestParam(name = "activeStatus") String activeStatus,
			@RequestParam(name = "activeChecked") String activeChecked,
			@RequestParam(name = "activeDetails") String activeDetails,
			@RequestParam(name = "activeDescription") String activeDescription
            ) {
		
		System.out.println("Adding an owner: " + IdPropertier + " " + nombre + " " + calle + " " + numero + " " + colonia + " " + municipio + " " + estado + " " + cp);
	
		System.out.println("Adding an active: " + NewIdActive + " " + NewActiveName + " " + ActiveValue + " " + purchaseDate + " " + IvaValue + " " + valueDepreciated + " " + activeStatus + " " + activeChecked + " " + activeDetails + " " + activeDescription);
		int activeLocation = objDao.addAnOwner(IdPropertier, nombre, calle, numero, colonia, municipio, estado, cp);
		
		// adds the info in activo table
		addAnActive(
				NewIdActive, 
				NewActiveName, 
				ActiveValue, 
				purchaseDate, 
				IvaValue, 
				valueDepreciated, 
				String.valueOf(activeLocation), 
				activeStatus, 
				activeChecked,
				activeDetails, 
				activeDescription
			);
		
		// adds the info in propietario and ubicacion tables
		objDao.addAnOwner(IdPropertier, nombre, calle, numero, colonia, municipio, estado, cp); 
		
		// adds the info in activopropietario table
		objDao.addAnActiveWithAnExistingOwner(String.valueOf(NewIdActive), String.valueOf(IdPropertier));
		
		return ResponseEntity.ok(activeLocation);
	}
	
	@PostMapping("/addAnActiveWithAnExistingOwner")
	public void addAnActiveWithAnExistingOwner(
			@RequestParam(name = "NewIdActive") int NewIdActive,
			@RequestParam(name = "NewActiveName") String NewActiveName,
			@RequestParam(name = "ActiveValue") float ActiveValue,
			@RequestParam(name = "purchaseDate") String purchaseDate, 
			@RequestParam(name = "IvaValue") float IvaValue,
			@RequestParam(name = "valueDepreciated") String valueDepreciated,
			@RequestParam(name = "newIdOwner") String newIdOwner,
			@RequestParam(name = "activeStatus") String activeStatus,
			@RequestParam(name = "activeChecked") String activeChecked,
			@RequestParam(name = "activeDetails") String activeDetails,
			@RequestParam(name = "activeDescription") String activeDescription
		) {
			addAnActive(
					NewIdActive, 
					NewActiveName, 
					ActiveValue, 
					purchaseDate, 
					IvaValue, 
					valueDepreciated,
					newIdOwner, 
					activeStatus, 
					activeChecked, 
					activeDetails, 
					activeDescription
				);
			
			objDao.addAnActiveWithAnExistingOwner(String.valueOf(NewIdActive), newIdOwner);
			
	}
	
	@PostMapping("/loadActiveToUpdate")
	@ResponseBody
	public ResponseEntity<ActivoModel> loadActiveToUpdate(@RequestParam(name = "IdActivo") int IdActivo) {
		return ResponseEntity.ok(objDao.getOneActive(IdActivo));
	}
}
