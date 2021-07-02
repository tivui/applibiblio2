// fonction Jquery au lancement de la page
jQuery(document).ready(function ($) {
    $.ajax({
        //récupération de la liste de livres existante dans la bdd triée par titre
        url: "/bdd/livrespartitre",
        type: "GET",
        dataType: "json"
    }).done(function (livresParTitre) { // 200
        //boucle pour afficher les livres dans l'input select du formulaire
        //la valeur de chaque option est l'id du livre
        if (livresParTitre.length == 0) {
            $('#titreListeLivres').html("Pas de livre disponible")
        }
        for (const livre of livresParTitre) {
            $('#listeLivres').append('<option value="' + livre.idLivre + '">' + livre.titre + ' (' + livre.prenomAuteur + ' ' + livre.nomAuteur + ')</option>');
        }
    }).fail(function () { // 400, 501..
        $("#message").html("Echec !");
    });


//**************Attribut date max à la date du jour**************************************************
    jQuery(document).ready(function ($) {
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //Janvier est normalisé à 0
        var yyyy = today.getFullYear();
        if (dd < 10) {
            dd = '0' + dd
        }
        if (mm < 10) {
            mm = '0' + mm
        }
        today = yyyy + '-' + mm + '-' + dd;
        document.getElementById("date").setAttribute("max", today);
    });


    //-------------------Debut de la fonction qui a été rajouté------------------------------------------------------------------------
    // fonction qui affiche la liste des emprunts dès qu'on  affiche la page Emprunt
    $(function () {
        $.ajax({
            url: "bdd/listEmprunts",
            type: "GET",
            dataType: "json"
        }).done(function (retour) { // 200
            if (retour.length === 0) {
                $("#message").html("Pas d'emprunts en cours pour le moment...");
            } else {
                $("#message").html("Nombre d'emprunts en cours :" + retour.length);
            }
            let lignes = "";
            for (const ligne of retour) {
                lignes += "<tr>" +
                        "<td>" + ligne.idEmprunt + "</td>" +
                        "<td>" + ligne.livre.titre + "</td>" +
                        "<td>" + ligne.livre.nomAuteur + "</td>" +
                        "<td>" + ligne.nomEmprunteur + "</td>" +
                        "<td>" + ligne.date + "</td>" +
                        "<td><button id='" + ligne.idEmprunt + "' onclick='EmpruntFini(this.id)'>action</button></td></tr>";
            }
            $("#listemprunts tbody").html(lignes);
        });
    });
    //--------------------fin de la fonction qui a été rajoué---------------------------------
});



//********************A LA VALIDATION DU FORMULAIRE******************************************************************************************************************
$(document).ready(function () {
    $("#formulaire").submit(function (e) {
        //récupération de l'id du livre sélectionné
        let id = $("#listeLivres option:selected").val();
        let nomEmprunteur = $("#nomEmprunteur").val();
        let date = $("#date").val();
        $.ajax({
            url: "/creation/emprunt/" + id + "/" + nomEmprunteur + "/" + date,
            type: "GET",
            dataType: "json"
        }).done(function (id) { // 200
            $("#message").html("Opération de création effectuée!");
        }).fail(function () { // 400, 501..
            $("#message2").html("Echec !");
        });
        //si les champs du formulaire sont bien remplis
        e.preventDefault();
        alert("Emprunt créé avec succès");
        //envoi de l'id pour la création d'un emprunt
        $.ajax({
            url: "bdd/listEmprunts",
            type: "GET",
            dataType: "json"
        }).done(function (retour) { // 200
            $("#message").html("Nombre d'emprunts dans la base :" + retour.length);
            let lignes = "";
            for (const ligne of retour) {
                lignes += "<tr>" +
                        "<td>" + ligne.idEmprunt + "</td>" +
                        "<td>" + ligne.livre.titre + "</td>" +
                        "<td>" + ligne.livre.nomAuteur + "</td>" +
                        "<td>" + ligne.nomEmprunteur + "</td>" +
                        "<td>" + ligne.date + "</td>" +
                        "<td><button id='" + ligne.idEmprunt + "' onclick='EmpruntFini(this.id)'>effectuer un retour</button></td></tr>";
            }
            $("#listemprunts tbody").html(lignes);
        }).then(function () {
            $.ajax({
                //récupération de la liste de livres existante dans la bdd triée par titre
                url: "/bdd/livrespartitre",
                type: "GET",
                dataType: "json"
            }).done(function (livresParTitre) { // 200
                //vide la liste  en rajoutant seulement la première option
                $('#listeLivres').empty();
                $('#listeLivres').append('<option id="titreListeLivres" value="">--Veuillez sélectionner un livre--</option>');
                if (livresParTitre.length === 0) {
                    $('#titreListeLivres').html("Pas de livre disponible");
                }
                for (const livre of livresParTitre) {
                    $('#listeLivres').append('<option value="' + livre.idLivre + '">' + livre.titre + ' (' + livre.prenomAuteur + ' ' + livre.nomAuteur + ')</option>');
                }
            }).fail(function () { // 400, 501..
                $("#message").html("Echec !");
            });
            //rechargemement de la page pour éviter que la fenêtre popup s'affiche plusieurs 
            //location.reload();
        });
    });
});



//********************************************fin CLIC bouton ENVOYER*******************************************************************






//fonction au clic d'un bouton action
function EmpruntFini(id) {
    $.ajax({
        url: "/miseajour/emprunt/" + id,
        type: "PUT",
        dataType: "json"
    });
    $(function (e) {
        //ouverture d'une boite de dialogue avec confirmation
        if (confirm("Confirmez vous la fin de l'emprunt à la date du jour?")) {
            $.ajax({
                url: "bdd/listEmprunts",
                type: "GET",
                dataType: "json"
            }).done(function (retour) { // 200
                $("#message").html("Nombre d'emprunts dans la base :" + retour.length);
                let lignes = "";
                for (const ligne of retour) {
                    lignes += "<tr>" +
                            "<td>" + ligne.idEmprunt + "</td>" +
                            "<td>" + ligne.livre.titre + "</td>" +
                            "<td>" + ligne.livre.nomAuteur + "</td>" +
                            "<td>" + ligne.nomEmprunteur + "</td>" +
                            "<td>" + ligne.date + "</td>" +
                            "<td><button id='" + ligne.idEmprunt + "' onclick='EmpruntFini(this.id)'>effectuer le retour</button></td></tr>";
                }
                $("#listemprunts tbody").html(lignes);
            }).then(function () {
                $.ajax({
                    //récupération de la liste de livres existante dans la bdd triée par titre
                    url: "/bdd/livrespartitre",
                    type: "GET",
                    dataType: "json"
                }).done(function (livresParTitre) { // 200
                    //vide la liste  en rajoutant seulement la première option
                    $('#listeLivres').empty();
                    $('#listeLivres').append('<option id="titreListeLivres" value="">--Veuillez sélectionner un livre--</option>');
                    if (livresParTitre.length === 0) {
                        $('#titreListeLivres').html("Pas de livre disponible");
                    }
                    for (const livre of livresParTitre) {
                        $('#listeLivres').append('<option value="' + livre.idLivre + '">' + livre.titre + ' (' + livre.prenomAuteur + ' ' + livre.nomAuteur + ')</option>');
                    }
                }).fail(function () { // 400, 501..
                    $("#message").html("Echec !");
                });
            });
        } else {
        }
    });

}





////****************************************TESTS*********************************************
////TEST fonction Jquery qui se lance au changement de livre sélectionné
//$(function () {
//    $("#listeLivres").on('change', function () {
//        //Affichage de l'id du livre
//        let id = $("#listeLivres option:selected").val();
//        $("#message2").html("Valeur de l'id sélectionnée : " + id);
//    });
//});
////TEST fonction Jquery qui se lance à la modification de l'input Emprunteur
//$(function () {
//    $("#nomEmprunteur").on('input', function () {
//        //Affichage du nom de l'Emprunteur rentré 
//        let nomEmprunteur = $("#nomEmprunteur").val();
//        $("#message3").html("Nom de l'emprunteur tapé: " + nomEmprunteur);
//    });
//});
////TEST fonction Jquery qui se lance à la modification de la date
//$(function () {
//    $("#date").on('change', function () {
//        //Affichage de la date sélectionnée
//        let date = $("#date").val();
//        $("#message4").html("Date sélectionnée: " + date);
//    });
//});
////******************************************************************************************






