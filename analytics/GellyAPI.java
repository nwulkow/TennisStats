package analytics;

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

import tools.StatsTools;
import tools.Tools;


public class GellyAPI {

	public static void main(String[] args) throws Exception {

		String edgesInputPath = "C:/Users/Niklas/TennisStatsData/resultRatioMatrix_Edges.csv";
		String vertexInputPath = "C:/Users/Niklas/TennisStatsData/resultRatioMatrix_vertices.csv";
		boolean fileOutput = false;
		pageRank(edgesInputPath, vertexInputPath, fileOutput);

		
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

	
	public static void communityDetection(Graph<Long, Long, Double> graph, boolean fileOutput, ExecutionEnvironment env) throws Exception{
		
		//ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		Integer maxIterations = CommunityDetectionData.MAX_ITERATIONS;
		Double delta = CommunityDetectionData.DELTA;
		DataSet<Vertex<Long, Long>> communityVertices = graph.run(new CommunityDetectionAlgorithm(maxIterations, delta))
				.getVertices();
		if (fileOutput) {
			communityVertices.writeAsCsv("C:/Users/Niklas/TennisStatsData/test_gellyapi_output", "\n", ",");
			// since file sinks are lazy, we trigger the execution explicitly
			env.execute("Executing Community Detection Example");
		}
		else {
			communityVertices.print();
		}
	}
	
	public static void communityDetection(String edgesInputPath, String vertexInputPath, boolean fileOutput) throws Exception{
		
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		
		DataSet<Edge<Long, Double>> edges = getEdgesDataSet(env, edgesInputPath);
		DataSet<Vertex<Long, Long>> vertices = getVerticesDataSet(env, vertexInputPath);
		Graph<Long, Long, Double> graph = Graph.fromDataSet(vertices, edges, env);
		communityDetection(graph, fileOutput, env);
	}
	
	public static List<Vertex<Long,Double>> pageRank(Graph<Long, Double, Double> graph, boolean fileOutput, ExecutionEnvironment env) throws Exception{
		
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
	
	public static List<Vertex<Long,Double>> pageRank(String edgesInputPath, String vertexInputPath, boolean fileOutput) throws Exception{
		
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

	private static DataSet<Edge<Long, Double>> getEdgesDataSet(ExecutionEnvironment env, String edgesInputPath) {
		return env.readCsvFile(edgesInputPath).ignoreComments("#").fieldDelimiter(",").lineDelimiter("\n")
				.types(Long.class, Long.class, Double.class).map(new Tuple3ToEdgeMap<Long, Double>());

	}

	private static DataSet<Vertex<Long, Long>> getVerticesDataSet(ExecutionEnvironment env, String vertexInputPath) {
		return env.readCsvFile(vertexInputPath).ignoreComments("#").fieldDelimiter(",").lineDelimiter("\n")
				.types(Long.class, Long.class).map(new Tuple2ToVertexMap<Long, Long>());

	}

}
