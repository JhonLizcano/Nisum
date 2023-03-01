# Nisum

Esta API se compila con comandos de Maven

Para construir el compilado de tipo JAR file se usa el comando
> mvn clean install

Y la ejecución del jar con el comando
> java -jar c:jarpath.jar

Una vez se despliega la API (Puerto 8080) esta misma crea la base de datos a partir de las variables del application.properties
y del mapeo que realiza de los objetos del modelo anotados con @Entity

Para validar u observar la base de datos se puede abrir en el navegador la siguiente URL
> http://localhost:8080/h2-console/

con los datos: 

> Driver Class => org.h2.Driver
> 
> JDBC URL => jdbc:h2:mem:nisum
> 
> User Name => admin
> 
> Password => 123

Adicionalmente a esto y con la ayuda de Potman se puede importar el proyecto

> Nisum.postman_collection.json

que está en la raíz del proyecto y con el que se pueden realizar test de los casos de prueba que cumplen
las condiciones de la prueba
