/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colositer.utils;

/**
 *
 * @author pavel
 */
      /*Class that help to extract extension from filename. Got from here:
     http://www.java2s.com/Code/Java/File-Input-Output/Getextensionpathandfilename.htm
     */
    public class Filename {

        private String fullPath;
        private char pathSeparator, extensionSeparator;

        public Filename(String str, char sep, char ext) {
            fullPath = str;
            pathSeparator = sep;
            extensionSeparator = ext;
        }

        public Filename(String str) {
            this(str, '/', '.');
        }

        public String extension() {
            int dot = fullPath.lastIndexOf(extensionSeparator);
            return fullPath.substring(dot + 1);
        }

        public String filename() { // gets filename without extension
            int dot = fullPath.lastIndexOf(extensionSeparator);
            int sep = fullPath.lastIndexOf(pathSeparator);
            return fullPath.substring(sep + 1, dot);
        }

        public String path() {
            int sep = fullPath.lastIndexOf(pathSeparator);
            return fullPath.substring(0, sep);
        }
    }
    
