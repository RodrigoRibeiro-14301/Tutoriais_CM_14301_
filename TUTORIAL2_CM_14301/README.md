# 1. Processamento de Eventos e Sealed Classes (Main.kt)

O objetivo deste programa é simular um sistema de logs de utilizador, processando diferentes tipos de interações (Login, Compra e Logout) de forma segura e organizada.

Para demonstrar a flexibilidade do Kotlin na manipulação de fluxos de dados, o exercício foi resolvido recorrendo a:

* **Utilizando Sealed Classes:** Criação de uma estrutura hierárquica para eventos, garantindo que o compilador verifique todos os tipos possíveis em expressões lógicas (`when`).
* **Utilizando Extension Functions:** Implementação de funções personalizadas (`filterByUser` e `totalSpent`) para estender as funcionalidades de listas nativas sem modificar o seu código fonte.
* **Utilizando Programação Funcional:** Uso de filtros de instância (`filterIsInstance`) e funções de agregação (`sumOf`) para processar e somar valores de compras de forma declarativa.
* **Utilizando Higher-Order Functions:** Implementação de uma função que aceita uma lambda como parâmetro, permitindo separar a lógica de iteração da lógica de exibição de dados.

____________________________________________________________________________________________

# 2. Implementação de Cache Genérica (Cache.kt)

O objetivo deste programa é criar um sistema de armazenamento temporário (Cache) genérico e reutilizável, capaz de gerir pares chave-valor de qualquer tipo, garantindo a integridade dos dados e facilitando operações comuns de manipulação.

Para demonstrar a robustez do sistema e o uso de tipos genéricos em Kotlin, o exercício foca-se em:

* **Utilizando Generics (K, V):** Implementação de uma classe que aceita qualquer tipo de objeto como chave ou valor (`Any`), permitindo a reutilização da lógica para diferentes contextos (ex: frequências de palavras ou registos de IDs).
* **Utilizando Higher-Order Functions:** Uso de lambdas em funções como `getOrPut` e `transform` para definir comportamentos dinâmicos, como a criação de valores padrão ou a modificação de valores existentes em memória.
* **Utilizando Gestão de Coleções:** Manipulação interna de um `MutableMap` para operações de inserção (`put`), remoção (`evict`) e consulta, protegendo o estado interno através de cópias seguras (`snapshot`).
* **Utilizando Predicados e Filtragem:** Implementação da função `filterValues` que utiliza uma função de ordem superior para filtrar o conteúdo da cache com base numa condição lógica definida em tempo de execução.


____________________________________________________________________________________________


# 3. Pipeline de Processamento de Dados (Pipeline.kt)

O objetivo deste programa é implementar um sistema de processamento sequencial (Pipeline), onde uma lista de dados de entrada (logs) passa por várias etapas de transformação até atingir o formato final desejado.

Para demonstrar o poder das DSLs (Domain Specific Languages) e o processamento de fluxos em Kotlin, o exercício utiliza:

* **Utilizando Type-Safe Builders (DSL):** Implementação da função `buildPipeline` que utiliza uma *lambda with receiver* (`Pipeline.() -> Unit`), permitindo configurar as etapas do pipeline de forma declarativa e intuitiva.
* **Utilizando a função fold:** Uso da função `fold` para acumular as transformações ao longo das várias etapas (stages), onde o resultado de uma etapa serve automaticamente como entrada para a seguinte.
* **Utilizando Transformações de Coleções:** Aplicação de funções funcionais como `map`, `filter`, `trim` e `mapIndexed` para limpar, filtrar erros e formatar os logs do sistema.
* **Utilizando Pairs e Higher-Order Functions:** Armazenamento de cada etapa como um `Pair` associando um nome descritivo a uma função de transformação, facilitando a depuração e a descrição da estrutura do pipeline.


____________________________________________________________________________________________


# 4. Vetores e Sobrecarga de Operadores (Vec2.kt)

O objetivo deste programa é criar uma classe de vetores bidimensionais (`Vec2`) que se comporta como um tipo primitivo da linguagem, permitindo realizar operações matemáticas de forma natural e intuitiva.

Para demonstrar a expressividade do Kotlin na criação de tipos de dados matemáticos, o exercício foca-se em:

* **Utilizando Operator Overloading:** Implementação de operadores aritméticos nativos (`+`, `-`, `*`) e unários (`-`), permitindo que objetos da classe sejam manipulados com símbolos matemáticos padrão.
* **Utilizando a Interface Comparable:** Implementação do operador `compareTo` com base na magnitude do vetor, o que permite utilizar operadores de comparação (`>`, `<`) e funções de agregação como `maxOrNull()` em coleções.
* **Utilizando Data Classes:** Uso de uma estrutura de dados leve que fornece automaticamente métodos como `toString()` e `equals()`, facilitando a depuração e comparação de vetores.
* **Utilizando Operadores de Acesso (get):** Sobrecarga do operador `get`, permitindo aceder às coordenadas do vetor através de índices (ex: `vetor[0]` para X), simulando o comportamento de um array.


____________________________________________________________________________________________


