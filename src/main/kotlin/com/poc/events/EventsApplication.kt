package com.poc.events

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaTemplate

@SpringBootApplication
class EventsApplication {

	@Bean
	fun topic() = NewTopic("topic1", 10, 1)

	@Bean
	fun topic2() =
		TopicBuilder.name("thing1")
			.partitions(10)
			.replicas(1)
			.compact()
			.build()


	//Consumer
//	@KafkaListener(id = "myId", topics = ["topic1"])
//	fun listen(value: String?) {
//		println(value)
//	}

	@KafkaListener(id = "myId", topics = ["topic1"])
	fun listen(record: ConsumerRecord<String, String>) {
		val topic = record.topic()
		val partition = record.partition()
		val offset = record.offset()
		val timestamp = record.timestamp()
		val value = record.value()

		println("Received message: $value from topic: $topic, partition: $partition, offset: $offset, timestamp: $timestamp")
	}


	//Producer
	@Bean
	fun runner(template: KafkaTemplate<String?, String?>) =
		ApplicationRunner { template.send("topic1", "hello world") }

}

fun main(args: Array<String>) {
	runApplication<EventsApplication>(*args)
}
