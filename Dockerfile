FROM ubuntu:latest
LABEL authors="hyuns"

ENTRYPOINT ["top", "-b"]