package edu.pnu.stem.binder;

import edu.pnu.stem.feature.core.*;
import edu.pnu.stem.feature.navigation.*;
import edu.pnu.stem.geometry.jts.Solid;
import net.opengis.gml.v_3_2_1.*;
import net.opengis.indoorgml.core.v_1_0.*;
import net.opengis.indoorgml.navigation.v_1_0.ObjectFactory;
import net.opengis.indoorgml.navigation.v_1_0.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

public class Convert2JaxbClass {
	static net.opengis.indoorgml.core.v_1_0.ObjectFactory indoorgmlcoreOF = new net.opengis.indoorgml.core.v_1_0.ObjectFactory();
	static net.opengis.gml.v_3_2_1.ObjectFactory gmlOF = new net.opengis.gml.v_3_2_1.ObjectFactory();
	static ObjectFactory indoorgmlnaviOF = new net.opengis.indoorgml.navigation.v_1_0.ObjectFactory();

	@SuppressWarnings("unchecked")
	public static CellSpaceType change2JaxbClass(IndoorGMLMap savedMap, CellSpace feature) throws JAXBException {

		CellSpaceType newFeature = indoorgmlcoreOF.createCellSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialBoundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialBoundedBy() != null) {
			for (int i = 0; i < feature.getPartialBoundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempCSB = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialBoundedByHref = feature.getPartialBoundedBy().get(i).getId();
				partialBoundedByHref = "#" + partialBoundedByHref;
				tempCSB.setHref(partialBoundedByHref);
				partialBoundedBy.add(tempCSB);
			}

			newFeature.setPartialboundedBy(partialBoundedBy);

		}

		if(feature.getLevel() != null) {
			List<String> lv = new ArrayList<>(feature.getLevel());
			newFeature.setLevel(lv);
		}

		// TODO setting Geometry 2D
		Geometry geom = feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static CellSpaceBoundaryType change2JaxbClass(IndoorGMLMap savedMap, CellSpaceBoundary feature) {
		CellSpaceBoundaryType newFeature = indoorgmlcoreOF.createCellSpaceBoundaryType();
		TransitionPropertyType duality = new TransitionPropertyType();
		newFeature.setId(feature.getId());

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
			newFeature.setId(feature.getId());
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		Geometry geom = feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry3D(polygonProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			} else if (geom instanceof LineString) {
				LineString l = (LineString) geom;
				LineStringType linestring = Convert2JaxbGeometry.Convert2LineStringType(l);
				JAXBElement<LineStringType> jaxbLineString = gmlOF.createLineString(linestring);
				CurvePropertyType lineProp = gmlOF.createCurvePropertyType();
				lineProp.setAbstractCurve(jaxbLineString);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF
						.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry2D(lineProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			}
		}

		return newFeature;
	}

	public static EdgesType change2JaxbClass(IndoorGMLMap savedMap, Edges p) throws JAXBException {
		EdgesType newFeature = indoorgmlcoreOF.createEdgesType();

		newFeature.setId(p.getId());

		List<TransitionMemberType> transitionMember = new ArrayList<TransitionMemberType>();

		if (p.getTransitionMember() != null) {
			for (int j = 0; j < p.getTransitionMember().size(); j++) {
				TransitionType tempTransition = change2JaxbClass(savedMap,
						(Transition) savedMap.getFeature(p.getTransitionMember().get(j).getId()));
				TransitionMemberType tempTM = indoorgmlcoreOF.createTransitionMemberType();
				tempTM.setTransition(tempTransition);
				transitionMember.add(tempTM);
			}
			newFeature.setTransitionMember(transitionMember);

		}

		// newFeature.setBoundedBy(feature.);
		return newFeature;
	}

	ExternalObjectReferenceType change2JaxbClass(ExternalObjectReference feature) {
		ExternalObjectReferenceType newFeature = new ExternalObjectReferenceType();
		newFeature.setUri(feature.getUri());

		return newFeature;
	}

	ExternalReferenceType change2JaxbClass(ExternalReference feature) {
		ExternalReferenceType newFeature = new ExternalReferenceType();
		newFeature.setExternalObject(change2JaxbClass(feature.externalObject));
		// TODO:change externalObjectReference

		return newFeature;
	}

	static public IndoorFeaturesType change2JaxbClass(IndoorGMLMap savedMap, IndoorFeatures feature)
			throws JAXBException {
		IndoorFeaturesType newFeature = new IndoorFeaturesType();
		newFeature.setId(feature.getId());

		if (feature.getBoundedBy() != null) {
			Envelope e = (Envelope) savedMap.getFeature(feature.getBoundedBy().getId());
			JAXBElement<EnvelopeType> jaxbEnvelope = gmlOF.createEnvelope(change2JaxbClass(savedMap, e));
			BoundingShapeType bs = gmlOF.createBoundingShapeType();
			bs.setEnvelope(jaxbEnvelope);
			newFeature.setBoundedBy(bs);
		}

		if (feature.getPrimalSpaceFeatures() != null) {
			PrimalSpaceFeatures p = (PrimalSpaceFeatures) savedMap.getFeature(feature.getPrimalSpaceFeatures().getId());
			PrimalSpaceFeaturesPropertyType pp = indoorgmlcoreOF.createPrimalSpaceFeaturesPropertyType();
			pp.setPrimalSpaceFeatures(change2JaxbClass(savedMap, p));
			newFeature.setPrimalSpaceFeatures(pp);
		}

		if (feature.getMultiLayeredGraph() != null) {
			MultiLayeredGraph m = (MultiLayeredGraph) savedMap.getFeature(feature.getMultiLayeredGraph().getId());
			MultiLayeredGraphPropertyType mp = indoorgmlcoreOF.createMultiLayeredGraphPropertyType();
			mp.setMultiLayeredGraph(change2JaxbClass(savedMap, m));
			newFeature.setMultiLayeredGraph(mp);
		}

		return newFeature;
	}

	static public EnvelopeType change2JaxbClass(IndoorGMLMap savedMap, Envelope feature) throws JAXBException {
		EnvelopeType newFeature = new EnvelopeType();
		Point low 	= (Point) feature.getLowerCorner();
		Point upper = (Point) feature.getUpperCorner();

		if (low != null) {
			PointType point = Convert2JaxbGeometry.Convert2PointType(low);
			newFeature.setLowerCorner(point.getPos());
		}
		if (upper != null) {
			PointType point = Convert2JaxbGeometry.Convert2PointType(upper);
			newFeature.setUpperCorner(point.getPos());
		}

		newFeature.setSrsName(feature.getSrsName());
		newFeature.setSrsDimension(feature.getSrsDimension());

		return newFeature;
	}

	private static MultiLayeredGraphType change2JaxbClass(IndoorGMLMap savedMap, MultiLayeredGraph feature)
			throws JAXBException {
		MultiLayeredGraphType newFeature = new MultiLayeredGraphType();
		newFeature.setId(feature.getId());

		List<SpaceLayersType> spaceLayers = new ArrayList<>();
		List<InterEdgesType> interEdges   = new ArrayList<>();

		if (feature.getSpaceLayers() != null) {
			for (int i = 0; i < feature.getSpaceLayers().size(); i++) {
				String tempId = feature.getSpaceLayers().get(i).getId();
				SpaceLayers tempSLs = (SpaceLayers) savedMap.getFeature(tempId);
				SpaceLayersType tempSL = change2JaxbClass(savedMap, tempSLs);
				spaceLayers.add(tempSL);
			}
			newFeature.setSpaceLayers(spaceLayers);
		}

		if (feature.getInterEdges() != null) {
			for (int i = 0; i < feature.getInterEdges().size(); i++) {
				InterEdges tempIEs = (InterEdges) savedMap.getFeature(feature.getInterEdges().get(i).getId());
				InterEdgesType tempIE = change2JaxbClass(savedMap, tempIEs);
				interEdges.add(tempIE);
			}
			newFeature.setInterEdges(interEdges);
		}

		return newFeature;
	}

	private static InterEdgesType change2JaxbClass(IndoorGMLMap savedMap, InterEdges feature) {
		InterEdgesType newFeature = indoorgmlcoreOF.createInterEdgesType();
		newFeature.setId(feature.getId());
		List<InterLayerConnectionMemberType> interLayerConnectionMember = new ArrayList<>();

		if (feature.getInterLayerConnectionMember() != null) {
			for (int i = 0; i < feature.getInterLayerConnectionMember().size(); i++) {
				InterLayerConnection tempILC = (InterLayerConnection) savedMap.getFeature(feature.getInterLayerConnectionMember().get(i).getId());
				InterLayerConnectionType temp = change2JaxbClass(savedMap, tempILC);
				InterLayerConnectionMemberType tempMember = indoorgmlcoreOF.createInterLayerConnectionMemberType();
				tempMember.setInterLayerConnection(temp);

				interLayerConnectionMember.add(tempMember);
			}
			newFeature.setInterLayerConnectionMember(interLayerConnectionMember);
		}

		return newFeature;
	}

	private static InterLayerConnectionType change2JaxbClass(IndoorGMLMap savedMap, InterLayerConnection feature) {
		InterLayerConnectionType newFeature = indoorgmlcoreOF.createInterLayerConnectionType();
		newFeature.setId(feature.getId());

		List<StatePropertyType> interConnects = new ArrayList<>();
		List<SpaceLayerPropertyType> connectedLayer = new ArrayList<>();

		if (feature.getInterConnects() != null) {
			StatePropertyType tempSP = indoorgmlcoreOF.createStatePropertyType();
			for (int i = 0; i < feature.getInterConnects().length; i++) {
				String href = feature.getInterConnects()[i].getId();
				href = "#" + href;
				tempSP.setHref(href);
				interConnects.add(tempSP);
			}
		}

		if (feature.getConnectedLayers() != null) {
			SpaceLayerPropertyType tempSLP = indoorgmlcoreOF.createSpaceLayerPropertyType();

			for (int i = 0; i < feature.getConnectedLayers().length; i++) {		
				if(feature.getConnectedLayers()[i]!=null) {
					String str= feature.getConnectedLayers()[i].getId();
					tempSLP.setHref("#" +str );
					connectedLayer.add(tempSLP);
				}
				else {
					System.out.println("Creating " + feature.getId() + " is fail.");
				}				
			}
		}
		if (feature.getTypeOfTopoExpression() != null) {
			newFeature.setTypeOfTopoExpression(feature.getTypeOfTopoExpression().type.toString());
		}

		newFeature.setConnectedLayers(connectedLayer);
		newFeature.setInterConnects(interConnects);

		return newFeature;
	}

	static SpaceLayersType change2JaxbClass(IndoorGMLMap savedMap, SpaceLayers feature) throws JAXBException {
		SpaceLayersType newFeature = new SpaceLayersType();
		newFeature.setId(feature.getId());

		List<SpaceLayerMemberType> spaceLayerMember = new ArrayList<SpaceLayerMemberType>();

		if (feature.getSpaceLayerMember() != null) {
			for (int i = 0; i < feature.getSpaceLayerMember().size(); i++) {
				String tempId = feature.getSpaceLayerMember().get(i).getId();
				SpaceLayer tempSL = (SpaceLayer) savedMap.getFeature(tempId);
				SpaceLayerType temp = change2JaxbClass(savedMap, tempSL);
				SpaceLayerMemberType tempSLMember = new SpaceLayerMemberType();
				tempSLMember.setSpaceLayer(temp);

				spaceLayerMember.add(tempSLMember);
			}
			newFeature.setSpaceLayerMember(spaceLayerMember);
		}

		return newFeature;
	}

	private static SpaceLayerType change2JaxbClass(IndoorGMLMap savedMap, SpaceLayer feature) throws JAXBException {
		SpaceLayerType newFeature = new SpaceLayerType();
		newFeature.setId(feature.getId());

		List<EdgesType> edgesTypeList = new ArrayList<>();
		List<NodesType> nodesTypeList = new ArrayList<>();

		for (int i = 0; i < feature.getNodes().size(); i++) {
			Nodes tempNodes = (Nodes) savedMap.getFeature(feature.getNodes().get(i).getId());
			NodesType tempNodesType = change2JaxbClass(savedMap, tempNodes);
			nodesTypeList.add(tempNodesType);
		}
		newFeature.setNodes(nodesTypeList);

		if (feature.getEdges() != null) {
			for (int i = 0; i < feature.getEdges().size(); i++) {
				Edges tempEdge = (Edges) savedMap.getFeature(feature.getEdges().get(i).getId());
				EdgesType tempEdgesType = change2JaxbClass(savedMap, tempEdge);
				edgesTypeList.add(tempEdgesType);
			}
			newFeature.setEdges(edgesTypeList);
		}

		return newFeature;
	}

	private static NodesType change2JaxbClass(IndoorGMLMap savedMap, Nodes feature) throws JAXBException {
		NodesType newFeature = new NodesType();
		newFeature.setId(feature.getId());

		List<StateMemberType> stateMemberTypeList = new ArrayList<StateMemberType>();

		if (feature.getStateMember() != null) {
			for (int i = 0; i < feature.getStateMember().size(); i++) {
				State tempState = (State) savedMap.getFeature(feature.getStateMember().get(i).getId());
				JAXBElement<StateType> jaxbState = indoorgmlcoreOF.createState(change2JaxbClass(savedMap, tempState));
				StateMemberType tempStateMember = indoorgmlcoreOF.createStateMemberType();
				tempStateMember.setState(jaxbState);
			
				stateMemberTypeList.add(tempStateMember);
			}

			newFeature.setStateMember(stateMemberTypeList);
		}

		return newFeature;
	}

	static PrimalSpaceFeaturesType change2JaxbClass(IndoorGMLMap savedMap, PrimalSpaceFeatures feature)
			throws JAXBException {
		PrimalSpaceFeaturesType newFeature = new PrimalSpaceFeaturesType();
		newFeature.setId(feature.getId());

		List<CellSpaceMemberType> cellSpaceMember = new ArrayList<CellSpaceMemberType>();
		List<CellSpaceBoundaryMemberType> cellSpaceBoundaryMember = new ArrayList<CellSpaceBoundaryMemberType>();

		if (feature.getCellSpaceMember() != null) {
			for (int i = 0; i < feature.getCellSpaceMember().size(); i++) {
				if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("GeneralSpace")) {
					GeneralSpace tempCellSpace = (GeneralSpace) savedMap.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempCellSpaceMember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempCellSpaceMember.setCellSpace(
							indoorgmlnaviOF.createGeneralSpace(change2JaxbClass(savedMap, tempCellSpace)));
					cellSpaceMember.add(tempCellSpaceMember);
				}
				else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("TransitionSpace")) {
					TransitionSpace tempCellSpace = (TransitionSpace) savedMap.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempCellSpaceMember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempCellSpaceMember.setCellSpace(
							indoorgmlnaviOF.createTransitionSpace(change2JaxbClass(savedMap, tempCellSpace)));
					cellSpaceMember.add(tempCellSpaceMember);
				}
				else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("ConnectionSpace")) {
					ConnectionSpace tempCellSpace = (ConnectionSpace) savedMap.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempCellSpaceMember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempCellSpaceMember.setCellSpace(
							indoorgmlnaviOF.createConnectionSpace(change2JaxbClass(savedMap, tempCellSpace)));
					cellSpaceMember.add(tempCellSpaceMember);
				}
				else if (feature.getCellSpaceMember().get(i).getClass().getSimpleName().equals("AnchorSpace")) {
					AnchorSpace tempCellSpace = (AnchorSpace) savedMap.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempCellSpaceMember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempCellSpaceMember.setCellSpace(
							indoorgmlnaviOF.createAnchorSpace(change2JaxbClass(savedMap, tempCellSpace)));
					cellSpaceMember.add(tempCellSpaceMember);
				}
				else {
					CellSpace tempCellSpace = (CellSpace) savedMap.getFeature(feature.getCellSpaceMember().get(i).getId());
					CellSpaceMemberType tempCellSpaceMember = indoorgmlcoreOF.createCellSpaceMemberType();
					tempCellSpaceMember.setCellSpace(
							indoorgmlcoreOF.createCellSpace(change2JaxbClass(savedMap, tempCellSpace)));
					cellSpaceMember.add(tempCellSpaceMember);
				}
			}
			newFeature.setCellSpaceMember(cellSpaceMember);
		}

		if (feature.getCellSpaceBoundaryMember() != null) {
			for (int i = 0; i < feature.getCellSpaceBoundaryMember().size(); i++) {
				if (feature.getCellSpaceBoundaryMember().get(i).getClass().getSimpleName().equals("AnchorBoundary")) {
					AnchorBoundary tempCellSpaceBoundary = (AnchorBoundary) savedMap.getFeature(feature.getCellSpaceBoundaryMember().get(i).getId());
					CellSpaceBoundaryMemberType tempCellSpaceBoundaryMember = indoorgmlcoreOF.createCellSpaceBoundaryMemberType();
					tempCellSpaceBoundaryMember.setCellSpaceBoundary(
							indoorgmlnaviOF.createAnchorBoundary(change2JaxbClass(savedMap, tempCellSpaceBoundary)));
					cellSpaceBoundaryMember.add(tempCellSpaceBoundaryMember);
				}
				else if (feature.getCellSpaceBoundaryMember().get(i).getClass().getSimpleName().equals("ConnectionBoundary")) {
					ConnectionBoundary tempCellSpaceBoundary = (ConnectionBoundary) savedMap.getFeature(feature.getCellSpaceBoundaryMember().get(i).getId());
					CellSpaceBoundaryMemberType tempCellSpaceBoundaryMember = indoorgmlcoreOF.createCellSpaceBoundaryMemberType();
					tempCellSpaceBoundaryMember.setCellSpaceBoundary(
							indoorgmlnaviOF.createConnectionBoundary(change2JaxbClass(savedMap, tempCellSpaceBoundary)));
					cellSpaceBoundaryMember.add(tempCellSpaceBoundaryMember);
				} else {
					CellSpaceBoundary tempCellSpaceBoundary = (CellSpaceBoundary) savedMap.getFeature(feature.getCellSpaceBoundaryMember().get(i).getId());
					CellSpaceBoundaryMemberType tempCellSpaceBoundaryMember = indoorgmlcoreOF.createCellSpaceBoundaryMemberType();
					tempCellSpaceBoundaryMember.setCellSpaceBoundary(
							indoorgmlcoreOF.createCellSpaceBoundary(change2JaxbClass(savedMap, tempCellSpaceBoundary)));
					cellSpaceBoundaryMember.add(tempCellSpaceBoundaryMember);
				}

			}
			newFeature.setCellSpaceBoundaryMember(cellSpaceBoundaryMember);
		}

		return newFeature;
	}

	static StateType change2JaxbClass(IndoorGMLMap savedMap, State feature) {
		StateType newFeature = new StateType();

		List<TransitionPropertyType> connects = new ArrayList<>();

		if (feature.getConnects() != null) {
			for (int i = 0; i < feature.getConnects().size(); i++) {
				TransitionPropertyType tempTransitionPropertyType = new TransitionPropertyType();
				String href = feature.getConnects().get(i).getId();
				href = "#" + href;
				tempTransitionPropertyType.setHref(href);
				connects.add(tempTransitionPropertyType);
			}
			newFeature.setConnects(connects);
		}

		Point geom = (Point) feature.getGeometry();
		if (geom != null) {
			PointType point = Convert2JaxbGeometry.Convert2PointType(geom);
			PointPropertyType pointProp = gmlOF.createPointPropertyType();
			pointProp.setPoint(point);
			newFeature.setGeometry(pointProp);
		}

		if (feature.getDuality() != null) {
			CellSpacePropertyType duality = indoorgmlcoreOF.createCellSpacePropertyType();
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		newFeature.setId(feature.getId());
		return newFeature;
	}

	static TransitionType change2JaxbClass(IndoorGMLMap savedMap, Transition feature) {
		TransitionType newFeature = new TransitionType();
		newFeature.setId(feature.getId());

		List<StatePropertyType> connects = new ArrayList<StatePropertyType>();

		if (feature.getConnects() != null) {
			for (int i = 0; i < feature.getConnects().length; i++) {
				StatePropertyType temp = indoorgmlcoreOF.createStatePropertyType();
				String href = feature.getConnects()[i].getId();
				href = "#" + href;
				temp.setHref(href);
				connects.add(temp);
			}

			newFeature.setConnects(connects);
		}

		LineString geom = (LineString) feature.getGeometry();
		if (geom != null) {
			LineStringType linestring = Convert2JaxbGeometry.Convert2LineStringType(geom);
			CurvePropertyType curveProperty = gmlOF.createCurvePropertyType();
			curveProperty.setAbstractCurve(gmlOF.createLineString(linestring));

			newFeature.setGeometry(curveProperty);
		}

		if (feature.getDuality() != null) {
			CellSpaceBoundaryPropertyType duality = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);

			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);

			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		newFeature.setWeight(feature.getWeight());

		return newFeature;

	}

	public static GeneralSpaceType change2JaxbClass(IndoorGMLMap savedMap, GeneralSpace feature) {
		GeneralSpaceType newFeature = indoorgmlnaviOF.createGeneralSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}
		if (feature.getClassType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getClassType());
			newFeature.setClazz(e);

		}
		if (feature.getFunctionType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getFunctionType());
			newFeature.setFunction(e);

		}
		if (feature.getUsageType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getUsageType());
			newFeature.setUsage(e);

		}
		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialBoundedBy = new ArrayList<CellSpaceBoundaryPropertyType>();

		if (feature.getPartialBoundedBy() != null) {
			for (int i = 0; i < feature.getPartialBoundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempCSB = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialBoundedByHref = feature.getPartialBoundedBy().get(i).getId();
				partialBoundedByHref = "#" + partialBoundedByHref;
				tempCSB.setHref(partialBoundedByHref);
				partialBoundedBy.add(tempCSB);
			}

			newFeature.setPartialboundedBy(partialBoundedBy);
		}

		// TODO setting Geometry 2D
		Geometry geom = feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static TransitionSpaceType change2JaxbClass(IndoorGMLMap savedMap, TransitionSpace feature) {

		TransitionSpaceType newFeature = indoorgmlnaviOF.createTransitionSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getClassType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getClassType());
			newFeature.setClazz(e);

		}

		if (feature.getFunctionType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getFunctionType());
			newFeature.setFunction(e);

		}

		if (feature.getUsageType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getUsageType());
			newFeature.setUsage(e);
		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialBoundedBy = new ArrayList<>();

		if (feature.getPartialBoundedBy() != null) {
			for (int i = 0; i < feature.getPartialBoundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempCSB = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialBoundedByHref = feature.getPartialBoundedBy().get(i).getId();
				partialBoundedByHref = "#" + partialBoundedByHref;
				tempCSB.setHref(partialBoundedByHref);
				partialBoundedBy.add(tempCSB);
			}

			newFeature.setPartialboundedBy(partialBoundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static ConnectionSpaceType change2JaxbClass(IndoorGMLMap savedMap, ConnectionSpace feature) {
		ConnectionSpaceType newFeature = indoorgmlnaviOF.createConnectionSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getClassType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getClassType());
			newFeature.setClazz(e);

		}

		if (feature.getFunctionType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getFunctionType());
			newFeature.setFunction(e);

		}

		if (feature.getUsageType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getUsageType());
			newFeature.setUsage(e);

		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialBoundedBy = new ArrayList<>();

		if (feature.getPartialBoundedBy() != null) {
			for (int i = 0; i < feature.getPartialBoundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempCSB = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialBoundedByHref = feature.getPartialBoundedBy().get(i).getId();
				partialBoundedByHref = "#" + partialBoundedByHref;
				tempCSB.setHref(partialBoundedByHref);
				partialBoundedBy.add(tempCSB);
			}

			newFeature.setPartialboundedBy(partialBoundedBy);

		}

		// TODO setting Geometry 2D
		Geometry geom = feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static AnchorSpaceType change2JaxbClass(IndoorGMLMap savedMap, AnchorSpace feature) {
		AnchorSpaceType newFeature = indoorgmlnaviOF.createAnchorSpaceType();
		StatePropertyType duality = new StatePropertyType();

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<CodeType>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		if (feature.getClassType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getClassType());
			newFeature.setClazz(e);

		}

		if (feature.getFunctionType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getFunctionType());
			newFeature.setFunction(e);

		}

		if (feature.getUsageType() != null) {
			CodeType e = new CodeType();
			e.setValue(feature.getUsageType());
			newFeature.setUsage(e);

		}

		newFeature.setId(feature.getId());

		List<CellSpaceBoundaryPropertyType> partialBoundedBy = new ArrayList<>();

		if (feature.getPartialBoundedBy() != null) {
			for (int i = 0; i < feature.getPartialBoundedBy().size(); i++) {
				CellSpaceBoundaryPropertyType tempCSB = indoorgmlcoreOF.createCellSpaceBoundaryPropertyType();
				String partialBoundedByHref = feature.getPartialBoundedBy().get(i).getId();
				partialBoundedByHref = "#" + partialBoundedByHref;
				tempCSB.setHref(partialBoundedByHref);
				partialBoundedBy.add(tempCSB);
			}

			newFeature.setPartialboundedBy(partialBoundedBy);
		}

		// TODO setting Geometry 2D
		Geometry geom = feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Solid) {
				Solid s = (Solid) geom;
				SolidType solid = Convert2JaxbGeometry.Convert2SolidType(s);
				JAXBElement<SolidType> jaxbSolid = gmlOF.createSolid(solid);
				SolidPropertyType solidProp = gmlOF.createSolidPropertyType();
				solidProp.setAbstractSolid(jaxbSolid);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry3D(solidProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			} else if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceGeometryType cellSpaceGeometryType = indoorgmlcoreOF.createCellSpaceGeometryType();
				cellSpaceGeometryType.setGeometry2D(polygonProp);

				newFeature.setCellSpaceGeometry(cellSpaceGeometryType);
			}
		}

		return newFeature;
	}

	public static AnchorBoundaryType change2JaxbClass(IndoorGMLMap savedMap, AnchorBoundary feature) {
		AnchorBoundaryType newFeature = indoorgmlnaviOF.createAnchorBoundaryType();
		TransitionPropertyType duality = new TransitionPropertyType();
		newFeature.setId(feature.getId());

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
			newFeature.setId(feature.getId());
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		Geometry geom = feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry3D(polygonProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			} else if (geom instanceof LineString) {
				LineString l = (LineString) geom;
				LineStringType linestring = Convert2JaxbGeometry.Convert2LineStringType(l);
				JAXBElement<LineStringType> jaxbLineString = gmlOF.createLineString(linestring);
				CurvePropertyType lineProp = gmlOF.createCurvePropertyType();
				lineProp.setAbstractCurve(jaxbLineString);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry2D(lineProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			}
		}

		return newFeature;
	}

	public static ConnectionBoundaryType change2JaxbClass(IndoorGMLMap savedMap, ConnectionBoundary feature) {
		ConnectionBoundaryType newFeature = indoorgmlnaviOF.createConnectionBoundaryType();
		TransitionPropertyType duality = new TransitionPropertyType();
		newFeature.setId(feature.getId());

		if (feature.getDuality() != null) {
			String href = feature.getDuality().getId();
			href = "#" + href;
			duality.setHref(href);
			newFeature.setDuality(duality);
			newFeature.setId(feature.getId());
		}

		if (feature.getName() != null) {
			List<CodeType> name = new ArrayList<>();
			CodeType e = new CodeType();
			e.setValue(feature.getName());
			name.add(e);
			newFeature.setName(name);
		}

		if (feature.getDescription() != null) {
			StringOrRefType e = new StringOrRefType();
			e.setValue(feature.getDescription());
			newFeature.setDescription(e);
		}

		Geometry geom = feature.getGeometry();
		if (geom != null) {

			if (geom instanceof Polygon) {
				Polygon p = (Polygon) geom;
				PolygonType polygon = Convert2JaxbGeometry.Convert2SurfaceType(p);
				JAXBElement<PolygonType> jaxbPolygon = gmlOF.createPolygon(polygon);
				SurfacePropertyType polygonProp = gmlOF.createSurfacePropertyType();
				polygonProp.setAbstractSurface(jaxbPolygon);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry3D(polygonProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			} else if (geom instanceof LineString) {
				LineString l = (LineString) geom;
				LineStringType linestring = Convert2JaxbGeometry.Convert2LineStringType(l);
				JAXBElement<LineStringType> jaxbLineString = gmlOF.createLineString(linestring);
				CurvePropertyType lineProp = gmlOF.createCurvePropertyType();
				lineProp.setAbstractCurve(jaxbLineString);

				CellSpaceBoundaryGeometryType cellSpaceBoundaryGeometryType = indoorgmlcoreOF.createCellSpaceBoundaryGeometryType();
				cellSpaceBoundaryGeometryType.setGeometry2D(lineProp);

				newFeature.setCellSpaceBoundaryGeometry(cellSpaceBoundaryGeometryType);
			}
		}

		return newFeature;
	}
}