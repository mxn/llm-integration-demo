#!/usr/bin/env bash

gcloud app deploy build/libs/demo-0.0.1-SNAPSHOT.jar \
 --appyaml gcp_appengine/app.yaml --project 'llm-integration-demo'