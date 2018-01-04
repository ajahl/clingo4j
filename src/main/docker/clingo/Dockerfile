FROM ubuntu:16.04

USER root

RUN apt-get -y update && apt-get -y upgrade && apt-get -y install build-essential git cmake bison gcc

RUN git clone https://github.com/potassco/clingo.git
RUN cd clingo && git checkout v5.2.2 

ADD clingo_build_release.sh clingo/
RUN cd clingo && chmod +x clingo_build_release.sh