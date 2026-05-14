Lien Swagger : http://localhost:8080/swagger-ui/index.html#/


Pour tester en local, remplacer le application.properties par ceci : 

```
# Connexion à SQL Server
#spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=BookHubDB;encrypt=true;trustServerCertificate=true
#spring.datasource.username=sa
#spring.datasource.password=Pa$$w0rd
#spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.settings.web-allow-others=true
server.servlet.session.cookie.same-site=strict

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
#spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
```
