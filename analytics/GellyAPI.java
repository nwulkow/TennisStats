package analytics;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.examples.java.graph.ConnectedComponents;
import org.apache.flink.examples.java.graph.PageRankBasic;
import org.apache.flink.examples.java.graph.util.PageRankData;
import org.apache.flink.graph.Edge;
import org.apache.flink.graph.Graph;
import org.apache.flink.graph.GraphAlgorithm;
import org.apache.flink.graph.Vertex;
import org.apache.flink.graph.example.PageRank;
//import org.apache.flink.api.scala.DataSet;
import org.apache.flink.graph.example.utils.CommunityDetectionData;
import org.apache.flink.graph.library.CommunityDetectionAlgorithm;
import org.apache.flink.graph.library.PageRankAlgorithm;
import org.apache.flink.graph.utils.Tuple2ToVertexMap;
import org.apache.flink.graph.utils.Tuple3ToEdgeMap;

import analysisFormats.ProbabilityTensor;
import archiveLoad.LoadMatchResults;
import tools.OutputTools;
import tools.StatsTools;
import tools.Tools;


public class GellyAPI {
	
	Graph<Long, Long, Double> graph;
	List<Vertex<Long, Long>> collectedVertices;
	int maxIterations = 10;
	double CDDDelta = CommunityDetectionData.DELTA;

	public GellyAPI(){
		
	}
	
	public GellyAPI(String edgesInputPath, String vertexInputPath){
		loadGraph(edgesInputPath, vertexInputPath);
	}

	public static void main(String[] args) throws Exception {

		String edgesInputPath = "C:/Users/Niklas/TennisStatsData/test.csv";
		String vertexInputPath = "C:/Users/Niklas/TennisStatsData/vertices_test.csv";
		boolean fileOutput = true;
		//pageRank(edgesInputPath, vertexInputPath, fileOutput);
		//communityDetection(edgesInputPath, vertexInputPath, new ArrayList<String>(), fileOutput);
		
		// Vertex<Long, Long> v = new Vertex<Long, Long>(1L, 2L);

		// Edge<Long, Double> e = new Edge<Long, Double>(1L, 2L, 0.5);

		// List<Vertex> lv = new ArrayList<Vertex>();
		// ----------------------------------------------------------------------------------

		/*ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		boolean fileOutput = true;

		String edgesInputPath = "C:/Users/Niklas/TennisStatsData/correlations_edges.csv";
		DataSet<Edge<Long, Double>> edges = getEdgesDataSet(env, edgesInputPath);

		System.out.println("Edges geladen");
		String vertexInputPath = "C:/Users/Niklas/TennisStatsData/correlations_vertices.csv";
		DataSet<Vertex<Long, Long>> vertices = getVerticesDataSet(env, vertexInputPath);
		System.out.println("Vertices geladen");
		Graph<Long, Double, Double> graph = Graph.fromDataSet(edges, new MapFunction<Long, Double>() {
			
			 			public Double map(Long value) throws Exception {
			 				return 1.0;
			 			}
			 		}, env);
			 
			 		DataSet<Tuple2<Long, Long>> vertexOutDegrees = graph.outDegrees();
			 
			 		// assign the transition probabilities as the edge weights
			 		Graph<Long, Double, Double> networkWithWeights = graph
			 				.joinWithEdgesOnSource(vertexOutDegrees,
			 						new MapFunction<Tuple2<Double, Long>, Double>() {
			 							public Double map(Tuple2<Double, Long> value) {
			 								return value.f0 / value.f1;
			 							}
			 						});
			 
			 		DataSet<Vertex<Long, Double>> pagerankVertices = networkWithWeights.run(
			 				new PageRankAlgorithm<Long>(0.5, 50))
			 				.getVertices();
		System.out.println(pagerankVertices.count() + " ------------------------");
		
		
		/*Graph<Long, Long, Double> graph = Graph.fromDataSet(vertices, edges, env);
		graph.outDegrees();
		
		// edges.writeAsCsv("/home/mi/nwulkow/ADL/Data/edgeoutput", "\n", ",");
		Integer maxIterations = 100;//CommunityDetectionData.MAX_ITERATIONS;
		Double delta = CommunityDetectionData.DELTA;
		DataSet<Vertex<Long, Long>> communityVertices = graph.run(new CommunityDetectionAlgorithm(maxIterations, delta))
				.getVertices();

		DataSet<Vertex<Long, Long>> pagerankVertices2 = graph.run(new PageRankAlgorithm(0.5, 50)).getVertices();
		//DataSet<Vertex<Long, Double>> pagerankVertices = graph.run(new PageRankAlgorithm<>(0.5, maxIterations)).getVertices();
		System.out.println(pagerankVertices.count() + "  ---------------------------------");
		// emit result		
		if (fileOutput) {
			//communityVertices.writeAsCsv("C:/Users/Niklas/TennisStatsData/test_gellyapi_output", "\n", ",");
			pagerankVertices.writeAsCsv("C:/Users/Niklas/TennisStatsData/test_pagerank_output", "\n", ",");
			pagerankVertices2.writeAsCsv("C:/Users/Niklas/TennisStatsData/test_pagerank2_output", "\n", ",");
			// since file sinks are lazy, we trigger the execution explicitly
			env.execute("Executing Community Detection Example");
		} else {
			//communityVertices.print();
			//List<Vertex<Long,Long>> verticeslist = communityVertices.collect();
			//for(int i = 0 ; i < verticeslist.size(); i++){
				//System.out.println(communityVertices.collect().get(i).f0 + " , " + communityVertices.collect().get(i).f1);
		//	}

		}
*/
	}

	
	public List<Vertex<Long, Long>> communityDetection(Graph<Long, Long, Double> graph, ArrayList<String> nodenames, boolean fileOutput, ExecutionEnvironment env) throws Exception{
		
		//ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		System.out.println("delta = " + CDDDelta + " , maxIterations = " + maxIterations);
		DataSet<Vertex<Long, Long>> communityVertices = graph.run(new CommunityDetectionAlgorithm(maxIterations, CDDDelta))
				.getVertices();
		List<Vertex<Long, Long>> collectedVertices = communityVertices.collect();
		this.collectedVertices = collectedVertices;
		if (fileOutput) {
			communityVertices.writeAsCsv("C:/Users/Niklas/TennisStatsData/test_gellyapi_output", "\n", ",");
			// since file sinks are lazy, we trigger the execution explicitly
			env.execute("Executing Community Detection Example");
		}
		else {
			communityVertices.print();
		}
		return collectedVertices;
	}
	
	public List<Vertex<Long, Long>> communityDetection(String edgesInputPath, String vertexInputPath, ArrayList<String> nodenames, boolean fileOutput) throws Exception{
		
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		
		DataSet<Edge<Long, Double>> edges = getEdgesDataSet(env, edgesInputPath);
		DataSet<Vertex<Long, Long>> vertices = getVerticesDataSet(env, vertexInputPath);
		Graph<Long, Long, Double> graph = Graph.fromDataSet(vertices, edges, env);
		this.graph = graph;
		return communityDetection(this.graph, nodenames, fileOutput, env);
	}
	
	
	public ArrayList<ArrayList<Double>> findCenterVertices(ArrayList<String> playernames) throws Exception{
		
		double noVertices = collectedVertices.size();
		ArrayList<Integer> clusterIDs = new ArrayList<Integer>();
		ArrayList<Double> clusterNumberOfVertices = new ArrayList<Double>();
		
		ArrayList<Double> weightsExceptClusterMates = new ArrayList<Double>(); // Liste, die fuer jeden Knoten die Summe der Kantengewichte enthaelt, die zu Knoten aus anderen Clustern fuehren
		ArrayList<Double> weightsBetweenClusterMates = new ArrayList<Double>(); // Gleiche Liste wie oben, nur dass die Knoten hernagezogen werden, die im gleichen Cluster liegen
		ArrayList<Integer> vertexNumbers = new ArrayList<Integer>();
		ArrayList<ArrayList<String>> playersInClusters = new ArrayList<ArrayList<String>>();
		// Zaehlen, wie viele Knoten zu jedem einzelnen Cluster gehoeren:
		for(int j = 0; j < noVertices; j++){
			vertexNumbers.add(collectedVertices.get(j).getId().intValue());
			weightsExceptClusterMates.add(0d);
			weightsBetweenClusterMates.add(0d);
			int clusterIndex = clusterIDs.indexOf(collectedVertices.get(j).getValue().intValue());
			if(clusterIndex >= 0){
				clusterNumberOfVertices.set(clusterIndex, clusterNumberOfVertices.get(clusterIndex) + 1);
			}
			else{
				clusterIDs.add(collectedVertices.get(j).getValue().intValue());
				clusterNumberOfVertices.add(1d);
				playersInClusters.add(new ArrayList<String>());
			}
		}
		
		List<Edge<Long,Double>> edges = graph.getEdges().collect();
		// Alle Edges durchgehen und Gewichtung notieren
		for(Edge<Long,Double> edge : edges){
			int v1Index = edge.f0.intValue(); // Knoten-Nummer im Graph
			int v2Index = edge.f1.intValue();
			Vertex<Long,Long> v1 = collectedVertices.get(vertexNumbers.indexOf(v1Index)); // Knoten, die zum Edge gehoeren
			Vertex<Long,Long> v2 = collectedVertices.get(vertexNumbers.indexOf(v2Index));
			double edgeWeight = edge.f2;
			if(!v1.getValue().equals(v2.getValue())){ // Value ist die Cluster-Nummer. Wenn zwei Vertices in verschiedenen Clustern sind, dann Gewicht des Edges zwischen beiden auf die persoenlichen Counter addieren
				weightsExceptClusterMates.set(v1Index, weightsExceptClusterMates.get(v1Index) + edgeWeight);
				weightsExceptClusterMates.set(v2Index, weightsExceptClusterMates.get(v2Index) + edgeWeight);
			}
			else{
				weightsBetweenClusterMates.set(v1Index, weightsBetweenClusterMates.get(v1Index) + edgeWeight);
				weightsBetweenClusterMates.set(v2Index, weightsBetweenClusterMates.get(v2Index) + edgeWeight);
			}
		}
		// Output und dividieren der weightECM-Werte durch die Anzahl der Knoten auﬂerhalb des Clusters
		for(int i = 0; i < noVertices; i++){
			int index = collectedVertices.get(i).getId().intValue();
			int clusterID = collectedVertices.get(i).getValue().intValue();
			int clusterIndex = clusterIDs.indexOf(clusterID);
			playersInClusters.get(clusterIndex).add(playernames.get(index));
			weightsExceptClusterMates.set(index, weightsExceptClusterMates.get(index) / (noVertices - clusterNumberOfVertices.get(clusterIndex)));
			weightsBetweenClusterMates.set(index, weightsBetweenClusterMates.get(index) / (clusterNumberOfVertices.get(clusterIndex)));
			
			System.out.println("Name: " + playernames.get(index) + " , ID: " + index + " , Cluster: " + collectedVertices.get(i).getValue() 
					+ " , weightsExceptClusterMates: " + weightsExceptClusterMates.get(index) + " , weightsBetweenClusterMates: " + weightsBetweenClusterMates.get(index));
		}
		System.out.println(clusterIDs);
		ArrayList<ArrayList<Double>> cummulatedEdgeWeights = new ArrayList<ArrayList<Double>>();
		cummulatedEdgeWeights.add(weightsExceptClusterMates);
		cummulatedEdgeWeights.add(weightsBetweenClusterMates);
		LoadMatchResults.allClustersAgainstEachOther(playersInClusters);
		return cummulatedEdgeWeights;
	}
	
	public List<Vertex<Long,Double>> pageRank(Graph<Long, Double, Double> graph, boolean fileOutput, ExecutionEnvironment env) throws Exception{
		
		//ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

				DataSet<Tuple2<Long, Long>> vertexOutDegrees = graph.outDegrees();
				 
		 		// assign the transition probabilities as the edge weights
		 		Graph<Long, Double, Double> networkWithWeights = graph
		 				.joinWithEdgesOnSource(vertexOutDegrees,
		 						new MapFunction<Tuple2<Double, Long>, Double>() {
		 							public Double map(Tuple2<Double, Long> value) {
		 								return value.f0 / value.f1;
		 							}
		 						});
		 
		 		DataSet<Vertex<Long, Double>> pagerankVertices = networkWithWeights.run(
		 				new PageRankAlgorithm<Long>(0.5, 50))
		 				.getVertices();
		
		List<Vertex<Long,Double>> collectedVertices = pagerankVertices.collect();
		double[] ranks = new double[(int) collectedVertices.size()];
		for(int i = 0; i < collectedVertices.size(); i++){
			ranks[i] = collectedVertices.get(i).getValue();
		}
		// emit result		
		if (fileOutput) {
		pagerankVertices.writeAsCsv("C:/Users/Niklas/TennisStatsData/test_pagerank_output", "\n", ",");
		// since file sinks are lazy, we trigger the execution explicitly
		env.execute("Executing Community Detection Example");
		}
		else {
			//pagerankVertices.print();
			ranks = StatsTools.absToRel(ranks);
			//System.out.println("STD = " + StatsTools.standarddeviation(ranks));
		}
		return collectedVertices;
	}
	
	public List<Vertex<Long,Double>> pageRank(String edgesInputPath, String vertexInputPath, boolean fileOutput) throws Exception{
		
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		DataSet<Edge<Long, Double>> edges = getEdgesDataSet(env, edgesInputPath);
		//DataSet<Vertex<Long, Long>> vertices = getVerticesDataSet(env, vertexInputPath);
		Graph<Long, Double, Double> graph = Graph.fromDataSet(edges, new MapFunction<Long, Double>() {
			
			 			public Double map(Long value) throws Exception {
			 				return 1.0;
			 			}
			 		}, env);	 
		return pageRank(graph, fileOutput, env);
	}
	
	
	public static class TwoTuple {
		String first;
		String second;

		public TwoTuple(String first, String second) {
			this.first = first;
			this.second = second;
		}
	}

	private DataSet<Edge<Long, Double>> getEdgesDataSet(ExecutionEnvironment env, String edgesInputPath) {
		return env.readCsvFile(edgesInputPath).ignoreComments("#").fieldDelimiter(",").lineDelimiter("\n")
				.types(Long.class, Long.class, Double.class).map(new Tuple3ToEdgeMap<Long, Double>());
	}

	private DataSet<Vertex<Long, Long>> getVerticesDataSet(ExecutionEnvironment env, String vertexInputPath) {
		return env.readCsvFile(vertexInputPath).ignoreComments("#").fieldDelimiter(",").lineDelimiter("\n")
				.types(Long.class, Long.class).map(new Tuple2ToVertexMap<Long, Long>());
	}
	
	private void loadGraph(String edgesInputPath, String vertexInputPath){
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		DataSet<Edge<Long, Double>> edges = getEdgesDataSet(env, edgesInputPath);
		DataSet<Vertex<Long, Long>> vertices = getVerticesDataSet(env, vertexInputPath);
		Graph<Long, Long, Double> graph = Graph.fromDataSet(vertices, edges, env);
		this.graph = graph;
	}

	public Graph<Long, Long, Double> getGraph() {
		return graph;
	}

	public void setGraph(Graph<Long, Long, Double> graph) {
		this.graph = graph;
	}

	public List<Vertex<Long, Long>> getCollectedVertices() {
		return collectedVertices;
	}

	public void setCollectedVertices(List<Vertex<Long, Long>> collectedVertices) {
		this.collectedVertices = collectedVertices;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public double getCDDDelta() {
		return CDDDelta;
	}

	public void setCDDDelta(double cDDDelta) {
		CDDDelta = cDDDelta;
	}

}
