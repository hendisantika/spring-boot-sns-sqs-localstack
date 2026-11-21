# Spring Boot + SNS + SQS + LocalStack

[![Java CI with Gradle](https://github.com/hendisantika/spring-boot-sns-sqs-localstack/actions/workflows/gradle.yml/badge.svg)](https://github.com/hendisantika/spring-boot-sns-sqs-localstack/actions/workflows/gradle.yml)
![JDK](https://img.shields.io/badge/JDK-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?logo=springboot)
![Kotlin](https://img.shields.io/badge/Kotlin-2.4-7F52FF?logo=kotlin)
![Gradle](https://img.shields.io/badge/Gradle-9.9-02303A?logo=gradle)

[LocalStack](https://github.com/localstack/localstack) is an open-source Amazon Web Services (AWS) mocking service.
Also, it supports aws-cli commands in the shell. We can use LocalStack to test and debug our code without deploying it
on the Amazon environment.
LocalStack comes with three versions: standard, pro, and enterprise. The standard version already provides common AWS
APIs such as Lambda, SNS, SQS, S3.

In this project, we integrate Spring Boot with the Simple Notification Service (SNS) and Simple Queue Service (SQS) of
AWS on the local environment by using LocalStack.
First, we run LocalStack and then connect to SNS/SQS from Spring Boot by using the
official [AWS SDK for Java](https://aws.amazon.com/sdk-for-java/).
Then we execute a couple of SNS/SQS scenarios such as creating queues, publishing messages and filter policies.

## Tech Stack

| Component       | Version                     |
|-----------------|-----------------------------|
| Java (JDK)      | 25 (Gradle toolchain)       |
| Kotlin          | 2.4.x                       |
| Spring Boot     | 4.1.x                       |
| Gradle          | 9.9.x (wrapper)             |
| AWS SDK for Java| 1.12.x (`sqs`, `sns`)       |
| LocalStack      | 3.4.0 (Docker)              |

## Prerequisites

- JDK 25 (e.g. [Eclipse Temurin](https://adoptium.net/))
- Docker & Docker Compose

## Project Structure

```
src
├── main/kotlin/id/my/hendisantika/springbootsnssqslocalstack
│   ├── SpringBootSnsSqsLocalstackApplication.kt
│   ├── sns/AWSSNSConfig.kt      # AmazonSNS client -> http://localhost:4566
│   └── sqs/AWSSQSConfig.kt      # AmazonSQSAsync client -> http://localhost:4566
└── test/kotlin/id/my/hendisantika/springbootsnssqslocalstack
    ├── TestSQS.kt               # create (standard & FIFO), list, send, delete message & queue
    ├── TestSubscriptions.kt     # create topic & queues, subscribe queues to topic, publish
    └── TestFilterPolicy.kt      # SNS subscription filter policy routing to specific queues
```

Both clients point to LocalStack at `http://localhost:4566` in region `ap-southeast-1` with dummy credentials.

## Running LocalStack

We have a couple of options to run LocalStack in the local environment. Let's go with the Docker Compose approach
(`compose.yaml`):

```yaml
services:
  localstack:
    image: localstack/localstack:3.4.0
    container_name: localstack_sns_sqs
    ports:
      - '4566:4566'
    environment:
      - DEFAULT_REGION=ap-southeast-1
      - SERVICES=sns,sqs
      - DEBUG=1
      - DATA_DIR=/tmp/localstack/data
    volumes:
      - '/var/run/docker.sock:/var/run/docker.sock'
```

With a simple Docker Compose command, we can initialize and run LocalStack:

```shell
docker compose up -d
```

In the first run, it might take some time to download the Docker image. In consecutive runs, it will start faster.
Check that SNS & SQS are ready:

```shell
curl -s http://localhost:4566/_localstack/health
```

We can use SNS and SQS offline as if we are in the AWS environment.

## Running Tests

The tests are integration tests and require LocalStack to be running.

```shell
./gradlew clean test
```

Build the whole project (compile + tests):

```shell
./gradlew build
```

Stop LocalStack when done:

```shell
docker compose down
```

## Continuous Integration

GitHub Actions ([`.github/workflows/gradle.yml`](.github/workflows/gradle.yml)) runs on every push and pull request
to `main`:

- **build** — sets up JDK 25 (Temurin), starts a LocalStack service container on port `4566`, then runs
  `./gradlew build`.
- **dependency-submission** — submits the Gradle dependency graph so Dependabot alerts cover all dependencies.

## Dependency Updates

[Dependabot](.github/dependabot.yml) keeps dependencies up to date:

- **Gradle** — daily, grouped into `spring-boot`, `kotlin`, `aws-sdk` and `test` updates.
- **GitHub Actions** — daily, grouped into a single PR.
- **Docker Compose** — weekly updates for the LocalStack image.

## Summary

In this project, we implemented a couple of Amazon SQS functionalities such as creating a queue and sending a message.
Also, we integrated Amazon SQS and SNS, redirected SNS messages to the corresponding queues and used subscription
filter policies. Thanks to LocalStack, we have tested all implementations offline without connecting to the Amazon
Cloud.
