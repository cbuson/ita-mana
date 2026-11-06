#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
for test in ui-check privacy-check bootstrap-check tripod-check tripod-flow-check geo-check geo-flow-check notebook-check; do
  node "tests/$test.cjs"
done
mkdir -p test-out
java com.sun.tools.javac.Main -d test-out src/br/org/itaarandu/itamana/LevelMath.java src/br/org/itaarandu/itamana/ProjectLinks.java src/br/org/itaarandu/itamana/AppPolicy.java tests/*Check.java
for test in LevelMathCheck ProjectLinksCheck AppPolicyCheck; do
  java -cp test-out "$test"
done
