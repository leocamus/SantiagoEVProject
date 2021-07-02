package runSantiagoTests;

import java.io.BufferedWriter;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.utils.io.IOUtils;

public class SantiagoStuckAgentsAnalysis {
	
	private String runDir;
	
	public SantiagoStuckAgentsAnalysis(String baseFolderPath){

		this.runDir = baseFolderPath; //This is the base folder where your events files

	}
	
	private void createDir(File file) {
		file.mkdirs();	
	}
	
	public void writeStuckEvents(int it){
		String analysisDirPath = this.runDir + "/analysis";
		File analysisDir = new File(analysisDirPath);
		if(!analysisDir.exists()) createDir(analysisDir);

		String eventsFile = this.runDir + "/" + String.valueOf(it) + ".events.xml.gz";

		SantiagoStuckAndAbortEventHandler handler = new SantiagoStuckAndAbortEventHandler();
		EventsManager events = EventsUtils.createEventsManager();
		events.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(events);
		reader.readFile(eventsFile);

		SortedMap<String, Map<Id<Person>, List<Double>>> mode2PersonId2Times = handler.getMode2IdAgentsStuck2Time();

		String outputFile = analysisDirPath + String.valueOf(it) + ".modeStuckAgents.txt";
		try (BufferedWriter writer = IOUtils.getBufferedWriter(outputFile)) {
			writer.write("mode\tpersonId\teventTime\n");
			for(String mode : mode2PersonId2Times.keySet()){				
				for (Id<Person> person: mode2PersonId2Times.get(mode).keySet()){
					for (double eventTime: mode2PersonId2Times.get(mode).get(person)){
						writer.write(mode+"\t"+  person + "\t" + eventTime + "\n");		
					}					
				}
			}

			writer.close();
		} catch (Exception e) {
			throw new RuntimeException("Data is not written. Reason "+e );
		}
	}

	public List<Id<Person>> getStuckAgents (int it){
		String eventsFile = this.runDir + "/" + String.valueOf(it) + ".events.xml.gz";
		SantiagoStuckAndAbortEventHandler handler = new SantiagoStuckAndAbortEventHandler();
		EventsManager events = EventsUtils.createEventsManager();
		events.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(events);
		reader.readFile(eventsFile);		
		return handler.getAgentsStuck();
	}
}
