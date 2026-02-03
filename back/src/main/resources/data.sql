-- Spécialités
MERGE INTO specialite (id, nom) KEY(id) VALUES (1, '🌊 Élémentaliste des eaux');
MERGE INTO specialite (id, nom) KEY(id) VALUES (2, '🪃 Frondée');
MERGE INTO specialite (id, nom) KEY(id) VALUES (3, '🍃 Druide');
MERGE INTO specialite (id, nom) KEY(id) VALUES (4, '🔮 Reflet élémentaire');
MERGE INTO specialite (id, nom) KEY(id) VALUES (5, '🏹 Chasseur');
MERGE INTO specialite (id, nom) KEY(id) VALUES (6, '🌙 Sorcier');
MERGE INTO specialite (id, nom) KEY(id) VALUES (7, '🗡️ Assassin');
MERGE INTO specialite (id, nom) KEY(id) VALUES (8, '⚔️ Guerrier');
MERGE INTO specialite (id, nom) KEY(id) VALUES (9, '👻 Invocateur');
MERGE INTO specialite (id, nom) KEY(id) VALUES (10, '✨ Prêtre guérisseur');
MERGE INTO specialite (id, nom) KEY(id) VALUES (11, '🛡️ Maître d''armes');
MERGE INTO specialite (id, nom) KEY(id) VALUES (12, '⚜️ Paladin');
MERGE INTO specialite (id, nom) KEY(id) VALUES (13, '🔥 Élémentaliste');
MERGE INTO specialite (id, nom) KEY(id) VALUES (14, '🧘 Moine spirituel');
MERGE INTO specialite (id, nom) KEY(id) VALUES (15, '🎵 Barde');
MERGE INTO specialite (id, nom) KEY(id) VALUES (16, '🌲 Rôdeur');
MERGE INTO specialite (id, nom) KEY(id) VALUES (17, '🗺️ Aventurier');
MERGE INTO specialite (id, nom) KEY(id) VALUES (18, '🐴 Chevalier');
MERGE INTO specialite (id, nom) KEY(id) VALUES (19, '🎯 Archer');
MERGE INTO specialite (id, nom) KEY(id) VALUES (20, '⚡ Rapide');
MERGE INTO specialite (id, nom) KEY(id) VALUES (21, '🌀 Ensorceleur');
MERGE INTO specialite (id, nom) KEY(id) VALUES (22, '⏳ Moine Tempus spirituel');
MERGE INTO specialite (id, nom) KEY(id) VALUES (23, '⚔️ Spadassin');
MERGE INTO specialite (id, nom) KEY(id) VALUES (24, '💫 Enchanteur');

-- Aventuriers
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (1, 'Aria Merveilleuse', 2500, 1, 250, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (2, 'Thoran Forgehammer', 6800, 8, 350, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (3, 'Luna Shadowstep', 4200, 7, 300, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (4, 'Eldrin Stormcaller', 9500, 6, 450, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (5, 'Sylvana Leafwhisper', 1800, 3, 200, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (6, 'Marcus Swiftbow', 5400, 5, 320, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (7, 'Celestia Lightbringer', 8200, 10, 400, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (8, 'Ragnar Ironshield', 4600, 11, 280, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (9, 'Zephyr Windwalker', 3100, 20, 260, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (10, 'Morgana Spiritcaller', 7300, 9, 370, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (11, 'Kael Fireheart', 5900, 13, 340, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (12, 'Seraphine Songweaver', 2200, 15, 220, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (13, 'Drake Valorheart', 9800, 12, 480, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (14, 'Nyx Voidwalker', 8700, 21, 420, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (15, 'Thalion Earthshaper', 4100, 3, 290, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (16, 'Isolde Frostweaver', 6500, 4, 360, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (17, 'Viktor Bladedancer', 5600, 23, 330, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (18, 'Lyra Mooncharm', 3400, 24, 270, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (19, 'Gareth Stonefist', 8100, 18, 410, 'DISPONIBLE', CURRENT_DATE);
MERGE INTO aventurier (id, nom, niveau_experience, specialite_id, taux_journalier_base, disponibilite, date_disponibilite) KEY(id) VALUES (20, 'Astrid Stargazer', 4800, 14, 310, 'DISPONIBLE', CURRENT_DATE);

-- Users
MERGE INTO users (id, user_name, password, roles) KEY(id) VALUES (1, 'commanditaire1', '$2a$10$AafbEtxpXCEuaxPpjEnPRuRG97l2aLfvsNJay5yato3YgXfTd9SdC', 'COMMANDITAIRE');
MERGE INTO users (id, user_name, password, roles) KEY(id) VALUES (3, 'commanditaire2', '$2a$10$AafbEtxpXCEuaxPpjEnPRuRG97l2aLfvsNJay5yato3YgXfTd9SdC', 'COMMANDITAIRE');
MERGE INTO users (id, user_name, password, roles) KEY(id) VALUES (4, 'commanditaire3', '$2a$10$AafbEtxpXCEuaxPpjEnPRuRG97l2aLfvsNJay5yato3YgXfTd9SdC', 'COMMANDITAIRE');
MERGE INTO users (id, user_name, password, roles) KEY(id) VALUES (5, 'commanditaire4', '$2a$10$AafbEtxpXCEuaxPpjEnPRuRG97l2aLfvsNJay5yato3YgXfTd9SdC', 'COMMANDITAIRE');
MERGE INTO users (id, user_name, password, roles) KEY(id) VALUES (6, 'commanditaire5', '$2a$10$AafbEtxpXCEuaxPpjEnPRuRG97l2aLfvsNJay5yato3YgXfTd9SdC', 'COMMANDITAIRE');
MERGE INTO users (id, user_name, password, roles) KEY(id) VALUES (2, 'assistant1', '$2a$10$AafbEtxpXCEuaxPpjEnPRuRG97l2aLfvsNJay5yato3YgXfTd9SdC', 'ASSISTANT');

-- Quetes
MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (1, 1, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Défendre le village contre les raids de gobelins', 5, 'Protection du village de Lumebois', 2500, 'NOUVELLE');

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (2, 1, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Retrouver l''amulette volée dans les cryptes anciennes', 8, 'Récupération de l''artefact sacré', 4500, 'NOUVELLE');

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (3, 3, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Protéger la caravane marchande jusqu''à la capitale', 3, 'Escorte de la caravane royale', 1800, 'NOUVELLE');

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (4, 3, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Éliminer le dragon terrorisant la région', 12, 'Extermination du dragon des marais', 8000, 'NOUVELLE');

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (5, 4, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Cartographier les anciennes ruines et rapporter des artéfacts', 6, 'Exploration des ruines elfiques', 3000, 'NOUVELLE');

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (6, 4, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Libérer le prince retenu par des bandits', 7, 'Sauvetage du prince kidnappé', 5500, 'NOUVELLE');

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (7, 5, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Chasser les esprits corrompus du temple', 4, 'Purification du temple maudit', 2800, 'NOUVELLE');

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (8, 5, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Capturer ou éliminer le basilic des montagnes noires', 10, 'Chasse au basilic légendaire', 7200, 'NOUVELLE');

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (9, 6, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Infiltrer et démanteler la secte des ombres', 9, 'Investigation sur la secte obscure', 4800, 'NOUVELLE');

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut) KEY(id)
    VALUES (10, 6, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Acheminer des potions de guérison vers les villages isolés', 2, 'Livraison de remèdes urgents', 1500, 'NOUVELLE');

-- Équipes
MERGE INTO equipes (id, nom, date_depart, date_retour_prevue, cout_total, ratio_rentabilite) KEY(id)
    VALUES (1, 'Les Griffes d''Acier', DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -13, CURRENT_DATE), 5250, 1.4);

MERGE INTO equipes (id, nom, date_depart, date_retour_prevue, cout_total, ratio_rentabilite) KEY(id)
    VALUES (2, 'Les Ombres Silencieuses', DATEADD('DAY', -15, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), 3500, 1.6);

MERGE INTO equipes (id, nom, date_depart, date_retour_prevue, cout_total, ratio_rentabilite) KEY(id)
    VALUES (3, 'Les Gardiens de la Lumière', DATEADD('DAY', -25, CURRENT_DATE), DATEADD('DAY', -19, CURRENT_DATE), 4100, 1.3);

-- Participations équipe
MERGE INTO participation_equipe (id, equipe_id, aventurier_id, date_affectation, date_retour, etat, gain_experience) KEY(id)
    VALUES (1, 1, 2, DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -13, CURRENT_DATE), 'TERMINE', 500);

MERGE INTO participation_equipe (id, equipe_id, aventurier_id, date_affectation, date_retour, etat, gain_experience) KEY(id)
    VALUES (2, 1, 8, DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -13, CURRENT_DATE), 'TERMINE', 500);

MERGE INTO participation_equipe (id, equipe_id, aventurier_id, date_affectation, date_retour, etat, gain_experience) KEY(id)
    VALUES (3, 2, 3, DATEADD('DAY', -15, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), 'TERMINE', 400);

MERGE INTO participation_equipe (id, equipe_id, aventurier_id, date_affectation, date_retour, etat, gain_experience) KEY(id)
    VALUES (4, 3, 7, DATEADD('DAY', -25, CURRENT_DATE), DATEADD('DAY', -19, CURRENT_DATE), 'TERMINE', 450);

-- Quêtes terminées pour commanditaire 1
MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut, equipe_id, experience_gagnee) KEY(id)
    VALUES (11, 1, DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', 10, CURRENT_DATE), 'Élimination des bandits sur la route commerciale', 7, 'Sécurisation de la route', 3000, 'TERMINEE', 1, 500);

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut, equipe_id, experience_gagnee) KEY(id)
    VALUES (12, 1, DATEADD('DAY', -15, CURRENT_DATE), DATEADD('DAY', 15, CURRENT_DATE), 'Recherche d''artefacts magiques dans les cavernes', 5, 'Exploration des cavernes', 2500, 'TERMINEE', 2, 400);

-- Quêtes terminées pour commanditaire 2
MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut, equipe_id, experience_gagnee) KEY(id)
    VALUES (13, 3, DATEADD('DAY', -25, CURRENT_DATE), DATEADD('DAY', 5, CURRENT_DATE), 'Défense du convoi marchand contre les pillards', 6, 'Protection du convoi', 3200, 'TERMINEE', 1, 450);

MERGE INTO quetes (id, commanditaire_id, date_creation, date_peremption, description, duree_estimee, nom, prime, statut, equipe_id, experience_gagnee) KEY(id)
    VALUES (14, 3, DATEADD('DAY', -18, CURRENT_DATE), DATEADD('DAY', 12, CURRENT_DATE), 'Récupération de documents volés', 4, 'Vol de documents', 1800, 'TERMINEE', 3, 300);

ALTER TABLE quetes ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM quetes);
ALTER TABLE specialite ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM specialite);
ALTER TABLE aventurier ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM aventurier);
ALTER TABLE users ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM users);
ALTER TABLE quetes ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM quetes);
ALTER TABLE equipes ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM equipes);
ALTER TABLE participation_equipe ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM participation_equipe);