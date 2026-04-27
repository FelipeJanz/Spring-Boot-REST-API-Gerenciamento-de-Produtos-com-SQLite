Produto API — REST API com Spring Boot e SQLite

Uma API RESTful completa para gerenciamento de produtos, desenvolvida com Spring Boot 4, Spring Data JPA, SQLite e Hibernate. O projeto implementa todas as operações CRUD, além de endpoints avançados de busca por nome, preço, quantidade e status — com um cliente Java (RestTemplate) integrado para consumo direto da API.


TECNOLOGIAS

Java 22 — Linguagem principal
Spring Boot 4.0.5 — Framework principal
Spring Data JPA — Abstração de persistência
Spring Web — Criação dos endpoints REST
Spring Validation — Validação dos campos da entidade
Hibernate Community Dialects — Dialeto SQLite para o Hibernate
SQLite (Xerial JDBC) — Banco de dados embutido
Spring DevTools — Hot reload em desenvolvimento
Maven — Gerenciador de dependências


ESTRUTURA DO PROJETO

src/main/java/com/produtoapi/
  ProjetoSpringbootApplication.java   -> Ponto de entrada da aplicação
  controller/
    ProdutoController.java            -> Recebe e roteia as requisições HTTP
  service/
    ProdutoService.java               -> Regras de negócio
  repository/
    ProdutoRepository.java            -> Acesso ao banco de dados
  model/
    Produto.java                      -> Entidade JPA (tabela no banco)
  runner/
    StartupRestClientRunner.java      -> Executa chamada REST ao iniciar a app
  client/
    CRUDJavaClient.java               -> Cliente Java para consumir a API
    crud-produto.html                 -> Interface CRUD web
    client-web-listagem.html          -> Interface web de listagem


ENDPOINTS DA API

Base URL: http://localhost:8080

CRUD Principal

GET    /produtos                  -> Lista todos os produtos
GET    /produtos/{id}             -> Busca produto por ID
POST   /produtos                  -> Cria um novo produto
POST   /produtos/salvarLista      -> Cria uma lista de produtos
PUT    /produtos/{id}             -> Atualiza um produto existente
DELETE /produtos/{id}             -> Remove um produto

Busca por Nome

GET /produtos/buscarPorNome?valor=                        -> Nome exato
GET /produtos/buscarPorNomeContendo?valor=                -> Nome contém o trecho
GET /produtos/buscarPorNomeComecandoCom?valor=            -> Nome começa com
GET /produtos/buscarPorNomeTerminandoCom?valor=           -> Nome termina com
GET /produtos/buscarPorNomeEStatus?nome=&status=          -> Nome + Status

Busca por Preço

GET /produtos/buscarPorPreco?valor=                       -> Preço exato
GET /produtos/buscarPorPrecoMaiorQue?valor=               -> Preço maior que
GET /produtos/buscarPorPrecoMenorQue?valor=               -> Preço menor que
GET /produtos/buscarTotalPreco                            -> Soma total de todos os preços
GET /produtos/buscarPorPrecoEStatus?preco=&status=        -> Preço + Status

Busca por Quantidade

GET /produtos/buscarPorQuantidade?valor=                  -> Quantidade exata
GET /produtos/buscarPorQuantidadeMaiorQue?valor=          -> Quantidade maior que
GET /produtos/buscarPorQuantidadeMenorQue?valor=          -> Quantidade menor que

Busca por Status

GET /produtos/buscarPorStatus?valor=                      -> Filtra por status
GET /produtos/buscarPorStatusNulos                        -> Produtos sem status
GET /produtos/buscarPorStatusPadrao                       -> Retorna "Disponível" por padrão
GET /produtos/contarTotalDeProdutos                       -> Total de produtos cadastrados


EXPLICAÇÃO DAS FUNÇÕES E ANOTAÇÕES


Produto.java — Modelo / Entidade

@Entity — Marca a classe como uma entidade JPA. O Hibernate usa isso para criar e mapear automaticamente a tabela correspondente no banco de dados.

@Id — Define o campo id como chave primária da tabela.

@GeneratedValue(strategy = GenerationType.AUTO) — O banco gera o valor do ID automaticamente a cada novo registro.

@NotEmpty(message = "Informe um nome.") — Validação da Bean Validation: impede que o campo nome seja salvo vazio ou nulo. A mensagem é retornada ao cliente em caso de violação.

Por que assim? O modelo representa exatamente a tabela no banco. Usar anotações JPA elimina a necessidade de escrever SQL manualmente para criar a estrutura, e a validação garante integridade dos dados na camada de entrada.


ProdutoRepository.java — Acesso ao Banco

JpaRepository<Produto, Long> — Herda automaticamente métodos prontos como findAll(), save(), deleteById(), findById(), count(), saveAll(), entre outros. Não é necessário implementar nada.

Query Methods por convenção de nome — O Spring Data JPA interpreta o nome dos métodos e gera o SQL correspondente automaticamente:
  findByNome(String nome)                       -> SELECT * FROM produto WHERE nome = ?
  findByNomeContaining(String nome)             -> WHERE nome LIKE '%?%'
  findByNomeStartingWith(String prefix)         -> WHERE nome LIKE '?%'
  findByNomeEndingWith(String suffix)           -> WHERE nome LIKE '%?'
  findByPrecoGreaterThan(Double preco)          -> WHERE preco > ?
  findByPrecoLessThan(Double preco)             -> WHERE preco < ?
  findByStatusIsNull()                          -> WHERE status IS NULL
  findByNomeAndStatus(String nome, String status) -> WHERE nome = ? AND status = ?

@Query — Usada quando a convenção de nome não é suficiente. Permite escrever JPQL diretamente. Exemplo: @Query("SELECT SUM(p.preco) FROM Produto p") para calcular o total de preços. Somatórios e agregações não são expressáveis por convenção de nome, então o @Query entra para consultas customizadas.


ProdutoService.java — Camada de Negócio

@Service — Registra a classe como um componente de serviço no contexto do Spring. Fica entre o Controller e o Repository.

@Autowired — Injeta automaticamente a dependência do ProdutoRepository, sem necessidade de instanciar manualmente.

atualizar(Long id, Produto produto) — Verifica com existsById(id) se o produto existe antes de salvar, lançando RuntimeException caso contrário. Isso evita criar um novo registro ao tentar atualizar um ID inexistente.

Por que ter uma camada de serviço separada? Para isolar as regras de negócio do Controller (que cuida apenas de HTTP) e do Repository (que cuida apenas do banco). Isso facilita testes e manutenção.


ProdutoController.java — Camada de Requisição HTTP

@RestController — Combina @Controller + @ResponseBody. Toda resposta dos métodos é automaticamente serializada para JSON e enviada no corpo da resposta HTTP.

@RequestMapping("/produtos") — Define a URL base para todos os endpoints da classe.

@CrossOrigin(origins = "*") — Habilita CORS para todas as origens. Permite que os clientes HTML façam requisições para a API sem bloqueio do navegador.

@GetMapping, @PostMapping, @PutMapping, @DeleteMapping — Mapeiam os métodos para os verbos HTTP correspondentes.

@PathVariable — Extrai valores dinâmicos da URL, como o {id} em /produtos/{id}.

@RequestBody — Desserializa o JSON do corpo da requisição para um objeto Java (Produto).

@RequestParam — Lê parâmetros de query string da URL, como ?valor=notebook.

@RequestParam(defaultValue = "Disponível") — Define um valor padrão caso o parâmetro não seja informado pelo cliente. Usado no endpoint buscarPorStatusPadrao.

@RequestParam(required = false) — Torna o parâmetro opcional, sem lançar erro se omitido.


StartupRestClientRunner.java — Execução ao Iniciar

CommandLineRunner — Interface do Spring Boot que executa o método run() automaticamente logo após o contexto da aplicação ser carregado.

@Component — Registra a classe como bean do Spring para que seja detectada e executada.

RestTemplate — Cliente HTTP do Spring para fazer chamadas REST de dentro do Java. Aqui é usado para buscar todos os produtos da própria API e exibir no console ao inicializar.

Por que usar isso? É útil para verificar o estado do banco de dados automaticamente ao subir a aplicação, funcionando como um health check manual dos dados.


CRUDJavaClient.java — Cliente Java da API

RestTemplate — Realiza todas as operações CRUD contra a API REST via HTTP.

getForEntity(url, Produto[].class) — Faz um GET e retorna a resposta com status e corpo.

postForObject(url, request, Produto.class) — Faz um POST enviando o produto no corpo e retorna o objeto criado.

restTemplate.delete(url + "/" + id) — Faz um DELETE para remover um recurso pelo ID.

exchange(url, HttpMethod.PUT, request, Produto.class) — Usado para PUT pois o RestTemplate não tem método direto para PUT com retorno de corpo. O exchange é mais flexível e permite qualquer verbo HTTP.

HttpEntity<Produto> — Encapsula o objeto e os cabeçalhos HTTP para envio no corpo da requisição.

Por que ter um cliente Java além dos HTMLs? Demonstra como consumir a API de dentro de outra aplicação Java, útil em contextos de integração entre sistemas ou microsserviços.


COMO EXECUTAR

Pré-requisitos:
  Java 22+
  Maven 3.8+

Passos:
  1. Clone o repositório
     git clone https://github.com/seu-usuario/produto-api-springboot.git
     cd produto-api-springboot

  2. Execute a aplicação
     ./mvnw spring-boot:run

A API estará disponível em http://localhost:8080.
O banco de dados SQLite (meu_banco_de_dados.db) é criado automaticamente na raiz do projeto na primeira execução.

Exemplos de requisição:

  Criar um produto:
  curl -X POST http://localhost:8080/produtos -H "Content-Type: application/json" -d '{"nome":"Notebook","preco":3500.00,"quantidade":10,"status":"Disponível"}'

  Listar todos:
  curl http://localhost:8080/produtos

  Buscar por preço maior que 100:
  curl "http://localhost:8080/produtos/buscarPorPrecoMaiorQue?valor=100"


CLIENTES DA API

O projeto inclui dois clientes HTML prontos para uso:

  crud-produto.html          -> Interface web completa com formulário de criação, atualização e exclusão de produtos.
  client-web-listagem.html   -> Interface de listagem e visualização dos produtos cadastrados.

Abra os arquivos diretamente no navegador com a API em execução.


CONFIGURAÇÃO (application.properties)

spring.datasource.url=jdbc:sqlite:meu_banco_de_dados.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.databaseplatform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

ddl-auto=update — O Hibernate atualiza o schema do banco automaticamente conforme as entidades, sem apagar os dados existentes.
show-sql=true — Exibe no console todas as queries SQL geradas pelo Hibernate, útil para debug.
