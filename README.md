# TodoApp – Testdriven utveckling med CI

Detta är en testdriven applikation byggd med Spring Boot, Java och MySQL, där målet var att följa principerna inom TDD (Test Driven Development) samt implementera kontinuerlig integration med GitHub Actions.

##  Funktioner

- Skapa, hämta, uppdatera och radera användare (`User`) och uppgifter (`Task`)
- REST API byggt med tre lager (Controller, Service, Repository)
- JPA med MySQL för persistens
- Testdriven utveckling med Unit, Component och Integration Tester
- Automatisk körning av tester med GitHub Actions

##  Tester

Projektet innehåller följande tester:

 **Unit-tester (med Mockito)**  
→ `UserServiceTest`, `UserServiceVGTest`, `TaskServiceTest`

 **Component-tester (med MockMvc)**  
→ `UserControllerTest`, `TaskControllerTest`

 **Integrationstester (mot riktig databas)**  
→ `TaskIntegrationTest`

Totalt finns det över **10 tester** som verifierar applikationens funktionalitet, både med vanliga och avancerade testfall (VG-nivå).

##  Continuous Integration

En GitHub Actions workflow (`main.yml`) kör testerna automatiskt vid varje push och pull request:

- `mvn clean test` körs som en job
- Resultat loggas i Actions-fliken
- Alla tester måste passera för att koden ska kunna mergas

##  Teknikstack

- Java 17
- Spring Boot
- Maven
- MySQL (lokal databas & H2 för test)
- GitHub Actions

## ▶ Så här kör du applikationen

1. Klona projektet:
   ```bash
   git clone https://github.com/ditt-användarnamn/todoapp.git
   cd todoapp

Projektstruktur
src
├── main
│   └── java
│       └── com.example.todoapp
│           ├── controller
│           ├── model
│           ├── repository
│           └── service
└── test
└── java
└── com.example.todoapp
├── unit
├── component
└── integration


Examinationsuppgift

Detta projekt är inlämningen för kursen Testning i Spring Boot, och presenterades
individuellt. Alla krav gällande TDD och CI är uppfyllda.