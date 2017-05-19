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
	
	private UndirectedGraph<Author,Link> graph;
	private List<Author> autori;
	private AuthorIdMap authorIdMap=new AuthorIdMap();
	private PaperIdMap paperIdMap=new PaperIdMap();
	private DijkstraShortestPath<Author,Link> dijkstra;
	
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
	
	private UndirectedGraph<Author, Link> getGrafo(){
		//affinchè grafo sia nullo new graph nel metodo di creazione e non nel costruttore di model!
		if(this.graph==null){
			this.creaGrafo();
		}
		return this.graph;
		
	}
	
	public void creaGrafo(){
		this.graph = new SimpleGraph<>(Link.class) ;
		
		PortoDAO dao=new PortoDAO();
		Graphs.addAllVertices(graph, this.getAutori());
		
		for(Author autore:graph.vertexSet()){
			for(Author coautore:dao.getCoautori(autore,authorIdMap)){
				Link ltemp=graph.addEdge(autore, coautore);
				//occhio quando usi subito dopo creazione l'arco se già esistente nel caso del grafo semplice ti resituisce null !!!
				if(ltemp!=null){
				boolean trovato=false;
				for(Paper p1:autore.getArticoli()){
					if(trovato==true){
						break;
					}
					for(Paper p2:coautore.getArticoli()){
						if(p1.equals(p2)){
							ltemp.setArticolo(p1);
							trovato=true;
							break;
						}
					}
			}
		}
		}
		
		
	}
	}
	
	public List<Author> trovaCoautori(Author a){
	  this.getGrafo();
	  return Graphs.neighborListOf(graph, a);
		
	}

	public List<Paper> getSequenza(Author a1, Author a2) {
		dijkstra=new DijkstraShortestPath<Author,Link>(graph, a1, a2);
		List<Paper> ptemp= new ArrayList<Paper>();
		for(Link l:dijkstra.getPathEdgeList()){
				ptemp.add(l.getArticolo());
				
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
