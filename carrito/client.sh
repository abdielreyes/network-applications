#! /bin/bash

echo "Compiling files..."
javac -d "classes" Product.java
javac -d "classes" ShoppingCart.java
javac -d "classes" Server.java
javac -cp ".:./libs/itextpdf-5.5.9.jar" -d "classes" Client.java
echo "Running client..."
java  -cp ".:./libs/itextpdf-5.5.9.jar" -d "classes" Client
