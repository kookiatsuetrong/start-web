/**
 * Bobcat is an embedded version of Tomcat.
 * 
 * Example of usage:
 * 
 * java Bobcat --home web --port 1558
 *
 */
import java.io.File;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.connector.Connector;

class Bobcat {
	public static void main(String[] data) {
		double k = 80;
		int port = 12345;
		String home = "web";
		String working = "working";
		boolean verbose = false;
		
		for (int i = 0; i < data.length; i++) {
			if ("--port".equals(data[i])) {
				i++;
				port = Integer.parseInt(data[i]);
			}
			if ("--home".equals(data[i])) {
				i++;
				home = data[i];
			}
			if ("--verbose".equals(data[i])) {
				verbose = true;
			}
		}

		try {
			Tomcat tomcat = new Tomcat();
			tomcat.setPort(port);
			tomcat.setBaseDir(working);
			tomcat.setSilent(!verbose);
			tomcat.setAddDefaultWebXmlToWebapp(true);
			Connector connector = tomcat.getConnector();   // Mandatory
			File file = new File(home);
			Context c = tomcat.addWebapp("", file.getAbsolutePath());
			// c.setAltDDName(file.getAbsolutePath() + "/../web.xml");
			
			tomcat.start();
			tomcat.getServer().await();
		} catch (Exception e) {
			System.out.println("ERROR " + e);
		}
		
		// try {
		// tomcat.stop();
		// tomcat.destroy();
		// remove the temporary? check the uploaded file before
		// } catch (Exception e) { }
	}
}

// --port 12345
// --home web
// --war sample.war
