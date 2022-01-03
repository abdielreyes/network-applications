#! /bin/bash

echo "Compiling files.."
javac -d "classes" Database.java
javac -d "classes" Product.java
javac -d "classes" Server.java
echo "Running server..."
java -d "classes" Server
