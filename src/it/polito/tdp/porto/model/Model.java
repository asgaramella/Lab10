package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;



public class Model {
	
	private UndirectedGraph<Author,DefaultEdge> graph;
	private List<Author> autori;
	private AuthorIdMap authorIdMap;
	private PaperIdMap paperIdMap;
	private DijkstraShortestPath<Author,DefaultEdge> dijkstra;
	
	public	Model() {
	}
	
	public List<Author> getAutori(){
		if(this.autori==null){
			PortoDAO dao=new PortoDAO();
			this.autori=dao.listAutori(authorIdMap);
			for(Author a: autori){
				dao.getArticoliDiAutore(a,paperIdMap);
			}
		}
		return this.autori;
	}
	
	private UndirectedGraph<Author, DefaultEdge> getGrafo(){
		//affinchè grafo sia nullo new graph nel metodo di creazione e non nel costruttore di model!
		if(this.graph==null){
			this.creaGrafo();
		}
		return this.graph;
		
	}
	
	public void creaGrafo(){
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		
		PortoDAO dao=new PortoDAO();
		Graphs.addAllVertices(graph, this.getAutori());
		
		for(Author autore:graph.vertexSet()){
			for(Author coautore:dao.getCoautori(autore,authorIdMap)){
				graph.addEdge(autore, coautore);
			}
		}
		
		
	}
	
	public List<Author> trovaCoautori(Author a){
	  this.getGrafo();
	  return Graphs.neighborListOf(graph, a);
		
	}

	public List<Paper> getSequenza(Author a1, Author a2) {
		dijkstra=new DijkstraShortestPath<Author,DefaultEdge>(graph, a1, a2);
		PortoDAO dao=new PortoDAO();
		List<Paper> ptemp= new ArrayList<Paper>();
		for(DefaultEdge e:dijkstra.getPathEdgeList()){
			boolean trovato=false;
			for(Paper p1:graph.getEdgeSource(e).getArticoli()){
				while(trovato==false){
				for(Paper p2:graph.getEdgeTarget(e).getArticoli()){
					while(trovato==false){
					if(p1.equals(p2)){
						ptemp.add(p1);
						trovato=true;
					}
				}
				}
			}
			}
			
			
		}
		return ptemp;
	}

	public List<Author> getAutoriRimanenti(Author autore) {
		List<Author> ltemp=new ArrayList(this.getAutori());
		ltemp.remove(autore);
		ltemp.removeAll(this.trovaCoautori(autore));
		return ltemp;
	}
	
	
	
}
