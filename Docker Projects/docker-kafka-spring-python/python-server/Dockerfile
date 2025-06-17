FROM python:3.10-slim

WORKDIR /app

# Copy the application code and dependencies
COPY python-server.py .
COPY pip-packages/ /pip-packages/

# Install confluent-kafka from local packages
RUN pip install --no-index --find-links=/pip-packages confluent-kafka

# Run the Python server
CMD ["python", "docker-python-server.py"]