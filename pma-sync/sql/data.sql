INSERT INTO ex_convict (id, first_name, last_name, pseudonym, address, date_of_birth, crime , description, gender, photo) 
	VALUES (1, 'Pavle', 'Avakumović', 'Pablo', 'Bulevar patrijarha Pavla 14, 21 000, Novi Sad', '1955-05-05', 'ubistvo sa predumišljajem, pljačka',
			'Visina 185cm, težina 80 kg, boja očiju: plava, plava kosa vezana u rep, tetovaža u obliku zmaja na vratu, slika džokera na levoj potkolenici, ožiljci na licu, potiljku i gornjem delu leve ruke, beleg na stomaku.',
			'Muški', 1);	
INSERT INTO ex_convict (id, first_name, last_name, pseudonym, address, date_of_birth, crime , description, gender, photo) 
	VALUES (2, 'Marko', 'Marković', 'Markić', 'Bulevar patrijarha Pavla 132, 21 000, Novi Sad', '1965-06-06', 'ubistvo sa predumišljajem, pljačka',
			'Visina 202cm, težina 130 kg, boja očiju: plava, plava kosa vezana u rep, tetovaža u obliku zmaja na vratu, slika džokera na levoj potkolenici, ožiljci na licu, potiljku i gornjem delu leve ruke, beleg na stomaku.',
			'Muški', 2);
INSERT INTO ex_convict (id, first_name, last_name, pseudonym, address, date_of_birth, crime , description, gender, photo) 
	VALUES (3, 'Veljko', 'Prelić', 'Veljan', 'Bulevar patrijarha Pavla 14, 21 000, Novi Sad', '1955-05-05', 'ubistvo sa predumišljajem, pljačka',
			'Visina 185cm, težina 80 kg, boja očiju: plava, plava kosa vezana u rep, tetovaža u obliku zmaja na vratu, slika džokera na levoj potkolenici, ožiljci na licu, potiljku i gornjem delu leve ruke, beleg na stomaku.',
			'Muški', 3);	
			
# Lozinka je "lozinka"
INSERT INTO user (id, email, first_name, last_name, password,  is_sync)
	VALUES (100, "borkovac.dragan@gmail.com", "Dragan", "Borkovac", "$2a$10$BddBmEuvDOS7ZSoTyzEtR.oQy8kwy81PZomsfzQVA.4DixWpHe6PS", true);
INSERT INTO user (id, email, first_name, last_name, password,  is_sync)
	VALUES (200, "savic.olga@gmail.com", "Olga", "Savić", "$2a$10$BddBmEuvDOS7ZSoTyzEtR.oQy8kwy81PZomsfzQVA.4DixWpHe6PS", true);
INSERT INTO user (id, email, first_name, last_name, password,  is_sync)
	VALUES (300, "novakovic.jovana@gmail.com", "Jovana", "Novaković", "$2a$10$BddBmEuvDOS7ZSoTyzEtR.oQy8kwy81PZomsfzQVA.4DixWpHe6PS", true);
			
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (1, 45.2359337,19.8277852, '1300 kaplara, Liman, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (2, 45.2451097,19.8095541, 'Bogdana Šuputa, Telep, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (3, 45.2523492,19.7960865, 'Braće Dronjak, Bistrica, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (4, 45.2425503,19.8343992, 'Bulevar cara Lazara, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (5, 45.2488709,19.8225385, 'Cara Dušana, Sajmište, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (6, 45.2477284,19.8343505, 'Danila Kiša, Grbavica, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (7, 45.2517194,19.835018, 'Bulevar Oslobođenja, Novi Sad');



INSERT INTO report (id, city, comment, date, ex_convict_id_id, lang, lat, location, user_id_id, is_sync)
	VALUES (1, "Novi Sad","Komentar za korisnika", "Fri Jul 03 00:06:28 GMT+02:00 2020", 1, 19.8277852,  45.2359337, "1300 kaplara, Liman, Novi Sad", 100, true);
	
INSERT INTO report (id, city, comment, date, ex_convict_id_id, lang, lat, location, user_id_id, is_sync)
	VALUES (2, "Novi Sad","Komentar za korisnika", "Sat Jul 04 00:06:28 GMT+02:00 2020", 1, 19.8095541,  45.2451097, "Bogdana Šuputa, Telep, Novi Sad", 100, true);
	
INSERT INTO report (id, city, comment, date, ex_convict_id_id, lang, lat, location, user_id_id, is_sync)
	VALUES (3, "Novi Sad","Komentar za korisnika", "Fri Jul 03 00:06:28 GMT+02:00 2020", 2, 19.7960865,  45.2523492, "Braće Dronjak, Bistrica, Novi Sad", 200, true);

INSERT INTO report (id, city, comment, date, ex_convict_id_id, lang, lat, location, user_id_id, is_sync)
	VALUES (4, "Novi Sad","Komentar za korisnika", "Sat Jul 04 00:06:28 GMT+02:00 2020", 2, 19.8343992,  45.2425503, "Bulevar cara Lazara, Novi Sad", 200, true);
			

INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (8, 45.236414,19.8082477, 'Feješ Klare, Telep, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (9, 45.2604486,19.8122981, 'Janka Veselinovića, Detelinara, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (10, 45.2523114,19.8533436, 'Kej žrtava racije, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (11, 45.246279,19.8357557, 'Lasla Gala, Grbavica, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (12, 45.2514376,19.785457, 'Mileve Marić, Bistrica, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (13, 45.2628015,19.8470217, 'Pavla Stamatovica, Podbara');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (14, 45.2309476,19.7908401, 'Slavujeva, Adice, Novi Sad');
INSERT INTO `db_pma`.`address` (`id`, `lat`,`lang`, `name`) VALUES (15, 45.2961749,19.8238808, 'Velebitska, Klisa, Novi Sad');

