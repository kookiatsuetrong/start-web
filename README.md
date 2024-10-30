# framework
Java framework for web application and web service





### Configuration file
The configuration is the file "setup.txt" file.

This program needs an SMTP service,
but it is not easy to setup. So the configuration
file (setup.txt) can disable this feature
by writing "emailEnable = false".

Sample of configuration file.
```
emailEnable   = true
emailPassword = Hello12345
emailAddress  = support@sample.com
emailServer   = smtp.sample.com
emailSender   = Sender Name
emailPort     = 587
emailSecurity = TLSv1.2
```


### Forward versus Redirect

```
Context.forward() reserves HTTP verb.
Context.redirect() sends HTTP 301 code.
```
