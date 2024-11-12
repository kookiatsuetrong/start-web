javac --enable-preview --source 23 \
-classpath "code:runtime/*:runtime" code/*.java

mv code/*.class runtime

javac -classpath "code:runtime/*:runtime" code/start/web/*.java

rm temporary/start/web/*.class
mv code/start/web/*.class temporary/start/web

jar cf runtime/start.jar -C temporary .

java -classpath ".:runtime:runtime/*" \
Bobcat --home web --port 7300
