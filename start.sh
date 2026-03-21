#!/bin/bash
find src -name "*.java" -exec javac -d build {} +
java -cp build server.Main