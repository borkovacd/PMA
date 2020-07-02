INSERT INTO ex_convict (id, first_name, last_name, pseudonym, address, date_of_birth, crime , description, gender, photo) 
	VALUES (1, 'Pavle', 'Avakumović', 'Pablo', 'Bulevar patrijarha Pavla 14, 21 000, Novi Sad', '12.12.1955', 'ubistvo sa predumišljajem, pljačka',
			'Visina 185cm, težina 80 kg, boja očiju: plava, plava kosa vezana u rep, tetovaža u obliku zmaja na vratu, slika džokera na levoj potkolenici, ožiljci na licu, potiljku i gornjem delu leve ruke, beleg na stomaku.',
			'Muški', 1);	
INSERT INTO ex_convict (id, first_name, last_name, pseudonym, address, date_of_birth, crime , description, gender, photo) 
	VALUES (2, 'Marko', 'Marković', 'Markić', 'Bulevar patrijarha Pavla 132, 21 000, Novi Sad', '22.10.1965', 'ubistvo sa predumišljajem, pljačka',
			'Visina 202cm, težina 130 kg, boja očiju: plava, plava kosa vezana u rep, tetovaža u obliku zmaja na vratu, slika džokera na levoj potkolenici, ožiljci na licu, potiljku i gornjem delu leve ruke, beleg na stomaku.',
			'Muški', 2);
			
# Lozinka je "lozinka"
INSERT INTO user (id, email, first_name, last_name, password,  is_sync)
	VALUES (1, "borkovac.dragan@gmail.com", "Dragan", "Borkovac", "$2a$10$BddBmEuvDOS7ZSoTyzEtR.oQy8kwy81PZomsfzQVA.4DixWpHe6PS", true);
INSERT INTO user (id, email, first_name, last_name, password,  is_sync)
	VALUES (2, "savic.olga@gmail.com", "Olga", "Savić", "$2a$10$BddBmEuvDOS7ZSoTyzEtR.oQy8kwy81PZomsfzQVA.4DixWpHe6PS", true);
INSERT INTO user (id, email, first_name, last_name, password,  is_sync)
	VALUES (3, "novakovic.jovana@gmail.com", "Jovana", "Novaković", "$2a$10$BddBmEuvDOS7ZSoTyzEtR.oQy8kwy81PZomsfzQVA.4DixWpHe6PS", true);
			
INSERT INTO report (id, city, comment, date, ex_convict_id, lang, lat, location, user_id, is_sync)
	VALUES (1, "Novi Sad","-", "25.06.2020", 2, 45.264251,  19.827240, "Železnicka stanica", 1, true);
	

	