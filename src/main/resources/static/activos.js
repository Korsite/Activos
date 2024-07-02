$(document).ready(function() {
	$("#inputGroupSelect02").click(function() {
		
		var location = document.getElementById("inputGroupSelect01");
		var activeStatus = document.getElementById("inputGroupSelect02").value;

		if(activeStatus == 4){
			console.log("No encontrado");
			location.value = 0;
			location.disabled = true;
		}else{
			console.log("Disable");
			location.disabled = false;
		}

	});
});