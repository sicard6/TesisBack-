package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import model.DiagramPackage;
import readers.BPMNDiagramReader;

public class HandlerDiagramMaker implements HttpHandler{

	private DiagramPackage diagramPackage;

	private ObjectMapper mapper;


	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		System.out.println(exchange.getRequestMethod());

		//BODY
		String sss = exchange.getProtocol();
		System.out.println(sss);
		InputStream data = exchange.getRequestBody();

		File targetFile = new File("./models/model.xml");
		OutputStream outStream = new FileOutputStream(targetFile);

		byte[] buffer = new byte[8 * 1024];
		int bytesRead;
		bytesRead = data.read(buffer);
		while (bytesRead != -1) {
			outStream.write(buffer, 0, bytesRead);
			bytesRead = data.read(buffer);
			if(bytesRead == -1) {
			}
		} 		
		outStream.close();
		
		modifyFile("./models/model.xml");

		//DIAGRAM
		mapper = new ObjectMapper();


		BPMNDiagramReader diagramReader = new BPMNDiagramReader();
		diagramPackage = diagramReader.readDiagram("./models/model.xml");

		//RESPONSE
		byte [] response = "".getBytes();

		try {
			String json = mapper.writeValueAsString(diagramPackage);
			response = json.getBytes();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}


		exchange.sendResponseHeaders(200, response.length);
		OutputStream os = exchange.getResponseBody();
		os.write(response);
		os.close();
	}

	private void modifyFile(String string) throws FileNotFoundException, IOException {
		StringBuilder builder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(string)))) {
			String a = br.readLine();
			while (!a.contains("<")) {
				a = br.readLine();
			}

			while (a != null && !a.contains("WebKitFormBoundary")) {
				builder.append(a + "\n");
				a = br.readLine();
			}
		}
		try (PrintStream pr = new PrintStream(new File(string))) {
			pr.println(builder.toString());
			pr.flush();
		}
	}

}

