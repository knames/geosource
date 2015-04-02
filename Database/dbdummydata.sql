insert into users values ("0001", "okenso");
insert into users values ("115161761450794914816", "Josh");
insert into users values ("171717171717", "frank");
insert into users values ("1515151", "xx420Blazexx");

insert into channels values ("okenso's channel", "okenso", 1, true);	
insert into channels values ("okenso's other channel", "okenso", 2, true);
insert into channels values ("pothole", "okenso", 3, true);
insert into channels values ("poly", "josh", 1, true);

insert into channelfavs values ("okenso", "pothole", "okenso");
insert into channelfavs values ("okenso", "okenso's channel", "okenso");
insert into channelfavs values ("josh", "poly", "josh");

insert into admin (a_username) values ("josh");


insert into admin (a_username) values ("okenso");

insert into private_view_channels values 
	("okenso", "poly", "josh"),
	("josh", "pothole", "okenso")
	;




insert into channelmods values
	("okenso", "poly", "josh"),
	("josh", "pothole", "okenso")
	;


insert into users_fav_posts values 
	("okenso", "pothole", "okenso", 1),
	("josh", "pothole", "okenso", 1)
	;


