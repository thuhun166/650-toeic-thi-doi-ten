#!/bin/sh
set -eu

PORT="${PORT:-8080}"
NODE_ID="${NODE_ID:-1}"
BIND_HOST="${BIND_HOST:-0.0.0.0}"

echo "------------------------------------------"
echo "Khoi dong may chu in phan tan ${NODE_ID}"
echo "Dia chi bind: ${BIND_HOST}"
echo "Cong: ${PORT}"
echo "------------------------------------------"

exec java ${JAVA_OPTS:-} -cp "build:lib/*" server.Main
