import sys, getopt
import statistics
from os import walk

resultsL=""
perfL=""
outL=""
errorL=""
headprinted = False
skipdata = 200

resultfiles = []
perffiles = []

def removeblankdatainlist(datalist):
  returndata = []
  for d in datalist:
    if d != '':
      returndata.extend([d])
  return returndata

try:
  opts, args = getopt.getopt(sys.argv[1:],"r:p:o:",["results=,perfs=,out="])
except getopt.GetoptError:
  print("Error")
  sys.exit()
for opt, arg in opts:
  if opt in ("-r", "--results"):
    resultsL = arg
  elif opt in ("-p","--perfs"):
    perfL = arg
  elif opt in ("-o","--out"):
    outL = arg
for (dirpath, dirnames, filenames) in walk(resultsL):
  resultsL = dirpath
  resultfiles.extend(filenames)
  break
for (dirpath, dirnames, filenames) in walk(perfL):
  perfL = dirpath
  perffiles.extend(filenames)
with open("results.csv","w") as outfile:
  errorsfile = open("errors.csv", "w")
  for result in resultfiles:
    rep = result.split("-")[1]
    seed = result.split("-")[2].split('.')[0]
    results_data = [[],[],[],[],[],[],[],[],[],[]]
    isError = False
    with open(resultsL+result,"r") as f:
      if not headprinted:
        head = str(f.readline()).strip()+",seq"
        param = str(f.readline()).strip()+","+str(rep)
        head_titles = str(f.readline()).strip().split(',')
        head_mean = ""
        head_sd = ""
        for title in head_titles:
          if head_mean == "":
            head_mean = title+"-mean"
            head_sd = title+"-sd"
          else:
            head_mean = head_mean+","+title+"-mean"
            head_sd = head_sd+","+title+"-sd"
        head = head+","+str(head_mean).strip()+","+str(head_sd)
      else:
        f.readline()
        param = str(f.readline()).strip()+","+str(rep)
        f.readline()
      skip = skipdata
      for line in f:
        line = line.strip()
        if skip <= 0:
           skip = skip-1
           continue
        if line == "OutOfMemoryError":
          line = "0,0,0,0,0,0,0,0,0,0"
          isError = True
        numbers = line.split(',')
        results_data[0].extend([numbers[0]])
        results_data[1].extend([numbers[1]])
        results_data[2].extend([numbers[2]])
        results_data[3].extend([numbers[3]])
        results_data[4].extend([numbers[4]])
        results_data[5].extend([numbers[5]])
        results_data[6].extend([numbers[6]])
        results_data[7].extend([numbers[7]])
        results_data[8].extend([numbers[8]])
        results_data[9].extend([numbers[9]])
    for data in results_data:
      if len(data) < 2:
        mean = 0
      else:
        data = list(map(int,data))
        mean = statistics.mean(data)
      mean = round(mean,2)  
      param = param+","+str(mean)
    for data in results_data:
      sd = 0
      if len(data) < 2:
        sd = 0
      else:
        data = list(map(int,data))
        sd = statistics.stdev(data)
      sd = round(sd,2)
      param = param+","+str(sd)
    matchingperf = ""
    for perffile in perffiles:
      perfparts = perffile.split('-')
      perfrep = perfparts[1]
      perfseed = perfparts[2].split('.')[0]
      if perfrep == rep and perfseed == seed:
        matchingperf = perffile
        break
    perfData = []
    with open(perfL+matchingperf) as fperf:
      lines = fperf.readlines()[5:]
      for line in lines:
        parseline = removeblankdatainlist(line.strip().split(" "))
        if parseline == []:
          continue
        if parseline[1] == "task-clock":
          continue
        perfData.extend([parseline[0]])
        if parseline[1] == "seconds":
          head = head+","+"seconds-time-elapsed"
        else:
          head = head+","+parseline[1]+"/second"
    for data in perfData[0:5]:
      newdata = float(data)/float(perfData[5])
      param = param+","+str(newdata)
    param = param+","+perfData[5]
    if not headprinted:
      outfile.write(head+"\n")
      errorsfile.write(head+"\n")
      headprinted = True
    if isError:
      errorsfile.write(param+"\n")
    else:
      outfile.write(param+"\n")
  errorsfile.close()
