OVO SU SVI POSTMAN UPITI ZA SERVER SIDE ZAGTEVE


http://localhost:8090/api/ispit SVI ISPITI
http://localhost:8090/api/ispit/5 VRATI ISPIT PO ID
http://localhost:8090/api/ispit KREIRAJ NOVI ISPIT
{
  "datumOdrzavanja": "2025-01-20",
  "vremePocetka": "12:00",
  "zakljucen": false,
  "predmetId": 3,
  "nastavnikId": 2,
  "ispitniRokId": 1
}
http://localhost:8090/api/ispit/5 UPDATE ISPITA
{
  "datumOdrzavanja": "2025-02-01",
  "vremePocetka": "09:00",
  "zakljucen": true,
  "predmetId": 4,
  "nastavnikId": 1,
  "ispitniRokId": 2
}
http://localhost:8090/api/ispit/5 OBRISI ISPIT
http://localhost:8090/api/ispit/5/prijavljeni PRIJAVLJEN STUDENT ZA ISPIT
http://localhost:8090/api/ispit/5/prosecna-ocena PROSECNA OCENA NA ISPITU
http://localhost:8090/api/ispit/rezultati/5 REZULTATI ISPITA
http://localhost:8090/api/ispit/broj-polaganja?indeksId=3&predmetId=2 KOLIKO PUTA JE STUDENT POLAGAO PREDMET

http://localhost:8090/api/prijava PRIJAVLJIVANJE ISPITA
{
  "ispitId": 5,
  "studentIndeksId": 3
}
http://localhost:8090/api/prijava/unesi-izlazak/12 UPISIVANJE POLOZENIH PREDMETA U TABELU POLOZEN PREDMET

http://localhost:8090/api/student KREIRANJE STUDENTA
{
  "ime": "Marko",
  "prezime": "Marković",
  "srednjeIme": "Petar",
  "jmbg": "1234567890123",
  "email": "marko@example.com",
  "godinaUpisa": 2023,
  "studProgramOznaka": "RI",
  "broj": 101,
  "aktivanIndeks": true,
  "studijskiProgramId": 2
}
http://localhost:8090/api/student/all SELEKCIJA SVIH STUDENATA
http://localhost:8090/api/student/10 STUDENT PO ID
http://localhost:8090/api/student/10 BRISANJE STUDENTA
http://localhost:8090/api/student/indeks?broj=101&godina=2023 UVID U STUDENTA PO INDEKSU
http://localhost:8090/api/student/polozeni?broj=101&godina=2023&page=0&size=10 POLOZENI ISPITI STUDENTA PO INDEKSU
http://localhost:8090/api/student/nepolozeni?broj=101&godina=2023&page=0&size=10 NEPOLOZENI ISPITI
http://localhost:8090/api/student/upisane-godine?broj=101&godina=2023 PREGLED UPISANIH GODINA PO BROJU INDEKSA
http://localhost:8090/api/student/upisi-godinu UPIS STUDENATA U NOVU SKOLSKU GODINU
{
  "broj": 101,
  "godina": 2023,
  "godinaKojaSeUpisuje": 2,
  "prenetiPredmetiIds": [1, 2]
}
http://localhost:8090/api/student/obnovljene-godine?broj=101&godina=2023 PREGLED SVIH OBNOVLJENIH GODINA PO INDEKSU STUDENTA
http://localhost:8090/api/student/obnova-godine OBNOVA GODINE STUDNETA
{
  "broj": 101,
  "godina": 2023,
  "godinaKojaSeObnavlja": 2,
  "nepolozeniIds": [3, 4],
  "izborIzNaredneGodine": [5],
  "espbUkupno": 45
}
http://localhost:8090/api/student/uplata DODAVANJE NOVE UPLATE
{
  "broj": 101,
  "godina": 2023,
  "iznosDin": 25000
}
http://localhost:8090/api/student/preostalo-za-uplatu?broj=101&godina=2023 PREOSTALO ZA UPLATU
http://localhost:8090/api/student/pretraga?ime=Marko&prezime=&page=0&size=10 PRETRAGA PO IMENU ILI PREZIMENU
http://localhost:8090/api/student/pretraga-srednja-skola-naziv?naziv=Gimnazija&page=0&size=10 PRETRAGA PO NAZIVU SREDNJE SKOLE
