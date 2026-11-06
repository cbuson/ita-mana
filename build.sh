#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
: "${VISADA_BUILD_TOOLS:?Set VISADA_BUILD_TOOLS to Android build-tools 35.0.0}"
: "${VISADA_ANDROID_JAR:?Set VISADA_ANDROID_JAR to platforms/android-35/android.jar}"
: "${VISADA_SIGNING_KEY:?Set VISADA_SIGNING_KEY to your private keystore, outside this source tree}"
: "${VISADA_SIGNING_PASSWORD_FILE:?Set VISADA_SIGNING_PASSWORD_FILE to a private password file}"
VISADA_SIGNING_ALIAS="${VISADA_SIGNING_ALIAS:-itamana-release}"
rm -rf out/classes out/dex
mkdir -p out/classes out/dex
python3 - <<'PY'
from pathlib import Path
import re,hashlib,base64
html=Path('assets/index.html').read_text()
def hashes(pattern):
 return ' '.join("'sha256-"+base64.b64encode(hashlib.sha256(s.encode()).digest()).decode()+"'" for s in re.findall(pattern,html,re.S))
scripts=hashes(r'<script[^>]*>(.*?)</script>');styles=hashes(r'<style[^>]*>(.*?)</style>')
assert scripts and styles
policy="default-src 'none'; script-src "+scripts+"; style-src "+styles+"; img-src data:; media-src blob:; connect-src 'none'; frame-src 'none'; child-src 'none'; object-src 'none'; worker-src 'none'; base-uri 'none'; form-action 'none'; frame-ancestors 'none'"
Path('assets/security-policy.txt').write_text(policy+'\n')
PY
"$VISADA_BUILD_TOOLS/aapt2" compile --dir res -o out/resources.zip
"$VISADA_BUILD_TOOLS/aapt2" link -o out/base.apk -I "$VISADA_ANDROID_JAR" --manifest AndroidManifest.xml --min-sdk-version 24 --target-sdk-version 35 -A assets out/resources.zip
java com.sun.tools.javac.Main -source 8 -target 8 -classpath "$VISADA_ANDROID_JAR" -d out/classes src/br/org/itaarandu/itamana/*.java
python3 - <<'PY'
from pathlib import Path
from zipfile import ZipFile
with ZipFile('out/classes.jar','w') as z:
 for p in Path('out/classes').rglob('*.class'):z.write(p,p.relative_to('out/classes'))
PY
java -cp "$VISADA_BUILD_TOOLS/lib/d8.jar" com.android.tools.r8.D8 --min-api 24 --lib "$VISADA_ANDROID_JAR" --output out/dex out/classes.jar
python3 - <<'PY'
from zipfile import ZipFile,ZIP_DEFLATED
from pathlib import Path
with ZipFile('out/base.apk','a',ZIP_DEFLATED) as z:
 for p in Path('out/dex').glob('*.dex'):z.write(p,p.name)
PY
"$VISADA_BUILD_TOOLS/zipalign" -f 4 out/base.apk out/aligned.apk
java -jar "$VISADA_BUILD_TOOLS/lib/apksigner.jar" sign --ks "$VISADA_SIGNING_KEY" --ks-pass "file:$VISADA_SIGNING_PASSWORD_FILE" --ks-key-alias "$VISADA_SIGNING_ALIAS" --out out/ITA_MANA_0_7_1.apk out/aligned.apk
java -jar "$VISADA_BUILD_TOOLS/lib/apksigner.jar" verify --verbose out/ITA_MANA_0_7_1.apk
