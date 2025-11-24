# Integrative Project

**Integrantes:**
- Luis Gabriel Cely Niño
- Julian Alegrandro Gil
- Laura Sofia Lopez Mora
- Laura Valentina Arevalo Sierra

**Asignatura:** Programación Orientada a Objetos, bases de datos

**Tema Integrador** Sistema para control de ingreso de equipos tecnologicos y biomedicos externos

## Contexto y Proposito
Desarrollar un sistema que permita registrar, monitorear y controlar el ingreso de equipos tecnologicos y biomedicos externos al hospital, con el fin de garantizar la trazabilidad, seguridad operativa y cumplimiento de los protocolos institucionales establecidos.

## Aplicacion de Clean Architecture

los siguientes paquetes cumplen con el concepto de arquitectura limpia.

- edu.usta.application
    - usecases
    - dto
- edu.usta.domain
  - entities
  - enums
  - repositories 
- edu.usta.infrastructure
    - config
- edu.usta.ui
  - bootstrap


## PROBLEMÁTICA DEL PROYECTO INTEGRADOR
El proyecto integrador aborda la necesidad de gestionar de manera eficiente la información relacionada con equipos, proveedores y personas dentro de un sistema. La problemática principal se centra en la falta de una plataforma centralizada que permita registrar, consultar y administrar datos esenciales de forma rápida y ordenada.
Nos pudimos dar cuenta de que cuando la información está dispersa o no tiene una estructura clara, empiezan a aparecer varios problemas: se pierde tiempo buscando datos, se repiten registros sin darse cuenta, y es difícil llevar un control real del estado de los equipos o de las personas involucradas. Además, la trazabilidad se vuelve casi imposible, porque no hay una ruta clara de quién hizo qué o cuándo.
Todo esto generaba procesos lentos, confusos y poco eficientes. Por eso surgió la necesidad de crear un sistema más organizado, que permitiera centralizar la información y mejorar la manera en que se gestiona. Con este proyecto, la idea es resolver esos inconvenientes para que los datos sean más accesibles, estén mejor estructurados y se puedan consultar sin complicaciones.

## LISTADO DE HERRAMIENTAS USADAS
- Java 17 (Lógica de negocio y estructura del proyecto).
- Maven (Gestión de dependencias con el archivo pom.xml).
- PostgreSQL (Base de datos relacional).
- SQL (Uso de archivos DDL y DML para creación y carga de tablas).
- Draw.io (Diseño del diagrama entidad-relación).
- Entidades y repositorios (Arquitectura de dominio con clases como Person, Provider, Equipment, etc.). 
- Git / GitHub (Control de versiones).
- VS Code (Entorno de desarrollo).

## SOLUCIÓN APLICADA EN EL PROYECTO
La solución que implementamos en el proyecto integrador se basó en organizar toda la información dentro de un sistema mucho más claro y estructurado. Para eso, primero se definió las entidades principales como los equipos, los proveedores y las personas, asegurándonos de que cada una tuviera sus atributos correctos y que pudieran relacionarse entre sí sin generar confusión.
Después de tener la estructura conceptual, Se construyo la base de datos en PostgreSQL. Allí se crearon las tablas bien organizadas y normalizadas usando los archivos DDL, y luego se cargaron los datos iniciales con los DML para poder probar el funcionamiento. Esto permitió ver cómo fluía la información de verdad y si las relaciones estaban funcionando como se esperaba.
En la parte lógica del sistema se usó Java, donde se implementó casos de uso que permitían hacer operaciones básicas como registrar, consultar, actualizar o eliminar información. Estos casos de uso estaban diseñados de forma genérica para que el sistema fuera flexible y fácil de mantener más adelante.
También nos apoyamos en el diagrama hecho en Draw.io para asegurarnos de que todo lo que se estaba creando coincidiera exactamente con el diseño inicial. Gracias a eso, se pudo tener una visión más clara y evitar errores mientras se desarrollaba la aplicación.
En general, la solución aplicada permitió centralizar toda la información, mejorar su organización y hacer que el manejo de datos fuera más rápido y eficiente.
