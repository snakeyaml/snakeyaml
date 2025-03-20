FROM mcr.microsoft.com/devcontainers/java:17-jdk-bookworm

################### Docker-from-Docker installation ###################

# Install required packages
RUN apt-get update \
    && apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release

# Import the Docker repository GPG key
RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# Set permissions on GPG key
RUN chmod a+r /usr/share/keyrings/docker-archive-keyring.gpg

# Add the Docker repository to the APT sources
RUN echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu jammy stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# Update package lists and install Docker
RUN apt-get update \
    && apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin \
    && rm -rf /var/lib/apt/lists/*

# https://github.com/microsoft/vscode-dev-containers/tree/main/containers/docker-from-docker
ARG NONROOT_USER=vscode

RUN echo -e "#!/bin/sh\n\
    sudoIf() { if [ \"\$(id -u)\" -ne 0 ]; then sudo \"\$@\"; else \"\$@\"; fi }\n\
    SOCKET_GID=\$(stat -c '%g' /var/run/docker.sock) \n\
    if [ \"${SOCKET_GID}\" != '0' ]; then\n\
        if [ \"\$(cat /etc/group | grep :\${SOCKET_GID}:)\" = '' ]; then sudoIf groupadd --gid \${SOCKET_GID} docker-host; fi \n\
        if [ \"\$(id ${NONROOT_USER} | grep -E \"groups=.*(=|,)\${SOCKET_GID}\(\")\" = '' ]; then sudoIf usermod -aG \${SOCKET_GID} ${NONROOT_USER}; fi\n\
    fi\n\
    exec \"\$@\"" > /usr/local/share/docker-init.sh \
    && chmod +x /usr/local/share/docker-init.sh

############## End Docker-from-Docker section #############

################### Maven configuration ##################

# Establish .m2 folder and set ownership
RUN mkdir -p /home/vscode/.m2/repository 
RUN chown -R vscode:vscode /home/vscode/.m2

################### End Maven section ####################

######### More Docker-from-Docker recommendations ########

# https://github.com/microsoft/vscode-dev-containers/tree/main/containers/docker-from-docker
# VS Code overrides ENTRYPOINT and CMD when executing `docker run` by default.
# Setting the ENTRYPOINT to docker-init.sh will configure non-root access to
# the Docker socket if "overrideCommand": false is set in devcontainer.json.
# The script will also execute CMD if you need to alter startup behaviors.
ENTRYPOINT [ "/usr/local/share/docker-init.sh" ]
CMD [ "sleep", "infinity" ]

######### End Docker-from-Docker recommendations #########