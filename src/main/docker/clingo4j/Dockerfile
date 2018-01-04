FROM lorislab/clingo

USER root
RUN apt-get -y install openjdk-8-jdk maven 

ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-amd64
ENV PATH "$PATH":/${JAVA_HOME}/bin:.:

CMD git clone https://github.com/lorislab/clingo4j.git && cd clingo4j && mvn install