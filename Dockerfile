FROM openjdk:18-slim

COPY build/install/playlistmgr /playlistmgr

ENTRYPOINT /playlistmgr/bin/playlistmgr