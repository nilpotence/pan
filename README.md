#ipq-mas-metier

## setup the dev environment

### setup the database

```
$ cd ipq-mas-metier/
$ docker-compose up -d db
$ docker-compose exec db psql -U postgres
psql> CREATE SCHEMA masmetier;
psql> ALTER SCHEMA masmetier OWNER TO pg_database_owner;
psql> REVOKE ALL ON SCHEMA masmetier FROM PUBLIC;
psql> CREATE ROLE ipq LOGIN ENCRYPTED PASSWORD 'password';
psql> GRANT ALL ON SCHEMA masmetier TO ipq;
psql> CREATE EXTENSION "uuid-ossp" WITH SCHEMA masmetier;
```
### setup object storage buckets

```
$ docker-compose up -d s3
```

- In a browser, go to [http://localhost:9001](http://localhost:9001), connect using login ```minioadmin``` 
and password ```minioadmin```.
- create bucket ```infoparquet-docs```
- create bucket ```infoparquet-capsules```


## run the dev environment

### start
```
$ cd ipq-mas-metier/
$ docker-compose up -d
```

### monitor spring boot logs
```
$ docker-compose logs -f --tail 100 web
```

