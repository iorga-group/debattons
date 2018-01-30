# FROM maven:3.5-jdk-8-alpine
FROM maven:3-jdk-8-alpine

ADD setup/proxy.sh /usr/local/bin/setup-proxy.sh

CMD setup-proxy.sh && mvn spring-boot:run -Duser.home=$HOME -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
# using -Duser.home to point to another thing that /root in order to download the dependencies in another location than "?" in current workdir thanks to https://discuss.gradle.org/t/gradles-wrapper-is-creating-a-folder-called/10905/5
