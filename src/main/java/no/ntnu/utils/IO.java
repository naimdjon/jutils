package no.ntnu.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

public class IO {
    public static final IO instance = new IO();

    public static String getVersionId() {
        return "$Id: IO.java 1873 2011-12-12 11:44:24Z takhirov $";
    }

    private IO() {
    }


    public static void flushAndClose(OutputStream... streams) {
        if (streams == null || streams.length == 0) return;
        try {
            for (OutputStream out : streams) {
                instance.flush(out);
                close(out);
            }
        } catch (Exception e) {
            System.err.println("could not flush/close IO stream/s");
        }
    }

    public static void flushAndClose(PrintWriter... printWriters) {
        if (printWriters == null || printWriters.length == 0) return;
        try {
            for (PrintWriter pw : printWriters) {
                instance.flush(pw);
                close(pw);
            }
        } catch (Exception e) {
            System.err.println("could not flush/close print writer/s");
        }
    }

    public static void xmlEncode(String filename, Object object) {
        try {
            XMLEncoder encoder = new XMLEncoder(new FileOutputStream(filename));
            try {
                encoder.writeObject(object);
            } finally {
                encoder.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T xmlDecode(String filename) {
        try {
            final File encodedXMLFile = new File(filename);
            if (!encodedXMLFile.exists()) {
                System.err.println(filename + ", does not exist!");
                return null;
            }
            XMLDecoder decoder = new XMLDecoder(new FileInputStream(encodedXMLFile));
            try {
                return (T) decoder.readObject();
            } finally {
                decoder.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void readLines(final String filename, final LineProcessor processor) {
        readLines(new File(filename), processor);
    }

    public static void readLines(final File file, final LineProcessor processor) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            try {
                String line;
                while ((line = reader.readLine()) != null)
                    processor.process(line);
            } finally {
                close(reader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readLines(final BufferedReader reader, final LineProcessor processor) {
        try {
            String line;
            while ((line = reader.readLine()) != null)
                processor.process(line);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(reader);
        }

    }

    public static Properties load(File filename) {
        try {
            Properties p = new Properties();
            p.load(new FileReader(filename));
            return p;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void writeObject(Serializable obj, String fileName) {
        final FileOutputStream fos = instance.fileOutputStream(fileName);
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(fos);
            output.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            flushAndClose(output, fos);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readObject(String fileName) {
        T obj = null;
        final FileInputStream fis = fis(fileName);
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(fis);
            obj = (T) input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fis, input);
        }
        return obj;
    }

    public static void close(Closeable... streams) {
        if (streams == null || streams.length == 0) return;
        try {
            for (Closeable s : streams)
                close(s);
        } catch (Exception e) {
            System.err.println("could not flush/close IO stream/s");
        }
    }


    public IO finish(GZIPOutputStream out) {
        try {
            out.finish();
        } catch (IOException e) {
            System.err.println("could not finish:" + e);
        }
        return instance;
    }


    public FileOutputStream fileOutputStream(String fileName) {
        try {
            return new FileOutputStream(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FileInputStream fis(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObjectOutputStream oos(String fileName) {
        return oos(fos(fileName));
    }

    public static ObjectOutputStream oos(OutputStream os) {
        try {
            return new ObjectOutputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FileOutputStream fos(String fileName) {
        return instance.fileOutputStream(fileName);
    }

    public static FileOutputStream fos(File fileName) {
        return instance.fileOutputStream(fileName.getAbsolutePath());
    }

    public IO println(PrintWriter pw, String s) {
        try {
            if (pw != null)
                pw.println(s);
        } catch (Throwable e) {
            System.err.println("could not println!");
        }
        return instance;
    }

    public IO print(PrintWriter pw, String s) {
        try {
            if (pw != null)
                pw.print(s);
        } catch (Throwable e) {
            System.err.println("could not print!");
        }
        return instance;
    }

    public static void writeFlushClose(final Writer writer, final String content) {
        try {
            if (writer != null)
                try {
                    writer.write(content);
                } finally {
                    instance.flush(writer);
                    close(writer);
                }
        } catch (Throwable e) {
            System.err.println("could not write or flush or close!");
        }
    }


    public static void printlnAndClose(PrintWriter pw, String s) {
        instance.println(pw, s).flush(pw).clos(pw);
    }


    public static void printAndClose(PrintWriter pw, String s) {
        instance.print(pw, s)
                .flush(pw)
                .clos(pw);
    }


    public IO flush(Flushable f) {

        try {
            if (f != null)
                f.flush();
        } catch (Throwable e) {
            System.err.println("IO:could not flush!");
        }
        return instance;
    }

    public static void cloze(Closeable c) {
        close(c);
    }

    public static IO close(Closeable c) {
        try {
            if (c != null)
                c.close();
        } catch (Throwable e) {
            System.err.println("could not flush!");
        }
        return instance;
    }

    public IO clos(Closeable c) {
        close(c);
        return this;
    }

    public IO write(String str, String out) {
        try {
            final FileOutputStream fos = fileOutputStream(out);
            try {
                return write(str, fos);
            } finally {
                flushAndClose(fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return instance;
        }
    }

    public static void writeAndClose(String str, OutputStream os) {
        instance.write(str, os);
    }

    public IO write(String str, OutputStream os) {
        return write(new ByteArrayInputStream(str.getBytes()), os);
    }

    public static void writeAndClose(java.util.Properties data, File file) {
        try {
            final FileOutputStream out = new FileOutputStream(file);
            data.store(out, "hashed urls");
            flushAndClose(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public IO writeAndClose(byte[] bytes, OutputStream out) {
        try {
            return write(new ByteArrayInputStream(bytes), out);
        } finally {
            flushAndClose(out);
        }
    }

    public IO write(byte[] bytes, File filename) {
        return writeAndClose(bytes, getFileoutputStream(filename));
    }

    public static IO writeAndClose(byte[] bytes, File filename) {
        return instance.write(bytes, filename);
    }

    public static IO writeAndClose(String content, File file) {
        return writeAndClose(content.getBytes(), file);
    }

    public static IO writeUTF8AndClose(String content, File file) {
        try {
            return writeAndClose(content.getBytes("UTF-8"), file);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return instance;
        }
    }

    public static IO writeAndClose(byte[] bytes, String filename) {
        return instance.write(bytes, new File(filename));
    }

    public static IO writeAndClose(String content, String filename) {
        return instance.write(content.getBytes(), new File(filename));
    }

    private FileOutputStream getFileoutputStream(File filename) {
        try {
            return new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public IO write(InputStream is, OutputStream os) {
        if (!Boolean.getBoolean("IO.quite"))
            System.out.println("writing with encoding: " + System.getProperty("file.encoding"));
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(is);
            flush(os);
            close(os);
        }
        return instance;
    }

    public static String gunzip(byte[] data) {
        try {
            InputStream in = new GZIPInputStream(new ByteArrayInputStream(data));
            OutputStream os = new ByteArrayOutputStream();
            instance.write(in, os);
            close(in);
            close(os);
            return os.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("could not gunzip");
        }
        return "";
    }

    public static String readFileString(String file) throws RuntimeException {
        return new String(readFile(new File(file)));
    }

    public static URL toURL(String fileName) {
        try {
            return new File(fileName).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String readUrl(String url, Writer resultWriter) {
        try {
            URL urlEndpoint = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlEndpoint.openStream()));
            String inputLine;
            final StringBuffer s = new StringBuffer();
            try {
                while ((inputLine = in.readLine()) != null)
                    s.append(inputLine);
            } finally {
                close(in);
            }
            return s.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static class Header {
        public final String key;
        public final String value;

        public Header(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static String readUrlWithHeaders(String url, Header... headers) {
        try {
            URL urlEndpoint = new URL(url);
            URLConnection con = urlEndpoint.openConnection();
            for (Header h : headers) con.addRequestProperty(h.key, h.value);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            final StringBuffer s = new StringBuffer();
            try {
                while ((inputLine = in.readLine()) != null)
                    s.append(inputLine);
            } finally {
                close(in);
            }
            return s.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String readUrl(String url) {
        try {
            URL urlEndpoint = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlEndpoint.openStream()));
            String inputLine;
            final StringBuffer s = new StringBuffer();
            try {
                while ((inputLine = in.readLine()) != null)
                    s.append(inputLine);
            } finally {
                close(in);
            }
            return s.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static String sendDoc(String url, String doc) {
        try {
            URL urlEndpoint = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlEndpoint.openConnection();
            con.setDoOutput(true);
            DataOutputStream ostream = new DataOutputStream(con.getOutputStream());
            ostream.write(doc.getBytes());
            ostream.close();
            InputStream input = con.getInputStream();
            byte[] buffer = new byte[1024 * 4];
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int n;
            while (-1 != (n = input.read(buffer)))
                output.write(buffer, 0, n);
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static InputStream inputStreamMaskUserAgent(String url) {
        try {
            URL urlEndpoint = new URL(url);
            final URLConnection con = urlEndpoint.openConnection();
            con.setConnectTimeout(1000);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.12");
            con.setRequestProperty("Method", "GET");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            //System.out.println("con.getContentType():"+con.getContentType());
            return con.getInputStream();
        } catch (Exception e) {
            System.err.println("IO:510:" + e.getMessage());
        }
        return null;
    }


    public static MaskedInputStream maskUserAgent(String url) {
        try {
            URL urlEndpoint = new URL(url);
            final URLConnection con = urlEndpoint.openConnection();
            con.setConnectTimeout(1000);
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.12");
            con.setRequestProperty("Method", "GET");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            String contentType = con.getContentType();
            String charset = parseCharset(contentType, "UTF-8");
            return new MaskedInputStream(con.getInputStream(), charset, contentType, con.getContentLength());
        } catch (Exception e) {
            System.err.println("IO:519:" + e.getMessage());
        }
        return null;
    }

    private static String parseCharset(String contentType, String def) {
        if (contentType == null || contentType.length() <= 0)
            return null;
        final String str = "charset=";
        final int i = contentType.indexOf(str);
        if (i > -1)
            return contentType.substring(i + str.length());
        return def;
    }

    public static String readUrlMaskUserAgent(String url) {
        return readUrlMaskUserAgent(url, false);
    }


    public static String readUrlMaskUserAgent(String url, boolean retry) {
        try {
            URL urlEndpoint = new URL(url);
            final URLConnection con = urlEndpoint.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.12");
            con.setRequestProperty("Method", "GET");
            final InputStream is = con.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            final StringBuffer s = new StringBuffer();
            try {
                while ((inputLine = in.readLine()) != null)
                    s.append(inputLine);
            } finally {
                close(in);
            }
            return s.toString();
        } catch (Exception e) {
            e.printStackTrace();
            if (!retry) {
                try {
                    Thread.sleep(4000);
                    System.out.println("retryring...");
                    return readUrlMaskUserAgent(url, true);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                }
            } else
                throw new RuntimeException(e);
        }
    }

    public static byte[] readUrlBinary(String url) {
        try {
            URLConnection uc = new URL(url).openConnection();
            int contentLength = uc.getContentLength();
            byte[] data = new byte[contentLength];
            InputStream raw = uc.getInputStream();
            InputStream in = new BufferedInputStream(raw);
            try {
                int bytesRead = 0;
                int offset = 0;
                while (offset < contentLength) {
                    bytesRead = in.read(data, offset, data.length - offset);
                    if (bytesRead == -1)
                        break;
                    offset += bytesRead;
                }
                in.close();
            } finally {
                close(in);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static byte[] readFile(String absoluteFilePath) {
        return readFile(new java.io.File(absoluteFilePath));
    }

    /**
     * Reads the content of a given file.
     *
     * @param file the file to be read
     * @return bytes read from a file.
     */
    public static byte[] readFile(File file) {
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(in);
        }
        return bytes;
    }

    public static String readFileStr(File file) {
        return new String(readFile(file));
    }

    public static byte[] read(InputStream input) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int bufferSize = 1024 * 16;
            ByteBuffer buf = ByteBuffer.allocateDirect(bufferSize);
            ReadableByteChannel readableChannel = Channels.newChannel(input);
            WritableByteChannel writableChannel = Channels.newChannel(output);
            int numberOfBytesRead;
            do {
                buf.rewind();
                numberOfBytesRead = readableChannel.read(buf);
                if (numberOfBytesRead > 0 && numberOfBytesRead < bufferSize)
                    buf.limit(numberOfBytesRead);

                buf.rewind();

                if (numberOfBytesRead > 0)
                    writableChannel.write(buf);
            } while (numberOfBytesRead > 0);

            output.flush();
            return output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static boolean delete(String filename) {
        return delete(new File(filename));
    }

    public static boolean deleteQuietly(File f) {
        try {
            return delete(f);
        } catch (Exception e) {
            System.err.println(format("Could not delete the file:%s", f.getAbsolutePath()));
            return false;
        }
    }

    public static FilenameFilter extensionFilter(final String extension) {
        return new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(extension);
            }
        };
    }

    public static boolean del(String filename) {
        return delete(new File(filename));
    }


    public static boolean delete(File file) {
        return (file == null
                || !file.exists()
                || file.delete());
    }


    public static boolean deleteR(String file) {
        return deleteR(new File(file));
    }

    public static File[] listFiles(File dir) {
        return listFiles(dir, null);
    }

    public static Iterator<File> iterateFiles(File dir) {
        return iterateFiles(dir, null);
    }

    public static Iterator<File> iterateFiles(File dir, FilenameFilter filter) {
        return asList(listFiles(dir, filter)).iterator();
    }

    public static File[] listFiles(File dir, FilenameFilter filter) {
        if (dir.isFile()) return new File[]{dir};
        if (filter != null)
            return dir.listFiles(filter);
        else return dir.listFiles();
    }


    public static boolean deleteR(File file) {
        final File[] children = file.listFiles();
        if (children != null)
            for (File f : children)
                deleteR(f);
        return delete(file);
    }

    public static String getSimpleName(File f) {
        final String name = f.getName();
        final int index = name.lastIndexOf(".");
        if (index != -1) return name.substring(0, index);
        return name;
    }

    public static File ensureDir(String file) {
        return ensureDir(new File(file));
    }

    public static boolean exists(String filename) {
        return StringUtils.hasLength(filename) && new File(filename).exists();
    }

    public static boolean exists(File f) {
        return f != null && f.exists();
    }

    public static Properties prop(File f) {
        if (!exists(f)) return null;

        Properties p = new Properties();
        try {
            p.load(new FileReader(f));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return p;
    }

    public static File ensureDir(File file) {
        try {
            boolean b = file.exists() || file.mkdirs();
            if (b)
                return file;
            else System.err.println("Could not mkdirs the dir " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    public static boolean isValidHttpUrl(String url) {
        final int responseCode = getHTTPConStatus(url);
        return responseCode < 400;
    }

    public static int getHTTPConStatus(String url) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setConnectTimeout(3 * 1000);
            con.setRequestMethod("HEAD");
            return con.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
            //System.err.println(StringUtils.concat(url," is not valid!"));
            return 500;
        }
    }

    public static String toStr(InputStream input) {
        return new String(read(input));
    }


    public static String mkdir(String dir) {
        try {
            File f = new File(dir);
            if (!f.exists()) f.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;
    }

    public static String readResourceFromCP(String s) {
        //System.out.println("resource: "+Thread.currentThread().getContextClassLoader().getResource("config.xml"));
        //System.out.println("resource stream: "+IO.class.getResourceAsStream(s));
        //System.out.println("classloader:"+IO.class.getClassLoader());
        //final InputStream stream = IO.class.getClassLoader().getResourceAsStream(s);
        final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(s);
        System.out.println("reading " + s + ":::" + stream);
        return new String(read(stream));
    }

    public static String encode(String label) {
        try {
            return URLEncoder.encode(label, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return label;
        }
    }

    public static String decode(String label) {
        try {
            return URLDecoder.decode(label, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return label;
        }
    }

    public static void printCP() {
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i].getFile());
        }
    }

    public static void download(String url, String dst) {
        MaskedInputStream mua = maskUserAgent(url);
        int contentLength = mua.contentlength;
        BigDecimal cl = new BigDecimal(contentLength);
        BigDecimal p = new BigDecimal(100);
        System.out.println("downloading " + url);
        System.out.println("destination:" + dst);
        InputStream in = mua.is;//new BufferedInputStream(mua.is);
        FileOutputStream output = fos(dst);
        int b;
        try {
            int counter = 0;
            while ((b = in.read()) != -1) {
                output.write((byte) b);
                if (counter++ % 65536 == 0) {
                    BigDecimal a = new BigDecimal(counter);
                    BigDecimal res = a.divide(cl, 1, RoundingMode.HALF_EVEN).multiply(p);
                    System.out.print(new StringBuilder("   downloading: ".concat(StringUtils.cat("(", res.toString(), " % ) ", valueOf(counter), " of ", valueOf(contentLength)))).append('\r'));
                }
                if (counter % 10485760 == 0)
                    output.flush();
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in, output);
        }
        System.out.println("download finished!");

    }

    public static void close(Socket s) {
        try {
            if (s != null)
                s.close();
        } catch (Exception e) {
            System.err.println("Could not close the socket!");
        }
    }


    public static void appendL(String filename, String line) {
        append(filename, StringUtils.cat(line, "\n"));
    }

    public static synchronized void append(String filename, String line) {

        try {
            FileWriter fstream = new FileWriter(filename, true);
            BufferedWriter out = new BufferedWriter(fstream);
            try {
                out.write(line);
            } finally {
                cloze(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File childOf(String parent, String filename, String extension) {
        return new File(parent, StringUtils.cat(filename, extension));
    }

    public static void deleteChildren(String f) {
        deleteChildren(new File(f));
    }

    public static void deleteChildren(File f) {
        File[] children = f.listFiles();
        if (children == null) return;
        for (File c : children)
            c.delete();
    }

    public static InputStream loadResource(String str) {
        return instance.getClass().getResourceAsStream(str);
    }

    public static interface LineProcessor {
        public void process(final String line);
    }

    public static File fileInCurrentDir(String name) {
        return new File(System.getProperty("user.dir"), name);
    }

    public static File fileInUserHomeDir(String name) {
        return new File(System.getProperty("user.home"), name);
    }

    public static BufferedWriter bufferedWriter(String file) {
        try {
            return new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static File dirsInUserHomeDir(String name) {
        File parent = new File(System.getProperty("user.home"));
        String[] split = name.split(File.separator);
        File file = parent;
        for (String s : split) {
            file = new File(file, s);
            file.mkdir();
        }

        return file;
    }

    public static String toUrl(String baseUrl, Collection<String> params) {
        StringBuilder sb = new StringBuilder(baseUrl);
        if (!baseUrl.endsWith("?"))
            sb.append("?");
        for (String param : params)
            sb.append(param).append("&");
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static ByteArrayInputStream bais(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    public static void storeProp(File outputFile, String k, String v) {
        Properties p = new Properties();
        p.put(k, v);
        try {
            p.store(new FileWriter(outputFile), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class MaskedInputStream {
        private final InputStream is;
        private final String charset;
        @SuppressWarnings("unused")
        private final String contenttype;
        private final int contentlength;

        public MaskedInputStream(InputStream is, String charset, String contenttype, int contentlength) {
            this.is = is;
            this.charset = charset;
            this.contenttype = contenttype;
            this.contentlength = contentlength;
        }

        public InputStream getInputStream() {
            return is;
        }

        public String getCharset() {
            return charset;
        }
    }
}
