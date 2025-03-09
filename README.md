# Movie API

## Feladat

Write a single REST api which uses 2 different public APIs (http://www.omdbapi.com/ and  https://www.themoviedb.org/documentation/api) to retrieve information about a given  movie. By design, the fast response of this interface will be the primary goal. The  secondary goal is to store the search pattern for future statistics purposes. The number of predicted queries cause the need to use a cache mechanism to avoid the  risk of high load.

There should be two parameters: one to specify which API is to be used and the other to  specify the movie title. i.e. http://localhost:8080/movies/{movieTitle}?api={apiName}

**Expected output:**

A list of all matching movies, with year + director.

**Response:**

```json
{
  "movies": [
    {
      "Title": "Countdown",
      "Year": "1967",
      "Director": ["Robert Altman"]
    },
    {
      "Title": "Count down now",
      "Year": "2100",
      "Director": ["John Doe", "Jane Doe"]
    }
  ]
}
```

**omdbapi.com:**

1. Search movie
   http://www.omdbapi.com/?s=Avengers&apikey=<<api key>>
2. Get detail of a movie http://www.omdbapi.com/?t={Specific title  
   of movie}&apikey=<<api key>>
3. http://www.omdbapi.com/?i={imdbID}&apikey=<<api key>>
   
**themoviedb.org:**

1. Search movie:https://api.themoviedb.org/3/search/movie?api_key=<<api  key>>&query=Avengers&include_adult=true
2. Get detail of a movie: https://api.themoviedb.org/3/movie/{movie id}?api_key=<<api key>> 3. Get movie credits: https://api.themoviedb.org/3/movie/{movie id}/credits?api_key=<<api key>>
   
Please try to pay attention to the different responses of the APIs, regarding performance (not only  from user perspective, but technical and maintaining perspective as well), user-friendly aspects.

Please write the code to a production standard that you think would be acceptable for deployment  on our stack.

Stack:

- Platform: Spring Boot, Hibernate  
- Caching: Redis
- Database: MySQL
- Toolchain, testing: Maven, JUnit, Mockito

## Funkciók

* Filmek keresése cím alapján.
* Az eredmények egyesítése az OMDB és TMDB adatbázisokból.
* Az API válaszai JSON formátumban érhetők el.
* Redis alapú gyorsítótárazás a teljesítmény növelése érdekében.
* MySQL adatbázis a keresési naplók tárolására.
* Aszinkron eseménykezelés a keresési naplók mentéséhez.
* Reaktív webkliens a külső API-k lekérdezéséhez.
* OpenAPI dokumentáció (Swagger UI).
* Docker támogatás.
* WireMock alapú integrációs tesztek.

## Használat

1.  Klónozza a repót: `git clone <repo_url>`
2.  Navigáljon a projekt mappájába: `cd movie-api`
3.  Állítsa be az API kulcsokat az `application.properties` fájlban.
4.  Állítsa be a MySQL és Redis konfigurációját az `application.properties` fájlban vagy a `docker-compose.yml` fájlban.
5.  Buildelje és futtassa az alkalmazást Maven segítségével: `mvn spring-boot:run`
6.  Vagy buildelje és futtassa az alkalmazást Dockerrel: `docker-compose build && docker-compose up -d`
7.  Az API a `http://localhost:8080/movies/{movieTitle}` címen érhető el.
8.  A Swagger UI a `http://localhost:8080/swagger-ui.html` címen érhető el.

## Docker

A projekt Dockerrel is futtatható. A `docker-compose.yml` fájl tartalmazza a szükséges konfigurációt a MySQL, Redis és az alkalmazás konténerekhez.

mysql adatbázis indítása:

```bash
docker run --name movieinfo \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=movieinfo \
  -p 3306:3306 \
  -d mysql:latest
  ```

redis indítása:

```bash
docker run --name movieinfo-redis -p 6379:6379 -d redis:latest
```

## Tesztek

```bash
mvn test -Dspring.profiles.active=test`
```

## Függőségek

* Spring Boot
* Spring WebFlux
* Spring Data Redis
* Spring Data JPA
* MySQL Connector/J
