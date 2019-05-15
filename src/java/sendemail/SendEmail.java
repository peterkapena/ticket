package sendemail;

import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.*;
import org.primefaces.PrimeFaces;

public class SendEmail {

    public static final String port = "1234",
            host = "localhost",
            D_url = "jdbc:derby://localhost:1527/TicketVerifier",
            D_u = "Peter",
            D_p = "Peter@2684";

    public static void main(String[] args) {
        new SendEmail().resetPasswordEmail("p@p.com", "Peter");

    }

    public void send(String to, String sub, String msg, final String user, final String pass) {
        Properties props = new Properties();

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "fallse");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        }
        );

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(sub);
            message.setText(msg);

            Transport.send(message);
            System.out.println("Msg Sent!");
        } catch (MessagingException mex) {
            System.out.println(mex);
        }
    }

    public boolean resetPasswordEmail(String to, String username) {

        Properties props = new Properties();

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");

        Session session = Session.getInstance(props);

        try (Statement st = DriverManager.getConnection(D_url, D_u, D_p).createStatement();) {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress("noreply@ticket.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Password reset link", "UTF-8");

            SecureRandom secureRandom = new SecureRandom();
            byte[] rand = new byte[10];
            secureRandom.nextBytes(rand);
            String token1 = Base64.getUrlEncoder().withoutPadding().encodeToString(rand);

            Random randLong = new Random();
            long token2 = randLong.nextLong() * 1000;
            long token3 = randLong.nextLong() * 1000;
            
             long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            java.sql.Time time = new java.sql.Time(millis);
            //username = "Peter";
//            String msg = "Good Day  "
//                    + "<b>" + username + "</b>"
//                    + "<br/><br/>"
//                    + "The link below will redirect you to the password reset page:\n"
//                    + "<br/>"
//                    + "<a href=\"http://localhost:28322/TicketVerifier/faces/forgotPass2.xhtml?token1=" + token1 + "&"
//                    + "token2=" + token2 + "&token3=" + token3 + "\"> click here </a>"
//                    + "<br/><br/>Please note that this link will be inaccessible after a duration of 15 minutes."
//                    + "<br/><br/>If the link expires, please regenerate the email for the link one more time and the email will be resent."
//                    + "<br/><br/><br/> <b>Ticket Verifier Team</b>"
//                    + "<br/><b><a href=\"tel:+27 81 217 4767\"> Mobile</a></b>"
//                    + "<br/> <b><a href=\"mailto:passycahspassy@gmail.com\">Email</a></b>";

            String msg = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\"><head><META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=us-ascii\"><meta name=Generator content=\"Microsoft Word 15 (filtered medium)\"><style><!--\n"
                    + "/* Font Definitions */\n"
                    + "@font-face\n"
                    + "	{font-family:\"Cambria Math\";\n"
                    + "	panose-1:2 4 5 3 5 4 6 3 2 4;}\n"
                    + "@font-face\n"
                    + "	{font-family:Calibri;\n"
                    + "	panose-1:2 15 5 2 2 2 4 3 2 4;}\n"
                    + "/* Style Definitions */\n"
                    + "p.MsoNormal, li.MsoNormal, div.MsoNormal\n"
                    + "	{margin:0in;\n"
                    + "	margin-bottom:.0001pt;\n"
                    + "	font-size:11.0pt;\n"
                    + "	font-family:\"Calibri\",sans-serif;\n"
                    + "	mso-fareast-language:EN-US;}\n"
                    + "a:link, span.MsoHyperlink\n"
                    + "	{mso-style-priority:99;\n"
                    + "	color:#0563C1;\n"
                    + "	text-decoration:underline;}\n"
                    + "a:visited, span.MsoHyperlinkFollowed\n"
                    + "	{mso-style-priority:99;\n"
                    + "	color:#954F72;\n"
                    + "	text-decoration:underline;}\n"
                    + "span.EmailStyle17\n"
                    + "	{mso-style-type:personal-compose;\n"
                    + "	font-family:\"Calibri\",sans-serif;\n"
                    + "	color:windowtext;}\n"
                    + ".MsoChpDefault\n"
                    + "	{mso-style-type:export-only;\n"
                    + "	font-family:\"Calibri\",sans-serif;\n"
                    + "	mso-fareast-language:EN-US;}\n"
                    + "@page WordSection1\n"
                    + "	{size:8.5in 11.0in;\n"
                    + "	margin:1.0in 1.0in 1.0in 1.0in;}\n"
                    + "div.WordSection1\n"
                    + "	{page:WordSection1;}\n"
                    + "--></style><!--[if gte mso 9]><xml>\n"
                    + "<o:shapedefaults v:ext=\"edit\" spidmax=\"1026\" />\n"
                    + "</xml><![endif]--><!--[if gte mso 9]><xml>\n"
                    + "<o:shapelayout v:ext=\"edit\">\n"
                    + "<o:idmap v:ext=\"edit\" data=\"1\" />\n"
                    + "</o:shapelayout></xml><![endif]--></head><body lang=EN-GB link=\"#0563C1\" vlink=\"#954F72\"><div class=WordSection1><p class=MsoNormal><span style='font-size:12.0pt'>Good Day</span><span style='font-size:14.0pt'> </span><b><span style='font-size:12.0pt'>" + username + "</span></b><br><span style='font-size:10.0pt'><br>The link below will redirect you to the password reset page: <br><a href=\"http://localhost:28322/TicketVerifier/faces/forgotPass2.xhtml?token="
                    + token1 + token2 + token3 + "\"><span style='color:red'>click here</span> </a>.<br><br>Please note that this link will be inaccessible after a duration of 15 minutes.<br>If the link expires, please regenerate the email for the link one more time and the email will be resent</span>.<br><br><span style='color:#4472C4'>________________________<br></span><b><span style='font-size:16.0pt;color:#4472C4'>Ticket Verifier<sup>T</sup></span></b><span style='font-size:10.0pt;color:#4472C4'> <br></span><b><span style='font-size:10.0pt;color:red'><a href=\"tel:+27%2081%20217%204767\"><span style='color:red'>Mobile</span></a></span></b><span style='font-size:10.0pt;color:red'><br><b><a href=\"mailto:passycahspassy@gmail.com\"><span style='color:red'>Email</span></a></b></span><span style='color:red'> </span><span style='color:#4472C4;mso-fareast-language:EN-GB'><o:p></o:p></span></p><p class=MsoNormal><o:p>&nbsp;</o:p></p></div></body></html>";
           
            String token = token1 + token2 + token3;
            if(token.length()>=100)
            {
                token = token.substring(0,99);
            }
            
            String sqlInsertPassReset = "INSERT INTO PETER.PASSWORDRESET (TOKEN, USERNAME, EMAIL, ISRESET) \n"
                    + "VALUES ('" + token + "', '" + username + "', '" + to + "', false)";
            st.executeUpdate(sqlInsertPassReset);

            message.setContent(msg, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("Msg Sent!\n " + token1 + "\n" + token2 + "\n" + token3 + "");
            return true;
        } catch (MessagingException | SQLException mex) {
            //System.out.println(mex);
            FacesContext.getCurrentInstance().
                            addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contact the DevOps for this error",
                                    mex +""));
            mex.printStackTrace();
            return false;
        }
        
    }

}
