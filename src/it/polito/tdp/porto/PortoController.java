package it.polito.tdp.porto;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	Author autore=boxPrimo.getValue();
    	if(autore!=null){
    		
    		for(Author atemp:model.trovaCoautori(autore))
    			txtResult.appendText(atemp.toString()+"\n");
    		
    		boxSecondo.getItems().clear();
    		boxSecondo.getItems().addAll(model.getAutoriRimanenti(autore));
    	}
    	else
    		txtResult.appendText("Inserire autore! \n");
    	

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	Author a1=boxPrimo.getValue();
    	Author a2=boxSecondo.getValue();
    	if(a1!=null && a2!=null){
    	for(Paper ptemp:model.getSequenza (a1, a2))
    		txtResult.appendText(ptemp.toString()+"\n");
    			
    			
    	}
    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) {
		this.model=model;
		boxPrimo.getItems().addAll(model.getAutori());
	}
}
