package runSantiagoTests;

import java.io.BufferedWriter;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

public class SantiagoTravelDistancesAnalysis {
	
	private String runDir;	
	private List<Id<Person>> stuckAgents;

	public SantiagoTravelDistancesAnalysis(String baseFolderPath, List<Id<Person>> stuckAgents) {		
		this.runDir = baseFolderPath;
		this.stuckAgents=stuckAgents;
	}

	private void createDir(File file) {
		file.mkdirs();	
	}

	public void writeFileForTravelDistancesByMode(int it){

		String analysisDirPath = this.runDir + "/analysis";
		File analysisDir = new File(analysisDirPath);
		if(!analysisDir.exists()) createDir(analysisDir);

		String configFile = runDir + "/output_config.xml";
		String netFile = runDir + "/output_network.xml.gz";
		String popFile = runDir + "/" + String.valueOf(it) + ".plans.xml.gz";
		String eventsFile = runDir + "/" + String.valueOf(it) + ".events.xml.gz";
		String outputFile = analysisDirPath + "/" + String.valueOf(it) + ".modeTravelDistances.txt";

		Config config = ConfigUtils.loadConfig(configFile);
		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile(netFile);
		Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
		PopulationReader popReader = new PopulationReader(scenario);
		popReader.readFile(popFile);
		Population population =  scenario.getPopulation();

		SantiagoModeTripTravelDistanceHandler handler = new SantiagoModeTripTravelDistanceHandler(config,network,stuckAgents);
		EventsManager events = EventsUtils.createEventsManager();
		events.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(events);
		reader.readFile(eventsFile);

		SortedMap<String, Map<Id<Person>, List<String>>> privateTravelDistanceByMode = handler.getMode2PersonId2TravelDistances();
		//		SortedMap<String, Map<Id<Person>,List<String>>> PtTravelDistanceByMode = handler.getPT2PersonId2TravelDistances();

		try (BufferedWriter writer = IOUtils.getBufferedWriter(outputFile)) {
			writer.write("mode\tpersonId\tstartTime-distance\n");

			for(String mode : privateTravelDistanceByMode.keySet()){				
				for (Id<Person> person: privateTravelDistanceByMode.get(mode).keySet()){
					for (String distances: privateTravelDistanceByMode.get(mode).get(person)){
						writer.write(mode+"\t"+  person + "\t" + distances + "\n");					
					}	
				}
			}

			SantiagoPTDistanceFromPlans ptDistancesHand = new SantiagoPTDistanceFromPlans(population);
			SortedMap<String,Map<Id<Person>,List<String>>> PtTravelDistanceByMode=ptDistancesHand.getPt2PersonId2TravelDistances(stuckAgents);
			for(String mode : PtTravelDistanceByMode.keySet()){				
				for (Id<Person> person: PtTravelDistanceByMode.get(mode).keySet()){
					for (String distances: PtTravelDistanceByMode.get(mode).get(person)){
						writer.write(mode+"\t"+  person + "\t" + distances + "\n");					
					}	
				}
			}


			writer.close();
		} catch (Exception e) {
			throw new RuntimeException("Data is not written. Reason "+e );
		}


	}


}

