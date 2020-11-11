package edu.pnu.stem;

import edu.pnu.stem.api.Container;
import edu.pnu.stem.binder.Convert2JaxbClass;
import edu.pnu.stem.binder.IndoorGMLMap;
import edu.pnu.stem.binder.Marshaller;
import edu.pnu.stem.binder.UnMarshaller;
import edu.pnu.stem.feature.core.IndoorFeatures;
import junit.framework.TestCase;
import net.opengis.indoorgml.core.v_1_0.IndoorFeaturesType;

public class testApp extends TestCase {
	public void testConverter(){
		try {
			IndoorGMLMap map = Container.createDocument("test");
			IndoorFeaturesType doc = UnMarshaller.importIndoorGML("test","src/test/resources/FJK_1_0_3.gml");
			IndoorFeatures savedDoc = edu.pnu.stem.binder.Convert2FeatureClass.change2FeatureClass(map,"test", doc);
			Marshaller.marshalIndoorFeatures(null, Convert2JaxbClass.change2JaxbClass(map,savedDoc));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
