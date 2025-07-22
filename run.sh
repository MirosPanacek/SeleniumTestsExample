#!/bin/bash
docker compose up -d
echo "🔄 Waiting, for server na http://127.0.0.1:8089  (max. 5 min)..."

MAX_WAIT=300    # Max waiting time in sec. (5 min.)
WAIT_INTERVAL=5 # loop 5 sec
WAITED=0        # Initial time value

while [ "$WAITED" -lt "$MAX_WAIT" ]; do
  HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:8089 -I)

  if [ "$HTTP_CODE" -eq 302 ]; then
    echo "✅ Server Is running (HTTP 302). Testy going to run..."

    # Start tests
    mvn clean test
    docker compose down
    exit 0
  else
    echo "⏳ Server does not run (HTTP $HTTP_CODE). Waiting for $WAITED sec..."
    sleep "$WAIT_INTERVAL"
    WAITED=$((WAITED + WAIT_INTERVAL))
  fi
done

echo "❌ Timeout: Server does not work for 5 min."
exit 1