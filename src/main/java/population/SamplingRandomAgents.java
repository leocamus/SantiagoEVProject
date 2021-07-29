package population;

import java.util.ArrayList;
import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.ConfigWriter;
import org.matsim.core.config.groups.ControlerConfigGroup;
import org.matsim.core.config.groups.CountsConfigGroup;
import org.matsim.core.config.groups.GlobalConfigGroup;
import org.matsim.core.config.groups.NetworkConfigGroup;
import org.matsim.core.config.groups.PlansConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.population.io.PopulationWriter;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.pt.config.TransitConfigGroup;

public class SamplingRandomAgents {
	
	static final String ORIGINAL_PLANS = "../../Desktop/Devs/MATSim_EV_Scenarios/v2a/Santiago/input/randomized_sampled_plans.xml.gz";
	static final String ORIGINAL_CONFIG = "../../Desktop/Devs/MATSim_EV_Scenarios/v2a/Santiago/config_baseCase1pct.xml";
	static final double ORIGINAL_PERCENTAGE = 0.01; //original sampled rate
	
	static final String SAMPLED_PLANS = "../../Desktop/Devs/MATSim_EV_Scenarios/v2a/Santiago/input/randomized_sampled_plans_problematic.xml.gz";
	static final String SAMPLED_CONFIG = "../../Desktop/Devs/MATSim_EV_Scenarios/v2a/Santiago/config_baseCase1pct_problematic.xml";
	static final double SAMPLED_PERCENTAGE = 1;
	
	static final String INPUT_DIR = "../santiago/";
	static final String OUTPUT_DIR = "../../Desktop/Devs/MATSim_EV_Scenarios/v2a/Santiago/output_baseCase1pct_problematic/";
	
	public SamplingRandomAgents(){
		
	}
	
	private void sampling(double sampledPercentage, String originalPlans, String sampledPlans) {
		Scenario scenarioTmp = ScenarioUtils.createScenario(ConfigUtils.createConfig());
		new PopulationReader(scenarioTmp).readFile(originalPlans);
		Population pop = scenarioTmp.getPopulation();

		Population newPop = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getPopulation() ;

		List<Id<Person>> problematicAgents = new ArrayList<Id<Person>>();
		Id<Person> id1 = Id.create("16517105_30", Person.class);
		Id<Person> id2 = Id.create("16517105_37", Person.class);
		Id<Person> id3 = Id.create("16517105_4", Person.class);
		Id<Person> id4 = Id.create("16517105_40", Person.class);
		Id<Person> id5 = Id.create("16517105_9", Person.class);
		Id<Person> id6 = Id.create("10086202_10", Person.class);

		problematicAgents.add(id1);
		problematicAgents.add(id2);
		problematicAgents.add(id3);
		problematicAgents.add(id4);
		problematicAgents.add(id5);
		problematicAgents.add(id6);


		
		for ( Person person : pop.getPersons().values() ) {
//			if ( Math.random() < sampledPercentage ) {
//				System.out.println("adding person...");
//				newPop.addPerson(person);
//			}
			if (problematicAgents.contains(person.getId())) {
				System.out.println("adding person...");
				newPop.addPerson(person);
			}
		}
		
	
		PopulationWriter popwriter = new PopulationWriter(newPop) ;
		popwriter.write( sampledPlans );

		System.out.println("done.");

	}
	
	private void changeAndWriteNewFiles(String originalConfig, String sampledConfig, String inputDir, String outputDir) {
		double finalSampleRate = Math.floor( ORIGINAL_PERCENTAGE * SAMPLED_PERCENTAGE * 1000) / 1000 ; //BE AWARE OF THIS
		Config config = ConfigUtils.loadConfig( originalConfig );
		
		GlobalConfigGroup global = config.global();
		global.setNumberOfThreads(1);
		
		QSimConfigGroup qsim = config.qsim();
		qsim.setFlowCapFactor(finalSampleRate);
		double storageCapFactor = Math.ceil( ( ( finalSampleRate / ( Math.pow ( finalSampleRate , 0.25 ) ) ) ) * 1000 ) / 1000;
		qsim.setStorageCapFactor(storageCapFactor);
		qsim.setNumberOfThreads(1);

	

		CountsConfigGroup counts = config.counts();
		counts.setCountsScaleFactor( Math.pow( finalSampleRate , -1 ) );

		
		
		ControlerConfigGroup cc = config.controler();
		cc.setOutputDirectory(outputDir);
		cc.setLastIteration(2);

		counts.setInputFile(inputDir + "input/counts_merged_VEH_C01.xml" );

		NetworkConfigGroup net = config.network();
		net.setInputFile(inputDir + "input/network_merged_cl.xml.gz" );
		
		PlansConfigGroup plans = config.plans();

		plans.setInputFile( inputDir + "input/randomized_sampled_plans_problematic.xml.gz" );
		plans.setInputPersonAttributeFile( inputDir + "input/sampledAgentAttributes.xml");
		
		TransitConfigGroup transit = config.transit();
		transit.setTransitScheduleFile(inputDir + "input/transitschedule_simplified.xml" );
		transit.setVehiclesFile(inputDir + "input/transitvehicles.xml" );
		
	
		

		
		new ConfigWriter(config).write( sampledConfig );
		
		


		
	}
	
	public static void main(String[] args) {
		SamplingRandomAgents sra = new SamplingRandomAgents();
		sra.sampling(SAMPLED_PERCENTAGE, ORIGINAL_PLANS, SAMPLED_PLANS);
		sra.changeAndWriteNewFiles(ORIGINAL_CONFIG, SAMPLED_CONFIG, INPUT_DIR, OUTPUT_DIR);
	}

}
