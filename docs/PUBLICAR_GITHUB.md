# Publicar en GitHub

Este paquete no crea ni publica el repositorio por sí mismo.

## Publicación desde la web

1. Crea un repositorio llamado `ita-mana`, público si quieres compartirlo. Descripción sugerida: “Herramienta educativa Android para practicar orientación, nivelación y comparación angular · ES/PT/EN · MIT”.
2. No generes otro README ni licencia: ya se incluyen.
3. Descomprime el ZIP y entra en la carpeta `ita-mana`. Sube su contenido a la raíz del repositorio: README.md junto a src/, assets/ y downloads/. No subas solo el ZIP.
4. Comprueba que `.gitignore` también se haya incluido; algunos exploradores ocultan archivos que empiezan por punto. Si no aparece, añádelo por separado o utiliza Git.
5. Confirma la subida y comprueba el enlace del README al APK.
6. Crea una publicación de versión con etiqueta `v0.7.1` y título “ITA MAÑA 0.7.1 · Prototipo educativo”. Copia el texto de docs/RELEASE_0_6.md. Adjunta el APK y SHA256SUMS.txt de downloads/. Márcala como preliminar porque sigue en pruebas.

No necesitas GitHub Pages. El código y APK se descargan desde el repositorio o las versiones. El HTML depende del contenedor Android para funciones nativas.

## Alternativa con Git

Desde la carpeta descomprimida:

```bash
git init
git add .
git status --short
git commit -m "Publicar ITA MAÑA 0.7.1, prototipo educativo"
git branch -M main
```

Copia desde tu repositorio vacío su URL real y úsala con `git remote add origin`. Tras configurar tu autenticación, ejecuta `git push -u origin main`. No se incluye una URL supuesta para evitar subir a otro destino.

## Clave y actualizaciones

No publiques el respaldo privado de firma, ni siquiera como archivo de una versión. Este ZIP no contiene claves ni contraseñas. Para actualizar: revisa código, incrementa versiones, compila/firma con la misma clave, prueba físicamente, actualiza APK/documentación/huella y publica otra etiqueta. Mantén anteriores versiones en Releases.

## Después: Google Play

Falta preparar y verificar AAB, ficha, privacidad, capturas propias y requisitos vigentes. No anunciarlo como instrumento certificado. Contrasta lecturas y ensaya con estudiantes antes de atribuir precisión o resultados educativos.
