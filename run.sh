#/bin/bash
docker run --privileged --cpus 2 -m 1024MB -v `pwd`:/vol -w /vol guigcj9 /bin/bash -c "/entrypoint.sh && (perf stat -o /vol/Perf-Results/perf-${1}-${2}.txt -e cpu-cycles,cache-misses,page-faults,instructions,ref-cycles java -XX:+OnOutOfMemoryError -Xmx800m -Xgcpolicy:gencon -cp .:./jfxrt.jar GUIGCBench --autoRun=true --seed=$2 --asyncGCOnSleep=true > /vol/GC-Results/gc-${1}-${2}.csv 2> /vol/ERROR-Results/errors-${1}-${2}.txt)"

rm -f Snap* core* javacore* heapdump*
