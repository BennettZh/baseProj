@echo off
cd /d %~dp0
md src
java -jar mybatis-generator-core-1.3.2.jar -configfile generatorConfig.xml -overwrite
echo Please refresh this folder to check the generated files.
pause