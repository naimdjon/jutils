package no.ntnu.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.*;


public class ZipUtils {
    public static final int BUFFER = 2048;

    private final static int BUFFER_SIZE_STRINGCOMP = 1000 * 1024; // 1Mb


    public static File zipDirectory(File toZip, String filename) throws IOException {
        if (filename != null && filename.length() > 0 && !filename.endsWith(".zip")) {
            filename = filename + ".zip";
        } else if (filename == null || filename.length() < 1)
            filename = toZip.getPath() + ".zip";
        File ret = new File(filename);
        ZipOutputStream targetStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(ret)));
        try {
            zip("", toZip, targetStream);
            targetStream.setMethod(ZipOutputStream.STORED);
        } finally {
            targetStream.flush();
            targetStream.close();
        }
        return ret;
    }


    public static void zip(String path, File directory, ZipOutputStream out) throws IOException {
        File[] files = directory.listFiles();
        byte data[] = new byte[BUFFER];
        for (int i = 0; files != null && i < files.length; i++) {
            if (!files[i].isDirectory()) {
                FileInputStream fi = new FileInputStream(files[i]);
                BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(path + files[i].getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            } else {
                if (path.length() == 0) {
                    zip(files[i].getName() + File.separator, files[i], out);
                } else {
                    zip(path + files[i].getName() + File.separator, files[i], out);
                }
            }
        }
    }


    public static void unZip(File zipFile/*the zip file**/, String toDirectory/*the directory to which unzip*/) throws IOException {
        FileInputStream fis = new FileInputStream(zipFile);
        CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
        ZipEntry entry = zis.getNextEntry();

        while (entry != null) {
            //log.debug("Extracting: " + entry);
            int count;
            byte data[] = new byte[BUFFER];
            // write the files to the disk
            if (toDirectory == null || toDirectory.length() < 1) {
                toDirectory = zipFile.getParent();
            }
            char fileSeparator = (entry.getName().indexOf(File.separatorChar) != -1 ? File.separatorChar : (File.separatorChar == '/') ? '\\' : '/');
            String courseFolder = zipFile.getName().substring(0, zipFile.getName().lastIndexOf("."));
            String fileName = entry.getName();
            //log.debug("CourseFolder: " + courseFolder);
            //log.debug("FileName: " + fileName);
            String path = toDirectory + File.separator + courseFolder + File.separator + fileName.replace(fileSeparator == '\\' ? '\\' : fileSeparator, File.separatorChar);

            File f = new File(path);
            if (entry.isDirectory()) {
                f.mkdirs();
                try {
                    entry = zis.getNextEntry();
                } catch (Exception e) {
                    entry = null;
                }
                continue;
            }

            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            try {
                FileOutputStream fos = new FileOutputStream(f);
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                entry = zis.getNextEntry();
            } catch (Exception e) {
                entry = null;
                e.printStackTrace();
            }

        }
        zis.close();
        zipFile.delete();
        //log.debug("Checksum:" + checksum.getChecksum().getValue());
    }


    public static byte[] unzip(byte[] zipfile, String path) throws IOException {
        byte[] file = null;
        CheckedInputStream checksum = new CheckedInputStream(new ByteArrayInputStream(zipfile), new Adler32());
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
        ZipEntry entry = zis.getNextEntry();
        DataInputStream dis = new DataInputStream(zis);

        while (entry != null && !entry.getName().equals(path)) {
//            log.debug(entry.getName());
            entry = zis.getNextEntry();
        }
        if (entry != null && !entry.isDirectory()) {
            if (entry.getSize() > 0) {
                file = new byte[(int) entry.getSize()];
                dis.readFully(file);
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int i = zis.read();
                while (i >= 0) {
                    baos.write(i);
                    i = zis.read();
                }
                file = baos.toByteArray();
            }
        }

        zis.close();
        return file;
    }

    public static Map<String, byte[]> unzipFiles(byte[] zipfile, String regex) throws IOException {
        ByteArrayInputStream zipFileStream = new ByteArrayInputStream(zipfile);
        return unzipFiles(zipFileStream, regex);
    }

    public static Map<String, byte[]> unzipFiles(InputStream zipFileStream) throws IOException {
        return unzipFiles(zipFileStream, null);
    }

    public static Map<String, byte[]> unzipFiles(ZipInputStream zis, String regex) throws IOException {
        DataInputStream dis = new DataInputStream(zis);
        ZipEntry entry = zis.getNextEntry();

        Map<String, byte[]> map = new HashMap<String, byte[]>();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        while (entry != null) {
            String name = entry.getName();
            if (pattern.matcher(name).matches() && entry.getSize() > 0) {
                byte[] file = new byte[(int) entry.getSize()];
                dis.readFully(file);
                map.put(name, file);
            }
            entry = zis.getNextEntry();
        }
        return map;
    }

    public static Map<String, byte[]> unzipFiles(InputStream zipFileStream, String regex) throws IOException {
        CheckedInputStream checksum = new CheckedInputStream(zipFileStream, new Adler32());
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
        return unzipFiles(zis, regex);
    }

    public static void putNextEntry(byte[] data, String entryPath, ZipOutputStream zipfile) throws IOException {
        ZipEntry e = new ZipEntry(entryPath);
        zipfile.putNextEntry(e);
        zipfile.write(data, 0, data.length);
    }


    public static byte[] zip(Map<String, byte[]> data) throws IOException {
        if (data == null || data.size() == 0) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        for (Map.Entry<String, byte[]> entry : data.entrySet()) {
            putNextEntry(entry.getValue(), entry.getKey(), zos);
        }
        zos.flush();
        zos.close();
        return baos.toByteArray();
    }


    public static byte[] compressString(String s, String charsetName) {
        if (s == null) {
            return null;
        }
        try {
            return compress(s.getBytes(charsetName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String st = "je suis une longue chaine de caracteres";
        System.out.println("String to compress : " + st);
        byte[] compst = ZipUtils.compressString(st);
        System.out.println("Compressed : " + compst);
        String decompst = ZipUtils.decompressString(compst);
        System.out.println("Decompressed string : " + decompst);
    }

    public static byte[] compressString(String s) {
        //log.debug("compressString start, string length in bytes is " + s.getBytes().length);
        if (s == null) {
            return null;
        }
        try {
            byte[] buf = new byte[BUFFER_SIZE_STRINGCOMP];
            int offset = 0;
            int l = 0;
            Deflater comp = new Deflater();
            comp.setInput(s.getBytes());
            comp.finish();
            while (!comp.finished()) {
                // zip bytes into buffer
                l = comp.deflate(buf, offset, BUFFER_SIZE_STRINGCOMP);
                if (!comp.finished()) {
                    // create larger buffer
                    byte[] temp = new byte[buf.length + BUFFER_SIZE_STRINGCOMP];
                    offset = buf.length;
                    // copy zipped bytes into new buffer
                    System.arraycopy(buf, 0, temp, 0, buf.length);
                    buf = temp;
                } else {
                    // buf is (possibly) larger than actual number of compressed bytes
                    // actual size is offset (bytes zipped before) + l (bytes zipped now)
                    byte[] temp = new byte[offset + l];
                    System.arraycopy(buf, 0, temp, 0, offset + l);
                    buf = temp;
                }
            }
            comp.end();
            /*if(log.isDebugEnabled())
            log.debug("compress done with resulting size " + buf.length);*/
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decompressString(byte[] compressedString) {
        return decompressString(compressedString, null);
    }

    public static String decompressString(byte[] compressedString, String charset) {
        //log.debug("decompressString start, compressed length in bytes is " + compressedString.length);
        if (compressedString == null) {
            return "";
        }
        try {
            byte[] buf = new byte[BUFFER_SIZE_STRINGCOMP];
            int offset = 0;
            int length = 0;
            Inflater decomp = new Inflater();
            decomp.setInput(compressedString);
            while (!decomp.finished()) {
                // unzip bytes into buffer
                length = decomp.inflate(buf, offset, BUFFER_SIZE_STRINGCOMP);
                if (!decomp.finished()) {
                    // create larger buffer
                    byte[] temp = new byte[buf.length + BUFFER_SIZE_STRINGCOMP];
                    offset = buf.length;
                    // copy unzipped bytes into new buffer
                    System.arraycopy(buf, 0, temp, 0, buf.length);
                    buf = temp;
                } /*else { // won't create final buffer, just using actual size for string creation on return
                    // buf is (possibly) larger than actual number of decompressed bytes
                    // actual size is offset (bytes unzipped before) + length (bytes unzipped now)
                    byte[] temp = new byte[offset + length];
                    System.arraycopy(buf, 0, temp, 0, offset+length);
                    buf = temp;
                }*/
            }
            //log.debug("decompress done with resulting size " + (offset + length));
            if (charset != null) {
                String s = new String(buf, 0, offset + length, charset);
                return "" + s;
            } else {
                return new String(buf, 0, offset + length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] compress(byte[] in) {
        //log.debug("compress start, string length in bytes is " + in.length);
        if (in == null) {
            return null;
        }
        try {
            byte[] buf = new byte[BUFFER_SIZE_STRINGCOMP];
            int offset = 0;
            int length = 0;
            Deflater comp = new Deflater();
            comp.setInput(in);
            comp.finish();
            while (!comp.finished()) {
                // zip bytes into buffer
                length = comp.deflate(buf, offset, BUFFER_SIZE_STRINGCOMP);
                if (!comp.finished()) {
                    // create larger buffer
                    byte[] temp = new byte[buf.length + BUFFER_SIZE_STRINGCOMP];
                    offset = buf.length;
                    // copy zipped bytes into new buffer
                    System.arraycopy(buf, 0, temp, 0, buf.length);
                    buf = temp;
                } else {
                    // buf is (possibly) larger than actual number of compressed bytes
                    // actual size is offset (bytes zipped before) + length (bytes zipped now)
                    byte[] temp = new byte[offset + length];
                    System.arraycopy(buf, 0, temp, 0, offset + length);
                    buf = temp;
                }
            }
            comp.end();
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decompress(byte[] compressedData) {
        //log.debug("decompress start, compressed length in bytes is " + compressedData.length);
        if (compressedData == null) {
            return null;
        }
        try {
            byte[] buf = new byte[BUFFER_SIZE_STRINGCOMP];
            int offset = 0;
            int l = 0;
            Inflater decomp = new Inflater();
            decomp.setInput(compressedData);
            while (!decomp.finished()) {
                // unzip bytes into buffer
                l = decomp.inflate(buf, offset, BUFFER_SIZE_STRINGCOMP);
                if (!decomp.finished()) {
                    // create larger buffer
                    byte[] temp = new byte[buf.length + BUFFER_SIZE_STRINGCOMP];
                    offset = buf.length;
                    // copy unzipped bytes into new buffer
                    System.arraycopy(buf, 0, temp, 0, buf.length);
                    buf = temp;
                } else {
                    // buf is (possibly) larger than actual number of decompressed bytes
                    // actual size is offset (bytes unzipped before) + l (bytes unzipped now)
                    byte[] temp = new byte[offset + l];
                    System.arraycopy(buf, 0, temp, 0, offset + l);
                    buf = temp;
                }
            }
            //log.debug("decompress done with resulting size " + buf.length);
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static byte[] gzipCompress(String s) {
        try {
            BufferedReader in = new BufferedReader(new StringReader(s));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(output));
            int c;
            while ((c = in.read()) != -1) out.write(c);
            IO.instance
                    .clos(in)
                    .clos(out);
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    static final private int Adler32_BASE = 65521;
    static final private int Adler32_NMAX = 5552;

    public static long adler32(long adler, byte[] buf, int index, int len) {
        if (buf == null) {
            return 1L;
        }

        long s1 = adler & 0xffff;
        long s2 = (adler >> 16) & 0xffff;
        int k;

        while (len > 0) {

            k = len < Adler32_NMAX ? len : Adler32_NMAX;
            len -= k;
            while (k >= 16) {
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                k -= 16;
            }
            if (k != 0) {
                do {
                    s1 += buf[index++] & 0xff;
                    s2 += s1;
                }
                while (--k != 0);
            }
            s1 %= Adler32_BASE;
            s2 %= Adler32_BASE;
        }
        return (s2 << 16) | s1;
    }

}