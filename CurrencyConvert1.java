//1o Θέμα 1ης Εργασίας 
//Ρούμελης Παντελής 
//ΑΜ 2249


package mypackage;

import java.io.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/currencyconvert1")
public class CurrencyConvert1 extends HttpServlet {
    private static final double EURtoUSD = 1.12; // exchange rate from Euro to USD
    private static final double EURtoGBP = 0.86; // exchange rate from Euro to GBP

    public void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        rs.setContentType("text/html");
        rs.setCharacterEncoding("utf-8");
        PrintWriter out = rs.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("<title>Currency Converter</title>");
        out.println("</head>");
        out.println("<body>");
        form_function(out);
        printError(out, rq);
        out.println("</body>");
        out.println("</html>");  
    }

    public void doPost(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
    	doGet(rq,rs);
        String amountStr = rq.getParameter("amount");
        String currency1 = rq.getParameter("currency1");
        String currency2 = rq.getParameter("currency2");
        double amount = 0.0;
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                amount = Double.parseDouble(amountStr);
                if (amount<=0) {
                	rq.setAttribute("errormsg", "Λάθος:Παρακαλώ δώστε ένα ποσό μεγαλύτερο του μηδενός για μετατροπή");
                    RequestDispatcher dispatcher = rq.getRequestDispatcher("/currencyconvert1");
                    dispatcher.forward(rq, rs);
                }
            } catch (NumberFormatException e) {
                rq.setAttribute("errormsg", "Λάθος: Παρακαλώ δώστε κανονικό ποσό για μετατροπή");
                RequestDispatcher dispatcher = rq.getRequestDispatcher("/currencyconvert1");
                dispatcher.forward(rq, rs);
            }
        }
        else if(amountStr.isEmpty()){
             rq.setAttribute("errormsg","Λάθος:Παρακαλώ δώστε ένα ποσό για μετατροπή");
             RequestDispatcher dispatcher= rq.getRequestDispatcher("/currencyconvert1");
             dispatcher.forward(rq, rs);
        }
        
        double result = currency_conv(amount, currency1, currency2);
        PrintWriter out = rs.getWriter();
        out.println("<h2>"+result+"</h2>");
    }

    public void form_function(PrintWriter out) {

    	    out.println("<h1>Μετατροπή Ποσών σε Διαφορετικά Νομίσματα</h1>");
    	    out.println("<form action=\"currencyconvert1\" method=\"post\">");
    	    out.println("Ποσό: <input type=\"text\" name=\"amount\" value=\"\">");
    	    out.println("Από: ");
    	    out.println("<select name=\"currency1\">");
    	    out.println("<option value=\"EUR\">Ευρώ</option>");
    	    out.println("<option value=\"USD\">Δολάριο ΗΠΑ</option>");
    	    out.println("<option value=\"GBP\">Λίρα Αγγλίας</option>");
    	    out.println("</select>");
    	    out.println("Σε: ");
    	    out.println("<select name=\"currency2\">");
    	    out.println("<option value=\"EUR\">Ευρώ</option>");
    	    out.println("<option value=\"USD\">Δολάριο ΗΠΑ</option>");
    	    out.println("<option value=\"GBP\">Λίρα Αγγλίας</option>");
    	    out.println("</select>");
    	    out.println("<input type=\"submit\" value=\"Μετατροπή\">");
    	    out.println("</form>");
    }

    public double currency_conv(double amount, String currency1, String currency2) {
        if (amount > 0.0) {
            if (currency1.equals(currency2)) {
                return amount;
            }
            switch (currency1) {
                case "EUR":
                    if (currency2.equals("USD")) {
                        return amount * EURtoUSD;
                    } else if (currency2.equals("GBP")) {
                        return amount * EURtoGBP;
                    }
                    break;
                case "USD":
                    if (currency2.equals("EUR")) {
                        return amount / EURtoUSD;
                    } else if (currency2.equals("GBP")) {
                        return (amount / EURtoUSD) * EURtoGBP;
                    }
                    break;
                case "GBP":
                    if (currency2.equals("EUR")) {
                        return amount / EURtoGBP;
                    } else if (currency2.equals("USD")) {
                        return (amount / EURtoGBP) * EURtoUSD;
                    }
                    break;
            }
        }
        return 1;
    }

    void printError(PrintWriter out, HttpServletRequest rq) {
        String errorMessage = (String) rq.getAttribute("errormsg");
        //String errorMessage1=(String) rq.getAttribute("errormsg1");
        if (errorMessage != null) {
            out.println("<h2 style=\"color:red\">" + errorMessage + "</h2>");
        }
    }
}