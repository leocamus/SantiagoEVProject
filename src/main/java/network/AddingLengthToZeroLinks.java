package network;


import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;


public class AddingLengthToZeroLinks {
	
	public static void main(String[] args) {	
	
	Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());	
	new MatsimNetworkReader(scenario.getNetwork()).readFile("../../Desktop/Devs/MATSim_EV_Scenarios/v2a/Santiago/input/network_merged_cl.xml.gz");
	Network network = (Network) scenario.getNetwork();
	int i = 0;
	
	for(Link ll : network.getLinks().values()){
		if (ll.getLength() == 0) {
			ll.setLength(1);
			i = i+1;

		}
		
		
	}
	
	System.out.println("The number of modified links is: " + Integer.toString(i));
	
	new NetworkWriter(network).write("../../Desktop/Devs/MATSim_EV_Scenarios/v2a/Santiago/input/network_merged_cl_nonZero.xml.gz");
	
	}

}
