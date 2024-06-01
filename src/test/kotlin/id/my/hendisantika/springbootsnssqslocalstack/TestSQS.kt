package id.my.hendisantika.springbootsnssqslocalstack

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.Message
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
 * Time: 15.00
 * To change this template use File | Settings | File Templates.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestSQS {

    private val queue = UUID.randomUUID().toString()

    @Autowired
    private lateinit var amazonSQS: AmazonSQSAsync

    private lateinit var queueUrl: String

    private lateinit var message: Message

    @Test
    @Order(1)
    fun testCreateQueue() {
        val result = amazonSQS.createQueue(queue)
        queueUrl = result.queueUrl
        Assertions.assertEquals(200, result.sdkHttpMetadata.httpStatusCode)
    }
}
