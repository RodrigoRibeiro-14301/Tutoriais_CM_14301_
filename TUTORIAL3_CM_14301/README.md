# Tutorial 3 — Anotações, MVVM e Jetpack Compose

Projeto desenvolvido no âmbito da cadeira de **Computação Móvel** (CM) na ENIDH.  
O tutorial está dividido em duas partes: um processador de anotações em Kotlin JVM e uma aplicação Android de meteorologia com arquitetura MVVM.

---

## Parte 1 — Annotation Processor (Kotlin JVM)

### Estrutura do projeto

Projeto multi-módulo Gradle com três módulos:

```
GreetingProcessorProject/
├── annotations/   # Define as anotações personalizadas
├── processor/     # Implementa os processadores de anotações
└── app/           # Usa as anotações e as classes geradas
```

### Módulo annotations

Define duas anotações:

- `@Greeting(message: String)` — aplicada a funções; o processador gera uma classe wrapper que imprime a mensagem antes de chamar o método original.
- `@Extract(regex: String)` — aplicada a funções abstratas; o processador gera uma classe que implementa cada método usando a expressão regular definida na anotação.

Ambas são `@Retention(SOURCE)`, ou seja, existem apenas em tempo de compilação.

### Módulo processor

Contém dois processadores que usam **KotlinPoet** para gerar código e **AutoService** para se registarem automaticamente:

**GreetingProcessor** — gera uma classe wrapper por **composição**:
```kotlin
public final class MyClassWrapper(public val original: MyClass) {
    public final fun sayHello() {
        println("Hello from MyClass!")
        original.sayHello()
    }
}
```

**RegexProcessor** — gera uma classe extratora por **herança**:
```kotlin
public class DataProcessorExtractor(input: String) : DataProcessor(input) {
    override fun getName(): String? {
        val match = Regex("Name:(\\w+)").find(input)
        return match?.groupValues?.get(1)
    }
}
```

### Como funciona

```
Código com anotações (@Greeting / @Extract)
        ↓
      kapt (ativado em tempo de compilação)
        ↓
GreetingProcessor / RegexProcessor
        ↓
     KotlinPoet (escreve ficheiros .kt)
        ↓
MyClassWrapper / DataProcessorExtractor (em build/generated/)
        ↓
     Usados no Main.kt como classes normais
```

### Ferramentas utilizadas

| Ferramenta | Função |
|------------|--------|
| **kapt** | Ativa os processadores em tempo de compilação |
| **KotlinPoet** | Gera ficheiros Kotlin de forma programática |
| **AutoService** | Regista os processadores automaticamente |

### Executar

```bash
./gradlew :app:build
./gradlew :app:run
```

---

## Parte 2 — Weather App (MVVM + Jetpack Compose)

Reconstrução do WeatherApp do tutorial anterior, agora com arquitetura **MVVM** e interface construída com **Jetpack Compose**.

### Estrutura do projeto

```
com.example.cooljetpackweatherapp/
├── data/          # WeatherData.kt e WeatherApiClient.kt
├── viewmodel/     # WeatherViewModel.kt
└── ui/            # Composables da interface
```

### Camada de dados

- `WeatherData.kt` — data classes anotadas com `@Serializable` para fazer parse do JSON
- `WeatherApiClient.kt` — cliente HTTP com Ktor que consulta a API Open-Meteo

### ViewModel

O `WeatherViewModel` mantém o estado da UI num `StateFlow<WeatherUIState>` e gere os eventos vindos da interface: atualização de latitude, longitude e pedido de dados meteorológicos.

### Interface (Jetpack Compose)

A UI adapta-se automaticamente à orientação do dispositivo:
- **Portrait** — imagem do tempo, campos de coordenadas, tabela de dados e botão de atualização
- **Landscape** — layout reorganizado em colunas

Composables principais: `WeatherUI`, `CoordinatesCard`, `WeatherCard`, `WeatherRow`.

### Funcionalidades

- Consulta de dados meteorológicos via [Open-Meteo API](https://open-meteo.com/) (sem API key)
- Parâmetros exibidos: temperatura, velocidade do vento, direção do vento, pressão ao nível do mar, código meteorológico e hora
- Suporte multilingue: Português e Inglês (sem strings hardcoded na UI)
- Suporte a orientação portrait e landscape

---

## Tecnologias

`Kotlin` · `kapt` · `KotlinPoet` · `AutoService` · `Jetpack Compose` · `Ktor` · `MVVM` · `Open-Meteo API`
