#!/bin/sh

#
# Copyright © 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
defaultJvmOpts=""

# Use the maximum available, or set MAX_FD != -1 to use that value.
maxFdLimit=""

##############################################################################
##
##  End of file
##
##############################################################################

# Attempt to set APP_HOME

# Resolve links: $0 may be a link
app_path="$0"

# Need this for daisy-chained symlinks.
while
    APP_HOME="${app_path%/*}"
    case "$APP_HOME" in
        '')
            APP_HOME=.
            ;;
    esac
    test -x "$APP_HOME"/../lib/gradle-wrapper.jar || {
        echo "ERROR: Gradle not found. Please set the GRADLE_HOME environment variable or add gradle to the PATH."
        exit 1
    }
    APP_NAME="${APP_HOME##*/}"
    APP_BASE_NAME="${APP_NAME:-gradle}"
    
    # Could not export APP_HOME
    export APP_HOME
    export APP_BASE_NAME

    # For Cygwin, ensure paths are in UNIX format before anything is done
    if $cygwin ; then
        [ -n "$APP_HOME" ] && APP_HOME=$(cygpath --path --unix "$APP_HOME")
    fi

    # If we're running in a directory without gradle wrapper jar, try to find it in standard locations
    if [ ! -r "$APP_HOME"/../lib/gradle-wrapper.jar ] ; then
        APP_HOME="$GRADLE_HOME"
        if [ ! -r "$APP_HOME"/lib/gradle-wrapper.jar ] ; then
            APP_HOME="$HOME/.gradle/wrapper/dists/gradle-8.0/re/gradle-8.0"
        fi
    fi

    # Try to find gradle wrapper jar in standard locations
    if [ ! -r "$APP_HOME"/lib/gradle-wrapper.jar ] ; then
        echo "ERROR: Gradle wrapper jar not found. Please check your Gradle installation."
        exit 1
    fi

    # For Cygwin, switch paths to Windows format before running java
    if $cygwin ; then
        [ -n "$APP_HOME" ] && APP_HOME=$(cygpath --path --windows "$APP_HOME")
    fi

    # Unset original variables to avoid polluting the environment
    unset APP_PATH
    unset APP_HOME_IS_SET
    unset DIR_NAME

    # Set JAVA_HOME or fall back to system default
    JAVA_HOME="${JAVA_HOME:-$(dirname $(dirname $(readlink -f $(which java))))}"
    
    # Try to find a Java executable
    if [ -x "$JAVA_HOME/bin/java" ] ; then
        javaexe="$JAVA_HOME/bin/java"
    elif [ -x "$(command -v java)" ] ; then
        javaexe="java"
    else
        echo "ERROR: Java not found. Please set the JAVA_HOME environment variable or add java to the PATH."
        exit 1
    fi

    # Execute Gradle
    exec "$javaexe" "${defaultJvmOpts[@]}" "-classpath" "$APP_HOME/lib/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
