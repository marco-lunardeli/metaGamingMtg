@echo off
rem -----------------------------------------------------------------------------
rem Gradle startup script for Windows
rem -----------------------------------------------------------------------------

set DEFAULT_JVM_OPTS=-Xmx1024m
set APP_BASE_NAME=%~n0

if not defined JAVA_HOME goto findJavaFromPath
set RUNJAVABIN=%JAVA_HOME%\bin\java.exe
if exist "%RUNJAVABIN%" goto init

:findJavaFromPath
set RUNJAVABIN=java

:init
set CLASSPATH=%~dp0gradle\wrapper\gradle-wrapper.jar
"%RUNJAVABIN%" %DEFAULT_JVM_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
