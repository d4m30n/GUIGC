FROM ibmjava:8-sdk

RUN mkdir /vol
VOLUME /vol

RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get install -y  software-properties-common
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get install libwebkit2gtk-3.0-25 -y
RUN apt-get install fonts-freefont-ttf -y
RUN apt-get install libasound2 -y
RUN apt-get install libgtk-3-dev -y
RUN apt-get install libswt-gtk-3-java -y
RUN apt-get install p7zip-full
RUN apt-get install linux-tools-generic -y
RUN apt-get update
RUN apt-get clean

RUN wget ftp://ftp.pbone.net/mirror/download.fedora.redhat.com/pub/fedora/linux/updates/29/Everything/x86_64/Packages/o/openjfx-8.0.202-2.b02.fc29.x86_64.rpm
RUN 7z x openjfx-8.0.202-2.b02.fc29.x86_64.rpm
RUN mkdir openjfx
RUN 7z x openjfx-8.0.202-2.b02.fc29.x86_64.cpio -o/openjfx/
RUN cp /openjfx/usr/lib/jvm/openjfx/rt/lib/amd64/*.so /opt/ibm/java/jre/lib/amd64/
RUN cp /openjfx/usr/lib/jvm/openjfx/rt/lib/ext/jfxrt.jar /opt/ibm/java/jre/lib/
RUN rm -r /openjfx && rm openjfx-8.0.202-2.b02.fc29.x86_64.rpm && rm openjfx-8.0.202-2.b02.fc29.x86_64.cpio

# cd to vol
CMD cd /vol && java GUIGCBench
