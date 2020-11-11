package edu.pnu.stem.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.stem.api.exception.DocumentNotFoundException;
import edu.pnu.stem.api.exception.UndefinedDocumentException;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.binder.Unmashaller;
import edu.pnu.stem.feature.core.IndoorFeatures;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;

/**
 * @author Hyung-Gyu Ryoo (hyunggyu.ryoo@gmail.com, Pusan National University)
 *
 */

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/documents")
public class DocumentController {

	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createDocument(@PathVariable("id") String id,
							   HttpServletRequest request, HttpServletResponse response) {
		if(id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}

		IndoorGMLMap map = Container.getDocument(id);
		if(map != null) {
			System.out.println("This document ID is already exist: "+id);
		}
		map = Container.createDocument(id);

		String contentType = request.getContentType();
		if(contentType != null) {
			if(request.getContentType().contains("xml")) {
				// Importing IndoorGML Document
				try {
					InputStream xml 		= request.getInputStream();
					IndoorFeaturesType doc 	= Unmashaller.importIndoorGML(id, xml);
					IndoorFeatures savedDoc = edu.pnu.stem.binder.Convert2FeatureClass.change2FeatureClass(map, id, doc);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(request.getContentType().contains("json")) {
				//TODO : indoorJSON
			}
		}
				
		// Empty Document is made.
		response.setHeader("Location", request.getRequestURL().toString());
		System.out.println("Creating Document : " + id);
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.FOUND)
	public void getDocument(@PathVariable("id") String id,
							HttpServletRequest request, HttpServletResponse response) {
		//String type = json.get("type").asText().trim();
		File theDir = new File("temp");

		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    System.out.println("creating directory: " + theDir.getName());
		    boolean result = false;

		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }

		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}

		IndoorGMLMap map = Container.getDocument(id);
		if(map != null) {
			map.Marshall("temp/" + id + ".igml");
			try {
				response.setContentType("text/xml;charset=UTF-8");
				PrintWriter    out 		= response.getWriter();
				File 		   file 	= new File("temp/" + id + ".igml");
				BufferedReader reader 	= new BufferedReader(new FileReader (file));
				String         ls 		= System.getProperty("line.separator");
				String         line		= "";
				StringBuilder  stringBuilder = new StringBuilder();

				while((line = reader.readLine()) != null) {
					stringBuilder.append(line);
					stringBuilder.append(ls);
				}
				reader.close();

				String content = stringBuilder.toString();
				out.write(content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new DocumentNotFoundException();
		}

		System.out.println("Document is created : " + id);
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteDocument(@PathVariable("id") String id,
							   HttpServletRequest request, HttpServletResponse response) {
		try {
			IndoorGMLMap map = Container.getDocument(id);
			if(map != null) {
				//	map.clearMap();
				Container.removeDocument(id);
			}
		}
		catch(NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}
}
