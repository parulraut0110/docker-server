from confluent_kafka import Producer, Consumer, KafkaError
import json

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
            if msg.error().code() == KafkaError._PARTITION_EOF:
                continue
            else:
                print(msg.error())
                break
        process_message(msg)