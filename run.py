import subprocess
import signal
import os
import time

pstotal = 1
process = []
logfile = "log.txt"
errorfile = "error.txt"
endprogram = False
pwd = os.getcwd()

seeds = [3712,31592,24901,570,312,27169,8116,3123,25250,23677,11583,18286,8644,21547,24125,16489,24402,3544,15964,10020,2987,14319,30121,27965,15715,28399,26447,9408,15830,1949,19008,19542,773,11141,20112,1085,5542,28229,4209,30793,19138,15792,16311,27782,4571,7669,11503,28974,11213,27467,6226,14200,9018,3579,9398,24733,31979,3077,1373,15041,5026,20381,1815,5799,31523,21927,6884,4297,17388,11093,2322,3758,26885,18634,31540,31457,26303,10275,27663,4748,4974,1121,18949,13992,4701,28347,5958,3912,31424,7331,18953,3682,27713,20768,9481,26468,9927,16365,30765,27316,27459,320,31074,21576,18954,29847,20265,12489,7354,15160,7237,12329,16282,3418,26321,20983,31765,32279,24895,30421,6843,11080,1335,1788,31848,10816,28256,9007,27182,26253,3555,21873,26573,1862,10681,12759,31709,30947,25248,6295,13339,9718,18624,29621,13136,12178,17836,12134,11689]


def sigint(signum, frame):
	print("\nWaiting for Process to end")
	endprogram = True
	for ps in process:
		ps.wait()
	exit(0)


def createProcess(rep, seed):
	return subprocess.Popen(["bash", pwd+"/run.sh", str(rep), str(seed)])


signal.signal(signal.SIGINT, sigint)
getzip = subprocess.Popen(["bash", pwd+"/get-required-zip.sh"])
getzip.wait()
with open(logfile, "w") as file:
	file.write(time.strftime("%Y-%m-%d %H:%M")+"\n")
	for rep in range(1, 9):
		for seed in seeds:
			if not endprogram:
				if len(process) >= pstotal:
					file.write("Waiting for process to end\n")
					psend = False
					while not psend:
						time.sleep(5)
						for ps in process:
							status = ps.poll()
							if status == None:
								continue
							else:
								process.remove(ps)
								psend = True
								break
				file.write("PS START - "+time.strftime("%Y-%m-%d %H:%M")+" seed: "+str(seed)+"\trep: "+str(rep)+"\n")
				file.flush()
				ps = createProcess(rep, seed)
				process.append(ps)
	sigint(None, None)
