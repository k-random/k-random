FROM gitpod/workspace-java-17

RUN sudo apt-get update \
    && sudo apt-get install -y python3-pip \
    && pip install pre-commit \
    && sudo rm -rf /var/lib/apt/lists/*
