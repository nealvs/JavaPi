
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

import java.nio.ByteBuffer;
import java.io.IOException;

public class SpiMCP3008 {

    // SPI device
    public static SpiDevice spi = null;

    // SPI operations
    public static byte INIT_CMD = (byte) 0xD0; // 11010000

    
    public static void main(String args[]) throws InterruptedException, IOException {
        System.out.println("Pi4J - SPI test program using MCP3008 AtoD Chip");

        // create SPI object instance for SPI for communication
        spi = SpiFactory.getInstance(SpiChannel.CS0,
                                     SpiDevice.DEFAULT_SPI_SPEED, // default spi speed 1 MHz
                                     SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0

        // infinite loop
        while(true) {
            read(0);
	    read(1);
            Thread.sleep(100);
        }
    }

    public static void read(int channel) throws IOException {
        
        // send test ASCII message
        byte packet[] = new byte[3];
        packet[0] = 0x01;   // address byte
        packet[1] = (byte) ((0x08 + channel) << 4);  // singleEnded + channel
        packet[2] = 0x00;
           
        byte[] result = spi.write(packet);
        System.out.println( ((result[1] & 0x03 ) << 8) | (result[2] & 0xff) );
    }
   
    public static long bytesToInt(byte[] barray) {
        ByteBuffer bb = ByteBuffer.wrap(barray);
        return bb.getLong();
    } 
    
    public static String bytesToBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j];
            sb.append(Integer.toBinaryString(v)).append(" ");
        }
        return sb.toString();
    }    

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }    
}
