# Config API

sbt
assembly

sudo docker build -t config_service
sudo docker run --rm -p 8080:9090 config_service

curl http://localhost:8080