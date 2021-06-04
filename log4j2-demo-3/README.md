# 说明
## 工程说明
实现 log4j2 + disruptor 异步日志

## 其他
log4j2.xml中RollingFile，immediateFlush="false"  
默认为true，每次写入都会执行flush。这可以保证每次数据都被写入磁盘，但是会影响性能。在同步的loggers中每次写入执行flush，那就非常有用。异步loggers和appenders将会在一系列事件结束后自动执行flush，即使设置为false。这也保证了数据写入到磁盘而且很高效