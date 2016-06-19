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

import de.pilzschaf.rootexample.shell.CommandExecutionException;
import de.pilzschaf.rootexample.shell.CommandOutput;
import de.pilzschaf.rootexample.shell.Shell;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
}
