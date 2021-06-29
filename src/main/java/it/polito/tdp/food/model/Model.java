package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	//QUESTA E' LA SOLUZIONE DEL PROF
	//private List<Food> cibi;
	
	private Map<Integer,Food> idMap;
	private Graph<Food,DefaultWeightedEdge> grafo;
	//private Graph<Food,DefaultWeightedEdge> graph;
	private FoodDao dao;
	private List<Adiacenza> listaAdiacenze;
	
	public Model() {
		 dao= new FoodDao();
		//idMap=new HashMap<>();
		// cibi=new ArrayList<>();
	} 
	
	
	public Map<Integer, Food> getIdMap() {
		return idMap;
	}

	public void creaGrafo(Integer portions) {
		listaAdiacenze=new ArrayList<>();
		idMap=new HashMap<>();
		this.grafo= new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
		dao.listFoodsPorzioni(idMap, portions);
		Graphs.addAllVertices(grafo, idMap.values());
		for(Food f1:idMap.values()) {
			for(Food f2:idMap.values()) {
				Adiacenza a=dao.getArchi(f1, f2);
				if(a!=null) {
					Graphs.addEdge(grafo, a.getF1(), a.getF2(), a.getPeso());
				}
			}
		}
//		listaAdiacenze=new ArrayList<>(dao.getArchi(idMap));
//		for(Adiacenza a:listaAdiacenze) {
//			Graphs.addEdge(grafo, a.getF1(), a.getF2(), a.getPeso());
//		}
		
		
	}
	
//	
//		public List<Food> getFoods(int portions) {
//			FoodDao dao = new FoodDao() ;
//			this.cibi = dao.getFoodsByPortions(portions) ;
//			System.out.println("#cibi= "+cibi.size());
//
//			// Crea un grafo nuovo e vuoto
//			this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class) ;
//			
//
//			// Aggiungi i vertici
//			Graphs.addAllVertices(this.graph, this.cibi) ;
//			
//			// Aggiungi gli archi
//			for(Food f1: this.cibi) {
//				for(Food f2: this.cibi) {
//					if(!f1.equals(f2) && f1.getFood_code()<f2.getFood_code()) {
//						Double peso = dao.calorieCongiunte(f1, f2) ;
//						if(peso!=null) {
//							Graphs.addEdge(this.graph, f1, f2, peso) ;
//						}
//					}
//				}
//			}
//			System.out.println(this.graph) ;
//			
//			return this.cibi ;
//		}
	
	
	public String geDimensioniGrafo() {
		System.out.println(String.format("Grafo creato con %d vertici e %d archi\n",
				this.grafo.vertexSet().size(),
				this.grafo.edgeSet().size()) );
		return String.format("Grafo creato con %d vertici e %d archi\n",
				this.grafo.vertexSet().size(),
				this.grafo.edgeSet().size()) ;


	}
	
	public Map<Food,Double> doCalorie(Food f) {
		List<Food> vicini=Graphs.neighborListOf(grafo, f);
		System.out.println("#lista vicini= "+vicini.size());
		if(vicini.size()>0) {
			System.out.println("Il primo vicino è "+vicini.get(0).toString());
		}
		//System.out.println("Il primo vicino è "+vicini.get(0).toString());
		Map<Food,Double> mapPesi=new HashMap<>();
		List<Adiacenza> lOrdinata=new ArrayList<>();
		for(Food ff:vicini) {
			DefaultWeightedEdge e=null;
			e=grafo.getEdge(ff, f);
			if(e==null) {
				e=grafo.getEdge(f, ff);
			}
			lOrdinata.add(new Adiacenza(f,ff,grafo.getEdgeWeight(e)));
		}
		Collections.sort(lOrdinata);
		System.out.println("#lista ordinata= "+lOrdinata.size());
		for(Adiacenza a:lOrdinata) {
			System.out.println("\n"+a.toString());
			mapPesi.put(a.getF2(), a.getPeso());
			
//			if(mapPesi.size()==5) {
//				break;
//			}
		}
		return mapPesi;
	}


	public Graph<Food, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	
	
//public List<FoodCalories> elencoCibiConnessi(Food f) {
//		
//		List<FoodCalories> result = new ArrayList<>() ;
//		
//		List<Food> vicini = Graphs.neighborListOf(this.graph, f) ;
//		
//		for(Food v: vicini) {
//			Double calorie = this.graph.getEdgeWeight(this.graph.getEdge(f, v)) ;
//			result.add(new FoodCalories(v, calorie)) ;
//		}
//		
//		Collections.sort(result);
//		
//		return result ;
//	}

	
	

}
