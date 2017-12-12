#!/usr/bin/env bash
function mytest {
    "$@"
    local status=$?
    if [ $status -ne 0 ]; then
        echo "error with $1" >&2
    fi
    return $status
}

var newmantest =  node_modules/.bin/newman run uniTalq.postman_collection.json -e heroku.postman_environment.json
