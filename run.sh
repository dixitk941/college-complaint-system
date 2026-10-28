#!/usr/bin/env bash
# Starts MySQL if needed, then runs the complaint system at http://localhost:8080/ccms/
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

MYSQL_CONTAINER="ccms-mysql"
MYSQL_PORT=3306
APP_PORT=8080

if ! command -v java >/dev/null 2>&1; then
  echo "Java is not installed. Install JDK 8, 11, or 17."
  exit 1
fi

port_open() {
  lsof -nP -iTCP:"$1" -sTCP:LISTEN >/dev/null 2>&1
}

if port_open "$APP_PORT"; then
  echo "Port ${APP_PORT} is already in use. Open http://localhost:${APP_PORT}/ccms/"
  exit 1
fi

start_mysql_container() {
  if ! command -v docker >/dev/null 2>&1; then
    echo "MySQL is not running on port ${MYSQL_PORT}, and Docker is not installed."
    echo "Start MySQL with username root and password root, then run this script again."
    exit 1
  fi

  if ! docker info >/dev/null 2>&1; then
    if command -v colima >/dev/null 2>&1; then
      echo "Starting Colima..."
      colima start
    else
      echo "Docker is installed, but the Docker daemon is not running."
      exit 1
    fi
  fi

  if docker ps -a --format '{{.Names}}' | grep -qx "$MYSQL_CONTAINER"; then
    echo "Starting MySQL container ${MYSQL_CONTAINER}..."
    docker start "$MYSQL_CONTAINER" >/dev/null
  else
    echo "Creating MySQL container ${MYSQL_CONTAINER}..."
    docker run -d --name "$MYSQL_CONTAINER" \
      -e MYSQL_ROOT_PASSWORD=root \
      -e MYSQL_DATABASE=college_cms \
      -p "${MYSQL_PORT}:3306" \
      mysql:8.0 >/dev/null
  fi

  echo "Waiting for MySQL..."
  for _ in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30; do
    if docker exec "$MYSQL_CONTAINER" mysqladmin ping -uroot -proot --silent >/dev/null 2>&1; then
      echo "MySQL is ready."
      return
    fi
    sleep 2
  done

  echo "MySQL did not become ready. Check the log with: docker logs ${MYSQL_CONTAINER}"
  exit 1
}

if port_open "$MYSQL_PORT"; then
  echo "MySQL is already running on port ${MYSQL_PORT}."
else
  start_mysql_container
fi

if command -v mvn >/dev/null 2>&1; then
  MVN="$(command -v mvn)"
elif [[ -x /tmp/apache-maven-3.9.9/bin/mvn ]]; then
  MVN="/tmp/apache-maven-3.9.9/bin/mvn"
else
  echo "Maven was not found. Install Maven, then run this script again."
  exit 1
fi

echo "Starting the app at http://localhost:${APP_PORT}/ccms/"
echo "Administrator: admin / admin123"
echo "Student:       student / student123"
echo "Press Ctrl+C to stop the app."
exec "$MVN" -f "$ROOT/pom.xml" jetty:run
