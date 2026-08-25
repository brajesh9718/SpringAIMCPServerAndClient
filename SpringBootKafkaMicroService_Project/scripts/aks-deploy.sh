#!/usr/bin/env bash
set -euo pipefail
kubectl apply -k k8s/base
kubectl get pods -n springboot-kafka-ms
