-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Lun 15 Janvier 2018 à 19:10
-- Version du serveur :  5.7.20-0ubuntu0.16.04.1
-- Version de PHP :  7.0.22-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `Projet_S5`
--

-- --------------------------------------------------------

--
-- Structure de la table `APPARTIENT`
--

CREATE TABLE `APPARTIENT` (
  `identifiant` varchar(50) NOT NULL,
  `nom_groupe` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `APPARTIENT`
--

INSERT INTO `APPARTIENT` (`identifiant`, `nom_groupe`) VALUES
('lipton.icetea@univ-tlse3.fr', 'groupe_administration'),
('captain.morgan@univ-tlse3.fr', 'groupe_enseignant'),
('jack.daniel@univ-tlse3.fr', 'groupe_etudiant'),
('bobby.bob@univ-tlse3.fr', 'groupe_informaticien'),
('red.bull@univ-tlse3.fr', 'groupe_informaticien'),
('coca.cola@univ-tlse3.fr', 'groupe_plombier');

-- --------------------------------------------------------

--
-- Structure de la table `DISCUSSION`
--

CREATE TABLE `DISCUSSION` (
  `numero_discussion` int(5) NOT NULL,
  `titre` varchar(80) NOT NULL,
  `contenu` text NOT NULL,
  `auteur_discussion` varchar(50) NOT NULL,
  `nom_groupe` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `DISCUSSION`
--

INSERT INTO `DISCUSSION` (`numero_discussion`, `titre`, `contenu`, `auteur_discussion`, `nom_groupe`) VALUES
(6, 'test                                                                           ', 'Test des discussions', 'captain.morgan@univ-tlse3.fr', 'groupe_etudiant              '),
(7, 'test de disc                                                                   ', 'ceci est un test', 'jack.daniel@univ-tlse3.fr', 'groupe_etudiant              '),
(8, 'disc 3                                                                         ', 'Une autre discussion', 'jack.daniel@univ-tlse3.fr', 'groupe_etudiant              '),
(9, 'Disc pour enseignant                                                           ', 'Contenu de la disc', 'bobby.bob@univ-tlse3.fr', 'groupe_enseignant            '),
(10, 'Pour enseignant                                                                ', 'une discussion', 'bobby.bob@univ-tlse3.fr', 'groupe_enseignant            ');

-- --------------------------------------------------------

--
-- Structure de la table `GROUPE`
--

CREATE TABLE `GROUPE` (
  `nom_groupe` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `GROUPE`
--

INSERT INTO `GROUPE` (`nom_groupe`) VALUES
('groupe_administration'),
('groupe_enseignant'),
('groupe_etudiant'),
('groupe_informaticien'),
('groupe_plombier');

-- --------------------------------------------------------

--
-- Structure de la table `MESSAGE`
--

CREATE TABLE `MESSAGE` (
  `numero_message` int(5) NOT NULL,
  `reponse` text,
  `numero_discussion` int(5) DEFAULT NULL,
  `auteur_message` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `MESSAGE`
--

INSERT INTO `MESSAGE` (`numero_message`, `reponse`, `numero_discussion`, `auteur_message`) VALUES
(10, 'plop', 8, 'jack.daniel@univ-tlse3.fr'),
(11, 'plop bis', 8, 'jack.daniel@univ-tlse3.fr');

-- --------------------------------------------------------

--
-- Structure de la table `UTILISATEUR`
--

CREATE TABLE `UTILISATEUR` (
  `identifiant` varchar(50) NOT NULL,
  `nom_utilisateur` varchar(30) DEFAULT NULL,
  `prenom_utilisateur` varchar(30) DEFAULT NULL,
  `mot_de_passe` varchar(30) DEFAULT NULL,
  `type_utilisateur` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `UTILISATEUR`
--

INSERT INTO `UTILISATEUR` (`identifiant`, `nom_utilisateur`, `prenom_utilisateur`, `mot_de_passe`, `type_utilisateur`) VALUES
('bobby.bob@univ-tlse3.fr', 'Bobby', 'Bob', 'plop', 'INFORMATICIEN'),
('captain.morgan@univ-tlse3.fr', 'Morgan', 'Captain', 'rhum', 'ENSEIGNANT'),
('coca.cola@univ-tlse3.fr', 'Cola', 'Coca', 'soda', 'PLOMBIER'),
('jack.daniel@univ-tlse3.fr', 'Daniel', 'Jack', 'whisky', 'ETUDIANT'),
('lipton.icetea@univ-tlse3.fr', 'Icetea', 'Lipton', 'eau', 'ADMINISTRATION'),
('red.bull@univ-tlse3.fr', 'Bull', 'Red', 'energisant', 'INFORMATICIEN'),
('user1_mail', 'user1_name', 'user1_fname', 'plop', 'ENSEIGNANT');

--
-- Index pour les tables exportées
--

--
-- Index pour la table `APPARTIENT`
--
ALTER TABLE `APPARTIENT`
  ADD PRIMARY KEY (`identifiant`,`nom_groupe`),
  ADD KEY `fk_groupe_APPARTIENT` (`nom_groupe`);

--
-- Index pour la table `DISCUSSION`
--
ALTER TABLE `DISCUSSION`
  ADD PRIMARY KEY (`numero_discussion`),
  ADD KEY `fk_groupe_DISCUSSION` (`nom_groupe`),
  ADD KEY `fk_utilisateur_DISCUSSION` (`auteur_discussion`) USING BTREE;

--
-- Index pour la table `GROUPE`
--
ALTER TABLE `GROUPE`
  ADD PRIMARY KEY (`nom_groupe`);

--
-- Index pour la table `MESSAGE`
--
ALTER TABLE `MESSAGE`
  ADD PRIMARY KEY (`numero_message`),
  ADD KEY `fk_utilisateur_MESSAGE` (`auteur_message`),
  ADD KEY `fk_discussion_MESSAGE` (`numero_discussion`);

--
-- Index pour la table `UTILISATEUR`
--
ALTER TABLE `UTILISATEUR`
  ADD PRIMARY KEY (`identifiant`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `DISCUSSION`
--
ALTER TABLE `DISCUSSION`
  MODIFY `numero_discussion` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT pour la table `MESSAGE`
--
ALTER TABLE `MESSAGE`
  MODIFY `numero_message` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `APPARTIENT`
--
ALTER TABLE `APPARTIENT`
  ADD CONSTRAINT `fk_groupe_APPARTIENT` FOREIGN KEY (`nom_groupe`) REFERENCES `GROUPE` (`nom_groupe`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_utilisateur_APPARTIENT` FOREIGN KEY (`identifiant`) REFERENCES `UTILISATEUR` (`identifiant`) ON DELETE CASCADE;

--
-- Contraintes pour la table `DISCUSSION`
--
ALTER TABLE `DISCUSSION`
  ADD CONSTRAINT `DISCUSSION_ibfk_1` FOREIGN KEY (`nom_groupe`) REFERENCES `GROUPE` (`nom_groupe`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_utilisateur_DISCUSSION` FOREIGN KEY (`auteur_discussion`) REFERENCES `UTILISATEUR` (`identifiant`) ON DELETE CASCADE;

--
-- Contraintes pour la table `MESSAGE`
--
ALTER TABLE `MESSAGE`
  ADD CONSTRAINT `fk_discussion_MESSAGE` FOREIGN KEY (`numero_discussion`) REFERENCES `DISCUSSION` (`numero_discussion`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_utilisateur_MESSAGE` FOREIGN KEY (`auteur_message`) REFERENCES `UTILISATEUR` (`identifiant`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
