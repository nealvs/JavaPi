
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
        // This SPI example is using the Pi4J SPI interface to communicate with
        // the SPI hardware interface connected to a MCP23S17 I/O Expander.
        // this source code was adapted from:
        // https://github.com/thomasmacpherson/piface/blob/master/python/piface/pfio.py
        // see this blog post for additional details on SPI and WiringPi
        // http://wiringpi.com/reference/spi-library/
        // see the link below for the data sheet on the MCP23S17 chip:
        // http://ww1.microchip.com/downloads/en/devicedoc/21952b.pdf
        
        System.out.println("<--Pi4J--> SPI test program using MCP3008 AtoD Chip");

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
        packet[0] = 0x01;  // INIT_CMD;  // address byte
        packet[1] = (byte) ((0x08 + channel) << 4);  // singleEnded + channel
        packet[2] = 0x00;
           
        //System.out.println("-----------------------------------------------");
        //System.out.println("[TX] " + bytesToHex(packet));
        byte[] result = spi.write(packet);
        //System.out.println("[RX] " + bytesToHex(result) + " -> " + bytesToBinary(result));
        System.out.println( ((result[1] & 0x03 ) << 8) | (result[2] & 0xff) );
        //System.out.println(bytesToInt(result));
        //int[] ints = bytesToInts(result);
        //for(int i : ints) {
        //    System.out.println(i);
        //}
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
