package network;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.PointFeatureFactory;
import org.matsim.core.utils.gis.PolylineFeatureFactory;
import org.matsim.core.utils.gis.ShapeFileWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.locationtech.jts.geom.Coordinate;


import scenarioTranslation.SantiagoScenarioConstants;

public class ConvertNet2Shape {
	
	private static Logger log = Logger.getLogger(ConvertNet2Shape.class);
	private static String crs = SantiagoScenarioConstants.toCRS;
	
	private static String inputDir = "../../Desktop/Devs/MATSim_EV_Scenarios/v2a/Santiago/input/";
	private static String MATSimNetworkDir = inputDir + "network_merged_cl.xml.gz";
	private static String outputDir = inputDir + "shapes/";

	
	
	public static void main(String[] args) {
		
		
		Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
		new MatsimNetworkReader(scenario.getNetwork()).readFile(MATSimNetworkDir);
		Network network = (Network) scenario.getNetwork();
		
	    File directory = new File(outputDir);
	    if (! directory.exists()){
			createDir(new File(outputDir));		
	    }


		Collection<SimpleFeature> features = new ArrayList<SimpleFeature>();
		PolylineFeatureFactory linkFactory = new PolylineFeatureFactory.Builder().
				setCrs(MGC.getCRS(crs)).
				setName("link").
				addAttribute("ID", String.class).
				addAttribute("fromID", String.class).
				addAttribute("toID", String.class).
				addAttribute("length", Double.class).
				addAttribute("type", String.class).
				addAttribute("capacity", Double.class).
				addAttribute("freespeed", Double.class).
				create();

		for (Link link : network.getLinks().values()) {
			Coordinate fromNodeCoordinate = new Coordinate(link.getFromNode().getCoord().getX(), link.getFromNode().getCoord().getY());
			Coordinate toNodeCoordinate = new Coordinate(link.getToNode().getCoord().getX(), link.getToNode().getCoord().getY());
			Coordinate linkCoordinate = new Coordinate(link.getCoord().getX(), link.getCoord().getY());
			Coordinate[] coordArray = new Coordinate[] {fromNodeCoordinate, linkCoordinate, toNodeCoordinate};
			Object[] attributeValues = new Object [] {link.getId().toString(), link.getFromNode().getId().toString(),link.getToNode().getId().toString(), link.getLength(), NetworkUtils.getType(((Link)link)), link.getCapacity(), link.getFreespeed()}; 
			String idLink = link.getId().toString();
			SimpleFeature ft = linkFactory.createPolyline(coordArray,attributeValues, idLink);
			features.add(ft);
		}
		
		ShapeFileWriter.writeGeometries(features, outputDir + "santiago_network_links.shp");

		features.clear();

		PointFeatureFactory nodeFactory = new PointFeatureFactory.Builder().
				setCrs(MGC.getCRS(crs)).
				setName("nodes").
				addAttribute("ID", String.class).
				create();

		for (Node node : network.getNodes().values()) {
			SimpleFeature ft = nodeFactory.createPoint(node.getCoord(), new Object[] {node.getId().toString()}, null);
			features.add(ft);
		}
		ShapeFileWriter.writeGeometries(features, outputDir + "santiago_network_nodes.shp");

	}
	private static void createDir(File file) {
		log.info("Directory " + file + " created: "+ file.mkdirs());	
	}
}

