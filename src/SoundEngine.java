package src;

import javax.sound.sampled.*;

public class SoundEngine {
    private static byte[] buf;
    private static AudioFormat af = new AudioFormat(44100, 8, 1, true, false);
    static {
        int s = (int) (0.04 * 44100);
        buf = new byte[s];
        for (int i = 0; i < s; i++) {
            double a = 2.0 * Math.PI * i * 1100 / 44100.0;
            buf[i] = (byte) (Math.sin(a) * 110 * ((i < 100) ? i / 100.0 : (i > s - 100) ? (s - i) / 100.0 : 1.0));
        }
    }

    public static void playBeep() {
        new Thread(() -> {
            try {
                Clip c = AudioSystem.getClip();
                c.open(af, buf, 0, buf.length);
                c.start();
            } catch (Exception e) {
            }
        }).start();
    }
}