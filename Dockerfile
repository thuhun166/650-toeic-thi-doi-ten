# Multi-stage build for Token Ring distributed system
FROM eclipse-temurin:11-jdk as builder

WORKDIR /app

# Copy source code
COPY src/ src/
COPY build/ build/

# Compile Java files
RUN find src -name "*.java" -type f | xargs javac -cp build -d build

# Runtime stage
FROM eclipse-temurin:11-jre

WORKDIR /app

# Copy compiled classes
COPY --from=builder /app/build build/
COPY --from=builder /app/src src/

# Expose port
EXPOSE 8080

# Set default port
ENV PORT=8080

# Run server
CMD ["java", "-cp", "build", "server.Main"]
