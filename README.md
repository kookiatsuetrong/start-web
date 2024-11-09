# framework
Java framework for web application and web service

```
1. Clone the project
   git clone https://github.com/kookiatsuetrong/start-web
   cd start-web

2. Create the configuration file (setup.txt)
   echo "emailEnabled = false" > setup.txt

3. Execute database schema (schema.sql)
   sudo mysql < schema.sql

4. Build the system
   bash build.sh

5. Open web browser to http://localhost:7300
```

### Configuration file

The configuration is the file "setup.txt".

```
emailEnabled  = true
emailPassword = Hello12345
emailAddress  = support@sample.com
emailServer   = smtp.sample.com
emailSender   = Sender Name
emailPort     = 587
emailSecurity = TLSv1.2
```

The transaction email can be disable by
writing "emailEnabled = false".


### Sample Code
```java
import start.web.Server;

class Sample {
	
	void start() {
		var server = Server.getInstance();
		server.handle("/another", () -> "Another Web Page");
	}
	
}
```

### Forward versus Redirect

```
Context.forward() reserves HTTP verb.
Context.redirect() sends HTTP 301 code.
```
