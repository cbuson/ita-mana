# ITA MAÑA

Herramienta educativa Android para practicar orientación, nivelación y comparación de ángulos en actividades de campo. Vinculada al proyecto ITA ARANDU. Autor: Carlos Busón Buesa · Licencia MIT.

**0.7.1 · Prototipo educativo en pruebas.** Precisión no validada para topografía. No sustituye un teodolito profesional ni mediciones certificadas.

[Descargar APK](downloads/ITA_MANA_0_7_1.apk) · [Guía](docs/USO.md) · [Privacidad](docs/PRIVACIDAD.md) · [Compilar](docs/COMPILAR.md) · [Cambios](CHANGELOG.md)

## Finalidad educativa

Apoya el desarrollo de competencias técnicas para usar equipamiento de observación y medición: apuntar con una mira, nivelar, fijar referencias, leer y comparar ángulos, registrar observaciones y reconocer errores. Complementa la formación y práctica supervisada con instrumentos reales. No se han medido todavía sus resultados de aprendizaje.

La brújula muestra NM (rojo) y NG estimado (azul), con declinación calculada localmente desde una ubicación reciente. Las cifras de ambas referencias corresponden al azimut del borde superior de pantalla. La lectura central conserva la dirección geológica magnética. Consulta [los cambios y límites](docs/RELEASE_0_7_1.md).

## Escribir y grabar notas

Cuaderno → Añadir nota → escribir → Guardar. No requiere estación ni sensores. Audio/vídeo opcionales: grabar, detener, revisar y guardar, hasta 60 s por nota. Permiso de micrófono solo al solicitarlo. [Cambios y límites de la grabación](docs/RELEASE_0_7_1.md).

## Cuaderno de campo

Estaciones y afloramientos con notas, muestras, descripción, interpretación y medidas vinculadas de los tres instrumentos. Conserva registros anteriores y revisiones. Exporta JSON, CSV, GeoJSON, ZIP y PDF textual. Importa JSON sin sobrescribir. [Guía y límites](docs/RELEASE_0_7_1.md).

## Funciones

- Cámara trasera, mira circular transparente, referencia fija y nivel lateral móvil; cuadrícula opcional.
- Azimut magnético, ángulo vertical y nivel lateral. Modo relativo sin norte si el teléfono dispone del sensor.
- Referencia A → B, diferencias horizontal y vertical, fotos vinculadas y comprobación manual del retorno a A.
- Cuaderno local, notas, exportación JSON/JPEG y ubicación guardada solo si se activa.
- Castellano, portugués e inglés; orientación vertical/horizontal, vista ampliada, ayuda y salida.

Medición y cuaderno funcionan sin conexión. Los enlaces de proyectos abren el navegador externo y requieren Internet. Es una aplicación Android con interfaz HTML local y sensores nativos. Abrir el HTML por separado no garantiza cámara/sensores y no equivale al APK.

## Instalar y empezar

Android 7.0 (API 24) o posterior, cámara trasera y sensores compatibles. La disponibilidad de lecturas depende del teléfono.

1. Descarga el APK y comprueba su huella en [SHA256SUMS.txt](downloads/SHA256SUMS.txt).
2. Abre el APK. Si Android lo solicita, autoriza la instalación para la aplicación desde la que lo abriste; puedes retirar ese permiso después.
3. Abre ITA MAÑA y concede cámara y, si la necesitas, ubicación. Pulsa **Activar instrumento**.
4. Abre **A → B**, apunta al primer punto y pulsa **1 · Fijar A / 0°**. Gira hacia B sin mover la base del trípode: aparecen ΔH y ΔV. Pulsa **2 · Registrar B**.
5. Vuelve al mismo A y pulsa **3 · Retorno A** para registrar el desacuerdo de retorno.

Actualiza las versiones 0.4–0.5.3 del mismo paquete y firma sin desinstalar. Exporta el cuaderno antes de borrar datos o desinstalar. Las versiones 0.1–0.3 tienen otro paquete y cuaderno independiente; no hay importación automática.

## Límites y estado de pruebas

La mira y cuadrícula no tienen calibración óptica ni escala angular. El verde señala una condición de nivel lateral, no exactitud de ±1° ni verticalidad del eje del trípode. El azimut magnético no aplica declinación automática. El modo relativo puede derivar. El ángulo de una visual no es el buzamiento de una capa. Fijar A / 0° no calibra el instrumento.

Las pruebas automáticas de cálculo, interfaz y privacidad han pasado. Las capturas aportadas por el usuario muestran funcionamiento de versiones previas en su teléfono; no validan exactitud. La 0.7.1 no ha sido ensayada físicamente desde el entorno de desarrollo. No hay auditoría independiente de seguridad ni garantía de riesgo cero.

## Código y documentación

`src/`: Java nativo. `assets/`: HTML/CSS/JavaScript local. `res/`: recursos Android. `logic/`: cálculo A/B. `tests/`: pruebas. `downloads/`: APK. No incluye SDK, JDK, claves privadas ni contraseñas.

[Construcción y pruebas](docs/COMPILAR.md) · [Seguridad](SECURITY.md) · [Revisión técnica](docs/REVISION_SEGURIDAD.md) · [Publicar en GitHub](docs/PUBLICAR_GITHUB.md) · [Notas de versión](docs/RELEASE_0_7_1.md)

## Português

Ferramenta educativa para desenvolver competências de uso de equipamentos de observação e medição. Permite praticar pontaria, nivelamento, referências e comparação angular. Interface e ajuda em português, espanhol e inglês. Precisão não validada para topografia; não substitui teodolito profissional nem prática supervisionada com equipamentos reais. [Baixar APK](downloads/ITA_MANA_0_7_1.apk).

## English

Educational Android tool for practising sighting, levelling, reference setting and angle comparison. Spanish, Portuguese and English interface/help. Offline measurements and a local notebook, with optional location storage. Accuracy has not been validated for surveying; it does not replace a professional theodolite or supervised practice with real equipment. [Download APK](downloads/ITA_MANA_0_7_1.apk).

## Proyectos relacionados

- [ITA ARANDU MS](https://cbuson.github.io/atlas-geocientifico-ms/)
- [PAG-ETR MS](https://cbuson.github.io/pag-etr-ms/)
- [PIH-MS](https://cbuson.github.io/pih-ms/)

ITA MAÑA es un nombre provisional formado a partir de ita y maña. La composición requiere revisión lingüística; no se afirma una traducción literal confirmada de «apuntar piedra».

## Licencia

Copyright © 2026 Carlos Busón Buesa. [MIT](LICENSE). La finalidad educativa describe el proyecto; MIT permite otros usos y modificaciones con sus condiciones. No se garantiza el comportamiento de versiones modificadas.


## Instrumentos integrados en 0.7.1

Selector Teodolito / Brújula / Clinómetro y controles comunes de Registrar, Cuaderno, Ajustes, Ayuda y Salir, también en vista ampliada. Esfera transparente sobre la cámara con lectura central sólida. La brújula ofrece Plano (dirección de buzamiento, buzamiento y rumbo RHR), Línea (dirección descendente e inmersión) y Azimut (borde superior físico). El clinómetro mide inclinación del borde largo y porcentaje de pendiente. Usa la matriz nativa completa; los ejes de contacto son físicos y no cambian con el giro de la pantalla.

En brújula y clinómetro se puede desactivar la cámara de fondo en Ajustes. Con o sin cámara, Registrar guarda la lectura en el mismo cuaderno; la foto es opcional y se toma al registrar, no al retener. Su fecha queda separada de la fecha del sensor. Guardar ubicación permanece desactivado por defecto. Retener congela valores y referencia, muestra antigüedad y se libera al detener o cambiar modo/tipo/corrección.

Ayuda ES/PT/EN ampliada con colocación, buzamiento, inclinación por contacto y método A/B. La cara de contacto debe ser paralela a la pantalla y plana; la funda o cámaras salientes no deben inclinarla. El usuario comprueba el apoyo: no se detecta automáticamente. Para líneas y clinómetro se utiliza el borde largo desde la base al extremo original del auricular. No confundirlo con el eje óptico de la cámara.

Las direcciones geográficas se omiten sin norte magnético fiable, incluso en modo giroscópico relativo; la inclinación sigue disponible si hay gravedad válida. Planos casi horizontales (<0,5°) no tienen dirección presentada; casi verticales (>89,5°) muestran eje de rumbo sin lado único. Líneas casi horizontales muestran eje y casi verticales omiten dirección. Estos umbrales son de representación, no exactitud. La corrección manual sigue siendo un ajuste registrado, no una declinación consultada automáticamente ni calibración.

Pruebas nuevas de geometría: cuadrantes, rotación dentro del mismo plano, cara opuesta, inversión de lineación, casos singulares, datos inválidos y ausencia de norte. Flujo probado con y sin cámara, retención/liberación, foto opcional y privacidad. Revisión de interfaz con orientación simulada excluida del APK. Compilación y firma verificadas. No se ha ensayado esta versión en teléfono físico ni validado exactitud o resultados educativos.

Base técnica: matriz East/North/Up y ejes físicos Android, https://developer.android.com/develop/sensors-and-location/sensors/sensors_position. Los métodos geológicos y convenciones aplicados se documentan en logic/geo-core.cjs y sus pruebas.


## Interfaz 0.7.1

Distribución vertical con panel A/B compacto y lecturas abreviadas. En horizontal, mira/cámara a la izquierda y lecturas/controles en una columna lateral; las acciones A/B quedan bajo la mira. La esfera se adapta al área libre. Se elimina el acceso duplicado a ayuda en vista normal; en vista ampliada sigue disponible junto al selector de instrumentos. Se conservan controles comunes, lectura central roja, ayuda completa y datos de medición.

Verificación visual y de límites en navegador con datos simulados en 320×640, 360×800, 478×1024, 640×360, 844×390 y 1018×478, incluyendo vista ampliada y tres instrumentos. A 360×800 con A/B abierto, cámara de aproximadamente 434 px de alto y panel A/B de 99 px. A 844×390, acciones dentro de pantalla y panel A/B de 93 px. Son medidas de la revisión de interfaz, no exactitud instrumental. Falta confirmar esta edición en teléfono físico.
