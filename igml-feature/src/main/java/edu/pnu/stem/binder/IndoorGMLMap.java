package edu.pnu.stem.binder;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.JAXBException;
import org.locationtech.jts.geom.Geometry;
import edu.pnu.stem.feature.core.IndoorFeatures;
import edu.pnu.stem.util.GeometryUtil;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;

public class IndoorGMLMap implements Serializable {
	protected ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> collection;
	private String docId;
	
	public IndoorGMLMap() {
		this.collection = new ConcurrentHashMap<>();
		setFeatureClassContainer();
	}

	private void setFeatureClassContainer() {
		collection.put("ID", new ConcurrentHashMap<>());
		collection.put("FutureID", new ConcurrentHashMap<>());
		// IndoorGML Core Module
		collection.put("IndoorFeatures", new ConcurrentHashMap<>());
		collection.put("MultiLayeredGraph", new ConcurrentHashMap<>());
		collection.put("PrimalSpaceFeatures", new ConcurrentHashMap<>());
		collection.put("CellSpace", new ConcurrentHashMap<>());
		collection.put("CellSpaceBoundary", new ConcurrentHashMap<>());
		collection.put("SpaceLayers", new ConcurrentHashMap<>());
		collection.put("SpaceLayer", new ConcurrentHashMap<>());
		collection.put("Nodes", new ConcurrentHashMap<>());
		collection.put("Edges", new ConcurrentHashMap<>());
		collection.put("Transition", new ConcurrentHashMap<>());
		collection.put("InterLayerConnection", new ConcurrentHashMap<>());
		collection.put("InterEdges", new ConcurrentHashMap<>());
		collection.put("CellSpaceGeometry", new ConcurrentHashMap<>());
		collection.put("State", new ConcurrentHashMap<>());
		collection.put("Reference", new ConcurrentHashMap<>());
		collection.put("Envelope", new ConcurrentHashMap<>());
		collection.put("Geometry", new ConcurrentHashMap<>());
		// IndoorGML Navi Module
		collection.put("GeneralSpace", new ConcurrentHashMap<>());
		collection.put("TransitionSpace", new ConcurrentHashMap<>());
		collection.put("ConnectionSpace", new ConcurrentHashMap<>());
		collection.put("AnchorSpace", new ConcurrentHashMap<>());
		collection.put("ConnectionBoundary", new ConcurrentHashMap<>());
		collection.put("AnchorBoundary", new ConcurrentHashMap<>());
	}
	
	public void clearMap() {
		collection.clear();
	}

	public boolean hasID(String id) {
		boolean flag = false;

		ConcurrentHashMap<String, Object> idContainer = getFeatureContainer("ID");
		if (idContainer.containsKey(id)) {
			flag = true;
		}

		return flag;
	}
	
	public boolean hasFutureID(String id){
		boolean flag = false;

		ConcurrentHashMap<String, Object> idContainer = getFeatureContainer("FutureID");
		if (idContainer.containsKey(id)) {
			flag = true;
		}

		return flag;
	}
	
	public void setFutureFeature(String id, String featureName){
		if(!hasID(id)){
			collection.get("FutureID").put(id, featureName);
			//System.out.println("Do not forget to create the feature id : "+id+" later : " + featureName);
		}
	}
	
	public void setFutureFeature(String id, Object feature){				
		collection.get("FutureID").put(id, feature);
		//System.out.println("Do not forget to create the feature id : "+id+" later");
	}
	
	private void setID(String id, String featureName) {
		if(!hasID(id)){
			getFeatureContainer("ID").put(id, featureName);
		}
	}
	
	public void removeFeature(String id) {
		if(hasID(id)) {
			String featureName = (String)collection.get("ID").get(id);
			collection.get(featureName).remove(id);
			removeID(id);
		}
	}
	
	public void removeFutureID(String id){
		getFeatureContainer("FutureID").remove(id);
	}
	
	private void removeID(String id){
		getFeatureContainer("ID").remove(id);
	}
	
	public String getFeatureNameFromID(String id) {
		ConcurrentHashMap<String, Object> idContainer = getFeatureContainer("id");
		return (String) idContainer.get(id);
	}
	
	public ConcurrentHashMap<String, Object> getFeatureContainer(String featureName) {
		ConcurrentHashMap<String, Object> newFeatureContainer = null;

		if (collection.containsKey(featureName)) {
			newFeatureContainer = collection.get(featureName);
		}

		return newFeatureContainer;
	}
	
	public Object getFeature(String id){
		Object newFeature = null;

		if(hasID(id)){
			String typeName = (String) getFeatureContainer("ID").get(id);
			newFeature = collection.get(typeName).get(id);
		} else {
			//TODO Exception
		}

		return newFeature;
	}
	
	public Object getFutureFeature(String id){
		Object newFeature = null;

		if(hasFutureID(id)){
			newFeature = getFeatureContainer("FutureID").get(id);
		}
		else{
			//TODO : Exception
		}

		return newFeature;
	}
	
	
	// TODO : Merge those two functions with feature setter and getter
	public Geometry getFeature4Geometry(String id){
		ConcurrentHashMap<String,Object> geomContainer = collection.get("Geometry");
		Geometry geom = null;

		if(geomContainer.containsKey(id)){
			geom = (Geometry)geomContainer.get(id);
		}
		else{
			//TODO Exception
		}

		return geom;
	}
	public void setFeature4Geometry(String id, Geometry geom){
		GeometryUtil.setMetadata(geom, "id", id);
		ConcurrentHashMap<String,Object> geomContainer = collection.get("Geometry");

		if(!geomContainer.containsKey(id)){
			geomContainer.put(id, geom);
		}
		else{
			//TODO Exception
		}
	}
	
	public void setFeature(String id, String featureName, Object featureValue){
		if(!hasID(id)){
			if(hasFutureID(id)){
				//System.out.println("Create feature from Future feature list : "+id);
				collection.get("FutureID").remove(id);
			}
			setID(id,featureName);
			collection.get(featureName).put(id, featureValue);
			System.out.println("Create feature : "+id + " which type is :"+featureName);
		}
		else{
			System.out.println("Already Exist Id : " + id+", Feature :" + featureName );			
			//container.get(featureName).put(id, featureValue);
		}
	}
	
	public void setReference(String id){
		if(hasID(id)){
			ConcurrentHashMap<String, Object> referenceContainer = getFeatureContainer("Reference");
			if(!referenceContainer.containsKey(id)){
				referenceContainer.put(id, 1);
			}
			else{
				Integer count =(Integer) referenceContainer.get(id);
				count = count+1;
				referenceContainer.remove(id);
				referenceContainer.put(id, count);
			}
		}
	}

	public void setDocId(String id) {
		this.docId = id;		
	}

	public String getDocId(){
		return this.docId;
	}
	
	public void Marshall(String path) {
		Enumeration<Object> fe = collection.get("IndoorFeatures").elements();
		IndoorFeatures features;
		if(fe.hasMoreElements()) {
			features = (IndoorFeatures) fe.nextElement();

			IndoorFeaturesType resultDoc;
			try {
				resultDoc = edu.pnu.stem.binder.Convert2JaxbClass.change2JaxbClass(this, features);
				System.out.println(resultDoc.getId());
				Mashaller.marshalIndoorFeatures(path, resultDoc);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//TODO Exception
		}
	}
}
