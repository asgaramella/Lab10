package it.polito.tdp.porto.model;

import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;



public class Model {
	
	private UndirectedGraph<Author,DefaultEdge> graph;
	private List<Author> autori;
	private AuthorIdMap authorIdMap;
	private PaperIdMap paperIdMap;
	
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
	
	
	
}
