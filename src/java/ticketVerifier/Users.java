package ticketVerifier;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.*;
import javax.inject.Named;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import org.primefaces.PrimeFaces;
import sendemail.*;
import javax.servlet.http.HttpServletRequest;


@Named("Users")
@javax.faces.view.ViewScoped

public class Users implements Serializable {
    public static final String port = "1234",
            host = "localhost",
            D_url = "jdbc:derby://localhost:1527/TicketVerifier",
            D_u = "Peter",
            D_p = "Peter@2684";
    
    private String userName = "", password = "", email, phone, confirmPassword;

    public Users() {

    }

    public Users(String u, String p, String e, String phone) {
        userName = u;
        password = p;
        email = e;
        this.phone = phone;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSave() {
        if (userName == null || userName.equals("")) {
            return "";
        }
        if (password.equals(confirmPassword)) {
            return "Good";
        } else {
            return "Bad";
        }
    }//this is used in the creste user page

    public String createUser() {
        Users[] user = new Users[1];
        user[0] = new Users(userName, password, email, phone);

        if (confirmPassword.equals(password)) {
            if (DataBase.addUser(user)) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Messsage",
                                "User Account created; please use your details to login"));
                return "index.xhtml";
            } else {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System error",
                                String.format("The user name exists already in the system. \n\nPlease use another username, "
                                        + "or get along with the Administrator or Developer, if problem persists")));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            String.format("Password did not match. Try again and confirm password")));
            return "";
        }
    }

    public String changePassword() {
        try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/TicketVerifier", "Peter", "Peter@2684");
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); //ResultSet rst = st.executeQuery("SELECT EMAIL FROM USERS WHERE USERNAME = '"+userName+"'");)
                ) {
            if (confirmPassword.equals(password)) {
                st.executeUpdate("UPDATE PETER.USERS SET PASSWORD = '" + password + "' WHERE USERNAME = '" + userName + "'");

                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Notice",
                                String.format("Password has been reset. You can now login with your new password!")));
            }
            else {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            String.format("Password did not match. Try again and confirm password")));
            return "";
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            
        }
            return "index.xhtml";
    }//it is used in the forgot password 2 page

    public String logIn() {
        boolean isUser = false;
        try {
            List<Users> users = new Users().getUsers();
            for (Users user : users) {
                if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                    isUser = true;
                }
                //System.out.println("Peter is found");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (isUser) {
                return "secondPage";
            } else {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                                "No username or password supplied has been matched"));
                return "";
            }
        }
    }//it is used in the index page to validate the user login credrentials

    public List getUsers() throws SQLException {
        List<Users> users = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/TicketVerifier", "Peter", "Peter@2684");
                Statement st = con.createStatement();
                ResultSet rst = st.executeQuery("select * from users")) {
            while (rst.next()) {
                users.add(new Users(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4)));
            }

        } finally {
            return users;
        }
    }

    public ResultSet getUsersRst() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/TicketVerifier", "Peter", "Peter@2684");
        Statement st = con.createStatement();

        try {
            CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
            rowSet.populate(st.executeQuery("select * from users"));
            return rowSet;
        } finally {
            con.close();
        }
    }

    public String verifyEmail() throws SQLException {
        //boolean verified = false;
        try (Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/TicketVerifier", "Peter", "Peter@2684");
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rst = st.executeQuery("SELECT EMAIL FROM USERS WHERE USERNAME = '" + userName + "' AND EMAIL = '" + email.toLowerCase() + "'");) {

            //verified = false;
            if (rst.last()) {

                if (new SendEmail().resetPasswordEmail(email, userName)) {
                    FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Messsage",
                                    "Email sent!! In case the email cannot be found in the inboxes, please check in your spams!"));
                    PrimeFaces.current().executeScript("PF('dlg1').hide();");
                }

                return "";
            } else {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                "Unrecognized username or password. Please enter correct details."));
                return "";
            }
        } catch (SQLException ex) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "System error",
                            ex.toString()));
            ex.printStackTrace();
            return "";
        }
    }

    public String adminVerify() {
        boolean isUser = false;
        try {
            List<Users> users = new Users().getUsers();
            for (Users user : users) {
                if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                    isUser = true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (isUser) {
                PrimeFaces.current().executeScript("PF('dlg2').hide();");
                PrimeFaces.current().executeScript("PF('dlg4').show();");
            } else {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                "No username or password supplied has been matched"));

            }
        }
        //return "adminPage.xhtml";
        return "";
    }

    public static void main(String[] args) {

        //System.out.println(new Users("L", "L", "L", "L").createUser());
    }
    
    public void onPageLoad()
    {
        
        //PrimeFaces.current().executeScript("PF('dlg1').show()");
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = req.getRequestURL().toString();
        //req.getQueryString();
        
        String  query = req.getQueryString();
        if(query==null)
        {
                PrimeFaces.current().executeScript("PF('dlg1').show()");
        return;
        }
        
        String
                token1 = query.substring(query.indexOf("token1") + 7, query.indexOf("&token2")),
                token2 = query.substring(query.indexOf("token2") + 7, query.indexOf("&token3")),
                token3 = query.substring(query.indexOf("token3") + 7, query.indexOf("&username")),
                uname = query.substring(query.indexOf("username") + 9, query.indexOf("&to=")),
                to = query.substring(query.indexOf("to=") + 3, query.indexOf("&date")),
                date = query.substring(query.indexOf("date") + 5, query.indexOf("&time")),
                time = query.substring(query.indexOf("time") + 5);
        
        String token1D="",token2D="",token3D="", unameD="", toD="", dateD="", timeD="";
        
        String queryDataBaseToken = "SELECT * FROM PETER.PASSWORDRESET WHERE TOKEN1 = '"+token1+"' AND\n"
                + "TOKEN2 = '"+token2+"' AND TOKEN3 = '"+token3+"' AND USERNAME = '"+uname+"' AND EMAIL = '"+to+"'\n"
                + "AND EMAILDATE = '"+date+"' AND EMAILTIME = '"+time+"'";
        
        try (Statement st = DriverManager.getConnection(D_url, D_u, D_p).createStatement();
                ResultSet rst = st.executeQuery(queryDataBaseToken);) 
        {
            while(rst.next())
            {
                token1D = rst.getString(2);     token2D = rst.getString(3);     token3D = rst.getString(4);     
                unameD = rst.getString(5);     toD = rst.getString(6);     timeD = rst.getString(7);   dateD = rst.getString(8);
                
            }
            if(!(token1.equals(token1D) && token2.equals(token2D) && token3.equals(token3D) && uname.equals(unameD)
                    && to.equals(toD)))
            {
                PrimeFaces.current().executeScript("PF('dlg1').show()");
            }
//            else
//                System.out.println("matched url correct one");
            //if the D tokens equals the url tokens let the user submits the page and change his password
        }catch (SQLException mex) {
            //System.out.println(mex);
            mex.printStackTrace();
        }
        //System.out.println(req.getQueryString());
        //PrimeFaces.current().executeScript("window.location.assign(\"http://localhost:8080/TicketVerifier/faces/index.xhtml\");");
    }
}//End of the class Users
