# URL Shortener - Bitly Clone
![Architechture](architecture.svg)
## 🚀 Introduction
This is a **URL Shortener** system similar to Bitly, built using **Spring Boot** and **MySQL**. It allows users to generate short URLs from long ones and track analytics such as click counts and access details.

## 🛠 Tech Stack
- **Backend:** Java Spring Boot
- **Database:** MySQL
- **Cache:** Redis (for quick lookups)
- **Authentication:** JWT (for user authentication)
- **Message Queue:** Kafka (for handling analytics and logging)
- **Deployment:** Docker & Kubernetes

## 📌 Features
- ✅ Generate short URLs from long URLs
- ✅ Redirect short URLs to the original long URLs
- ✅ Track analytics (click count, location, user agent, etc.)
- ✅ User authentication (register, login, manage links)
- ✅ Custom short links (user-defined slugs)
- ✅ Expiry for short links

## 📦 Installation & Setup

### Prerequisites:
- Java 23
- MySQL 8
- Redis (optional for caching)
- Kafka (optional for event processing)

### Steps:
1. Clone this repository:
   ```sh
   git clone https://github.com/dqh999/url-shortener.git
   cd url-shortener
   ```
2. Configure the database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/urlshortener
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```
3. Build and run the application:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## 📌 API Endpoints

### 🔹 Shorten a URL
```http
POST /api/shortener/create
```
**Request Body:**
```json
{
  "originalUrl": "https://example.com/some-long-url"
}
```
**Response:**
```json
{
  "shortUrl": "http://localhost:8080/abc123"
}
```

### 🔹 Redirect to Original URL
```http
GET /{shortUrl}
```
**Example:**
```
GET http://localhost:8080/abc123
```
Redirects to `https://example.com/some-long-url`

### 🔹 Get Analytics for a Short URL
```http
GET /api/analytics/{shortUrl}
```
**Response:**
```json
{
  "clicks": 120,
  "created_at": "2025-02-07",
  "last_accessed": "2025-02-07T12:45:00Z"
}
```

## 🚀 Deployment
To deploy using Docker:
```sh
docker build -t url-shortener .
docker run -p 8080:8080 url-shortener
```

## 👨‍💻 Author
- **Hop Do Quang** ([GitHub](https://github.com/dqh999) | [Website](https://dqhdev.com))

## 📜 License
This project is licensed under the MIT License.

