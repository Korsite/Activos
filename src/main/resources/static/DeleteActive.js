$(document).ready(function() {
	$('.btn.btn-danger').click(function() {

		var IdActivo = $(this).data('parametro') || 0;
		console.log(IdActivo, 'IdActivo to delete confirm');
		$('#modal-body').hide();
		
		
		if (IdActivo != 0) {
			$('#showActiveToConfirm').text(IdActivo);
			var showActiveToConfirm = $('#showActiveToConfirm').text();
			$('.DeleteActive').data('parametro', showActiveToConfirm);
			$('#modal-body').show();
		}
	});
});

$(document).ready(function() {
	$('.DeleteActive').click(function() {
		var IdActivo = $(this).data('parametro') || 0;
		console.log(IdActivo, 'Deleting...');
		var activeDeleted = false
		$.ajax({
			url: '/deleteActive',
			type: 'POST',
			data: { IdActivo: IdActivo },
			success: function(response) {	
				activeDeleted = response	
				console.log("active " + IdActivo + "was deleted?", activeDeleted); // check if the active was deleted in console
                if (activeDeleted) {
                    $('#activeDeleted').text("El activo " + IdActivo + " se eliminó correctamente ");
                } else {
                    $('#activeDeleted').text("Error al eliminar el activo " + IdActivo);
                }			
            }
		});
	});
});

$(document).ready(function() {
	$('#UpdateChangeOfDeleting').click(function() { 
		window.location.href = "/activos";
	});
});