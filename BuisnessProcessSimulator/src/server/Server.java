package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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
		JFileChooser fileChooser  = new JFileChooser("../");
		fileChooser.setFileFilter(filter);
		fileChooser.showOpenDialog(null);
		String r = fileChooser.getSelectedFile().getAbsolutePath();
		HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
		HttpContext context = server.createContext("/prueba", new HandlerPrueba(r));
		server.setExecutor(null); // creates a default executor
		server.start();

	}

	public class HandlerPrueba implements HttpHandler{
		
		private DiagramPackage diagramPackage;
		
		private ObjectMapper mapper;
		
		public HandlerPrueba(String route) {
			
			mapper = new ObjectMapper();
			
			
			BPMNDiagramReader diagramReader = new BPMNDiagramReader();
			diagramPackage = diagramReader.readDiagram(route);

		}

		@Override
		public void handle(HttpExchange t) throws IOException {
			
			byte [] response = "Welcome Real's HowTo test page".getBytes();
			
			try {
				String json = mapper.writeValueAsString(diagramPackage);
				System.out.println(json);
				response = json.getBytes();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			t.sendResponseHeaders(200, response.length);
			OutputStream os = t.getResponseBody();
			os.write(response);
			os.close();

		}

	}

}
