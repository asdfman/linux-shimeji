#!/usr/bin/env bash

rm conf
rm img
if [ "$1" = "" ]
then
	echo "No Shimeji specified, loading default Shimeji"
	ln -s Shimejis/default/conf conf
	ln -s Shimejis/default/img img
else
	if cd "Shimejis/$1"; then
		echo "Loading $1 Shimeji"
		cd ../..
		ln -s "Shimejis/$1/conf" conf
		ln -s "Shimejis/$1/img" img
	else
		echo "Couldn't find Shimeji $1, loading default Shimeji"
		ln -s Shimejis/default/conf conf
		ln -s Shimejis/default/img img
	fi
fi

java -classpath Shimeji.jar -Xmx1000m com.group_finity.mascot.Main -Djava.util.logging.config.file=./conf/logging.properties
