# Documentação do Projeto - Tech Events API

## Descrição Geral
Este projeto é uma API para gerenciar eventos tecnológicos. Ele fornece funcionalidades para criação, consulta e manipulação de eventos, incluindo detalhes como cupons, endereços e imagens. A API está integrada com a AWS, utilizando S3 Bucket para armazenamento de imagens, RDS para o banco de dados relacional e EC2 para o ambiente de produção.

---

## Estrutura do Projeto

### Classes Principais

#### 1. **Event**
Representa um evento na aplicação.

**Atributos:**
- `id`: Identificador único do evento (UUID).
- `title`: Título do evento.
- `description`: Descrição do evento.
- `imgUrl`: URL da imagem do evento.
- `eventUrl`: URL do evento.
- `remote`: Indica se o evento é remoto (Boolean).
- `date`: Data do evento (Date).
- `address`: Relacionamento com a entidade `Address`.

**Relacionamento:**
- `OneToOne` com a classe `Address`.

#### 2. **Address**
Representa o endereço associado a um evento.

**Atributos:**
- `id`: Identificador único do endereço (UUID).
- `city`: Cidade do evento.
- `uf`: Estado (unidade federativa).
- `event`: Relacionamento com a entidade `Event`.

**Relacionamento:**
- `ManyToOne` com a classe `Event`.

#### 3. **Coupon**
Representa um cupom associado a um evento.

**Atributos:**
- `id`: Identificador único do cupom (UUID).
- `code`: Código do cupom.
- `discount`: Percentual de desconto.
- `valid`: Data de validade do cupom.
- `event`: Relacionamento com a entidade `Event`.

**Relacionamento:**
- `ManyToOne` com a classe `Event`.

---

### DTOs (Data Transfer Objects)

#### **EventRequestDTO**
Estrutura para requisição de criação de eventos.

**Atributos:**
- `title`: Título do evento.
- `description`: Descrição.
- `date`: Data do evento (Long).
- `city`: Cidade.
- `state`: Estado.
- `remote`: Indica se o evento é remoto.
- `eventUrl`: URL do evento.
- `image`: Arquivo da imagem (MultipartFile).

#### **EventResponseDTO**
Estrutura para resposta dos eventos.

**Atributos:**
- `id`: Identificador único.
- `title`: Título.
- `description`: Descrição.
- `date`: Data do evento.
- `city`: Cidade.
- `state`: Estado.
- `remote`: Indica se o evento é remoto.
- `eventUrl`: URL do evento.
- `imageUrl`: URL da imagem.

#### **EventDetailsDTO**
Estrutura para exibir detalhes completos de um evento.

**Atributos:**
- `id`: Identificador único.
- `title`: Título.
- `description`: Descrição.
- `date`: Data do evento.
- `city`: Cidade.
- `uf`: Estado.
- `eventUrl`: URL do evento.
- `imageUrl`: URL da imagem.
- `coupons`: Lista de cupons associados.

#### **CouponRequestDTO**
Estrutura para requisição de criação de cupons.

**Atributos:**
- `code`: Código do cupom.
- `discount`: Percentual de desconto.
- `valid`: Data de validade (Long).

---

### Repositórios

#### **AddressRepository**
- Extende `JpaRepository` para manipulação de dados da entidade `Address`.

#### **CouponRepository**
- Extende `JpaRepository` para manipulação de dados da entidade `Coupon`.
- Métodos personalizados:
  - `findByEventIdAndValidAfter(UUID eventId, Date currentDate)`

---

## Controladores

### **EventController**
Controlador responsável por gerenciar os eventos.

**Endpoints:**
- **`POST /api/event`**: Criação de um novo evento.
- **`GET /api/event`**: Listar eventos paginados.
- **`GET /api/event/filter`**: Filtrar eventos por título, cidade, estado, ou intervalo de datas.
- **`GET /api/event/{eventId}`**: Buscar detalhes de um evento específico.

### **CouponController**
Controlador responsável por gerenciar cupons.

**Endpoints:**
- **`POST /api/coupon/event/{eventId}`**: Adicionar um cupom a um evento.

---

## Integração com AWS

### Configuração AWS
Arquivo de configuração `AWSConfig`:

- **Serviço S3**:
  - Utilizado para armazenar imagens dos eventos.
  - Configuração definida com a região especificada em `application.properties`.

---

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **JPA/Hibernate**
- **AWS (S3, RDS, EC2)**
- **Maven**
- **Lombok**
- **H2 Database (para testes locais)**

---

## Deploy

1. **S3 Bucket**: Armazenamento de imagens.
2. **RDS**: Banco de dados relacional em produção.
3. **EC2**: Ambiente para hospedagem da API.

---

## Configuração

### application.properties
```properties
aws.region=us-east-1
spring.datasource.url=jdbc:mysql://your-rds-instance:3306/your-database
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
```

---

## Melhorias Futuras
- Implementar autenticação e autorização.
- Melhorar a validação de dados de entrada.
- Adicionar logs estruturados.
- Criar testes automatizados (unitários e de integração).
