FROM docker-reg.emotibot.com.cn:55688/base/sles12sp3-java8:20171221

# Copy the source into the docker
RUN mkdir -p /usr/src/app/
WORKDIR /usr/src/app

#ADD . /usr/src/app/

COPY docker ./docker
COPY entrypoint.sh .
COPY config ./config
COPY target/sf-statistics*with-dependencies.jar .


# Entrypoint that reads the shell ENV variables as config flags
ENTRYPOINT ["/usr/src/app/entrypoint.sh"]
