# Bước 1: Build stage (Sử dụng JDK để biên dịch)
FROM eclipse-temurin:11-jdk AS build
WORKDIR /app

# Cài đặt wget để tải driver MySQL
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# Tải MySQL JDBC Driver 8.0.33
RUN mkdir -p lib && \
    wget https://repo1.maven.org/maven2/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar -O lib/mysql-connector.jar

# Copy mã nguồn vào và biên dịch
COPY . .
RUN mkdir -p build && javac -d build -cp "lib/mysql-connector.jar" src/server/*.java src/client/*.java

# Bước 2: Run stage (Sử dụng JRE để chạy cho nhẹ)
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy kết quả biên dịch và driver từ stage build
COPY --from=build /app/build ./build
COPY --from=build /app/lib ./lib
COPY start.sh .

# Cấp quyền thực thi cho script khởi động
RUN chmod +x start.sh

# Railway sẽ sử dụng biến PORT mặc định
EXPOSE 8080

CMD ["./start.sh"]