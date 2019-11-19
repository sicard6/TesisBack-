package server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.StandardCopyOption;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import model.DiagramPackage;
import readers.BPMNDiagramReader;

public class Server {

	public static void main(String[] args) {

		try {
			new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Server() throws IOException {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"xml Files", "xml");
		HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
		HttpContext context2 = server.createContext("/MakeDiagram", new HandlerDiagramMaker());
		server.setExecutor(null); // creates a default executor
		server.start();

	}
	
	

}
