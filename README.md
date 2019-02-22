# GUI GC: A Java EE Cloud Benchmark

This project provides a JavaFX benchmark that focuses on stressing the GC of the underlying runtime targeting GUI applications. Automated testing can also be conducted and four preset settings are provided. Additionally, a Dockerfile is included for standardized deployment on the IBM J9 JVM (a number of .so files might be required to be present on the folder: libglass, libjavafx_font, libjavafx_font_t2k, libprism_es2 and libprism_sw. Finally, a test script and results-parsing are included.

<img src="Screenshot.png?raw=true" alt="drawing" width="300"/>

## Building the docker image
To build the docker image it is a simple matter of running the docker build command below.

```bash
docker build -t guigcj9 .
```
* **-t guigcj9** - creates a tag with the name *guigcj9* you can use a diffrent name if you want but you will be required to modify the scripts if you are using them to match the name you want. 

If this works with no errors you don not need to read the **Getting the .so files** section.

### Getting the .so files
1. The .so files listed above are automaticly added as long as the link in the line below is working in the dockerfile 
    ```bash
    RUN wget ftp://ftp.pbone.net/mirror/download.fedora.redhat.com/pub/fedora/linux/updates/29/Everything/x86_64/Packages/o/openjfx-8.0.202-2.b02.fc29.x86_64.rpm
    ```
2. If the link above no longer works, you will need to download the files yourself the easyest way is to find the **openjfx-8.0.202-2.b02.fc29.x86_64.rpm** file, then extract the .so files located in */usr/lib/jvm/openjfx/rt/lib/amd64/*. You will also need jfxrt.jar located in the same rpm in */usr/lib/jvm/openjfx/rt/lib/ext/jfxrt.jar*

3. If both the options above fail to work then you will be required to find the .so files yourself. Files that you will be required to find are listed below.
    * libdecora_sse.so
    * libglassgtk2.so
    * libglass.so
    * libjavafx_font_freetype.so
    * libjavafx_font_pango.so
    * libjavafx_font.so
    * libjavafx_iio.so
    * libprism_common.so
    * libprism_es2.so
    * libprism_sw.so
    * jfxrt.so


### Getting the jfxrt.jar file
You will need jfxrt.jar file in the local directory, to get this file run the docker command below. However, if you needed to get the .so files following the steps above you will need to do the same for jfxrt.jar.

```bash
docker run -v $PWD:/vol -w vol guigcj9 cp /opt/ibm/java/jre/lib/jfxrt.jar /vol
```
* **-v $PWD:/vol** - Create are virtual drive that is linked to the same location as the GUIGC program on your computer

## Running the docker container

### GUI Mode

If the above was build correctly then running the docker container is as simple as running the command below.

```bash
docker run -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=unix$DISPLAY -v $PWD:/vol -w /vol guigcj9 java  -cp .:./jfxrt.jar GUIGCBench
```


if you end up running into a DISPLAY error in java you may need to run the command below, to allow the use of the X11.

```bash
xhost +
```

### Non GUI Mode

If you are running automated test it is posible to run the GUIGC with out the need of connecting it to the display. This is achived through the use of *X Virtual Frame Buffer (Xvfb)* to run in this mode run the command below.

```bash
docker run -v `pwd`:/vol -w /vol guigcj9 java -cp .:./jfxrt.jar GUIGCBench --autoRun=true --seed=<Seed Number> --asyncGCOnSleep=true
```

* **--seed** - The seed number that you are wanting to run GUIGC V2 with
* **--autorun=true** - Tells the program to run without any user input