#!/bin/bash
set -e

# Build do Frontend (React + Vite)
echo "🔨 Building Frontend..."
npm install
npm run build

# Build do Backend (Spring Boot)
echo "🔨 Building Backend..."
./mvnw clean package -DskipTests

echo "✅ Build completo!"

