# SnakeYAML
FROM java:8
MAINTAINER Andrey Somov <public.somov@gmail.com>

RUN apt-get update && apt-get install -y mercurial

# Install Maven manually to avoid Java 7 installation
WORKDIR /tmp
RUN wget http://apache.mirrors.hoobly.com/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
RUN tar -xzvf apache-maven-3.3.3-bin.tar.gz -C /opt
ENV M2_HOME=/opt/apache-maven-3.3.3
ENV PATH=$PATH:$M2_HOME/bin
RUN mkdir /projects
WORKDIR /projects
RUN hg clone https://bitbucket.org/asomov/snakeyaml
WORKDIR /projects/snakeyaml


# Define default command.
CMD ["bash"]

