# GUI GC: A Java EE Cloud Benchmark

This project provides a JavaFX benchmark that focuses on stressing the GC of the underlying runtime targeting GUI applications. Automated testing can also be conducted and four preset settings are provided. Additionally, a Dockerfile is included for standardized deployment on the IBM J9 JVM (a number of .so files might be required to be present on the folder: libglass, libjavafx_font, libjavafx_font_t2k, libprism_es2 and libprism_sw. Finally, a test script and results-parsing are included.

![Main image](Screenshot.png?raw=true)

## Running

### Getting the .so files
1. The .so files listed above are automaticly added as long as the link in the line below is working in the dockerfile 
    ```bash
    RUN wget ftp://ftp.pbone.net/mirror/download.fedora.redhat.com/pub/fedora/linux/updates/29/Everything/x86_64/Packages/o/openjfx-8.0.202-2.b02.fc29.x86_64.rpm
    ```
2. If the link above is no longer working you will need to download these files yourself the easyest way is to find the **openjfx-8.0.202-2.b02.fc29.x86_64.rpm** file and then extract the .so files located in */usr/lib/jvm/openjfx/rt/lib/amd64/* and you will also need the jfxrt.jar file located in the same rpm file located in */usr/lib/jvm/openjfx/rt/lib/ext/jfxrt.jar*

3. If both the options above fail to work then you will be required to find these .so files yourself the files that you will be required to find are listed below.
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


### Getting the jfxrt.jar file
You will need the jfxrt.jar file in the local directory as set as the class path to get this file run the docker command below. However if you needed to get the .so files by following the steps above you will need to do the same for the jfxrt.jar.

```bash
docker run -v 'pwd':/vol -w vol guigcj9 cp /opt/ibm/java/jre/lib/jfxrt.jar /vol
```