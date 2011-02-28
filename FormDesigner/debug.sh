#!/bin/sh
MAVEN_CLASSPATH=`mvn dependency:build-classpath|grep -v "^\["`

#Need to add gwt-dev to the CLASSPATH
CLASSPATH_WITH_NEWLINES=`echo $MAVEN_CLASSPATH | sed s/':'/'\n'/g`

#Code in FormDesigner
CLASSPATH=src/:target/generated-sources/gwt/

#Code in SharedLib
CLASSPATH=$CLASSPATH:../SharedLib/src/main/java/:../SharedLib/target/generated-sources/gwt

for i in $CLASSPATH_WITH_NEWLINES
do
  CLASSPATH=$CLASSPATH:$i
  IS_GWT_USER=`echo $i | grep gwt-user`
  if [ $? -eq 0 ]
  then
    GWT_DEV=`echo $i | sed s/gwt-user/gwt-dev/g`
    CLASSPATH=$CLASSPATH:$GWT_DEV
  fi
done

#Run in debug mode
java -Xdebug -Xmx512m -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n -classpath $CLASSPATH com.google.gwt.dev.DevMode -startupUrl FormDesigner.html org.openxdata.designer.FormDesigner
