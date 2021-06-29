package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public void listFoodsPorzioni(Map<Integer,Food> idMap,Integer porzioni){
		String sql = "SELECT f.* "
				+ "FROM food f,portions p "
				+ "WHERE f.food_code=p.food_code "
				+ "GROUP BY f.food_code,f.display_name "
				+ "HAVING COUNT(f.food_code)=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			
			//List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Food f=(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
					idMap.put(f.getFood_code(), f);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			//return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return  ;
		}

	}
	
	public Adiacenza getArchi(Food f1,Food f2) {
		String sql = "SELECT c1.food_code,c2.food_code, AVG( cc.condiment_calories) AS peso "
				+ "FROM condiment cc, food_condiment c1,food_condiment c2 "
				+ "WHERE c1.condiment_code= c2.condiment_code AND cc.condiment_code=c1.condiment_code AND "
				+ "	c1.food_code<>c2.food_code AND c1.food_code=? AND c2.food_code=? "
				+ "GROUP BY c1.food_code,c2.food_code";
		
		

		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, f1.getFood_code());
			st.setInt(2, f2.getFood_code());
			
			
			Adiacenza result = null ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					
					if(res.getDouble("peso")!=0) {
						Adiacenza a=new Adiacenza(f1,f2,res.getDouble("peso"));
						result=a;
						
					}
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	//metodi prof
	public List<Food> getFoodsByPortions(int portions) {
		String sql = "SELECT food.food_code, food.display_name, COUNT(DISTINCT p.portion_id) AS CNT " + 
				"FROM food, portions p " + 
				"WHERE food.food_code=p.food_code " + 
				"GROUP BY food.food_code " + 
				"HAVING CNT=? " +
				"ORDER BY food.display_name ASC" ;
		
		List<Food> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, portions);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Food(
						res.getInt("food_code"),
						res.getString("display_name")) ) ;
			}
			
			conn.close();
			return result ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Double calorieCongiunte(Food f1, Food f2) {
		String sql = "SELECT fc1.food_code, fc2.food_code,  " + 
				"		 AVG(condiment.condiment_calories) AS cal " + 
				"FROM food_condiment AS fc1, food_condiment AS fc2, condiment " + 
				"WHERE fc1.condiment_code=fc2.condiment_code " + 
				"AND condiment.condiment_code=fc1.condiment_code " + 
				"AND fc1.id<>fc2.id " + 
				"AND fc1.food_code=? " + 
				"AND fc2.food_code=? " + 
				"GROUP BY fc1.food_code, fc2.food_code" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, f1.getFood_code());
			st.setInt(2, f2.getFood_code());
			
			ResultSet res = st.executeQuery() ;
			
			Double calories = null ;
			if(res.first()) {
				calories = res.getDouble("cal") ;
			}
			// altimenti rimane null
			
			conn.close();
			return calories ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}
