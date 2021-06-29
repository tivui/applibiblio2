// document ready
jQuery(document).ready(function ($) {
    alert('Message lancé grâce à la méthode .ready de Jquery');
});


$(function () {
    $("#envoyer").on('click', function () {
        alert("Tout se passe bien!");
    });
});



