#!/bin/sh
cygwin=false
darwin=false
os400=false
case "`uname`" in
CYGWIN*) cygwin=true;;
Darwin*) darwin=true;;
OS400*) os400=true;;
esac

# resolve links - $0 may be a softlink
PRG="$0"
while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set APPLIATION_HOME if not already set
[ -z "$APPLIATION_HOME" ] && APPLIATION_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

# Copy APPLIATION_BASE from APPLIATION_HOME if not already set
[ -z "$APPLIATION_BASE" ] && APPLIATION_BASE="$APPLIATION_HOME"

# reset user defined classpath to avoid java library conflict
# but allow them to be specified in setenv.sh, in rare case when it is needed.
CLASSPATH=

if [ -r "$APPLIATION_BASE/bin/setenv.sh" ]; then
  . "$APPLIATION_BASE/bin/setenv.sh"
elif [ -r "$APPLIATION_BASE/bin/setenv.sh" ]; then
  . "$APPLIATION_BASE/bin/setenv.sh"
fi

# Get standard Java environment variables
if $os400; then
  # -r will Only work on the os400 if the files are:
  # 1. owned by the user
  # 2. owned by the PRIMARY group of the user
  # this will not work if the user belongs in secondary groups
  . "$APPLICATION_HOME"/bin/setjava.sh
else
  if [ -r "$APPLICATION_HOME"/bin/setjava.sh ]; then
    . "$APPLICATION_HOME"/bin/setjava.sh
  else
    echo "Cannot find $APPLICATION_HOME/bin/setclasspath.sh"
    echo "This file is needed to run this program"
    exit 1
  fi
fi

# Bugzilla 37848: When no TTY is available, don't output to console
have_tty=0
if [ "`tty`" != "not a tty" ]; then
    have_tty=1
fi



# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  JAVA_HOME=`cygpath --absolute --windows "$JAVA_HOME"`
  JRE_HOME=`cygpath --absolute --windows "$JRE_HOME"`
  APPLICATION_HOME=`cygpath --absolute --windows "$APPLICATION_HOME"`
  APPLICATION_BASE=`cygpath --absolute --windows "$APPLICATION_BASE"`
  APPLICATION_TMPDIR=`cygpath --absolute --windows "$APPLICATION_TMPDIR"`
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  JAVA_ENDORSED_DIRS=`cygpath --path --windows "$JAVA_ENDORSED_DIRS"` 
fi







