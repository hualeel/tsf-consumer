FROM 10.0.0.143:60080/demo/tomcat:latest
ADD target/tsf-spring-cloud-consumer-0.0.1-SNAPSHOT.jar /
EXPOSE 18083
CMD java -Dtsf_consul_ip=106.13.228.9 -Dtsf_consul_port=8500 -jar /tsf-spring-cloud-consumer-0.0.1-SNAPSHOT.jar