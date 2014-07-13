package no.ntnu.utils;

import java.io.File;
import java.util.Properties;
import java.util.Set;

import static java.lang.String.format;
import static no.ntnu.utils.Clazz.instantiate;
import static no.ntnu.utils.Clazz.invoke;
import static no.ntnu.utils.Col.newSet;
import static no.ntnu.utils.IO.*;
import static no.ntnu.utils.IO.prop;
import static no.ntnu.utils.StringUtils.*;
import static no.ntnu.utils.Sys.*;

/**
 * Created by NT
 * Date: 3/14/12
 * Time: 12:16 AM
 * This class requires that jsch-xx.jar is in the classpath. Jsch is used via reflection.
 * @author takhirov
 */
@SuppressWarnings("unchecked")
public class Tunnel {
    @SuppressWarnings("rawtypes")
	private static final Set sessions = newSet();

    static {
        registerCleanup();
    }

    public static void tunnel(String remoteHost, int portNumber) {
        checkAndSetPass();
        String loginserver=null;
        try {
            Object jsch = instantiate("com.jcraft.jsch.JSch");
            String s = getHome();
            String home = cat(s, USER);
            int port = 22;
            loginserver= format("%s@%s:%s", USER, remoteHost, port);
            D.i("Connecting to %s", loginserver);
            Properties config = new Properties();
            Object session = invoke(jsch, "getSession", USER, remoteHost, port);
            String knownHosts = cat(home, "/.ssh/known_hosts");
            if (exists(knownHosts)) invoke(jsch, "setKnownHosts", knownHosts);
            String id_rsa = cat(home, "/.ssh/id_rsa");
            if (exists(id_rsa)) {
                invoke(jsch, "addIdentity", id_rsa);
            }else if (hasLength(System.getProperty(pass))) {
                D.i("logging with password.");
                invoke(session,"setPassword",System.getProperty(pass));
            } else {
                D.w("~/.ssh/id_rsa did not exist! Passwordless login will probably result in Auth fail!");
            }
            config.put("StrictHostKeyChecking", "no");
            invoke(session, "setConfig", config);
            //invoke(session, "setServerAliveInterval", 30);
            invoke(session, "connect");
            Object assinged_port = invoke(session, "setPortForwardingL", portNumber, "localhost", portNumber);
            D.d("forwarding: localhost:" + assinged_port + " -> localhost:" + portNumber);
            sessions.add(session);
        } catch (RuntimeException e) {
            retryWithPassword(remoteHost, portNumber, loginserver, e);
        }
    }

    private static void retryWithPassword(String remoteHost, int portNumber, String loginserver, RuntimeException e) {
        if (contains(stackTraceToStr(e), ("Auth fail")) && !Boolean.getBoolean("nt-utils.tunnel.retryWithPassword")) {
            D.e("Authentication failed. Please enter your ssh password [%s]: ", loginserver);
            String passwd = Sys.readPasswd();
            storeProp(config, enc.encrypt(loginserver), enc.encrypt(passwd));
            config = fileInUserHomeDir(configFile);
            System.setProperty(pass,passwd);
            System.setProperty("nt-utils.tunnel.retryWithPassword","true");
            tunnel(remoteHost,portNumber);
        }else
            e.printStackTrace();
    }

    private static void checkAndSetPass() {
        if (exists(config)) {
            D.i("config file [%s] exists!",config.getAbsolutePath());
            Properties prop = prop(config);
            Object k = prop.keys().nextElement();
            Object v=prop.get(k);
            //String server = enc.decrypt(k.toString());
            String p = enc.decrypt(v.toString());
            System.setProperty(pass,p);
        }
    }

    private static String getHome() {
        String s = "/home/";
        if (isMac)
            s = "/Users/";
        return s;
    }

    static void registerCleanup() {
        runOnExit(new Runnable(){
            public void run() {
                D.d("cleaning up ssh sessions!");
                for (Object session : sessions) {
                    try {
                        invoke(session, "disconnect");
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    static final Encryption enc= new Encryption("Huj4u");
    private static final String configFile = ".nt-utils-tunnel";
    private  static File config = fileInUserHomeDir(configFile);
    private static final String pass = "tunnel.passwd.str";

}
