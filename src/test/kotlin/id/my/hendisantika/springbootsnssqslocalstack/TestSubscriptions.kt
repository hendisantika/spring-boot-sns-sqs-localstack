package id.my.hendisantika.springbootsnssqslocalstack

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sqs.AmazonSQSAsync
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-sns-sqs-localstack
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 01/06/24
 * Time: 15.02
 * To change this template use File | Settings | File Templates.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestSubscriptions {

    private val topic = "topic"

    private val queue1 = UUID.randomUUID().toString()

    private val queue2 = UUID.randomUUID().toString()

    @Autowired
    private lateinit var amazonSNS: AmazonSNS

    @Autowired
    private lateinit var amazonSQS: AmazonSQSAsync

    private lateinit var topicArn: String

    private lateinit var queueUrl1: String
    private lateinit var queueUrl2: String

    @Test
    @Order(1)
    fun testCreateTopic() {
        val createTopic = amazonSNS.createTopic(topic)
        topicArn = createTopic.topicArn
        Assertions.assertEquals(200, createTopic.sdkHttpMetadata.httpStatusCode)
    }
}
