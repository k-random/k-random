FROM gitpod/workspace-java-21

RUN sudo add-apt-repository ppa:longsleep/golang-backports -y \
    && sudo apt-get update \
    && sudo apt-get install -y python3-pip golang-go \
    && pip install pre-commit \
    && sudo rm -rf /var/lib/apt/lists/*
