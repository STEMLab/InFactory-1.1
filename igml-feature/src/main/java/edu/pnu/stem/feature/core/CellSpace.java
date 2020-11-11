package edu.pnu.stem.feature.core;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Geometry;

import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.util.GeometryUtil;

/**
 * @author jungh Implements CellSpaceType of IndoorGML 1.0.3
 */
public class CellSpace extends AbstractFeature {
	
	/**
	 * value of geometry of feature
	 */
	private String geometry;

	/**
	 * boundary of the CellSpace
	 */
	private List<String> partialBoundedBy;
	/**
	 * value of State which has duality relationship with the CellSpace
	 */
	private String duality;
	/**
	 * If External Reference of the feature is exist, then set this.
	 */
	private ExternalReference externalReference;

	/**
	 * ID of parent feature instance.
	 */
	private String parentId;
	
	private List<String> level;

	public CellSpace(IndoorGMLMap doc, String id){
		super(doc, id);		
		partialBoundedBy = new ArrayList<>();
	}

	public PrimalSpaceFeatures getParent() {
		PrimalSpaceFeatures feature = (PrimalSpaceFeatures) indoorGMLMap.getFeature(this.parentId);
		if(feature == null) {
			feature = (PrimalSpaceFeatures) indoorGMLMap.getFutureFeature(this.parentId);
		}

		return feature;
	}

	public Geometry getGeometry() {
		Geometry feature = null;

		if(this.geometry != null){
			feature = indoorGMLMap.getFeature4Geometry(this.geometry);
		}

		return feature;
	}
	
	public void setGeometry(Geometry geom) {
		String gId = GeometryUtil.getMetadata(geom, "id");

		Geometry found = indoorGMLMap.getFeature4Geometry(gId);
		if(found == null) {
			indoorGMLMap.setFeature4Geometry(gId, geom);
		}
		this.geometry = gId;
	}

	public boolean hasDuality() {
		return this.duality != null;
	}
	
	public void resetDuality() {
		this.duality = null;
	}
	
	public State getDuality() {
		State feature = null;

		if (hasDuality()) {
			feature = (State) indoorGMLMap.getFeature(this.duality);
			if(feature == null) {
				if(indoorGMLMap.hasFutureID(this.duality)) {
					feature = (State) indoorGMLMap.getFutureFeature(this.duality);
				}
			}
		}

		return feature;
	}
	
	public void setDuality(State s) {
		State found = (State) indoorGMLMap.getFeature(s.getId());

		if(found == null) {
			if(!indoorGMLMap.hasFutureID(s.getId())){
				indoorGMLMap.setFutureFeature(s.getId(), s);
			}
		}

		this.duality = s.getId();
	}

	public List<CellSpaceBoundary> getPartialBoundedBy() {
		List<CellSpaceBoundary> cellBoundaries = new ArrayList<>();

		if (this.partialBoundedBy == null)
			throw new AssertionError();
		if(this.partialBoundedBy.size() != 0){
			for (String s : this.partialBoundedBy) {
				CellSpaceBoundary found = (CellSpaceBoundary) indoorGMLMap.getFeature(s);
				if(found == null)
					found = (CellSpaceBoundary) indoorGMLMap.getFutureFeature(s);

				cellBoundaries.add(found);
			}
		}
		
		return cellBoundaries;
	}

	public void setPartialBoundedBy(List<CellSpaceBoundary> csbList) {
		this.partialBoundedBy = new ArrayList<>();

		for(CellSpaceBoundary cb : csbList){
			cb.setCellSpace(this);
			CellSpaceBoundary found;
			found = (CellSpaceBoundary)indoorGMLMap.getFeature(cb.getId());
			if(found == null){
				indoorGMLMap.setFutureFeature(cb.getId(), cb);
			}			
			if(!this.partialBoundedBy.contains(cb.getId())){
				this.partialBoundedBy.add(cb.getId());
			}
		}
	}
	
	public void addPartialBoundedBy(CellSpaceBoundary cb) {
		if(!this.partialBoundedBy.contains(cb.getId())){
			this.partialBoundedBy.add(cb.getId());
			indoorGMLMap.setFeature(cb.getId(), "CellSpaceBoundary", cb);
		}
	}
	
	public void resetPartialBoundedBy() {
		this.partialBoundedBy = null;
	}

	public ExternalReference getExternalReference() {
		return this.externalReference;
	}

	public void setExternalReference(ExternalReference e) {
		this.externalReference = e;
	}
	
	public void setParent(PrimalSpaceFeatures parent) {
		PrimalSpaceFeatures found = (PrimalSpaceFeatures)indoorGMLMap.getFeature(parent.getId());
		if(found == null){
			indoorGMLMap.setFutureFeature(parent.getId(), parent);
		}

		this.parentId = parent.getId();		
	}

	public void resetParent() {
		this.parentId = null;		
	}
	
	public void deletePartialBoundedBy(CellSpaceBoundary cb) {
		if(this.partialBoundedBy != null)
			this.partialBoundedBy.remove(cb.getId());
	}
	
	public void setLevel(List<String> level) {
		this.level = new ArrayList<>();
		this.level.addAll(level);
	}
	
	public void addLevel(String level) {
		this.level.add(level);
	}

	public List <String> getLevel() {
		List<String> level = new ArrayList<>();
		if (this.level != null && this.level.size() != 0){
			level.addAll(this.level);
		}		
		
		return level;
	}
}
