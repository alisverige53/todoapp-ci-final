# README.md

## Teststrategi och syfte

I detta projekt har vi implementerat tre olika typer av tester för vår Spring Boot-applikation:

- **Unit Tester**: För att testa enskilda metoder i `ProductService` utan beroenden till andra lager.
- **Komponenttester**: För att testa `ProductController` i isolering, med ett mockat service-lager.
- **Integrationstester**: För att testa hela applikationsflödet mot en riktig MySQL-databas (`product_db`).

Syftet med dessa tester är att säkerställa kvalitet, korrekt logik och upptäcka buggar i ett tidigt skede av utvecklingen. Genom att använda olika typer av tester kan vi verifiera både enskilda komponenter och systemets helhet.

---

## Verktyg och tekniker

- **JUnit 5**: Huvudramverket för att skriva och köra tester.
- **Mockito**: För att mocka beroenden i unit- och komponenttester.
- **TestRestTemplate**: För att skicka HTTP-anrop och testa integrationer.
- **Spring Boot Test**: För att stödja testning av Spring Boot-komponenter och konfigurera testmiljön.

Dessa verktyg valdes eftersom de integreras sömlöst med Spring Boot och erbjuder starkt stöd för både enhetstester och integrationstester.

---

## Hur man kör applikationen

1. Klona projektet från GitHub.
2. Bygg och kör applikationen med Maven:

```bash
mvn clean install
mvn spring-boot:run
