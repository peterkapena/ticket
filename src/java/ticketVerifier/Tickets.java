/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketVerifier;

import java.io.Serializable;
import java.sql.*;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import org.primefaces.PrimeFaces;
import static org.primefaces.component.datatable.DataTable.PropertyKeys.summary;

@ManagedBean(name = "tickets")

public class Tickets implements Serializable {

    private String ticketNum, lastEntrance, entranceTimes, verifiedMessage;
    String ticketNumTried;
    public Tickets() {
    }//Null constructor

    public Tickets(String ticketNum) {
        this.ticketNum = ticketNum;
    }

    public Tickets(String ticketNum, String lastEntrance, String entranceTimes) {
        this.ticketNum = ticketNum;
        this.lastEntrance = lastEntrance;
        this.entranceTimes = entranceTimes;
    }//The constructor

    public String getVerifiedMessage() {
        return verifiedMessage;
    }//verifiedMessage getter

    public void setVerifiedMessage(String verifiedMessage) {
        this.verifiedMessage = verifiedMessage;
    }//verifiedMessage setter

    public String getLastEntrance() {
        return lastEntrance;
    }//lastEntrance getter

    public void setLastEntrance(String lastEntrance) {
        this.lastEntrance = lastEntrance;
    }//lastEntrance setter

    public String getEntranceTimes() {
        return entranceTimes;
    }//entranceTimes getter

    public void setEntranceTimes(String entranceTimes) {
        this.entranceTimes = entranceTimes;
    }//entranceTimes setter

    public String getTicketNum() {
        return ticketNum;
    }//ticketNum getter

    public void setTicketNum(String ticketNum) {
        this.ticketNum = ticketNum;
    }//ticketNum setter

    public String getVerify() {
        try {
            if (ticketNum.equals("") || ticketNum == null || ticketNum.length() > 3) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Oups!", "An incorrect ticket number is detected"));
                return "";
            }//for 

            int nOfEntrances = DataBase.addToDatabase(Integer.parseInt(ticketNum));
            if (nOfEntrances >= 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Wait", String.format("This Guest has been In Yet!\n.Did the He confirm how many times?.\nThe Database says it is: %d", nOfEntrances)));
                ticketNumTried = ticketNum;
                
                
                UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
                
                //String ticketForm1 = String.valueOf(view.findComponent("form1:ticketNumber").getAttributes().get("value"));
                view.findComponent("fDialog3:ticketNumber").getAttributes().put(
                        "value",view.findComponent("form1:ticketNumber").getAttributes().get("value")
                    );
                
                System.out.println(String.valueOf(view.findComponent("form1:ticketNumber").getAttributes().get("value")));
                
                PrimeFaces.current().executeScript("PF('dlg3').show();");
                return "";
            } else {

                DataBase.addToDatabase(Integer.parseInt(ticketNum));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "All Good!", "The guest can get in"));
                verifiedMessage = "";
                return verifiedMessage;
            }
        } catch (Exception ex) {
            return "";
        }
    }//the verification method  ***********************************************
    
    public String getVerified() {
        try {
            DataBase.verified(Integer.parseInt(ticketNum));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "All Good!", "The guest can get in"));
            return "secondPage.xhtml";
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System Error", "Ticket Number = "+ticketNum));
            return "";
        }
        //return"index.xhtml";
    }


    public ResultSet getTickets() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/TicketVerifier", "Peter", "Peter@2684");
        Statement st = con.createStatement();

        try {
            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(st.executeQuery("select * from Tickets"));
            return rowSet;
        } finally {
            con.close();
        }
    }

    public String getReset() {
        if(DataBase.resetDatabase()>0)
        {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_INFO, 
                            "Message", "All the captured tickets has been deleted from the system. The App has been set new for another concert"));
            return "index.xhtml";
        }
        else 
        {
            FacesContext.getCurrentInstance().
                    addMessage(null, 
                                new FacesMessage(
                                FacesMessage.SEVERITY_INFO, 
                                "Message", "There was nothing found to delete. You have been redirected to the login page for the secutity purposes."));
            return "index.xhtml";
        }
    }
    
    public String invalidTicketNumber()
    {
        FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_WARN, 
                            "Alert", 
                            "The Guest should not get in then."));
        return "secondPage.xhtml";
    }
    
    public String editTicket()
    {
        try{
            int tNum = Integer.parseInt(ticketNum);
            if(DataBase.editTicket(tNum, lastEntrance, entranceTimes))
            {
                FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_INFO, 
                            "Info", 
                            "Details changed"));
                return"";
            }
            else
            {
                FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_INFO, 
                            "Error", 
                            "Details could not be changed or the ticket number is nonexistence "));
                return"";
            }
        }catch(NumberFormatException ex){
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, 
                            "Error", 
                            ex.toString()));
                return"";
        }
    }

    public static void main(String[] args) {
        System.out.println(new Tickets("111").getVerified());
    }//The main method is for quick test

}




