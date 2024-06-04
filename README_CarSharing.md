# README_CarSharing

## Umsetzung
Aus Zeitgründen wurden "nur" die Requirements aus der Angabe implementiert und nicht wie vorgehabt auch der Bonus Teil (GeoCode-API) sowie eine GUI mit Thymeleaf.
Wie in der Beschreibung wurde für User und Vehicle die JPA-Repositories benutzt und die UserServices und VehicleService Classes dementsprechend modifiziert.

## Aufbau
Die docker-compose sowie der db Ordner wurde von Ihnen aus dem gitlab Projekt entnommen und für "carsharing" modifiziert. Zum Starten von postgreSQL im Docker muss man in der Ordnerstruktur **docker** in den **postgreSQL** Ordner, von wo man mit "docker compose up", den Prozess starten kann.

## Struktur
Ich habe ein "jpa" Package hinzugefügt (mit den JPA-Interfaces), allerdings sind die alten Repositories noch in "repository" drinnen, die für BillingSystem und VehicleStatusControler gebraucht werden. (Wird dann auch noch geändert, wirft aber sonst zu viele Fehler um sie gleich zu löschen).

## Constraints:

Mein OccupyVehicle Endpoint updated noch nicht korrekt die StatusDetails des Vehicles, sobald ein Fahrer dem Vehicle zugewiesen wird.

  


