package runSantiagoTests;

import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;

public class SantiagoRunAnalysis {
	private static final String BASE_FOLDER_PATH = "/home/leocamus/Escritorio/MATSim_FromScratch/eventsExample/baseCase1pct";
	private static final int IT_TO_EVALUATE = 100; //From the local counter

	
	
	public static void main (String[]args){
		SantiagoStuckAgentsAnalysis stuckAnalysis = new SantiagoStuckAgentsAnalysis(BASE_FOLDER_PATH);
		

		int it = IT_TO_EVALUATE;
		
//		while(itAux<=LAST_IT){
			
			List<Id<Person>> stuckAgents = stuckAnalysis.getStuckAgents(it);
			
//			SantiagoTravelTimesAnalysis timesAnalysis = new SantiagoTravelTimesAnalysis(CASE_NAME, STEP_NAME, stuckAgents);
//			timesAnalysis.writeFileForTravelTimesByMode(it,itAux);
		
//			SantiagoTrafficVolumesAnalysis trafficAnalysis = new SantiagoTrafficVolumesAnalysis(CASE_NAME,STEP_NAME);
//			trafficAnalysis.writeFileForLinkVolumes(it, itAux);
		
//			SantiagoCarLegsAnalysis carLegAnalysis = new SantiagoCarLegsAnalysis(CASE_NAME, STEP_NAME);
//			carLegAnalysis.writeCarLegs(it, itAux);
			
			SantiagoTravelDistancesAnalysis distancesAnalysis = new SantiagoTravelDistancesAnalysis(BASE_FOLDER_PATH, stuckAgents);
			distancesAnalysis.writeFileForTravelDistancesByMode(it);
			
//			it+=50;
//			itAux+=50;	
//		}
				
		
	}
	
	
}
