FROM openjdk:18-slim
EXPOSE 8080

COPY build/install/playlistmgr /playlistmgr

ENTRYPOINT /playlistmgr/bin/playlistmgr