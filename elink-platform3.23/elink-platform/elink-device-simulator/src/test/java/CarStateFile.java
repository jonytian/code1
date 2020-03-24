import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

public class CarStateFile {

    public static void main(String[] args) {
        int total = 10;
        MessageBuilder mb0 = new MessageBuilder();
        for (int i = 0; i < 10; i++) {
            MessageBuilder mb = new MessageBuilder();
            mb.addDword(total);
            mb.addDword(i);
            mb.addDword(getRandom(1, 10000));
            mb.addDword(getRandom(1, 10000));
            mb.addDword(getRandom(80000000, 120000000));
            mb.addDword(getRandom(80000000, 120000000));
            mb.addWord(getRandom(0, 1000));
            mb.addWord(getRandom(0, 150));
            mb.addWord(getRandom(0, 359));
            mb.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(System.currentTimeMillis()), 6));
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 150));
            mb.addWord(getRandom(0, 150));
            mb.addByte(getRandom(0, 11));
            mb.addByte(getRandom(0, 100));
            mb.addByte(getRandom(0, 100));
            mb.addByte(getRandom(0, 1));
            mb.addWord(getRandom(0, 5000));
            mb.addWord(getRandom(0, 360) + 360);
            mb.addByte(getRandom(0, 2));
            mb.addWord(getRandom(0, 1000));
            byte[] bytes = mb.getBytes();
            int code = bytes[0];
            for (int j = 1; j < bytes.length; j++) {
                code += bytes[j];
            }
            mb.addByte(code);
            mb0.append(mb.getBytes());
        }
        mb0.getBytes();
    }

    private static int getRandom(int start, int end) {
        return (int) (start + Math.random() * end);
    }

}
