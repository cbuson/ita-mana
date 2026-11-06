# Compilar y probar

Proyecto Java y HTML/CSS/JavaScript local con herramientas de línea de comandos. No contiene Gradle ni wrapper. No basta con abrirlo como proyecto Gradle en Android Studio.

## Requisitos

Linux/entorno Bash, Python 3, JDK con compilador (verificado con OpenJDK 17), Android SDK Platform 35 (`android.jar`) y Build Tools 35.0.0 (`aapt2`, `zipalign`, `lib/d8.jar`, `lib/apksigner.jar`). Node.js para pruebas JavaScript. No se incluyen estas herramientas externas.

Desde la raíz, ajusta estas rutas al entorno real:

```bash
export VISADA_BUILD_TOOLS="$HOME/Android/Sdk/build-tools/35.0.0"
export VISADA_ANDROID_JAR="$HOME/Android/Sdk/platforms/android-35/android.jar"
export VISADA_SIGNING_KEY="$HOME/itamana-signing/itamana-release.jks"
export VISADA_SIGNING_PASSWORD_FILE="$HOME/itamana-signing/password.txt"
export VISADA_SIGNING_ALIAS="itamana-release"
bash build.sh
```

La clave y el archivo de contraseña deben permanecer fuera del repositorio. El script lee la contraseña desde archivo. El resultado es `out/ITA_MANA_0_7_1.apk`. Regenera hashes CSP, compila recursos y Java, genera DEX, alinea, firma y verifica. No se garantiza identidad byte a byte entre compilaciones en distintos entornos.

El APK incluido usa la firma de distribución del autor. Otra clave no permite actualizar esa instalación. Los colaboradores pueden probar y compilar con su firma propia; no necesitan la privada del autor. La continuidad de las actualizaciones depende de conservar la firma.

## Pruebas

```bash
bash scripts/check.sh
```

Comprueban geometría, cálculo circular, estabilidad/antigüedad, aislamiento de sesiones, captura A/B/retorno con fotos simuladas, privacidad, interfaz y canal nativo. No verifican el funcionamiento real de Android.

En teléfono queda comprobar cámara, permisos, GPS, giro, pausa/reanudación, salida, exportación, actualización y A/B/retorno. Contrastar con un instrumento de referencia antes de atribuir exactitud.

## Google Play

Este paquete contiene APK, no AAB ni entrega lista para Google Play. La publicación futura requiere preparar y verificar por separado paquete, ficha, privacidad, pruebas y requisitos de la plataforma aplicables entonces.


## Instrumentos integrados en 0.7.1

Selector Teodolito / Brújula / Clinómetro y controles comunes de Registrar, Cuaderno, Ajustes, Ayuda y Salir, también en vista ampliada. Esfera transparente sobre la cámara con lectura central sólida. La brújula ofrece Plano (dirección de buzamiento, buzamiento y rumbo RHR), Línea (dirección descendente e inmersión) y Azimut (borde superior físico). El clinómetro mide inclinación del borde largo y porcentaje de pendiente. Usa la matriz nativa completa; los ejes de contacto son físicos y no cambian con el giro de la pantalla.

En brújula y clinómetro se puede desactivar la cámara de fondo en Ajustes. Con o sin cámara, Registrar guarda la lectura en el mismo cuaderno; la foto es opcional y se toma al registrar, no al retener. Su fecha queda separada de la fecha del sensor. Guardar ubicación permanece desactivado por defecto. Retener congela valores y referencia, muestra antigüedad y se libera al detener o cambiar modo/tipo/corrección.

Ayuda ES/PT/EN ampliada con colocación, buzamiento, inclinación por contacto y método A/B. La cara de contacto debe ser paralela a la pantalla y plana; la funda o cámaras salientes no deben inclinarla. El usuario comprueba el apoyo: no se detecta automáticamente. Para líneas y clinómetro se utiliza el borde largo desde la base al extremo original del auricular. No confundirlo con el eje óptico de la cámara.

Las direcciones geográficas se omiten sin norte magnético fiable, incluso en modo giroscópico relativo; la inclinación sigue disponible si hay gravedad válida. Planos casi horizontales (<0,5°) no tienen dirección presentada; casi verticales (>89,5°) muestran eje de rumbo sin lado único. Líneas casi horizontales muestran eje y casi verticales omiten dirección. Estos umbrales son de representación, no exactitud. La corrección manual sigue siendo un ajuste registrado, no una declinación consultada automáticamente ni calibración.

Pruebas nuevas de geometría: cuadrantes, rotación dentro del mismo plano, cara opuesta, inversión de lineación, casos singulares, datos inválidos y ausencia de norte. Flujo probado con y sin cámara, retención/liberación, foto opcional y privacidad. Revisión de interfaz con orientación simulada excluida del APK. Compilación y firma verificadas. No se ha ensayado esta versión en teléfono físico ni validado exactitud o resultados educativos.

Base técnica: matriz East/North/Up y ejes físicos Android, https://developer.android.com/develop/sensors-and-location/sensors/sensors_position. Los métodos geológicos y convenciones aplicados se documentan en logic/geo-core.cjs y sus pruebas.
