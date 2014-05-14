MyProjectGitHub
===============
Projet PKI
 Objectif : créer un ensemble d’applications client-serveur implémentant une PKI.
 Protocôle d’enregistrement d’un client auprès de sa RA.
– Protocôle hors-ligne – sinon pbs. d’attaque Man-In-The-Middle, ou nécessité d’utiliser
une autre infrastructure de connexion sécurisée.
 Protocôle de demande et d’obtention d’un certificat signé par sa CA.
– Après la génération d’une nouvelle clé publique client.
 Protocôle de communication entre clients.
– Peut engendrer une échange de certificats entre interlocuteurs, au démarrage.
– Ou peut se baser sur un autre protocôle de récupération du certificat de l’interlocuteur,
auprès d’un répertoire des certificats.
– Exemple : utiliser Needham-Schroeder pour la création d’une clé de session.
 Politique de gestion de la CRL :
– Implémentation des deux méthodes : demande périodique de CRL et vérification
d’appartenance dans la CRL et vérification par OCSP lors de chaque nouvelle
connexion.
– Protocole à définir pour demander le rajout d’un certificat dans une CRL.
 Implémentation du protocole Needham-Schroder pour une application de type chat
sécurisé, avec une gestion de type PKI des clés et génération de clé de session à partir de
la nonce NB.

1) il faut inclure les 3 librairies  qui sont dans le dossier "Jar a inclure"
2) créer une base de données MySQL avec le fichier "dataBase_PKI2014.sql"
3) une petite démonstration en vidéo sur l'utilisation de l'application: http://www.youtube.com/watch?v=38HRivBa1GI 

MyProjectGitHub
