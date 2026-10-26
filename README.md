# LittleDokyHub — avance 3

Aplicación de pedidos de pizzería con Spring Boot 3.3.4, Java 17, Thymeleaf,
Spring Security, Spring Data JPA/Hibernate y MySQL 8+.

La persistencia de la aplicación utiliza **JPA/Hibernate**: productos, categorías,
usuarios, opciones, contacto, carritos y pedidos se gestionan mediante entidades,
repositorios Spring Data JPA y servicios transaccionales. Las consultas en cascada
usan JPQL y proyecciones DTO; la confirmación del pedido usa bloqueos JPA para
proteger el stock y conserva sus detalles y datos de entrega.

El código de aplicación no utiliza `JdbcTemplate` ni consultas SQL nativas.
Hibernate sí utiliza internamente JDBC y el controlador de MySQL para conectarse.
Los scripts SQL de instalación/migración se conservan como entregables; algunas
pruebas usan JDBC únicamente para preparar datos y comprobar resultados.

## Requisitos

- Git para descargar el repositorio.
- **JDK 17**, con `java` y `JAVA_HOME` configurados para esa instalación.
- MySQL 8+ instalado y su servicio iniciado para ejecutar la aplicación.
- Cliente `mysql` disponible en el PATH, o MySQL Workbench para importar el SQL.
- Conexión a internet para la primera descarga de Maven y las dependencias.
  Bootstrap e iconos también se cargan desde CDN al abrir las páginas.

Maven no necesita instalarse por separado: se utiliza el wrapper incluido.
Node.js 18+ y npm son opcionales y solo se requieren para las pruebas de interfaz.

## Documentos del grupo

- [Informe compartido](https://utpedupe-my.sharepoint.com/:w:/g/personal/u24268913_utp_edu_pe/IQAj_ABtRRULRZs7FaCx9Uj9AQ8qbTL_AkfGYUdiAa0A1Uw)
- [Presentación](https://www.canva.com/design/DAHWceo15xg/nIt4bVkshpe6G_nwakZIHg/edit)
