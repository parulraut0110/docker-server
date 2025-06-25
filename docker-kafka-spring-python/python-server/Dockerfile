FROM python:3.10-slim
WORKDIR /app

# Set Python to unbuffered output
ENV PYTHONUNBUFFERED=1

# Install system dependencies and build librdkafka-2.10.1
RUN apt-get update && apt-get install -y gcc g++ libssl-dev zlib1g-dev wget libsasl2-dev make && \
    wget -q https://github.com/edenhill/librdkafka/archive/refs/tags/v2.10.1.tar.gz -O /tmp/librdkafka-2.10.1.tar.gz && \
    tar -xzf /tmp/librdkafka-2.10.1.tar.gz -C /tmp && \
    cd /tmp/librdkafka-2.10.1 && \
    ./configure && make && make install && ldconfig && \
    cd .. && rm -rf /tmp/librdkafka-2.10.1 /tmp/librdkafka-2.10.1.tar.gz
    

COPY python-server.py .
COPY pip-packages/ /app/pip-packages/    

# Install confluent-kafka from local packages
RUN pip install --no-index --find-links=/app/pip-packages wheel
RUN pip install --no-index --find-links=/app/pip-packages setuptools==80.9.0
RUN pip install --no-index --find-links=/app/pip-packages confluent-kafka

RUN apt-get remove -y gcc g++ libssl-dev zlib1g-dev wget make && apt-get autoremove -y && apt-get clean

# Run the Python server
# CMD ["python", "python-server.py"]
ENTRYPOINT ["python3", "python-server.py"]