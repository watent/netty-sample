#netty-Sample

##ssl 导入证书
````

keytool -import -alias netty -keystore "/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/security/cacerts" -file "/var/folders/tt/4b2h8vqd38s_q0541w07yryh0000gn/T/keyutil_localhost_3516568636520152273.crt" -storepass changeit

##ssl 删除证书
keytool -delete -alias netty -keystore "/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home/jre/lib/security/cacerts" -storepass changeit


````