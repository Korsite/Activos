$(document).ready(function() {
    $('.OwnerSearchButton').click(function(e) {
		$('#InfoFound').hide(); 
        $('#InfoNotFound').hide();
        e.preventDefault();
                
        var IdActivo = $(this).data('parametro') || 0;

        console.log('IdActivo:', IdActivo); // Verify value of IdActivo in console

        $.ajax({
            url: '/loadData',
            type: 'POST',	
            data: { IdActivo: IdActivo },
            success: function(response) {

                console.log(response.IdPropertier);
				console.log(response.nombre);
				console.log(response.ubicacion);
	
                if (response.IdPropertier !== null) {
                    $('#IdPropertier').text(response.IdPropertier);
                    $('#nombre').text(response.nombre);
                    $('#ubicacion').text(response.ubicacion);
                    $('#InfoFound').show(); // Muestra el contenedor si IdPropertier no es nulo
                } else {
                    $('#InfoNotFound').show(); // Muestra el contenedor de error
                    $('#IdActivo').text(IdActivo);
                }
            },
            error: function(xhr, status, error) {
                // Maneja errores de la solicitud AJAX aquí
                alert('Error al cargar los datos: ' + error);
            }
        });
    });
});

