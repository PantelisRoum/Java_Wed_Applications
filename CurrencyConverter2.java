//2ο Θέμα 
//Ρούμελης Παντελής
//2249


package mypackage;

import mypackage.CurrencyDB;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/currencyconverter2")
public class CurrencyConverter2 extends HttpServlet {
    private CurrencyDB currencyDB;

    public void init() {
        System.out.println("Initializing CurrencyConverter2 servlet...");
        currencyDB = CurrencyDB.getInstance();
        loadFromTXT();
        System.out.println("CurrencyConverter2 servlet initialized successfully.");
    }

    private void loadFromTXT() {
        String filePath = getServletContext().getRealPath("/WEB-INF/euro_rates.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            System.out.println("Loading exchange rates from file: " + filePath);
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String currencyCode = parts[0];
                double currencyRate = Double.parseDouble(parts[2]);
                currencyDB.addExchangeCode(currencyCode, currencyRate);
            }
            System.out.println("Exchange rates loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading exchange rates: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest rq, HttpServletResponse rs) throws IOException, ServletException {
        rs.setContentType("text/html");
        rs.setCharacterEncoding("utf-8");
        PrintWriter out = rs.getWriter();
        out.println("<DOCTYPE!><html><body>");
        functionForm(out);
        printError(out, rq);
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest rq, HttpServletResponse rs) throws IOException, ServletException {
    	doGet(rq,rs);
        System.out.println("You entered into Post method");
        String amountStr = rq.getParameter("amount");
        String currency1 = rq.getParameter("currency1");
        String currency2 = rq.getParameter("currency2");

        double amount = 0.0;
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                amount = Double.parseDouble(amountStr);
                if(amount<=0) {
                	rq.setAttribute("errormsg", "Λάθος:Παρακαλώ δώστε ένα ποσό μεγαλύτερο του μηδενός");
                    RequestDispatcher dispatcher = rq.getRequestDispatcher("NewFile.jsp");
                    dispatcher.forward(rq, rs);
                    return;	
                }
            } catch (NumberFormatException e) {
                rq.setAttribute("errormsg", "Λάθος:Παρακαλώ δώστε κανονικό ποσό για μετατροπή");
                RequestDispatcher dispatcher = rq.getRequestDispatcher("NewFile.jsp");
                dispatcher.forward(rq, rs);
                return;
            }
        } else {
            rq.setAttribute("errormsg", "Λάθος: Παρακαλώ δώστε ένα ποσό για μετατροπή");
            RequestDispatcher dispatcher = rq.getRequestDispatcher("/NewFile.jsp");
            dispatcher.forward(rq, rs);
            return;
        }

        double result = convertFunction(amount, currency1, currency2);
        PrintWriter out = rs.getWriter();
        out.println("<h2>" + result + "</h2>");
    }

    public double convertFunction(double amount, String currency1, String currency2) {
        double rate1 = currencyDB.getExchangeRate(currency1);
        System.out.println("the value of rate1 is " + rate1);
        double rate2 = currencyDB.getExchangeRate(currency2);
        System.out.println("the value of rate2 is: " + rate2);

        if (rate1 == -1.0 || rate2 == -1.0) {
            // Invalid currency code(s)
            return -1.0; // or handle error as needed
        }

        if (currency1.equals(currency2)) {
            return amount;
        }

        // if the currency one is Euro we just multiplying the amount with rate of the currency2
        if (currency1.equals("EUR")) {
            return amount * rate2;
        }

        // converting any type of currency to Euro
        double amountInEuro = amount / rate1;
        // converting from Euro to the other currency
        return amountInEuro * rate2;
    }

    // HTML form function
    public void functionForm(PrintWriter out) {
    	out.println("<h2>Μετατροπή Ποσών σε Διαφορετικά Νομίσματα</h2>");
        out.println("<form action=\"currencyconverter2\" method=\"post\">");
        out.println("Ποσό:<input type=\"text\" name=\"amount\" value=\"\">");
        out.println("Από:<select name=\"currency1\">");
        out.println("<option value=\"EUR\">Ευρώ</option>");
        out.println("<option value=\"USD\">Δολλάριο ΗΠΑ</option>");
        out.println("<option value=\"GBP\">Λίρα Αγγλιάς</option>");
        out.println("<option value=\"AUD\">Δολάριο Αυστραλίας</option>");
        out.println("<option value=\"CAD\">Δολλάριο Καναδά</option>");
        out.println("<option value=\"CHF\">Φράγκο Ελβετία</option>");
        out.println("<option value=\"JPY\">Γιέν Ιαπωνίας</option>");
        out.println("<option value=\"ALL\">Λεκ Αλβανίας</option>");
        out.println("<option value=\"CNY\">Γουάν Κίνας</option>");
        out.println("<option value=\"RUB\">Ρούβλι Ρωσίας</option>");
        out.println("</select>");
        out.println("Σε:");
        out.println("<select name=\"currency2\">");
        out.println("<option value=\"EUR\">Ευρώ</option>");
        out.println("<option value=\"USD\">Δολλάριο ΗΠΑ</option>");
        out.println("<option value=\"GBP\">Λίρα Αγγλιάς</option>");
        out.println("<option value=\"AUD\">Δολάριο Αυστραλίας</option>");
        out.println("<option value=\"CAD\">Δολλάριο Καναδά</option>");
        out.println("<option value=\"CHF\">Φράγκο Ελβετία</option>");
        out.println("<option value=\"JPY\">Γιέν Ιαπωνίας</option>");
        out.println("<option value=\"ALL\">Λεκ Αλβανίας</option>");
        out.println("<option value=\"CNY\">Γουάν Κίνας</option>");
        out.println("<option value=\"RUB\">Ρούβλι Ρωσίας</option>");
        out.println("</select>");
        out.println("<input type=\"submit\" value=\"Μετατροπή\">");
        out.println("</form>");
    }

    // Function to print error message
    public void printError(PrintWriter out, HttpServletRequest rq) {
        String errorMsg = (String) rq.getAttribute("errormsg");
        if (errorMsg != null && !errorMsg.isEmpty()) {
            out.println("<h2 style=\"color:red\">" + errorMsg + "</h2>");
        }
    }
}