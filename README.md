Implmentado os scripts de flyway pra cada tabela.
Implementado o CRUD das entidades Cliente, Endereço, Ocorrência e FOto da ocorrência.
Implementado paginação para todas as consulta.
Implementado a gravação das informações no postgres.
Implementado o upload dos arquivos no Min.io.
Criado o docker-compose.yaml.

Tive bastante problema com versionamento de dependencias, começando com o flyway até que eu descobri que tem uma dependencia especifica do flyway para postgres, tentei implemntar queryDSL para fazer uma query mais customizada e bonita, porém tive bastante problema com dependencias entre javax e jakarta e por fim tive o mesmo problema ao adicionar a autenticação e como estava acabando o meu tempo acabei desistindo de implementar a autenticação.
Tive varios momentos de frustação e felicidades, acabei commitando tudo de uma vez so.

Para testar a aplicação:
    - Rodar o arquivo docker-compose.yaml, ele subira o min.IO e o postgres em um container.
    - Rodar o projeto, não fiz nenhum executavel para rodar o projeto no docker, precisara criar o executavel ou abrir o IDE para executar.
    - Todos as entidades tem o CRUD padrão, que seria gravar, consultar, alterar e deletar.

Como testar as entidades:
Para gravar um Cliente - POST:

Url: http://localhost:8080/client
Body: 
{
    "name": "Douglas",
    "birthday": "1999-03-14",
    "cpf": 1233123,
    "creationDate": "2024-11-13"
}

Para Alterar o cliente precisa passar o código dele na Url - PUT:

Url: http://localhost:8080/client/1
Body:
{
    "name": "Douglas Royer",
    "birthday": "1999-03-14",
    "cpf": 1233123,
    "creationDate": "2024-11-13"
}

Para buscar cliente. Caso queira altera a paginação passe nos parametros as seguintes chaves page e size e os valores que queira testar - GET:
Url: http://localhost:8080/client
Body: {}

Para deletar cliente, informar o código do cliente na Url- DELETE:
Url: http://localhost:8080/client/1
Body: {}

Para cadastrar endereço - POST:
Url: http://localhost:8080/address
Body: 
{
    "address": "Rua sete",
    "neighborhood": "Itoupava",
    "cep": 1,
    "city": "Blumenau",
    "state": "Santa catarina"
}

Para Alterar endereço precisa passar o código do endereço na Url - PUT:
Url: http://localhost:8080/address/1
Body: 
{
    "address": "Rua quinze",
    "neighborhood": "Itoupava",
    "cep": 1,
    "city": "Blumenau",
    "state": "Santa catarina"
}

Para buscar os endereço. Caso queira altera a paginação passe nos parametros as seguintes chaves page e size e os valores que queira testar - GET:
Url: http://localhost:8080/address
Body: {}

Para deletar enderço, informar o código do endereço na Url - DELETAR:
Url: http://localhost:8080/address/1
Body: {}

Para Cadastrar uma ocorrência ela sempre começa com o status ativa e precisa passar endereço válido e cliente - POST:
Url: http://localhost:8080/event
Body: 
{
    "clientCode": 1,
    "addressCode": 1,
    "eventDate": "2022-01-01"
}

Para alterar a ocorrência precisa passar cliente válido e endereço e o código da ocorrência na Url - PUT:
Url: http://localhost:8080/event/1
Body: 
{
    "clientCode": 1,
    "addressCode": 1,
    "eventDate": "2024-01-01"
}

Para buscar a ocorrência. Adicionado paginação so precisa passar as chaves no params page, size e os valores. Também adicionado filtro para a ocorrência, podendo filtar por nome do cliente, cpf, data de criação da ocorrência, cidade e também foi adicionado a ordenção por data de criação e cidade.  - GET
Url: http://localhost:8080/event
Body: {}
params: Nome - name 
        CPF - cpf
        data de criação da ocorrência - eventDate
        cidade - city
        orderBy - exemplo: eventDate asc,city desc
        page - 1
        size - 10

Para deletar a ocorrÊncia - DELETE
Url: http://localhost:8080/event/1
Body: {}

Para cadastrar foto da ocorrencia - POST
Url: http://localhost:8080/eventPhoto
Body - form-data: Informar 2 chaves e valores, primeira chave será file, altere para tipo file e no valor selecione o arquivo desejado e a segunda chave será data com as seguintes informações:
{
    "eventCode": 3, 
    "creationDate": "2010-01-01",
}

Para alterar foto da ocorrencia - PUT
Url: http://localhost:8080/eventPhoto/1
Body - form-data: Informar 2 chaves e valores, primeira chave será file, altere para tipo file e no valor selecione o arquivo desejado e a segunda chave será data com as seguintes informações:
{
    "eventCode": 3, 
    "creationDate": "2010-01-01",
}

Para busca foto da ocorrência. Caso queira altera a paginação passe nos parametros as seguintes chaves page e size e os valores que queira testar - GET:
Url: http://localhost:8080/eventPhoto
Body: {}

Para deletar foto da ocorrência: DELETE
Url: http://localhost:8080/eventPhoto/1
Body: {}

Para cadastrar uma ocorrência, com a foto e o endereço, precisa passar o CPF válido do cliente na url - POST
Url: http://localhost:8080/event/address/1222
Body - form-data:  Informar 2 chaves e valores, primeira chave será file, altere para tipo file e no valor selecione o arquivo desejado e a segunda chave será data com as seguintes informações:
{
    "address": "Rua 15", 
    "neighborhood": "centro",
    "cep": 1,
    "city": "Blumenau", 
    "state": "Santa catarina"
}

Para finalizar uma ocorrência. Precisa passar o código da ocorrência na Url - POST
Url: http://localhost:8080/event/1
Body: {}