package done;


import java.io.*;

//code de https://www.enseignement.polytechnique.fr/informatique/profs/Philippe.Chassignet/PGM/pgm_java.html
//et légèrement modifié


class PixmapReader extends FileInputStream {

    private char c;

    public PixmapReader(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public boolean matchKey(String key) throws IOException {
        byte[] buf = new byte[key.length()];
        read(buf, 0, key.length());
        return key.compareTo(new String(buf)) == 0;
    }

    public void getChar() throws IOException {
        c = (char) read();
    }

    public int getInt() throws IOException {
        StringBuilder s = new StringBuilder();
        while ((c != '\n') && Character.isSpaceChar(c))
            getChar();
        while ((c != '\n') && !Character.isSpaceChar(c)) {
            s.append(c);
            getChar();
        }
        return Integer.parseInt(s.toString());
    }

    public void skipLine() throws IOException {
        while (c != '\n')
            getChar();
    }

    public void skipComment(char code) throws IOException {
        getChar();
        while (c == code) {
            skipLine();
            getChar();
        }
    }

    public byte[] loadData(int size) throws IOException {
        byte[] data = new byte[size];
        read(data, 0, size);
        return data;
    }

    public void close() {
        try {
            super.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }

}
