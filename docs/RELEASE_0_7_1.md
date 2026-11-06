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
