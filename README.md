
Esse projeto é parte da disciplina de Arquiteturas Avançadas de Software com Microsserviços e Spring Framework do curso de pós grauação do Instituto Infnet.

Esse repositório foi criado para desenvolver as feature 3 e 4 do trabalho da disciplina.

## Feature 3

Foi criado um novo repositório para aplicação do conceito de módulos. Os dois serviços anteriores foram unidos em um só repositório, dividido em três módulos:

**common-domain** → responsável por ter as classes de entidades e de comum interesse para regras de negócio

**external-api** → responsável por se conectar a API do Open Food.

**main-app** → módulo que contém as regras de negócio e a camada de controller.

O objeto principal desse projeto é o registro de compras. Ao criar o produto que não está no banco de dados, como por exemplo o  de código 7896024761651, será feita uma consulta na API do OpenFood para tentar buscar esses dados.


## Feature 4

Utilização do Spring Security para autorização e autenticação.

Para a autenticação, foram criados dois usuários em memória para realização de testes.
Um usuário com role admin e outro com role user.

Foi definido que, para qualquer endpoint, com exceção do endpoint para acessar o console do h2, exija autenticação.

A forma de autenticação utilizada foi Auth Basic, com username e senha.

Para endpoints da api de produtos, foi criada autorização através de anotações nos métodos da classe controller.

Já nos endpoints da api de compras, a autorização foi por URL, sendo criadas regras na classe SecurityConfig.

Dessa forma, todos endpoints passam a exigir autenticação e também autorização, respeitando também o  Princípio do Menor Privilégio, uma vez que um usuário consegue acessar apenas aqueles recursos que estão assinalados com sua role 