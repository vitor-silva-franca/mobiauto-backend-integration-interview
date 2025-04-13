# mobiauto-backend-integration-interview

Backend desenvolvido para o desafio da Mobiauto, com foco em calcular a distância entre um cliente e diversos lojistas com base em CEP. 

A aplicação integra-se com APIs externas como [ViaCEP](https://viacep.com.br/), [Nominatim (OpenStreetMap)](https://nominatim.openstreetmap.org/ui/search.html) e [OSRM](https://project-osrm.org/) para enriquecer endereços e calcular rotas reais (distância e tempo).

Também inclui uma funcionalidade de IA via [OpenAI](https://openai.com/) para classificar os lojistas por proximidade e explicar os resultados.

## 🔧 Como rodar o projeto localmente

### 1. Pré-requisitos

Java 21

Maven 3.8+

MySQL Server 8+

### 2. Configurar o banco de dados MySQL

Crie um banco de dados MySQL com o nome "mobiautotestdb" e configure o application.properties ou application.yml com as credenciais:

```
spring.datasource.url=jdbc:mysql://localhost:3306/mobiautotestdb?createDatabaseIfNotExist=true

spring.datasource.username=root

spring.datasource.password=25051999

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
```

Para fazer uso do endpoint com I.A. que ordenará os lojistas por proximidade será necessário incluir uma chave OpenAI no campo abaixo:

```
spring.ai.openai.api-key =
spring.ai.openai.chat.options.model = gpt-4o
```

### 3. Rodar a aplicação

Antes de rodar a aplicação pela primeira vez, execute os comandos abaixo para garantir que todas as dependências estejam corretamente baixadas e o projeto esteja limpo:

```
mvn clean install
```

```
mvn spring-boot:run
```

A aplicação será iniciada em:

http://localhost:8080

## 🔍 Acessar a documentação Swagger

Após iniciar a aplicação, acesse:

http://localhost:8080/swagger-ui/index.html

Todos os endpoints estarão documentados com exemplos e descrições.

## 🚀 Tecnologias utilizadas

Java 21

Spring Boot

Spring Data JPA

Spring Validation

MySQL

Swagger

MapStruct

Spring AI + OpenAI

Jackson

Maven

JUnit 5

Mockito

## 💾 Exemplos de payloads JSON

Criar Cliente (POST /api/cliente)

```
{
  "nome": "João Silva",
  "cep": "01310200"
}
```

Criar Anunciante (POST /api/anunciante)

```
{
  "nome": "Lojista Auto Center",
  "cep": "04547000"
}
```

Resposta Distância (GET /api/distancia)

```
[
  {
    "lojistaId": 1,
    "nome": "Lojista Auto Center",
    "distanciaKm": 12.4,
    "duracaoMin": 25.3
  }
]
```

Resposta Distância com I.A. (GET /api/distancia/openai)

```
Para organizar a lista de lojistas por ordem de proximidade, levaremos em consideração a distância em quilômetros. Aqui está a lista organizada:

1. Anunciante A → Distância: 2,12 km, Tempo estimado: 4 minutos
2. Lojista Ítalo → Distância: 3,70 km, Tempo estimado: 7 minutos
3. Anunciante B → Distância: 6,38 km, Tempo estimado: 11 minutos
4. Lojista Auto Center → Distância: 8,51 km, Tempo estimado: 12 minutos
5. Teste → Distância: 9,20 km, Tempo estimado: 14 minutos
6. Lojista Auto Center → Distância: 9,20 km, Tempo estimado: 14 minutos
7. Lojista Mauro → Distância: 9,96 km, Tempo estimado: 18 minutos

Os lojistas mais próximos, com base na distância, são o Anunciante A (2,12 km) e o Lojista Ítalo (3,70 km).
```

## 🔌 Endpoints principais

POST /api/cliente -> Cadastra um novo cliente

POST /api/anunciante -> Cadastra um novo lojista

GET /api/distancia?clienteId=1 -> Calcula a distância de um cliente até todos os lojistas

GET /api/distancia?cep=01310200 -> Calcula distância usando um CEP direto

GET /api/distancia/openai?clienteId=1 -> Calcula a distância de um cliente até todos os lojistas e ordena por proximidade utilizando I.A.

GET /api/distancia/openai?cep=01310200 -> Calcula distância usando um CEP direto e ordena por proximidade utilizando I.A.

## 🔧 Instruções para uso

Cadastrar Clientes e Lojistas: via endpoints POST /api/cliente e POST /api/anunciante, informando nome e CEP.

Consultar Distância: via GET /api/distancia?clienteId=... ou ?cep=... ou via GET /api/distancia/openai?clienteId=... ou ?cep=..., você obtém a distância em quilômetros e tempo estimado em minutos até cada lojista.

Documentação: navegue pela interface Swagger para explorar os endpoints com exemplos reais e schemas validados.
