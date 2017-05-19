package it.polito.tdp.porto.model;

import org.jgrapht.graph.DefaultEdge;

public class Link extends DefaultEdge {

	private Paper articolo;

	public Link() {
		super();
	}

	
	public Paper getArticolo() {
		return articolo;
	}


	public void setArticolo(Paper articolo) {
		this.articolo = articolo;
	}
	
	
	
}
