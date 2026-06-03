# Platformă Food Delivery - Proiect Java (Etapa II)

Acest proiect reprezintă o aplicație de tip consolă pentru gestionarea unei platforme de Food Delivery. Aplicația permite administrarea restaurantelor, produselor, utilizatorilor (clienți și șoferi) și fluxului de comenzi, utilizând o bază de date relațională pentru persistență.

## 1. Structura Proiectului (Pachete)
Codul este organizat modular pentru a respecta principiile de design software:
*   `ro.delivery.model`: Conține clasele de tip entitate (POJO).
*   `ro.delivery.config`: Gestionarea conexiunii la baza de date (JDBC).
*   `ro.delivery.repository`: Clasele care execută interogările SQL (CRUD).
*   `ro.delivery.service`: Logica de business și serviciul de audit.
*   `ro.delivery.main`: Clasa de pornire (`Main`) și interfața cu utilizatorul (`TerminalApp`).

## 2. Entități și Concepte OOP
*   **Utilizator (Abstract):** Definește proprietățile comune.
*   **Client & Sofer & Admin:** Moștenesc clasa `Utilizator`.
*   **Restaurant & Produs:** Relație de tip *One-to-Many* (un restaurant are mai multe produse).
*   **Comanda:** Entitate centrală ce face legătura între Client, Restaurant și Șofer.
*   **Adresa:** Folosită prin compoziție în clasa `Client`.

**Concepte OOP aplicate:** Moștenire, Încapsulare, Polimorfism, Abstracție, Singleton Pattern (pentru Repouri, Audit și Configurație).

## 3. Funcționalități Principale

### Admin Panel (Gestiune Completă CRUD)
*   **Restaurante:** Adăugare, Modificare (Smart Update), Ștergere, Vizualizare.
*   **Produse:** Gestionarea meniurilor pentru fiecare restaurant.
*   **Utilizatori:** Administrarea listelor de clienți și șoferi.
*   **Monitorizare:** Vizualizarea tuturor comenzilor din sistem și a statusurilor acestora.

### Portal Client
*   **Autentificare:** Login pe bază de nume sau Înregistrare cont nou.
*   **Comenzi:** Vizualizare meniuri, selecție produse și plasare comandă (calcul automat total).
*   **Istoric:** Vizualizarea comenzilor proprii cu separare vizuală între cele Active și cele Finalizate.

### Portal Șofer
*   **Gestiune Livrări:** Vizualizarea comenzilor disponibile (`PENDING`).
*   **Flux Livrare:** Preluarea unei comenzi (`IN_PROGRESS`) și finalizarea acesteia (`DELIVERED`).

## 4. Serviciul de Audit
Toate acțiunile utilizatorilor sunt salvate automat într-un fișier local `audit.csv`. Fiecare rând conține:
*   Numele acțiunii executate.
*   Timestamp-ul exact (dată și oră).

## 5. Configurare și Instalare

### Premise
1.  Server MySQL pornit (recomandat XAMPP).
2.  Driver JDBC (`mysql-connector-j`) adăugat în bibliotecile proiectului.

### Setup Bază de Date
Executați următorul script SQL în MySQL (phpMyAdmin):

```sql
CREATE DATABASE IF NOT EXISTS food_delivery;
USE food_delivery;

CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS restaurante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS produse (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    pret DOUBLE NOT NULL,
    id_restaurant INT,
    FOREIGN KEY (id_restaurant) REFERENCES restaurante(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS clienti (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    oras VARCHAR(100),
    strada VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS soferi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    vehicul VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS comenzi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_client INT,
    id_restaurant INT,
    id_sofer INT DEFAULT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    total DOUBLE,
    FOREIGN KEY (id_client) REFERENCES clienti(id),
    FOREIGN KEY (id_restaurant) REFERENCES restaurante(id),
    FOREIGN KEY (id_sofer) REFERENCES soferi(id)
);

INSERT INTO admins (username, password) VALUES ('admin', 'admin123');
```

## 6. Moduri de Rulare (Clasa Main)
La pornirea aplicației, utilizatorul are două opțiuni:

1.  **Manual (Interactiv):** Aplicația pornește normal, permițând utilizatorului să navigheze prin meniuri și să introducă date de la tastatură.
2.  **Test Automat (Script):**
    *   Sistemul **golește automat** toate tabelele bazei de date și resetează ID-urile la 1 (folosind `TRUNCATE` și `FOREIGN_KEY_CHECKS`).
    *   Se injectează un flux de date predefinit folosind `ByteArrayInputStream`.
    *   Se simulează automat: Creare date admin -> Înregistrare Client -> Plasare Comandă -> Livrare Șofer -> Raport final.
    *   Este ideal pentru demonstrarea rapidă a funcționalității întregului sistem.

## 7. Caracteristici Speciale (Bonus)
*   **Smart Update:** La modificarea unei entități, sistemul aduce datele actuale din DB. Utilizatorul poate apăsa `Enter` pentru a păstra valoarea veche sau poate scrie o valoare nouă.
*   **Input Safety:** Metode custom pentru citirea numerelor, prevenind închiderea programului în cazul introducerii accidentale de text.
*   **Securitate:** Sistem de login pentru admini cu număr limitat de încercări (3).

## 8. Tehnologii și Concepte OOP utilizate:
*   **Încapsulare:** Toate atributele sunt `private/protected`, accesate prin gettere și settere.
*   **Moștenire:** Ierarhia de clase `Utilizator` (părinte) -> `Client`, `Sofer`, `Admin` (copii).
*   **Polimorfism:** Utilizarea referințelor de tip părinte pentru a gestiona obiecte de tip copil în colecții.
*   **JDBC (Java Database Connectivity):** Utilizarea interfețelor `Connection`, `PreparedStatement` și `ResultSet` pentru comunicarea cu MySQL.
*   **Singleton Pattern:** Implementat în `AuditService`, `GenericRepository` și `DatabaseConfiguration` pentru un management eficient al instanțelor unice.
*   **File I/O:** Implementarea unui sistem de logare persistent în fișier extern (CSV).
*   **Input Streams:** Manipularea `System.in` pentru simularea input-ului de la tastatură în scopuri de testare.
