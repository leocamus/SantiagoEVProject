package ScenarioTranslation;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.ControlerConfigGroup;
import org.matsim.core.config.groups.ControlerConfigGroup.EventsFileFormat;
import org.matsim.core.config.groups.ControlerConfigGroup.MobsimType;
import org.matsim.core.config.groups.ControlerConfigGroup.RoutingAlgorithmType;
import org.matsim.core.config.groups.CountsConfigGroup;
import org.matsim.core.config.groups.GlobalConfigGroup;
import org.matsim.core.config.groups.LinkStatsConfigGroup;
import org.matsim.core.config.groups.NetworkConfigGroup;
import org.matsim.core.config.groups.ParallelEventHandlingConfigGroup;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ModeParams;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup.ModeRoutingParams;
import org.matsim.core.config.groups.PlansConfigGroup;
import org.matsim.core.config.groups.PlansConfigGroup.ActivityDurationInterpretation;
import org.matsim.core.config.groups.PlansConfigGroup.NetworkRouteType;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup.LinkDynamics;
import org.matsim.core.config.groups.QSimConfigGroup.SnapshotStyle;
import org.matsim.core.config.groups.QSimConfigGroup.StarttimeInterpretation;
import org.matsim.core.config.groups.QSimConfigGroup.TrafficDynamics;
import org.matsim.core.config.groups.QSimConfigGroup.VehicleBehavior;
import org.matsim.core.config.groups.StrategyConfigGroup;
import org.matsim.core.config.groups.TravelTimeCalculatorConfigGroup;
import org.matsim.core.config.groups.TravelTimeCalculatorConfigGroup.TravelTimeCalculatorType;
import org.matsim.core.config.groups.VspExperimentalConfigGroup;
import org.matsim.core.config.groups.VspExperimentalConfigGroup.VspDefaultsCheckingLevel;
import org.matsim.pt.config.TransitConfigGroup;
import org.matsim.pt.config.TransitRouterConfigGroup;

import ScenarioTranslation.SantiagoScenarioConstants.SubpopulationName;

public class TranslatingInputs {
/*	
	private void writeNewConfig(){
		Config config = ConfigUtils.createConfig();
	}

	
	private void setTransitParameters(TransitConfigGroup transit, TransitRouterConfigGroup transitRouter) {
		Set<String> transitModes = new HashSet<String>();
		transitModes.add(TransportMode.pt);
		transitModes.add(SantiagoScenarioConstants.Modes.bus.toString());
		transitModes.add(SantiagoScenarioConstants.Modes.metro.toString());
//		transitModes.add(SantiagoScenarioConstants.Modes.train.toString());
		transit.setTransitModes(transitModes);
//		transit.setTransitScheduleFile(pathForMatsim + "input/transitschedule.xml");
		transit.setTransitScheduleFile(pathForMatsim + "input/transitschedule_simplified.xml");
		transit.setVehiclesFile(pathForMatsim + "input/transitvehicles.xml");
		transit.setUseTransit(true);
//		transitRouter.setMaxBeelineWalkConnectionDistance(500.);
//		transitRouter.setSearchRadius(200.);
		transitRouter.setExtensionRadius(500.);
	}

	private void setControlerParameters(ControlerConfigGroup cc){
		cc.setLinkToLinkRoutingEnabled(false);
		HashSet<EventsFileFormat> eventsFileFormats = new HashSet<EventsFileFormat>();
		eventsFileFormats.add(EventsFileFormat.xml);
		cc.setEventsFileFormats(eventsFileFormats);
		cc.setFirstIteration(0);
		cc.setLastIteration(100);
		cc.setMobsim(MobsimType.qsim.name());
		cc.setOutputDirectory(pathForMatsim + "output/");
		cc.setRoutingAlgorithmType(RoutingAlgorithmType.Dijkstra);
		cc.setRunId(null);	//should not be "", because then all file names start with a dot. --> null or any number. (KT, 2015-08-17) 
		cc.setWriteEventsInterval(writeStuffInterval);
		cc.setWritePlansInterval(writeStuffInterval);
		cc.setSnapshotFormat(CollectionUtils.stringToSet("otfvis"));
		cc.setWriteSnapshotsInterval(0);
	}
	
	private void setCountsParameters(CountsConfigGroup counts, double sampleSizeEOD){
		// TODO: check what adding taxi, colectivo, and freight changes
		counts.setAnalyzedModes(TransportMode.car);
		counts.setAverageCountsOverIterations(5);
		counts.setCountsScaleFactor(SantiagoScenarioConstants.N / sampleSizeEOD);
		counts.setDistanceFilter(null);
		counts.setDistanceFilterCenterNode(null);
		counts.setFilterModes(false);
		counts.setInputFile(pathForMatsim + "input/counts_merged_VEH_C01.xml");
		counts.setOutputFormat("all");
		counts.setWriteCountsInterval(writeStuffInterval);
	}
	
	private void setGlobalParameters(GlobalConfigGroup global){
		global.setCoordinateSystem(SantiagoScenarioConstants.toCRS);
		global.setNumberOfThreads(nrOfThreads);
		global.setRandomSeed(4711);
	}
	
	private void setLinkStatsParameters(LinkStatsConfigGroup ls){
		ls.setAverageLinkStatsOverIterations(5);
		ls.setWriteLinkStatsInterval(writeStuffInterval);
	}
	
	private void setNetworkParameters(NetworkConfigGroup net){
		net.setChangeEventsInputFile(null);
		net.setInputFile(pathForMatsim + "input/network_merged_cl.xml.gz");
		net.setLaneDefinitionsFile(null);
		net.setTimeVariantNetwork(false);
	}
	
	private void setParallelEventHandlingParameters(ParallelEventHandlingConfigGroup peh){
		peh.setEstimatedNumberOfEvents(null);
		peh.setNumberOfThreads(nrOfThreads);
	}
	
	private void setPlanCalcScoreParameters(PlanCalcScoreConfigGroup pcs){
		pcs.setBrainExpBeta(1.0);
//		pcs.setPathSizeLogitBeta(0.0);
//		pcs.setEarlyDeparture_utils_hr(-0.0);
		pcs.setFractionOfIterationsToStartScoreMSA(0.8);
//		pcs.setLateArrival_utils_hr(-18.0);
		pcs.setLearningRate(1.0);
		double marginalUtlOfMoney = 0.0023;
		pcs.setMarginalUtilityOfMoney(marginalUtlOfMoney);
		pcs.setPerforming_utils_hr(4.014);
		pcs.setUsingOldScoringBelowZeroUtilityDuration(false);
//		pcs.setUtilityOfLineSwitch(-1.0);
//		pcs.setMarginalUtlOfWaiting_utils_hr(-0.0);
		pcs.setWriteExperiencedPlans(false);
		
		ModeParams carParams = new ModeParams(TransportMode.car);
		carParams.setConstant(0.0);
//		carParams.setMarginalUtilityOfDistance(0.0);
		carParams.setMarginalUtilityOfTraveling(-1.056);
		carParams.setMonetaryDistanceRate(-0.248);
		pcs.addModeParams(carParams);
		
		ModeParams rideParams = new ModeParams(TransportMode.ride);
		rideParams.setConstant(0.0);
//		rideParams.setMarginalUtilityOfDistance(0.0);
		rideParams.setMarginalUtilityOfTraveling(-1.056);
		rideParams.setMonetaryDistanceRate(-0.0);
		pcs.addModeParams(rideParams);
		
		// fare from Alejandro (Seremi de Transportes y Telecommunicationes de la Region Metropolitana):
		// in 2013: 250 Pesos fixed fare; 120 Pesos per 200m
		ModeParams taxiParams = new ModeParams(SantiagoScenarioConstants.Modes.taxi.toString());
		taxiParams.setConstant(marginalUtlOfMoney * (-250.));
//		taxiParams.setMarginalUtilityOfDistance(0.0);
		taxiParams.setMarginalUtilityOfTraveling(-1.056);
		taxiParams.setMonetaryDistanceRate(-0.6);
		pcs.addModeParams(taxiParams);
		
		// some things on colectivos, see http://www.ubicatucolectivo.cl/cliente_final/all_lines/vercion_1.php?id=14
		ModeParams colectivoParams = new ModeParams(SantiagoScenarioConstants.Modes.colectivo.toString());
		colectivoParams.setConstant(0.0);
//		colectivoParams.setMarginalUtilityOfDistance(0.0);
		colectivoParams.setMarginalUtilityOfTraveling(-1.056);
		colectivoParams.setMonetaryDistanceRate(-0.0);
		pcs.addModeParams(colectivoParams);
		
		ModeParams trainParams = new ModeParams(SantiagoScenarioConstants.Modes.train.toString());
		trainParams.setConstant(0.0);
//		trainParams.setMarginalUtilityOfDistance(0.0);
		trainParams.setMarginalUtilityOfTraveling(-1.056);
		trainParams.setMonetaryDistanceRate(-0.0);
		pcs.addModeParams(trainParams);
		
		/*
		 * begin pt parameter settings
		 * */
/*
		if(!prepareForModeChoice){
			ModeParams busParams = new ModeParams(SantiagoScenarioConstants.Modes.bus.toString());
			busParams.setConstant(0.0);
//			busParams.setMarginalUtilityOfDistance(0.0);
			busParams.setMarginalUtilityOfTraveling(-1.056);
			busParams.setMonetaryDistanceRate(-0.0);
			pcs.addModeParams(busParams);
			
			ModeParams metroParams = new ModeParams(SantiagoScenarioConstants.Modes.metro.toString());
			metroParams.setConstant(0.0);
//			metroParams.setMarginalUtilityOfDistance(0.0);
			metroParams.setMarginalUtilityOfTraveling(-1.056);
			metroParams.setMonetaryDistanceRate(-0.0);
			pcs.addModeParams(metroParams);
			
		} else {
			ModeParams ptParams = new ModeParams(TransportMode.pt);
//			walkParams.setConstant(0.0);
//			walkParams.setConstant(-1.0);
			ptParams.setConstant(-1.0575263095);
//			ptParams.setMarginalUtilityOfDistance(0.0);
			ptParams.setMarginalUtilityOfTraveling(-1.056);
			ptParams.setMonetaryDistanceRate(-0.0);
			pcs.addModeParams(ptParams);
		}
		/*
		 * end pt parameter settings
		 * */
/*		
		ModeParams walkParams = new ModeParams(TransportMode.walk);
//		walkParams.setConstant(0.0);
		walkParams.setConstant(-0.1432823063);
//		walkParams.setMarginalUtilityOfDistance(0.0);
		walkParams.setMarginalUtilityOfTraveling(-1.056);
		walkParams.setMonetaryDistanceRate(-0.0);
		pcs.addModeParams(walkParams);
		
		ModeParams bikeParams = new ModeParams(TransportMode.bike);
		bikeParams.setConstant(0.0);
//		bikeParams.setMarginalUtilityOfDistance(0.0);
		bikeParams.setMarginalUtilityOfTraveling(-1.056);
		bikeParams.setMonetaryDistanceRate(-0.0);
		pcs.addModeParams(bikeParams);
		
		ModeParams otherModeParams = new ModeParams(TransportMode.other);
		otherModeParams.setConstant(0.0);
//		otherModeParams.setMarginalUtilityOfDistance(0.0);
		otherModeParams.setMarginalUtilityOfTraveling(-1.056);
		otherModeParams.setMonetaryDistanceRate(-0.0);
		pcs.addModeParams(otherModeParams);
		
//		ModeParams motorcycleParams = new ModeParams(SantiagoScenarioConstants.Modes.motorcycle.toString());
//		motorcycleParams.setConstant(0.0);
////		motorcycleParams.setMarginalUtilityOfDistance(0.0);
//		motorcycleParams.setMarginalUtilityOfTraveling(-1.056);
//		motorcycleParams.setMonetaryDistanceRate(-0.0);
//		pcs.addModeParams(motorcycleParams);
//
//		ModeParams schoolBusParams = new ModeParams(SantiagoScenarioConstants.Modes.school_bus.toString());
//		schoolBusParams.setConstant(0.0);
////		schoolBusParams.setMarginalUtilityOfDistance(0.0);
//		schoolBusParams.setMarginalUtilityOfTraveling(-1.056);
//		schoolBusParams.setMonetaryDistanceRate(-0.0);
//		pcs.addModeParams(schoolBusParams);
	}
	
	private void setPlanParameters(PlansConfigGroup plans){
		plans.setActivityDurationInterpretation(ActivityDurationInterpretation.tryEndTimeThenDuration);
		if(prepareForModeChoice) plans.setInputPersonAttributeFile(pathForMatsim + "input/" +"agentAttributes.xml");
		plans.setInputFile(pathForMatsim + "input/" + "plans_final" + ".xml.gz");
		plans.setNetworkRouteType(NetworkRouteType.LinkNetworkRoute);
//		plans.setSubpopulationAttributeName(SubpopulationName.carUsers); 
		plans.setRemovingUnneccessaryPlanAttributes(true);
	}
	
	private void setPlansCalcRouteParameters(PlansCalcRouteConfigGroup pcr){
		Set<String> networkModes = new HashSet<String>();
		networkModes.add(TransportMode.car);
		networkModes.add(TransportMode.ride);
		networkModes.add(SantiagoScenarioConstants.Modes.taxi.toString());
		networkModes.add(SantiagoScenarioConstants.Modes.colectivo.toString());
		networkModes.add(SantiagoScenarioConstants.Modes.other.toString());
//		networkModes.add(SantiagoScenarioConstants.Modes.motorcycle.toString());
//		networkModes.add(SantiagoScenarioConstants.Modes.school_bus.toString());
//		if(prepareForModeChoice) networkModes.add(TransportMode.pt);
		pcr.setNetworkModes(networkModes);
		
	/*
	 * begin pt parameter settings
	 * */
/*
		if(!prepareForModeChoice){
			ModeRoutingParams busParams = new ModeRoutingParams(SantiagoScenarioConstants.Modes.bus.toString());
			busParams.setBeelineDistanceFactor(1.3);
			busParams.setTeleportedModeSpeed(25 / 3.6);
			pcr.addModeRoutingParams(busParams);

			ModeRoutingParams metroParams = new ModeRoutingParams(SantiagoScenarioConstants.Modes.metro.toString());
			metroParams.setBeelineDistanceFactor(1.3);
			metroParams.setTeleportedModeSpeed(32 / 3.6);
			pcr.addModeRoutingParams(metroParams);
		} else {
			//TODO: This is by default set to walk parameters; changing this might require defining my own TripRouter...
//			ModeRoutingParams transitWalkParams = new ModeRoutingParams(TransportMode.transit_walk);
//			transitWalkParams.setBeelineDistanceFactor(1.3);
//			transitWalkParams.setTeleportedModeSpeed(3 / 3.6);
//			pcr.addModeRoutingParams(transitWalkParams);
		}
	/*
	 * end pt parameter settings
	 * */
/*		
		ModeRoutingParams trainParams = new ModeRoutingParams(SantiagoScenarioConstants.Modes.train.toString());
		trainParams.setBeelineDistanceFactor(1.3);
		trainParams.setTeleportedModeSpeed(50 / 3.6);
		pcr.addModeRoutingParams(trainParams);
			
		ModeRoutingParams walkParams = new ModeRoutingParams(TransportMode.walk);
		walkParams.setBeelineDistanceFactor(1.3);
		walkParams.setTeleportedModeSpeed(4 / 3.6);
		pcr.addModeRoutingParams(walkParams);
		
		ModeRoutingParams bikeParams = new ModeRoutingParams(TransportMode.bike);
		bikeParams.setBeelineDistanceFactor(1.3);
		bikeParams.setTeleportedModeSpeed(10 / 3.6);
		pcr.addModeRoutingParams(bikeParams);
	}
	
	private void setQSimParameters(QSimConfigGroup qsim, double sampleSizeEOD){
		qsim.setStartTime(0 * 3600);
		qsim.setEndTime(30 * 3600);
		double flowCapFactor = (sampleSizeEOD / SantiagoScenarioConstants.N);
		qsim.setFlowCapFactor(flowCapFactor);
//		qsim.setStorageCapFactor(0.015);
		qsim.setStorageCapFactor(flowCapFactor * 3.);
		qsim.setInsertingWaitingVehiclesBeforeDrivingVehicles(false);
		qsim.setLinkDynamics(LinkDynamics.FIFO);
//		qsim.setLinkWidth(30);
		Set<String> mainModes = new HashSet<String>();
		mainModes.add(TransportMode.car);
		// not necessary since pt is not a congested mode
//		if(prepareForModeChoice) mainModes.add(TransportMode.pt);
		qsim.setMainModes(mainModes);
		qsim.setNodeOffset(0.0);
		qsim.setNumberOfThreads(nrOfThreads);
		qsim.setSimStarttimeInterpretation(StarttimeInterpretation.maxOfStarttimeAndEarliestActivityEnd);
		qsim.setSnapshotStyle(SnapshotStyle.equiDist);
		qsim.setSnapshotPeriod(0.0);
//		qsim.setStuckTime(10.0);
//		qsim.setTimeStepSize(1.0);
		qsim.setTrafficDynamics(TrafficDynamics.queue);
		qsim.setUsePersonIdForMissingVehicleId(true);
		qsim.setUsingFastCapacityUpdate(false);
//		qsim.setUsingThreadpool(false);
		qsim.setVehicleBehavior(VehicleBehavior.teleport);
	}
	
	private void setStrategyParameters(StrategyConfigGroup strategy){
		strategy.setFractionOfIterationsToDisableInnovation(0.8);
		strategy.setMaxAgentPlanMemorySize(5);
		strategy.setPlanSelectorForRemoval("WorstPlanSelector");
	}
	
	private void setTravelTimeCalculatorParameters(TravelTimeCalculatorConfigGroup ttc){
		ttc.setAnalyzedModes(TransportMode.car);
		ttc.setCalculateLinkToLinkTravelTimes(false);
		ttc.setCalculateLinkTravelTimes(true);
		ttc.setFilterModes(false);
		ttc.setTravelTimeAggregatorType("optimistic");
		ttc.setTraveltimeBinSize(900);
		ttc.setTravelTimeCalculatorType(TravelTimeCalculatorType.TravelTimeCalculatorArray.name());
		ttc.setTravelTimeGetterType("average");
	}
	
	private void setVspExperimentalParameters(VspExperimentalConfigGroup vsp){
		vsp.setLogitScaleParamForPlansRemoval(1.0);
		vsp.setVspDefaultsCheckingLevel(VspDefaultsCheckingLevel.warn);
		vsp.setWritingOutputEvents(true);
	}
	
	private void createDir(File file) {
		log.info("Directory " + file + " created: "+ file.mkdirs());	
	}

*/
}
