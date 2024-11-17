javac --enable-preview --source 23 ^
-classpath "code;runtime/*;runtime" code/*.java

move code\*.class runtime

javac -classpath "code;runtime/*;runtime" code/start/web/*.java

erase temporary\start\web\*.class
move code\start\web\*.class temporary\start\web

jar cf runtime\start.jar -C temporary .

java -classpath ".;runtime;runtime/*" ^
Bobcat --home web --port 7300
