# Temel görüntüyü kullan
FROM openjdk:21

# Uygulama JAR dosyasının adını belirt
ARG JAR_FILE=target/lt-project-0.0.1.jar

# JAR dosyasını konteyner içindeki app klasörüne kopyala
COPY ${JAR_FILE} app.jar

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "/app.jar"]
