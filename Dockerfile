FROM ubuntu:latest
LABEL authors="alexe"

ENTRYPOINT ["top", "-b"]