from confluent_kafka import Producer, Consumer, KafkaError, TopicPartition # Import TopicPartition for clarity
import json
import time # Import time for sleep

# Kafka Producer configuration
producer_conf = {
    'bootstrap.servers': 'kafka:9092'
}
producer = Producer(producer_conf)

# Kafka Consumer configuration
consumer_conf = {
    'bootstrap.servers': 'kafka:9092',
    'group.id': 'python-group',
    'auto.offset.reset': 'earliest'
}
consumer = Consumer(consumer_conf)
consumer.subscribe(['greeting-requests'])

def process_message(msg):
    try:
        request = json.loads(msg.value().decode('utf-8'))
        full_name = request['fullName']
        first_name = full_name.split()[0] if full_name else "Unknown"
        response = {"message": f"Hello {first_name}"}
        print(f"Consumed message: {request}, producing response: {response}")
        # Make sure the producer is sending to 'greeting-responses'
        producer.produce('greeting-responses', key=msg.key().decode('utf-8'), value=json.dumps(response))
        producer.flush()
    except Exception as e:
        print(f"Error processing message: {e}")

if __name__ == "__main__":
    print("Starting Kafka consumer...")
    while True:
        msg = consumer.poll(timeout=1.0)
        if msg is None:
            continue

        if msg.error():
            error_code = msg.error().code()
            if error_code == KafkaError._PARTITION_EOF:
                # Normal, nothing to consume for now on this partition
                continue
            elif error_code == KafkaError.UNKNOWN_TOPIC_OR_PART:
                # This is the error we're seeing. It means the topic is not yet known to the client.
                # The client will retry metadata lookup automatically.
                # Just log and continue, don't break the loop.
                print(f"KafkaError: {msg.error()} - Retrying subscription...")
                time.sleep(2) # A small delay to avoid hammering the broker, though client retries handle this
                continue # IMPORTANT: Continue the loop, don't break
            else:
                # Other, more serious errors should probably stop the consumer or be handled differently
                print(f"Fatal KafkaError: {msg.error()}")
                break # Break for genuinely unrecoverable errors
        
        # If no error, process the message
        process_message(msg)

    # Clean up on exit
    consumer.close()