Projet de ROUMILI Morjana, ZARAGOZA Jérémy, BOURGEOIS Julien

#Sommaire : 

	Partie 1 : Installation de la base de donnée
	Partie 2 : Configuration du serveur
	Partie 3 : Fonctionnement du logiciel

#Partie 1 : Installation de la base de donnée
	
	Pré-requis : _WAMP ( Windows )
		     _LAMP ( Linux )
	Lancez votre logiciel pour la base de donnée ( WAMP/LAMP ) et 
	lancez le serveur localhost ( http://localhost/phpmyadmin/ ).
	Connectez vous avec vos identifiants et cliquez sur "Nouvelle
	base de données"->"Importer".
	Dans la partie fichier a importer selectionnez le fichier "Projet_S5.sql"
	disponible dans le Dossier "\Code", puis cliquez sur "Executer".

#Partie 2 : Configuration du serveur

	Afin que votre Serveur puisse consulter la BDD il va vous falloir le
	configurer. Pour cela rendez vous dans "\Code\Serveur\src\Serveur\server"
	et ouvrez Database.java avec l'éditeur de code de votre choix.
	Modifiez le contenu de la variable login et mot de passe par vos 
	identifiant et mot de passe de votre BDD.
	Si la BDD n'est pas hebergé localement modifiez également l'url.
	Il vous faudra re-compiler votre code afin que celui ci s'adapte 
	a votre BDD.
	/!\
	SINON mettez en login "root" et en mot de passe "jeremy".
	/!\

#Partie 3 : Fonctionnement du logiciel 

	Etape 1 : Lancez votre BDD
	Etape 2 : Lancez votre serveur ( vous pouvez y créer un utilisateur )
	Etape 3 : Lancez le client
	Etape 4 : Connectez vous ( soit avec un utilisateur deja présent dans la BDD,
				   soit avec un utilisateur créer sur le serveur )
	Enjoy !

