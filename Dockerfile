# Bước 1: Build stage
FROM openjdk:11-jdk-slim AS build
WORKDIR /app

# Cài đặt wget để tải driver
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# Tải MySQL Driver ngay lúc build
RUN mkdir -p lib && \
    wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar -O lib/mysql-connector.jar

# Copy mã nguồn và biên dịch
COPY src ./src
RUN mkdir -p build && javac -d build -cp "lib/mysql-connector.jar" src/server/*.java src/client/*.java

# Bước 2: Run stage
FROM openjdk:11-jre-slim
WORKDIR /app

# Copy kết quả từ stage build sang
COPY --from=build /app/build ./build
COPY --from=build /app/lib ./lib

# Script khởi động (không cần tải driver nữa)
COPY start.sh .
RUN chmod +x start.sh

CMD ["./start.sh"]