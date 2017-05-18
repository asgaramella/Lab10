package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Paper;
import it.polito.tdp.porto.model.PaperIdMap;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db");                    
		}                         
	}

	
	public List<Author> listAutori(AuthorIdMap authorIdMap) {
		final String sql ="SELECT * "+
				"FROM author ORDER BY lastname";

                                                                                 
		try {                                                           
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);                               
			                                                                                                                         
			ResultSet res = st.executeQuery() ;
			
			List<Author> list = new ArrayList<>() ;
			
			while(res.next()) {
				Author a= authorIdMap.get(res.getInt("id"));
				if(a==null){
				a = new Author(res.getInt("id"), res.getString("lastname"), res.getString("firstname")) ;
				a = authorIdMap.put(a);
				}
				
				list.add(a);
			
			}
			
			res.close();
			conn.close();
			
			return list ;
		} catch (SQLException e) {
		
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Author> getCoautori(Author autore, AuthorIdMap authorIdMap) {
			final String sql ="SELECT DISTINCT author.id,author.lastname,author.firstname "+
					"from creator,author "+
					"WHERE author.id=creator.authorid && creator.eprintid IN (select cc.eprintid "+
					"FROM creator as cc "+
					"WHERE cc.authorid=?)";

	                                                                                 
			try {                                                           
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);  
				
				st.setInt(1, autore.getId());
				                                                                                                                         
				ResultSet res = st.executeQuery() ;
				
				List<Author> list = new ArrayList<>() ;
				
				while(res.next()) {
					
					Author a= authorIdMap.get(res.getInt("id"));
					if(a==null){
					a = new Author(res.getInt("id"), res.getString("lastname"), res.getString("firstname")) ;
					a = authorIdMap.put(a);
					}
					list.add(a);
				
				}
				
				list.remove(autore);
				res.close();
				conn.close();
				
				return list ;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null ;
			}

			
		}

	public void getArticoliDiAutore(Author a, PaperIdMap paperIdMap) {
		final String sql ="SELECT paper.eprintid, paper.title, paper.issn, paper.publication, paper.`type`, paper.types "+
				"from creator,paper "+
				"WHERE paper.eprintid=creator.eprintid &&  creator.authorid=?";

                                                                                 
		try {                                                           
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);  
			
			st.setInt(1, a.getId());
			                                                                                                                         
			ResultSet rs = st.executeQuery() ;
			
			
			while(rs.next()) {
				
				
				
				Paper paper=paperIdMap.get(rs.getInt("paper.eprintid"));
				if(paper==null){
					paper=new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
							rs.getString("publication"), rs.getString("type"), rs.getString("types"));
					paper= paperIdMap.put(paper);
				}
				
				a.getArticoli().add(paper);
			
			
			}
			
		
			rs.close();
			conn.close();
			
		
		} catch (SQLException e) {
		
			e.printStackTrace();
			
		}

		
		
	}

	}                                                                        
		

		
	
