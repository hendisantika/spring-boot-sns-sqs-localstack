package id.my.hendisantika.springbootsnssqslocalstack

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.CreateQueueRequest
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.SendMessageRequest
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

    @Test
    @Order(1)
    fun testCreateFifoQueue() {
        val request = CreateQueueRequest()
        request.queueName = "$queue.fifo"
        request.addAttributesEntry("FifoQueue", "true")
        val result = amazonSQS.createQueue(request)
        Assertions.assertEquals(200, result.sdkHttpMetadata.httpStatusCode)
    }

    @Test
    @Order(2)
    fun testListQueues() {
        val result = amazonSQS.listQueues()
        Assertions.assertEquals(200, result.sdkHttpMetadata.httpStatusCode)
        Assertions.assertTrue(result.queueUrls.isNotEmpty())
        Assertions.assertTrue(result.queueUrls.contains(queueUrl))
    }

    @Test
    @Order(3)
    fun testSendMessage() {
        val request = SendMessageRequest()
        request.messageBody = "This is SQS message"
        request.queueUrl = queueUrl
        val result = amazonSQS.sendMessage(request)
        val messageId = result.messageId
        Assertions.assertNotNull(messageId)
        val receiveMessageResult = amazonSQS.receiveMessage(queueUrl)
        message = receiveMessageResult.messages.first()
        Assertions.assertEquals(200, result.sdkHttpMetadata.httpStatusCode)
        Assertions.assertEquals(request.messageBody, message.body)
        Assertions.assertEquals(messageId, message.messageId)
    }
}
