# Revisión de protección · ITA MAÑA 0.6.1

Paquete br.org.itaarandu.itamana, versionCode 11, Android API 24+ y target API 35. Revisión local de código y pruebas, no auditoría independiente ni certificación.

Permisos: cámara y ubicación durante uso; micrófono opcional solo al solicitar grabación; sin Internet, contactos ni ubicación de fondo. Sin cuentas, anuncios o analítica. La interfaz sirve solo el documento local exacto y bloquea navegación/recursos remotos. Canal WebMessagePort para origen y ventana principal, métodos limitados, sin addJavascriptInterface. CSP con hashes, cookies y depuración desactivadas, errores TLS cancelados. Las notas se tratan como texto. Exportación revisada, nombres/tipos/tamaños limitados y selector Android. No hay envío automático.

Cuaderno privado, coordenadas excluidas de nuevos registros hasta activar su guardado. Sin cifrado propio adicional. Copias de seguridad declaradas desactivadas. La matriz de orientación se guarda con lecturas geológicas para documentar ejes y método. Fotos opcionales tomadas al registrar; fecha de foto separada del instante del sensor. No se requiere cámara para datos de contacto.

Mismos controles y cuaderno para tres instrumentos. Cámara/sensores se detienen al pausar/salir. Al detener, cambiar instrumento/tipo o corrección se invalida retención; el modo A/B se reinicia. Sin norte fiable no se muestran direcciones geográficas geológicas. Singularidades cerca de horizontal/vertical se identifican sin presentar umbrales como precisión.

APK SHA-256: `3d57edc7780b6af98c0ceed8d114b8fbeb51c9f07723340d06b9ea97721ccb7f`.
Certificado SHA-256: `e988afcde1d9f3206dc737bfd727d1c41b86036753ac17181a32048d307f8c05`.
Firma v2/v3 verificada. Claves y contraseñas fuera de fuente/APK. Compatible por firma/paquete con 0.4–0.5.3.

Pruebas automáticas pasadas: geometría de cámara, planos y líneas; invariancia de plano frente a giro dentro de él; caras opuestas; inversión de líneas; cuadrantes, singularidades y falta de norte; registro sin cámara, retención, privacidad, interfaz y canal nativo. Revisión visual con matrices simuladas excluidas del APK. No se ha probado esta versión en teléfono físico/emulador, ni realizado calibración, ensayo de penetración o evaluación educativa.

Android, WebView y estado del dispositivo siguen siendo dependencias. Un teléfono comprometido puede exponer datos; se permiten capturas. La app no controla exportaciones y sus proveedores. MIT permite otros usos y modificaciones; no garantiza su comportamiento ni riesgo cero.


Interfaz 0.6.1: distribución por áreas de pantalla, panel A/B compacto y columna lateral horizontal. Sin cambios en los módulos geométricos. Comprobadas vista normal/ampliada y apertura/cierre de ayuda; datos de orientación simulados solo en la revisión, excluidos del APK. Pruebas automáticas de regresión pasadas; falta ensayo en teléfono físico de esta edición.
