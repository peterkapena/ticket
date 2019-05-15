/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketVerifier;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.primefaces.PrimeFaces;
import static ticketVerifier.Users.D_p;
import static ticketVerifier.Users.D_u;
import static ticketVerifier.Users.D_url;

/**
 *
 * @author Peter
 */
public class TestIt {
    public static void main(String[] args) {
       String query = "http://localhost:28322/TicketVerifier/faces/forgotPass2.xhtml?token=PGiEkzfeU2fTRA 4108349732002467968 1006013404682030920"
       ,token = query.substring(query.indexOf("token") + 6)
               ;
       
//String petr = "peter";
        
        System.out.println(token);
        
        try (Statement st = DriverManager.getConnection(D_url, D_u, D_p).createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rst = st.executeQuery("SELECT ISRESET FROM PETER.PASSWORDRESET WHERE TOKEN = '"+token+"'");) 
        {
            boolean isReset=true;
            
            while(rst.next())
            {
                isReset = rst.getBoolean(1);
                System.out.println("checked");
            }
            if(!isReset)
            {
                System.out.println("No");
                //PrimeFaces.current().executeScript("PF('dlg1').show()");
            }
            else
                System.out.println("matched url correct one");
            //if the D tokens equals the url tokens let the user submits the page and change his password
            rst.close();        st.close();
            
        }catch (SQLException mex) {
            
            //System.out.println(mex);
            mex.printStackTrace();
        }
    }
}
