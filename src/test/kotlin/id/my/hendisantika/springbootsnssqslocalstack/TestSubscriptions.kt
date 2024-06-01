package id.my.hendisantika.springbootsnssqslocalstack

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.amazonaws.services.sns.util.Topics
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.ReceiveMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
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

    @Test
    @Order(2)
    fun testCreateQueues() {
        var result = amazonSQS.createQueue(queue1)
        queueUrl1 = result.queueUrl
        Assertions.assertEquals(200, result.sdkHttpMetadata.httpStatusCode)

        result = amazonSQS.createQueue(queue2)
        queueUrl2 = result.queueUrl
        Assertions.assertEquals(200, result.sdkHttpMetadata.httpStatusCode)
    }

    @Test
    @Order(3)
    fun testSubscriptions() {
        // first queue
        var subscribeQueue = Topics.subscribeQueue(amazonSNS, amazonSQS, topicArn, queueUrl1)
        Assertions.assertTrue(subscribeQueue.contains(topic))

        // second queue
        subscribeQueue = Topics.subscribeQueue(amazonSNS, amazonSQS, topicArn, queueUrl2)
        Assertions.assertTrue(subscribeQueue.contains(topic))
    }

    @Test
    @Order(4)
    fun testPublish() {
        val request = PublishRequest()
        request.topicArn = topicArn
        request.subject = "This is a sample subject"
        request.message = "This foo is a sample message"
        request.messageGroupId = "ExampleGroupId"
        val result = amazonSNS.publish(request)

        val receiveMessageResult1 = amazonSQS.receiveMessage(
            ReceiveMessageRequest()
                .withWaitTimeSeconds(5)
                .withQueueUrl(queueUrl1)
        )

        val receiveMessageResult2 = amazonSQS.receiveMessage(
            ReceiveMessageRequest()
                .withWaitTimeSeconds(5)
                .withQueueUrl(queueUrl2)
        )

        val objectMapper = ObjectMapper()

        val message1 = receiveMessageResult1.messages.first()
        val bodyMap1 = objectMapper.readValue(message1.body, Map::class.java)

        val message2 = receiveMessageResult2.messages.first()
        val bodyMap2 = objectMapper.readValue(message2.body, Map::class.java)

        Assertions.assertEquals(200, result.sdkHttpMetadata.httpStatusCode)
        Assertions.assertNotNull(result.messageId)

        Assertions.assertTrue(receiveMessageResult1.messages.isNotEmpty())
        Assertions.assertEquals(request.message, bodyMap1["Message"])
        Assertions.assertEquals(topicArn, bodyMap1["TopicArn"])
        Assertions.assertEquals(request.subject, bodyMap1["Subject"])

        Assertions.assertTrue(receiveMessageResult2.messages.isNotEmpty())
        Assertions.assertEquals(request.message, bodyMap2["Message"])
        Assertions.assertEquals(topicArn, bodyMap2["TopicArn"])
        Assertions.assertEquals(request.subject, bodyMap2["Subject"])
    }
}
