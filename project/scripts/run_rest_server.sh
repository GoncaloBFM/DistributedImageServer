if [[ $# -eq 1 ]]; then
        java -classpath out/production/PictureServer/:lib/* sd.tp1.server.RestServerRun server_dir.$1 Server$1 $(( 8000 + $1 ))
        exit
fi

java -classpath out/production/PictureServer/:lib/* sd.tp1.server.RestServerRun $@
