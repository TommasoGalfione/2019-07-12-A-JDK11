/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodCalories;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Creazione grafo...");
    	Integer porzioni=Integer.parseInt(this.txtPorzioni.getText());
    	
    	model.creaGrafo(porzioni);
    	this.txtResult.appendText(model.geDimensioniGrafo());
    	this.boxFood.getItems().addAll(model.getIdMap().values());
    	
    	//metodo prof
//    	String porzioniStr = txtPorzioni.getText();
//		try {
//			int portions = Integer.parseInt(porzioniStr);
//			List<Food> cibi = model.getFoods(portions);
//			boxFood.getItems().clear();
//			boxFood.getItems().addAll(cibi) ;
//			
//		} catch (NumberFormatException ex) {
//			txtResult.appendText("ERRORE: Devi inserire un numero\n");
//			return;
//		}
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Analisi calorie...");
    	Food food=this.boxFood.getValue();
    	int i=0;
    	for(Food f:model.doCalorie(food).keySet()) {
    		i++;
    		txtResult.appendText("\nIl cibo "+f+" con "+model.doCalorie(food).get(f)+" calorie aggiunte");
    		if(i>4) {
    			break;
    		}
    	}
    	
    	
    	//metodo prof
//    	Food f = boxFood.getValue() ;
//		
//		if(f==null) {
//			txtResult.appendText("ERRORE: devi selezionare un cibo\n");
//			return ;
//		}
//		
//		List<FoodCalories> lista = model.elencoCibiConnessi(f);
//		
//		for(int i=0; i<5 && i<lista.size(); i++) {
//			txtResult.appendText(String.format("%s %f\n",
//					lista.get(i).getFood().getDisplay_name(),
//					lista.get(i).getCalories()));
//		}
//    	
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
