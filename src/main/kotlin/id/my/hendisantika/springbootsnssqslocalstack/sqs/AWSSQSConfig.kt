package id.my.hendisantika.springbootsnssqslocalstack.sqs

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-sns-sqs-localstack
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 01/06/24
 * Time: 14.57
 * To change this template use File | Settings | File Templates.
 */
@Configuration
class AWSSQSConfig {

    @Bean(destroyMethod = "shutdown")
    fun amazonSQS(): AmazonSQSAsync {
        return AmazonSQSAsyncClient.asyncBuilder()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    "http://localhost:4566", "ap-southeast-1"
                )
            )
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials("foo", "bar")
                )
            )
            .build()
    }
}
