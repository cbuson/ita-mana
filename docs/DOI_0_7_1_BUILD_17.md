# Incorporación del DOI — 0.7.1, compilación 17

Añade en Ayuda la referencia del software y el enlace https://doi.org/10.5281/zenodo.24012745, en castellano, portugués e inglés. El enlace abre el navegador externo mediante un destino fijo autorizado. Añade CITATION.cff y referencia en README.

La versión visible continúa siendo 0.7.1. El código interno Android pasa de 16 a 17 y el APK se firma con el mismo certificado. Cambia su huella SHA256. El DOI corresponde a la publicación ya archivada, anterior a esta incorporación de metadatos.

## Sustituir en GitHub Desktop

1. Extrae este ZIP y copia el contenido de su carpeta ITA_MANA_0_7_1 dentro del repositorio local ita-mana. Acepta sustituir archivos. Conserva la carpeta .git existente.
2. En GitHub Desktop revisa los cambios y crea un commit: `Añadir DOI en Ayuda y cita de ITA MAÑA 0.7.1`.
3. Pulsa Push origin.

El nuevo APK queda en downloads/ITA_MANA_0_7_1.apk. No recrees ni muevas la etiqueta v0.7.1 ya archivada en Zenodo; esta actualización de main no modifica aquel archivo. No necesitas crear otra release para sustituir estos archivos de main.

Pruebas: regresión automatizada de interfaz/cuaderno/instrumentos y enlaces permitidos, compilación y verificación de firma APK. No se ha probado esta compilación en un Android físico.
