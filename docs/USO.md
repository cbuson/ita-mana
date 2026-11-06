# 0.7.1 — Escritura corregida y notas multimedia

Corrige el error Cannot set properties of null (setting value): el formulario intentaba localizar por id campos creados dentro de un contenedor opcional aún no añadido al documento. La sección opcional ahora se conecta antes de inicializar los campos. Las pruebas anteriores con nodos simulados no detectaban este error; esta corrección también se comprobó en DOM real mediante escritura, guardado y reapertura.

## Escribir en campo

Cuaderno → Añadir nota. Escribe en el área principal y pulsa Guardar. No exige estación ni sensores. Puedes elegir estación antes o asignarla después con Anotar / asignar. Detalles de observación opcionales, plegados. Botón Guardar fijo dentro del editor, texto de 17 px, hasta 6000 caracteres por campo de texto largo. Se conservan registros existentes y revisiones. Exportaciones en una sección secundaria para dejar a mano escribir y registrar.

## Audio y vídeo

Dentro de la nota, Grabar audio o Grabar vídeo → Detener grabación → revisar el clip → Guardar. Un clip de hasta 60 s y aproximadamente 6 MB por nota; crea otra nota para otro clip. El vídeo tiene previsualización y va sin sonido por defecto; activa Incluir sonido si lo necesitas. Texto y clip se guardan juntos. La grabación detenida sigue siendo borrador hasta Guardar. Si el formato no está soportado o se deniega el permiso, el texto sigue disponible. WebM o MP4 según soporte real del WebView. La compatibilidad de reproducción depende del dispositivo.

Permiso de micrófono únicamente al elegir grabar audio o vídeo con sonido. No se añade a los permisos pedidos al arrancar. Se detiene la captura al cerrar la ficha, salir de primer plano o llegar al límite. No graba en segundo plano. Sin dictado automático, transcripción, cuentas ni envío a servidores. Actualizada la documentación de privacidad.

## Copias

Para una copia con clips, marca Incluir ubicaciones, fotos, audio y vídeo. JSON conserva clips embebidos; ZIP incluye también ficheros multimedia separados. PDF, CSV y GeoJSON conservan descripción y metadatos, sin bytes multimedia. Desmarcado se omiten ubicaciones y medios, también en revisiones. Tamaño máximo del JSON importado/exportado: 12 MB; los clips y el historial pueden hacer que un cuaderno supere ese tamaño. Divide las exportaciones por estación cuando corresponda.

## Comprobaciones y límites

- DOM real: abrir Añadir nota, escribir texto, añadir método opcional, guardar y reabrir recuperando el texto. Pantallas de 360×800 y 640×360.
- Pruebas automatizadas: regresión de los tres instrumentos, estaciones, muestras, revisiones, copia/importación y privacidad. Grabador simulado: inicio, bloqueo de Guardar mientras graba, parada, clip adjunto a nota sin texto, liberación de pistas y denegación del micrófono. Validación de tipo, tamaño y cabecera de adjuntos.
- APK compilado y firma verificada. No se probaron el micrófono ni la cámara de un Android físico; grabación, reproducción y exportación multimedia necesitan esa comprobación. No se presenta como validación de campo.

# 0.7.0 — Cuaderno por estaciones y afloramientos

## Uso

1. Cuaderno → Nueva estación. Solo código y nombre obligatorios.
2. Despliega los datos opcionales para salida/proyecto, observador, litología, textura, estructuras, alteración, interpretación, dudas y acceso. GPS reciente o coordenadas WGS84 manuales, opcionales y con fuente identificada.
3. Selecciona la estación → Medir / foto. Registrar en cualquiera de los tres instrumentos vincula la medida. Notas y muestras pueden registrarse sin activar sensores.
4. Anotar / asignar en una medida permite añadir elemento medido, método, condiciones, incertidumbre, grupo de repeticiones, pie de foto, escala y código de muestra. El motivo de modificación es obligatorio y se conserva la versión anterior. Los ángulos originales no se editan.
5. Los registros previos siguen disponibles en Registros sin estación y se pueden asignar. No se modifica la versión de IndexedDB ni se borra la base existente.

## Copias y exportación

La exportación usa la selección actual: todas las estaciones, una estación o registros sin estación.

- JSON: copia reimportable con revisiones y metadatos.
- ZIP: notebook.json y JPEG separados por número de registro. Importa el JSON después de extraer el ZIP. Las fotos capturadas se conservan tal como fueron registradas por la aplicación; no se presentan como originales sin tratamiento ni con EXIF garantizado.
- CSV: tabla con detalles JSON por fila; mantiene números negativos y protege textos contra fórmulas de hoja de cálculo.
- GeoJSON: puntos WGS84 solo cuando hay coordenadas explícitas. Nunca hereda la ubicación de la estación como si fuera una lectura GPS de la medida.
- PDF: ficha textual con paginación, sin imágenes. Para las fotografías usa ZIP o JPEG.

Marca incluir ubicaciones y fotografías para una copia completa. Desmarcado, elimina ambas también del historial y las estructuras anidadas, pues las imágenes pueden tener coordenadas impresas. La casilla no borra nada del dispositivo.

Importación: JSON de ITA MAÑA, validación de tamaño/estructura/fechas/fotos, vista previa y confirmación. Añade copias con identificadores nuevos; remapea enlaces de estaciones y referencias y no sobrescribe. Importar dos veces crea dos conjuntos de copias. Límite de archivo 12 MB; PDF textual hasta 500.000 caracteres. Para cuadernos grandes, exporta por estación. Datos locales, sin nuevos permisos ni acceso de red. Sin almacenamiento disponible, los registros solo duran la sesión y se avisa.

## Alcance

Salidas/proyectos se agrupan mediante un campo en cada estación, sin una entidad independiente. Grupos de repetición se identifican por texto: no calcula promedios de orientaciones. No añade grabación de audio. La precisión de los instrumentos continúa sin validación física.

## Validación

Pruebas automatizadas: creación de estación, medidas vinculadas, muestras, revisiones conservadas, privacidad, GeoJSON, importación sin sobrescribir y enlaces remapeados, CSV y regresión de los tres instrumentos. Revisión visual en pantallas vertical y horizontal con creación y guardado de estación y nota. Compilación y firma APK verificadas. No se ejecutó el selector de archivos ni las exportaciones nativas en un teléfono Android en esta sesión.

# 0.6.2 — Referencias de norte

- Escala giratoria orientada respecto al borde superior de pantalla, en las cuatro orientaciones.
- Flechas NM (roja) y NG (azul), separadas de la dirección medida.
- Ambos azimuts del borde superior de pantalla y declinación estimada visibles en Brújula.
- Corrección geográfica local mediante Android GeomagneticField, sin Internet. No almacena coordenadas en registros salvo opción explícita.
- Sin posición reciente (120 s) o sin norte fiable, no presenta un norte geográfico calculado. Cerca de la vertical se ocultan referencias indeterminadas. Retener congela también la corrección y la orientación.
- El modelo magnético depende del sistema Android y puede estar desactualizado. Lecturas educativas, sin validación de precisión de campo.

Pruebas: controles ES/EN/PT, geometría, cuatro rotaciones, corrección positiva/negativa y paso por 0°, retención, falta de ubicación, privacidad y firma APK. Revisión visual con sensores simulados en vertical y horizontal. No se probó un sensor físico.

# Guía de uso

## Activar

Pulsa Activar instrumento y permite cámara y, si la necesitas, ubicación durante el uso. Si se deniegan, revisa los permisos en Android. No requiere cuenta. El vídeo superpuesto de otra aplicación no forma parte de ITA MAÑA y puede tapar controles.

Arriba se ven inclinación vertical y nivel lateral. Abajo se añade el azimut. Solo la lectura central inferior es roja y en negrita: énfasis, no alarma. Un guion indica dato ausente o antiguo. La cuadrícula no tiene escala angular.

## Referencia y comparación A → B

1. Sujeta el teléfono al trípode, nivela físicamente su base y conserva su posición. La burbuja solo indica nivel lateral del teléfono, no verticalidad del eje.
2. Abre A → B y apunta con el centro de la mira al primer objetivo. **1 · Fijar A / 0°** guarda foto y orientación inicial. No es necesario que la visual sea horizontal.
3. Gira la cabeza hacia B sin mover la base. ΔH muestra giro horizontal respecto de A; ΔV muestra el cambio vertical.
4. **2 · Registrar B** guarda foto, ángulos y diferencias vinculados a A.
5. Vuelve manualmente al mismo A y pulsa **3 · Retorno A**. El residuo muestra el desacuerdo observado; no reconoce objetivos ni certifica precisión.

Para registrar se necesita cámara lista, orientación reciente/válida, nivel lateral dentro de ±1° y calidad magnética no marcada como no fiable. El panel explica el bloqueo. La estabilidad es orientativa y no impide registrar.

ΔH es el arco corto con signo [-180°,180°); de 359° a 1° da +2°. El caso 180° se presenta como −180°. ΔV = vertical actual − vertical inicial. No acumula vueltas ni calcula distancias. A fija una referencia angular, no el objeto en los píxeles de la pantalla.

Restablecer conserva los registros. Detener, pasar a segundo plano, cambiar sensor o corrección manual invalida A: fija otra referencia al volver.

## Sensores

Ajustes ofrece magnético o relativo si existe TYPE_GAME_ROTATION_VECTOR. Magnético indica norte magnético sin declinación automática; relativo no indica norte y puede derivar. Evita imanes, fundas magnéticas, vehículos y estructuras metálicas. La calidad Android es cualitativa, no un error angular.

Verde significa nivel lateral dentro de ±1°, no precisión de ±1°. Estable requiere ≥8 muestras, ventana ≥1,2 s, rangos horizontal/vertical ≤0,35° y lateral ≤0,5°, con muestra reciente ≤500 ms. Son criterios heurísticos, no incertidumbre instrumental validada.

## Cuaderno, exportación y salida

Registrar guarda foto con banda de datos y nota. Guardar ubicación empieza desactivado en Ajustes; activarlo incluye coordenadas en nuevos registros. Ver GPS en vivo no implica guardarlo. En Cuaderno puedes exportar JSON y fotos JPEG. JSON incluye fotos y metadatos; si supera el límite de exportación, guarda fotos por separado. No escribe EXIF ni incluye importación o sincronización.

El selector de archivos pausa la medición: vuelve a activarla al regresar. Salir detiene cámara/sensores y cierra la actividad; conserva el cuaderno persistente. Exporta antes de desinstalar o borrar datos. Si solo hay memoria temporal, se avisa antes de salir.

## Práctica con docente

Repite A/B/retorno con objetivos fijos. Compara con un instrumento de referencia y anota dispositivo, condiciones y discrepancias. El ejercicio permite practicar lectura y registro; no certifica competencias ni calibra el aparato por sí solo. Complementa la formación con práctica supervisada con equipamiento real.


## Instrumentos integrados en 0.6.0

Selector Teodolito / Brújula / Clinómetro y controles comunes de Registrar, Cuaderno, Ajustes, Ayuda y Salir, también en vista ampliada. Esfera transparente sobre la cámara con lectura central sólida. La brújula ofrece Plano (dirección de buzamiento, buzamiento y rumbo RHR), Línea (dirección descendente e inmersión) y Azimut (borde superior físico). El clinómetro mide inclinación del borde largo y porcentaje de pendiente. Usa la matriz nativa completa; los ejes de contacto son físicos y no cambian con el giro de la pantalla.

En brújula y clinómetro se puede desactivar la cámara de fondo en Ajustes. Con o sin cámara, Registrar guarda la lectura en el mismo cuaderno; la foto es opcional y se toma al registrar, no al retener. Su fecha queda separada de la fecha del sensor. Guardar ubicación permanece desactivado por defecto. Retener congela valores y referencia, muestra antigüedad y se libera al detener o cambiar modo/tipo/corrección.

Ayuda ES/PT/EN ampliada con colocación, buzamiento, inclinación por contacto y método A/B. La cara de contacto debe ser paralela a la pantalla y plana; la funda o cámaras salientes no deben inclinarla. El usuario comprueba el apoyo: no se detecta automáticamente. Para líneas y clinómetro se utiliza el borde largo desde la base al extremo original del auricular. No confundirlo con el eje óptico de la cámara.

Las direcciones geográficas se omiten sin norte magnético fiable, incluso en modo giroscópico relativo; la inclinación sigue disponible si hay gravedad válida. Planos casi horizontales (<0,5°) no tienen dirección presentada; casi verticales (>89,5°) muestran eje de rumbo sin lado único. Líneas casi horizontales muestran eje y casi verticales omiten dirección. Estos umbrales son de representación, no exactitud. La corrección manual sigue siendo un ajuste registrado, no una declinación consultada automáticamente ni calibración.

Pruebas nuevas de geometría: cuadrantes, rotación dentro del mismo plano, cara opuesta, inversión de lineación, casos singulares, datos inválidos y ausencia de norte. Flujo probado con y sin cámara, retención/liberación, foto opcional y privacidad. Revisión de interfaz con orientación simulada excluida del APK. Compilación y firma verificadas. No se ha ensayado esta versión en teléfono físico ni validado exactitud o resultados educativos.

Base técnica: matriz East/North/Up y ejes físicos Android, https://developer.android.com/develop/sensors-and-location/sensors/sensors_position. Los métodos geológicos y convenciones aplicados se documentan en logic/geo-core.cjs y sus pruebas.


## Interfaz 0.6.1

Distribución vertical con panel A/B compacto y lecturas abreviadas. En horizontal, mira/cámara a la izquierda y lecturas/controles en una columna lateral; las acciones A/B quedan bajo la mira. La esfera se adapta al área libre. Se elimina el acceso duplicado a ayuda en vista normal; en vista ampliada sigue disponible junto al selector de instrumentos. Se conservan controles comunes, lectura central roja, ayuda completa y datos de medición.

Verificación visual y de límites en navegador con datos simulados en 320×640, 360×800, 478×1024, 640×360, 844×390 y 1018×478, incluyendo vista ampliada y tres instrumentos. A 360×800 con A/B abierto, cámara de aproximadamente 434 px de alto y panel A/B de 99 px. A 844×390, acciones dentro de pantalla y panel A/B de 93 px. Son medidas de la revisión de interfaz, no exactitud instrumental. Falta confirmar esta edición en teléfono físico.
