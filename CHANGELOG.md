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

# 0.6.4 — Clinómetro y niveles X/Y

- Línea móvil azul y referencia horizontal ámbar de 0°, recuadro de lectura reducido.
- Dos burbujas con valores angulares: X transversal e Y longitudinal respecto a los ejes físicos del teléfono. Verde dentro de ±1°; ámbar fuera.
- Las burbujas usan la gravedad, sin necesitar norte. Retener congela ambas. La posición de burbuja sigue el seno del ángulo; los grados se leen en cifras.
- El clinómetro mide el borde largo, no la dirección óptica de la cámara. +85° apunta casi vertical arriba; no se cambia el signo para compensar la vista de cámara.
- Ayuda ES/EN/PT explica los ejes y el uso de los niveles: en una pendiente Y no tiene que quedar en cero. Para centrar ambos, pantalla aproximadamente horizontal.

Validación: comprobaciones automatizadas de geometría, niveles X/Y, retención, interfaz y privacidad. Revisión visual con datos simulados en vertical y horizontal. Precisión física sin validar.

# 0.6.3

Elimina el panel central en los tres modos de brújula para dejar visibles las agujas y el fondo de cámara. Las lecturas permanecen en la zona inferior. Conserva el indicador central del clinómetro.

# 0.6.2 — Referencias de norte

- Escala giratoria orientada respecto al borde superior de pantalla, en las cuatro orientaciones.
- Flechas NM (roja) y NG (azul), separadas de la dirección medida.
- Ambos azimuts del borde superior de pantalla y declinación estimada visibles en Brújula.
- Corrección geográfica local mediante Android GeomagneticField, sin Internet. No almacena coordenadas en registros salvo opción explícita.
- Sin posición reciente (120 s) o sin norte fiable, no presenta un norte geográfico calculado. Cerca de la vertical se ocultan referencias indeterminadas. Retener congela también la corrección y la orientación.
- El modelo magnético depende del sistema Android y puede estar desactualizado. Lecturas educativas, sin validación de precisión de campo.

Pruebas: controles ES/EN/PT, geometría, cuatro rotaciones, corrección positiva/negativa y paso por 0°, retención, falta de ubicación, privacidad y firma APK. Revisión visual con sensores simulados en vertical y horizontal. No se probó un sensor físico.

# Cambios

## 0.6.1 · Optimización de interfaz
Panel A/B compacto; distribución horizontal con columna lateral para lecturas y controles; esfera adaptable al espacio libre; instrucciones breves y menos repeticiones; ayuda disponible en vista normal y ampliada.


## 0.6.0 · Instrumentos integrados
Teodolito, brújula geológica y clinómetro con controles/cuaderno comunes. Esfera transparente, matriz nativa completa, plano/línea/azimut, inclinación/porcentaje, retención, registro sin cámara y ayuda ES/PT/EN. Pruebas nuevas de geometría y flujo.


## 0.5.3
Ayuda ES/PT/EN: competencias técnicas y práctica supervisada con equipamiento real.

## 0.5.2
Aviso educativo destacado y límites de precisión y uso.

## 0.5.1
Estabilidad orientativa sin bloquear registro. Instrucciones numeradas, próxima acción destacada y nombres explicativos de ΔH/ΔV.

## 0.5.0
Referencia A/B, diferencias con signo, fotos vinculadas, retorno, burbuja, estabilidad y sensor relativo compatible. Lecturas arriba y solo la central inferior roja/negrita.

## 0.4.1
Tarjetas y botones alineados, tamaños e iconos uniformes.

## 0.4
Paquete br.org.itaarandu.itamana y firma privada de distribución. Protección del contenido local, privacidad y salida. Los prototipos anteriores tienen paquete y cuaderno independientes.
