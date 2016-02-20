
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public class I2CTest {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting:");

        final I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);

        MotorController motorController = new MotorController(bus);
        motorController.init();

        while (true) {
	    motorController.write(1024);
            Thread.sleep(5000);
            motorController.write(2048);
            Thread.sleep(5000);
            motorController.write(3064);
            Thread.sleep(5000);
        }

    }

    public static class MotorController {

        //private I2CDevice initDevice;
        private I2CDevice device;

	int REG_WRITEDAC = 0x40;
        int REG_WRITEDACEEPROM = 0x60;

        public MotorController(I2CBus bus) throws IOException {
            //initDevice = bus.getDevice(0x62);
            device = bus.getDevice(0x62);
        }

        public void init() {
            //try {
            //    initDevice.write(0xfe, (byte)0x04);
            //} catch (IOException ignore) {
            //    ignore.printStackTrace();
            //}
        }

        public void write(int voltage) throws IOException {
	    if (voltage > 4095) {
                voltage = 4095;
            }
            if (voltage < 0) {
                voltage = 0;
            }
            System.out.println("Setting voltage to: " + voltage);
            // Value needs to be left-shifted four bytes for the MCP4725
            byte[] bytes = {(byte)((voltage >> 4) & 0xFF),(byte)((voltage << 4) & 0xFF)};
            device.write(REG_WRITEDACEEPROM, bytes, 0, 2);
        }

        public int read() throws IOException {
            byte[] buf = new byte[256];
            int res = device.read(0, buf, 0, 6);
            return 0;
        }

        private int asInt(byte b) {
            int i = b;
            if (i < 0) { i = i + 256; }
            return i;
        }
    }

}

