#!/usr/bin/env bash
set -euo pipefail
docker exec kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --if-not-exists --topic order.created.v1 --partitions 3 --replication-factor 1
