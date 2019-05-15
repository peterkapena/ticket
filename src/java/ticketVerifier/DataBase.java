/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ticketVerifier;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class DataBase {

    static final String DATABASE_URL = "jdbc:derby://localhost:1527/TicketVerifier";

    public static int addToDatabase(int ticketNum) //Add to the database and get the number of entrances
    {
        int numberOfEntrances = 0;

        try (Statement st = DriverManager.getConnection(DATABASE_URL, "Peter", "Peter@2684").createStatement();) {
            //for the entrance times, the app should display a message indicating how many times the guest has entered into the concert
            //, it can  be implemented in the catch
            st.executeUpdate("INSERT INTO Tickets ( TicketNum, LastEntrance, EntranceTimes ) VALUES (" + ticketNum + ", '" + LocalTime.now().toString().substring(0,8) + "', " + 1 + ")");
            //JOptionPane.showMessageDialog(null, "Done!", "Submitted", 1);
            //return 0;
        } catch (SQLException e) //The following catch codes snippets will be used when an available ticket in the database has been entered
        {
            try (Statement st = DriverManager.getConnection(DATABASE_URL, "Peter", "Peter@2684").createStatement();
                    ResultSet rst = st.executeQuery("select EntranceTimes from tickets where TicketNum = " + ticketNum + "")) {
                while (rst.next()) {
                    numberOfEntrances = rst.getInt(1);//get the number of entrances
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }//The catch codes for the reentrance of a guest
        finally {
            return numberOfEntrances;
        }
    }

    public static void verified(int ticketNum) {
        try (Statement st = DriverManager.getConnection(DATABASE_URL, "Peter", "Peter@2684").createStatement();) {
            String sql = "UPDATE PETER.TICKETS SET \"LASTENTRANCE\" = '" + LocalTime.now() + "', "
                    + "\"ENTRANCETIMES\" = (select \"ENTRANCETIMES\" from tickets WHERE TICKETNUM = " + ticketNum + ") + 1 "
                    + "WHERE TICKETNUM = " + ticketNum + "\n"
                    + "";
            st.executeUpdate(sql);
            System.out.println("Well done");
        } catch (SQLException ex) { System.out.println(ex);
        }
    }

    public static int resetDatabase() {
        
        try (Statement st = DriverManager.getConnection(DATABASE_URL, "Peter", "Peter@2684").createStatement();) {
            System.out.println("No error");            
            return st.executeUpdate("DELETE FROM Tickets");

        }   catch (SQLException ex) {
                System.out.println(ex);
                return 0;
            }
        
    }
    
    public static List<Users> getUsersInDatabase() {
        List<Users> users = new ArrayList<>();
        try (Statement st = DriverManager.getConnection(DATABASE_URL).createStatement();
                ResultSet rst = st.executeQuery("select * from users");) {
            while (rst.next()) {
                Users u = new Users(rst.getString("Username"), rst.getString("Password"), rst.getString("Email"), rst.getString("PhoneNumber"));
                users.add(u);
                //System.out.print(users.get(0).getEmail());
            }
        } catch (SQLException ex) {
        }
        return users;
    }
    
    public static boolean addUser(Users[] user)
    {
        try (Statement st = DriverManager.getConnection(DATABASE_URL, "Peter", "Peter@2684").createStatement();) {
            String sql = "INSERT INTO PETER.USERS (USERNAME, PASSWORD, EMAIL, PHONENUMBER) \n" +
                        "VALUES ('"+user[0].getUserName()+"', '"+user[0].getPassword()+"', '"+user[0].getEmail().toLowerCase()+"',"
                        + "'"+user[0].getPhone()+"')";
            st.executeUpdate(sql);
            //System.out.println("Well done");
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
        //userName="", password="", email, phone,
    }
    
    //UPDATE PETER.TICKETS SET "TICKETNUM" = 959 WHERE TICKETNUM = 999;
public static boolean editTicket(int ticketNum, String lastEntrance,String entranceTimes )
    {
        try (Statement st = DriverManager.getConnection(DATABASE_URL, "Peter", "Peter@2684").createStatement();
                ResultSet rst = st.executeQuery("SELECT * FROM PETER.TICKETS")) 
        {
            if(rst.next())
            {
                st.executeUpdate(""
                    + "UPDATE PETER.TICKETS SET \"LASTENTRANCE\" = '"+lastEntrance+"', \"ENTRANCETIMES\" = "+entranceTimes+" WHERE TICKETNUM = "+ticketNum+"\n" +
                        "");
            System.out.println("Done");
            return true;
            }
            else return false;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    public static void main(String[] args) {
        //DataBase.editTicket((110));
    }

}
