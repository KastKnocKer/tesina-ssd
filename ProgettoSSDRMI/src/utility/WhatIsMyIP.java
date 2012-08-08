package utility;
/*
 *  SyncIT , easy synchronization of files between computers and devices.
 *  Copyright (C) 2011  Christian Masus <cmasus(at)gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version. If redistributing and/or modifying
 *  this file, this notice must be included in the redistributed or
 *  modified copy.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Contains function to find out what IP the user is having.
 * @author Christian Masus
 *
 */
public class WhatIsMyIP {

        public WhatIsMyIP() {

        }


        /**
         * Gets all the local addresses of this computer.
         * @return a matrix with the IP and an explanation of what kind of IP it is, paired.
         */
        public String[][] getLocalIPs() {
                try {
                        InetAddress[] inetAddr = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
                        String[][] ret = new String[inetAddr.length][2];
                        for(int i = 0; i < inetAddr.length; i++) {
                                ret[i][0] = inetAddr[i].getHostAddress();
                                if(inetAddr[i].isLoopbackAddress()) {
                                        ret[i][1] = "Loopback address";
                                }
                                else if(inetAddr[i].isSiteLocalAddress()) {
                                        ret[i][1] = "LAN address";
                                }
                                else if(inetAddr[i].isMulticastAddress()) {
                                        ret[i][1] = "Multicast address";
                                }
                                else if(inetAddr[i].getAddress().length > 4) {
                                        ret[i][1] = "IPv6 address";
                                }
                                else {
                                        ret[i][1] = "";
                                }
                        }
                        return ret;
                } catch (UnknownHostException e) {
                }
                return null;
        }



        /**
         * Tries to get this computers IP address from www.whatismyip.org through a nio socket.
         * If you are behind a NAT you need an external source to get your Internet IP.
         * Tries first with whatismyip.se for 6 seconds, if that fails then tries whatismyip.org
         * for the same time.
         */
        public String getGlobalIP() {
                String webPage = getWebPage("www.whatismyip.se", 80);
                if(webPage != null && !webPage.equals("")) {
                        String ipStr = parseWhatIsMyIPSe(webPage);
                        if(isRealIP(ipStr)) {
                                return ipStr;
                        }
                }

                String webPage2 = getWebPage("www.whatismyip.org", 80);
                if(webPage2 != null && !webPage2.equals("")) {
                        String ipStr = parseWhatIsMyIPOrg(webPage2);
                        if(isRealIP(ipStr)) {
                                return ipStr;
                        }
                }

                return null;
        }

        /**
         * Retrieves a web page as a String. Checks that the return code of the web page is 200 OK.
         * @param host the host address.
         * @param port the host port.
         * @return the web page as a String or null if something went wrong.
         */
        private String getWebPage(String host, int port) {
                String getRequest = "GET / HTTP/1.1\n" +
                "Host: " + host + "\n" +
                "Accept: text/plain, text/html\n" +
                "Accept-Charset: utf-8\n" +
                "Connection: close\n" +
                "\n";

                Charset charset = Charset.forName("UTF-8");
                CharsetDecoder decoder = charset.newDecoder();
                try {
                        SocketChannel channel = SocketChannel.open();
                        channel.socket().connect(new InetSocketAddress(host, port), 6000); // Set connection timeout
                        ByteBuffer buf = ByteBuffer.wrap(getRequest.getBytes());
                        channel.write(buf);
                        buf = ByteBuffer.allocate(256);

                        String webPage = "";
                        while(buf.hasRemaining() && channel.read(buf) != -1) {
                                buf.flip();
                                String str = decoder.decode(buf).toString();
                                webPage += str;
                                buf.clear();
                        }
                        channel.close();
                        if(webPage.startsWith("HTTP/1.0 200 OK") || webPage.startsWith("HTTP/1.1 200 OK")) {
                                return webPage;
                        }
                } catch (IOException e) {
                }
                return null;
        }

        /**
         * Parses the whatismyip.se page after the IP address.
         * @param webPage the web page as a String.
         * @return the IP address as a String.
         */
        private String parseWhatIsMyIPSe(String webPage) {
                int start = webPage.indexOf("IP: ");
                String roughIPStr = webPage.substring(start+4, start+4+15);
                int counter = 0;
                for(int i = (roughIPStr.length()-1); i >= 0; i--) {
                        char c = roughIPStr.charAt(i);
                        if(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
                                break;
                        }
                        counter++;
                }
                String ipStr = roughIPStr.substring(0, roughIPStr.length()-counter);
                return ipStr;
        }

        /**
         * Parses the whatismyip.org page after the IP address.
         * @param webPage the web page as a String.
         * @return the IP address as a String.
         */
        private String parseWhatIsMyIPOrg(String webPage) {
                String[] split = webPage.split("\r\n\r\n");
                return split[1];
        }

        /**
         * Checks if the string IP really is a valid IP number.
         * @param ip the String to be checked
         * @return true if the input String is a valid IP number, otherwise false
         */
        private boolean isRealIP(String ip) {
                if(ip != null && !ip.equals("")) {
                        String[] ipSplit = ip.split("\\.");
                        if(ipSplit.length == 4) {
                                for(int i = 0; i < ipSplit.length; i++) {
                                        try {
                                                int num = Integer.parseInt( ipSplit[i] );
                                                if(num < 0 || num > 255) {
                                                        return false;
                                                }
                                        } catch(Exception e) {
                                                return false;
                                        }
                                }
                                return true;
                        }
                }
                return false;
        }

}
