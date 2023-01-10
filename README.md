# Wepa-projekti Twiitser
Sosiaalisen median palvelu Twiitser web-palvelinohjelmointi 2021 -kurssilta.

[Tehtävänanto](https://web-palvelinohjelmointi-21.mooc.fi/projekti) sisälsi palvelun pakolliset toiminnot. Toteutustapa oli melko vapaa. Springiä piti käyttää ja kurssin aika opittuja asioita sai hyödyntää.


### KÄYTTÖOHJE

Luo uusi tunnus napista *Sign up* tai kirjaudu sisään *Log in* valmiilla tunnuksella.
Lokaalisti käynnistäessä tietokannassa on valmiiksi vähän sisältöä testauksen helpottamiseksi.

___________________________
Lokaalin valmis testitunnus:
username: napis
salasana: kissa
___________________________

Omalla seinällä (My wall) näkee omat sekä seuraajiensa uusimmat viestit kommentteineen.
Vain seuraajat voivat nähdä käyttäjän viestit ja kuvat.
Kuka vain voi alkaa seurata ketä tahansa, mutta jos toisen käyttäjän torjuu (Block) niin
sitten kumpikaan ei pysty seuraamaan enää toista ennen kuin torjunta perutaan.
Torjuminen ei estä kommenttien näkymistä muiden käyttäjien sisällöissä.

Aloita esimerkiksi hakutoiminnolla niin saat muita käyttäjiä näkyviin.


### KÄYNNISTÄMINEN LOKAALISTI LINUX-YMPÄRISTÖSSÄ

- lataa repositorion tiedostot
- aja juuressa komento `mvn spring-boot:run`
- sovelluksen aukeaa osoitteessa http://localhost:8080
