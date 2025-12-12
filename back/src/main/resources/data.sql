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

-- Reset sequences to start after the pre-loaded data (dynamically based on current max ID)
ALTER TABLE specialite ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM specialite);
ALTER TABLE aventurier ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM aventurier);