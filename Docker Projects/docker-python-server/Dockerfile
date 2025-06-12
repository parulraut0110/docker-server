FROM python:3.10-slim

WORKDIR /app

COPY pip-packages/ /packages/
COPY python-server.crt .
COPY python-server.key .
COPY docker-python-server.py .

RUN pip install --no-index --find-links=/packages flask

EXPOSE 5000
CMD ["python", "docker-python-server.py"]



