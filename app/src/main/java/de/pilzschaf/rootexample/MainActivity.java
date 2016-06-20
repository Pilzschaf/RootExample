/*
 * Copyright (c) 2016 Pilzschaf
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package de.pilzschaf.rootexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import de.pilzschaf.rootexample.shell.CommandExecutionException;
import de.pilzschaf.rootexample.shell.CommandOutput;
import de.pilzschaf.rootexample.shell.Shell;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Simple Example using the shell package
        Shell shell = new Shell();
        try {
            //Command requires root privileges to output
            CommandOutput output = shell.executeRootCommand("ls -la /data");
            if(output.exitCode != 0 && output.error.contains("Permission denied")) {
                System.out.println("Root not granted");
            }else {
                System.out.println(output);
            }
        } catch (CommandExecutionException e) {
            System.out.println("Error executing command");
            System.out.println("Device not rooted?");
            e.printStackTrace();
        }

        //Advanced example using the RootToolsLibrary
        //Available at https://github.com/Stericson/RootTools
        System.out.println("Root available: "+RootTools.isRootAvailable());
        System.out.println("Busybox available: "+RootTools.isBusyboxAvailable());

        //Does a request for root access
        System.out.println("Root access given: "+RootTools.isAccessGiven());

        //Run non root command
        Command command1 = new Command(0, "ls -la");
        try {
            RootTools.getShell(false).add(command1);
        } catch (IOException | RootDeniedException | TimeoutException e) {
            e.printStackTrace();
        }

        //Run a root command
        Command command2 = new Command(0, "ls -la /data");
        try {
            RootTools.getShell(true).add(command2);
        } catch (IOException | RootDeniedException | TimeoutException e){
            e.printStackTrace();
        }

        //Run command and print output
        Command command3 = new Command(0, "ls -la /data")
        {
            @Override
            public void commandOutput(int id, String line){
                System.out.println(line);
                super.commandOutput(id, line);
            }
            @Override
            public void commandTerminated(int id, String reason) {
                System.out.println("Command terminated because: "+reason);
                super.commandTerminated(id, reason);
            }

            @Override
            public void commandCompleted(int id, int exitCode) {
                System.out.println("Command completed with exit code: "+exitCode);
                super.commandCompleted(id, exitCode);
            }
        };
        try {
            RootTools.getShell(true).add(command3);
        } catch (IOException | RootDeniedException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
