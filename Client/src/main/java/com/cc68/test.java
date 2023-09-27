package com.cc68;

import java.io.*;

public class test {
    public static void main(String[] args) throws IOException {
        StreamTest streamTest = new StreamTest();
        String data = streamTest.getData();
        System.out.println(data);
        serialize(streamTest);
        System.out.println(streamTest.getData());
        streamTest.close();
    }

    public static void serialize(StreamTest streamTest) throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("a.sc"));
        os.writeObject(streamTest);
        os.flush();
        os.close();
    }
}
class StreamTest implements Serializable{
    private BufferedReader reader;
    public StreamTest() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("C:\\Users\\Administrator\\Downloads\\CommunicationSystem\\Client\\src\\main\\resources\\temp.txt"));
    }

    public String getData() throws IOException {
        String s = reader.readLine();
        return s;
    }

    public void close() throws IOException {
        reader.close();
    }
}
