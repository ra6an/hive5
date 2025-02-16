## Pokretanje aplikacije

1 Klonirajte repozirotijum:
  ```bash
   git clone https://github.com/ra6an/hive5.git
   ```

2 U root folderu projekta pokrenite Docker Compose za DB
  ```bash
  docker-compose up --build
  ```

3 Pokrenite komandu za build aplikacije
  ```bash
  mvn clean package
  ```

4 Pokrenite aplikaciju (/target)
  ```bash
  java -jar ./<ime_buildanog_filea_u_target_folderu>.jar
  ```

## Frontend mozete klonirati sa https://github.com/ra6an/hive5front
