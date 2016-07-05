cd /d %~dp0
md src
java -jar mybatis-generator-core-1.3.2.jar -configfile generatorConfig.xml -overwrite
pause