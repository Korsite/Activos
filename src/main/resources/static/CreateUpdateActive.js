// Check CreateUpdateActive.html to see the form that uses this script 
// This script is used to validate the inputs of the form to create or update an active
document.addEventListener('DOMContentLoaded', function() {
	
	var timer; // variable to store the timeout
	
	// Error messages to display
	var errorNewIdActive = $('#errorNewIdActive').hide(); 
	var errorNewActiveName = $('#errorNewActiveName').hide();
	
	// variables to listen
	var NewIdActive = document.getElementById('NewIdActive');
	var NewActiveName = document.getElementById('NewActiveName');
	var ActiveValue = document.getElementById('ActiveValue');
	var purchaseDate = document.getElementById('purchaseDate');
	var IvaValue = document.getElementById('IvaValue');
	var valueDepreciated = document.getElementById('valueDepreciated');
	var activeLocation = document.getElementById('activeLocation');
	var activeStatus = document.getElementById('activeStatus');
	var activeChecked = document.getElementById('activeChecked');
	var activeDetails = document.getElementById('activeDetails');
	var activeDescription = document.getElementById('activeDescription');
	
	var activateButtonToUpdateActive = document.getElementById('activateButtonToUpdateActive');
	var activateButtonToSaveActive = document.getElementById('activateButtonToSaveActive');
	
	
	document.getElementById('newIdOwner').value = '6';
	document.getElementById('newNameOwner').value = 'Test0';
	document.getElementById('newStreetOfOwnerLocation').value = 'Test1';
	document.getElementById('newNumberOfOwnerLocation').value = 100;
	document.getElementById('newColonyOfOwnerLocation').value = 'Test2';
	document.getElementById('newCityOfOwnerLocation').value = 'Test3';
	document.getElementById('newStateOfOwnerLocation').value = 'Test4';
	document.getElementById('newCpOfOwnerLocation').value = 10010;
	
	// Disable the button until all the inputs are valid
	var button = document.getElementById('buttonToSaveActive'); button.disabled = true;
	
	// Disable some buttons
	$('#showButtonToAddActive').hide();
	$('#showButtonToUpdateActive').hide();
	
	// Disable remote form submission, activate when activeLocation.value = "uso remoto"
	$('#remoteForm').hide();
	
	$.ajax({
		url: '/checkNextActiveIdAvailable',		
		type: 'GET',
		success: function(nextActiveIdAvailable) {
			NewIdActive.placeholder = "Sig. ID: " + nextActiveIdAvailable;
		},
	});
	
	console.log(NewIdActive.value);
    NewIdActive.addEventListener('input', function() {
		clearTimeout(timer); 
		timer = setTimeout(function() {
        	console.log('NewIdActive:', NewIdActive.value);
        	
        	$.ajax({
            	url: '/searchAnActive',
            	type: 'POST',
            	data: {
            	NewIdActive: NewIdActive.value || 0, 
            	// when input is empty, send 0, which makes
            	// activeAlreadyExistsOrInvalid return false so errorNewIdActive is hidden
            	},
          		success: function(activeAlreadyExistsOrInvalid) {
					if(activeAlreadyExistsOrInvalid) {
						errorNewIdActive.show();
						button.disabled = true;
					}else{
						if(NewIdActive.value == 0){
							NewIdActive.value = '';
						}
						checkInputs();
						errorNewIdActive.hide();
					}
				},
        	});
   
        }, 100);
    });

    NewActiveName.addEventListener('input', function() {
		// Validate that the name contains only letters and spaces
		clearTimeout(timer);
        timer = setTimeout(function() {
            console.log('Input 2:', NewActiveName.value);
            if (!/^[a-zA-Z\s]*$/.test(NewActiveName.value)) {
				errorNewActiveName.show();
				button.disabled = true;
			} else { errorNewActiveName.hide(); }
			
			checkInputs();

        }, 200); 
    });
    
    ActiveValue.addEventListener('input', function() {
		checkInputs();
		
		setTimeout(function() {
			var value = parseFloat(ActiveValue.value.replace('$', ''));
			console.log('Input 3:', value); 

			if(value > 0) { // this must be done because NaN is a possible value
				var ivaValue = value * 0.16;
				ivaValue = Math.round(ivaValue * 100) / 100; // Round 2 decimal places
				IvaValue.value = "$" + ivaValue; }
			else{
				IvaValue.value = '$0.00';
			}
		}, 250);
    });
    
    purchaseDate.addEventListener('input', function() {
		checkInputs();
	});
	
	activeLocation.addEventListener('input', function() {
		if (activeLocation.value == 'uso remoto') {
			$('#remoteForm').show();
		} else {
			$('#remoteForm').hide();
		}
		checkInputs();
	});
	
	activeStatus.addEventListener('input', function() {
		if (activeStatus.value == 'no encontrado') {
			activeLocation.value = '';
			activeDetails.value = '';
			activeDescription.value = '';
			activeLocation.disabled = true;
			activeDetails.disabled = true;
			activeDescription.disabled = true;
		}
		
		if(activeStatus.value !== 'no encontrado' && activeLocation.disabled){
			activeLocation.value = 'Seleccionar';
			activeDescription.disabled = false;
			activeLocation.disabled = false;
			activeDetails.disabled = false;
		}
		checkInputs();
	});
	
	activeChecked.addEventListener('input', function() {
		checkInputs();
	});
	
	activeDetails.addEventListener('input', function() {
		checkInputs();
	});
	
	activeDescription.addEventListener('input', function() {
		clearTimeout(timer);	
		timer = setTimeout(function() {
			console.log('Input 8:', activeDescription.value);
			checkInputs();
		}, 150);
	});
	
	$('.ActiveUpdateButton').click(function(){
		$('#showButtonToUpdateActive').show();
		$('#showButtonToAddActive').hide();
   		var idActivo = $(this).data('param');
    	console.log('update ID Activo:', idActivo);	
    	
    	$.ajax({
			url: '/loadActiveToUpdate',
			type: 'POST',
			data: {
				IdActivo: idActivo,
			},
			success: function(ActiveInfo) {

			},
		});
	});
	
	activateButtonToSaveActive.addEventListener('click', function() {
		$('#showButtonToUpdateActive').hide();
		$('#showButtonToAddActive').show();
	});
   	
    function checkInputs() {
      if (
		NewIdActive.value.trim() !== ''
		&& NewActiveName.value.trim() !== '' && /^[a-zA-Z\s]*$/.test(NewActiveName.value)
        && /\d/.test(ActiveValue.value) // check if there is a number in the value
        && purchaseDate.value.trim() !== ''
        && activeLocation.value !== 'Seleccionar'
        && activeStatus.value !== 'Seleccionar'
        && activeChecked.value.trim() !== ''
        && activeDetails.value.trim() !== 'Seleccionar'
        && (activeDescription.value.trim() !== '' || activeDescription.disabled == true)
		) {
         button.disabled = false;
      } else {
         button.disabled = true;
      }
    }
    
    
});

// this script is used to control the form when the activeLocation is 'uso remoto'
// if I have time, make a timer control 
// and if a new owner is added, the form must be completed
// manage also execptions in fields
$(document).ready(function() {
	
	var newIdOwner = document.getElementById('newIdOwner');

	$('#activeLocation').click(function() {
		if ($('#activeLocation').val() == 'uso remoto') {
			
		}
	});
	
	newIdOwner.addEventListener('input',function() {
		
		if (newIdOwner.value <= 0) {
			newIdOwner.value = '';
		}
		
		$.ajax({
			url: '/checkIfNewOwnerExists',
			type: 'POST',
			data: {
				IdPropietario: newIdOwner.value,
			},
			success: function(OwnerExists) {
				if (OwnerExists) {
					console.log('Owner exists');
					$.ajax({
						url: '/loadOwnerInfo',
						type: 'POST',
						data: {
							IdPropietario: newIdOwner.value,
						},
						success: function(OwnerInfo){
							var newNameOwner = document.getElementById('newNameOwner');
							var newStreetOfOwnerLocation = document.getElementById('newStreetOfOwnerLocation');
							var newNumberOfOwnerLocation = document.getElementById('newNumberOfOwnerLocation');
							var newColonyOfOwnerLocation = document.getElementById('newColonyOfOwnerLocation');
							var newCityOfOwnerLocation = document.getElementById('newCityOfOwnerLocation');
							var newStateOfOwnerLocation = document.getElementById('newStateOfOwnerLocation');
							var newCpOfOwnerLocation = document.getElementById('newCpOfOwnerLocation');
						
							newNameOwner.value = OwnerInfo.nombre; newNameOwner.disabled = true;
							newStreetOfOwnerLocation.value = OwnerInfo.calle; newStreetOfOwnerLocation.disabled = true;
							newNumberOfOwnerLocation.value = OwnerInfo.numero; newNumberOfOwnerLocation.disabled = true;
							newColonyOfOwnerLocation.value = OwnerInfo.colonia; newColonyOfOwnerLocation.disabled = true;
							newCityOfOwnerLocation.value = OwnerInfo.municipio; newCityOfOwnerLocation.disabled = true;
							newStateOfOwnerLocation.value = OwnerInfo.estado; newStateOfOwnerLocation.disabled = true;
							newCpOfOwnerLocation.value = OwnerInfo.cp	; newCpOfOwnerLocation.disabled = true;
						}
					});
				}else{
					newNameOwner.value = ""; newNameOwner.disabled = false;
					newStreetOfOwnerLocation.value = ""; newStreetOfOwnerLocation.disabled = false;
					newNumberOfOwnerLocation.value = ""; newNumberOfOwnerLocation.disabled = false;
					newColonyOfOwnerLocation.value = ""; newColonyOfOwnerLocation.disabled = false;
					newCityOfOwnerLocation.value = ""; newCityOfOwnerLocation.disabled = false;
					newStateOfOwnerLocation.value = ""; newStateOfOwnerLocation.disabled = false;
					newCpOfOwnerLocation.value = ""; newCpOfOwnerLocation.disabled = false;
				}
			},
		});
	});
});

// this script is used to send the data to the server and create the active
$(document).ready(function() {
	$('#buttonToSaveActive').click(function() {
		
	console.log('Button clicked');

		if($('#newIdOwner').val() >= 1){
			var newIdOwner = document.getElementById('newIdOwner');
			
			console.log('Adding a new owner to the active');
			$.ajax({
				url: '/checkIfNewOwnerExists',
				type: 'POST',
				data: {
					IdPropietario: $('#newIdOwner').val(),
			    },
				success: function(ownerExists) {
					if (!ownerExists) {
							// add active and owner
							var newNameOwner = document.getElementById('newNameOwner');
							var newStreetOfOwnerLocation = document.getElementById('newStreetOfOwnerLocation');
							var newNumberOfOwnerLocation = document.getElementById('newNumberOfOwnerLocation');
							var newColonyOfOwnerLocation = document.getElementById('newColonyOfOwnerLocation');
							var newCityOfOwnerLocation = document.getElementById('newCityOfOwnerLocation');
							var newStateOfOwnerLocation = document.getElementById('newStateOfOwnerLocation');
							var newCpOfOwnerLocation = document.getElementById('newCpOfOwnerLocation');
							
						$.ajax({
							url: '/addAnOwnerAndAnActive',
							type: 'POST',
							data: {
								// data from the owner
								IdPropertier: newIdOwner.value,
								nombre: newNameOwner.value,
								calle: newStreetOfOwnerLocation.value,
								numero: newNumberOfOwnerLocation.value,
								colonia: newColonyOfOwnerLocation.value,
								municipio: newCityOfOwnerLocation.value,
								estado: newStateOfOwnerLocation.value,
								cp: newCpOfOwnerLocation.value,
								
								// data from the active
								NewIdActive: $('#NewIdActive').val(),
								NewActiveName: $('#NewActiveName').val(),
								ActiveValue: $('#ActiveValue').val().replace('$', ''),
								purchaseDate: $('#purchaseDate').val(),
								IvaValue: $('#IvaValue').val().replace('$', ''),
								valueDepreciated: $('#valueDepreciated').val().replace('$', ''),
								activeStatus: $('#activeStatus').val(),
								activeChecked: $('#activeChecked').val(),
								activeDetails: $('#activeDetails').val(),
								activeDescription: $('#activeDescription').val(),
							},
						});
					}else{
						console.log('existe el propietario y esta en remoto');

						if($('#activeLocation').val() == "uso remoto"){
							$.ajax({
								url: '/addAnActiveWithAnExistingOwner',
								type: 'POST',
								data: {
								NewIdActive: $('#NewIdActive').val(),
								NewActiveName: $('#NewActiveName').val(),
								ActiveValue: $('#ActiveValue').val().replace('$', ''),
								purchaseDate: $('#purchaseDate').val(),
								IvaValue: $('#IvaValue').val().replace('$', ''),
								valueDepreciated: $('#valueDepreciated').val().replace('$', ''),
								newIdOwner: $('#newIdOwner').val(),
								activeStatus: $('#activeStatus').val(),
								activeChecked: $('#activeChecked').val(),
								activeDetails: $('#activeDetails').val(),
								activeDescription: $('#activeDescription').val()								},
							})
						}else{
							
							// add only the active
							console.log('existe el propietario y no esta en remoto')
							$.ajax({
								url: '/addAnActive',
								type: 'POST',
								data: {
								NewIdActive: $('#NewIdActive').val(),
								NewActiveName: $('#NewActiveName').val(),
								ActiveValue: $('#ActiveValue').val().replace('$', ''),
								purchaseDate: $('#purchaseDate').val(),
								IvaValue: $('#IvaValue').val().replace('$', ''),
								valueDepreciated: $('#valueDepreciated').val().replace('$', ''),
								activeLocation: $('#activeLocation').val(),
								activeStatus: $('#activeStatus').val(),
								activeChecked: $('#activeChecked').val(),
								activeDetails: $('#activeDetails').val(),
								activeDescription: $('#activeDescription').val(),
							},
						});
						}
					}
				},
			});
		}
	});
});