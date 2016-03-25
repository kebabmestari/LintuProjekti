/**
* Rekisteröi uusi tunnus
*/

var nimiMinLength = 5;

function register(){
	var newName  = document.querySelector('#reknimi').value;
	var newPasw  = document.querySelector('#reksalasana').value;
	var newPasw2 = document.querySelector('#reksalasana2').value;

	var failp = document.querySelector('#failmessage');
	var failure = false;

	if(newName.length < 5){
		createMsg('Name is too short. Minimum of 5 characters.', failp);
		failure = true;
	}
	if(newPasw != newPasw2){
		createMsg("Passwords don't match", failp);
		failure = true;
	}
	if(failure){
		$('#failmessage').show();
		return;
	}

	$.ajax({
		url: 'register.php',
		method: 'GET',
		success: function(data){
			var rivit = data.split('\n');
			if(rivit[0].toLowerCase().indexOf('success') > -1){
				$('#rekisterointi').modal({show: false});
				$('#rekisterointisuccess').modal({show: true});
			}
		}
	});
}

function createMsg(msg, parent){
	var newp = document.createElement('p');
	var newmsg = document.createTextNode(msg);
	newp.appendChild(newmsg);
	parent.appendChild(newp);
}

$(document).ready(function(){
	$('#rekbutton').click(function(){
		register();
	});

});