import start.web.Server;
import start.web.Context;
import java.util.Map;
import jakarta.json.JsonObject;

class Another {
	
	void start() {
		var server = Server.getInstance();
		server.handle("/another", () -> "Another Web Page");
	}
}

/*
// Additional Idea

import static start.web.Server.*;

void main() {
	handle("/sample").by( () -> "Welcome to Vetala" );
}

*/