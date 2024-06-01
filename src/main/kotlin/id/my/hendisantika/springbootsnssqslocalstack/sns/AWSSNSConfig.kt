package id.my.hendisantika.springbootsnssqslocalstack.sns

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-sns-sqs-localstack
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 01/06/24
 * Time: 14.55
 * To change this template use File | Settings | File Templates.
 */
@Configuration
class AWSSNSConfig {

    @Bean(destroyMethod = "shutdown")
    fun amazonSNS(): AmazonSNS {
        return AmazonSNSClient.builder()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    "http://localhost:4566", "us-east-1"
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
