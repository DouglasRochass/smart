# Stage 1: Build do Frontend
FROM node:20-alpine AS frontend-builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Build do Backend
FROM maven:3.9-eclipse-temurin-22 AS backend-builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:22-jre
WORKDIR /app

# Copiar o JAR do backend
COPY --from=backend-builder /app/target/*.jar app.jar

# Copiar os arquivos estáticos do frontend (se necessário)
# COPY --from=frontend-builder /app/dist ./src/main/resources/static

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

