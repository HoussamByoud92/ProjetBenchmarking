
[Rapport_JAMIM_BYOUD_G8.pdf](https://github.com/user-attachments/files/23426056/Rapport_JAMIM_BYOUD_G8.pdf)

# Nada Jamim
# Houssam Byoud



# REST Web Services Performance Benchmark

**Contributors:** Nada JAMIM, Houssam BYOUD

## Project Overview
Comprehensive performance benchmark comparing three REST API implementations:
- **Variant A:** JAX-RS (Jersey) + JPA/Hibernate
- **Variant C:** Spring Boot + @RestController + JPA/Hibernate
- **Variant D:** Spring Boot + Spring Data REST

## Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Docker & Docker Compose
- JMeter 5.5+
- Git

## Quick Start

### 1. Start Infrastructure
\`\`\`bash
docker-compose up -d
\`\`\`

### 2. Build All Variants
\`\`\`bash
mvn clean install -DskipTests
\`\`\`

### 3. Initialize Database
\`\`\`bash
# Database automatically initialized by docker-compose
# Seed data inserted via docker-entrypoint-initdb.d scripts
psql -h localhost -U postgres -d benchmark -f database/schema.sql
psql -h localhost -U postgres -d benchmark -f database/seed-data.sql
\`\`\`

### 4. Run Variants
\`\`\`bash
# Terminal 1: Variant A (Jersey)
cd variant-a-jersey && java -jar target/variant-a-jersey-1.0.0-shaded.jar

# Terminal 2: Variant C (Spring MVC)
cd variant-c-spring-mvc && mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"

# Terminal 3: Variant D (Spring Data REST)
cd variant-d-spring-data-rest && mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
\`\`\`

### 5. Run JMeter Tests
\`\`\`bash
jmeter -n -t jmeter/scenarios/01-read-heavy.jmx -l results/read-heavy-a.jtl
\`\`\`

### 6. View Metrics
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)
- InfluxDB: http://localhost:8086

## Project Structure
\`\`\`
.
├── benchmark-commons/          # Shared JPA entities & DTOs
├── variant-a-jersey/           # JAX-RS implementation
├── variant-c-spring-mvc/       # Spring MVC @RestController implementation
├── variant-d-spring-data-rest/ # Spring Data REST implementation
├── database/                   # SQL schemas & seed data
├── jmeter/                     # JMeter test plans & data
├── monitoring/                 # Prometheus configuration
└── docker-compose.yml          # Infrastructure
\`\`\`

## Performance Testing Scenarios
1. **READ-heavy:** 50% list items, 20% items by category, 20% cat→items, 10% cat list
2. **JOIN-filter:** 70% items?categoryId, 30% item/{id}
3. **MIXED:** GET/POST/PUT/DELETE on items + categories
4. **HEAVY-body:** POST/PUT items with 5KB payload

## Key Metrics
- Throughput (RPS)
- Latency (p50/p95/p99)
- Error Rate (%)
- CPU/RAM Usage
- Garbage Collection Time
- Active Threads

## Build Notes
- All variants use identical database schema
- HikariCP pool: maxPoolSize=20, minIdle=10
- No L2 cache enabled for fair comparison
- Jackson JSON serialization across all variants
- Pagination: page/size parameters consistent

## Troubleshooting

### Port Already in Use
\`\`\`bash
# Change port in docker-compose.yml or stop conflicting services
lsof -i :8080
kill -9 <PID>
\`\`\`

### Database Connection Error
\`\`\`bash
# Check PostgreSQL is running
docker-compose logs postgres

# Verify connection
psql -h localhost -U postgres -d benchmark
\`\`\`

### JMeter Data Not Showing
\`\`\`bash
# Ensure InfluxDB is running and accessible
curl http://localhost:8086/health
\`\`\`

## References
- Jersey: https://eclipse-ee.github.io/jersey/
- Spring Boot: https://spring.io/projects/spring-boot
- Spring Data REST: https://spring.io/projects/spring-data-rest
- JMeter: https://jmeter.apache.org/
