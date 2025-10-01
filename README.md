
Esse projeto é parte da disciplina de Arquiteturas Avançadas de Software com Microsserviços e Spring Framework do curso de pós grauação do Instituto Infnet.

Esse repositório foi criado para desenvolver as feature 3 e 4 do trabalho da disciplina.

## Feature 3

Foi criado um novo repositório para aplicação do conceito de módulos. Os dois serviços anteriores foram unidos em um só repositório, dividido em três módulos:

**common-domain** → responsável por ter as classes de entidades e de comum interesse para regras de negócio

**external-api** → responsável por se conectar a API do Open Food.

**main-app** → módulo que contém as regras de negócio e a camada de controller.

O objeto principal desse projeto é o registro de compras. Ao criar o produto que não está no banco de dados, como por exemplo o  de código 7896024761651, será feita uma consulta na API do OpenFood para tentar buscar esses dados.
