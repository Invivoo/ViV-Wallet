#-------------------------------------------------------------------------------------------------------------
# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License. See https://go.microsoft.com/fwlink/?linkid=2090316 for license information.
#-------------------------------------------------------------------------------------------------------------

FROM circleci/openjdk:11-jdk-browsers

# Avoid warnings by switching to noninteractive
ENV DEBIAN_FRONTEND=noninteractive

# The node image comes with a base non-root 'node' user which this Dockerfile
# gives sudo access. However, for Linux, this user's GID/UID must match your local
# user UID/GID to avoid permission issues with bind mounts. Update USER_UID / USER_GID 
# if yours is not 1000. See https://aka.ms/vscode-remote/containers/non-root-user.
ARG USER_UID=1000
ARG USER_GID=$USER_UID

USER root

RUN echo 'kernel.unprivileged_userns_clone=1' > /etc/sysctl.d/userns.conf
# Configure apt and install packages
RUN rm -rf /var/lib/apt/lists/* && apt-get update \
  && apt-get -y install --no-install-recommends apt-utils dialog 2>&1 \ 
  #
  # Verify git and needed tools are installed
  && apt-get -y install git iproute2 procps \
  && apt-get install -y curl apt-transport-https lsb-release \
  && curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash - \
  && apt-get install -y nodejs \
  #
  # Install tslint and typescript globally
  && npm install -g tslint typescript \
  && npm install -g npm-check-updates \
  #
  # [Optional] Update a non-root user to match UID/GID - see https://aka.ms/vscode-remote/containers/non-root-user.
  && if [ "$USER_GID" != "1000" ]; then groupmod circleci --gid $USER_GID; fi \
  && if [ "$USER_UID" != "1000" ]; then usermod --uid $USER_UID circleci; fi \
  # [Optional] Add add sudo support for non-root user
  && apt-get install -y sudo \
  && echo circleci ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/circleci \
  && chmod 0440 /etc/sudoers.d/circleci \
  #
  # Clean up
  && apt-get autoremove -y \
  && apt-get clean -y \
  && rm -rf /var/lib/apt/lists/*

# Switch back to dialog for any ad-hoc use of apt-get
ENV DEBIAN_FRONTEND=

USER circleci


# Allow for a consistant java home location for settings - image is changing over time
# RUN if [ ! -d "/docker-java-home" ]; then ln -s "${JAVA_HOME}" /docker-java-home; fi
