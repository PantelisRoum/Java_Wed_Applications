//Roumelis Pantelis 
//2249
package mypackage;
import java.util.*;
public class CurrencyDB {
    private static CurrencyDB instance=null;
    private HashMap<String,Double> exchangeRate=new HashMap<> ();
    private CurrencyDB() {
    	
    }
    public static CurrencyDB getInstance() {
    	if (instance==null) {
    		instance=new CurrencyDB();
    	}
    	return instance;
    }
    public void addExchangeCode(String code,double rate) {
    	exchangeRate.put(code, rate);
    	  
    }
    public double getExchangeRate(String code) {
    	return exchangeRate.getOrDefault(code, -1.0);
    }
}
