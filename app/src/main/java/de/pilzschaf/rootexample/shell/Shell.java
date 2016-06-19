/*
 * Original work Copyright (c) 2011 Adam Outler
 * Located at https://github.com/adamoutler/CASUAL
 * Modified work Copyright (c) 2016 Pilzschaf
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

package de.pilzschaf.rootexample.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Shell {

    public CommandOutput executeCommand(String command) throws CommandExecutionException {
        return this.executeCommand(new String[] {command});
    }

    public CommandOutput executeRootCommand(String command) throws CommandExecutionException {
        return this.executeCommand(new String[] {"su", "-c",command});
    }

    protected CommandOutput executeCommand(String[] command) throws CommandExecutionException{
        CommandOutput output = new CommandOutput();
        try {
            Process process = new ProcessBuilder(command).start();
            BufferedReader STDOUT = new BufferedReader( new InputStreamReader(process.getInputStream()));
            BufferedReader STDERR = new BufferedReader( new InputStreamReader(process.getErrorStream()));
            try {
                process.waitFor();
                output.exitCode = process.exitValue();
            } catch (InterruptedException e) {
                throw new CommandExecutionException(e.toString());
            }
            output.output = readBufferedReaderToString(STDOUT);
            output.error = readBufferedReaderToString(STDERR);
        } catch (IOException e) {
            throw new CommandExecutionException(e.toString());
        }
        return output;
    }

    private String readBufferedReaderToString(BufferedReader reader) throws IOException {
        String output = "";
        String line;
        while ((line = reader.readLine()) != null) {
            output = output + "\n" + line;
        }
        return output;
    }
}
