# Bước 1: Build stage
FROM eclipse-temurin:11-jdk AS build
WORKDIR /app

# Cài đặt wget (mặc dù một số bản slim đã có sẵn nhưng cứ để cho chắc)
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# Tải MySQL JDBC Driver 8.0.33 - ĐÃ SỬA URL CHÍNH XÁC
RUN mkdir -p lib && \
    wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar -O lib/mysql-connector.jar

# Copy code và biên dịch
COPY . .
RUN mkdir -p build && javac -d build -cp "lib/mysql-connector.jar" src/server/*.java src/client/*.java

# Bước 2: Run stage
FROM eclipse-temurin:11-jre
WORKDIR /app

COPY --from=build /app/build ./build
COPY --from=build /app/lib ./lib
COPY start.sh .

RUN chmod +x start.sh
EXPOSE 8080

CMD ["./start.sh"]