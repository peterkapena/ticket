/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IPAdress;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
 * @author Peter
 */
public class WLANIPAdress {

    public WLANIPAdress() {

    }//The null constructor

    public static void getWlanAddress()  {
        try {
            Enumeration<NetworkInterface> nics = NetworkInterface
                    .getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                Enumeration<InetAddress> addrs = nic.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (nic.getName().equals("wlan2") && Character.isDigit(addr.getHostAddress().charAt(0))) {
                        System.out.printf("%s%n",
                                addr.getHostAddress());
                        System.out.println(InetAddress.getLocalHost().getHostName());
                        System.out.println(System.getProperty("user.name"));
                    }//So you can use the IP address or the HostName for Local Access to the web page
                    
                }
            }
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        WLANIPAdress.getWlanAddress();
    }//main method

}
