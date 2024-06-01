# Spring Boot + SNS + SQS + LocalStack

[LocalStack](https://github.com/localstack/localstack) is an open-source Amazon Web Services (AWS) mocking service.
Also, it supports aws-cli commands in the shell. We can use LocalStack to test and debug our code without deploying it
on the Amazon environment.
LocalStack comes with three versions: standard, pro, and enterprise. The standard version already provides common AWS
APIs such as Lambda, SNS, SQS, S3.

In this post, we are going to integrate Spring Boot with the Simple Notification Service (SNS) of AWS on the local
environment by using the LocalStack.
First, we will run LocalStack and then connect to the SNS service from Spring Boot by using the
official [AWS API](https://aws.amazon.com/sdk-for-java/).
Then we are going to execute a couple of SNS scenarios such as publishing a message.

### Running LocalStack

We have a couple of options to run LocalStack in the local environment. Let's go with the docker-compose approach.
Docker needs to be installed for this approach.

```shell
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

With a simple docker-compose command, we can initialize and run the LocalStack.

```shell
docker-compose up -d
```

In the first run, it might take some time to download the docker image. In the consecutive runs, it will start faster.
We can use the SNS offline as if we are in the AWS environment.

Running Test

```shell
mvn clean test
```

### Summary

In this post, we implemented a couple of Amazon SQS functionality such as creating a queue, sending a message.
Also, we integrated the Amazon SQS and SNS and redirected an SNS message to the corresponding queue. Thanks to the
LocalStack,
we have tested all implementations offline without connecting to the Amazon Cloud.

