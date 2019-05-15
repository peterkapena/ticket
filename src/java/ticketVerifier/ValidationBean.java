// ValidationBean.java
// Validating user input.
package ticketVerifier;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;

@ManagedBean( name="validationBean" )
public class ValidationBean implements Serializable
{
   private String name;
   // return the name String
   public String getName()
   {
      return name;
   } // end method getName

   // set the name String
   public void setName( String name )
   {
      this.name = name;
   } // end method setName
   
   // returns result for rendering on the client
   public String getResult()
   {
       return getName();
   } // end method getResult
   
   public String getVerify()
   {
       return name;
   }
} // end class ValidationBean

/*************************************************************************
 * (C) Copyright 1992-2012 by Deitel & Associates, Inc. and              *
 * Pearson Education, Inc. All Rights Reserved.                          *
 *                                                                       *
 * DISCLAIMER: The authors and publisher of this book have used their    *
 * best efforts in preparing the book. These efforts include the         *
 * development, research, and testing of the theories and programs       *
 * to determine their effectiveness. The authors and publisher make      *
 * no warranty of any kind, expressed or implied, with regard to these   *
 * programs or to the documentation contained in these books. The authors*
 * and publisher shall not be liable in any event for incidental or      *
 * consequential damages in connection with, or arising out of, the      *
 * furnishing, performance, or use of these programs.                    *
 ************************************************************************/

