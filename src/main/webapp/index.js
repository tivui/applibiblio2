
//****************************************TESTS*********************************************

//TEST fonction Jquery qui se lance à la modification de l'input Livres
$(function () {
    $("#titre").on('input', function () {
        //Affichage du titre rentré 
        let titre = $("#titre").val();
        $("#message12").html("Titre tapé: " + titre);
    });
});

//TEST fonction Jquery qui se lance à la modification de l'input Livres
$(function () {
    $("#nomAuteur").on('input', function () {
        //Affichage du nom de l'Auteur rentré 
        let nomAuteur = $("#nomAuteur").val();
        $("#message13").html("Nom de l'auteur tapé: " + nomAuteur);
    });
});

//TEST fonction Jquery qui se lance à la modification de l'input Livres
$(function () {
    $("#prenomAuteur").on('input', function () {
        //Affichage du prenom de l'Auteur rentré 
        let prenomAuteur = $("#prenomAuteur").val();
        $("#message14").html("Prenom de l'auteur tapé: " + prenomAuteur);
    });
});

//TEST fonction Jquery qui se lance à la modification de l'input Livres
$(function () {
    $("#annee").on('change', function () {
        //Affichage de l'annee sélectionnée
        let annee = $("#annee").val();
        $("#message15").html("annee rentrée: " + annee);
    });
});

//TEST fonction Jquery qui se lance à la modification de l'input Livres
$(function () {
    $("#editeur").on('change', function () {
        //Affichage de l'editeur sélectionnée
        let editeur = $("#editeur").val();
        $("#message16").html("editeur selectionné: " + editeur);
    });
});

//fonction Jquery qui se lance au clic du bouton envoyer
$(function () {
    $("#envoyer").on('click', function () {
        //récupération des données du livre sélectionné
        
        let titre = $("#titre").val();
        let nomAuteur = $("#nomAuteur").val();
        let prenomAuteur = $("#prenomAuteur").val();
        let annee = $("#annee").val();
        let editeur = $("#editeur").val();
        //envoi de l'id pour la création d'un livre
        $.ajax({
            url: "/creation/livre/" + titre + "/" + nomAuteur + "/" + prenomAuteur + "/" + annee + "/" + editeur,
            type: "GET",
            dataType: "json"
        }).done(function () { // 200
            $("#message10").html("Opération de création effectuée!");
        }).fail(function () { // 400, 501..
            $("#message11").html("Echec !");
        });
    });
});