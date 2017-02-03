package com.example;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

class Hello {

    static Properties props;

    public static void main(String[] args) {

        Hello sendMyFiles = new Hello();

        sendMyFiles.startFTP("", "upload.txr");

    }

    public boolean startFTP(String propertiesFilename, String fileToFTP){

        props = new Properties();
        StandardFileSystemManager manager = new StandardFileSystemManager();

        try {

            //props.load(new FileInputStream("properties/" + propertiesFilename));
//            String serverAddress = props.getProperty("serverAddress").trim();
//            String userId = props.getProperty("userId").trim();
//            String password = props.getProperty("password").trim();
//            String remoteDirectory = props.getProperty("remoteDirectory").trim();
//            String localDirectory = props.getProperty("localDirectory").trim();



            String serverAddress =  "10.0.14.22";
            String userId = "transactions";
            String password = "-KQhJK5,=B";
            String remoteDirectory = "test";
            String localDirectory = "./";


            //check if the file exists
            String filepath = localDirectory +  fileToFTP;

            File file = new File(filepath);
            if (!file.exists())
                throw new RuntimeException("Error. Local file not found");

            //Initializes the file manager
            System.out.println("will try uploading "+ filepath);
            manager.init();

            //Setup our SFTP configuration
            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
                    opts, "no");
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
            SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

            //Create the SFTP URI using the host name, userid, password,  remote path and file name
            String sftpUri = "sftp://" + userId + ":" + password +  "@" + serverAddress + "/" +
                    remoteDirectory + fileToFTP;

            // Create local file object
            FileObject localFile = manager.resolveFile(file.getAbsolutePath());
            System.out.println("about to connect...");
            // Create remote file object
            FileObject remoteFile = manager.resolveFile(sftpUri, opts);

            // Copy local file to sftp server
            remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
            System.out.println("File upload successful");

        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        finally {
            manager.close();
        }

        return true;
    }



    public static void testMain(String[] args) {

        FTPSClient ftp = new FTPSClient();
        FTPClientConfig config = new FTPClientConfig();
        //config.setXXX(YYY); // change required options
        // for example config.setServerTimeZoneId("Pacific/Pitcairn")
        ftp.configure(config );
        boolean error = false;
        try {
            int reply;
            String server = "10.0.14.22";
            ftp.connect(server);
            ftp.login("transactions", "-KQhJK5,=B");
            System.out.println("Connected to " + server + ".");
            System.out.print(ftp.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftp.getReplyCode();

            if(!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }

            System.out.println("++Connected!!");

            ftp.logout();
        } catch(IOException e) {
            error = true;
            e.printStackTrace();
        } finally {
            if(ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch(IOException ioe) {
                    // do nothing
                }
            }
            System.exit(error ? 1 : 0);
        }


    }
}
