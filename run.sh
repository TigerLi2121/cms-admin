#!/bin/sh
## java env
export JRE_HOME=$JAVA_HOME/jre

APP_NAME='cms-admin'
PORT='8080'
CURRENT_DIR="/chroot/server/${APP_NAME}"
LOG_DIR="${CURRENT_DIR}/logs"
JAVA_OPTS="-Xms512m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=256m -XX:ParallelGCThreads=10 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -Xloggc:${LOG_DIR}/gc.log"
EXEC_COMMAND="${JAVA_HOME}/bin/java $JAVA_OPTS -jar ${CURRENT_DIR}/${APP_NAME}.jar"

usage() {
   echo "$0 start|stop|restart|log|backup"
   exit 1
}

start() {
    if [ -f ${CURRENT_DIR}/${APP_NAME}.jar ]
    then
        echo "start ${APP_NAME}"
        nohup $EXEC_COMMAND > $LOG_DIR/$APP_NAME.out 2>&1 &
        echo "end of start ${APP_NAME}"
    else
      echo "${APP_NAME}.jar is not exists!"
    fi
    RETVAL=$?
    echo
    return $RETVAL
}

stop() {
    pid=`netstat -nlp|grep ${PORT} | awk '{print $7}'| awk -F '[ / ]' '{print $1}'`
    echo "stopping..."
    kill -9 $pid || failure
    echo "done"
    RETVAL=$?
    echo
    return $RETVAL
}

#重启
restart() {
  stop
  start
}

#日志
log() {
  # 输出实时日志
  tail -f ${LOG_DIR}/${APP_NAME}'.log'
}

#备份
backup(){
  #获取当前时间作为备份文件名
  BACKUP_DATE=`date +"%Y%m%d(%H:%M:%S)"`
  echo 'backup file ->'${CURRENT_DIR}'/'${APP_NAME}$BACKUP_DATE'.jar'
      #备份当前jar包
  cp -r ${CURRENT_DIR}'/'${APP_NAME}'.jar' ${CURRENT_DIR}'/'${APP_NAME}'_'$BACKUP_DATE'.jar'
}

#根据输入参数,选择执行对应方法,不输入则执行使用说明
case "$1" in
"start")
  start
  ;;
"stop")
  stop
  ;;
"restart")
  restart
  ;;
"log")
  log
  ;;
"backup")
  backup
  ;;
*)
  usage
  ;;
esac
exit 0