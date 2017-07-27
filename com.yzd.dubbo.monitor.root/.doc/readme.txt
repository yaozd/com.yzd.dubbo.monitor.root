运维版启动命令
java -Dserver.port=9999 -Ddubbo.registry.group=dev -Ddubbo.registry.default.address=zookeeper://192.168.3.244:2181 -jar com.yzd.dubbo.monitor.web-1.0-SNAPSHOT.jar