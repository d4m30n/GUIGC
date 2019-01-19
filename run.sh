#/bin/bash
docker run -v $PWD:/vol -w /vol guigcj9 cp /opt/ibm/java/jre/lib/jfxrt.jar /vol
pid1=0
pid2=0
pid3=0
pid4=0

pidcheck () {
	if [ "$pid4" != 0 ] 
	then
		wait $pid1
		pid1=0
		echo "$pid1 DONE"
		wait $pid2
		pid2=0
		echo "$pid2 DONE"
		wait $pid3
		pid3=0
		echo "$pid3 DONE"
		wait $pid4
		pid4=0
		echo "$pid4 DONE"
	fi
}
assignpid () {
	pidcheck
	if [ "$pid1" == 0 ]
	then
		pid1=$!
    echo "Process 1 started"
	elif [ "$pid2" == 0 ]
	then	
		pid2=$!
    echo "Process 2 started"
	elif [ "$pid3" == 0 ]
	then
		pid3=$!
    echo "Process 3 started"
	else
		pid4=$!
    echo "Process 4 started"
	fi
}
for rep in 1 2 3 4 5 6 7 8
do
	for cpus in 1 2 4 8
	do
		for mem in 400 800 1600
		do
			for preset in E F G H
			do
				xmx=$((mem * 3 / 4))
				xns1=2
				xns2=1
				xns2=4
				docker run --cpus ${cpus} -m ${mem}MB -v `pwd`:/vol -w /vol guigcj9 /bin/bash -c "/entrypoint.sh && java -Xms8m -Xmn2m -Xmx${xmx}m -Xgcpolicy:gencon -cp .:./jfxrt.jar GUIGCBench --autoRun=true --preset=$preset --asyncGCOnSleep=true" > gc_gen.${rep}.${cpus}.${mem}.${preset}.25%.txt &
				assignpid
				docker run --cpus ${cpus} -m ${mem}MB -v `pwd`:/vol -w /vol guigcj9 /bin/bash -c "/entrypoint.sh && java -Xms8m -Xmn1m -Xmx${xmx}m -Xgcpolicy:gencon -cp .:./jfxrt.jar GUIGCBench --autoRun=true --preset=$preset --asyncGCOnSleep=true" > gc_gen.${rep}.${cpus}.${mem}.${preset}.12-5%.txt &
				assignpid
				docker run --cpus ${cpus} -m ${mem}MB -v `pwd`:/vol -w /vol guigcj9 /bin/bash -c "/entrypoint.sh && java -Xms8m -Xmn4m -Xmx${xmx}m -Xgcpolicy:gencon -cp .:./jfxrt.jar GUIGCBench --autoRun=true --preset=$preset --asyncGCOnSleep=true" > gc_gen.${rep}.${cpus}.${mem}.${preset}.50%.txt &
				assignpid
			done
		done
	done
done

