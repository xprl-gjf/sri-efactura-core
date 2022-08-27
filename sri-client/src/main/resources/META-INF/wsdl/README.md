Archivos WSDL descargando por el ambiente de producción, el 17-JUN-2022:

* https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl
* https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl

### ¡ADVERTENCIA!

El uso del `ref=tns:XXX` elementos en el WSDL da como resultados mensajes SOAP que no conforman
con ellos de los servidores SRI.

(La raíz del problema es el atributo `elementFormDefault="unqualified"` por el elemento `&lt;xs:schema&gt;`...)

Para generar código Java correcto, tenía que modificar el WSDL con el patch siguiente:

```diff
diff --git a/lib/src/main/resources/META-INF/wsdl/cel.sri.gob.ec.AutorizacionComprobantesOffline.wsdl b/lib/src/main/resources/META-INF/wsdl/cel.sri.gob.ec.AutorizacionComprobantesOffline.wsdl
index 8bb55f9..f33af37 100644
--- a/lib/src/main/resources/META-INF/wsdl/cel.sri.gob.ec.AutorizacionComprobantesOffline.wsdl
+++ b/lib/src/main/resources/META-INF/wsdl/cel.sri.gob.ec.AutorizacionComprobantesOffline.wsdl
@@ -37,3 +37,3 @@
           <xs:sequence>
-            <xs:element maxOccurs="unbounded" minOccurs="0" ref="tns:autorizacion"/>
+            <xs:element maxOccurs="unbounded" minOccurs="0" name="autorizacion" type="tns:autorizacion"/>
           </xs:sequence>
@@ -54,3 +54,3 @@
           <xs:sequence>
-            <xs:element maxOccurs="unbounded" minOccurs="0" ref="tns:mensaje"/>
+            <xs:element maxOccurs="unbounded" minOccurs="0" name="mensaje" type="tns:mensaje"/>
           </xs:sequence>
@@ -89,3 +89,3 @@
           <xs:sequence>
-            <xs:element maxOccurs="unbounded" minOccurs="0" ref="tns:autorizacion"/>
+            <xs:element maxOccurs="unbounded" minOccurs="0" name="autorizacion" type="tns:autorizacion"/>
           </xs:sequence>
diff --git a/lib/src/main/resources/META-INF/wsdl/cel.sri.gob.ec.RecepcionComprobantesOffline.wsdl b/lib/src/main/resources/META-INF/wsdl/cel.sri.gob.ec.RecepcionComprobantesOffline.wsdl
index 0848dfd..b77fa75 100644
--- a/lib/src/main/resources/META-INF/wsdl/cel.sri.gob.ec.RecepcionComprobantesOffline.wsdl
+++ b/lib/src/main/resources/META-INF/wsdl/cel.sri.gob.ec.RecepcionComprobantesOffline.wsdl
@@ -32,3 +32,3 @@
           <xs:sequence>
-            <xs:element maxOccurs="unbounded" minOccurs="0" ref="tns:comprobante"/>
+            <xs:element maxOccurs="unbounded" minOccurs="0" name="comprobante" type="tns:comprobante"/>
           </xs:sequence>
@@ -45,3 +45,3 @@
           <xs:sequence>
-            <xs:element maxOccurs="unbounded" minOccurs="0" ref="tns:mensaje"/>
+            <xs:element maxOccurs="unbounded" minOccurs="0" name="mensaje" type="tns:mensaje"/>
           </xs:sequence>
```
