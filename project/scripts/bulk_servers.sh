BULK_DIR="bulk_servers"

TEMPLATE_DIR="template.dir"

NR_SOAP=10
NR_REST=10

_II=0
start_server(){
	_II=$(( $_II + 1 ))
	cp -R $TEMPLATE_DIR $_II
	cd $TEMPLATE_DIR
}

start_soap(){
	start_server
	../run_soap_server.sh &
	cd ..
}

start_rest(){
	start_server
	../run_rest_server.sh &
	cd ..
}

cd $BULK_DIR

_I=0
while [[ $_I -lt $NR_SOAP ]]; do
	start_soap
	_I=$(( $_I + 1 ))

done

_I=0
while [[ $_I -lt $NR_REST ]]; do
	start_rest
	_I=$(( $_I + 1 ))
done

exit 0
