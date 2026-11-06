# Privacidad · ITA MAÑA 0.6.1

Responsable del proyecto: Carlos Busón Buesa. Texto de 5 de noviembre de 2026.

## Datos y permisos

La cámara trasera proporciona la imagen para apuntar. Al registrar una observación o A/B/retorno se guarda una foto. Los sensores proporcionan orientación e inclinación. La ubicación permite mostrar coordenadas, precisión y antigüedad si se concede el permiso. El micrófono es opcional y se solicita solo al grabar audio o vídeo con sonido. No solicita contactos ni ubicación en segundo plano. Las grabaciones se realizan en primer plano, se detienen al salir de la aplicación y quedan como borrador hasta Guardar. No hay transcripción ni envío a servidores.

Guardar ubicación está desactivado inicialmente. Puede activarse para incluir coordenadas en nuevos registros y sus exportaciones. El cuaderno puede contener fotos, notas, ángulos, fecha/hora y coordenadas si se activaron. Las instantáneas angulares A/B/C no copian por sí mismas ubicación.

## Almacenamiento y comunicaciones

Los registros permanecen en almacenamiento privado de la app mediante IndexedDB. No hay cuentas, servidor de registros, anuncios, analítica ni envío automático. El APK no solicita permiso de Internet. El cuaderno no tiene cifrado propio adicional; depende de la protección del dispositivo Android.

Los enlaces de Ayuda abren el navegador externo solo al pulsarlos. Esos sitios y navegador tienen sus condiciones y pueden recibir información normal de navegación; la app no añade notas ni coordenadas a sus URLs.

El usuario elige dónde exportar mediante el selector Android, que puede ofrecer nube o almacenamiento local. Revisa fotos, notas y coordenadas antes de compartir. La app no controla los archivos exportados ni sus copias posteriores.

## Conservación y control

Los permisos pueden revocarse en Ajustes de Android. Cámara/sensores se detienen al pausar/salir. El manifiesto desactiva copias de seguridad y declara reglas de extracción de datos.

No hay borrado automático del cuaderno. Borrar datos o desinstalar elimina los registros internos: exporta antes si quieres conservarlos. Esto no elimina copias exportadas. Si falla el almacenamiento persistente, los registros pueden quedar solo en memoria; se advierte antes de salir.

## Consultas

Una vez publicado el repositorio, utiliza Issues para preguntas sin datos personales. Para problemas sensibles, consulta SECURITY.md. Este texto describe esta versión; las modificadas pueden comportarse de otra forma.


## Instrumentos integrados en 0.6.1

Selector Teodolito / Brújula / Clinómetro y controles comunes de Registrar, Cuaderno, Ajustes, Ayuda y Salir, también en vista ampliada. Esfera transparente sobre la cámara con lectura central sólida. La brújula ofrece Plano (dirección de buzamiento, buzamiento y rumbo RHR), Línea (dirección descendente e inmersión) y Azimut (borde superior físico). El clinómetro mide inclinación del borde largo y porcentaje de pendiente. Usa la matriz nativa completa; los ejes de contacto son físicos y no cambian con el giro de la pantalla.

En brújula y clinómetro se puede desactivar la cámara de fondo en Ajustes. Con o sin cámara, Registrar guarda la lectura en el mismo cuaderno; la foto es opcional y se toma al registrar, no al retener. Su fecha queda separada de la fecha del sensor. Guardar ubicación permanece desactivado por defecto. Retener congela valores y referencia, muestra antigüedad y se libera al detener o cambiar modo/tipo/corrección.

Ayuda ES/PT/EN ampliada con colocación, buzamiento, inclinación por contacto y método A/B. La cara de contacto debe ser paralela a la pantalla y plana; la funda o cámaras salientes no deben inclinarla. El usuario comprueba el apoyo: no se detecta automáticamente. Para líneas y clinómetro se utiliza el borde largo desde la base al extremo original del auricular. No confundirlo con el eje óptico de la cámara.

Las direcciones geográficas se omiten sin norte magnético fiable, incluso en modo giroscópico relativo; la inclinación sigue disponible si hay gravedad válida. Planos casi horizontales (<0,5°) no tienen dirección presentada; casi verticales (>89,5°) muestran eje de rumbo sin lado único. Líneas casi horizontales muestran eje y casi verticales omiten dirección. Estos umbrales son de representación, no exactitud. La corrección manual sigue siendo un ajuste registrado, no una declinación consultada automáticamente ni calibración.

Pruebas nuevas de geometría: cuadrantes, rotación dentro del mismo plano, cara opuesta, inversión de lineación, casos singulares, datos inválidos y ausencia de norte. Flujo probado con y sin cámara, retención/liberación, foto opcional y privacidad. Revisión de interfaz con orientación simulada excluida del APK. Compilación y firma verificadas. No se ha ensayado esta versión en teléfono físico ni validado exactitud o resultados educativos.

Base técnica: matriz East/North/Up y ejes físicos Android, https://developer.android.com/develop/sensors-and-location/sensors/sensors_position. Los métodos geológicos y convenciones aplicados se documentan en logic/geo-core.cjs y sus pruebas.
