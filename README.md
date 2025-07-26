# 🚀 LT Spring Boot Backend Application

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.2-green.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.3-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Container-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 🎯 Proje Amacı

**LT (Log Tracking) Backend Application**, sunucu güvenliği ve log yönetimi için geliştirilmiş kapsamlı bir backend sistemidir. Bu uygulama, sistem yöneticilerinin ve güvenlik uzmanlarının sunucu loglarını izlemesine, şüpheli IP adreslerini tespit etmesine ve güvenlik tehditlerine karşı proaktif önlemler almasına olanak sağlar.

### 🔍 Ana İşlevler

#### 📊 **Log İzleme ve Analiz**
- **Gerçek Zamanlı Log Dinleme** - Sunucu loglarını sürekli izleme
- **Regex Pattern Matching** - Özel regex desenleri ile log analizi
- **Log Filtreleme** - Belirli kriterlere göre log filtreleme
- **Log İstatistikleri** - Log aktivitelerinin detaylı analizi

#### 🛡️ **Güvenlik Yönetimi**
- **IP Adresi Kontrolü** - AbuseIPDB entegrasyonu ile IP güvenlik kontrolü
- **Blacklist Yönetimi** - Şüpheli IP'leri blacklist'e ekleme
- **Otomatik Ban Sistemi** - Tehlikeli IP'leri otomatik engelleme
- **Coğrafi Konum Tespiti** - IP adreslerinin coğrafi konumunu belirleme

#### 🔐 **Kimlik Doğrulama ve Yetkilendirme**
- **JWT Authentication** - Güvenli token tabanlı kimlik doğrulama
- **Role-based Access Control** - Rol tabanlı erişim kontrolü
- **API Güvenliği** - Tüm endpoint'lerin güvenli erişimi

#### 📧 **Bildirim Sistemi**
- **Email Notifications** - Güvenlik olayları için e-posta bildirimleri
- **Telegram Bot** - Anlık Telegram bildirimleri
- **Alert Management** - Güvenlik uyarılarının yönetimi

#### 🌍 **Coğrafi Analiz**
- **GeoIP2 Entegrasyonu** - IP adreslerinin coğrafi konumunu tespit
- **Ülke Bazlı Filtreleme** - Belirli ülkelerden gelen trafiği filtreleme
- **Bölgesel Güvenlik** - Coğrafi bazlı güvenlik politikaları

---

## 📋 İçindekiler

- [Özellikler](#-özellikler)
- [Teknolojiler](#-teknolojiler)
- [Proje Yapısı](#-proje-yapısı)
- [Kurulum](#-kurulum)
- [Kullanım](#-kullanım)
- [API Dokümantasyonu](#-api-dokümantasyonu)
- [Güvenlik](#-güvenlik)
- [Deployment](#-deployment)
- [Katkıda Bulunma](#-katkıda-bulunma)

---

## ✨ Özellikler

### 🔐 Güvenlik
- **JWT Authentication** - Token tabanlı kimlik doğrulama
- **Spring Security** - Kapsamlı güvenlik yönetimi
- **Password Encryption** - Şifre şifreleme
- **Role-based Access Control** - Rol tabanlı erişim kontrolü

### 🗄️ Veritabanı
- **MySQL 8.3** - Güçlü veritabanı desteği
- **Spring Data JPA** - ORM ve veritabanı erişimi
- **Hibernate** - JPA implementasyonu
- **Database Migrations** - Veritabanı migrasyonları

### 🌐 API & Dokümantasyon
- **RESTful API** - REST standartlarına uygun
- **OpenAPI/Swagger** - Otomatik API dokümantasyonu
- **Validation** - Veri doğrulama
- **Exception Handling** - Kapsamlı hata yönetimi

### 🔧 Geliştirme Araçları
- **Lombok** - Kod azaltma ve otomatik getter/setter
- **DevTools** - Geliştirme araçları
- **Logging** - Kapsamlı loglama sistemi

### 📧 Entegrasyonlar
- **Email Service** - E-posta gönderimi
- **Telegram Bot** - Telegram entegrasyonu
- **GeoIP2** - Coğrafi konum servisi
- **JSoup** - Web scraping
- **JSch** - SSH bağlantıları

### 🐳 DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-service deployment
- **Maven** - Build automation

---

## 🛠️ Teknolojiler

### Backend Framework
- **Java 21** - En güncel Java sürümü
- **Spring Boot 3.1.2** - Modern Spring framework
- **Spring Security 6.2.2** - Güvenlik framework'ü
- **Spring Data JPA** - Veritabanı erişimi

### Veritabanı
- **MySQL 8.3** - İlişkisel veritabanı
- **Hibernate** - ORM framework'ü

### Güvenlik
- **JWT (JSON Web Tokens)** - Token tabanlı kimlik doğrulama
- **Spring Security Crypto** - Şifreleme işlemleri

### API & Dokümantasyon
- **SpringDoc OpenAPI 2.2.0** - API dokümantasyonu
- **Spring Boot Validation** - Veri doğrulama
- **Jackson 2.15.0** - JSON işleme

### Entegrasyonlar
- **Spring Boot Mail** - E-posta servisi
- **Telegram Bots 6.8.0** - Telegram bot entegrasyonu
- **GeoIP2 4.2.1** - Coğrafi konum servisi
- **JSoup 1.17.2** - HTML parsing
- **JSch 0.1.55** - SSH bağlantıları

### DevOps & Build
- **Maven** - Dependency management
- **Docker** - Containerization
- **Docker Compose** - Multi-service orchestration

---

## 📁 Proje Yapısı

```
src/main/java/org/lt/project/
├── config/          # Konfigürasyon sınıfları
├── controller/      # REST API controller'ları
│   ├── AbuseApi.java           # IP güvenlik kontrolü
│   ├── AuthenticationApi.java  # Kimlik doğrulama
│   ├── BanningIpApi.java      # IP engelleme
│   ├── FileApi.java           # Dosya işlemleri
│   ├── GeoIPCountryController.java # Coğrafi konum
│   ├── IpCheckController.java  # IP kontrolü
│   ├── LogListenerApi.java    # Log dinleme
│   ├── ServerApi.java         # Sunucu yönetimi
│   └── SettingsApi.java       # Ayarlar
├── dto/            # Data Transfer Objects
├── exception/      # Özel exception sınıfları
├── model/          # Entity sınıfları
├── repository/     # Data access layer
├── security/       # Güvenlik konfigürasyonu
├── service/        # Business logic layer
├── specification/  # JPA Specification'ları
├── util/           # Utility sınıfları
└── validator/      # Custom validator'lar
```

---

## 🚀 Kurulum

### Gereksinimler
- **Java 21** veya üzeri
- **Maven 3.6+**
- **MySQL 8.3**
- **Docker & Docker Compose** (opsiyonel)

### 1. Repository'yi Klonlayın
```bash
git clone https://github.com/OnurErkanToros/lt-bg.git
cd lt-bg
```

### 2. Veritabanını Hazırlayın
```sql
CREATE DATABASE ltdb;
CREATE USER 'ltuser'@'localhost' IDENTIFIED BY 'ltpassword';
GRANT ALL PRIVILEGES ON ltdb.* TO 'ltuser'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Uygulama Özelliklerini Yapılandırın
`src/main/resources/application.yml` dosyasını düzenleyin:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ltdb
    username: ltuser
    password: ltpassword
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect

server:
  port: 1999

logging:
  file:
    name: ltapp.log
```

### 4. Uygulamayı Çalıştırın

#### Maven ile
```bash
mvn clean install
mvn spring-boot:run
```

#### JAR dosyası ile
```bash
mvn clean package
java -jar target/lt-project-0.0.1.jar
```

---

## 🐳 Docker ile Kurulum

### Docker Compose ile Hızlı Başlangıç
```bash
# Uygulamayı başlat
docker-compose up -d

# Logları görüntüle
docker-compose logs -f

# Uygulamayı durdur
docker-compose down
```

### Manuel Docker Build
```bash
# Docker image oluştur
docker build -t lt-spring-boot-app .

# Container çalıştır
docker run -p 1999:1999 lt-spring-boot-app
```

---

## 📖 Kullanım

### API Endpoint'leri
Uygulama başlatıldıktan sonra aşağıdaki endpoint'lere erişebilirsiniz:

- **Swagger UI:** `http://localhost:1999/swagger-ui.html`
- **API Docs:** `http://localhost:1999/v3/api-docs`
- **Health Check:** `http://localhost:1999/actuator/health`

### Ana Özellikler

#### 🔍 **Log İzleme**
```bash
# Log dinlemeyi başlat
curl -X POST http://localhost:1999/log-listener/start

# Log dinleme durumunu kontrol et
curl http://localhost:1999/log-listener/status

# Log dinlemeyi durdur
curl -X POST http://localhost:1999/log-listener/stop
```

#### 🛡️ **IP Güvenlik Kontrolü**
```bash
# IP adresini kontrol et
curl -X POST "http://localhost:1999/abuse/check-ip?ipAddress=192.168.1.1&maxAgeInDays=30"

# Blacklist'i yenile
curl -X POST http://localhost:1999/abuse/blacklist/refresh

# Blacklist'i görüntüle
curl http://localhost:1999/abuse/blacklist/all?page=0&size=10
```

#### 🌍 **Coğrafi Konum Tespiti**
```bash
# IP'nin coğrafi konumunu öğren
curl http://localhost:1999/geoip/country?ip=8.8.8.8
```

#### 🔐 **Kimlik Doğrulama**
```bash
# Giriş yap
curl -X POST http://localhost:1999/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'
```

---

## 📚 API Dokümantasyonu

Uygulama çalıştığında Swagger UI üzerinden interaktif API dokümantasyonuna erişebilirsiniz:

**URL:** `http://localhost:1999/swagger-ui.html`

### API Kategorileri:
- **Authentication** - Kimlik doğrulama işlemleri
- **Log Listener** - Log izleme ve yönetimi
- **Abuse IP** - IP güvenlik kontrolü
- **GeoIP** - Coğrafi konum servisleri
- **Server Management** - Sunucu yönetimi
- **Settings** - Sistem ayarları

---

## 🔐 Güvenlik

### JWT Authentication
Uygulama JWT (JSON Web Token) tabanlı kimlik doğrulama kullanır:

```java
// Token oluşturma
String token = jwtService.generateToken(user);

// Token doğrulama
boolean isValid = jwtService.validateToken(token);
```

### Spring Security
- **Password Encryption** - BCrypt şifreleme
- **Role-based Access** - Rol tabanlı erişim
- **CORS Configuration** - Cross-origin resource sharing
- **CSRF Protection** - Cross-site request forgery koruması

### IP Güvenlik Kontrolü
- **AbuseIPDB Entegrasyonu** - Gerçek zamanlı IP kontrolü
- **Blacklist Yönetimi** - Şüpheli IP'leri engelleme
- **Otomatik Ban Sistemi** - Tehlikeli IP'leri otomatik engelleme

---

## 🚀 Deployment

### Production Deployment
```bash
# Production build
mvn clean package -Pprod

# JAR dosyasını çalıştır
java -jar -Dspring.profiles.active=prod target/lt-project-0.0.1.jar
```

### Docker Deployment
```bash
# Production Docker image
docker build -t lt-spring-boot-app:prod .

# Docker Compose ile production
docker-compose -f docker-compose.prod.yml up -d
```

### Environment Variables
```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/ltdb
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=prod_password
```

---

## 🧪 Test

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### API Tests
```bash
# Swagger UI üzerinden test
http://localhost:1999/swagger-ui.html
```

---

## 📊 Monitoring & Logging

### Health Checks
- **Application Health:** `/actuator/health`
- **Database Health:** `/actuator/health/db`
- **Disk Space:** `/actuator/health/disk`

### Logging
- **Application Logs:** `ltapp.log`
- **Log Level:** `INFO` (production), `DEBUG` (development)
- **Structured Logging:** JSON format

### Güvenlik İzleme
- **IP Kontrol Logları** - Güvenlik olaylarının kaydı
- **Blacklist Güncellemeleri** - Engellenen IP'lerin takibi
- **Coğrafi Konum Analizi** - IP lokasyon verilerinin kaydı

---

## 🤝 Katkıda Bulunma

1. **Fork** yapın
2. **Feature branch** oluşturun (`git checkout -b feature/amazing-feature`)
3. **Commit** yapın (`git commit -m 'Add amazing feature'`)
4. **Push** yapın (`git push origin feature/amazing-feature`)
5. **Pull Request** oluşturun

### Geliştirme Kuralları
- ✅ Clean Code prensiplerini takip edin
- ✅ Unit test yazın
- ✅ API dokümantasyonunu güncelleyin
- ✅ Commit mesajlarını açıklayıcı yazın

---

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.

---

## 👨‍💻 Geliştirici

**Onur Erkan Toros**

- **GitHub:** [@OnurErkanToros](https://github.com/OnurErkanToros)
- **LinkedIn:** [Onur Erkan Toros](https://www.linkedin.com/in/onurerkantoros/)

---

<div align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.1.2-green?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-8.3-blue?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-Container-blue?style=for-the-badge&logo=docker&logoColor=white"/>
</div>

---

*"Sunucu güvenliği ve log yönetimi için güçlü, güvenilir ve ölçeklenebilir çözümler geliştiriyoruz!"* 🚀