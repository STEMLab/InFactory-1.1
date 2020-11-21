package edu.pnu.stem.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.pnu.stem.api.exception.UndefinedDocumentException;
import edu.pnu.stem.binder.Convert2Json;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.dao.IndoorFeaturesDAO;
import edu.pnu.stem.feature.core.IndoorFeatures;

/**
 * @author Hyung-Gyu Ryoo (hyunggyu.ryoo@gmail.com, Pusan National University)
 *
 */
@RestController
@RequestMapping("/documents/{docId}/indoorfeatures")
public class IndoorFeaturesController {
	//private static final Logger Logger = LoggerFactory.getLogger(IndoorFeaturesController.class);

	@PostMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createIndoorFeatures(@PathVariable("docId") String docId, @PathVariable("id") String id,
									 @RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		String name 				= null;
		String description 			= null;
		String envelope 			= null;
		String multilayeredgraph 	= null;
		String primalspacefeatures 	= null;
			
		if(id == null || id.isEmpty()) {
			id = UUID.randomUUID().toString();
		}
	
		if(json.has("properties")) {
			if(json.get("properties").has("name")) {
				name = json.get("properties").get("name").asText().trim();
			}
			if(json.get("properties").has("description")) {
				description = json.get("properties").get("description").asText().trim();
			}
			if(json.get("properties").has("multiLayeredGraph")) {
				multilayeredgraph = json.get("properties").get("multiLayeredGraph").asText().trim();
			}
			if(json.get("properties").has("primalSpaceFeatures")) {
				primalspacefeatures = json.get("properties").get("primalSpaceFeatures").asText().trim();
			}
			if(json.get("properties").has("envelope")) {
				envelope = json.get("properties").get("envelope").asText().trim();
			}
		}

		IndoorFeatures f;
		try {
			IndoorGMLMap map = Container.getDocument(docId);
			f = IndoorFeaturesDAO.createIndoorFeatures(map, id, name, description, envelope, multilayeredgraph, primalspacefeatures);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}

		response.setHeader("Location", request.getRequestURL().append(f.getId()).toString());
	}
	
	@GetMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.FOUND)
	public void getIndoorFeatures(@PathVariable("docId") String docId,@PathVariable("id") String id,
								  HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			IndoorGMLMap map = Container.getDocument(docId);
			assert map != null;
			ObjectNode target = Convert2Json.convert2JSON(map, IndoorFeaturesDAO.readIndoorFeatures(map, id));

			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(target);
			out.flush();
		}catch(NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteIndoorFeatures(@PathVariable("docId") String docId,@PathVariable("id") String id,
									 @RequestBody ObjectNode json, HttpServletRequest request, HttpServletResponse response) {
		try {
			IndoorGMLMap map = Container.getDocument(docId);
			assert map != null;
			IndoorFeaturesDAO.deleteIndoorFeatures(map, id);
		}
		catch(NullPointerException e) {
			e.printStackTrace();
			throw new UndefinedDocumentException();
		}
	}
}
