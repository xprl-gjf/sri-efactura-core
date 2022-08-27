# sri-efactura-model

Clases de Java para representar comprobantes electrónicos por SRI.
La mayoría del código es generado automáticamente por JAXB XJC.

Las clases generadas incluyen anotaciones de validación por `jakarta.validation`.
Las pruebas unitarias muestran ejemplos de cómo realizar la validación contra las anotaciones
`jakarta` por `org.hibernate.validator`.
(Ver, por ejemplo, [JaxbTestFixture](test/java/ec/gob/sri/JaxbTestFixture.java).)
