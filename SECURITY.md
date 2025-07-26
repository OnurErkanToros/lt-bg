# 🔒 Güvenlik Rehberi

Bu doküman, LT Backend uygulamasının güvenlik yapılandırması ve best practices hakkında bilgi verir.

## ⚠️ Önemli Güvenlik Notları

### 1. Environment Variables Kullanımı
Hassas bilgiler asla doğrudan kod içinde saklanmamalıdır. Bunun yerine environment variables kullanın:

```bash
# ❌ YANLIŞ - Hassas bilgileri kod içinde saklama
jwt.key=fd5da6aa3c328a76abcaa19d50695ab704e28e57fd3eb7e368c4c510a5abe728320c7cc9c3a77a7c7899d75694e1859286e7d345ac80ea4df616c5fe9b1e061a71

# ✅ DOĞRU - Environment variable kullan
jwt.key=${JWT_SECRET_KEY}
```

### 2. Güvenli Şifre Oluşturma
Güçlü şifreler ve anahtarlar oluşturmak için:

```bash
# JWT Secret Key (minimum 256 bit)
openssl rand -hex 32

# Database Password
openssl rand -base64 32

# IP Check Secret
openssl rand -hex 64
```

### 3. Production Ortamı Güvenliği

#### Environment Variables
```bash
# .env dosyası oluşturun (git'e eklemeyin!)
DB_USERNAME=ltuser
DB_PASSWORD=your_secure_database_password_here
JWT_SECRET_KEY=your_secure_jwt_secret_key_here_minimum_256_bits
IP_CHECK_SECRET_KEY=your_secure_ip_check_secret_key_here
SWAGGER_USERNAME=admin
SWAGGER_PASSWORD=your_secure_swagger_password_here
```

#### Docker Secrets (Production)
```yaml
# docker-compose.prod.yml
services:
  spring-app:
    secrets:
      - db_password
      - jwt_secret
      - ip_check_secret

secrets:
  db_password:
    file: ./secrets/db_password.txt
  jwt_secret:
    file: ./secrets/jwt_secret.txt
  ip_check_secret:
    file: ./secrets/ip_check_secret.txt
```

### 4. Veritabanı Güvenliği

#### MySQL Güvenlik Ayarları
```sql
-- Güçlü şifre ile kullanıcı oluştur
CREATE USER 'ltuser'@'localhost' IDENTIFIED BY 'your_secure_password_here';

-- Sadece gerekli yetkileri ver
GRANT SELECT, INSERT, UPDATE, DELETE ON ltdb.* TO 'ltuser'@'localhost';

-- Root şifresini değiştir
ALTER USER 'root'@'localhost' IDENTIFIED BY 'your_secure_root_password';

-- Yetkileri uygula
FLUSH PRIVILEGES;
```

### 5. Network Güvenliği

#### Firewall Ayarları
```bash
# Sadece gerekli portları aç
sudo ufw allow 1999/tcp  # Application port
sudo ufw allow 3306/tcp  # MySQL (sadece local)
sudo ufw enable
```

#### SSL/TLS Yapılandırması
```yaml
# application-prod.properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
```

### 6. Log Güvenliği

#### Hassas Bilgileri Loglamayın
```java
// ❌ YANLIŞ - Hassas bilgileri loglama
logger.info("User password: " + password);

// ✅ DOĞRU - Hassas bilgileri maskeleme
logger.info("User authentication attempt for: " + username);
```

#### Log Dosyası Güvenliği
```bash
# Log dosyalarını koruyun
chmod 600 /var/log/ltapp/application.log
chown ltuser:ltuser /var/log/ltapp/application.log
```

### 7. API Güvenliği

#### Rate Limiting
```properties
# application-prod.properties
rate.limit.max-attempts=5
rate.limit.duration=300000
```

#### CORS Yapılandırması
```properties
# Sadece gerekli origin'leri izin ver
allowed.origins=https://yourdomain.com,https://admin.yourdomain.com
```

### 8. JWT Güvenliği

#### Token Yapılandırması
```properties
# JWT ayarları
jwt.expiration=86400000  # 24 saat
jwt.refresh-expiration=604800000  # 7 gün
```

#### Token Validation
```java
// Token'ları her zaman validate edin
if (!jwtService.validateToken(token)) {
    throw new UnauthorizedException("Invalid token");
}
```

### 9. Dosya Güvenliği

#### .gitignore Kontrolü
Aşağıdaki dosyaların .gitignore'da olduğundan emin olun:
```
.env
*.key
*.pem
*.p12
*.jks
secrets/
config/secrets/
```

#### Dosya İzinleri
```bash
# Hassas dosyaları koruyun
chmod 600 .env
chmod 600 keystore.p12
chmod 600 secrets/*
```

### 10. Monitoring ve Alerting

#### Güvenlik Logları
```properties
# Güvenlik olaylarını logla
logging.level.org.springframework.security=DEBUG
logging.level.org.lt.project.security=DEBUG
```

#### Alerting
```java
// Başarısız giriş denemelerini izle
if (failedAttempts > MAX_ATTEMPTS) {
    securityAlertService.sendAlert("Multiple failed login attempts", ipAddress);
}
```

## 🚨 Acil Güvenlik Kontrol Listesi

- [ ] Tüm JWT anahtarlarını değiştirin
- [ ] Veritabanı şifrelerini değiştirin
- [ ] IP check secret key'ini değiştirin
- [ ] Swagger kimlik bilgilerini değiştirin
- [ ] .env dosyasını oluşturun ve git'e eklemeyin
- [ ] Production ortamında SSL kullanın
- [ ] Firewall kurallarını yapılandırın
- [ ] Log dosyalarını güvenli hale getirin
- [ ] Rate limiting'i etkinleştirin
- [ ] CORS ayarlarını sınırlayın

## 📞 Güvenlik İletişimi

Güvenlik sorunları için:
- **Email:** [Güvenlik ekibi email adresi]
- **GitHub Issues:** [Security issue template kullanın]

## 🔄 Güvenlik Güncellemeleri

Bu doküman düzenli olarak güncellenir. Son güncelleme: 2025-07-26